function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var documentScope=null;
angular.module('settings', [])
 .directive("passwordVerify", function() {
	   return {
		      'require': "ngModel",
		      'scope': {
		        passwordVerify: '='
		      },
		      'link':function($scope, $element, $attrs, $ctrl) {
		    	  $scope.$watch(function() {
		            var combined;
		            if ($scope.passwordVerify || $ctrl.$viewValue) {
		               combined = $scope.passwordVerify + '_' + $ctrl.$viewValue; 
		            }                    
		            return combined;
		        }, function(value) {
		        	if (value) {
		        		$ctrl.$parsers.unshift(function(viewValue) {
		                    var origin = $scope.passwordVerify;
		                    if (origin !== viewValue) {
		                    	$ctrl.$setValidity("passwordVerify", false);
		                        return undefined;
		                    } else {
		                    	$ctrl.$setValidity("passwordVerify", true);
		                        return viewValue;
		                    }
		                });
		            }
		        });
		     }
		   };
		})
.service('settingsService',['$http', '$location', function($http, $location) {
	return {		
		changePassword:function(_oldPassword, _newPassword,_id){
			var my = this;
			var passwordData = {
					"oldPassword": _oldPassword,
				    "newPassword": _newPassword
			};
			var promise = $http({headers:header_encoding,  method: 'POST', url: 'api/users/user-'+_id+'/password', data: passwordData  });
			return promise;			
		},
		addAnotherEmail:function(_altEmail,_id){
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.altEmail",
				    "value":_altEmail
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH',url: 'api/users/user-'+_id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return promise;
		},
		updateDOB:function(_dob,_id){
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.dob",
				    "value":_dob
			}];
			//var dobStr = _dob.replace(/-/g,'/');			 
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}  });		
			return  promise;
		},
		updateGender:function(_sex,_id){				
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.sex",
				    "value":_sex
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data:userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'} });
			return  promise; 
		},
		updateFirstName:function(_fname,_id){				
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.firstname",
				    "value":_fname
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise; 
		},
		updateLastName:function(_lname,_id){				
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.lastname",
				    "value":_lname
			}];
			var promise = $http({ headers:header_encoding, method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise; 
		},
		
		updateMaritalStatus:function(_maritalStatus,_id){					
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.maritalStatus",
				    "value":_maritalStatus
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise; 
			  
		},
		updateEducationStatus:function(_education,_id){
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.education",
				    "value":_education
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise;			
		},
		updateCollegeStatus:function(_college,_id){
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.college",
				    "value":_college
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise;			
		},
		updateCrunion:function(_crUnion,_id){
			var my = this;			
			var userProfileData = [{
					"operation" : "REPLACE",
					"property": "profile.crUnion",
				    "value":_crUnion
			}];
			var promise = $http({headers:header_encoding,  method: 'PATCH', url: 'api/users/user-' + _id, data: userProfileData, headers: {'Content-Type': 'application/json;charset=utf-8'}   });
			return  promise; 
		},		
		privacy_settings:function(path){
			var index = BASE_URL.lastIndexOf('/');
			var url = BASE_URL.substring(0,index + 1);
			window.location.href = url+path;	
		},
		email_preferences:function(path){
			var index = BASE_URL.lastIndexOf('/');
			var url = BASE_URL.substring(0,index + 1);
			window.location.href = url+path;	
		},
		settings:function(path){
			var index = BASE_URL.lastIndexOf('/');
			var url = BASE_URL.substring(0,index + 1);
			window.location.href = url+path;	
		},
		connectToProvider : function(provider) {
			$http({ headers:header_encoding,method: 'POST', url: 'api/login/' + provider + '?redirect=/settings' }).
			  success(function (data, status, headers, config) {
				  window.location.href = data.response.url;
				  $location.replace(data.response.url);				  
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
			});
		},
		disconnectToProvider : function(provider,_id) {
			var promise=$http({headers:header_encoding, method: 'DELETE', url: 'api/users/user-' + _id +'/socialProfiles/'+provider });
			return promise;
		}
	};
}])
.controller('settingsCtrl',
		[ '$scope', 'settingsService', function($scope, settingsService) {
			
			documentScope = $scope;	
			var user_id=$("#user_id").text();			
			var userObj = getCurrentUserInfo();
			currentLoginUserObj=userObj;
			$scope.email=userObj.email;
			
			// setting scopes arrays for values
			$scope.status = [
			                 { id: 1, name: 'Single' },
			                 { id: 2, name: 'Married' }
			             ];
			$scope.genders = [
			                 { id: 1, name: 'Male' },
			                 { id: 2, name: 'Female' }
			             ];
			if($scope.email==null){
				$scope.email="N/A";
			}
			$scope.altEmail=userObj.altEmail;			
			$scope.fname=userObj.firstname;
			$scope.lname=userObj.lastname;
			if($scope.fname==null){
				$scope.fname="";
			}
			if($scope.lname==null){
				$scope.lname="";
			}
			$scope.fnameText=$scope.fname;
			$scope.lnameText=$scope.lname;
			if(userObj.dob!=null){
				$scope.dob=userObj.dob.trim().split(" ")[0];
				var dobArr=$scope.dob.split("-");
				$scope.dob = dobArr[1]+" "+getMonthNameByNumber(parseInt(dobArr[0])-1)+" "+dobArr[2];	
				 $scope.dobInput=dobArr[0]+"-"+dobArr[1]+"-"+dobArr[2];
				$("#datepicker").val($scope.dobInput);
			}
			if(userObj.sex!=null){
				$scope.sex=userObj.sex;
				$scope.selectedGender=returnMatchedIndexOfArray($scope.genders,$scope.sex);
            }	
			if(userObj.maritalStatus!=null){
				$scope.maritalStatus=userObj.maritalStatus;
				$scope.selectedStatus=returnMatchedIndexOfArray($scope.status,$scope.maritalStatus);
            }	
			$scope.education = userObj.education;
			$scope.crUnion = userObj.crUnion;
			
			$scope.facebookDisconnectEnable=false;
			$scope.linkedInDisconnectEnable=false;
			$scope.googleDisconnectEnable=false;
			$scope.twitterDisconnectEnable=false;
			
			
			$scope.facebookConnectEnable = false;
			$scope.linkedInConnectEnable=false;
			$scope.googleConnectEnable=false;
			$scope.twitterConnectEnable=false;
			
			$scope.facebookProviderUserProfileUrl = "";
			$scope.linkedInProviderUserProfileUrl="";
			$scope.googleProviderUserProfileurl="";
			$scope.twitterProviderUserProfileurl="";
			
			if(userSocialProfileObj.length > 0){
				for(i=0;i < userSocialProfileObj.length;i++){
					if(userSocialProfileObj[i].site =="LINKEDIN"){
						$scope.linkedInDisconnectEnable = true;
						$scope.linkedInConnectEnable=true;
						$scope.linkedInProviderUserId = userSocialProfileObj[i].userId;
						$scope.linkedInSocialUserName = userObj.firstname+" "+userObj.lastname;
						$scope.linkedInProviderUserProfileUrl = userSocialProfileObj[i].profileUrl;
					}
				    if(userSocialProfileObj[i].site =="FACEBOOK"){
				    	$scope.facebookDisconnectEnable = true;
				    	$scope.facebookConnectEnable=true;
				    	$scope.facebookProviderUserId = userSocialProfileObj[i].userId;
				    	$scope.facebookProviderUserProfileUrl = userSocialProfileObj[i].profileUrl;
				    	$scope.facebookSocialUserName =userObj.firstname+" "+userObj.lastname;
				    }
				    if(userSocialProfileObj[i].site =="GOOGLE"){
				    	$scope.googleDisconnectEnable = true;
				    	$scope.googleConnectEnable=true;
				    	$scope.googleProviderUserId = userSocialProfileObj[i].userId;
				    	$scope.googleProviderUserProfileUrl = userSocialProfileObj[i].profileUrl;
				    	$scope.googleSocialUserName =userObj.firstname+" "+userObj.lastname;
				    }
				    if(userSocialProfileObj[i].site =="TWITTER"){
				    	$scope.twitterDisconnectEnable = true;
				    	$scope.twitterConnectEnable=true;
				    	$scope.twitterProviderUserId = userSocialProfileObj[i].userId;
				    	$scope.twitterProviderUserProfileUrl = userSocialProfileObj[i].profileUrl;
				    	if(userObj.lastname != null){
				    		$scope.twitterSocialUserName =userObj.firstname+" "+userObj.lastname;
				    	}
				    	else{
				    		$scope.twitterSocialUserName =userObj.firstname;
				    	}
				    	//$scope.twitterSocialUserName =userObj.firstname+" "+userObj.lastname;
				    }
				}
			}				
			if($scope.altEmail==null || $scope.altEmail=="" || $scope.altEmail==undefined){
				$scope.altEmailEditorEnabled = false;
				$scope.otherEmailEditorEnabled=true;
			}
			else{
				$scope.altEmailEditorEnabled = true;
				$scope.otherEmailEditorEnabled=false;
			}				
			$scope.addAnotherEmailEditorEnabled= false;
			
			$scope.changePasswordEditorEnabled = false;
				
			$scope.email_preferences_checkbox = true;
			
			genderEditorEnabled=false;
			
			$scope.fnameEditorEnabled=false;
			
			$scope.lnameEditorEnabled=false;
			
			$scope.addAnotherEmailEnableEditor = function() {
				$scope.addAnotherEmailEditorEnabled = true;   
			},
			$scope.addAnotherEmailDisableEditor = function() {
				$scope.addAnotherEmailEditorEnabled = false;				
			},
			$scope.addAnotherEmail = function() {
			   var userObjPromise = settingsService.addAnotherEmail($scope.altEmailInput,user_id)
				userObjPromise.success(function (data) {
					 $scope.altEmail = data.response.profile.altEmail;
					 $scope.altEmailEditorEnabled = true;
					 $scope.otherEmailEditorEnabled=false;				
				})
				.error(function (data) {
			          console.log("In error "+data);
			          $scope.addAnotherEmailDisableEditor();

				})
			    .then(function (response) {
				        	
			    });				
			},			
			$scope.changePasswordEnableEditor = function() {
				$scope.changePasswordEditorEnabled = true;
				$scope.sOldPassword="";
				$scope.sPassword="";
				$scope.sConfirmPassword="";
				$("#inputPasswordConfrim").val("");
			},
			$scope.changePasswordDisableEditor = function() {
				$scope.changePasswordEditorEnabled = false;
				$scope.oldPassword = "";
				$scope.password = "";
				$("#inputPasswordConfrim").val("");
			},
			$scope.changePassword = function() {			
				var passwordPromise = settingsService.changePassword($scope.sOldPassword,$scope.sPassword,user_id);
				passwordPromise.success(function (data, status, headers, config) {
					if(data.status=="ERROR"){
						showErrorMessage(data.error.message);
						$scope.sOldPassword="";
					}
					else{
						 showErrorMessage("Password changed successful.");
						 $scope.changePasswordDisableEditor();
						 $scope.sOldPassword="";
						 $scope.sPassword="";
						 $scope.sConfirmPassword="";
						 $("#inputPasswordConfrim").val("");
					}
				}).
				error(function (data, status, headers, config) {
					console.log(data);
				});				
			},
		    $scope.dobEnableEditor = function() {
				$scope.dobEditorEnabled = true;
			},		
			$scope.dobDisableEditor = function() {
				$scope.dobEditorEnabled = false;
				$("#datepicker").val($scope.dobInput);
			},
			$scope.saveDOB = function() {
				if($("#datepicker").val()==null || $("#datepicker").val()==undefined || $("#datepicker").val()==""){
					return;
				}
				var userObjPromise = settingsService.updateDOB($("#datepicker").val(),user_id);
				userObjPromise.success(function (data) {					
					 $scope.dob =  data.response.profile.dob;
					 $scope.dob =$scope.dob.trim().split(" ")[0];
					 var dobArr=$scope.dob.split("-");
					 $scope.dob = dobArr[1]+" "+getMonthNameByNumber(parseInt(dobArr[0])-1)+" "+dobArr[2];	
					 $scope.dobInput=dobArr[0]+"-"+dobArr[1]+"-"+dobArr[2];					 
					 $scope.dobDisableEditor();				
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (date) {
				        	
			    });				
			},
			$scope.statusEnableEditor = function() {
				$scope.statusEditorEnabled = true;   
			},
			$scope.statusDisableEditor = function(){
				$scope.selectedStatus=returnMatchedIndexOfArray($scope.status,$scope.maritalStatus);
				$scope.statusEditorEnabled = false;
			},
			$scope.saveMaritalStatus = function() {				
				var userObjPromise = settingsService.updateMaritalStatus($scope.selectedStatus.name,user_id);
				userObjPromise.success(function (data) {					
					 $scope.maritalStatus =  data.response.profile.maritalStatus;
					 $scope.selectedStatus=returnMatchedIndexOfArray($scope.status,$scope.maritalStatus);
					 $scope.statusDisableEditor();			
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
			    	
			    });			
			},
			$scope.genderEnableEditor = function() {
				$scope.genderEditorEnabled = true;   
			},
			$scope.genderDisableEditor = function() {
				$scope.selectedGender=returnMatchedIndexOfArray($scope.genders,$scope.sex);
				$scope.genderEditorEnabled = false;
			},
			$scope.saveGender = function() { 
				var userObjPromise = settingsService.updateGender($scope.selectedGender.name,user_id);
				userObjPromise.success(function (data) {					 
					$scope.sex = data.response.profile.sex;
					$scope.selectedGender=returnMatchedIndexOfArray($scope.genders,$scope.sex);
					$scope.genderDisableEditor();			
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });				
			},
			$scope.fnameEnableEditor = function() {
				$scope.fnameEditorEnabled = true;
				$("#user_fname_text_box").focus();
			},
			$scope.fnameDisableEditor = function() {
				$scope.fnameEditorEnabled = false;
				$scope.fnameText = $scope.fname;
			},
			$scope.saveFname = function() { 
				var userObjPromise = settingsService.updateFirstName($scope.fnameText,user_id);
				userObjPromise.success(function (data) {					 
				     $scope.fnameText = data.response.profile.firstname;
				     $('#authName').html(cammelCaseOfString(data.response.profile.firstname));
				     $scope.fname = data.response.profile.firstname
					 $scope.fnameDisableEditor();			
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });				
			},
			$scope.lnameEnableEditor = function() {
				$scope.lnameEditorEnabled = true;
				$("#user_lname_text_box").focus();
			},
			$scope.lnameDisableEditor = function() {
				$scope.lnameEditorEnabled = false;
				 $scope.lnameText = $scope.lname;
			},
			$scope.saveLname = function() { 
				var userObjPromise = settingsService.updateLastName($scope.lnameText,user_id);
				userObjPromise.success(function (data) {					 
				     $scope.lnameText = data.response.profile.lastname;
				     $scope.lname = data.response.profile.lastname;
				     $scope.lnameDisableEditor();			
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });				
			},
			$scope.educationStatusEnableEditor = function() {
				$scope.educationStatusEditorEnabled = true;   
			},
			$scope.educationStatusDisableEditor = function() {
				$scope.educationStatusEditorEnabled = false;
			},
			$scope.updateUserHigestEducation=function(){
			  var userObjPromise = settingsService.updateEducationStatus($scope.selectedHighestEducation.name,user_id);
			    userObjPromise.success(function (data) {					
					 $scope.education =  data.response.profile.education;
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });	
			   	
			},
			$scope.saveCollegeStatus = function() {			   
			   var userObjPromise = settingsService.updateCollegeStatus($("#college-control").val(),user_id);
			    userObjPromise.success(function (data) {					
					 $scope.college =  data.response.profile.college;
					 $scope.educationStatusDisableEditor();			
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });	
			   	
			},
			$scope.creditUnionAffiEnableEditor = function() {
				$scope.creditUnionAffiEditorEnabled = true;   
			},
			$scope.creditUnionAffiDisableEditor = function() {
				$scope.creditUnionAffiEditorEnabled = false;
			},
			$scope.saveCreditUnionAffi = function() { 				
				var userObjPromise = settingsService.updateCrunion($scope.selectedcrUnion.name,user_id);
				userObjPromise.success(function (data) {					 
					 $scope.crUnion =  data.response.profile.crUnion;
					 $scope.creditUnionAffiDisableEditor();	
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });
			},
			$scope.connectToFacebook = function() {
				return settingsService.connectToProvider('facebook');
			},
			$scope.connectToLinkedIn = function() {
				return settingsService.connectToProvider('linkedin');
			},
			
			$scope.connectToGoogle = function() {
				return settingsService.connectToProvider('google');
			},
       	    $scope.connectToTwitter = function() {
				return settingsService.connectToProvider('twitter');
			},
			$scope.disconnectToFacebook = function(){				
				var userObjPromise = settingsService.disconnectToProvider('facebook',user_id);
				userObjPromise.success(function (data) {					 
					$scope.facebookDisconnectEnable=false;
					$scope.facebookConnectEnable = false;
				})
			    .error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });	
				
			},
			$scope.disconnectToLinkedIn = function() {
				var userObjPromise = settingsService.disconnectToProvider('linkedin',user_id);
				userObjPromise.success(function (data) {					 
					$scope.linkedInDisconnectEnable=false;
					$scope.linkedInConnectEnable=false;
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });				
			},
			
			$scope.disconnectToGoogle = function() {				
				var userObjPromise = settingsService.disconnectToProvider('google',user_id);
				userObjPromise.success(function (data) {					 
					$scope.googleDisconnectEnable=false;
					$scope.googleConnectEnable=false;
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });				
			},
       	    $scope.disconnectToTwitter = function() {
				var userObjPromise =  settingsService.disconnectToProvider('twitter',user_id);
				userObjPromise.success(function (data) {					 
					$scope.twitterDisconnectEnable=false;
					$scope.twitterConnectEnable=false;
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });
			},
			$scope.privacy_settings=function(){
				return settingsService.privacy_settings('privacy_settings');
			},
			$scope.settings=function(){
				return settingsService.settings('settings');
			},
			$scope.changeTopOfHint = function(){
				$("tt-hint").css("top",'-8px');
			},
			$scope.email_preferences=function(){
				return settingsService.email_preferences('email_preferences');
			};								
} ]);
var userSocialProfileObj;
function getCurrentUserInfo(){
	//showTwitterUserAddEmailForm();
	var userObj;
	var user_id=$("#user_id").text();
	$.ajax({  
	    async:false,
	    url: 'api/users/user-'+user_id,		        
	    success: function(data) { 	        	
	    	if(data.status == 'ERROR'){
	    		userObj=null;
	    	}
	    	else{					  
	    		userObj = data.response.profile;
	    		var userInfo=data.response;
	    		if(userInfo.primarySocialNetworkConnection==null){
	    			$("#user_change_password_content").show();
	    		}
	    		userSocialProfileObj = data.response.socialProfiles;						  
	    		console.log("in userobj"+userObj.email);
	    	}      
	    }
	});	
	return userObj;
}
