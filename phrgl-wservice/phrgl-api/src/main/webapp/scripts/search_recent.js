var BASE_URL = location.protocol + "//" + document.location.host
		+ window.location.pathname;
$(document).ready(function (){
	expandTextarea("rightAddNewQuestionTitle");
	expandTextarea("addNewQuestionRightDetail");
	
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();	
	$(".main-container").scroll(function () {     	
		if($(window).scrollTop() + $(window).height() > $(document).height() - 100){
			if(processFlag==false){
				DELAY=100;
			    $("#page_loader_image").show();			    
				documentScope.showAllRecentQuestions();
			}
         }
	});
});

function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
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
var DELAY=0;
var currentLoginUserObj="";
var recentPageSize=10;
var recentPageIndex=0;
var trendingPageSize=5;
var trendingPageIndex=0;
	angular.module('search-recent', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('searchRecentService',['$http', '$location', function($http, $location) {		
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
				var promise = $http({ headers:header_encoding, method: 'POST',url: 'api/posts/question', data: questionData  });
				return promise;
			},			
			getAllTags:function(){
				var promise = $http({headers:header_encoding,  method: 'GET',url: 'api/tags'});
				return promise;
			},
			getRecentQuestions:function(){			
				var promise = $http({headers:header_encoding,  method: 'GET',url: 'api/posts/questions?showMostRecent=true&pageAt='+recentPageIndex+'&pageSize='+recentPageSize});
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
				var promise = $http({ headers:header_encoding,  method: 'POST',url: 'api/tags/user-'+_id,data:tagsData});
				return promise;
			}
		};
	}])
	.controller('searchRecentCtrl',
			[ '$scope', 'searchRecentService', function($scope, searchRecentService) {		
		var userObj = null;
		if($("#login_user_id").text()!=undefined && $("#login_user_id").text()!=null && $("#login_user_id").text()!=""){
			userObj= getUserProfileById($("#login_user_id").text());
		}		
		currentLoginUserObj =userObj;	
		$scope.categoryNames=[];
		documentScope = $scope;
		$scope.resendMail="";
		$scope.initFunctionCall = function(){
			var tagsJson = $.cookie("tagsArray");
			var tagsArray = $.parseJSON(tagsJson);
			if(tagsArray == null || tagsArray == undefined || tagsArray.length <=0){
				var allCategoryPromise = searchRecentService.getAllTags();
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
			this.showAllRecentQuestions();
			this.showAllTrendingQuestions();
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
			
			var questionObjPromise = searchRecentService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllCategories,isAnonymousFlag);
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
		$scope.addUserTag = function(tagVal,user_id) {
			var userObjPromise = searchRecentService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		// getting user recent questions by support@phroogal.com at staging and production 
		// for local its ankur@qsstechnosoft.com
		$scope.showAllRecentQuestions = function(){
			processFlag=true;
			 setTimeout(function (){
			    var recentQuestionsObjPromise = searchRecentService.getRecentQuestions();
				recentQuestionsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						if(data.response.content.length>0){
							setRecentQuestions(data.response);  
							recentPageIndex=recentPageIndex+1;
						}
						else{
							$("#recent_questions_content_links").append("<div style='color:black;'>No recent questions found</div>");
						}
						if(data.response.lastPage==false){
							processFlag=false;
						}
						else{
							processFlag=true;
						}					
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    	
			    });
		     },DELAY);	  	      	
		},
		$scope.showAllTrendingQuestions = function(){
			var allTrendingQuestionsPromise = searchRecentService.getTrendingQuestion();
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
	var processFlag = false;	
	function setRecentQuestions(responseObj){		
    	var questionsStr="";
    	var obj = responseObj.content;
    	$.each(obj, function(index, element) {
    		var border="1px solid #E0E0E0";
			var id = element.docId;
			var title = element.title;
		    var content= element.content;
		    var contentStr="";
		    var answerStr="";
		    var postedTypeInfo="";
		    var urlRed=formQuestionDetailRedirectUrl(element.link);		    
			var totalAnswerCount = element.totalAnswerCount;
			var totalCommentCount = element.totalCommentCount;
		    if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				content= $("#tempTextContainer").text();
   				contentStr='<div style="font-size:14px;margin-left:12px;color:black;">'+content+'</div>';
   			}
   			if(element.answered==true)
   			{
   				answerStr='<div style="font-size:12px;margin-left:12px;"><a style="color: #0088CC;" href="'+urlRed+'" target="_self" >Answers ('+totalAnswerCount+')</a><a style="margin-left:5px;color: #0088CC;" href="'+urlRed+'"target="_self" >Comments ('+totalCommentCount+')</a></div>';
   			}
   			else{
   				urlRed=urlRed+"?answer=0";
   				answerStr='<a href="'+urlRed+'"target="_self"  style="font-size:12px;margin-left:12px;">Be the first to answer</a><br/>';
   			}
   		    questionsStr=questionsStr+'<div id="user_question'+id+'" style="padding:10px 0px 10px 0px;border-bottom:'+border+';">'+
   			'<div><a href="'+urlRed+'"target="_self"  style="font-weight:bold;font-size:16px;color: #0088CC;">'+title+'</a></div>'+
   			contentStr+answerStr+'</div>';   			
        });
        $("#recent_questions_content_links").append(questionsStr);
        $("#page_loader_image").hide();
    }

	function setTrendingQuestions(responseObj){
		var obj = responseObj.content;
		for(i=0;i<obj.length;i++)
		{	
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
			$(".trending_questions_content_links").append('<p class="trending_questions_results_p" style="font-size: 16px;"><a href="'+urlRed+'"target="_self" >'+obj[i].title+'</a></p>')
		}			
		if(trendingPageIndex==0 && obj.length <=1){
			$(".trending_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>'); 
		}
		if(responseObj.lastPage==false){
			$("#more_trending_questions").show();
		}
		else{
			$("#more_trending_questions").hide();
		}
	} 