$(document).ready(function () {
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();
	if(getURLParameter("userWeb")!=null && getURLParameter("userWeb")=="true"){
		$("#user_profile_container_div").hide();
		$("#personal_website_frame_div").show();
    }
	else
	{
		$("#user_profile_container_div").show();
		$("#personal_website_frame_div").hide();
	}
	// adding tags
	$('#userAddTagInput').keyup(function(e) {
		if (e.which === 188) {
	    	var val = $('#userAddTagInput').val();
	    	val=val.trim();
    		if(val=="," || val=="" || val==null || val==undefined){
    			$('#userAddTagInput').val("");
	    		return;
	    	}
	    	var found = checkTagsAvalable($('#userAddTagInput').val().substr(0,$('#userAddTagInput').val().length-1));
	    	if(found>=0){
	    		$('#userAddTagInput').val("");
	    	}
	    	else{
	    		$('#userAddTagInput').val($('#userAddTagInput').val().substr(0,$('#userAddTagInput').val().length-1));
	    		documentScope.addUserTag();
	    	}
	    }
	    if (e.which === 13) {
	    	var val = cammelCaseOfString($('#userAddTagInput').val());
	    	val=val.trim();
	    	if(val=="" || val==null || val==undefined){
	    		return;
	    	}
	    	documentScope.addUserTag();
	    }
	});			
 });
var typeHeadSelectedObj;
$(function() {
	$('#location-search').typeahead([ {
	name: 'location-search',
	minLength: 3,
	items: 5,
	valueKey: 'name',
	limit: '20',
	remote: {
	   url: 'api/locations/query?keyword=%QUERY',
	   filter: function(data) {
		    if (data.status == 'FAILURE' || data.response.length == 0)
		   	{
		    	return "";
		   	}
		   	else{
		   	   	$(".add-location-content").hide();
		       	return data.response;
		    }
	   }
	},		 
	template: '<span><p>{{name}}</p></span>',
	engine: Hogan,
	header: '<h4 style="display:none">Locations</h4>'
	}		
	]).on('typeahead:selected', function($e, data) {
		console.log("selected question id is :"+data.id);
		typeHeadSelectedObj=data;
	});
});
$(function() {
	$('#college-search').typeahead([ 
    {		
 		name: 'colleges',
 		minLength: 3,
 		items: 5,
 		valueKey: 'name',
 		limit: '20',
 		remote: {
 		   url:'api/posts/colleges/query?keyword=%QUERY',
 		   filter: function(data) {
 		   	if (data.status == 'FAILURE') {
 		   		return '';
 		   	};
 		       return data.response;
 		   }
 		},		 
 		template: '<span><p>{{name}}</p></span>',
 		engine: Hogan,
 		header: '<h4 style="margin-left:15px;">Colleges</h4>'
 	},
 	]).on('typeahead:selected', function($e, data) {				
 	});
});
$(function() {
	$('#credit-union-search').typeahead([ 
	    {		
		name: 'creditUnions',
		minLength: 3,
		items: 5,
		valueKey: 'name',
		limit: '20',
		remote: {
		   url:'api/posts/creditUnions/query?keyword=%QUERY',
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response;
		   }
		},		 
		template: '<span><p>{{name}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;">Credit Unions</h4>'
	},
	]).on('typeahead:selected', function($e, data) {				
   });
});
function addNewLocationToDB(){
	 var scope = $('.question-to-new-answer-add-control').scope();
	 scope.addNewLocationSaveToDB(getURLParameter('question_id'),content);
     $("#question-to-new-answer-add").show();		 
}
var paramUserId;
$(document).ready(function () {
	$("#profile_image_container").hide();
	paramUserId = getURLParameter("user");
	var current_user_id = $("#user_id").text().trim();
	if(current_user_id==null || current_user_id==undefined || current_user_id==""){
		window.location.href=getBaseUrlOfSite();
	}		
	if(paramUserId!=null && paramUserId!=undefined && paramUserId!=current_user_id){
		$("#user_bio_edit").hide();
		$("#user_location_edit").hide();
		$("#user_picture_edit").hide();
		$("#user_personal_website_edit").hide();
		$("#user_college_edit").hide();
		$("#user_credit_union_edit").hide();
	}	
});
var currentLoginUserObj=null;
var documentScope;
var trendingPageSize=10;
var trendingPageIndex=0;
var socialQuestionPageSize=10;
var socialQuestionPageIndex=0;
var answersPageSize=10;
var answersPageIndex=0;
var questionsPageSize=10;
var questionsPageIndex=0;
var trendingObjArr = new Array();
var categoryString = new Array();

function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var inputPic;
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
var addAllTagsArray= new Array();
function setQuestionTags(value){
	$(".middle_selected_categories").show();
	$(".middle_selected_categories_empty_error").hide();
	if(addAllTagsArray.length >=5){
		$(".middle_question_tags_category_limit_error_alert").show();
		$(".middle_selected_categories_empty_error").hide();
		$(".middle_selected_categories_non_exist_tag_error").hide();
		return;
	}
	addAllTagsArray.push(value);
	var id = addAllTagsArray.length;
	$(".middle_selected_categories").append('<span  id="mid_question_tags_category'+id+'" class="question_tags_category">'+value+'<a class="topic_remove" onclick="removeTag(\''+value+'\',\''+id+'\')" href="">x</a></span>&nbsp;')
	$("#addSearchTag").attr("value","");
}
function removeTag(tags,id){
	addAllTagsArray.pop(tag);
	$(".middle_question_tags_category_limit_error_alert").hide();
	$(".middle_selected_categories_empty_error").hide();
	$(".middle_selected_categories_non_exist_tag_error").hide();
	
	$("#mid_question_tags_category"+id).remove()
	if(addAllTagsArray.length==0){
		$(".middle_selected_categories").hide();
	}
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

 //define flag to spinner show hide
var socialQuestionSpinerFlag = false;
var answersSpinerFlag = false;
var questionsSpinerFlag = false;
var BASE_URL = location.protocol + "//" + "" + document.location.host + window.location.pathname;
angular.module('profile', []).
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
		updateUsername:function(_firstname,_id){			
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.firstname",
				    "value": _firstname
			}];
			var promise = $http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id,  data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'} });
			return promise;
		},
		updateUserLocation:function(_locationRef, _locationDisplayName,_id){			
			var locationData;
			var locationData1 = {
				"locationRef" : _locationRef			
			};
			var locationData2 ={
				"displayName": _locationDisplayName
			};
			if(_locationRef!=null || _locationRef!=undefined){
				locationData = locationData1;
			}
			else{
				locationData = locationData2;
			}
			var promise = $http({ headers:header_encoding,method: 'POST', url: 'api/users/user-' + _id+"/location", data: locationData });
		    return promise;
		},
		updateUserBio:function(_bio, _id){			
			var userProfileData = [{
				"operation" : "REPLACE",
				"property": "profile.bio",
			    "value": _bio
			}];
			var promise=$http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });
			return promise;
		},
		updateUserLifeStyleGoal:function(_lifeStyleGoal, _id){			
			var userProfileData = [{
				"operation" : "REPLACE",
				"property": "profile.lifeStyleGoal",
			    "value":_lifeStyleGoal
			}];
			var promise=$http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });
			return promise;
		},
		updateUserPersonalWebsite:function(_websiteUrl, _id){			
			var userProfileData = [{
				"operation" : "REPLACE",
				"property": "profile.websiteUrl",
			    "value": _websiteUrl
			}];
			var promise=$http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });
			return promise;
		},
		updateUserCollege:function(_college, _id){			
			var userProfileData = [{
				"operation" : "REPLACE",
				"property": "profile.college",
			    "value": _college
			}];
			var promise=$http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });
			return promise;
		},
		updateUserCreditUnion:function(_crUnion, _id){			
			var userProfileData = [{
				"operation" : "REPLACE",
				"property": "profile.crUnion",
			    "value": _crUnion
			}];
			var promise=$http({ headers:header_encoding,method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });
			return promise;
		},
		uploadImage : function(image,id) {
			var file_data = $("#inputImage").prop("files")[0];   	
			var form_data = new FormData();                  		
			form_data.append("file", file_data);        
			var url='api/users/user-' + id+'/profile-picture';
			var uploadStatus = false; 
			$.ajax({
		           url: url,
		           cache: false,
		           async:false,
		           data: form_data,
		           processData: false,
		           contentType:false,                              
		           type: 'post',
		           success: function(result){
		        	 if (result.status == 'ERROR'){
		        		 uploadStatus=false;
		        		 $("#profile_image_container").attr("src","");
		        		 $("#upload_image_message").hide();
		        		 $("#upload_image_message").html("");		        		 
					 }
		        	 else{
		        		 uploadStatus=true;
		        		 $(".login-user-image").attr("src",result.response.profile.profilePictureUrl);
		        		 $("#profile_image_container").attr("src","");
		        		 $("#upload_image_message").hide();
		        		 $("#upload_image_message").html("");		        		
						 $("#inputImage").val("");
		        	 }
		           }			       
		     });
			return uploadStatus;
		},
		updateProfileExpertise:function(_id,tag,oprationType,profileData){
			if(oprationType=="ADD"){
					expertiseObjArr.push(tag);
					profileData.profile.expertise = expertiseObjArr;
			}
			else{
				var index = expertiseObjArr.indexOf(tag);
				if (index > -1) {
					expertiseObjArr.splice(index, 1);
				}
				profileData.profile.expertise=expertiseObjArr;					
			}
			$("#profile_expertise_error_alert").text("");
			var promise = $http({  method: 'POST',url: 'api/users/user', data: profileData  });
			return promise;
		},
		updateUserQuestionAnonymousType:function(id,isAnonymous){
			var promise = $http({  headers:header_encoding,method: 'POST',url: 'api/posts/question-'+id+'/updatePostType?anonymous='+isAnonymous});
			return promise;
		},
		getUserPostedQuestions:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions?postBy='+_id+'&pageAt='+questionsPageIndex+'&pageSize='+questionsPageSize});
		    return promise;			  	
		},
		getUserSocialPostedQuestions:function(_id){	
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions?socialContactsOf='+_id+'&pageAt='+socialQuestionPageIndex+'&pageSize='+socialQuestionPageSize});
		    return promise;
		},
		getUserPostedAnswers:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/questions/answers?postBy='+_id+'&pageAt='+answersPageIndex+'&pageSize='+answersPageSize});
		    return promise;			  	
		},
		getTrendingQuestion:function(){
			var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions?showTrending=true&pageAt='+trendingPageIndex+'&pageSize='+trendingPageSize});
			return promise;
		},
		getUserPostedReviews:function(_id){			
			var promise = $http({headers:header_encoding,method: 'GET',url: 'api/posts/reviews?postBy='+_id});
		    return promise;			  	
		},
		getAllTags:function(){
			var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/tags'});
			return promise;
		},
		getTagsByUserId:function(_id){
			var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/tags?userId='+_id});
			return promise;
		},
		deleteUserTag:function(_id,tag,_tagId){
			var data={
					"name":tag
					};
			var promise = $http({  headers:header_encoding,method: 'DELETE', url: 'api/tags/user/tag-'+_tagId });
			return promise;
		},
		addTagByUserId:function(tag,_id){
			var tagsData ={
							"name":tag
						   };
			var promise = $http({   headers:header_encoding,method: 'POST',url: 'api/tags/user-'+_id,data:tagsData});
			return promise;
		},
		getUserByEmail:function(_Email){
			var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/users/user?email='+_Email });
			return promise;
		},
		addNewQuestion:function(_title,_content,_tags,isAnonymous){
			_title=_title.replace(/[`%|\;:'"\{\}\[\]\\\/]/gi, '')
			if(_title.charAt(_title.length-2)=="?"){
				_title = _title.substr(0,_title.length-1);
			}
			if(_title.charAt(_title.length-1)!="?")
			{
				_title = _title+"?";
			}
			_title =capitaliseFirstLetter(_title);
			var questionData = {
					 title : _title,
					 content:_content,
					 tags:_tags,
					 anonymous:isAnonymous
			};
			var promise = $http({  headers:header_encoding,method: 'POST',url: 'api/posts/question', data: questionData  });
			return promise;
		},
    };
}])
.controller('profileCtrl',
		[ '$scope', 'profileService', function($scope, profileService) {
			
			var userObj = null;
			if($("#user_id").text()!=undefined && $("#user_id").text()!=null && $("#user_id").text()!=""){
				userObj= getUserProfileById($("#user_id").text());
			}
			currentLoginUserObj =userObj;
			
			var user_id=$("#user_id").text();
			var passedId ;
			var userParam=getURLParameter("user");
			if(userParam!=null && userParam!=undefined && userParam!="" ){
				passedId = userParam;
			}
			else{
				passedId = user_id;
			}			
			$scope.nameEditorEnabled = false;
			$scope.locationEditorEnabled = false;
			$scope.bioEditorEnabled = false;
			$scope.dobEditorEnabled = false;
			$scope.genderEditorEnabled = false;
			$scope.statusEditorEnabled = false;
			$scope.personalWebsiteEditorEnabled=false;
			$scope.lifeStyleGoalEditorEnabled=false;
			$scope.collegeEditorEnabled=false;
			$scope.creditUnionEditorEnabled=false;
			$scope.fullName = false;
			$scope.personalWebsitePageUrl="";
			$scope.categoryNames=[];
			$scope.lifeStyleGoal="";
			$scope.userCreatedOn="";			
			$scope.redirectFreeToolsApp = addhttp(getBaseUrlOfSite()+"freetoolapp");
			
			$scope.addTagEditorEnabled = false;
			
			// asign scope to variable
			documentScope = $scope;
			
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
//					 var lname=data.response.profile.lastname.charAt(0).toUpperCase()+data.response.profile.lastname.substring(1);
//					 $scope.fullName = fname+" "+lname; 
					 
					 $scope.bio = data.response.profile.bio;
					 if($scope.bio==null || $scope.bio==""){
						 $scope.bio="";
						 $scope.bioText="Share a bit about yourself to the community";
					 }
					 else{
						 $scope.bioText=$scope.bio;
					 }
					 $scope.personalWebsite = data.response.profile.websiteUrl;
					 if($scope.personalWebsite==null){
						 $scope.personalWebsite="";
						 $scope.personalWebsitePageUrl="";
						 $scope.personalWebsiteURL="";
						 $("#website-link-text").text("Enter your personal website, blog or social network url (Facebook, Twitter, LinkedIn, Google+ or About.Me)");
						 $("#website-link-text").show();
					 }
					 else{
						 $scope.personalWebsitePageUrl;
						 $scope.personalWebsiteURL = addhttp($scope.personalWebsite);
						 $("#website-link-text").hide();
					 }
					 $scope.lifeStyleGoal = data.response.profile.lifeStyleGoal;
					 if($scope.lifeStyleGoal==null){
						 $scope.lifeStyleGoal="";
						 $scope.lifeStyleGoalText="Set your lifestyle goal. Having a lifestyle goal help you focus on achieving your dream life. Lifestyle goals are not financial goals";						 
					 }
					 else{
						 $scope.lifeStyleGoalText=$scope.lifeStyleGoal;
					 }
					 $scope.college = data.response.profile.college;
					 if($scope.college==null){
						 $scope.college="";
						 $scope.collegeStaticStr="Enter College ";
					 }
					 else{
						 $scope.collegeStaticStr="";
					 }
					 $scope.creditUnion = data.response.profile.crUnion;
					 if($scope.creditUnion==null){
						 $scope.creditUnion="";
						 $scope.creditUnionStaticStr="Enter Credit Union";
					 }
					 else{
						 $scope.creditUnionStaticStr="";
					 }
					 if(data.response.profile.location!=null){
						 if(data.response.profile.location.displayName!=null && data.response.profile.location.displayName!=""){
							 $scope.location = data.response.profile.location.displayName;
							 $scope.usrLocation = data.response.profile.location.displayName;
						 }
						 else{
							 $scope.location="Enter Location ";
						 }
					 }
					 else{
						 $scope.location="Enter Location ";
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
					 var expertise = data.response.profile.expertise;
					 var expertiseStr = "";
					 var expertiseRemoveStr = "";
					 var editlinkStr="";
					 if(expertise!=null){
						 for(i=0;i < expertise.length;i++)
						 {
							 categoryString.push(expertise[i]);
							 expertiseStr=expertiseStr+'<a  class="profile-expertise" id="profile_expertise_tags_'+i+'" href="#" onclick="javascript:showQuestionByTag(\''+expertise[i]+'\')">'+expertise[i]+'</a>';
							 expertiseRemoveStr = expertiseRemoveStr + '<span class="profile-expertise"  id="profile_expertise_tags_editable'+i+'" ><a  href="">'+expertise[i]+'</a><a class="topic_remove" href="" onclick="removeProfileExpertise(\'' +i+'\',\''+passedId+'\',\''+expertise[i]+'\')">x</a></span>';
						 }
						 expertiseObjArr = expertise;
					  }
					  if(passedId==$("#user_id").text()){
						 editlinkStr='<a class="profile_expertise_content_edit edit"  href="#" onclick="javascript:profileExpertiseUpdateEnableEditor()">Edit</a>';
						 $(".profile_tags_edit").append(expertiseRemoveStr);
					  }
					  $(".profile_expertise_non_edit").append(expertiseStr);
					  $(".profile_expertise_edit_link").append(editlinkStr);
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				// showing trending questions 
				this.showAllTrendingQuestions();
				
				// showing social questions
				this.showAllSocialQuestions();
				
				// getting user question by id
				this.showAllUserQuestions();			
				
				// showing user answers
				this.showAllUserAnswers();				
				
				// getting user revies
				/*var userReviewsObjPromise = profileService.getUserPostedReviews(passedId);
				userReviewsObjPromise.success(function (data) {
					if(data.response!=null){
						$("#reviews_given_count").text(data.response.length);
					}
					else{
						$("#reviews_given_count").text(0);
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });*/
				
				// getting user tags by id
				var userTagsObjPromise = profileService.getTagsByUserId(passedId);
				userTagsObjPromise.success(function (data) {
					var discoverTagsStr="";
					if(data.status=="SUCCESS"){
						if(data.response.length<=0){
							$("#dashboard-predefined-tags").prepend("<div class='tag_container' style='color:black;'>You haven't added any tags of your own yet.</div>");
						}
						else{
							var tagRowId="";
							var rowId=0;
							for(i=0;i<data.response.length;i++){
								if(data.response[i].name!=null && data.response[i].name!=""){
									var tagUrl = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(data.response[i].name.trim());
									var tag = data.response[i].name;
									var count = data.response[i].totalNumQuestionsTagged;
									var index=i;
									var tagId = data.response[i].id;
									
									var ind=i;
									var divStr="";
									var remInd=0;
									if ( ind % 3 == 0){
										console.log("index value is:"+ind);
										tagRowId="tag_row_"+rowId;										
										divStr="<div class='user_tag_container' id="+tagRowId+" class='user_def_tag_div' style='clear:both;padding-top:5px;'></div>";
										$("#dashboard-user-defined-tags").append(divStr);
										rowId=rowId+1;
									}
								    console.log("tag row id is +"+tagRowId);
									var str='<div class="question_tags_category" id="user_tags_'+i+'" style="float:left;margin-left:15px;">'+
										'<a  target="_self" href="'+tagUrl+'">'+tag+'</a>'+
										'<a class="topic_remove" href="" onclick="removeUserTag(\'' + i + '\',\'' + tag + '\',\'' + tagId + '\')">x</a>'+
				         				'</div>';
									//discoverTagsStr=discoverTagsStr+str;
									$("#"+tagRowId).append(str);
								}
							}
							//$("#dashboard-user-defined-tags").append(discoverTagsStr);
						}
						tabsSpinnerHide("My-Tags-a");
					}
					else{
						tabsSpinnerHide("My-Tags-a");
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });	
			    		    
				var tagsJson = $.cookie("tagsArray");
				var tagsArray = $.parseJSON(tagsJson);
				if(tagsArray == null || tagsArray == undefined || tagsArray.length<=0)
				{
					var allTagsPromise = profileService.getAllTags();
					allTagsPromise.success(function (data) {					 
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
					console.log("in if part"+$scope.categoryNames[0]);
				}
				else{
					allTagsToFilter(tagsArray);
					console.log("in else part"+tagsArray[0]);
				}
			},
			$scope.removeUserTag=function(index,tag,tagId){
				// getting user tags by id
				var userTagsObjPromise = profileService.deleteUserTag(user_id,tag,tagId);
				console.log("index passed is: "+index);
				userTagsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						$("#user_tags_"+index).remove();
				    }			
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });			    
			
			}
			$scope.nameEnableEditor = function() {
				$scope.nameEditorEnabled = true;   
			},
			$scope.nameDisableEditor = function() {
				$scope.nameEditorEnabled = false;
			},
			$scope.saveName = function() { 
				var userObjPromise= profileService.updateUsername($scope.username,user_id);
				userObjPromise.success(function (data) {
					 $scope.firstname = data.response.profile.firstname;
					 $scope.nameDisableEditor()				 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
			},
			
			$scope.locationEnableEditor = function() {
				$scope.locationEditorEnabled = true;   
			},
			$scope.locationDisableEditor = function() {
				$scope.locationEditorEnabled = false;
			},
			$scope.saveLocation = function() {
				$("#progress-image-loader").show();
				var selectLocation;
				if(typeHeadSelectedObj!=undefined & typeHeadSelectedObj!=null)
					selectLocation=typeHeadSelectedObj.reference;
				else
					 selectLocation=null;
				var userObjPromise=profileService.updateUserLocation(selectLocation,$scope.usrLocation, user_id);
				userObjPromise.success(function (data) {
					 $scope.location = data.response.profile.location.displayName;
					 $scope.locationDisableEditor();
					 $scope.usrLocation = data.response.profile.location.displayName;
					 $("#progress-image-loader").hide();					 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
			
			},
		  	$scope.bioEnableEditor = function() {
		  		$scope.bioEditorEnabled = true;   
			},
			$scope.bioDisableEditor = function() {
				$scope.bioEditorEnabled = false;
			},
			$scope.saveBio = function() {
				var userObjPromise = profileService.updateUserBio($scope.bio, user_id);
				userObjPromise.success(function (data) {
					 $scope.bio = data.response.profile.bio;
					 $scope.bioText=data.response.profile.bio;
					 $scope.bioDisableEditor();			 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
		    },
		    $scope.addTagEnableEditor = function() {
		    	$("#userAddTagInput").focus();
		    	$("#userAddTagInput").val("");
		    	$("#tags_alert_message").hide();
		  		$scope.addTagEditorEnabled = true;   
			},
			$scope.addTagDisableEditor = function() {
				$("#tags_alert_message").hide();
				$("#userAddTagInput").val("");
				$scope.addTagEditorEnabled = false;
			},
			$scope.addUserTag = function() {
				var tagVal = cammelCaseOfString($("#userAddTagInput").val());
				tagVal.trim();
				if(tagVal!="" && tagVal!=undefined && tagVal!=null ){
					// check validate tags
					var found=checkTagsAvalable(tagVal);
			    	if(found < 0){
			    		var checkFlag=tagWithSpecialCharacters(tagVal);
			    		if(checkFlag){
			    			$("#tags_alert_message").show();
			    			$("#tags_alert_message").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
			    			return;
			    		}
			    	}
			    	else{
			    		tagVal=allTagArray[found];
			    	}	
			    	$("#add_user_own_tags_button").prop("disabled", true); 
					setLoaderImageOnButton('add_user_own_tags_button');
				
					var userObjPromise = profileService.addTagByUserId(tagVal,user_id);
					userObjPromise.success(function (data) {
						var index=$("#dashboard-user-defined-tags").find(".question_tags_category").length;
						var tagsRows=$("#dashboard-user-defined-tags").children().length;
						
						console.log("total rows of tags are: "+tagsRows);
						console.log(".........");
					    var lastRowIdIndex=parseInt(tagsRows)-1;
						var lastRowTags=$("#tag_row_"+lastRowIdIndex).children().length;
						
						console.log("Last row no. of tags are:"+lastRowTags);
						
						var tagUrl = getBaseUrlOfSite() + "question/tag/" + replaceSpaceWithhyphen(tagVal.trim());
						var tagId = data.response.id;
						var str='<div class="question_tags_category" id="user_tags_'+index+'" style="margin-left:15px;float:left;">'+
						'<a  target="_self" href="'+tagUrl+'">'+tagVal+'</a>'+
						'<a class="topic_remove" href="" onclick="removeUserTag(\'' + index + '\',\'' + tagVal + '\',\'' + tagId + '\')">x</a>'+
         				'</div>';
						var tagRowId="tag_row_";
						if(parseInt(lastRowTags)<3 && tagsRows>0){
							tagRowId=tagRowId+(parseInt(tagsRows)-1).toString();
						}
						else{
							tagRowId=tagRowId+(parseInt(tagsRows)).toString();
							var divStr="<div class='user_tag_container' id="+tagRowId+" class='user_def_tag_div' style='clear:both;padding-top:5px;'></div>";
							$("#dashboard-user-defined-tags").append(divStr);
						}
						$("#"+tagRowId).append(str);
						//$("#dashboard-user-defined-tags").append(str);
						$("#userAddTagInput").val("");
						$("#tags_alert_message").hide();
						$("#add_user_own_tags_button").prop("disabled",false);
						removeDisabledPropertyFromButton('add_user_own_tags_button');
						
					})
					.error(function (data) {
						console.log("In error "+data);		          
					})
				    .then(function (response) {
				    });
				}				
		    },
		  	$scope.personalWebsiteEnableEditor = function() {
		  		$scope.personalWebsiteEditorEnabled = true;   
			},
			$scope.personalWebsiteDisableEditor = function() {
				$scope.personalWebsiteEditorEnabled = false;
			},
			$scope.savePersonalWebsite = function() {
				var userObjPromise = profileService.updateUserPersonalWebsite($scope.personalWebsite, user_id);
				userObjPromise.success(function (data) {
					 $scope.personalWebsite = data.response.profile.websiteUrl;
					 $scope.personalWebsiteURL = addhttp(data.response.profile.websiteUrl);
					 $scope.personalWebsiteDisableEditor();
					 $("#website-link-text").hide();
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
		    },
		    $scope.lifeStyleGoalEnableEditor = function() {
		  		$scope.lifeStyleGoalEditorEnabled = true;   
			},
			$scope.lifeStyleGoalDisableEditor = function() {
				$scope.lifeStyleGoalEditorEnabled = false;
			},
			$scope.saveLifeStyleGoal = function() {
				var userObjPromise = profileService.updateUserLifeStyleGoal($scope.lifeStyleGoal,user_id);
				userObjPromise.success(function (data) {
					 $scope.lifeStyleGoal = data.response.profile.lifeStyleGoal;
					 $scope.lifeStyleGoalText = data.response.profile.lifeStyleGoal;
					 $scope.lifeStyleGoalDisableEditor();			 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
		    },
		    $scope.collegeEnableEditor = function() {
		  		$scope.collegeEditorEnabled = true;   
			},
			$scope.collegeDisableEditor = function() {
				$scope.collegeEditorEnabled = false;
			},
			$scope.saveCollege = function() {
				var userObjPromise = profileService.updateUserCollege($("#college-search").val(), user_id);
				userObjPromise.success(function (data) {
					 $scope.college = data.response.profile.college;
					 $scope.collegeStaticStr="";
					 $scope.collegeDisableEditor();			 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
		    },
		    $scope.creditUnionEnableEditor = function() {
		  		$scope.creditUnionEditorEnabled = true;   
			},
			$scope.creditUnionDisableEditor = function() {
				$scope.creditUnionEditorEnabled = false;
			},
			$scope.saveCreditUnion = function() {
				var userObjPromise = profileService.updateUserCreditUnion($("#credit-union-search").val(), user_id);
				userObjPromise.success(function (data) {
					 $scope.creditUnion = data.response.profile.crUnion;
					 $scope.creditUnionStaticStr="";
					 $scope.creditUnionDisableEditor();			 
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
				
		    },

		    $scope.setFile = function (elem) {
		    	$scope.inputField = elem;
		    	$scope.file = elem.files[0];
		    	readURL(elem);
		    },		    	 
		    $scope.uploadImage = function() {
		    	var imgHeight = $("#profile_image_container").width();
				var imgwidth = $("#profile_image_container").height();				
		    	if(parseInt(imgHeight) < 250 && parseInt(imgwidth) < 250){
		    		$("#upload_image_message").show();
		    		$("#upload_image_message").html("picture should be greater than or equal 250 by 250 px.");
		    	}
		    	else{
		    		var flag = profileService.uploadImage($scope.file,user_id);
		    		if(flag==false){
		    			$("#upload_image_message").show();
			    		$("#upload_image_message").html("Error uploading profile picture.")
		    		}
		    		else{
		    			 $("#add_image_close").click();
		    			 $("#addImageModal").hide();
						 $('#addImageModal').modal('hide');
						 $("#upload_image_message").hide();
				    	 $("#upload_image_message").html("");
				    	 $("#inputImage").val("");
		    		}
		    	}
			},
			$scope.showAllTrendingQuestions = function(){
				var allTrendingQuestionsPromise = profileService.getTrendingQuestion();
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
			},			
			$scope.showAllSocialQuestions = function(){
				if(socialQuestionSpinerFlag){
					$("#social_question_loader_image").show();
					$("#more_social_questions").hide();
				}
				var userQuestionsObjPromise = profileService.getUserSocialPostedQuestions(passedId);
				userQuestionsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						if(data.response.content.length!=null && data.response.content.length>0){
							setSocialQuestionToTab(data.response);
							socialQuestionPageIndex=socialQuestionPageIndex+1;
							socialQuestionSpinerFlag=true;
						}
						else{
							$("#asked-join-to").show();	
							tabsSpinnerHide("Social-a");
							$("#social_question_loader_image").hide();
							$("#more_social_questions").hide();							
						}
					}
					else{
						tabsSpinnerHide("Social-a");
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
		
			},
			$scope.showAllUserQuestions=function(){
				if(questionsSpinerFlag){
					$("#user_questions_loader_image").show();
					$("#more_questions_answers").hide();
				}
				var userQuestionsObjPromise = profileService.getUserPostedQuestions(passedId);
				userQuestionsObjPromise.success(function (data) {
					if(data.status=="SUCCESS"){
						if(data.response!=null){
							$("#questions_given_count").text(data.response.totalElements);
						}
						else{
							$("#questions_given_count").text(0);
						}
						if(data.response.content!=null && data.response.content.length > 0 ){
							setQuestionToTab(data.response);
							questionsPageIndex=questionsPageIndex+1;
							questionsSpinerFlag=true;
						}
						else{
							$("#add-new-question").show();
							tabsSpinnerHide("My-Questions-a");
							$("#user_questions_loader_image").hide();
							$("#more_user_questions").hide();		
						}
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });			
			},
			// getting user answers			
			$scope.showAllUserAnswers=function(){
				if(answersSpinerFlag){
					$("#user_answers_loader_image").show();
					$("#more_user_answers").hide();
				}
				var userAnswersObjPromise = profileService.getUserPostedAnswers(passedId);
				userAnswersObjPromise.success(function (data) {
					if(data.response!=null){
						$("#answers_given_count").text(data.response.totalElements);
					}
					else{
						$("#answers_given_count").text(0);
					}
					if(data.response.content!=null && data.response.content.length > 0 ){
						setAnsersToTab(data.response);
						answersPageIndex=answersPageIndex+1;
						answersSpinerFlag=true;
					}
					else{
						$("#add-new-answer").show();
						tabsSpinnerHide("My-Answers-a");
						$("#user_answers_loader_image").hide();
						$("#more_user_answers").hide();		
					}
				})
				.error(function (data) {
					console.log("In error "+data);		          
				})
			    .then(function (response) {
			    });
			},
			
			/*$scope.addNewProfileExpertise = function(expertise_tag){				
				var type="ADD";
				var found=checkExpertiseExist(expertise_tag);
			    if(found>=0){
			    	$("#profile_expertise_error_alert").text("Expertise already added.");
			    	return ;
			    }
				if($scope.profileData.profile.expertise!=null && $scope.profileData.profile.expertise.length>=10)
				{
					$("#profile_expertise_error_alert").text("You can only add upto 10 expertise.");
					return;
				}
				var profilePromise = profileService.updateProfileExpertise(user_id,expertise_tag,type,$scope.profileData);
				profilePromise.success(function (data) {
					 var id = data.response.profile.expertise.length;
					 $(".profile_tags_edit").append('<span class="profile-expertise"  id="profile_expertise_tags_editable'+id+'"><a  href="">'+expertise_tag+'</a><a class="topic_remove" href="" onclick="removeProfileExpertise(\'' +id+'\',\''+passedId+'\',\''+expertise_tag+'\')">x</a></span>');
					 $(".profile_expertise_non_edit").append('<a  class="profile-expertise" id="profile_expertise_tags_'+id+'" href="#" onclick="javascript:showQuestionByTag(\''+expertise_tag+'\')">'+expertise_tag+'</a>');	
					 $scope.profileExpertiseSelected = "";
					 $("#profile_expertise_error_alert").text("");
					 $('#profile_expertise_search').val("");
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });			
			};
			$scope.removeProfileExpertise = function(profileId,expertiseId,expertiseText){
				var type="REMOVE";
				var profilePromise = profileService.updateProfileExpertise(user_id,expertiseText,type,$scope.profileData);
				profilePromise.success(function (data) {				
					$("#profile_expertise_tags_editable"+expertiseId).remove();
					$("#profile_expertise_tags_"+expertiseId).remove();				   
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });			
			},*/
			$scope.updateUserQuestionAnonymousType = function(id,postType){
				var isAnonymous;
	            if(postType=="true"){
	            	isAnonymous=true;
	            }
	            else{
	            	isAnonymous=false;
	            }
	        	var profileUpdateQuestionPromise = profileService.updateUserQuestionAnonymousType(id,postType);
				profileUpdateQuestionPromise.success(function (data) {	
				    var elm = $("#postByQuestion-"+id);
				    var postedTypeInfo="";
				    var anonymousPostType="";
				    var linkIndicatedTitle="";
					if(postType=="true"){
						indicatedText="Posted as anonymous ";
						linkIndicatedTitle="Make it public";
						anonymousPostType="false";
						
					}
					else{
						indicatedText="Posted as public ";
						linkIndicatedTitle = "Make it private";
						anonymousPostType="true";
					}
					elm.empty();
					postedTypeInfo = '<div style="color:black;">'+indicatedText+'<a href="" onclick="changeQuestionAnonymousType(\'' +id+'\',\''+anonymousPostType+'\')">'+linkIndicatedTitle+'</a></div>';
					elm.html(postedTypeInfo);
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });
			},
			$scope.addQuestionButton=function(){
				if(addAllTagsArray.length==0){
					$(".right_selected_categories_empty_error").show();
					$(".right_selected_categories_non_exist_tag_error").hide();
					return;
				}
				var isAnonymous = $('#postAsAnonymous').is(':checked');						
				var isAnonymousFlag =isAnonymous;			
				var content = $scope.addNewQuestionDescription;
				if(content!="" && content !=null ){
					content=capitaliseFirstLetter(content);
				}
				var questionObjPromise = profileService.addNewQuestion($scope.addNewQuestionTitle,content,addAllTagsArray,isAnonymousFlag);
				questionObjPromise.success(function (data) {					 
					console.log("in success"+data);
					$(".right_selected_categories").empty();
					$(".right_selected_categories").hide();
					$(".right_questin_add_right-controls").show();
					$(".right_selected_categories_non_exist_tag_error").hide();
					$scope.addNewQuestionDescription="";
					$scope.addNewQuestionTitle="";
					$scope.allTagArray="";
					$('#postAsAnonymous').attr('checked',false);
					addAllTagsArray=[];
					var title = replaceSpaceWithhyphen(data.response.title);
					window.location.href = getBaseUrlOfSite()+"question/"+data.response.docId+"/"+removeLastLetter(title);
					
				})
				.error(function (data) {
			          console.log("In error "+data);
			          //alert("Question Has Been Added");
			          $scope.middleNewDescription="";
					  $scope.middleNewQuestion="";
			     })
			    .then(function (response) {			        	
			    });			
					
			}
		    $scope.shareCurrentUserProfile=function(){
			
			},			
			$scope.deactivateAccount=function(){
				
			};		
			
	} ]);
    function removeUserTag(index,tag,tagId){
    	documentScope.removeUserTag(index,tag,tagId);
    }
	function setTrendingQuestions(responseObj){
		var obj=responseObj.content;
		var trendingQuestionStr="";
		for(i=0;i<obj.length;i++)
		{	
			var border="1px solid #E0E0E0";
			var urlRed=formQuestionDetailRedirectUrl(obj[i].link);
			var date =new Date(obj[i].createdOn);
			var amPm;
			if(date.getHours()>12){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
			var contentStr="";
			var totalCommentCount=obj[i].totalCommentCount;
			var totalAnswerCount=obj[i].totalAnswerCount;
			var content=obj[i].content;
			var postTime = date.getUTCDate()+" "+getMonthNameByNumber(date.getUTCMonth())+" "+ date.getUTCFullYear()+" "+date.getHours()+":"+date.getMinutes()+" "+amPm;
			if(content!=null && content!="")
   			{
   				$("#tempTextContainer").html(content);
   				content= $("#tempTextContainer").text();
   				contentStr='<div style="font-size:14px;margin-left:12px;color:black;">'+content+'</div>';
   			}
   			if(obj[i].answered==true)
   			{
   				answerCommentStr='<div style="font-size:12px;margin-left:12px;"><a style="color: #0088CC;" href="'+urlRed+'" target="_self" >Answers ('+totalAnswerCount+')</a><a style="margin-left:5px;color: #0088CC;" href="'+urlRed+'"target="_self" >Comments ('+totalCommentCount+')</a></div>';
   			}
   			else{
   				urlRed=urlRed+"?answer=0";
   				answerCommentStr='<a href="'+urlRed+'"target="_self"  style="font-size:12px;margin-left:12px;color: #0088CC;">Be the first to answer</a><br/>';
   			}
			trendingQuestionStr = trendingQuestionStr+'<div class="trending_questions" style="padding:10px 0px 10px 10px;border-bottom:'+border+'">'+
								  '<div><a style="font-weight:bold;font-size: 16px;color: #0088CC;" href="'+urlRed+'"target="_self" >'+obj[i].title+'</a></div>'+
								  contentStr+answerCommentStr+'</div>';
		}
		$("#trending_questions").append(trendingQuestionStr);
		if(responseObj.firstPage==true && obj.length <=0){
			$("#trending_questions").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>'); 
		}
		if(responseObj.lastPage==false){
			$("#more_trending_questions").show();
		}
		else{
			$("#more_trending_questions").hide();
		}
		if(responseObj.first==true){
			tabsSpinnerHide("Feed-a")
		}
	}
	function setSocialQuestionToTab(responseObj){
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
		    if(index==obj.length-1){
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
   				var totalCommentCount = element.totalCommentCount;
   				answerStr='<div style="font-size:12px;margin-left:12px;"><a style="color: #0088CC;" href="'+urlRed+'" target="_self" >Answers ('+totalAnswerCount+')</a><a style="margin-left:5px;color: #0088CC;" href="'+urlRed+'" target="_self">Comments ('+totalCommentCount+')</a></div>';
   			}
   			else{
   				urlRed=urlRed+"?answer=0";
   				answerStr='<a href="'+urlRed+'"target="_self"  style="font-size:12px;margin-left:12px;color: #0088CC;">Be the first to answer</a><br/>';
   			}
   			questionsStr=questionsStr+'<div id="user_question'+id+'" style="padding:10px 0px 10px 0px;border-bottom:'+border+';">'+
   			'<div><a href="'+urlRed+'"target="_self"  style="font-weight:bold;font-size: 16px;color: #0088CC;">'+title+'</a></div>'+
   			contentStr+answerStr+'</div>';
    	});
        $("#dashboard-social").append(questionsStr);       
      
		if(responseObj.lastPage==false){
			$("#more_social_questions").show();
			$("#social_question_loader_image").hide();
		}
		else{
			$("#more_social_questions").hide();
			$("#social_question_loader_image").hide();
		}
		if(responseObj.firstPage==true){
			tabsSpinnerHide("Social-a");
		}		
	}
	    
	var objArr = new Array();
    function setQuestionToTab(questionsObj){
    	console.log("in set question tab");
    	questionsObj=questionsObj.content;
    	var questionsStr="";
    	$.each(questionsObj, function(index, element) {
    		var border="1px solid #E0E0E0";
			var id = element.docId;
			var title = element.title;
		    var content= element.content;
		    var contentStr="";
		    var answerStr="";
		    var postedTypeInfo="";
		    var link="question/"+id+"/"+replaceSpaceWithhyphen(title);;
		    var urlRed=formQuestionDetailRedirectUrl(link);
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
   				answerStr='<div style="font-size:12px;margin-left:12px;"><a style="color: #0088CC;" href="'+urlRed+'"target="_self" >Answers ('+totalAnswerCount+')</a><a style="margin-left:5px;color: #0088CC;" href="'+urlRed+'"target="_self" >Comments ('+totalCommentCount+')</a></div>';
   		   		
   			}
   			else{
   				answerStr="";
   			}
   			var indicatedText="";
   			var postType="";
   			var linkIndicatedTitle="";
   			var current_user_id = $("#user_id").text().trim();
   			if(element.postBy.id==current_user_id){
   				if(element.anonymous==true){
   					indicatedText="Posted as anonymous ";
					linkIndicatedTitle="Make it public";
					anonymousPostType="false";
				}
				else{
					indicatedText="Posted as public ";
					linkIndicatedTitle="Make it private";
					anonymousPostType="true";
				}
   				var postTypeId  ="postByQuestion-"+id;
   				postedTypeInfo = '<div id="'+postTypeId+'" style="font-size:12px;margin-left:11px;"><div style="color:black;">'+indicatedText+'<a href="" onclick="changeQuestionAnonymousType(\'' +id+'\',\''+anonymousPostType+'\')">'+linkIndicatedTitle+'</a></div></div>';
   			}
   			else{
   				postedTypeInfo="";
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
		    questionsStr=questionsStr+'<div id="user_question'+id+'" style="padding:10px 0px 10px 0px;border-bottom:'+border+';">'+
   			'<div><a href="'+urlRed+'"target="_self"  style="font-weight:bold;font-size: 16px;color: #0088CC;">'+title+'</a></div>'+
	   	    contentStr+answerStr+postedTypeInfo+'<div style="margin-left:12px;font-size:12px;color:gray;clear:both;">Posted On: '+postTime+'</div></div>';   			
        });
        $("#questions").append(questionsStr);
        if(questionsObj.length==questionsPageSize){
			$("#more_user_questions").show();
			$("#user_questions_loader_image").hide();
		}
		else{
			$("#more_user_questions").hide();
			$("#user_questions_loader_image").hide();
		}
		if(questionsPageIndex==0){
			tabsSpinnerHide("My-Questions-a");
		}	
        //tabsSpinnerHide("My-Questions-a");
    }
    function setAnsersToTab(answersObj){
    	var ansersStr="";
    	answersObj=answersObj.content;
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
   					contentStr='<div style="font-size: 16px;color:black;clear:both"><div>'+subText+'&nbsp;<a href="'+urlRed+'"target="_self"  style="color: #0088CC">More</a></div></div>';
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
			tabsSpinnerHide("My-Answers-a");
		}        
   }
   function profileExpertiseUpdateDisableEditor(){
	   $(".profile_expertise_non_edit").show();
	   $(".profile_expertise_edit_link").show();
	   $(".profile_expertise_edit_content").hide();
	   $("#profile_expertise_error_alert").text("");
   }
   function profileExpertiseUpdateEnableEditor(){	  
	   $(".profile_expertise_non_edit").hide();
	   $(".profile_expertise_edit_link").hide();
	   $(".profile_expertise_edit_content").show();
	   $("#profile_expertise_search").val("");
	   $("#profile_expertise_search").focus();
	   $("#profile_expertise_error_alert").text("");
   }
   function profileExpertiseAddNew(value){
	   documentScope.addNewProfileExpertise(value);
   }
   function removeProfileExpertise(categoryId,profileId,categoryText){
	   documentScope.removeProfileExpertise(profileId,categoryId,categoryText);
   }
   function changeQuestionAnonymousType(id,postType){
	   documentScope.updateUserQuestionAnonymousType(id,postType); 
   }
    function setFocusOnAddTagInput(){	
	   $("#userAddTagInput").focus();
   }
   function setFocusOnHeaderSearch(){
	   $("#search_home_control").focus();
   }
 
