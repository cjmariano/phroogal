package com.phroogal.core.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.exception.InvalidQuestionDocumentIdException;
import com.phroogal.core.exception.PostDeletionExceededAllowableTimeException;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.repository.impl.EmptyPage;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.UserSocialContactService;
import com.phroogal.core.utility.SearchKeywordUtility;
import com.phroogal.core.utility.SolrUtility;
import com.phroogal.core.validator.Validator;

/**
 * Default implementation of the {@link Question} interface
 * @author Christopher Mariano
 *
 */
@Service
public class QuestionServiceImpl extends BaseService<Question, ObjectId, QuestionRepository> implements QuestionService{

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepository;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserSocialContactService userSocialContactService;
	
	@Autowired
	private FlagNotificationService flagNotificationService;
	
	@Autowired
	@Qualifier("solrUtility")
	private SolrUtility solrUtility;
	
	@Autowired
	@Qualifier(value="questionTrendingRule")
	private Rule questionTrendingRule; 
	
	@Autowired
	@Qualifier(value="deletePostValidator")
	private Validator<Post> deletePostValidator;
	
	@Value("${question.trends.withinDays}")
	private int trendingQuestionsWithinDays;
	
	@Value("${question.recent.withinDays}")
	private int recentQuestionsWithinDays;
	
	@Override
	protected QuestionRepository getRepository() {
		return questionRepository;
	}
	
	@Override
	protected void onBeforeDelete(Question question) {
		if ( ! deletePostValidator.isValid(question)) {
			throw new PostDeletionExceededAllowableTimeException();
		}
		super.onBeforeDelete(question);
	}
	
	@Override
	protected void onAfterDelete(Question question) {
		super.onAfterDelete(question);
		flagNotificationService.updateStatus(question, FlagNotificationStatusType.DELETED);
	}
	
	@Override
	public Question getByDocId(long docId) throws InvalidQuestionDocumentIdException {
		Question question = questionRepository.findByDocId(docId);
		if (question != null) {
			applyAnswerSortHierarchy(question);
			return question;	
		} 
		throw new InvalidQuestionDocumentIdException();
	}
	
	@Override
	public Page<Question> getByUserId(ObjectId userId, int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return questionRepository.findByPostById(userId, pageRequest);
	}

	@Override
	public Page<QuestionIndex> searchIndexedQuestionByTitle(String keyword, int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return questionIndexRepository.searchByTitle(SearchKeywordUtility.distill(keyword), pageRequest);
	}

	@Override
	public Page<QuestionIndex> searchSimilarIndexedQuestion(String keyword, int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "rank");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);		
		return questionIndexRepository.searchBySimilarTitle(keyword,pageRequest);
	}
	
	@Override
	public Page<QuestionIndex> searchSimilarIndexedQuestion(String keyword, long excludeDocId, int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "rank");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);		
		return questionIndexRepository.searchBySimilarTitleAndNotDocId(keyword,excludeDocId,pageRequest);
	}
	
	@Override
	public Page<QuestionIndex> getRecentQuestions(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		DateTime from = DateTime.now().minusDays(recentQuestionsWithinDays + 1);
		DateTime to = DateTime.now();
		Page<QuestionIndex> results = questionIndexRepository.findByCreatedOnBetween(from, to, pageRequest);
		if (results != null) {
			return results;	
		}
		return new EmptyPage<QuestionIndex>();
	}
	
	@Override
	public Page<QuestionIndex> getFlaggedQuestions(int pageAt, int pageSize) {
		List<String> flaggedQuestionIds = flagNotificationService.getRefIdAsStringByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, pageAt, pageSize);
		PageRequest pageRequest = generatePageRequest(pageAt, pageSize);
		if (CollectionUtils.isNotEmpty(flaggedQuestionIds)) {
			return questionIndexRepository.findAll(flaggedQuestionIds, pageRequest);
		}
		return new EmptyPage<QuestionIndex>();
	}
	
	@Override
	public Page<Question> getSocialQuestions(ObjectId userId, int pageAt, int pageSize) {
		UserSocialContact userSocialContact = userSocialContactService.getByUserId(userId);
		if (userSocialContact != null) {
			Pageable pageRequest = generatePageRequest(pageAt, pageSize);
			return questionRepository.findByPostById(userSocialContact.getSocialContactIds(), pageRequest);	
		}
		return new EmptyPage<Question>();
	}

	@Override
	public Page<QuestionIndex> getUnansweredQuestions(int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return questionIndexRepository.findByIsAnswered(false, pageRequest);
	}

	@Override
	public Page<QuestionIndex> getRecentUnansweredQuestions(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		return questionIndexRepository.findByIsAnswered(false, pageRequest);
	}
	
	@Override
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize, int numberOfDays) {
		Sort sort = new Sort(Sort.Direction.DESC, "rank");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		DateTime from = DateTime.now().minusDays(numberOfDays + 1);
		DateTime to = DateTime.now();
		return questionIndexRepository.findByCreatedOnBetweenAndIsAnswered(from, to, true, pageRequest);
	}
	
	@Override
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize) {
		return getTrendingQuestions(pageAt, pageSize, trendingQuestionsWithinDays);
	}
	
	@Override
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize, long excludeDocId) {
		Sort sort = new Sort(Sort.Direction.DESC, "rank");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		DateTime from = DateTime.now().minusDays(trendingQuestionsWithinDays + 1);
		DateTime to = DateTime.now();
		return questionIndexRepository.findByCreatedOnBetweenAndIsAnsweredAndNotDocId(from, to, true, excludeDocId, pageRequest);
	}
	
	@Override
	public Page<QuestionIndex> getMostViewedQuestions(int pageAt, int pageSize, int numberOfDays) {
		Sort sort = new Sort(Sort.Direction.DESC, "totalViewCount");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		DateTime from = DateTime.now().minusDays(numberOfDays + 1);
		DateTime to = DateTime.now();
		return questionIndexRepository.findByCreatedOnBetween(from, to, pageRequest);
	}

	@Override
	public Page<QuestionIndex> getQuestionsByDateRange(DateTime from, DateTime to, int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		return questionIndexRepository.findByCreatedOnBetween(from, to, pageRequest);
	}

	@Override
	public Page<QuestionIndex> searchQuestion(String keyword, int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return questionIndexRepository.searchByTitle(keyword, pageRequest);
	}

	@Override
	public Page<QuestionIndex> searchQuestionByTag(String tag, int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return questionIndexRepository.searchByTags(tag, pageRequest);
	}
	
	@Override
	public void incrementTotalViewsCount(long docId) {
		Question question = questionRepository.findByDocId(docId);
		question.incrementTotalViewsCount();
		this.saveOrUpdate(question);
	}
	
	@Override
	public FlagNotificationRequest flagQuestion(Question question) {
		String content =  question.getTitle().concat(question.getContent());
		return flagNotificationService.createRequest(question, content);
	}

	@Override
	public void recalculateTrendingRank(Question question) {
		questionTrendingRule.setFacts(Arrays.asList(question));
		questionTrendingRule.execute();
	}
	
	@Override
	public void reIndex() throws SolrServerException, IOException {
		solrUtility.resetQuestionIndex();		
	}
	
	private void applyAnswerSortHierarchy(Question question) {
		List<Answer> answers = question.getAnswers();
		if (answers != null && answers.size() > 0) {
			answerService.sortByAnswerHierarchyRules(answers);
			question.setTopAnswer(answers.get(0));	
		}
	}
}
