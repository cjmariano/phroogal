package com.phroogal.web.utility;



import org.junit.Assert;
import org.junit.Test;

import com.phroogal.web.bean.QuestionIndexBean;

public class LinkGeneratorTest {

	@Test
		public void testGetQuestionLinkForQuestionIndex() throws Exception {
			QuestionIndexBean questionIndexBean = new QuestionIndexBean();
			questionIndexBean.setDocId(1000l);
			questionIndexBean.setTitle("What is mint.com?");
			
			Assert.assertEquals("question/1000/what-is-mint-com", LinkGenerator.getQuestionLink(questionIndexBean.getDocId() , questionIndexBean.getTitle()));
		}
}
