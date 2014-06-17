$(document).ready(function () {
	var elms=$(".nav-tabs").find("li a");
	console.log("tabs length ="+elms.length);
	for(i=elms.length;i>0;i--){
		var elm = $(elms[i-1]);
		var tabTitle=replaceSpaceWithhyphen(elm.text());
		elm.attr("id",tabTitle+"-a");
    	elm.css("width","75px");
		var baseUrl=getBaseUrlOfSite();
		var imageUrl=baseUrl+"img/loader.gif";
		$("#"+tabTitle+"-a").css('background-image', 'url(' + imageUrl + ')');
		$("#"+tabTitle+"-a").css('background-position', '96%');
		$("#"+tabTitle+"-a").css('background-repeat', 'no-repeat');
	}
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();
	if(getURLParameter("userWeb")!=null && getURLParameter("userWeb")=="true"){
		$("#user_profile_container_div").hide();
		$("#personal_website_frame_div").show();
    }
	else{
		$("#user_profile_container_div").show();
		$("#personal_website_frame_div").hide();
	}
 });    

var paramUserId;
$(document).ready(function () {
	$("#profile_image_container").hide();
	paramUserId = getURLParameter("user");
	var current_user_id = $("#user_id").text().trim();
	if(paramUserId!=null && paramUserId!=undefined && paramUserId!=current_user_id){
		$("#user_bio_edit").hide();
		$("#user_location_edit").hide();
		$("#user_picture_edit").hide();
		$("#user_personal_website_edit").hide();
		$("#user_college_edit").hide();
		$("#user_credit_union_edit").hide();
		$("#link_to_user_dashboard").hide();
	}	
});
var currentLoginUserObj=null;
var documentScope;
var answersPageSize=10;
var answersPageIndex=0;
var questionsPageSize=10;
var questionsPageIndex=0;
var answersSpinerFlag = false;
var totalAnswersCount=0;

function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var BASE_URL = location.protocol + "//" +"" + document.location.host + window.location.pathname;
angular.module('profile', ['common-login']).
  config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
  }])
  .directive('tabs',function() {
    return {
        'restrict': 'E',
        'transclude': true,
        'scope': {},
        'controller': ['$scope','$element',function($scope, $element) {
            var panes = $scope.panes = [];

            $scope.select = function(pane) {
                angular.forEach(panes, function(pane) {
                    pane.selected = false;
                });
                pane.selected = true;
            }
            this.addPane = function(pane) {
                if (panes.length == 0)
                    $scope.select(pane);
                panes.push(pane);
            }
        }],
        'template':
        '<div class="tabbable">' +
        '<ul class="nav nav-tabs">' +
        '<li ng-repeat="pane in panes" ng-class="{active:pane.selected}">'+
        '<a href="" ng-click="select(pane)">{{pane.title}}</a>' +
        '</li>' +
        '</ul>' +
        '<div class="tab-content" ng-transclude></div>' +
        '</div>',
        'replace': true
    };
  })
  .directive('pane',function() {
    return {
        'require': '^tabs',
        'restrict': 'E',
        'transclude': true,
        'scope': { title: '@' },
        'link':function(scope, element, attrs, tabsCtrl) {
            tabsCtrl.addPane(scope);
        },
        'template':
        '<div class="tab-pane" ng-class="{active: selected}" ng-transclude>' +
        '</div>',
        'replace': true
    };
  })
  .service('profileService',['$http', '$location', function($http, $location) {
	return {
		
		getUserObj:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/users/user-'+_id});
		    return promise;			  	
		},
		getUserPostedQuestions:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions?postBy='+_id+'&pageAt='+questionsPageIndex+'&pageSize='+questionsPageSize});
		    return promise;			  	
		},
		getUserPostedAnswers:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions/answers?postBy='+_id+'&pageAt='+answersPageIndex+'&pageSize='+answersPageSize});
		    return promise;			  	
		},
		getTrendingQuestion:function(){
			var promise = $http({headers:header_encoding, method: 'GET',url: 'api/posts/questions?showTrending=true&pageAt='+trendingPageIndex+'&pageSize='+trendingPageSize});
			return promise;
		},
		getUserPostedReviews:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/reviews?postBy='+_id});
		    return promise;			  	
		},
		getAllTags:function(){
			var promise = $http({headers:header_encoding, method: 'GET',url: 'api/tags'});
			return promise;
		}
	};
}])
.controller('profileCtrl',
		[ '$scope', 'profileService', function($scope, profileService) {
			var user_id=$("#user_id").text();
			var passedId ;
			var userParam=getURLParameter("user");
			if(userParam!=null && userParam!=undefined && userParam!="" ){
				passedId = userParam;
			}
			else{
				passedId = user_id;
			}					
			$scope.fullName = false;
			$scope.personalWebsitePageUrl="";
			// asign scope to variable
			documentScope = $scope;
			$scope.resendMail="";
			$scope.getLoginUserObj = function() {
				var userObjPromise = profileService.getUserObj(passedId);
				userObjPromise.success(function (data) {
					 $scope.profileData = data.response;
					 var fname=data.response.profile.firstname.charAt(0).toUpperCase()+data.response.profile.firstname.substring(1);
					 var lname="";
					 if(data.response.profile.lastname != null){
						 lname=data.response.profile.lastname.charAt(0).toUpperCase()+data.response.profile.lastname.substring(1); 
						 $scope.fullName = fname+" "+lname; 
					 }
					 else{
						 $scope.fullName = fname;
					 }
					 $scope.bio = data.response.profile.bio;
					 if($scope.bio==null || $scope.bio==""){
						 $scope.bio="No bio available";
					 }
					 $scope.personalWebsite = data.response.profile.websiteUrl;
					 if($scope.personalWebsite==null){
						 $scope.personalWebsite="No website entered";
						 $scope.personalWebsitePageUrl="";
					 }
					 else{
						 $scope.personalWebsitePageUrl;
						 $scope.personalWebsiteURL = addhttp($scope.personalWebsite);
						 $("#website-link-text").hide();
					 }
					 $scope.lifeStyleGoal = data.response.profile.lifeStyleGoal;
					 if($scope.lifeStyleGoal==null){
						 $scope.lifeStyleGoal="No lifestyle goal provided";
					 }
					 $scope.college = data.response.profile.college;
					 if($scope.college==null){
						 $scope.college="";
					 }					 
					 $scope.creditUnion = data.response.profile.crUnion;
					 if($scope.creditUnion==null){
						 $scope.creditUnion="";
					 }
					 if(data.response.profile.location!=null){
						 $scope.location = data.response.profile.location.displayName;
						 $scope.usrLocation = data.response.profile.location.displayName;
					 }					 
					 if(data.response.profile.profilePictureUrl=="" || data.response.profile.profilePictureUrl==null || data.response.profile.profilePictureUrl==undefined){
						 $(".login-user-image").attr("src","img/default-user.png"); 
			    	 }
					 else{
			    			 var isDefaultImage=data.response.profile.profilePictureUrl.indexOf("twimg.com");
				    		 if(isDefaultImage!=-1){
				    			 var res = data.response.profile.profilePictureUrl.split("_normal");
				    			 if(res.length>1)
				    				 $(".login-user-image").attr("src",res[0]+res[1]);
				    			 else
				    				 $(".login-user-image").attr("src",res[0]);	 
				    		 }
				    		 else{
				    			 $(".login-user-image").attr("src",data.response.profile.profilePictureUrl);
				    		 }
			    	 }
					 if(data.response.createdOn!=null && data.response.createdOn!="" && data.response.createdOn!=""){
						 var date =new Date(data.response.createdOn.replace(/-/g,'/').split('.')[0]);
						 $scope.userCreatedOn = getMonthNameByNumber(date.getUTCMonth())+" "+ date.getUTCFullYear();
					 }
					 else{
						 $scope.userCreatedOn="Feb 2014";
					 }
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				var userQuestionsObjPromise = profileService.getUserPostedQuestions(passedId);
				userQuestionsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						if(data.response!=null && data.response.content.length>0){
							$("#questions_given_count").text(data.response.totalElements);
						}
						else{
							$("#questions_given_count").text(0);							
						}
						//setQuestionToTab(data.response);				
					}
				})
				.error(function (data) {
					console.log("In error "+data);	
					$("#questions_given_count").text(0);
				})
			    .then(function (response) {
			    });
				// showing user answers
				this.showAllUserAnswers();
			},
			// getting user answers			
			$scope.showAllUserAnswers=function(){
				if(answersSpinerFlag){
					$("#user_answers_loader_image").show();
					$("#more_user_answers").hide();
				}
				var userAnswersObjPromise = profileService.getUserPostedAnswers(passedId);
				userAnswersObjPromise.success(function (data) {
					/*(if(data.response!=null){
						totalAnswersCount=totalAnswersCount+data.response.length;
						$("#answers_given_count").text(data.response.length);
					}
					else{
						$("#answers_given_count").text(0);
					}*/
					if(data.response.content!=null && data.response.content.length > 0 ){
						$("#answers_given_count").text(data.response.totalElements);
						setAnsersToTab(data.response.content);
						answersPageIndex=answersPageIndex+1;
						answersSpinerFlag=true;						
					}
					else{
						$("#user_answers_loader_image").hide();
						$("#more_user_answers").hide();
						$("#answers_given_count").text(0);
						tabsSpinnerHide("Answers-a");
						$("#user-no-answer_found").show();
					}
										
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
			};		
			
		} ]);
	function setAnsersToTab(answersObj){
		var ansersStr="";
		$.each(answersObj, function(index, element) {
			var border="1px solid #E0E0E0";
			var id = element.id;
			var title = element.title;			
		    var content= element.content;
		    var questionDocId=element.questionDocId;
		    var questionTitle=element.questionTitle;
		    questionTitle=questionTitle.replace(/%20/g," ");
		    questionTitle=questionTitle.replace(/%27/g,"'");
		    questionTitle=replacehyphenWithSpace(questionTitle);
		    var totalCommentCount=element.totalCommentCount;		    
		    var link="question/"+questionDocId+"/"+replaceSpaceWithhyphen(questionTitle);
		    if(questionTitle.charAt(questionTitle.length-1)!="?")
			{
		    	questionTitle = questionTitle+"?";
			}
		    var urlRed=formQuestionDetailRedirectUrl(link);
		    if(index==answersObj.length-1){
		    	border="0px solid #E0E0E0";
		    }
		    var contentStr="";
		    if(content!=null && content!="")
			{
				$("#tempTextContainer").html(content);
				var contenttxt= $("#tempTextContainer").text();
				content= $("#tempTextContainer").html();
				if(contenttxt.trim().length > 100){
					var subText=contenttxt.trim().substr(0,100)+".....";
					contentStr='<div style="font-size: 16px;color:black;clear:both"><div>'+subText+'&nbsp;<a href="'+urlRed+'"target="_self"  style="color: #0088CC;">More</a></div></div>';
				}
				else{
					contentStr='<div style="font-size: 16px;color:black;clear:both"><div>'+content+'</div></div>';
				}
			}
		    var date =new Date(element.createdOn.replace(/-/g,'/').split('.')[0]);
			var amPm;
			if(date.getHours()>12){
				amPm="PM";
			}
			else{
				amPm="AM";
			}			
			var postTime = date.getUTCDate()+" "+getMonthNameByNumber(date.getUTCMonth())+" "+ date.getUTCFullYear()+" "+addLeadingZero((date.getHours() + 11) % 12 + 1)+":"+addLeadingZero(date.getMinutes())+" "+amPm;
			var questionTitleStr="";
			if(questionDocId!=0 && questionTitle!=null){
				questionTitleStr='<div style="clear:both;"><a href="'+urlRed+'" target="_self" style="font-size: 14px;color: #0088CC;">'+questionTitle+'</a></div>';
	     	}
			else{
				questionTitleStr="";
			}
			var commentStr='<span><a style="font-size:12px;color: #0088CC;" href="'+urlRed+'"target="_self" >Comments ('+totalCommentCount+')</a></span>';
			ansersStr=ansersStr+'<div id="user_answer'+id+'" style="padding:10px 0px 10px 10px;border-bottom:'+border+'">'+
			contentStr+questionTitleStr+commentStr+
			'<span style="margin-left:12px;font-size:12px;color:gray;">Posted On: '+postTime+'</span></div>';   			
	    });
	    $("#answers").append(ansersStr);
	    if(answersObj.length==answersPageSize){
			$("#more_user_answers").show();
			$("#user_answers_loader_image").hide();
		}
		else{
			$("#more_user_answers").hide();
			$("#user_answers_loader_image").hide();
		}
		if(answersPageIndex==0){
			tabsSpinnerHide("Answers-a");
		}        
   }
   function changeQuestionAnonymousType(id,postType){
	   documentScope.updateUserQuestionAnonymousType(id,postType); 
   }
   
