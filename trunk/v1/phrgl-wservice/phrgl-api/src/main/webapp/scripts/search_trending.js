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
				documentScope.showAllTrendingQuestions();
			}
         }
	});
});
var DELAY=0;
var processFlag = false;
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
var currentLoginUserObj="";
var trendingPageSize=10;
var trendingPageIndex=0;
	angular.module('search-trending', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('searchTrendingService', ['$http', '$location', function($http, $location) {	
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
				var promise = $http({headers:header_encoding,  method: 'GET',url: 'api/tags'});
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
	.controller('searchTrendingCtrl',
			[ '$scope', 'searchTrendingService', function($scope, searchTrendingService) {		
		var userObj = null;
		if($("#login_user_id").text()!=undefined && $("#login_user_id").text()!=null && $("#login_user_id").text()!=""){
			userObj= getUserProfileById($("#login_user_id").text());
		}		
		currentLoginUserObj =userObj;				
		$scope.categoryNames=[];
		documentScope = $scope;
		$scope.resendMail="";
		$scope.rightAddNewQuestionEnableEditor = false;
		$scope.initFunctionCall = function(){
			var tagsJson = $.cookie("tagsArray");
			var tagsArray = $.parseJSON(tagsJson);
			if(tagsArray == null || tagsArray == undefined || tagsArray.length <=0){
				var allCategoryPromise = searchTrendingService.getAllTags();
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
			this.showAllTrendingQuestions();
		},	
		$scope.addNewRightQuestionButton=function(){
			if(currentLoginUserObj==null){
				showLoginByQuestionRedirect();
			    return;
		    }
			if(rightAllCategories.length==0){				
				$("#question_tags_alert_message").text(TAGS_LOWER_LIMIT_MESSAGE);
				$("#question_tags_alert_message").show();
				return;
			}
			var isAnonymous = $('#postAsAnonymousRight').is(':checked');						
			var isAnonymousFlag =isAnonymous;			
			var content = $scope.rightNewQuestionDescription;
			if(content!="" && content !=null ){
				content=capitaliseFirstLetter(content);
			}
			
			setLoaderImageOnButton('right_add_new_question_button');
			
			
			var questionObjPromise = searchTrendingService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllCategories,isAnonymousFlag);
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
			var userObjPromise = searchTrendingService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		$scope.showAllTrendingQuestions = function(){
			processFlag=true;
			setTimeout(function (){
				var allTrendingQuestionsPromise = searchTrendingService.getTrendingQuestion();
				allTrendingQuestionsPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setTrendingQuestions(data.response);
						trendingPageIndex=trendingPageIndex+1;
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
		};
	} ]);
	
	function setTrendingQuestions(responseObj){
		var obj = responseObj.content;
		var trendingQuestionStr="";
		for(i=0;i<obj.length;i++)
		{	
			var border="1px solid #E0E0E0";
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
			var content= obj[i].content;
    		var contentStr="";
    		var totalCommentCount=obj[i].totalCommentCount;
			var totalAnswerCount=obj[i].totalAnswerCount;
		    if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				content= $("#tempTextContainer").text();
   				contentStr='<div style="font-size: 14px;color:black;margin-left:12px;">'+content+'</div>';
   			}
    		if(obj[i].answered==true)
   			{
   				answerStr='<div style="font-size:12px;margin-left:12px;">'+
   							'<a style="color: #0088CC;" target="_self" href="'+urlRed+'">Answers ('+totalAnswerCount+')</a><a target="_self" style="margin-left:5px;color: #0088CC;" href="'+urlRed+'">Comments ('+totalCommentCount+')</a>'+
   							'</div>';
   			}
   			else{
   				answerStr='<a target="_self" href="'+urlRed+'" style="font-size:12px;margin-left:12px;">Be the first to answer</a><br/>';
   			}
			trendingQuestionStr = trendingQuestionStr+'<div class="trending_questions" style="padding:10px 0px;margin:0px 10px;border-bottom:'+border+'">'+
    							 '<div><a target="_self" style="font-weight:bold;font-size: 16px;color: #0088CC;" href="'+urlRed+'">'+obj[i].title+'</a></div>'+
		  					     contentStr+answerStr+'</div>'; 
		}
		$(".trending_questions_content_links").append(trendingQuestionStr);
		$("#page_loader_image").hide();
	}
	

	