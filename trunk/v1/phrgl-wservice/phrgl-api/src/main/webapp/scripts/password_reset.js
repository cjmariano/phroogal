var BASE_URL = location.protocol + "//" + document.location.host
		+ window.location.pathname;
// declaring heders for request
var header_encoding = {	"Accept-Encoding":"gzip"};
angular.module('signin', []).
  config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
  }]).directive("passwordVerify", function() {
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
 
/**
 * LoginService Handles login functions that will be used by the controller
 */
.service('loginService',['$http', '$location', function($http, $location) {
	return {

		/**
		 * logs the user in, by accepting registered username and password
		 * 
		 * @param username
		 *            of the user
		 * @param password
		 *            of the user
		 * @returns
		 */
		login : function(_email, _password) {
			var rememberMe = $('#rememberMe').is(':checked');
			var loginCredentials = {
				username : _email,
				password : _password
			};
			$http({ headers:header_encoding,method: 'POST', url: '../api/login?_spring_security_remember_me=' + rememberMe, data: loginCredentials  }).
			  success(function (data, status, headers, config) {				 
				  if (data.status == 'ERROR') {
					  if(data.error.code=="ERR_304"){
						  documentScope.resendMail = _email	
						  var messageHtml="We see you haven't verified this email yet.<br/>We can <a href='javascript:resendEmailConfirmation()'>resend the verification email.</a>";
						  $("#login_signup_message").show();
						  $("#login_signup_message").html(messageHtml);
					  }
					  else{
						  $("#login_signup_message").show();
						  $("#login_signup_message").text(data.error.message);
					  }
				  }
				  else {
					  window.location.href = getBaseUrlOfSite()+"dashboard";
				  }
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
			});
		},

		/**
		 * logs the user in, by accepting registered username and password
		 * 
		 * @param username
		 *            of the user
		 * @param password
		 *            of the user
		 * @returns
		 */
		loginProvider : function(provider) {
			$http({ headers:header_encoding,method: 'POST', url: '../api/login/' + provider + '?redirect=/dashboard'}).
			  success(function (data, status, headers, config) {
				  window.location.href = data.response.url;
				  $location.replace(data.response.url);
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
			});
		},
		/**
		 * terms and conditions show 
		 * 
		 */
		termsUse : function() {
			 var index = BASE_URL.lastIndexOf('/');
			 var url = BASE_URL.substring(0,index + 1);
			 window.location.href = url + "terms";
		},		
		/**
		 * Forgot Password Submit
		 * 
		*/
		forgotPassword : function(_email) {
			var data = {
				email : _email				
			};
			$http({ headers:header_encoding,method: 'POST', url: '../api/password-reset-request', data: data }).
			  success(function (data, status, headers, config) {
				  if (data.status == 'ERROR') {
					  $("#forgot_password_message").html("<strong>Oops!</strong><span> It seems like your email is not registered.</span>");
					  $("#forgot_password_message").show();
				  }
				  else if(data.status =='FAILURE'){
					  $("#forgot_password_message").html("<strong>Oops!</strong><span> Exception in sending mail.</span>");
					  $("#forgot_password_message").show();
				  }
				  else{
					  $("#forgotPasswordFormModel").hide();
					  $('#forgotPasswordFormModel').modal('hide');
					  $("#forgotPasswordFormModel").hide();
				      showErrorMessage("<span>We’ve sent a password reset link to your registered email address. Please check your email.</span>");
				      documentScope.forgotEmail="";
				  }
				  $("#progress-image-loader").hide();
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
			});
		},
		/**
		 * Check user with this email exist or not.
		 * 
		 */
		checkUserExist:function(_Email){
			var promise = $http({headers:header_encoding,  method: 'GET',url: '../api/users/user?email='+_Email });
			return promise;
		},
		/**
		 * Signs up a user with the data provided
		 * 
		 */
		
		signup : function(_firstname,_lastname,_password, _email) {
			$("#progress-image-loader").show();
			var my = this;
			var signupData = {
				firstname : _firstname,
				lastname : _lastname,
				password : _password,
			    email : _email
			};
			$http({headers:header_encoding, method: 'POST', url: '../api/signup', data: signupData  }).
			  success(function (data, status, headers, config) {
				 // my.login(_email, _password);
				  $("#progress-image-loader").hide()
				  if(data.status=="SUCCESS"){
					 // var msg = _firstname+" "+_lastname+": Please verify your account by following the instructions we will email you in a while.";
					  var msg= _firstname+", please verify your email address by following the instructions we’ve just emailed you. (Check your Spam or Junk folder as well.)";
					  $("#signupFormModal").hide();
					  $('#signupFormModal').modal('hide');
					  $("#signup_message").hide();
					  showErrorMessage(msg);
				  }
				  else{
					  $("#signup_message").text(data.error.message);
					  $("#signup_message").show();
				  }
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
				  $("#progress-image-loader").hide()
			});
		},
		resetPassword : function(_password) {
			var Data = {
				password : _password
			};			
			$http({headers:header_encoding, method: 'POST', url: '../api/password-reset-request-'+userId+'/password', data: Data  }).
			  success(function (data, status, headers, config) {				
				  if (data.status == 'SUCCESS') {
					  landingRedirectFlag=true;
					  	showErrorMessage("Change successful.");
				  }
				  else{
					  landingRedirectFlag=false;
					  $("#loginSignupFormModal").hide();
					  $('#loginSignupFormModal').modal('hide');
					  if(data.error.code=="ERR_402"){
						  showErrorMessage(data.error.message); 
					  }
					  else{
						  showErrorMessage(data.error.message);  
					  }					 
				  }
			  }).
			  error(function (data, status, headers, config) {
				  console.log(data);
			});
		},
		resendEmailConfirmation : function(_email) {
			$http({ headers:header_encoding,method: 'POST', url: '../api/email-confirmation?resendToEmail='+_email}).
			success(function (data, status, headers, config){				
				if (data.status == 'SUCCESS'){
					var msg= "please verify your email address by following the instructions we’ve just emailed you. (Check your Spam or Junk folder as well.)";
					$("#loginSignupFormModal").hide();
					$('#loginSignupFormModal').modal('hide');
					showErrorMessage(msg);
				}					  
			}).
			error(function (data, status, headers, config) {
				console.log(data);
			});
		}
	};
}])

/**
 * LoginCtrl Controller for delegating to login services
 */
.controller('LoginCtrl',
		[ '$scope', 'loginService', function($scope, loginService) {
			
			$scope.uniqueEmailError=false;
			documentScope = $scope;		
			$scope.resendMail="";
			/**
			 * Logins a user given a registered username and password
			 */
			$scope.login = function() {
				if ($scope.loginform.$valid){
					return loginService.login($scope.email, $scope.password);
		        }			
		    },
			
			/**
			 * Log in using Facebook
			 */
			$scope.loginFacebook = function() {
				return loginService.loginProvider('facebook');
			},
			
			/**
			 * Log in using LinkedIn
			 */
			$scope.loginLinkedIn = function() {
				return loginService.loginProvider('linkedin');
			},
			
			/**
			 * Log in using Google plus
			 */
			$scope.loginGoogle = function() {
				return loginService.loginProvider('google');
			},
            
			/**
			 * Log in using Twitter 
			 */
			$scope.loginTwitter = function() {
				return loginService.loginProvider('twitter');
			},
			/** 
			 * user termd and condition click
			 */
			$scope.termsUse = function() {
				return loginService.termsUse();
			},
			/**
			 * Signs up a user
			 */
			$scope.signup = function() {
				if($scope.uniqueEmailError==true)
				{
					 $("#signup_message").text("User with this email already exist.");
					 $("#signup_message").show();
					 return;
				}
				if(!$('#agree_terms_condition').is(':checked')){
					$("#signup_message").text("You must agree to the TOS and Privacy Policy to access Phroogal.");
					$("#signup_message").show();
					return;
				}
				$('#signup_form_button').attr('disabled','disabled');
				var fname=$scope.sFirstName.charAt(0).toUpperCase()+$scope.sFirstName.substring(1);
				var lname=$scope.sLastName.charAt(0).toUpperCase()+$scope.sLastName.substring(1);
				loginService.signup(fname,lname,$scope.sPassword, $scope.sEmail);
			    $('#signup_form_button').removeAttr('disabled');
				$scope.sFirstName="";
				$scope.sLastName="";
				$scope.sEmail="";
				$scope.sConfirmPassword="";
				$scope.sPassword="";				
			},
			/**
			 * Signs up a user check exist or not
			 */
			$scope.userExistCheck=function(){
				var userExistPromiseObj = loginService.checkUserExist($scope.sEmail)
				userExistPromiseObj.success(function (data) {
					if(data.status=="ERROR"){
						$scope.uniqueEmailError=false;
					}
					else{
						$scope.uniqueEmailError=true;
					}
				})
				.error(function (data) {
			          console.log("In error "+data);
			          $scope.uniqueEmailError=false;
				})
			    .then(function (response) {
				        	
			    });				

			},
			/**
			 * Forgot password
			 */
			$scope.forgotPassword = function() {
			    $("#progress-image-loader").show();
				loginService.forgotPassword($scope.forgotEmail);
			},
			$scope.resetPassword = function() {				
				return loginService.resetPassword($scope.sPassword);
		    },
		    $scope.resendEmailConfirmation = function() {				
				return loginService.resendEmailConfirmation($scope.resendMail);
		    };
	   } ]);
      function setModelHide(){
    	  $(".login-box").focus();
		  $(".modal-backdrop").css("opacity","0");
      }
      function showErrorMessage(message){
		  $('#error_message_link').click();
	      $("#error_message_text").html(message);				   	
      }
      function showLoginModel(){
		  $('#login-signup-form_link').click();
	  }     
      function resetSignUpForm(){
    	  $("#inputFirstName").val("");
    	  $("#inputLastName").val("");
    	  $("#inputEmail").val("");
    	  $("#inputPassword").val("");
    	  $("#inputPasswordConfirm").val("");
    	  $( "#agree_terms_condition" ).prop( "checked", false );
    	  $("#email-notvalid-span").hide();
    	  $("#password-match-span").hide();
    	  documentScope.sFirstName="";
    	  documentScope.sLastName="";
    	  documentScope.sEmail="";
    	  documentScope.sConfirmPassword="";
    	  documentScope.sPassword=""; 
    	  $("#signup_message").hide();
    	  $("#signup_message").html("");    	  
    	  documentScope.uniqueEmailError=false;
    	  
    	  // reset login fields
    	  documentScope.email="";
    	  documentScope.password="";
    	  $("#login_signup_message").hide();
      }
      function resetForgotFrom(){
    	  documentScope.forgotEmail="";    	  
      }
      function getURLParameter(name) {
  		return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
  	}
    function resendEmailConfirmation(){
    	documentScope.resendEmailConfirmation();
    }  


