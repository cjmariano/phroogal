var BASE_URL = location.protocol + "//" + document.location.host + window.location.pathname;
var DELETE_QUESTION_ANSWER_LIMIT_TIME = 15;
var dropDownSelectFlag=false;
window.onload = function () {
    // default show add answer
    var answersCount = getURLParameter("answers");
    if (answersCount != null || answersCount == "0") {
        setTeEditorToAddNewAnswer();
    }
}
$(document).ready(function () {
	$("#main-container-content").scroll(function () {
		if ($(window).scrollTop() + $(window).height() > $(
                document).height() - 200) {
                // console.log("calling.....");
                if (processFlag == false && showTagsQuestion == true) {
                    GetRecords();
                }
         }
	}); 
});
$(document).ready(function () {	
	$('#search-control').typeahead([ { 			 
		name: 'question-answers',
		minLength: 2,
		items: 5,
		valueKey: 'title',
		limit: '10',
		remote: {
		   url: '../../api/posts/questions/query?keyword=%QUERY',
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response.content;
		   }
		},		 
		template: '<span><p>{{title}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;padding:2px 0px">Questions and Answers</h4>'
	},
	{
		name: 'tags',
		minLength: 2,
		items: 5,
		valueKey: 'name',
		limit: '10',
		remote: {
		   url: '../../api/tags/query?keyword=%QUERY',			   
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response;
		   }
		},		 
		template: '<span><p>{{name}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;padding:2px 0px">Tags</h4>'
	}
	]).on('typeahead:selected', function($e, data) {
		  dropDownSelectFlag=true;
		  if(data.title==undefined || data.title==null){
			  window.location.href =  getBaseUrlOfSite()+"question/"+"tag/"+replaceSpaceWithhyphen(data.name);				
		  }
		  else{
			  var title = replaceSpaceWithhyphen(data.title);
			  window.location.href = getBaseUrlOfSite()+"question/"+data.docId+"/"+removeLastLetter(title);				
		  }
	});	
	$('#search-control').keyup(function(e) {
	    if (e.which === 13) {
	    	if(dropDownSelectFlag==false){
	    		searchByKeywordOnMain('search_home');
	    	}  
	    }
	    var query = $(this).val();
		$(".search-more-link").attr('href', 'javascript:searchByKeyword("search_home")');
		$(".search-more-link").find("span").text(query);
	});
	$("#search-control").focus();
});
function searchByKeywordOnMain(path){
	var rowVal=$("#search-control").val();
	var keyWord = rowVal.replace(/[`%^\#",\{\}\[\]\\\/]/gi, '');
	if(keyWord=="" || keyWord==null || keyWord==undefined)
		return;
	if(keyWord.charAt(keyWord.length-1)=="?")
		keyWord=keyWord.substring(0,keyWord.length-1);
	window.location.href = getBaseUrlOfSite() + path+"?keyWord="+replaceSpaceWithhyphen(keyWord);			
}

$(document).ready(function () {
	$('#top_question_category_search').keyup(function (e) {
		if (e.which === 188) {
			var val = cammelCaseOfString($('#top_question_category_search').val());
			val=val.trim();
	        if (val=="," || val == "" || val == null || val == undefined) {
	        	$('#top_question_category_search').val("");
	            return;
	        }
			var found = checkTagsAvalable($('#top_question_category_search').val().substr(0,$('#top_question_category_search').val().length - 1));
            if (found >= 0) {
	            questionCategoryAddNew(allTagArray[found]);
	            $('#top_question_category_search').val("");
            }
            else {
            	$('#top_question_category_search').val($('#top_question_category_search').val().substr(0,$('#top_question_category_search').val().length - 1));
                var _id = $("#login_user_id").text();
                var val = cammelCaseOfString($('#top_question_category_search').val());
                var checkFlag = tagWithSpecialCharacters(val);
                if (checkFlag)
                {
                	$("#question_tags_alert_message_top").show();
                	$("#question_tags_alert_message_top").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
                    return;
                }
                if (categoryString.length < 5) {
                	documentScope.addUserTag(val, _id);
                }
                questionCategoryAddNew(val);
            }
        }
        if (e.which === 13) {
	        var val = cammelCaseOfString($('#top_question_category_search').val());
	        val=val.trim();
	        if (val == "" || val == null || val == undefined) {
	            return;
	        }
	        var _id = $("#login_user_id").text();
	        var found = checkTagsAvalable(val);
	        if (found < 0)
	        {
	            var checkFlag = tagWithSpecialCharacters(val);
	            if (checkFlag) {
	                $("#question_tags_alert_message_top").show();
	                $("#question_tags_alert_message_top").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
	                return;
	            }
	            if (categoryString.length < 5) {
	                documentScope.addUserTag(val, _id);
	            }
	            questionCategoryAddNew(val);
	        }
	        else {
	        	questionCategoryAddNew(allTagArray[found]);
	        }
            $('#top_question_category_search').val("");
        }
	});
            // showing tool tip
    $("#userAnonymousPostName").hover(
    		function () {
    			if (!$(this).find(".anonymous_tooltip_popup").length > 0)
    			{
    				var msg = "A private phroogie asking for<br/>your answers.";
    				$(this).append('<div class="anonymous_tooltip_popup" style="top:-20px;left:-125px;">' + msg + '</div>');
    				$(this).css("position", "relative");
    			}
    			$(this).find("div").show();
            }, 
            function () {
                $(".anonymous_tooltip_popup").hide();
            }
    );
    // showing tooltip on report flag
    $("#report_question_flag_link").hover(
    		function () {
    			if (!$(this).find(".report_flag_tooltip_popup").length > 0)
    			{
    				var msg = "Report as SPAM and<br/>inappropriate.";
    				$(this).append('<div class="report_flag_tooltip_popup" style="top:-50px;left:-112px;">' + msg + '</div>');
    				$(this).css("position", "relative");
    			}
    			$(this).find("div").show();
            }, 
            function () {
                $(".report_flag_tooltip_popup").hide();
            }
    );
    $('textarea').keyup(function (e) {
        if (currentLoginUserObj == null) {
            showLoginByQuestionRedirect();
            return;
        }
    });
});
var jqteStatus = false;
function setTeEditorToAddNewAnswer()
{
    if (currentLoginUserObj != null) {
        if (jqteStatus == false) {
            jqteStatus = true;
            $('.addNewAnswerTEeditor').jqte({
                "status": jqteStatus
            });
        }
        $("#addNewAnswerBYLoginUserButton").show();
        $(".jqte_editor").focus();
    }
    else {
        $("#addNewAnswerContent").blur();
        showLoginByQuestionRedirect();
    }
}
function getBaseUrlOfSite() {
    var index1 = BASE_URL.lastIndexOf('/');
    var BASE_URL1 = BASE_URL.substring(0, index1);
    var index2 = BASE_URL1.lastIndexOf('/');
    var BASE_URL2 = BASE_URL1.substring(0, index2);
    var index3 = BASE_URL2.lastIndexOf('/');
    var url = BASE_URL2.substring(0, index3 + 1);
    return url;
}
function getQuestionContentById(questionId) {
    var questionObj;
    $.ajax({
        async: false,
        url: '../../api/posts/question-' + questionId,
        success: function (data) {
            questionObj = data.response;
        }
    });
    return questionObj;
}
function getAnswerContentById(answerId) {
    var answerObj;
    $.ajax({
        async: false,
        url: '../../api/posts/questions/answer-' + answerId,
        success: function (data) {
            answerObj = data.response;
        }
    });
    return answerObj;
}
function getUserProfileById(id) {
    var userObj;
    $.ajax({
        async: false,
        url: '../../api/users/user-' + id,
        success: function (data) {
            userObj = data.response;
        }
    });
    return userObj;
}
function getQuestionAllDetailByQuestionId(id) {
    var questionObj;
    $.ajax({
        async: false,
        url: '../../api/posts/question-' + id,
        success: function (data) {
            questionObj = data;
        }
    });
    return questionObj;
}
function showQuestionByTag(tag) {
    tag = tag.replace("/", "+");
    tag = tag.replace(" ", "-");
    var url = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(tag.trim());
    window.open(url, '_blank');
}
function getQuestionIdFromUrl() {
    var parts = window.location.pathname.split("/");
    var id = parts[parts.length - 2];
    return id;
}

function getQuestionTitleFromUrl() {
    var parts = window.location.pathname.split("/");
    var title = parts[parts.length - 1];
    return title;
}
var pageIndex = 0;
var pageSize = 10;
var pageCount = 1;
var selectedTag = "";
var processFlag = false;
var showTagsQuestion = false;
var trendingPageSize = 5;
var trendingPageIndex = 0;
var relatedPageSize = 5;
var relatedPageIndex = 0;
var trendingTagsPageIndex = 0;
var trendingTagsPageSize = 5;

function GetRecords() {
    if (processFlag == true) {
        return;
    }
    if (pageIndex <= pageCount) {
        processFlag = true;
        $("#page_loader_image").show();
        setTimeout("sendRequestToPage()", 100);
    }
}

function sendRequestToPage() {
    $.ajax({
        async: false,
        url: '../../api/posts/questions?tag=' + selectedTag + '&topAnswer=true&pageAt=' + pageIndex + '&pageSize=' + pageSize,
        success: OnSuccess,
        failure: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    });
}

function OnSuccess(data) {
    pageIndex++;
    pageCount = pageCount + 1;
    if (data.response.lastPage == true) {
        pageCount = 0;
    }
    setQuestionsContentToDom(data.response)
    processFlag = false;
    $("#page_loader_image").hide();
}
function setQuestionsContentToDom(responseObj) {
	var obj = responseObj.content;
    $.each(obj, function(index, element) {
        var id = element.docId;
        var title = element.title;
        var content = element.content;
        var link = element.link;
        var totalAnswerCount = element.totalAnswerCount;
        var totalCommentCount = element.totalCommentCount;
        var border = "1px solid #E0E0E0";
        var urlRed = formQuestionDetailRedirectUrl(link);
        $('.search_question-bytag_content_questions').append('<div id="search_question-bytag_content_questions' + id + '" style="padding-top:10px;padding-bottom:10px;border-bottom:' + border + '"></div>');
        $('#search_question-bytag_content_questions' + id).append('<a href="' + urlRed + '"target="_self"  style="font-weight:bold;font-size: 16px;">' + title + '</a><br/>');

        if (content != null && content != "") {
            $("#dummyAnswerContentText").html(content);
            content = $("#dummyAnswerContentText").text();
            $('#search_question-bytag_content_questions' + id).append('<div style="font-size: 14px;color:black;">' + content + '</div>');
        }
        if (element.answered == true) {
            $('#search_question-bytag_content_questions' + id).append('<span style="font-size:10pt;margin-left:12px;"><a href="' + urlRed + '"target="_self" >Answers (' + totalAnswerCount + ')<a><a style="margin-left:5px;" href="' + urlRed + '"target="_self" >Comments (' + totalCommentCount + ')<a></span>');
        } else {
            urlRed = urlRed + "?answers=0";
            var localTitle = "";
            $('#search_question-bytag_content_questions' + id).append('<a href="' + urlRed + '"target="_self"  style="font-size:14px;margin-left:12px;">Be the first to answer</a><br/>');
        }
    });
}
var questionId = "";
var questionDocId = "";
var questionTitle = "";
var currentLoginUserObj = "";
angular.module('question-answer',['common-login'])
    .config(['$locationProvider',function ($locationProvider)
             {
            	$locationProvider.html5Mode(true);
             }
    ])
   .service('questionService',['$http', '$location', function($http, $location) {
            return {
                getQuestionDetailsById: function (_id) {
                    var promise = $http({method: 'GET',url: '../../api/posts/question-' + _id});
                    return promise;
                },
                addNewQuestion: function (_title, _content, _tags,isAnonymous) {
                    _title = _title.replace(/[`%|\;:"\{\}\[\]\\\/]/gi,'');
                    if (_title.charAt(_title.length - 2) == "?") {
                        _title = _title.substr(0, _title.length - 1);
                    }
                    if (_title.charAt(_title.length - 1) != "?") {
                        _title = _title + "?";
                    }
                    _title = capitaliseFirstLetter(_title);
                    var _id=$("#current_login_user_id").text();
                    var _postBy={
                    	"id":_id
                    };
                    var questionData = {
                        title: _title,
                        content: _content,
                        postBy:_postBy,
                        tags: _tags,
                        anonymous: isAnonymous
                    };
                    var promise = $http({headers:header_encoding,method: 'POST', url: '../../api/posts/question',data: questionData});
                    return promise;
                },
                updateQuestionTitle: function (id, _title) {
                    if (_title.charAt(_title.length - 1) != "?") {
                        _title = _title + "?";
                    }
                    if (_title.charAt(_title.length - 2) == "?") {
                        _title = _title.substr(0, _title.length - 1);
                    }
                    var questionData = [{
    					"operation" : "REPLACE",
                        "property": "title",
                        "value": _title
                    }];
                    var promise = $http({headers:header_encoding,method: 'PATCH',url: '../../api/posts/question-' + id,data: questionData, headers: {'Content-Type': 'application/json;charset=utf-8'} });
                    return promise;
                },
                updateQuestionCategories: function (_id, tag,oprationType) {
                	var promise="";
                	if (oprationType == "ADD") {
                		categoryString.push(tag);
                		$("#question_tags_alert_message_top").hide();
                		promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/question-' + questionDocId + '/tags/' + tag});
                	}
                	else {
                		categoryString.pop(tag);
                		$("#question_tags_alert_message_top").hide();
                		promise = $http({method: 'DELETE',url: '../../api/posts/question-' + questionDocId + '/tags/' + tag});
                    }
                   // $("#question_tags_alert_message_top").hide();
                	//var promise = $http({method: 'POST',url: '../../api/posts/question-' + questionDocId + '/updateQuestionTags?tag=' + tag + '&action=' + oprationType});
                    return promise;
                },
                updateQuestionContent: function (id, _content) {
                    var questionData =[{
    					"operation" : "REPLACE",
                        "property": "content",
                        value: _content
                    }];
                    var promise = $http({headers:header_encoding,method: 'PATCH',url: '../../api/posts/question-' + id,data: questionData, headers: {'Content-Type': 'application/json;charset=utf-8'} });
                    return promise;
                },
                updateQuestionAnonymousType: function (id, isAnonymous) {
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/question-' + id + '/updatePostType?anonymous=' + isAnonymous});
                    return promise;
                },
                updateAnswerVotes: function (answerId, _action) {
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/questions/answer-' + answerId + '/rating?action=' + _action});
                    return promise;
                },
                addAnswerToQuestionSave: function (_questionRef,_content, _questionDocId, _questionTitle) {
                	var _id=$("#current_login_user_id").text();
                    var _postBy={
                    	"id":_id
                    };
                	var answerData = {
                        questionRef: _questionRef,
                        content: _content,
                        postBy:_postBy,
                        questionTitle: _questionTitle,
                        questionDocId: _questionDocId
                    };
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/questions/answer',data: answerData});
                    return promise;
                },
                addCommentsToAnswerSave: function (_answerRef, _content) {
                    _content = capitaliseFirstLetter(_content);
                    var _id=$("#current_login_user_id").text();
                    var _postBy={
                    	"id":_id
                    };
                    var commentsData = {
                        answerRef: _answerRef,
                        content: _content,
                        postBy:_postBy
                    };
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/questions/answers/comment',data: commentsData});
                    return promise;
                },
                addReplyToCommentsSave: function (_commentRef, _content) {
                    _content = capitaliseFirstLetter(_content);
                    var _id=$("#current_login_user_id").text();
                    var _postBy={
                    	"id":_id
                    };
                    var replyData = {
                        commentRef: _commentRef,
                        content: _content,
                        postBy:_postBy
                    };
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/questions/answers/comments/reply',data: replyData});
                    return promise;
                },
                getAllTags: function () {
                    var promise = $http({headers:header_encoding,method: 'GET',url: '../../api/tags'});
                    return promise;
                },
                getAllQuestionByTagName: function (tagName) {
                    selectedTag = tagName;
                    var promise = $http({headers:header_encoding,method: 'GET',url: '../../api/posts/questions?tag=' + tagName + '&topAnswer=true&pageAt=0&pageSize=' + pageSize});
                    return promise;
                },
                getRelatedQuestionsByKeyword: function (keyword,docId) {
                	var excludeIdParam = "";
                	if (! isNaN(docId)) {
                		excludeIdParam = '&excludeDocId='+docId;
                	}
                    var promise = $http({headers:header_encoding,method: 'GET',url: '../../api/posts/questions/query?keyword=' + keyword + excludeIdParam +'&showSimilar=true&pageAt=' + relatedPageIndex + '&pageSize=' + relatedPageSize});
                    return promise;
                },
                updateAnswerContent: function (answerId, _content) {
                    var answerData = [{
    					"operation" : "REPLACE",
                        "property": "content",
                        "value": _content
                    }];
                    var promise = $http({headers:header_encoding,async: false,method: 'PATCH',url: '../../api/posts/questions/answer-' + answerId,data: answerData, headers: {'Content-Type': 'application/json;charset=utf-8'} });
                    return promise;

                },
                getTrendingQuestion: function (docId) {
                	var excludeIdParam = "";
                	if (! isNaN(docId)) {
                		excludeIdParam = '&excludeDocId='+docId;
                	}
                    var promise = $http({headers:header_encoding,method: 'GET', url: '../../api/posts/questions?showTrending=true' + excludeIdParam +'&pageAt=' + trendingPageIndex + '&pageSize=' + trendingPageSize});
                    return promise;
                },
                setViewCountIncr: function (id) {
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/posts/question-' + id + '/totalViewCount'});
                    return promise;
                },
                deleteQuestionById: function (questionDocId) {
                    var promise = $http({headers:header_encoding,method: 'DELETE',url: '../../api/posts/question-' + questionDocId});
                    return promise;
                },
                deleteAnswerByAnswerId: function (answerId) {
                    var promise = $http({headers:header_encoding,method: 'DELETE',url: '../../api/posts/questions/answer-' + answerId});
                    return promise;
                },
                getTrendingTags: function () {
                    var promise = $http({headers:header_encoding,method: 'GET',url: '../../api/tags?&showTrending=true&pageAt=' + trendingTagsPageIndex + '&pageSize=' + trendingTagsPageSize});
                    return promise;
                },
                addTagByUserId: function (tag, _id) {
                    var tagsData = {
                        "name": tag
                    };
                    var promise = $http({headers:header_encoding,method: 'POST',url: '../../api/tags/user-' + _id,data: tagsData});
                    return promise;
                },
                reportQuestionFlagById:function(questionDocId){
                	var promise = $http({headers:header_encoding,method: 'POST', url: '../../api/posts/question-'+questionDocId+'?action=flag'});
                    return promise;
                },
                reportAnswerFlagById:function(answerId){
                	var promise = $http({headers:header_encoding,method: 'POST', url: '../../api/posts/questions/answer-'+answerId+'?action=flag'});
                    return promise;
                }
            };
        }])
        .controller('questionCtrl', ['$scope','questionService',function ($scope, questionService){
        	// setting links values on search page
        	$scope.redirectToTrendingPageUrl = addhttp(getBaseUrlOfSite()+"trending");
        	$scope.redirectToDiscoverPageUrl = addhttp(getBaseUrlOfSite()+"discovertags");
        		
        	var userObj = null;
        	if ($("#login_user_id").text() != undefined && $("#login_user_id").text() != null && $("#login_user_id").text() != "") {
        	    userObj = getUserProfileById($("#login_user_id").text());
        	}
        	currentLoginUserObj = userObj;
        	var date = new Date();
        	var currentDate = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();

        	// start login User Profile Object Info
        	if (userObj != null) {
        	    if (userObj.profile.profileSmallPictureUrl == null || userObj.profile.profileSmallPictureUrl == "") {

        	        $scope.loginUserProfilePicture = "../../img/default-user.png";
        	    } else {
        	        if (userObj.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || userObj.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
        	            $scope.loginUserProfilePicture = userObj.profile.profileSmallPictureUrl;
        	        } else {
        	            $scope.loginUserProfilePicture = getBaseUrlOfSite() + userObj.profile.profileSmallPictureUrl;
        	        }
        	    }
        	} 
        	else {
        	    $scope.loginUserProfilePicture = "../../img/default-user.png";
        	}
        	if (userObj != null) {
        	    $scope.loginUserFirstName = (userObj.profile.firstname == null || userObj.profile.firstname == "") ? "" : userObj.profile.firstname;
        	    $scope.loginUserLastName = (userObj.profile.lastname == null || userObj.profile.lastname == "") ? "": userObj.profile.lastname;
        	    if($scope.loginUserLastName != ""){
        	    	$scope.loginUserFullName = $scope.loginUserFirstName + " " + $scope.loginUserLastName;
        	    }
        	    else{
        	    	$scope.loginUserFullName = $scope.loginUserFirstName;
        	    }
        	} else {
        	    $scope.loginUserFirstName = "";
        	    $scope.loginUserLastName = "";
        	    $scope.loginUserFullName = "";
        	}
        	if (userObj != null)
        	{
        	    if (userObj.profile.bio != null && userObj.profile.bio != "")
        	        $scope.loginUserBio = " | " + userObj.profile.bio;
        	    else
        	        $scope.loginUserBio = "";
        	}
        	else {
        	    $scope.loginUserBio = "";
        	}
        	if (userObj != null) {
        	    if (userObj.profile.location != null && userObj.profile.location.displayName != null)
        	        $scope.loginUserLocation = " | " + userObj.profile.location.displayName;
        	    else
        	        $scope.loginUserLocation = "";
        	    $scope.loginUserDate = " | " + currentDate;
        	} else {
        	    $scope.loginUserLocation = "";
        	}

        	// end set user info
        	$scope.login_user_id = "";
        	$scope.rightAddNewQuestionEnableEditor = false;
        	$scope.middleAddNewQuestionEnableEditor = false;
        	$scope.questionTitleUpdateEnableEditor = false;
        	$scope.questionContentUpdateEnableEditor = false;

        	$scope.questionTitle = "";
        	$scope.questionContent = "";
        	$scope.postByPicture = "";
        	$scope.postByName = "";
        	$scope.userProfileShow = "";
        	$scope.postTime = "";
        	$scope.questionPostLocation = "";
        	$scope.questionPostBio = "";

        	$scope.answerContent = "";
        	$scope.answerpostByPictureUrl = "";
        	$scope.answerPostUserFullName = "";
        	$scope.answerPostTime = "";
        	$scope.newAnswersComments = "";
        	$scope.newAnswerId = "";

        	$scope.addAnswerToQuestion = false;
        	$scope.addCommentsToAnswer = false;

        	$scope.categoryNames = [];

        	$scope.uniqueEmailError = false;

        	// SETTING SOCPE TO DOCUMENT SCOPE
        	documentScope = $scope;
        	$scope.resendMail = "";

        	$scope.setQuestionIdToDelete = "";
        	$scope.setAnswerIdToDelete = "";

        	$scope.nonLoginAnonymousUser = "";
        	$scope.questionTitleFromDB = "";
        	$scope.initFunctionCall = function () {
        		// ckeck to return or not
        		if($("#redirectToSearchPage").text()=="false"){
        			return;
        		}
        		questionDocId = getQuestionIdFromUrl();
                questionTitle = getQuestionTitleFromUrl();
                var questionTitleForRelatedSearch = "";
                var tag = "tag";
                if (questionDocId != null && questionDocId != "" && questionDocId.toLowerCase() == tag.toLowerCase()) {
                    $(".search_question-bytag_content").show();
                    $(".search_question-bytag_content_tagName").text("Tag: " + replacehyphenWithSpace(getQuestionTitleFromUrl().replace("%20", " ").replace("+", '/').replace("-", " ")));
                    var questionsSearchByTagPromise = questionService.getAllQuestionByTagName(replacehyphenWithSpace(getQuestionTitleFromUrl().replace("+", '/').replace("-", " ")));
                    questionsSearchByTagPromise.success(function(data) {
                        if (data.status == "SUCCESS") {
                            pageIndex = pageIndex + 1;
                            setQuestionsContentToDom(data.response);
                            showTagsQuestion = true;
                            $(".question-detail-right-content_related_question_box").hide();
                            // $(".question-detail-right-content_related_question_box_seprator").hide();
                            $("#trending_tags_box").show();
                        }
                    }).error(function(data) {
                        console.log("In error " + data);
                    }).then(function(response) {
                    });
                    this.showAllTrendingQuestions();
                    this.showAllTrendingTags();
                }
                else {
	                $scope.login_user_id = $("#login_user_id").text().trim();
	                var loginUserId = $("#login_user_id").text().trim();
	                $(".question-detail-home-middle-content").show();
	                $(".question-detail-right-content_related_question_box").show();
	                $(".question-detail-right-content_related_question_box_seprator").show();
	                var questionDetailPromise = questionService.getQuestionDetailsById(questionDocId);
	                $("#answer_to_current_user_date").text(" | " + currentDate);
	                questionDetailPromise.success(function(data) {
	                	if (data.status == "SUCCESS") {
	                        questionId = data.response.id;
	                        var loginUserEmail = $("#login_user_email").text().trim();
	                        var date = new Date(data.response.createdOn.replace(/-/g, '/').split('.')[0]);
	
	                        if (data.response.postBy.id == loginUserId) {
	                            if (parseInt((new Date() - date) / 60000) <= DELETE_QUESTION_ANSWER_LIMIT_TIME) {
	                                $("#delete_question_link").show();
	                            }
	                            else {
	                                $("#delete_question_link").hide();
	                            }
	                        }
	                        else{
	                        	if(currentLoginUserObj!=null){
		                        	if(currentLoginUserObj.grantedAuthorities[0]!=undefined && currentLoginUserObj.grantedAuthorities[0]=="ADMIN" ){
		                        		$("#delete_question_link").show();
		                        		//console.log("current login user is info is with role"+currentLoginUserObj.grantedAuthorities[0]);
		                        	}
		                        	else{
		                        		$("#report_question_flag_link").show();
		                        	}
	                        	}
	                        }
	                        if (data.response.content != null) {
	                            $scope.questionContent = data.response.content;
	                        }
	                        else {
	                            $scope.questionContent = "";
	                        }
                            if($scope.questionContent == null || $scope.questionContent == "") {
                                if (data.response.postBy.id == loginUserId) {
                                    $("#question-content_top_div").html("<i style='font-size:12px'>Add details for a more specific answer</i>");
                                }
                                else {
                                    $("#question-content_top_div").html("");
                                }
                            }
                            else {
                                $("#question-content_top_div").html($scope.questionContent.toString());
                            }
                            if (data.response.title.charAt(data.response.title.length - 1) != "?") {
                                $scope.questionTitle = data.response.title + "?";
                                questionTitleForRelatedSearch = data.response.title + "?";
                            }
                            else if (data.response.title.charAt(data.response.title.length - 2) == "?") {
                                $scope.questionTitle = data.response.title.substr(0, data.response.title.length - 1);
                                questionTitleForRelatedSearch = data.response.title.substr(0, data.response.title.length - 1);
                            }
                            else {
                                $scope.questionTitle = data.response.title;
                                questionTitleForRelatedSearch = data.response.title;
                            }
                            //setting value to db question title to scope obj
                            $scope.questionTitleFromDB = data.response.title;
                            
                            $scope.questionTitle = toUpperIWordReturnString($scope.questionTitle);
                            	if (data.response.anonymous == false) {
                            		if (data.response.postBy.profile.profileSmallPictureUrl == null || data.response.postBy.profile.profileSmallPictureUrl == "") {
                            			$scope.postByPicture = "../../img/default-user.png";
                                    }
                            		else {
                            			if (data.response.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || data.response.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
                            				$scope.postByPicture = data.response.postBy.profile.profileSmallPictureUrl;
                                        }
                            			else {
                            				$scope.postByPicture = "../../" + data.response.postBy.profile.profileSmallPictureUrl;
                                        }
                                    }
                                    // $scope.postByName=data.response.postBy.profile.firstname+"// "+// data.response.postBy.profile.lastname.charAt(0).toUpperCase();
                                    $scope.postByName = "";
                                    $scope.userProfileShow = data.response.postBy.id;
                                    if (data.response.postBy.id == loginUserId)
                                    {
                                        $("#userAnonymousPostName").hide();
                                        $("#userPublicPostName").hide();
                                        $("#loginUserIndicateSpanToggleQuestion").show();
                                        $scope.indicatedText = "Posted as public";
                                        $scope.linkIndicatedTitle = "Make it private";
                                        $scope.anonymousPostType = "true";
                                        $scope.questionRefId = questionDocId;
                                    }
                                }
                            	else {
                                    if (data.response.postBy.id == loginUserId) {
                                        // $scope.postByName=data.response.postBy.profile.firstname+" // "+data.response.postBy.profile.lastname;
                                        $scope.userProfileShow = data.response.postBy.id;
                                        $("#userAnonymousPostName").hide();
                                        $("#userPublicPostName").hide();
                                        $("#loginUserIndicateSpanToggleQuestion").show();
                                        $scope.indicatedText = "Posted as anonymous";
                                        $scope.linkIndicatedTitle = "Make it public";
                                        $scope.anonymousPostType = "false";
                                        $scope.questionRefId = questionDocId;
                                        if (data.response.postBy.profile.profileSmallPictureUrl == null || data.response.postBy.profile.profileSmallPictureUrl == "") {
                                            $scope.postByPicture = "../../img/default-user.png";
                                        }
                                        else if(data.response.postBy.profile.profileSmallPictureUrl=="http://profile.ak.fbcdn.net/hprofile-ak-frc3/t5/370969_661740228_1704370330_n.jpg"){
                                        }
                                        else {
                                            if (data.response.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || data.response.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) 
                                            {
                                            	$scope.postByPicture = data.response.postBy.profile.profileSmallPictureUrl;
                                            }
                                            else {
                                                $scope.postByPicture = "../../" + data.response.postBy.profile.profileSmallPictureUrl;
                                            }
                                        }
                                    }
                                    else {
                                        $("#userAnonymousPostName").show();
                                        $("#userPublicPostName").hide();
                                        $scope.postByName = "Anonymous";
                                        $scope.postByPicture = "../../img/phrgl-smily.png";
                                        $scope.userProfileShow = null;
                                    }
                                }
                                var amPm;
                                if (date.getHours() > 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                if (data.response.postBy.profile.location != null && data.response.postBy.profile.location.displayName != null) {
                                    // $scope.questionPostLocation = "| "+data.response.postBy.profile.location.displayName+";
                                	$scope.questionPostLocation = data.response.postBy.profile.location.displayName + " | ";
                                }
                                else
                                {
                                    $scope.questionPostLocation = "";
                                }
                                $scope.questionPostBio = "";
                                $scope.postTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
                                // +"
                                // "+date.getHours()+":"+date.getMinutes()+"
                                // "+amPm;
                                //alert("current user auth is"+currentLoginUserObj.grantedAuthorities[0]);
                                if(currentLoginUserObj!=null){
    		                        if (data.response.postBy.id == loginUserId || (currentLoginUserObj.grantedAuthorities[0]!=undefined && currentLoginUserObj.grantedAuthorities[0]=="ADMIN") ) {
	                                    $(".question_title_edit").show();
	                                    $(".question_content_edit").show();
	                                }
                                }
                                // change  title of page dynamically  
                                
                                //$(document).attr('title',data.response.title);
                                
                                // showing  all answers
                                if (data.response.answers != null && data.response.answers.length != 0)
                                {
                                	showAllAnswersByQuestion(data.response.answers);
                                }
                                else {
                                    $("#questionAnswerCount").text(0);
                                }
                                var tags = data.response.tags;
                                var tagsStr = "";
                                var tagsRemoveStr = "";
                                var editlinkStr = "";
                                if (tags != null) 
                                {
                                    for (i = 0; i < tags.length; i++) {
                                        var tagVar = tags[i];
                                        tagVar = tagVar.replace("/", "+");
                                        tagVar = tagVar.replace(" ", "-");
                                        var tagUrl = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(tagVar.trim());
                                        categoryString.push(tags[i]);
                                        tagsStr = tagsStr + '<a  class="question_tags_category" id="question_tags_' + i + '" target="_self" href="' + tagUrl + '">' + tags[i] + '</a>&nbsp;';
                                        tagsRemoveStr = tagsRemoveStr + '<span class="question_tags_category"  id="question_tags_editable' + i + '" ><a  href="">' + tags[i] + '</a><a class="topic_remove" href="" onclick="removeCategory(\'' + i + '\',\'' + questionId + '\',\'' + tags[i] + '\')">x</a></span>&nbsp;';
                                    }
                                }
                                if (data.response.postBy.id == loginUserId)
                                {
                                    editlinkStr = '<a class="question_content_edit edit"  href="#" onclick="javascript:questionCategoryUpdateEnableEditor()">Edit</a>';
                                    $(".question_tags_edit").append(tagsRemoveStr);
                                }
                                else{
    	                        	if(currentLoginUserObj!=null){
    		                        	if(currentLoginUserObj.grantedAuthorities[0]!=undefined && currentLoginUserObj.grantedAuthorities[0]=="ADMIN" ){
    		                        		editlinkStr = '<a class="question_content_edit edit"  href="#" onclick="javascript:questionCategoryUpdateEnableEditor()">Edit</a>';
    	                                    $(".question_tags_edit").append(tagsRemoveStr);
    		                        	}
    	                        	}
    	                        }
                                $(".question_tags").append(tagsStr);
                                $(".question_tags_edit_link").append(editlinkStr);
                            }
	                		addTargetAndHttpToQuestionDescription();	                    
	                	})
	                    .error(function (data) {
	                    	console.log("In error " + data);
	                    })
	                    .then(function (response) {
	                    });

                        // setting view count
                        var viewCount = questionService.setViewCountIncr(questionDocId);
                        viewCount.success(function (data) {
                        	if (data.status == "SUCCESS"){
                            }
                        }).error(
                            function (data) {
                                console.log("In error " + data);
                            }).then(function (response) {

                        });
                        // getting trending tags and questions                       
                        this.showAllRelatedQuestions();
                        this.showAllTrendingQuestions();
                       
                    }
                    // var tagsJson =
                    // getCookie("tagsArray");
	                var tagsJson = $.cookie("tagsArray");
	                var tagsArray = $.parseJSON(tagsJson);
	                if (tagsArray == null || tagsArray == undefined || tagsArray.length <= 0)
	                {
	                    var allTagsPromise = questionService.getAllTags();
	                    allTagsPromise.success(function(data) {
	                        if (data.status == "SUCCESS") {
	                            var categoryArray = new Array();
	                            for (i = 0; i < data.response.length; i++) {
	                                $scope.categoryNames.push(data.response[i].name);
	                            }
	                        }
	                        var tagsJson = JSON.stringify($scope.categoryNames);
	                        setCookie("tagsArray", tagsJson, 3);
	                        allTagsToFilter($scope.categoryNames);
	                    })
	                    .error(function(data) {
	                        console.log("In error " + data);
	                    })
	                    .then(function(response) {
	                    });
	                }
	                else {
	                    allTagsToFilter(tagsArray);
	                    console.log("in else part" + tagsArray[0]);
	                }
                }, 
                $scope.initLoginPopBox = function () {
                	showLoginByQuestionRedirect();
                }
                $scope.showAllRelatedQuestions = function () {
                	var allRelatedQuestionsPromise = questionService.getRelatedQuestionsByKeyword(replacehyphenWithSpace(questionTitle),questionDocId);
                	allRelatedQuestionsPromise.success(function(data){
                		if (data.status == "SUCCESS") {
                			if(data.response.totalElements>0){
                				setRelatedQuestions(data.response);
                				relatedPageIndex = relatedPageIndex + 1;
                			}
                			else{
                				$(".related_questions_content_links").append('<div style="color:black;font-weight:bold;">No related questions found</div>');
                			}
                		}
                	})
                	.error(function(data) {
                		console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                },
                $scope.showAllTrendingQuestions = function () {
                	var allTrendingQuestionsPromise = questionService.getTrendingQuestion(questionDocId);
                	allTrendingQuestionsPromise.success(function(data) {
                	    if (data.status == "SUCCESS") {
                	        setTrendingQuestions(data.response);
                	        trendingPageIndex = trendingPageIndex + 1;
                	    }
                	})
                	.error(function(data) {
                		console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                },
                $scope.showAllTrendingTags = function () {
                	var allTrendingQuestionsPromise = questionService.getTrendingTags();
                	allTrendingQuestionsPromise.success(function(data) {
                	    if (data.status == "SUCCESS") {
                	    	if(data.response.totalElements!=0){
                	    		setTrendingTags(data.response);
                	    		trendingTagsPageIndex = trendingTagsPageIndex + 1;
                	        }
                	    	else{
                	    		 $("#trending_tags_content_links").append('<div style="color:black;font-weight:bold;">No trending tags found</div>');
                	    	}
                	    }
                	})
                	.error(function(data) {
                		console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                },
                $scope.addNewRightQuestionButton = function () {
                	if (currentLoginUserObj == null) {
                	    showLoginByQuestionRedirect();
                	    return;
                	}
                	if (rightAllCategories.length == 0) {
                	    $("#question_tags_alert_message").text(TAGS_LOWER_LIMIT_MESSAGE);
                	    $("#question_tags_alert_message").show();
                	    return;
                	}
                	var isAnonymous = $('#postAsAnonymousRight').is(':checked');
                	var isAnonymousFlag = isAnonymous;

                	var content = $scope.rightNewQuestionDescription;

                	if (content != "" && content != null) {
                	    content = capitaliseFirstLetter(content);
                	}
                	setLoaderImageOnButton('right_add_new_question_button');
                	var title = $scope.rightNewQuestionTitle;
                	var questionObjPromise = questionService.addNewQuestion($scope.rightNewQuestionTitle, content, rightAllCategories, isAnonymousFlag);
                	questionObjPromise.success(function(data) {
                	    if (data.status == "SUCCESS") {
                	        $scope.rightNewQuestionTitle = "";
                	        $scope.rightNewQuestionDescription = "";
                	        rightAllCategories = [];
                	        $("#modal-footer-question").hide();
                	        $("#modal-footer-answer").hide();
                	        $("#messageModelButton_div").hide()
                	        $("#modal-footer-questionAdd").show();
                	        $("#messageModal").css({"margin-left":"-168px","max-width": "325px"});
                	        $("#error_message_text").css("width","");
                	        $("#modal-title-message").html('MESSAGE');
                	        showErrorMessage('Thanks phroogie! Your question is now sent to the community. In the meantime search related questions, topics and external resources.');
                	        var title = replaceSpaceWithhyphen(data.response.title);
                	        newAddedQuestionLink = "question/" + data.response.docId + "/" + removeLastLetter(title);
                	    } else {
                	        removeLoaderImageOnButton('right_add_new_question_button');
                	    }
                	})
                	.error(function(data) {
                		console.log("In error " + data);
                		removeLoaderImageOnButton('right_add_new_question_button');
                	})
                	.then(function(response) {
                	});                
                },
                $scope.addUserTag = function (tagVal,user_id) {
                    var userObjPromise = questionService.addTagByUserId(tagVal, user_id);
                    userObjPromise.success(function(data) {
                    	
                    })
                    .error(function(data) {
                    	console.log("In error " + data);
                    })
                    .then(function(response) {
                    	
                    });
                },
                $scope.questionTitleUpdateEnableEditor = function () {
                    $scope.questionTitleUpdateEnableEditor = true;
                },
                $scope.questionTitleUpdatenDisableEditor = function () {
                    $scope.questionTitleUpdateEnableEditor = false;
                },
                $scope.questionTitleUpdate = function () {
                	if ($scope.questionTitle != "" && $scope.questionTitle != null)
                	{
                	    $scope.questionTitle = toUpperIWordReturnString($scope.questionTitle);
                	    var questionObjPromise = questionService.updateQuestionTitle(questionId, $scope.questionTitle);
                	    questionObjPromise.success(function(data) {
                	        $scope.questionTitle = data.response.title;
                	        $(".question-title").show();
                	        $(".update_question_title").hide();
                	    })
                	    .error(function(data) {
                	        console.log("In error " + data);
                	    })
                	    .then(function(response) {
                	    });
                	}
                	else {
                	    $scope.questionTitle = lastShowQuestionTitle;
                	    $(".question-title").show();
                	    $(".update_question_title").hide();
                	}
                	$scope.questionTitleUpdatenDisableEditor();
                },
                $scope.questionContentUpdateEnableEditor = function () {
                    $scope.questionContentUpdateEnableEditor = true;
                },
                $scope.questionContentUpdatenDisableEditor = function () {
                    $scope.questionContentUpdateEnableEditor = false;
                },
                $scope.questionContentUpdate = function () {
                	var content = $("#questionContentEditAreaDiv .jqte_editor")[0].innerHTML;
                	if (content == "" || content == undefined)
                	    return;
                	if (content.trim().length - content.trim().lastIndexOf("<br>") == 4)
                	{
                		content = content.substr(0, content.trim().lastIndexOf("<br>"))
                	}
                	$("#dummyAnswerContentText").html(content);
                	var element = $("#dummyAnswerContentText");
//                	var setFirstCapital = false;
                	if (element[0].childNodes[0].nodeType == 3 && element[0].childNodes[0].nodeValue != "") {
                	    element[0].childNodes[0].nodeValue = capitaliseFirstLetter(element[0].childNodes[0].nodeValue);
//                	    setFirstCapital = true;
                	}
                	elements = element.find("*");
                	for (i = 0; i < elements.length; i++)
                	{
                	    elements[i].style.fontSize = "14px";
                	    if (elements[i].tagName == "a" || elements[i].tagName == "A") 
                	    {
                	    } 
                	    else {
                	        elements[i].style.color = "black";
                	    }
                	    elements[i].style.fontFamily = "calibri";
                	}
                	var questionObjPromise = questionService.updateQuestionContent(questionId, $("#dummyAnswerContentText").html());
                	questionObjPromise.success(function(data) {
                	    $scope.questionContent = data.response.content;
                	    $("#question-content_top_div").html(data.response.content.toString());
                	    $(".question-post-by-content_top").show();
                	    $(".update_question_content").hide();
                	    $("#questionContentEditAreaDiv .jqte_editor")[0].innerHTML = $("#dummyAnswerContentText").html();
                	    addTargetAndHttpToQuestionDescription();
                	})
                	.error(function(data) {
                	    console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                	$scope.questionContentUpdatenDisableEditor();
                },
                $scope.updateQuestionAnonymousType = function (id, postType) {
                	var isAnonymous;
                	if (postType == "true")
                	{
                		isAnonymous = true;
                	}
                	else {
                	    isAnonymous = false;
                	}
                	var questionObjPromise = questionService.updateQuestionAnonymousType(id, isAnonymous);
                	questionObjPromise.success(function(data) {
                	    if (postType == "true") {
                	        $scope.indicatedText = "Posted as anonymous";
                	        $scope.linkIndicatedTitle = "Make it public";
                	        $scope.anonymousPostType = "false";
                	    }
                	    else
                	    {
                	        $scope.indicatedText = "Posted as public";
                	        $scope.linkIndicatedTitle = "Make it private";
                	        $scope.anonymousPostType = "true";
                	    }
                	})
                	.error(function(data) {
                	    console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                },
                $scope.addAnswerToQuestionEnableEditor = function () {
                    $scope.addAnswerToQuestion = true;
                },
                $scope.addAnswerToQuestionDisableEditor = function () {
                    $scope.addAnswerToQuestion = false;
                },
                $scope.addAnswerToQuestionSave = function (content) {
                	if (currentLoginUserObj == null) {
                	    showLoginByQuestionRedirect();
                	    return;
                	}
                	var answedrOblPromise = questionService.addAnswerToQuestionSave(questionId, content, questionDocId, $scope.questionTitleFromDB)
                	    answedrOblPromise.success(function(data) {
                	    if (data.status == "SUCCESS") {
                	        var answerObj = data.response;
                	        var answerPostUserFullName = "";
                	        if(answerObj.postBy.profile.firstname != null && answerObj.postBy.profile.firstname != ""){
                	        	answerPostUserFullName += answerObj.postBy.profile.firstname; 
                	        }
                	        if(answerObj.postBy.profile.lastname != null && answerObj.postBy.profile.lastname != ""){
                	        	answerPostUserFullName += " " + answerObj.postBy.profile.lastname; 
                	        }
                	        //var answerPostUserFullName = answerObj.postBy.profile.firstname + " " + answerObj.postBy.profile.lastname;
                	        var date = new Date(answerObj.createdOn.replace(/-/g, '/').split('.')[0]);
                	        var amPm;
                	        if (date.getHours() > 12) {
                	            amPm = "PM";
                	        } else {
                	            amPm = "AM";
                	        }
                	        var answerPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
                	        // +"
                	        // "+date.getHours()+":"+date.getMinutes()+"
                	        // "+amPm;
                	        var answerpostByPicture = "";
                	        var answerpostByLocation = "";
                	        var answerContent = answerObj.content;
                	        var answerCommentCount = 0;
                	        var profile_user_id = answerObj.postBy.id;
                	        if (answerObj.postBy.profile.location == null || answerObj.postBy.profile.location == "" || answerObj.postBy.profile.location.displayName == null) {
                	            answerpostByLocation = "";
                	        } else {
                	            answerpostByLocation = "| " + answerObj.postBy.profile.location.displayName + " ";
                	        }
                	        var answerpostByBio = "";
                	        if (answerObj.postBy.profile.bio == null || answerObj.postBy.profile.bio == "") {
                	            answerpostByBio = "";
                	        } else {
                	            answerpostByBio = "| " + answerObj.postBy.profile.bio + " ";
                	        }
                	        if (answerObj.postBy.profile.profileSmallPictureUrl == null || answerObj.postBy.profile.profileSmallPictureUrl == "") {
                	            answerpostByPicture = "../../img/default-user.png";
                	        } else {
                	            if (answerObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || answerObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
                	                answerpostByPicture = answerObj.postBy.profile.profileSmallPictureUrl;
                	            } else {
                	                answerpostByPicture = "../../" + answerObj.postBy.profile.profileSmallPictureUrl;
                	            }
                	        }
                	        var answersCount = $(".old-answer-descriptions").children().length;
                	        var id = answersCount;
                	        var voteTotal = answerObj.votes.total;
                	        var downVoteDisplay;
                	        if (voteTotal == "0") {
                	            downVoteDisplay = "block";
                	        } else {
                	            downVoteDisplay = "block";
                	        }
                	        var upLinkOpacity;
                	        var downLinkOpacity;
                	        var upLinkCursor;
                	        var downLinkCursor;
                	        var currentUserId = $("#login_user_id").text();
                	        if (answerObj.votes.userUpvotes.indexOf(currentUserId) < 0) {
                	            upLinkOpacity = 1;
                	            downLinkOpacity = 0.1;
                	            upLinkCursor = "pointer";
                	            downLinkCursor = "default";
                	        } else {
                	            upLinkOpacity = 0.1;
                	            downLinkOpacity = 1;
                	            upLinkCursor = "default";
                	            downLinkCursor = "pointer";
                	        }
                	        if (answerObj.postBy.primarySocialNetworkConnection) {
                	            profilePictureRadius = "50%";
                	        } else {
                	            profilePictureRadius = "0%";
                	        }
                	        var deleteLink = '<div id="delete_answer_link" style="float:right;margin-left: 15px;"><a href="#" onclick="deleteAnswerFromDom(\'' + answerObj.id + '\')">Delete</a></div>';
                	        // answerContent=openLinkToNewTab(answerContent);
                	    	var answerStr='<div style="width:100%;clear:both;margin-bottom:15px;margin-top: 15px;font-family:calibry;" id="'+answerObj.id+'">'+	
        					deleteLink+'<div class="postby_and_post_time" style="font-size:10px;">'+
        					
        					
        					
//        					'<span ><img class="answer-post-by-picture-img" src="'+answerpostByPicture+'"  style="width:25px;height:25px;margin-right:10px;border-radius:'+profilePictureRadius+';" /><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +answerPostUserFullName + '&nbsp;</a>'+answerpostByBio+answerpostByLocation+
//        		   			'|&nbsp;'+answerPostTime+'</span></div>'+
        		   			
        		   			
        		   			'<table><tr><td><img class="answer-post-by-picture-img" src="'+answerpostByPicture+'"  style="width:30px;height:30px;margin-right:10px;border-radius:'+profilePictureRadius+';"/></td><td><span ><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +answerPostUserFullName + '&nbsp;</a>'+answerpostByBio+answerpostByLocation+
        					'|&nbsp;'+answerPostTime+'</span></td></tr></table>'+
        		   			
        		   			
        		   			'<table><tr><td valign="top">'+	
        		   			'<div class="answer-post-by-picture-content_and_vote_up_dow">'+
        		   			'<div style="float:left;width:35px;height:55px;margin-top:15px;">'+
        		   			'<div class="vote_up_down_content">'+
        		   			'<div class="vote_up_content">'+
        		   			'<div class="vote_up_link_background" style="opacity:'+upLinkOpacity+';cursor:'+upLinkCursor+';"  id="vote_up_link'+answerObj.id+'" onclick="upVoteToAnswerById(\''+answerObj.id+'\')"></div><div class="vote_up_link_text" id="vote_up_link_text_'+answerObj.id+'">'+voteTotal+'</div></div>'+
        		   			'<a style="display:'+downVoteDisplay+';opacity:'+downLinkOpacity+';cursor:'+downLinkCursor+';" id="vote_down_link'+answerObj.id+'" href="" onclick="javascript:downVoteToAnswerById(\''+answerObj.id+'\')" class="vote_down_content vote_down_link"></a></div>'+
        		   			'</div></td><td>'+
        		   			'<div class="answer-post-by-content" id="question_answer'+id +'" style="float:left; width:565px;word-wrap: break-word;color:black;font-size:14px;">'+answerContent+'</div><div class="question_answer_edit_div" id="question_answer_edit_div'+id+'"><a class="edit" style="width:22px;" href="#" onclick="javascript:showEditableAnswerContentById(\'' +id+'\',\''+answerObj.id+'\')">Edit</a></div>'+
        		   			'<div id="question_answer_textareadiv'+id+'" style="display:none;width:560px;">'+
        		   			'<textarea class="editExistingAnswerTextarea" id="question_answer_textarea'+id+'" value='+answerContent+' ></textarea>'+
        		   			'<div style="margin-bottom:10px;margin-top:5px;float:right;">'+
        	   			    '<a href="#" onclick="javascript:disableUpdateAnswerUpdate(\''+id+'\')"  style="margin-right:5px;">Close</a>'+
        	   				'<button type="submit" class="btn btn-primary" onclick="updateAnswerContent(\''+id+'\',\''+answerObj.id+'\')">Update</button></div>'+	        				
        	   				'</div>'+
        	   				'</div></td></tr></table>'+
        	   				'<div style="margin-left:35px;">'+
        		   			'<span class="oldAnswersCommentsshow" style="clear:both;font-size:10px;">'+
        					'<img src="../../img/comment-icon.png" style="width:8px;height:8px"></img>'+
        					'<a id="answerCommentsCount'+answersCount+'" style="margin-left:6px;" href="javascript:commentsBoxShow(\'' +answersCount+'\',\''+answerObj.id+'\')">Comments&nbsp;('+answerCommentCount+')</a>'+	 			   			
         			   		'</span></div>'+ 			   		
        		    		'<div class="tip" id="answers-comments-content'+answersCount+'" style="width:562px;margin-left:35px;margin-top:5px;border:1px solid #E0E0E0;display:none;position:relative;"></div>'+
        		    		'<div class="add-new-comment-to-answer'+answersCount+'" style="display:none;margin-left:35px;margin-top:10px;">'+
         			   		'<div style="float:left;width:35px;height:35px;">'+
         			   		'<img class="answer-post-by-picture-img" src="../../img/default-user.png" style="width:30px;height:30px" />'+
         			   		'</div>'+
        		    		'<textarea  id="addNewComment_to_Answer_'+answersCount+'" placeholder="Enter Comment" style="width:312px;margin-right:5px;height:20px"/>'+
        		    		'<button style="margin-top:-10px;width:150px;" id="button_addNewComment_to_Answer_'+answersCount+'"  type="button" class="btn btn-txt" onclick="addCommentsToAnswerById(\'' +answersCount+'\',\''+answerObj.id+'\',\''+answerCommentCount+'\'\)">Add Comments</button>'+
        		    		'<a  style="margin-left:5px;" href="javascript:closeCommentsAddNewCommentsBox(\'' +answersCount+'\',\''+answerObj.id+'\')">Cancel</a>'+
        		    		'</div>'+
        		    		'</div><div class="seprator" id="seprator'+answerObj.id+'" style="background-color: #E0E0E0;height:2px;width:100%;clear:both;margin-top:5px;"></div>';
        					//$('#question_answer_textarea'+id).jqte();
        					$('.old-answer-descriptions').append(answerStr);
        					expandTextarea("addNewComment_to_Answer_"+answersCount);
        					var oldAnswerCount = $("#questionAnswerCount").text();
        					$("#questionAnswerCount").text(parseInt(oldAnswerCount)+1);
        					
        					//open links in target
        					addTargetAndHttpToAnswersLink();
        	            }
                	    // addAnswerToQuestionByIdSave();
                        removeDisabledPropertyFromButton("addNewAnswerToQuestionButton ");
                    })
                    .error(function (data) {
                    	$scope.addAnotherEmailDisableEditor();
                    }).then(function (response) {

                    });
                },
                $scope.addCommentsToAnswerEnableEditor = function () {
                    $scope.addCommentsToAnswer = true;
                },
                $scope.addCommentsToAnswerDisableEditor = function () {
                    $scope.addCommentsToAnswer = false;
                },
                $scope.addCommentsByAnswerId = function (index, answerId, content,commentsCount) {
                    if (currentLoginUserObj == null) {
                        showLoginByQuestionRedirect();
                        return;
                    }
                    var commentsOblPromise = questionService.addCommentsToAnswerSave(answerId, content)
                    commentsOblPromise.success(function(data) {
	                    if (data.status == "SUCCESS") {
	                        var commentCount = $("#answers-comments-content" + index).children().length;
	                        var newcommentCount = parseInt(commentCount) + 1;
	                        $("#answerCommentsCount" + index).text("Comments (" + newcommentCount + ")");
	                        var commentsObj = data.response;
	                        var commentPostUserFullName = "";
	                        if(commentsObj.postBy.profile.firstname != null && commentsObj.postBy.profile.firstname != ""){
	                           commentPostUserFullName += commentsObj.postBy.profile.firstname; 
	                        }
	                        if(commentsObj.postBy.profile.lastname != null && commentsObj.postBy.profile.lastname != ""){
		                           commentPostUserFullName += " " + commentsObj.postBy.profile.lastname; 
		                    }
	                        //var commentPostUserFullName = commentsObj.postBy.profile.firstname + " " + commentsObj.postBy.profile.lastname;
	                        //(userObj.profile.lastname == null || userObj.profile.lastname == "") ? "": userObj.profile.lastname;
	                        
	                        var date = new Date(commentsObj.createdOn.replace(/-/g, '/').split('.')[0]);
	                        var amPm;
	                        if (date.getHours() > 12) {
	                            amPm = "PM";
	                        } else {
	                            amPm = "AM";
	                        }
	                        var commentspostByLocation;
	                        if (commentsObj.postBy.profile.location == null || commentsObj.postBy.profile.location == "" || commentsObj.postBy.profile.location.displayName == null) {
	                            commentspostByLocation = "";
	                        } else {
	                            commentspostByLocation = "| " + commentsObj.postBy.profile.location.displayName + " ";
	                        }
	                        var commentPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
	                        // +"
	                        // "+date.getHours()+":"+date.getMinutes()+"
	                        // "+amPm;
	                        var commentpostByPicture = "";
	                        var commentContent = commentsObj.content;
	                        var commentsReplyCount = 0;
	                        if (commentsObj.postBy.profile.profileSmallPictureUrl == null || commentsObj.postBy.profile.profileSmallPictureUrl == "") {
	                            commentpostByPicture = "../../img/default-user.png";
	                        } else {
	                            if (commentsObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || commentsObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
	                                commentpostByPicture = commentsObj.postBy.profile.profileSmallPictureUrl;
	                            } else {
	                                commentpostByPicture = "../../" + commentsObj.postBy.profile.profileSmallPictureUrl;
	                            }
	                        }
	                        //commentContent=openLinkToNewTab(commentContent);
							var profile_user_id =commentsObj.postBy.id;
							var commentsStr='<div  id="'+commentsObj.id+'" style="padding:10px;min-height:35px;width:500px;" >'+
				   			'<div class="comments-by-and-post-time"  style="font-size:10px;"><span ><img class="comments-post-by-picture-img" src="'+commentpostByPicture+'"  style="width:25px;height:25px;margin-right:10px;"/><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +commentPostUserFullName + '&nbsp;</a>'+commentspostByLocation+
				   			'|&nbsp;'+commentPostTime+'</span></div>'+
				   			'<div class="comments-post-by-content" style="width:460px;word-wrap: break-word;margin-left:35px;color:black;font-size:14px;">'+commentContent+'</div>'+
			   				'<div style="clear:both;margin-left:35px;"><span class="oldCommentsReplyshow">'+
							'<img src="../../img/reply-icon.png" style="width:8px;height:8px" ></img>'+
							'<a id="commentsReplyCount'+index+''+commentCount+'" style="margin-left:6px;font-size:10px;" href="javascript:replyToCommentsBoxShow(\'' +index+'\',\'' +commentCount+'\',\''+commentsObj.id+'\')">Reply&nbsp;('+commentsReplyCount+')</a>'+	 			   			
							'</span>'+
			   				'</div>'+		   				
				    		'<div class="tip" id="comments-reply-content'+index+''+commentCount+'" style="width:460px;margin-left:35px;margin-top:5px;border:1px solid #E0E0E0;display:none;position:relative;"></div>'+
				    		'<div class="add-new-reply-to-comments'+index+''+commentCount+'" style="display:none;margin-top:10px;">'+
			   				'<div style="float:left;width:35px;height:35px;margin-left:35px;">'+
		 			   		'<img class="answer-post-by-picture-img" src="../../img/default-user.png" style="width:30px;height:30px" />'+
		 			   		'</div>'+
				    		'<textarea id="add-new-reply-to-comments_textarea'+index+''+commentCount+'" placeholder="Enter Reply" style="width:274px;height:20px;margin-right:5px;"/>'+
				    		'<button style="margin-top:-10px;width:90px;" type="button"  id="button_add-new-reply-to-comments_textarea'+index+''+commentCount+'" class="btn btn-txt" onclick="addReplyByCommentsId(\'' +index+'\',\'' +commentCount+'\',\''+commentsObj.id+'\',\''+commentsReplyCount+'\')">Reply</button>'+
				    		'<a style="margin-left:5px;" href="javascript:replyToCommentsBoxClose(\'' +index+'\',\'' +commentCount+'\',\''+commentsObj.id+'\')">Cancel</a>'+
				    		'</div>'+
				    		'</div>';
				   			$("#answers-comments-content"+index).append(commentsStr);
	                        expandTextarea("add-new-reply-to-comments_textarea" + index + "" + commentCount);
	                        removeCommentsReplyButtonDisabled("button_addNewComment_to_Answer_" + index);
	                    }
	                })
	                .error(function(data) {
	                    $scope.addAnotherEmailDisableEditor();
	                })
	                .then(function(response) {
	                	
	                });
                },
                $scope.addReplyByCommentsId = function (rowIndex,colIndex,commentsId,content,commentsReplyCount) {
                	if (currentLoginUserObj == null) {
                		showLoginByQuestionRedirect();
            		    return;
            		}
        		    var commentsObjPromise = questionService.addReplyToCommentsSave(commentsId, content)
        		    commentsObjPromise.success(function(data) {
        		        if (data.status == "SUCCESS") {
        		            var newReplyCount = parseInt(commentsReplyCount) + 1;
        		            $("#commentsReplyCount" + rowIndex + colIndex).text("Reply (" + newReplyCount + ")");
        		            var replyObj = data.response;
        		            var replyPostUserFullName = "";
	                        if(replyObj.postBy.profile.firstname != null && replyObj.postBy.profile.firstname != ""){
	                        	replyPostUserFullName += replyObj.postBy.profile.firstname; 
	                        }
	                        if(replyObj.postBy.profile.lastname != null && replyObj.postBy.profile.lastname != ""){
	                        	replyPostUserFullName += " " + replyObj.postBy.profile.lastname; 
		                    }
//        		            var replyPostUserFullName = replyObj.postBy.profile.firstname + " " + replyObj.postBy.profile.lastname;
        		            var date = new Date(replyObj.createdOn.replace(/-/g, '/').split('.')[0]);
        		            var amPm;
        		            if (date.getHours() > 12)
        		            {
        		                amPm = "PM";
        		            }
        		            else
        		            {
        		                amPm = "AM";
        		            }
        		            var replyPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
        		            var replypostByPicture = "";
        		            var replyContent = replyObj.content;
        		            var replypostByLocation;
        		            if (replyObj.postBy.profile.location == null || replyObj.postBy.profile.location == "" || replyObj.postBy.profile.location.displayName == null)
        		            {
        		            	replypostByLocation = "";
        		            }
        		            else
        		            {
        		                replypostByLocation = "| " + replyObj.postBy.profile.location.displayName + " ";
        		            }
        		            if (replyObj.postBy.profile.profileSmallPictureUrl == null || replyObj.postBy.profile.profileSmallPictureUrl == "") 
        		            {
        		                replypostByPicture = "../../img/default-user.png";
        		            }
        		            else {
        		                if (replyObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || replyObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1)
        		                {
        		                	replypostByPicture = replyObj.postBy.profile.profileSmallPictureUrl;
        		                }
        		                else {
        		                	replypostByPicture = "../../" + replyObj.postBy.profile.profileSmallPictureUrl;
        		                }
        		            }
        		            var profile_user_id = replyObj.postBy.id;
        		            var replyStr = '<div  id="' + replyObj.id + '" style="padding:10px;width:460px;min-height:30px;" >' + '<div class="reply-by-and-post-time"  style="font-size:10px;"><span ><img class="reply-post-by-picture-img" src="' + replypostByPicture + '" style="width:25px;height:25px;margin-right:10px;"/><a href="#" onclick="javascript:redirectToUserProfilePage(\'' + profile_user_id + '\')">' + replyPostUserFullName + '&nbsp;</a>' + replypostByLocation + '|&nbsp;' + replyPostTime + '</span></div>' + '<div class="reply-post-by-content" style="width:460px;word-wrap: break-word;min-height:35px;font-size:14px;color:black;margin-left:35px;">' + replyContent + '</div>' + '</div>';
        		            $("#comments-reply-content" + rowIndex + colIndex).append(replyStr);
        		            removeCommentsReplyButtonDisabled('button_add-new-reply-to-comments_textarea'+rowIndex+colIndex );
        		        }
        		    }).error(function(data) {
        		    	$scope.addAnotherEmailDisableEditor();
        		    })
        		    .then(function(response) {
        		    });
                },
                $scope.middleAddNewQuestionEnableEditor = function () {
                    $scope.middleAddNewQuestionEnableEditor = true;
                },
                $scope.middleAddNewQuestionDisableEditor = function () {
                    $scope.middleAddNewQuestionEnableEditor = false;
                },
                $scope.addNewMiddleQuestionButton = function () {
                    $scope
                        .middleAddNewQuestionDisableEditor();
                },
                $scope.addNewCategory = function (tag) {
                	if (categoryString.length >= 5) {
                	    $("#question_tags_alert_message_top").show();
                	    $("#question_tags_alert_message_top").text(TAGS_UPPER_LIMIT_MESSAGE);
                	    return;
                	}
                	var type = "ADD";
                	var questionObjPromise = questionService.updateQuestionCategories(questionId, tag, type);
                	questionObjPromise.success(function(data) {
                	    var id = data.response.tags.length;
                	    var tagVar = tag;
                	    tagVar = tagVar.replace("/", "+");
                	    tagVar = tagVar.replace(" ", "-");
                	    var tagUrl = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(tagVar.trim());
                	    $(".question_tags_edit").append('<span class="question_tags_category"  id="question_tags_editable' + id + '"><a  href="">' + tag + '</a><a class="topic_remove" href="" onclick="removeCategory(\'' + id + '\',\'' + questionId + '\',\'' + tag + '\')">x</a></span>&nbsp;');
                	    $(".question_tags").append('<a  class="question_tags_category" id="question_tags_' + id + '" href="' + tagUrl + '"  target="_self">' + tag + '</a>&nbsp;');
                	    $scope.questionCategorySelected = "";
                	}).error(function(data) {
                	    console.log("In error " + data);
                	}).then(function(response) {
                	});
                };
                $scope.removeCategory = function (questionId, categoryId,categoryText) {
                	if (categoryString.length <= 1) {
                	    $("#question_tags_alert_message_top").show();
                	    $("#question_tags_alert_message_top").text(TAGS_CANT_REMOVE_ALL);
                	    return;
                	}
                	var type = "REMOVE";
                	var questionObjPromise = questionService.updateQuestionCategories(questionId, categoryText, type);
                	questionObjPromise.success(function(data) {
                	    $("#question_tags_editable" + categoryId).remove();
                	    $("#question_tags_" + categoryId).remove();
                	}).error(function(data) {
                	    console.log("In error " + data);
                	}).then(function(response) {
                	});
                },
                $scope.updateAnswerContentByAnswerId = function (index, answerId, content) {
                	var answerObjPromise = questionService.updateAnswerContent(answerId, content);
                	answerObjPromise.success(function(data) {
                	    content = content;
                	    $("#question_answer" + index).html(content);
                	    var divContent = "#question_answer_textareadiv" + index;
                	    $('#question_answer_textarea' + index).attr('value', content);
                	    $(divContent + " .jqte_editor")[0].innerHTML = content;
                	    $("#question_answer" + index).show();
                	    $("#question_answer_edit_div" + index).show();
                	    $("#question_answer_textareadiv" + index).hide();
                	    // update answers link to new tab
                	    addTargetAndHttpToAnswersLink();
                	})
                	.error(function(data) {
                	    console.log("In error " + data);
                	})
                	.then(function(response) {
                	});
                },
                $scope.answerVotesUpDown = function ( answerId, action) {
                	if (currentLoginUserObj == null) {
                	    showLoginByQuestionRedirect();
                	    return;
                	}
                	if (action == "downvote") {
                	    var answerObj = getAnswerContentById(answerId);
                	    var currentUserId = $("#login_user_id").text();
                	    if ((answerObj.votes.userUpvotes.indexOf(currentUserId)) < 0) {
                	        return;
                	    }
                	}
                	var questionObjPromise = questionService.updateAnswerVotes(answerId, action);
                	questionObjPromise.success(function(data) {
                	    $("#vote_up_link_text_" + answerId).text(data.response.votes.total);
                	}).error(function(data) {
                	    console.log("In error " + data);
                	}).then(function(response) {
                	});
                	if (action == "downvote") {
                	    $("#vote_down_link" + answerId).css("opacity", "0.1");
                	    $("#vote_up_link" + answerId).css("opacity", "1");
                	    $("#vote_down_link" + answerId).css("cursor", "default");
                	    $("#vote_up_link" + answerId).css("cursor", "pointer");
                	} 
                	else {
                	    $("#vote_up_link" + answerId).css("opacity", "0.1");
                	    $("#vote_down_link" + answerId).css("opacity", "1");
                	    $("#vote_down_link" + answerId).css("cursor", "pointer");
                	    $("#vote_up_link" + answerId).css("cursor", "default");
                	}
                },
                $scope.deleteQuestionByQuestionId = function (questionDocId) {
                	var questionObjPromise = questionService.deleteQuestionById(questionDocId);
                	questionObjPromise.success(function(data) {
                	    window.location.href = getBaseUrlOfSite();
                	}).error(function(data) {
                	    console.log("In error " + data);
                	}).then(function(response) {
                	});
                },
                $scope.reportQuestionFlag = function(){
                	//alert("in report question flag"+"and question doc id is:"+questionDocId);
                	setLoaderImageOnButton('modal-footer-question-flag-ok');
                	$("#modal-footer-question-flag-cancel").prop("disabled", true);                        
                	var questionObjPromise = questionService.reportQuestionFlagById(questionDocId);
                	questionObjPromise.success(function(data) {                	
                 	    $("#reportFlagModal").hide();
                	    $("#reportFlagModal").modal('hide');
                	    removeLoaderImageOnButton('modal-footer-question-flag-ok');
                	    $("#modal-footer-question-flag-cancel").prop("disabled", false);
                	    $("#report_question_flag_link").hide();
                	})
                	.error(function(data) {
                	    console.log("In error " + data);
                	    removeLoaderImageOnButton('modal-footer-question-flag-ok');
                	    $("#modal-footer-question-flag-cancel").prop("disabled", false);
                	})
                	.then(function(response) {
                	});

                },
                $scope.reportAnswerFlag = function(){
                	//alert("in report question flag"+"and question doc id is:"+questionDocId);
                	setLoaderImageOnButton('modal-footer-answer-flag-ok');
                	$("#modal-footer-answer-flag-cancel").prop("disabled", true);                        
                	var answerObjPromise = questionService.reportAnswerFlagById(currentAnswerToFlagId);
                	answerObjPromise.success(function(data) {                	
                 	    $("#answerReportFlagModal").hide();
                	    $("#answerReportFlagModal").modal('hide');
                	    removeLoaderImageOnButton('modal-footer-answer-flag-ok');
                	    $("#modal-footer-answer-flag-cancel").prop("disabled", false);
                	    $("#report_answer_flag_link").hide();
                	})
                	.error(function(data) {
                	    console.log("In error " + data);
                	    removeLoaderImageOnButton('modal-footer-answer-flag-ok');
                	    $("#modal-footer-answer-flag-cancel").prop("disabled", false);
                	})
                	.then(function(response) {
                	});

                },
                $scope.deleteAnswerByAnswerId = function (answerId) {
                	var ansewrObjPromise = questionService.deleteAnswerByAnswerId(answerId);
                	ansewrObjPromise.success(function(data) {
                	    $("#" + answerId).remove();
                	    $("#seprator" + answerId).remove();
                	    var oldAnswerCount = $("#questionAnswerCount").text();
                	    $("#questionAnswerCount").text(parseInt(oldAnswerCount) - 1);
                	    $("#messageModal").hide();
                	    $("#messageModal").modal('hide');
                	    removeLoaderImageOnButton('delete_ok_btn');
                	    $("#delete_cancel_btn").prop("disabled", false);
                	}).error(function(data) {
                	    console.log("In error " + data);
                	    removeLoaderImageOnButton('delete_ok_btn');
                	    $("#delete_cancel_btn").prop("disabled", false);
                	}).then(function(response) {
                	});
                };
            }
        ]);
function setTrendingQuestions(responseObj) {
	var obj=responseObj.content;
    for (i = 0; i < obj.length; i++) {
        var display = "block";
        var urlRed = formQuestionDetailRedirectUrl(obj[i].link);
         $(".trending_questions_content_links").append('<p class="trending_questions_results_p" style="font-size: 16px;display:' + display + '"><a href="' + urlRed + '"target="_self" >' + obj[i].title + '</a></p>');
    }
    if (responseObj.firstPage == true) {
        if (obj.length == 0) {
            $(".trending_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>');
        }
    }
    if (responseObj.lastPage == false) {
        $("#more_trending_questions").show();
    } 
    else {
        $("#more_trending_questions").hide();
    }
}
function setRelatedQuestions(responseObj) {
	var obj=responseObj.content;
    for (i = 0; i < obj.length; i++) {
        var display = "block";
        var urlRed = formQuestionDetailRedirectUrl(obj[i].link);
         $(".related_questions_content_links").append('<p class="trending_questions_results_p" style="font-size: 16px;display:' + display + '"><a href="' + urlRed + '"target="_self" >' + obj[i].title + '</a></p>');
    }
    if (responseObj.firstPage == true && obj.length <= 0) {
            $(".related_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>');
    }
    if (responseObj.lastPage == false) {
        $("#more_related_questions").show();
    } 
    else {
        $("#more_related_questions").hide();
    }
}
function setTrendingTags(responseObj) {
	var data = responseObj.content;
    var trendingTagsStr = "";
    var tagsArray = data;
    var countZeroFlag = false;
    for (i = 0; i < tagsArray.length; i++) {
        var tag = tagsArray[i].name;
        var count = tagsArray[i].totalNumQuestionsTagged;
        var tagVar = tag;
        tagVar = tagVar.replace("/", "+");
        tagVar = tagVar.replace(" ", "-");
        var tagUrl = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(tagVar.trim());
        var str = "";
        if (parseInt(count)) {
            str = '<div style="float:left;padding:0px 5px;"><a style="background-color: #C2D7E7;border-radius: 3px 3px 3px 3px;color: #19558D;display: inline-block;padding:6px;text-decoration: none;" class="question_tags_category"  target="_self" href="' + tagUrl + '">' + tag + '<span style="color:gray">  X  </span><span style="color:black">' + count + '</span></a></div>';
        } else {
            $("#more_trending_tags").hide();
            countZeroFlag = true;
        }
        trendingTagsStr = trendingTagsStr + str;
    }
    if(tagsArray.length >=0 && tagsArray.length>=trendingTagsPageSize && countZeroFlag==false){
		$("#more_trending_tags").show();
	}
	else{
		$("#more_trending_tags").hide();
	}
    $("#trending_tags_content_links").append(trendingTagsStr);
}
var setAnswerFlag = false;
var documentScope;
var categoryString = new Array();
// var rightAllCategories = new Array();
function showAllAnswersByQuestion(answers) {
    if (answers == null && answers.length == 0) {
        $("#questionAnswerCount").text(0);
        return;
    }
    if (setAnswerFlag == true) {
        return;
    }

    $("#questionAnswerCount").text(answers.length);
    setAnswerFlag = true;
    // $(".old-answer-descriptions").show();
    var showBestVotedAnswerTag = false;
    var showBestSocialAnswerTag = false;
    var showBestLocationAnswerTag = false;
    var showBestVotedSocialAndLocationAnswerTag=false;
    
    for (i = 0; i < answers.length; i++) {
    	var answerPostUserFullName = "";
        if(answers[i].postBy.profile.firstname != null && answers[i].postBy.profile.firstname != ""){
        	answerPostUserFullName += answers[i].postBy.profile.firstname; 
        }
        if(answers[i].postBy.profile.lastname != null && answers[i].postBy.profile.lastname != ""){
        	answerPostUserFullName += " " + answers[i].postBy.profile.lastname; 
        }
//    	var answerPostUserFullName = answers[i].postBy.profile.firstname + " " + answers[i].postBy.profile.lastname;
        // var answerPostTime=answers[i].createdOn.dayOfMonth+"
        // "+getMonthNameByNumber(answers[i].createdOn.monthOfYear)+" "+
        // answers[i].createdOn.year+"
        // "+answers[i].createdOn.hourOfDay+":"+answers[i].createdOn.minuteOfHour+"
        // "+"am";
        var date = new Date(answers[i].createdOn.replace(/-/g, '/').split('.')[0]);
        var amPm;
        if (date.getHours() > 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        var answerPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
        // +"
        // "+date.getHours()+":"+date.getMinutes()+"
        // "+amPm;
        var answerpostByPicture = "";
        var answerpostByLocation = "";
        var answerContent = answers[i].content;
        // answerContent=openLinkToNewTab(answerContent);
        var answerCommentCount = answers[i].comments.length;
        if (answers[i].postBy.profile.location == null || answers[i].postBy.profile.location == "" || answers[i].postBy.profile.location.displayName == null) {
            answerpostByLocation = "";
        } else {
            answerpostByLocation = "| " + answers[i].postBy.profile.location.displayName + " ";
        }
        if (answers[i].postBy.profile.profileSmallPictureUrl == null || answers[i].postBy.profile.profileSmallPictureUrl == "") {
            answerpostByPicture = "../../img/default-user.png";
        } else {

            if (answers[i].postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || answers[i].postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
                answerpostByPicture = answers[i].postBy.profile.profileSmallPictureUrl;
            } else {
                answerpostByPicture = "../../" + answers[i].postBy.profile.profileSmallPictureUrl;
            }
        }

        var answerpostByBio = "";
        if (answers[i].postBy.profile.bio == null || answers[i].postBy.profile.bio == "") {
            answerpostByBio = "";
        } else {
            answerpostByBio = "| " + answers[i].postBy.profile.bio + " ";
        }
        var voteTotal = answers[i].votes.total;
        var downVoteDisplay;
        if (voteTotal == "0") {
            downVoteDisplay = "block";
        } else {
            downVoteDisplay = "block";
        }
        var currentUserId = $("#login_user_id").text().trim();
        var loginUserEmail = $("#login_user_email").text().trim();
        var editClickEnable = "none";
        if (answers[i].postBy.id == currentUserId) {
            editClickEnable = "block";
        }
        var upLinkOpacity;
        var downLinkOpacity;
        var upLinkCursor;
        var downLinkCursor;
        if (answers[i].votes.userUpvotes.indexOf(currentUserId) < 0) {
            upLinkOpacity = 1;
            downLinkOpacity = 0.1;
            upLinkCursor = "pointer";
            downLinkCursor = "default";
        } else {
            upLinkOpacity = 0.1;
            downLinkOpacity = 1;
            upLinkCursor = "default";
            downLinkCursor = "pointer";
        }
        var profilePictureRadius = "0%";
        if (answers[i].postBy.primarySocialNetworkConnection) {
            profilePictureRadius = "50%";
        } else {
            profilePictureRadius = "0%";
        }
        var profile_user_id = answers[i].postBy.id;
        // SOCIAL_MEDIA_FACEBOOK,
        // SOCIAL_MEDIA_LINKEDIN,SOCIAL_MEDIA_GOOGLE,LOCATION,VOTES
        var bestAnswerTag = "";   
        /*var showBestVotedAndLocationAnswerTag = false;
        var showBestSocialAndLocationAnswerTag = false;
        var showBestSocialAndVotedAnswerTag = false;*/
        
        if(answers[i].answerSortType.length>0){
        	if(answers[i].answerSortType.length>1){
        		if(answers[i].answerSortType.length==2){
        			var tagHTML = compareAnswerSortType(answers[i].answerSortType);
        			bestAnswerTag = tagHTML;
        		}
        		else if(answers[i].answerSortType.length==3 && showBestVotedSocialAndLocationAnswerTag==false){
        		   showBestVotedSocialAndLocationAnswerTag = true;
 		           bestAnswerTag = '<div class="best_answer_div sort_by_votes">Best Social, Location, And Voted Answer</div>';
        		}
        	}
//        	if(answers[i].answerSortType.length>=1){
//        		answers[i].answerSortType[0]="VOTES";
//        		answers[i].answerSortType[1]="SOCIAL_MEDIA_FACEBOOK";
//        		var tagHTML = compareAnswerSortType(answers[i].answerSortType);
//        		bestAnswerTag = tagHTML;
//         	}
        	else{
        		console.log("sort type"+answers[i].answerSortType[0]);
		       if (answers[i].answerSortType[0] == "VOTES" && showBestVotedAnswerTag == false)
		       {
		           showBestVotedAnswerTag = true;
		           bestAnswerTag = '<div class="best_answer_div sort_by_votes">Best Voted Answer</div>';
		       }
		       else if (answers[i].answerSortType[0] == "LOCATION" && showBestLocationAnswerTag == false)
		       {
		           showBestLocationAnswerTag = true;
		           bestAnswerTag = '<div class="best_answer_div sort_by_location">Best Location Answer</div>';
		       }
		       else {
		           if (showBestSocialAnswerTag == false && answers[i].answerSortType[0] != "VOTES" && answers[i].answerSortType[0] != "LOCATION") {
		               showBestSocialAnswerTag = true;
		               bestAnswerTag = '<div class="best_answer_div sort_by_social_network">Best Social Answer</div>';
		           }
		       }
        	}
        }
        var deleteLink = "";
        if (answers[i].postBy.id == currentUserId) {
        	deleteLink = '<div id="delete_answer_link" style="float:right;margin-left: 15px;"><a href="#" onclick="deleteAnswerFromDom(\'' + answers[i].id + '\')">Delete</a></div>'
        }
        else{
        	if(currentLoginUserObj!=null){
            	if(currentLoginUserObj.grantedAuthorities[0]!=undefined && currentLoginUserObj.grantedAuthorities[0]=="ADMIN" ){
            		deleteLink = '<div id="delete_answer_link" style="float:right;margin-left: 15px;"><a href="#" onclick="deleteAnswerFromDom(\'' + answers[i].id + '\')">Delete</a></div>'
            	}
            	else{
            		deleteLink = '<a id="answer_flag_message_link" data-toggle="modal" href="#answerReportFlagModal" data-dismiss="modal" style="width:0px;height:0px"></a><div id="report_answer_flag_link" style="float:right;margin-left:10px"><a href="#" onclick="flagAnswer(\'' + answers[i].id + '\')">Report</a></div>';
            	}
        	}
        }
        // commentContent=openLinkToNewTab(commentContent);
        var answerStr='<div style="width:100%;clear:both;margin-bottom:15px;margin-top:15px;position:relative;font-family:font-family:calibri" id="'+answers[i].id+'">'+
			deleteLink+bestAnswerTag+
			'<div class="postby_and_post_time" style="font-size:10px;">'+
			'<table><tr><td><img class="answer-post-by-picture-img" src="'+answerpostByPicture+'"  style="width:30px;height:30px;margin-right:10px;border-radius:'+profilePictureRadius+';"/></td><td><span ><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +answerPostUserFullName + '&nbsp;</a>'+answerpostByBio+answerpostByLocation+
			'|&nbsp;'+answerPostTime+'</span></td></tr></table>'+
			'</div>'+
			'<table><tr><td valign="top">'+		   			 
			'<div class="answer-post-by-picture-content_and_vote_up_dow">'+
			'<div style="float:left;width:35px;height:55px;margin-top:15px;">'+
			'<div class="vote_up_down_content">'+
			'<div class="vote_up_content">'+
			'<div class="vote_up_link_background" style="opacity:'+upLinkOpacity+';cursor:'+upLinkCursor+'" id="vote_up_link'+answers[i].id+'" onclick="upVoteToAnswerById(\''+answers[i].id+'\')"></div><div class="vote_up_link_text" id="vote_up_link_text_'+answers[i].id+'">'+voteTotal+'</div></div>'+
			'<a style="display:'+downVoteDisplay+';opacity:'+downLinkOpacity+';cursor:'+downLinkCursor+'" id="vote_down_link'+answers[i].id+'" href="" onclick="javascript:downVoteToAnswerById(\''+answers[i].id+'\')" class="vote_down_content vote_down_link"></a></div>'+
			'</div></td><td>'+
			'<div class="answer-post-by-content" id="question_answer'+i+'" style="width:565px;word-wrap: break-word;color:black;font-size:14px;">'+answerContent+'</div><div class="question_answer_edit_div" id="question_answer_edit_div'+i+'"><a class="edit" style="width:22px;display:'+editClickEnable+'" href="#" onclick="javascript:showEditableAnswerContentById(\'' +i+'\',\''+answers[i].id+'\')">Edit</a></div>'+
			'<div id="question_answer_textareadiv'+i+'" style="display:none;width:560px">'+
			'<textarea class="editExistingAnswerTextarea" id="question_answer_textarea'+i+'" value='+answerContent+' ></textarea>'+
			'<div style="margin-bottom:10px;margin-top:5px;float:right;">'+
		    '<a href="#" onclick="javascript:disableUpdateAnswerUpdate(\''+i+'\')" style="margin-right:5px;">Close</a>'+
			'<button type="submit" class="btn btn-primary" onclick="updateAnswerContent(\''+i+'\',\''+answers[i].id+'\')">Update</button></div>'+	        				
			'</div>'+
			'</div></td></tr></table>'+
			'<div style="margin-left:35px;">'+
			'<span class="oldAnswersCommentsshow" style="clear:both;font-size:10px;">'+
			'<img src="../../img/comment-icon.png" style="width:8px;height:8px"></img>'+
			'<a id="answerCommentsCount'+i+'" style="margin-left:6px;" href="javascript:commentsBoxShow(\'' +i+'\',\''+answers[i].id+'\')">Comments&nbsp;('+answerCommentCount+')</a>'+	 			   			
	   		'</span></div>'+ 			   		
	   		'<div class="tip" id="answers-comments-content'+i+'" style="width:562px;margin-left:35px;margin-top:10px;border:1px solid #E0E0E0;display:none;position:relative;"></div>'+
	   		'<div class="add-new-comment-to-answer'+i+'" style="display:none;margin-left:35px;margin-top:10px;">'+
	   	    '<div style="float:left;width:35px;height:35px;">'+
	   		'<img class="answer-post-by-picture-img" src="../../img/default-user.png" style="width:30px;height:30px" />'+
	   		'</div>'+
			'<textarea class="addNewComment_to_Answer" id="addNewComment_to_Answer_'+i+'"  placeholder="Enter Comment" style="width:312px;margin-right:5px;height:20px"/>'+
			'<button style="margin-top:-10px;width:150px;" type="button" id="button_addNewComment_to_Answer_'+i+'"  class="btn btn-txt" onclick="addCommentsToAnswerById(\'' +i+'\',\''+answers[i].id+'\',\''+answerCommentCount+'\'\)">Add Comments</button>'+
			'<a style="margin-left:5px;" href="javascript:closeCommentsAddNewCommentsBox(\'' +i+'\',\''+answers[i].id+'\')">Cancel</a>'+
			'</div>'+
			'</div><div id="seprator'+answers[i].id+'" class="seprator" style="background-color: #E0E0E0;height:2px;width:100%;clear:both;margin-top:5px;"></div>';

        $('.old-answer-descriptions').append(answerStr);
        expandTextarea("addNewComment_to_Answer_" + i);
        for (j = 0; j < answers[i].comments.length; j++) {
            var commentsObj = answers[i].comments[j];
            var commentPostUserFullName = "";
            if(commentsObj.postBy.profile.firstname != null && commentsObj.postBy.profile.firstname != ""){
            	commentPostUserFullName += commentsObj.postBy.profile.firstname; 
            }
            if(commentsObj.postBy.profile.lastname != null && commentsObj.postBy.profile.lastname != ""){
            	commentPostUserFullName += " " + commentsObj.postBy.profile.lastname; 
            }
//            var commentPostUserFullName = commentsObj.postBy.profile.firstname + " " + commentsObj.postBy.profile.lastname;
            // var commentPostTime=commentsObj.createdOn.dayOfMonth+"
            // "+getMonthNameByNumber(commentsObj.createdOn.monthOfYear)+" "+
            // commentsObj.createdOn.year+"
            // "+commentsObj.createdOn.hourOfDay+":"+commentsObj.createdOn.minuteOfHour+"
            // "+"am";
            var date = new Date(commentsObj.createdOn.replace(/-/g, '/').split('.')[0]);
            var amPm;
            if (date.getHours() > 12) {
                amPm = "PM";
            } else {
                amPm = "AM";
            }
            var commentspostByLocation;
            if (commentsObj.postBy.profile.location == null || commentsObj.postBy.profile.location == "" || commentsObj.postBy.profile.location.displayName == null) {
                commentspostByLocation = "";
            } else {
                commentspostByLocation = "| " + commentsObj.postBy.profile.location.displayName + " ";
            }
            var commentPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
            // +"
            // "+date.getHours()+":"+date.getMinutes()+"
            // "+amPm;
            var commentpostByPicture = "";
            var commentContent = commentsObj.content;
            var commentsReplyCount = commentsObj.replies.length;
            if (commentsObj.postBy.profile.profileSmallPictureUrl == null || commentsObj.postBy.profile.profileSmallPictureUrl == "") {
                commentpostByPicture = "../../img/default-user.png";
            } else {
                if (commentsObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || commentsObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
                    commentpostByPicture = commentsObj.postBy.profile.profileSmallPictureUrl;
                } else {
                    commentpostByPicture = "../../" + commentsObj.postBy.profile.profileSmallPictureUrl;
                }
            }
        	//commentContent=openLinkToNewTab(commentContent)
	        var profile_user_id = commentsObj.postBy.id;
			var commentsStr='<div  id="'+commentsObj.id+'" style="padding: 10px;min-height:35px;width:500px;" >'+
				'<div class="comments-by-and-post-time" style="font-size:10px;"><span ><img class="comments-post-by-picture-img" src="'+commentpostByPicture+'"  style="margin-right:10px;width:25px;height:25px;9 ("/><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +commentPostUserFullName + '&nbsp;</a>'+commentspostByLocation+
				'|&nbsp;'+commentPostTime+'</span></div>'+		   			
				'<div class="comments-post-by-content" style="width:460px;word-wrap: break-word;margin-left:35px;color:black;font-size:14px;">'+commentContent+'</div>'+
				'<div style="clear:both;margin-left:35px;font-size:10px;"><span class="oldCommentsReplyshow" >'+
				'<img src="../../img/reply-icon.png" style="width:8px;height:8px"></img>'+
				'<a id="commentsReplyCount'+i+''+j+'" style="margin-left:6px;" href="javascript:replyToCommentsBoxShow(\'' +i+'\',\'' +j+'\',\''+commentsObj.id+'\')">Reply&nbsp;('+commentsReplyCount+')</a>'+	 			   			
				'</span><div>'+	   			
				'<div class="tip" id="comments-reply-content'+i+''+j+'" style="width:460px;margin-top:5px;border:1px solid #E0E0E0;display:none;position:relative;"></div>'+
				'<div class="add-new-reply-to-comments'+i+''+j+'" style="display:none;margin-top:10px;">'+
				'<div style="float:left;width:35px;height:35px;">'+
				'<img class="answer-post-by-picture-img" src="../../img/default-user.png" style="width:30px;height:30px"/>'+
				'</div>'+
				'<textarea  id="add-new-reply-to-comments_textarea'+i+''+j+'" placeholder="Enter Reply" style="width:274px;margin-right:5px;height:20px;"/>'+
				'<button id="button_add-new-reply-to-comments_textarea'+i+''+j+'"  style="margin-top:-10px;width:90px;" type="button" class="btn btn-txt" onclick="addReplyByCommentsId(\'' +i+'\',\'' +j+'\',\''+commentsObj.id+'\',\''+commentsReplyCount+'\')">Reply</button>'+
				'<a  style="margin-left:5px;"href="javascript:replyToCommentsBoxClose(\'' +i+'\',\'' +j+'\',\''+commentsObj.id+'\')">Cancel</a>'+
				'</div>'+
				'</div>';
   			$("#answers-comments-content"+i).append(commentsStr);
   			expandTextarea("add-new-reply-to-comments_textarea"+i+""+j);
            for (k = 0; k < commentsObj.replies.length; k++) {

                var replyObj = commentsObj.replies[k];
                var replyPostUserFullName = "";
                if(replyObj.postBy.profile.firstname != null && replyObj.postBy.profile.firstname != ""){
                	replyPostUserFullName += replyObj.postBy.profile.firstname; 
                }
                if(replyObj.postBy.profile.lastname != null && replyObj.postBy.profile.lastname != ""){
                	replyPostUserFullName += " " + replyObj.postBy.profile.lastname; 
                }
//                var replyPostUserFullName = replyObj.postBy.profile.firstname + " " + replyObj.postBy.profile.lastname;
                // var replyPostTime=replyObj.createdOn.dayOfMonth+"
                // "+getMonthNameByNumber(replyObj.createdOn.monthOfYear)+" "+
                // replyObj.createdOn.year+"
                // "+replyObj.createdOn.hourOfDay+":"+replyObj.createdOn.minuteOfHour+"
                // "+"am";
                var date = new Date(replyObj.createdOn.replace(/-/g, '/').split('.')[0]);
                var amPm;
                if (date.getHours() > 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                var replypostByLocation;
                if (replyObj.postBy.profile.location == null || replyObj.postBy.profile.location == "" || replyObj.postBy.profile.location.displayName == null) {
                    replypostByLocation = "";
                } else {
                    replypostByLocation = "| " + replyObj.postBy.profile.location.displayName + " ";
                }
                var replyPostTime = date.getUTCDate() + " " + getMonthNameByNumber(date.getUTCMonth()) + " " + date.getUTCFullYear();
                // +"
                // "+date.getHours()+":"+date.getMinutes()+"
                // "+amPm;
                var replypostByPicture = "";
                var replyContent = replyObj.content;
                if (replyObj.postBy.profile.profileSmallPictureUrl == null || replyObj.postBy.profile.profileSmallPictureUrl == "") {
                    replypostByPicture = "../../img/default-user.png";
                } else
                {
                    if (replyObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") != -1 || replyObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http") != -1) {
                        replypostByPicture = replyObj.postBy.profile.profileSmallPictureUrl;
                    }
                    else {
                        replypostByPicture = "../../" + replyObj.postBy.profile.profileSmallPictureUrl;
                    }
                }
                var profile_user_id = replyObj.postBy.id;
				var replyStr='<div  id="'+replyObj.id+'" style="padding:10px;width:460px;min-height:30px;" >'+
	   			'<div class="reply-by-and-post-time" style="font-size:10px;"><span ><img class="reply-post-by-picture-img" src="'+replypostByPicture+'" style="margin-right:10px;width:25px;height:25px;"/><a href="#" onclick="javascript:redirectToUserProfilePage(\''+profile_user_id+'\')">' +replyPostUserFullName + '&nbsp;</a>'+replypostByLocation+
	   			'|&nbsp;'+replyPostTime+'</span></div>'+			   			
   				'<div class="reply-post-by-content" style="width:460px;word-wrap: break-word;font-size:14px;color:black;margin-left:35px;">'+replyContent+'</div>'+
   				'</div>';
				$("#comments-reply-content"+i+j).append(replyStr);
            }
        }
    }
    addTargetAndHttpToAnswersLink(); 
}
function addNewAnswerToQuestionControlsShow() {
    $("#question-to-new-answer-add").hide();
    $(".question-to-new-answer-add-control").show();
}

function addAnswerToQuestionDisableEditor() {
    var content = $('#addNewAnswerContent').html("");
    $(".question-to-new-answer-add-control input").attr("value", "");
}

function addAnswerToQuestionByIdSave() {
    if (currentLoginUserObj == null) {
        showLoginByQuestionRedirect();
        return;
    }
    var content = $("#iframe_blue .jqte_editor")[0].innerHTML;
    if (content.trim().length - content.trim().lastIndexOf("<br>") == 4) {
        content = content.substr(0, content.trim().lastIndexOf("<br>"))
        }
    if (content == "" || content == undefined)
        return;
    $("#dummyAnswerContentText").html(content);
    var element = $("#dummyAnswerContentText");
    if (element[0].childNodes[0].nodeType == 3 && element[0].childNodes[0].nodeValue != "") {
        element[0].childNodes[0].nodeValue = capitaliseFirstLetter(element[0].childNodes[0].nodeValue);
        //setFirstCapital = true;
    }
    elements = element.find("*");
    for (i = 0; i < elements.length; i++) {
        elements[i].style.fontSize = "14px";
        if (elements[i].tagName == "a" || elements[i].tagName == "A")
        {
            //elements[i].style.marginLeft = "4px";
        	elements[i].getAttribute("target");
        }
        else {
            elements[i].style.color = "black";
        }
        elements[i].style.fontFamily = "calibri";
    }
    setLoaderImageOnButton("addNewAnswerToQuestionButton");
    documentScope.addAnswerToQuestionSave($("#dummyAnswerContentText").html());
    $("#iframe_blue .jqte_editor")[0].innerHTML = "";
}
function commentsBoxShow(index, answerId) {
    if ($("#answers-comments-content" + index).css("display") == "block") {
        $("#answers-comments-content" + index).hide();
        $(".add-new-comment-to-answer" + index).hide();
    } else {
        $("#answers-comments-content" + index).show();
        $(".add-new-comment-to-answer" + index).show();
        if (currentLoginUserObj == null) {
            $("#addNewComment_to_Answer_" + index).focus(function(e) {
                $(this).blur();
                showLoginByQuestionRedirect();
            });
        }
    }
}

function closeCommentsAddNewCommentsBox(index, answerId) {
    // $(".add-new-comment-to-answer"+index).hide();
	$("#addNewComment_to_Answer_" + index).attr("value", "");
}

function addCommentsToAnswerById(index, answerId, answerCommentCount) {
    if (currentLoginUserObj == null) {
        showLoginByQuestionRedirect();
        return;
    }
    setLoaderImageOnButton("button_addNewComment_to_Answer_" + index);
    var content = $("#addNewComment_to_Answer_" + index).val();
    documentScope.addCommentsByAnswerId(index, answerId, content, answerCommentCount);
    $("#addNewComment_to_Answer_" + index).attr("value", "");
}

function replyToCommentsBoxShow(rowIndex, ColIndex, commentsId) {
    if ($("#comments-reply-content" + rowIndex + ColIndex).css("display") == "block") {
        $("#comments-reply-content" + rowIndex + ColIndex).hide();
        $(".add-new-reply-to-comments" + rowIndex + ColIndex).hide();
    } else {
        $("#comments-reply-content" + rowIndex + ColIndex).show();
        $(".add-new-reply-to-comments" + rowIndex + ColIndex).show();
        if (currentLoginUserObj == null) {
            $("#add-new-reply-to-comments_textarea" + rowIndex + ColIndex).focus(function(e) {
                $(this).blur();
                showLoginByQuestionRedirect();
            });
        }
    }
}

function replyToCommentsBoxClose(rowIndex, ColIndex, commentsId) {
    //$(".add-new-reply-to-comments"+rowIndex+ColIndex).hide();
    $("#add-new-reply-to-comments_textarea" + rowIndex + ColIndex).attr("value", "");
}

function addReplyByCommentsId(rowIndex, ColIndex, commentsId, commentsReplyCount) {
    if (currentLoginUserObj == null) {
        showLoginByQuestionRedirect();
        return;
    }
    var content = $("#add-new-reply-to-comments_textarea" + rowIndex + ColIndex).val();
    setLoaderImageOnButton("button_add-new-reply-to-comments_textarea" + rowIndex + ColIndex)
        documentScope.addReplyByCommentsId(rowIndex, ColIndex, commentsId, content, commentsReplyCount);
    $("#add-new-reply-to-comments_textarea" + rowIndex + ColIndex).attr("value", "");
}

function showEditableAnswerContentById(index, answerId) {
    var content = $("#question_answer" + index).html();
    $("#question_answer" + index).hide();
    $("#question_answer_edit_div" + index).hide();
    $("#question_answer_textareadiv" + index).show();
    var divContent = "#question_answer_textareadiv" + index;
    var htmlValue = $('#question_answer_textarea' + index).val();
    if ($(divContent + " .jqte_editor")[0] == undefined) {
        $("#question_answer_textarea" + index).jqte();
    }
    $(divContent + " .jqte_editor")[0].innerHTML = content;
    $(".jqte_editor").css('font-size', '14px');
}

function disableUpdateAnswerUpdate(id) {
    $("#question_answer" + id).show();
    $("#question_answer_edit_div" + id).show();
    $("#question_answer_textareadiv" + id).hide();
}

function updateAnswerContent(index, answerId) {
    var divContent = "#question_answer_textareadiv" + index;
    var content = $(divContent + " .jqte_editor")[0].innerHTML;

    if (content.trim().length - content.trim().lastIndexOf("<br>") == 4) {
        content = content.substr(0, content.trim().lastIndexOf("<br>"))
        }
    $("#dummyAnswerContentText").html(content);
    var element = $("#dummyAnswerContentText");
    elements = element.find("*");

    if (element.text() == null || element.text() == "") {
        var htmlContent = $("#question_answer" + index).html();
        var htmlContentDiv = "#question_answer_textareadiv" + index;
        $('#question_answer_textarea' + index).attr('value', htmlContent);
        $(htmlContentDiv + " .jqte_editor")[0].innerHTML = htmlContent;
        $("#question_answer" + index).show();
        $("#question_answer_edit_div" + index).show();
        $("#question_answer_textareadiv" + index).hide();
    }
    else {
        var setFirstCapital = false;
        if (element[0].childNodes[0].nodeType == 3 && element[0].childNodes[0].nodeValue != "") {
            element[0].childNodes[0].nodeValue = capitaliseFirstLetter(element[0].childNodes[0].nodeValue);
            setFirstCapital = true;
        }
        for (i = 0; i < elements.length; i++) {
            elements[i].style.fontSize = "14px";
            if (elements[i].tagName == "a" || elements[i].tagName == "A") {
                // elements[i].style.marginLeft="5px";
                } else {
                elements[i].style.color = "black";
            }
            if (setFirstCapital == false) {
                if (elements[i].childNodes[0].nodeType == 3) {
                    elements[i].childNodes[0].nodeValue = capitaliseFirstLetter(elements[i].childNodes[0].nodeValue);
                    setFirstCapital = true;
                }
            }
            elements[i].style.fontFamily = "calibri";
        }
        documentScope.updateAnswerContentByAnswerId(index, answerId, $("#dummyAnswerContentText").html());
        
    }
    
}

function questionCategoryUpdateDisableEditor() {
    $("#question_tags_alert_message_top").hide();
    $(".question_tags_non_edit").show();
    $(".question_tags_edit_category").hide();
    $("#top_edited_mode_question_tags_category_error_alert").hide();
    $(".top_selected_categories_non_exist_tag_error").hide();
}

function questionCategoryUpdateEnableEditor() {
    $("#question_tags_alert_message_top").hide();
    $(".question_tags_non_edit").hide();
    $(".question_tags_edit_category").show();
    $("#top_edited_mode_question_tags_category_error_alert").hide();
    $(".top_selected_categories_non_exist_tag_error").hide();
    $("#top_question_category_search").val("");
    $("#top_question_category_search").focus();
}

function questionCategoryAddNew(value) {
    documentScope.addNewCategory(value);
}

function removeCategory(categoryId, questionId, categoryText) {
    documentScope.removeCategory(questionId, categoryId, categoryText);
}

function upVoteToAnswerById(answerId) {
    if (currentLoginUserObj == null) {
        showLoginByQuestionRedirect();
        return;
    }
    var action = "upvote";
    documentScope.answerVotesUpDown(answerId, action);
}

function downVoteToAnswerById(answerId) {
    if (currentLoginUserObj == null) {
        showLoginByQuestionRedirect();
        return;
    }
    var action = "downvote";
    documentScope.answerVotesUpDown(answerId, action);
}

function questionTitleUpdatenDisableEditor() {
    $(".question-title").show();
    $(".update_question_title").hide();
}
var lastShowQuestionTitle = "";

function questionTitleUpdateEnableEditor() {
    lastShowQuestionTitle = $("#questionTitleEditArea").val();
    $(".question-title").hide();
    $(".update_question_title").show();
    $("#questionTitleEditArea").focus();
}

function questionContentUpdatenDisableEditor() {
    $(".question-post-by-content_top").show();
    $(".update_question_content").hide();
}
var initUpdateContentQuestionFlag = false;

function questionContentUpdateEnableEditor() {
    // Update Content of Question Editor
    if (initUpdateContentQuestionFlag == false) {
        $('#questionContentEditArea').jqte();
        initUpdateContentQuestionFlag = true;
    }
    $(".question-post-by-content_top").hide();
    $(".update_question_content").show();
    $("#questionContentEditAreaDiv .jqte_editor")[0].focus();
    $(".jqte").css("font-size", "14px");
}
var initEnableAnswerEditor = false;

function setFocusOnAddAnswer() {
    if (currentLoginUserObj != null) {
        if (initEnableAnswerEditor == false && jqteStatus == true) {
            setTeEditorToAddNewAnswer();
            initEnableAnswerEditor = true;
        } else {
            $(".jqte_editor").focus();
        }
        $(".jqte_editor").focus();
        $(".jqte_editor").css('font-size', '14px');
    } else {
        $("#addNewAnswerContent").blur();
        showLoginByQuestionRedirect();
    }
}
var currentShowingIndex = 5;

function showingAllRelatedQuestions() {
    var length = $('.related_questions_results_p').length;
    if (currentShowingIndex >= length) {
        $(".showing_all_related_questions").hide()
            return;
    } else {
        for (i = currentShowingIndex + 1; i <= (currentShowingIndex + 5); i++) {
            if (i >= length) {
                $(".showing_all_related_questions").hide()
                    break;
            } else {
                $('#related_question_link_p' + i).show();
            }
        }
        currentShowingIndex = currentShowingIndex + 5;
    }
}

function deleteQuestionFromDom() {
    $("#modal-footer-question").show();
    $("#modal-footer-answer").hide();
    $("#messageModelButton_div").hide()
    $("#modal-footer-questionAdd").hide();
    $("#messageModal").css({"margin-left":"-168px","max-width": "325px"});
    $("#error_message_text").css("width","");
    $("#modal-title-message").html('MESSAGE');
    showErrorMessage("You are about to delete your question. Are you sure?");
}

function deleteQuestionByOk() {
    documentScope.deleteQuestionByQuestionId(questionId);
}
var currentAnswerToDeleteId = "";

function deleteAnswerFromDom(answerId) {
    $("#modal-footer-answer").show();
    $("#modal-footer-question").hide();
    $("#messageModelButton_div").hide();
    $("#modal-footer-questionAdd").hide();
    $("#messageModal").css({"margin-left":"-230px","max-width": "450px"});
    $("#error_message_text").css("width","350px");
    $("#modal-title-message").html('Delete Answer');
    showErrorMessage("The phroogie community values your knowledge and someone might benefit from this answer. You can choose to edit your answer instead. Do you still want to delete your answer?");
    currentAnswerToDeleteId = answerId;
}

function deleteAnswerByOk() {
	setLoaderImageOnButton('delete_ok_btn');
	$("#delete_cancel_btn").prop("disabled", true);
    documentScope.deleteAnswerByAnswerId(currentAnswerToDeleteId);
}

var currentAnswerToFlagId="";

function flagAnswer(answerId) {
	$('#answer_flag_message_link').click();
	currentAnswerToFlagId = answerId;
}

function reportAnswerFlag() {
	documentScope.reportAnswerFlag(currentAnswerToFlagId);
}

function changeQuestionAnonymousType(id, postType) {
    documentScope.updateQuestionAnonymousType(id, postType);
}

// comparing sort type array with return html string
var showBestVotedAndLocationAnswerTag = false;
var showBestSocialAndLocationAnswerTag = false;
var showBestSocialAndVotedAnswerTag = false;

function compareAnswerSortType(answerSortType){	
	if(answerSortType[0].indexOf("SOCIAL_MEDIA")>=0){
		answerSortType[0]="SOCIAL_MEDIA";
	}
	if(answerSortType[1].indexOf("SOCIAL_MEDIA")>=0){
		answerSortType[1]="SOCIAL_MEDIA";
	}
	var str="";
	if($.inArray("VOTES",answerSortType) != -1 && $.inArray("LOCATION",answerSortType) != -1){
		if(showBestVotedAndLocationAnswerTag==false){
			str = '<div class="best_answer_div sort_by_votes">Best Location, And Best Voted Answer</div>';
		}
		showBestVotedAndLocationAnswerTag=true;
		return str;
	}
	else if($.inArray("VOTES",answerSortType) != -1 && $.inArray("SOCIAL_MEDIA",answerSortType) != -1)
	{
		if(showBestSocialAndVotedAnswerTag==false){
			str = '<div class="best_answer_div sort_by_location">Best Social, And Best Voted Answer</div>';
		}
		showBestSocialAndVotedAnswerTag=true;
		return str;
	}
	else if($.inArray("LOCATION",answerSortType) != -1 && $.inArray("SOCIAL_MEDIA",answerSortType) != -1)
	{
		if(showBestSocialAndLocationAnswerTag==false){
			str = '<div class="best_answer_div sort_by_social_network">Best Location, And Best Social Answer</div>';
		}
		showBestSocialAndLocationAnswerTag=true;
		return str;
	}
}
// answers link opne in new tab

function addTargetAndHttpToAnswersLink(){
	var ancElms=$('.answer-post-by-content a');
    for(i=0;i<ancElms.length;i++){
    	var elmHref = $(ancElms[i]).attr("href");
    	$(ancElms[i]).attr("href",addhttp(elmHref));
    	$(ancElms[i]).attr("target","_blank");
    }
}
function addTargetAndHttpToQuestionDescription(){	
	var ancElms=$('.question_description_container a');
    for(i=0;i<ancElms.length;i++){
    	var elmHref = $(ancElms[i]).attr("href");
    	$(ancElms[i]).attr("href",addhttp(elmHref));
    	$(ancElms[i]).attr("target","_blank");
    }
}

