var BASE_URL = location.protocol + "//" + document.location.host + window.location.pathname;
var currentLoginUserObj=null;
var documentScope;
var usersPageSize=10;
var usersPageIndex=0;
var USERS_SHOW_DELAY=0;
var usersProcessFlag = false;


var VIEWED_DAYS_NUMBER=30;
var mostViewedQuestionsPageIndex=0;
var mostViewedQuestionsPageSize=10;
var mostRecentQuestionsPageIndex=0;
var mostRecentQuestionsPageSize=10;

var flaggedQuestionsPageIndex=0;
var flaggedQuestionsPageSize=10;
var flaggedAnswersPageIndex=0;
var flaggedAnswersPageSize=10;


viewAllStatistics=true;
var MOST_VIEWED_DELAY=0;
showAllMostViewedQuestionsView=false;
showAllMostRecentQuestionsView=false;

var MOST_RECENT_DELAY=0;
var mostViewedProcessFlag = false;
var mostRecentProcessFlag = false;

var FLAGGED_QUESTIONS_DELAY=0;
showAllFlaggedQuestionsView=false;
var flaggedQuestionsProcessFlag = false;

var FLAGGED_ANSWERS_DELAY=0;
var flaggedAnswersProcessFlag = false;
showAllFlaggedAnswersView=false;

$(document).ready(function (){
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();	
	$(".main-container").scroll(function () {     	
		if ($(window).scrollTop() + $(window).height() > $(document).height() - 200) {        
			if(viewAllStatistics==false){
//				$("#page_loader_image").show();	
				if(showAllUsersView==true && usersProcessFlag==false){
					USERS_SHOW_DELAY=100;
					documentScope.showAllUsersInfo();
				}
				if(showAllMostViewedQuestionsView==true && mostViewedProcessFlag==false){
					MOST_VIEWED_DELAY=100;
					documentScope.showAllMostViewedQuestions();
				}
				if(showAllMostRecentQuestionsView==true && mostRecentProcessFlag==false){
					MOST_RECENT_DELAY=100;
					documentScope.showAllMostRecentQuestions();
				}
				if(showAllFlaggedQuestionsView==true && flaggedQuestionsProcessFlag==false){
					FLAGGED_QUESTIONS_DELAY=100;
					documentScope.showAllFlaggedQuestions();
				}
				if(showAllFlaggedAnswersView==true && flaggedAnswersProcessFlag==false){
					FLAGGED_ANSWERS_DELAY=100;
					documentScope.showAllFlaggedAnswers();
				}
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

	angular.module('admin-page', ['common-login']).
	config(['$locationProvider', function($locationProvider) {
		$locationProvider.html5Mode(true);
	}])
	.service('adminPageService',['$http', '$location', function($http, $location) {	
	return {		
			getTotalQuestionsCount:function(){
				$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?pageAt=0&pageSize=1'}).
					success(function (data, status, headers, config) {
						$("#total_questions_count").text(data.response.totalElements);
					
					}).
					error(function (data, status, headers, config) {
					 console.log(data);
					});
			},
			getTotalUnansweredQuestionsCount:function(){
				$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?showUnanswered=true&pageAt=0&pageSize=1'}).
				success(function (data, status, headers, config) {
					$("#total_unanswers_count").text(data.response.totalElements);
				}).
				error(function (data, status, headers, config) {
				 console.log(data);
				});
			},
			getTotalAnswersCount:function(){
				$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions/answers?pageAt=0&pageSize=1'}).
				success(function (data, status, headers, config) {
					$("#total_answers_count").text(data.response.totalElements);
				}).
				error(function (data, status, headers, config) {
				 console.log(data);
				});
			},
			getTotalCommentsCount:function(){
				$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions/answers/comments?pageAt=0&pageSize=1'}).
				success(function (data, status, headers, config) {
					$("#total_comments_count").text(data.response.totalElements);
				}).
				error(function (data, status, headers, config) {
				 console.log(data);
				});
			},
			getAllUsersInfo:function(){
				var promise=$http({  headers:header_encoding,method: 'GET',url: 'api/users?pageAt='+usersPageIndex+'&pageSize='+usersPageSize});
				return promise;
			},
			getMostViewedQuestions:function(){
				var promise=$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?showMostViewed=true&numberOfDays='+VIEWED_DAYS_NUMBER+'&pageAt='+mostViewedQuestionsPageIndex+'&pageSize='+mostViewedQuestionsPageSize});
				return promise;
			},
			getMostRecentQuestions:function(){
				var promise=$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?showMostRecent=true&pageAt='+mostRecentQuestionsPageIndex+'&pageSize='+mostRecentQuestionsPageSize});
				return promise;
			},
			getFlaggedQuestions:function(){
				var promise=$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?showFlagged=true&pageAt='+flaggedQuestionsPageIndex+'&pageSize='+flaggedQuestionsPageSize});
				return promise;
			},
			getFlaggedAnswers:function(){
				var promise=$http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions/answers?showFlagged=true&pageAt='+flaggedAnswersPageIndex+'&pageSize='+flaggedAnswersPageSize});
				return promise;
			}
		};
	}])
	.controller('adminPageCtrl',
			[ '$scope', 'adminPageService', function($scope, adminPageService) {
	
		var userObj = null;
		if($("#user_id").text()!=undefined && $("#user_id").text()!=null && $("#user_id").text()!=""){
			userObj= getUserProfileById($("#user_id").text());
		}
		currentLoginUserObj =userObj;
		documentScope = $scope;
		$scope.initFunctionCall = function(){
			//this.showAllTrendingQuestions();
			
			//showing total questions count
			adminPageService.getTotalQuestionsCount();
			
			//showing total unanswered questions count
			adminPageService.getTotalUnansweredQuestionsCount();
			
			//showing total answers count
			adminPageService.getTotalAnswersCount();
			
			//showing total comments count
			adminPageService.getTotalCommentsCount();
			
			// showing all users info			
			this.showAllUsersInfo();
			
			// showing most viewed question
			this.showAllMostViewedQuestions();
			
			// showing most recent question
			this.showAllMostRecentQuestions();
			
			// showing flagged questions
			this.showAllFlaggedQuestions();
			
			// showing flagged answers
			this.showAllFlaggedAnswers();
			
		},	
		$scope.showAllUsersInfo = function(){
			usersProcessFlag=true;
			setTimeout(function (){			
				var usersPromise = adminPageService.getAllUsersInfo();
				usersPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setUsersInfo(data.response);
						usersPageIndex=usersPageIndex+1;
						$("#total_users_count").text(data.response.totalElements);
						if(data.response.lastPage==false){
							usersProcessFlag=false;
						}
						else{
							usersProcessFlag=true;
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
				.then(function (response) {
				});
			},USERS_SHOW_DELAY);
		},
		$scope.showAllMostViewedQuestions = function(){
			mostViewedProcessFlag=true;
			setTimeout(function (){
				var questionsPromise = adminPageService.getMostViewedQuestions();
				questionsPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setMostViewedQuestions(data.response);
						mostViewedQuestionsPageIndex=mostViewedQuestionsPageIndex+1;
						if(data.response.lastPage==false){
							mostViewedProcessFlag=false;
						}
						else{
							mostViewedProcessFlag=true;
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
				.then(function (response) {
				});
			},MOST_VIEWED_DELAY);	
		},
		$scope.showAllMostRecentQuestions = function(){
			mostRecentProcessFlag=true;
			setTimeout(function (){
				var questionsPromise = adminPageService.getMostRecentQuestions();
				questionsPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setMostRecentQuestions(data.response);
						mostRecentQuestionsPageIndex=mostRecentQuestionsPageIndex+1;
						if(data.response.lastPage==false){
							mostRecentProcessFlag=false;
						}
						else{
							mostRecentProcessFlag=true;
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
				.then(function (response) {
				});
			},MOST_RECENT_DELAY);	
		},
		$scope.showAllFlaggedQuestions = function(){
			flaggedQuestionsProcessFlag=true;
			setTimeout(function (){
				var questionsPromise = adminPageService.getFlaggedQuestions();
				questionsPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setFlaggedQuestions(data.response);
						flaggedQuestionsPageIndex=flaggedQuestionsPageIndex+1;
						if(data.response.lastPage==false){
							flaggedQuestionsProcessFlag=false;
						}
						else{
							flaggedQuestionsProcessFlag=true;
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
				.then(function (response) {
				});
			},FLAGGED_QUESTIONS_DELAY);	
		},
		$scope.showAllFlaggedAnswers = function(){
			flaggedAnswersProcessFlag=true;
			setTimeout(function (){
				var questionsPromise = adminPageService.getFlaggedAnswers();
				questionsPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						setFlaggedAnswers(data.response);
						flaggedAnswersPageIndex=flaggedAnswersPageIndex+1;
						if(data.response.lastPage==false){
							flaggedAnswersProcessFlag=false;
						}
						else{
							flaggedAnswersProcessFlag=true;
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
				.then(function (response) {
				});
			},FLAGGED_ANSWERS_DELAY);	
		}
	} ]);
	function setUsersInfo(responseObj){
		var usersStr="";
		var obj=responseObj.content;
		for(i=0;i<obj.length;i++){
			var border="1px solid #E0E0E0";
			var urlRed="profile?user="+obj[i].id;
			var str="";
			var title=obj[i].profile.firstname+", "+obj[i].profile.lastname+" ("+obj[i].profile.email+")";
			var date1 = new Date(obj[i].createdOn.replace(/-/g, '/').split('.')[0]);
			var createdOn = date1.getUTCDate() + " " + getMonthNameByNumber(date1.getUTCMonth()) + ", " + date1.getUTCFullYear();
			var lastLoginOn="";
			if(obj[i].lastLoginOn==null || obj[i].lastLoginOn==""){
				lastLoginOn="none";
			}
			else{
				var date2 = new Date(obj[i].lastLoginOn.replace(/-/g, '/').split('.')[0]);
				lastLoginOn = date2.getUTCDate() + " " + getMonthNameByNumber(date2.getUTCMonth()) + ", " + date2.getUTCFullYear();
			}
        	var profilePic="";
			if (obj[i].profile.profileSmallPictureUrl == null || obj[i].profile.profileSmallPictureUrl == "") {
				profilePic = "img/default-user.png";
    	    }
			else {
				profilePic = obj[i].profile.profileSmallPictureUrl;
    	    }
			str='<div style="padding:10px 0px 10px 10px;border-bottom:'+border+';">'+
   			'<div><a href="'+urlRed+'" target="_self"  style="font-weight:bold;font-size:14px;color: #0088CC;">'+title+'</a></div>'+
   			'<div class="other_info" style="color:#003366;margin-top:5px;margin-left:12px;"><img src="'+profilePic+'" style="width:25px;height:25px;margin-right:10px;"></img>Joined phroogal on '+createdOn+' | Last access was '+lastLoginOn+'</div>'+
   			'</div>';			
			usersStr=usersStr+str;
			// for partial show
			if(responseObj.firstPage==true && i<5){
				$("#users_partial_data_container").append(str);
			}
        }
		//$("#users_partial_data_container").append(usersStr);
		$("#users_all_data_container").append(usersStr);
	}
	function setMostViewedQuestions(responseObj){
		var obj = responseObj.content;
		var questionStr="";
		for(i=0;i<obj.length;i++)
		{	
			var border="1px solid #E0E0E0";
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
			var content= obj[i].content;
    		var contentStr="";
    		var totalCommentCount=obj[i].totalCommentCount;
			var totalAnswerCount=obj[i].totalAnswerCount;
			var totalViewCount=obj[i].totalViewCount;
			var postedByUser=obj[i].postedByUser;
			
		    if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				content= $("#tempTextContainer").text();
   				contentStr='<div style="font-size: 14px;color:black;margin-left:12px;">'+content+'</div>';
   			}
    		if(obj[i].answered==true)
   			{
   				answerStr='<div style="font-size:12px;margin-left:12px;">'+
   							'<a style="color: #0088CC;" target="_self" href="'+urlRed+'">Views ('+totalViewCount+')</a><a style="color:#0088CC;margin-left:5px;" target="_self" href="'+urlRed+'">Answers ('+totalAnswerCount+')</a><a target="_self" style="margin-left:5px;color: #0088CC;" href="'+urlRed+'">Comments ('+totalCommentCount+')</a>'+
   							'</div>';
   			}
   			else{
   				answerStr='<a target="_self" href="'+urlRed+'" style="font-size:12px;margin-left:12px;">Be the first to answer</a><br/>';
   			}
    		var str='<div  style="padding:10px 0px;margin:0px 10px;border-bottom:'+border+'">'+
			 		'<div><a target="_self" style="font-weight:bold;font-size: 16px;color: #0088CC;" href="'+urlRed+'">'+obj[i].title+'</a></div>'+
			 		'<div style="color:black;font-size:11px;margin-left:12px;">Post By: '+postedByUser+'</div>'+contentStr+answerStr+'</div>'; 
    		questionStr = questionStr+str;
    		if(responseObj.firstPage==true && i<5){
    			$("#most_viewed_questions_partial_data_container").append(str);
    		}
		}		
		$("#most_viewed_questions_all_data_container").append(questionStr);
		$("#page_loader_image").hide();
	}
	function setMostRecentQuestions(responseObj){
		var obj = responseObj.content;
		var questionStr="";
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
    		var str='<div style="padding:10px 0px;margin:0px 10px;border-bottom:'+border+'">'+
			 		'<div><a target="_self" style="font-weight:bold;font-size: 16px;color: #0088CC;" href="'+urlRed+'">'+obj[i].title+'</a></div>'+
			 		contentStr+answerStr+'</div>';
    		questionStr = questionStr+str; 
    		if(responseObj.firstPage==true && i<5){
    			$("#most_recent_questions_partial_data_container").append(str);
    		}
		}		
		$("#most_recent_questions_all_data_container").append(questionStr);
	}
	
	function setFlaggedQuestions(responseObj){
		var obj = responseObj.content;
		var questionStr="";
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
    		var str='<div style="padding:10px 0px;margin:0px 10px;border-bottom:'+border+'">'+
			 		'<div><a target="_self" style="font-weight:bold;font-size: 16px;color: #0088CC;" href="'+urlRed+'">'+obj[i].title+'</a></div>'+
			 		contentStr+answerStr+'</div>';
    		questionStr = questionStr+str; 
    		if(responseObj.firstPage==true && i<5){
    			$("#flagged_questions_partial_data_container").append(str);
    		}
		}		
		$("#flagged_questions_all_data_container").append(questionStr);
	}
	
	function setFlaggedAnswers(responseObj){
    	var answersStr="";
    	var obj=responseObj.content;
    	for(i=0;i<obj.length;i++)
		{	
			var border="1px solid #E0E0E0";
			var id = obj[i].id;
			var title = obj[i].title;			
		    var content= obj[i].content;
		    var questionDocId=obj[i].questionDocId;
		    var questionTitle=obj[i].questionTitle;
		    questionTitle=questionTitle.replace(/%20/g," ");
		    questionTitle=questionTitle.replace(/%27/g,"'");
		    questionTitle=replacehyphenWithSpace(questionTitle);
		    var totalCommentCount=obj[i].totalCommentCount;		    
		    var link="question/"+questionDocId+"/"+replaceSpaceWithhyphen(questionTitle);
		    if(questionTitle.charAt(questionTitle.length-1)!="?")
			{
		    	questionTitle = questionTitle+"?";
			}
		    var urlRed=formQuestionDetailRedirectUrl(link);
//		    if(index==answersObj.length-1){
//		    	border="0px solid #E0E0E0";
//		    }
		    var contentStr="";
		    if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				var contenttxt= $("#tempTextContainer").text();
   				content= $("#tempTextContainer").html();
   				if(contenttxt.trim().length > 100){
   					var subText=contenttxt.trim().substr(0,100)+".....";
   					contentStr='<div style="font-size: 16px;color:black;clear:both"><div>'+subText+'&nbsp;<a href="'+urlRed+'"target="_self"  style="color: #0088CC">More</a></div></div>';
   				}
   				else{
   					contentStr='<div style="font-size: 16px;color:black;clear:both"><div>'+content+'</div></div>';
   				}
   			}
		    var questionTitleStr="";
   			if(questionDocId!=0 && questionTitle!=null){
   				questionTitleStr='<div style="clear:both;"><a href="'+urlRed+'" target="_self" style="font-size: 14px;color: #0088CC;">'+questionTitle+'</a></div>';
   	     	}
   			else{
   				questionTitleStr="";
   			}
   			var commentStr='<span><a style="font-size:12px;color: #0088CC;" href="'+urlRed+'"target="_self" >Comments ('+totalCommentCount+')</a></span>';
   			var str='<div id="user_answer'+id+'" style="padding:10px 0px 10px 10px;border-bottom:'+border+'">'+
   				contentStr+questionTitleStr+commentStr+'</div>';
   			answersStr=answersStr+str
   			if(responseObj.firstPage==true && i<5){
    			$("#flagged_answers_partial_data_container").append(str);
    		}
		}		
		$("#flagged_answers_all_data_container").append(answersStr);
                
   }
	var viewAllStatistics=true;
	var showAllMostViewedQuestionsView=false;
	var showAllMostRecentQuestionsView=false;
	var showAllUsersView=false;
	function leftCategoriesLinks(linkName,viewAllElm,viewAll)
	{
		showAllMostViewedQuestionsView=false;
		showAllMostRecentQuestionsView=true;
		
		$(".users_statistics_link_a").css('background-color','white');
		$(".questions_statistics_link_a").css('background-color','white');		
		$("."+linkName+"_link_a").css('background-color','#FFA824');
		if(viewAll=="true"){
			viewAllStatistics=false;
			$("#catogry_text_show_result_partial").hide();
			$("#catogry_text_show_result_all").show();
			$("#all_data_li_users_statistics").hide();
			$("#most_viewed_questions_data_container_li").hide();
			$("#most_recent_questions_data_container_li").hide();
			$("#flagged_questions_data_container_li").hide();
			$("#flagged_answers_data_container_li").hide();
			
			$("#"+viewAllElm).show();
			if(viewAllElm=="all_data_li_users_statistics"){
				showAllUsersView=true
				showAllMostViewedQuestionsView=false;
				showAllMostRecentQuestionsView=false;
				showAllFlaggedQuestionsView=false;
				showAllFlaggedAnswersView=false;				
			}
			else if(viewAllElm=="most_viewed_questions_data_container_li"){
				showAllMostViewedQuestionsView=true;
				showAllMostRecentQuestionsView=false;
				showAllFlaggedQuestionsView=false;
				showAllFlaggedAnswersView=false;
				showAllUsersView=false;
			}
			else if(viewAllElm=="flagged_questions_data_container_li"){
				showAllFlaggedQuestionsView=true;
				showAllFlaggedAnswersView=false;
				showAllMostViewedQuestionsView=false;
				showAllMostRecentQuestionsView=false;
				showAllUsersView=false;
			}
			else if(viewAllElm=="flagged_answers_data_container_li"){
				showAllFlaggedAnswersView=true;
				showAllFlaggedQuestionsView=false;
				showAllMostViewedQuestionsView=false;
				showAllMostRecentQuestionsView=false;
				showAllUsersView=false;
			}
			else{
				showAllMostViewedQuestionsView=false;
				showAllMostRecentQuestionsView=true;
				showAllFlaggedQuestionsView=false;
				showAllFlaggedAnswersView=false;
				showAllUsersView=false;
			}
		}
		else{
			viewAllStatistics=true;
			$("#catogry_text_show_result_partial").show();
			$("#catogry_text_show_result_all").hide();
			if(viewAllElm=="users_statistics"){
				$(".users_statistics_info").show();
				$(".questions_statistics_info").hide();
			}
			else{
				$(".questions_statistics_info").show();
				$(".users_statistics_info").hide();
			}
		}
		$(".show_all_catogory").show();		
	}
	var viewAllExternalLinks=true;
	function showAllCategories()
	{
		$(".users_statistics_link_a").css('background-color','white');
		$(".questions_statistics_link_a").css('background-color','white');
		$("#catogry_text_show_result_partial").show();
		$("#catogry_text_show_result_all").hide();
		$(".show_all_catogory").hide();
		$(".users_statistics_info").show();
		$(".questions_statistics_info").show();
		viewAllStatistics=true;
		showAllMostViewedQuestionsView=false;
		showAllMostRecentQuestionsView=true;
	}