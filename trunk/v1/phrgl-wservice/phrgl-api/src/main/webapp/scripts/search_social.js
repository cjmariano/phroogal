var BASE_URL = location.protocol + "//" + document.location.host+ window.location.pathname;
$(document).ready(function (){
	expandTextarea("rightAddNewQuestionTitle");
	expandTextarea("addNewQuestionRightDetail");
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();	
});
var USER_SOCIAL_CONNECTION_EMAIL="support@phroogal.com";
var DELAY=0;
var processFlag = false;
window.onload=function(){
	var e=document.getElementById("refreshed");
	if(e.value=="no"){
		e.value="yes";
	}	
};
	
function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var trendingObjArr = new Array();
function redirectToQuestionByTrendingQuestionsLinks(id,index){
	var questionsObj = trendingObjArr["trending_questions"];
	var title = questionsObj[parseInt(index)].title;		
	title=replaceSpaceWithhyphen(title);
	var url = getBaseUrlOfSite()+"question/"+id+"/"+removeLastLetter(title);	
	window.open(url, '_blank');
}
function redirectToQuestionDetailForSocialConnection(id,index){
	var questionsObj = socialQuestionObjArr["questions"];
	var title = questionsObj[parseInt(index)].title;
	title = replaceSpaceWithhyphen(title);
	var url = getBaseUrlOfSite()+"question/"+id+"/"+removeLastLetter(title);	
	window.open(url, '_blank');
}
function getUserProfileById(id){
	var userObj;
	$.ajax({  
        async:false,
        url: 'api/users/user-'+id,  
        success: function(data) {
        	userObj = data.response;  
        }        
	});
	return userObj;
}
function getUserProfileByEmail(email){
	var userObj;
	$.ajax({  
        async:false,
        url: 'api/users/user?email='+email,  
        success: function(data) {
        	userObj = data.response;  
        }        
	});
	return userObj;
}
var currentLoginUserObj="";
//var rightAllCategories = new Array();
var trendingPageSize=5;
var trendingPageIndex=0;
	angular.module('search-social', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('searchSocialService',['$http', '$location', function($http, $location) {		
	return {
			addNewQuestion:function(_title,_content,_tags,isAnonymous){
				_title=_title.replace(/[`%|\;:"\{\}\[\]\\\/]/gi, '')
				if(_title.charAt(_title.length-2)=="?"){
					_title = _title.substr(0,_title.length-1);
				}
				if(_title.charAt(_title.length-1)!="?")
				{
					_title = _title+"?";
				}
				_title =capitaliseFirstLetter(_title);
				var _id=$("#current_login_user_id").text();
	            var _postBy={
	            	"id":_id
	            };
				var questionData = {
						 title : _title,
						 content:_content,
						 tags:_tags,
						 postBy:_postBy,
						 anonymous:isAnonymous
				};
				var promise = $http({headers:header_encoding,  method: 'POST',url: 'api/posts/question', data: questionData  });
				return promise;
			},			
			getAllTags:function(){
				var promise = $http({ headers:header_encoding, method: 'GET',url: 'api/tags'});
				return promise;
			},
			getUserSocialPostedQuestions:function(_id){			
				var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions?postBy='+_id+'&pageSize=10'});
			    return promise;			  	
			},
			getTrendingQuestion:function(){
				var promise = $http({headers:header_encoding,  method: 'GET',url: 'api/posts/questions?showTrending=true&pageAt='+trendingPageIndex+'&pageSize='+trendingPageSize});
				return promise;
			},
			addTagByUserId:function(tag,_id){
				var tagsData ={
								"name":tag
							   };
				var promise = $http({headers:header_encoding,   method: 'POST',url: 'api/tags/user-'+_id,data:tagsData});
				return promise;
			}
		};
	}])
	.controller('searchSocialCtrl',
			[ '$scope', 'searchSocialService', function($scope, searchSocialService) {		
		var userObj = null;
		if($("#login_user_id").text()!=undefined && $("#login_user_id").text()!=null && $("#login_user_id").text()!=""){
			userObj= getUserProfileById($("#login_user_id").text());
		}		
		currentLoginUserObj =userObj;		
		var userSocialConnectionObj = null;
		userSocialConnectionObj = getUserProfileByEmail(USER_SOCIAL_CONNECTION_EMAIL);		
		$scope.categoryNames=[];
		documentScope = $scope;
		$scope.resendMail="";
		$scope.rightAddNewQuestionEnableEditor = false;
		$scope.initFunctionCall = function(){
			var tagsJson = $.cookie("tagsArray");
			var tagsArray = $.parseJSON(tagsJson);
			if(tagsArray == null || tagsArray == undefined || tagsArray.length <=0){
				var allCategoryPromise = searchSocialService.getAllTags();
				allCategoryPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						var categoryArray = new Array();
						for(i=0;i<data.response.length;i++){
							$scope.categoryNames.push(data.response[i].name);
						}
					}
					var tagsJson = JSON.stringify($scope.categoryNames);
					setCookie("tagsArray",tagsJson, 3);
					allTagsToFilter($scope.categoryNames);
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });	
			}
			else{
				allTagsToFilter(tagsArray);
				console.log("in else part"+tagsArray[0]);
			}
			this.showAllSocialQuestions();
			this.showAllTrendingQuestions();
		},	
		$scope.addUserTag = function(tagVal,user_id) {
			var userObjPromise = searchSocialService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		$scope.addNewRightQuestionButton=function(){
			if(currentLoginUserObj==null){
				showLoginByQuestionRedirect();
			    return;
		    }
			if(rightAllCategories.length==0){
				return;
			}
			var isAnonymous = $('#postAsAnonymousRight').is(':checked');						
			var isAnonymousFlag =isAnonymous;			
			var content = $scope.rightNewQuestionDescription;
			if(content!="" && content !=null ){
				content=capitaliseFirstLetter(content);
			}
			
			setLoaderImageOnButton('right_add_new_question_button');
			
			var questionObjPromise = searchSocialService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllCategories,isAnonymousFlag);
			questionObjPromise.success(function (data) {					 
				console.log("in success"+data);
				if(data.status=="SUCCESS"){
					$scope.rightNewQuestionDescription="";
					$scope.rightNewQuestionTitle="";
					$scope.rightCategorySelected="";
					$('#postAsAnonymousRight').attr('checked',false);
					rightAllCategories=[];
					$("#modal-footer-question").hide();
					$("#modal-footer-answer").hide();
					$("#messageModelButton_div").hide()
					$("#modal-footer-questionAdd").show();
					showErrorMessage('Thanks phroogie! Your question is now sent to the community. In the meantime search related questions, topics and external resources.');			
					var title = replaceSpaceWithhyphen(data.response.title);
				    newAddedQuestionLink="question/"+data.response.docId+"/"+removeLastLetter(title);
				}
				else{
					$scope.middleNewDescription="";
					$scope.middleNewQuestion="";
					removeLoaderImageOnButton('right_add_new_question_button');
				}
			})
			.error(function (data) {
	          console.log("In error "+data);
	          $scope.middleNewDescription="";
			  $scope.middleNewQuestion="";
			  removeLoaderImageOnButton('right_add_new_question_button');
		     })
		    .then(function (response) {			        	
		    });			
				
		},	
		// getting user social questions by support@phroogal.com at staging and production 
		// for local its ankur@qsstechnosoft.com
		$scope.showAllSocialQuestions = function(){
			if(userSocialConnectionObj!=null)
			{
				var userQuestionsObjPromise = searchSocialService.getUserSocialPostedQuestions(userSocialConnectionObj.id);
				userQuestionsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						if(data.response.length>0){
							setSocialQuestionToTab(data.response);
						}
						else{
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
			}
		},
		$scope.showAllTrendingQuestions = function(){
			var allTrendingQuestionsPromise = searchSocialService.getTrendingQuestion();
			allTrendingQuestionsPromise.success(function (data) {					 
				if(data.status=="SUCCESS"){
					setTrendingQuestions(data.response);
					trendingPageIndex=trendingPageIndex+1;
				}				
			 })
			.error(function (data) {
		          console.log("In error "+data);
		     })
		    .then(function (response) {
			        	
		    });			
		};
	} ]);
	var socialQuestionObjArr = new Array();
    function setSocialQuestionToTab(questionsObj){
    	var questionsStr="";
    	socialQuestionObjArr['questions'] = questionsObj;
    	$.each(questionsObj, function(index, element) {
    		var border="1px solid #E0E0E0";
			var id = element.docId;
			var title = element.title;
		    var content= element.content;
		    var contentStr="";
		    var answerStr="";
		    var postedTypeInfo="";
		    var urlRed=formQuestionDetailRedirectUrl(element.link);
		    
		    if(index==questionsObj.length-1){
		    	border="0px solid #E0E0E0";
		    }
		    if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				content= $("#tempTextContainer").text();
   				contentStr='<div style="font-size: 14px;">'+content+'</div>';
   			}
   			if(element.answered==true)
   			{
   				var totalAnswerCount = element.answers.length;
   				var totalCommentCount = 0;
   				for(i=0;i<element.answers.length;i++){
   					if(element.answers[i].comments!=null && element.answers[i].comments.length>0){
   						totalCommentCount = parseInt(totalCommentCount)+element.answers[i].comments.length;
   					}
   					else{
   						totalCommentCount=parseInt(totalCommentCount)+0;
   					}
   				}
   				answerStr='<div style="font-size:12px;margin-left:12px;"><a style="color: #0088CC;" href="'+urlRed+'" onclick="redirectToQuestionDetailForSocialConnection(\'' +id+'\',\''+index+'\')">Answers ('+totalAnswerCount+')</a><a style="margin-left:5px;color: #0088CC;" href="'+urlRed+'" onclick="redirectToQuestionDetailForSocialConnection(\'' +id+'\',\'' +index+'\')">Comments ('+totalCommentCount+')</a></div>';
   			}
   			else{
   				answerStr="";
   			}
   			var date =new Date(element.createdOn);
			var amPm;
			if(date.getHours()>12){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
   			var postTime = date.getUTCDate()+" "+getMonthNameByNumber(date.getUTCMonth())+" "+ date.getUTCFullYear()+" "+date.getHours()+":"+date.getMinutes()+" "+amPm;
   			
		    questionsStr=questionsStr+'<div id="user_question'+id+'" style="padding:10px 0px 10px 0px;border-bottom:'+border+';">'+
   			'<div><a href="'+urlRed+'"  onclick="redirectToQuestionDetailForSocialConnection(\'' +id+'\',\''+index+'\')" style="font-weight:bold;font-size: 16px;color: #0088CC;">'+title+'</a></div>'+
   			contentStr+answerStr+'<div style="margin-left:12px;font-size:12px;color:gray;clear:both;">Posted On: '+postTime+'</div></div>';   			
        });
        $("#social_questions_content_links").append(questionsStr);
    }
	function setTrendingQuestions(obj){
		for(i=0;i<obj.length;i++)
		{	
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
			$(".trending_questions_content_links").append('<p class="trending_questions_results_p" style="font-size: 16px;"><a href="'+urlRed+'"target="_self" >'+obj[i].title+'</a></p>')
		}			
		if(trendingPageIndex==0 && obj.length <=1){
			$(".trending_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>'); 
		}
		if(obj.length==trendingPageSize){
			$("#more_trending_questions").show();
		}
		else{
			$("#more_trending_questions").hide();
		}
	}
