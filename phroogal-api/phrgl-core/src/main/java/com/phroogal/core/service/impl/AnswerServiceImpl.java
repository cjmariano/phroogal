package com.phroogal.core.service.impl;

import java.util.List;

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
import com.phroogal.core.repository.AnswerRepository;
import com.phroogal.core.repository.impl.EmptyPage;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.QuestionThreadPostService;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.validator.Validator;

/**
 * Default implementation of the {@link Answer} interface
 * @author Christopher Mariano
 *
 */
@Service
public class AnswerServiceImpl extends BaseService<Answer, ObjectId, AnswerRepository> implements AnswerService{

	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	@Qualifier(value="answerHierarchyRule")
	private Rule answerHierarchyRule; 
	
	@Autowired
	private FlagNotificationService flagNotificationService;
	
	@Autowired
	@Qualifier(value="deletePostValidator")
	private Validator<Post> deletePostValidator;
	
	@Autowired
	private QuestionThreadPostService questionThreadPostService;
	
	@Value("${answer.recent.withinDays}")
	private int recentAnswersWithinDays;
	
	@Override
	protected AnswerRepository getRepository() {
		return answerRepository;
	}
	
	@Override
	public void delete(Answer answer) {
		super.delete(answer);
		questionThreadPostService.refreshQuestionAnswerThread(answer);
	}
	
	@Override
	protected void onAfterDelete(Answer answer) {
		super.onAfterDelete(answer);
		flagNotificationService.updateStatus(answer, FlagNotificationStatusType.DELETED);
	}
	
	@Override
	public Page<Answer> getRecentAnswers(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		DateTime from = DateTime.now().minusDays(recentAnswersWithinDays + 1);
		DateTime to = DateTime.now();
		Page<Answer> results = answerRepository.findByCreatedOnBetween(from, to, pageRequest);
		if (results != null) {
			return results;	
		}
		return new EmptyPage<Answer>();
	}
	
	@Override
	public Page<Answer> getFlaggedAnswers(int pageAt, int pageSize) {
		List<ObjectId> flaggedAnswerIds = flagNotificationService.getRefIdByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.ANSWER, pageAt, pageSize);
		PageRequest pageRequest = generatePageRequest(pageAt, pageSize);
		return answerRepository.findAll(flaggedAnswerIds, pageRequest);
	}

	@Override
	public List<Answer> getByQuestionRef(ObjectId questionRef) {
		return answerRepository.findByQuestionRef(questionRef);
	}
	
	@Override
	public Page<Answer> getByUserId(ObjectId userId, int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return answerRepository.findByPostById(userId, pageRequest);
	}
	
	@Override
	public FlagNotificationRequest flagAnswer(Answer answer) {
		return flagNotificationService.createRequest(answer, answer.getContent());
	}

	@Override
	public void sortByAnswerHierarchyRules(List<Answer> answers) {
		if (answers!= null && !answers.isEmpty()) {
			answerHierarchyRule.setFacts(answers);
			answerHierarchyRule.execute();
			CollectionUtil.sortElementsByDescRank(answers);
		}
	}
}
