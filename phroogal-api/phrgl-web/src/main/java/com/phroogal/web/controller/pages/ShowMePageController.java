package com.phroogal.web.controller.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.User;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.web.utility.SlugGenerator;

@Controller
public class ShowMePageController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepo;
	
	@Autowired
	private FlagNotificationService flagNotificationService;
	
	@Value("${application.url}")
	private String applicationUrl;
	
	@RequestMapping(value = "/showme", method = RequestMethod.GET)
	public String userEmailPreferencesPage(Model model, WebRequest request, RedirectAttributes redirectAttributes)
	{
		long questionCount = questionIndexRepo.count();
		List<HtmlQuestionDisplay> flaggedQuestions = retrieveFlaggedQuestions();
		List<HtmlAnswerDisplay> flaggedAnswers = retrieveFlaggedAnswers();
		List<HtmlQuestionDisplay> unansweredQuestions = retrieveUnansweredQuestions();
		List<HtmlQuestionDisplay> recentQuestions = retrieveRecentQuestions();
		List<HtmlQuestionDisplay> mostViewedQuestions = retrieveMostViewedQuestions();
		List<HtmlAnswerDisplay> recentAnswers = retrieveRecentAnswers();
		
		model.addAttribute("users", getAllUsers());
		model.addAttribute("userCount", getUserCount());
		model.addAttribute("questionCount", questionCount);
		model.addAttribute("answerCount", answerService.count());
		model.addAttribute("answeredQuestions", questionCount - unansweredQuestions.size());
		
		model.addAttribute("flaggedQuestions", flaggedQuestions);
		model.addAttribute("flaggedAnswers", flaggedAnswers);
		
		model.addAttribute("mostViewedQuestions", mostViewedQuestions);
		model.addAttribute("unansweredQuestions", unansweredQuestions);
		model.addAttribute("unansweredQuestionsCount", unansweredQuestions.size());
		model.addAttribute("recentQuestions", recentQuestions);
		model.addAttribute("recentQuestionsCount", recentQuestions.size());
		model.addAttribute("recentAnswers", recentAnswers);
		
		return  "showme";		
	}

	private List<HtmlQuestionDisplay> retrieveFlaggedQuestions() {
		List<FlagNotificationRequest> flaggedQuestions = flagNotificationService.getByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, 0, 100);
		List<HtmlQuestionDisplay> flaggedQuestionsDisplayList = CollectionUtil.arrayList();
		for (FlagNotificationRequest request : flaggedQuestions) {
			Question question = questionService.findById(request.getRefId());
			HtmlQuestionDisplay unansweredQuestionDisplay = new HtmlQuestionDisplay();
			flaggedQuestionsDisplayList.add(unansweredQuestionDisplay.generate(question.getIndex()));
		}
		return flaggedQuestionsDisplayList;
	}
	
	private List<HtmlAnswerDisplay> retrieveFlaggedAnswers() {
		List<FlagNotificationRequest> flaggedAnswers = flagNotificationService.getByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.ANSWER, 0 ,10);
		List<HtmlAnswerDisplay> flaggedAnswersDisplayList = CollectionUtil.arrayList();
		for (FlagNotificationRequest request : flaggedAnswers) {
			HtmlAnswerDisplay answerDisplay = new HtmlAnswerDisplay();
			flaggedAnswersDisplayList.add(answerDisplay.generate(answerService.findById(request.getRefId())));
		}
		return flaggedAnswersDisplayList;
	}

	private List<HtmlQuestionDisplay> retrieveMostViewedQuestions() {
		List<QuestionIndex> mostViewedQuestions = questionService.getTrendingQuestions(0, Integer.MAX_VALUE, 90).getContent();
		List<HtmlQuestionDisplay> unansweredQuestionsDisplayList = CollectionUtil.arrayList();
		for (QuestionIndex questionIdx : mostViewedQuestions) {
			HtmlQuestionDisplay unansweredQuestionDisplay = new HtmlQuestionDisplay();
			unansweredQuestionsDisplayList.add(unansweredQuestionDisplay.generate(questionIdx));
		}
		return unansweredQuestionsDisplayList;
	}

	private List<HtmlQuestionDisplay> retrieveUnansweredQuestions() {
		List<QuestionIndex> unansweredQuestions = questionService.getRecentUnansweredQuestions(0, Integer.MAX_VALUE).getContent();
		List<HtmlQuestionDisplay> unansweredQuestionsDisplayList = CollectionUtil.arrayList();
		for (QuestionIndex questionIdx : unansweredQuestions) {
			HtmlQuestionDisplay unansweredQuestionDisplay = new HtmlQuestionDisplay();
			unansweredQuestionsDisplayList.add(unansweredQuestionDisplay.generate(questionIdx));
		}
		return unansweredQuestionsDisplayList;
	}
	
	private List<HtmlQuestionDisplay> retrieveRecentQuestions() {
		DateTime from = DateTime.now().minusDays(6);
		DateTime to = DateTime.now();
		List<QuestionIndex> recentQuestions = questionService.getQuestionsByDateRange(from, to, 0, 100).getContent();
		List<HtmlQuestionDisplay> recentQuestionsDisplayList = CollectionUtil.arrayList();
		for (QuestionIndex questionIdx : recentQuestions) {
			HtmlQuestionDisplay recentQuestionsDisplay = new HtmlQuestionDisplay();
			recentQuestionsDisplayList.add(recentQuestionsDisplay.generate(questionIdx));
		}
		return recentQuestionsDisplayList;
	}

	private List<HtmlAnswerDisplay> retrieveRecentAnswers() {
		List<HtmlAnswerDisplay> recentAnswersDisplayList = CollectionUtil.arrayList();
		for (Answer answer : answerService.getRecentAnswers(0, 100)) {
			HtmlAnswerDisplay answerDisplay = new HtmlAnswerDisplay();
			recentAnswersDisplayList.add(answerDisplay.generate(answer));
		}
		return recentAnswersDisplayList;
	}

	private List<User> getAllUsers() {
		List<User> users = userService.findAll();
		Collections.sort(users, new Comparator<User>(){
			@Override
			public int compare(User user1, User user2) {
				if (user1.getProfile().getLastname() != null && user2.getProfile().getLastname() != null) {
					return user1.getProfile().getLastname().compareTo(user2.getProfile().getLastname());					
				}
				return 0;
			}
		});
		return users;
	}

	private long getUserCount() {
		return userService.count();
	}
	
	private class HtmlQuestionDisplay extends Question {
		
		private static final long serialVersionUID = -3981130468530915793L;
		private String link;
		private String postedByUser;
		private String postedOn;
		
		public HtmlQuestionDisplay generate(QuestionIndex questionIdx) {
			HtmlQuestionDisplay me = new HtmlQuestionDisplay();
			me.setId(new ObjectId(questionIdx.getId()));
			me.setTitle(questionIdx.getTitle());
			me.setDocId(questionIdx.getDocId());
			me.setLink(generateQuestionLink(me.getDocId(), me.getTitle()));
			me.setTotalViewCount(questionIdx.getTotalViewCount());
			me.setPostedByUser(questionIdx.getPostedByUser());
			me.setPostedOn(questionIdx.getCreatedOn());
			return me;
		}

		@SuppressWarnings("unused")
		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		@SuppressWarnings("unused")
		public String getPostedOn() {
			return postedOn;
		}

		public void setPostedOn(DateTime postedOn) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss").withLocale(Locale.US);
			this.postedOn = formatter.print(postedOn);
		}

		@SuppressWarnings("unused")
		public String getPostedByUser() {
			return postedByUser;
		}

		public void setPostedByUser(String postedByUser) {
			this.postedByUser = postedByUser;
		}
	}
	
	private class HtmlAnswerDisplay extends Answer {
	
		private static final long serialVersionUID = 1995881000137668928L;
		
		private String questionLink;
		
		public HtmlAnswerDisplay generate(Answer answer) {
			HtmlAnswerDisplay me = new HtmlAnswerDisplay();
			me.setId(new ObjectId(answer.getId().toString()));
			me.setContent(answer.getContent());
			me.setPostBy(answer.getPostBy());
			me.setCreatedOn(answer.getCreatedOn());
			me.setQuestionTitle(answer.getQuestionTitle());
			me.setQuestionLink(generateQuestionLink(answer.getQuestionDocId(), answer.getQuestionTitle()));
			return me;
		}

		@SuppressWarnings("unused")
		public String getQuestionLink() {
			return questionLink;
		}

		public void setQuestionLink(String questionLink) {
			this.questionLink = questionLink;
		}
	}
	
	private String generateQuestionLink(long docId, String title) {
		StringBuffer sb = new StringBuffer();
		sb.append(applicationUrl);
		sb.append("question/");
		sb.append(docId);
		sb.append("/");
		sb.append(SlugGenerator.toSlug(title));
		return sb.toString();
	}
}
