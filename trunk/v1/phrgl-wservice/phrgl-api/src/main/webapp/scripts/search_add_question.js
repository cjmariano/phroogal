var BASE_URL = location.protocol + "//" + document.location.host
		+ window.location.pathname;
$(document).ready(function (){
	expandTextarea("middelAddNewQuestionTitle");
	expandTextarea("middleDetailsContent");
	
	// TE Jquery plugin
	$('.TEeditor').jqte();
	var jqteStatus = false;
	$('.TEeditor').jqte({"status" : jqteStatus});
	if(currentLoginUserObj!=null){
		$("#middelAddNewQuestionTitle").focus();
    }
	
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();

});
function  middleDetailsContentClick (){
	if(currentLoginUserObj!=null){
		var jqteStatus = true;
		$('.TEeditor').jqte({"status" : jqteStatus});
		$(".jqte_editor").focus();
		$(".jqte_editor").css('font-size','14px');	
	}
	else{
		showLoginByQuestionRedirect();
	}
}

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
var currentLoginUserObj="";
var trendingPageSize=5;
var trendingPageIndex=0;
	angular.module('search-social', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('addQuestionService',['$http', '$location', function($http, $location) {		
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
				var promise = $http({headers:header_encoding, method: 'POST',url: 'api/tags/user-'+_id,data:tagsData});
				return promise;
			}
	     };
	}])
	.controller('searchSocialCtrl',
			[ '$scope', 'addQuestionService', function($scope, addQuestionService) {		
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
				var allCategoryPromise = addQuestionService.getAllTags();
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
		$scope.addNewMiddleQuestionButton = function(){
			if(currentLoginUserObj==null){
				   showLoginByQuestionRedirect();
			     	return;
		    }			
//			if(middleAllCategories.length==0){				
			if(midAllCategories.length==0){			
				$("#question_tags_alert_message").text(TAGS_LOWER_LIMIT_MESSAGE);
				$("#question_tags_alert_message").show();
				return;
			}
			var isAnonymous = $('#postAsAnonymousMiddle').is(':checked');						
			var isAnonymousFlag =isAnonymous;
			var content = $(".jqte_editor").html();
		    if(content!=undefined){
				if( content.trim().length - content.trim().lastIndexOf("<br>")==4){
					content = content.substr(0,content.trim().lastIndexOf("<br>"))
				}
			}
			$("#dummyQuestionContentText").html(content);
			if(content!="" && content !=null ){
		        var element = $("#dummyQuestionContentText");
		        var setFirstCapital = false;
		        if(element[0].childNodes[0].nodeType==3 && element[0].childNodes[0].nodeValue!=""){
		        	 element[0].childNodes[0].nodeValue = capitaliseFirstLetter(element[0].childNodes[0].nodeValue.trim());
		        	 setFirstCapital=true;
		        }
		        elements = element.find( "*" );
		        for( i=0; i < elements.length; i++ ){
		             elements[i].style.fontSize="14px";
		             if(elements[i].tagName=="a" ||  elements[i].tagName=="A"){
		            	 elements[i].style.marginLeft="5px";
		      	   	 }
		      	   	 else
		      	   	 {
		      		   elements[i].style.color = "black";
		      	   	 }
		             if(setFirstCapital==false){
		      		   if(elements[i].childNodes[0].nodeType==3){
		      	      	 elements[i].childNodes[0].nodeValue = capitaliseFirstLetter(elements[i].childNodes[0].nodeValue.trim());
		      	      	 setFirstCapital=true;
		      		   }
		      	     }
		             elements[i].style.fontFamily="Helvetica Neue,Helvetica,Arial,sans-serif";
		        }
	        }
			setLoaderImageOnButton('center_add_new_question_button');
		    content = $("#dummyQuestionContentText").html();
			var questionObjPromise = addQuestionService.addNewQuestion($scope.middleNewQuestionTitle,content,midAllCategories,isAnonymousFlag);
			questionObjPromise.success(function (data) {					 
				console.log("in success"+data);
				if(data.status=="SUCCESS"){
					$(".middle_selected_categories").empty();
					$(".mid_questin_add_right-controls").show();
					$(".middle_selected_categories_non_exist_tag_error").hide();
					$('#postAsAnonymousMiddle').attr('checked',false);
					midAllCategories=[];
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
					removeLoaderImageOnButton('center_add_new_question_button');
				}
		
			})
			.error(function (data) {
		          console.log("In error "+data);
		          $scope.middleNewDescription="";
				  $scope.middleNewQuestion="";
				  removeLoaderImageOnButton('center_add_new_question_button');
		     })
		    .then(function (response) {			        	
		    });			
		},
		$scope.addUserTag = function(tagVal,user_id) {
			var userObjPromise = addQuestionService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		$scope.showAllTrendingQuestions = function(){
			var allTrendingQuestionsPromise = addQuestionService.getTrendingQuestion();
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
		
   