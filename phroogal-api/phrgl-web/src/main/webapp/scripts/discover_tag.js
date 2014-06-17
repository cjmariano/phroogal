var BASE_URL = location.protocol + "//" + document.location.host
		+ window.location.pathname;
$(document).ready(function (){
	expandTextarea("rightAddNewQuestionTitle");
	expandTextarea("addNewQuestionRightDetail");
	
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();	
});
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
var trendingPageSize=5;
var trendingPageIndex=0;
var discoverTagsPageSize=10;
var discoverTagsPageIndex=0;
	angular.module('search-discover-tags', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('searchDiscoverTagService',['$http', '$location', function($http, $location) {		
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
				var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/tags'});
				return promise;
			},
			getTrendingQuestion:function(){
				var promise = $http({ headers:header_encoding, method: 'GET',url: 'api/posts/questions?showTrending=true&pageAt='+trendingPageIndex+'&pageSize='+trendingPageSize});
				return promise;
			},
			getDiscoverTags:function(){
				var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/tags?&sortByQuestionsTagged=true&pageAt='+discoverTagsPageIndex+'&pageSize='+discoverTagsPageSize});
				return promise;
			},
			addTagByUserId:function(tag,_id){
				var tagsData ={
								"name":tag
							   };
				var promise = $http({   headers:header_encoding,method: 'POST',url: 'api/tags/user-'+_id,data:tagsData});
				return promise;
			}
		};
	}])
	.controller('searchDiscoverTagCtrl',
			[ '$scope', 'searchDiscoverTagService', function($scope, searchDiscoverTagService) {  
				
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
				var allCategoryPromise = searchDiscoverTagService.getAllTags();
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
			this.showAllDiscoverTags();
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
			
			var questionObjPromise = searchDiscoverTagService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllCategories,isAnonymousFlag);
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
			var userObjPromise = searchDiscoverTagService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		$scope.showAllDiscoverTags = function(){
			var allDiscoverTagsPromise = searchDiscoverTagService.getDiscoverTags();
			allDiscoverTagsPromise.success(function (data) {					 
				if(data.status=="SUCCESS"){
					setDiscoverTags(data.response);
					discoverTagsPageIndex=discoverTagsPageIndex+1;
				}				
			 })
			.error(function (data) {
				console.log("In error "+data);
		     })
		    .then(function (response) {			        	
		    });			
		},	
		$scope.showAllTrendingQuestions = function(){
			var allTrendingQuestionsPromise = searchDiscoverTagService.getTrendingQuestion();
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
	function setDiscoverTags(responseObj){	
		var data = responseObj.content;
	    var discoverTagsStr="";
		var tagsArray= data;
		var countZeroFlag = false;
		for(i=0;i<tagsArray.length;i++){
			var tag = tagsArray[i].name;
			var count = tagsArray[i].totalNumQuestionsTagged;
			var tagVar = tag;
			tagVar = tagVar.replace("/","+");
			tagVar = tagVar.replace(" ","-");
			var tagUrl = getBaseUrlOfSite()+"question/tag/"+replaceSpaceWithhyphen(tag.trim());	
		    var str="";
		    
			if(parseInt(count)){
				str='<div style="float:left;padding:3px 10px;"><a style="background-color: #C2D7E7;border-radius: 3px 3px 3px 3px;color: #19558D;display: inline-block;padding:6px;text-decoration: none;" class="question_tags_category"  target="_self" href="'+tagUrl+'">'+tag+'<span style="color:gray">  X  </span><span style="color:black">'+count+'</span></a></div>';
			}
			else{
				$("#more_discover-tags").hide();
				countZeroFlag=true;
			}
			discoverTagsStr=discoverTagsStr+str;
		}
		$("#discover-tags_content_links").append(discoverTagsStr);
		if(tagsArray.length >=0 && tagsArray.length>=discoverTagsPageSize && countZeroFlag==false){
			$("#more_discover-tags").show();
		}
		else{
			$("#more_discover-tags").hide();
		}
	}
	function setTrendingQuestions(responseObj){
		var obj=responseObj.content;
		var trendingQuestionStr="";
		for(i=0;i<obj.length;i++)
		{	
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
    		trendingQuestionStr=trendingQuestionStr+'<p class="trending_questions_results_p" style="font-size: 16px;"><a href="'+urlRed+'"target="_self" >'+obj[i].title+'</a></p>';
		}
		$(".trending_questions_content_links").append(trendingQuestionStr);
		if(responseObj.firstPage == true && obj.length <=0){
			$(".trending_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>'); 
		}
		if (responseObj.lastPage == false) {
	        $("#more_trending_questions").show();
	    } 
	    else {
	        $("#more_trending_questions").hide();
	    }
	}
		