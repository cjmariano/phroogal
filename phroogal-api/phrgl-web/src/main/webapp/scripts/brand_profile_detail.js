	function getMonthNameByNumber(num){
	   num = num-1;
		var month_names_short=['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	    return month_names_short[parseInt(num)]
    }
	
    var selectedRating="0";
    var questionsPageIndex=0;
    var questionsPageSize=10;
    var questionsPageCount=1;
	var documentScope;
    var reviewAllTags = new Array();
    var rightAllQuestionsTags = new Array();
    var objArr = new Array();
	
    var reviewsProcessFlag = false;
	var questionsProcessFlag = false;
	var showQuestionsTab = false;
	var showReviewsTab = true;
	
	$(document).ready(function () {		
	});
	
	function getNextQuestions() {
    	if(questionsProcessFlag==true){
    		return;
    	}
        if (questionsPageIndex <= questionsPageCount) {
        	questionsProcessFlag=true;
        	//$("#page_loader_image").show();
        	setTimeout("sendRequestToQuestionsPage()", 100);           
        }
    }	
    function sendRequestToQuestionsPage(){
    	var name = getBrandNameFromUrl().trim();
		$.ajax({
		 	async:false,
		     url:  '../api/posts/questions/query?keyword='+name+'&topAnswer=true&pageAt='+questionsPageIndex+'&pageSize='+questionsPageSize,                              
		     success: OnQuestionsSuccess,
		     failure: function (response) {
		         console.log(response);
		     },
		     error: function (response) {
		    	 console.log(response);
		     }
		});
    }
    function OnQuestionsSuccess(data) {
    	questionsPageIndex++;
    	questionsPageCount=questionsPageCount+1;
    	if(data.response.length <=0 || data.response.length < questionsPageSize){
    		questionsPageCount = 0;
    	}
    	var questions = setQuestionsContentToDom(data.response);
		$("#questions_search_result_content").append(questions);
		$("#questions_count").text(parseInt($("#questions_count").text())+data.response.length);
    	questionsProcessFlag=false;
        //$("#page_loader_image").hide();
    }   
	function showSmilyEmostionBackground(param)
	{	
		selectedRating = param;
	}	
	function getBaseUrlOfSite(){
		var index1 = BASE_URL.lastIndexOf('/');
		var BASE_URL1 = BASE_URL.substring(0,index1);
		var index2 = BASE_URL1.lastIndexOf('/');
		var url = BASE_URL1.substring(0,index2+1);
		return url;
	}
	function getBrandIdFromUrl(){
		var parts = window.location.pathname.split("/");
		var id = parts[parts.length-2]; 
		return id;
	}
	function getBrandNameFromUrl(){
		var parts = window.location.pathname.split("/");
		var name =parts[parts.length-1];
		return replacehyphenWithSpace(name);
	}
	function capitaliseFirstLetter(string)
	{
	    return string.charAt(0).toUpperCase() + string.slice(1);
	}
	function removeLastLetter(string)
	{
	    return string.substring(0,string.length-1);
	}
	function getReviewContentById(reviewId){
		var reviewObj;
		$.ajax({  
	        async:false,
	        url:   '../api/posts/review-'+reviewId,  
	        success: function(data) {
	        	reviewObj = data.response;  
	        }        
		});
		return reviewObj;
    }
	function redirectToQuestionDetail(id,index){		
		var questionsObj = objArr["question_answers"];
		var title = questionsObj[parseInt(index)].title;		
		title=replaceSpaceWithhyphen(title);
		window.location.href = getBaseUrlOfSite()+"question/"+id+"/"+removeLastLetter(title);
	}
	
	function redirectToQuestionDetailFirstAnswer(id,index){		
		var questionsObj = objArr["question_answers"];
		var title = questionsObj[parseInt(index)].title;		
		title=replaceSpaceWithhyphen(title);
		window.location.href = getBaseUrlOfSite()+"question/"+id+"/"+removeLastLetter(title)+"?answers=0";
	}
	function setRightCategories(value){
		$(".right_selected_categories_empty_error").hide();
		$(".right_selected_categories").show();
		if(rightAllQuestionsTags.length >=5){
			$(".right_question_tags_category_limit_error_alert").show();
			$(".right_selected_categories_empty_error").hide();
			$(".right_selected_categories_non_exist_tag_error").hide();
			return;
		}
		rightAllQuestionsTags.push(value);
		var id = rightAllQuestionsTags.length;
		$(".right_selected_categories").append('<span  id="right_question_tags_category'+id+'" class="question_tags">'+value+'<a class="topic_remove" onclick="removeRightCategory(\''+value+'\',\''+id+'\')" href="" style="margin-left:3px;">x</a></span>&nbsp;')
		$("#rightSelectedCategory").attr("value","")
	}
	function removeRightCategory(category,id){
		rightAllQuestionsTags.pop(category);	
		$(".right_question_tags_category_limit_error_alert").hide();
		$(".right_selected_categories_empty_error").hide();
		$(".right_selected_categories_non_exist_tag_error").hide();
		$("#right_question_tags_category"+id).remove()
		if(rightAllQuestionsTags.length==0){
			$(".right_selected_categories").hide();
		}
	}
	var totalCount=0;
	var totalLike=0;
	function avgRatingsGet(totalCount,likes){
		var avgRatings = Math.round(likes*100/totalCount);
		return avgRatings+"%"; 
	}
   angular.module('brandProfileDetail', []).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('brandProfileDetailService',['$http', '$location', function($http, $location) {		
		return  {
			
		 	getAllTags:function(){
				var promise = $http({headers:header_encoding,  method: 'GET',url: '../api/tags'});
				return promise;
			},
			getBrandById:function(_brandId){
				var promise = $http({headers:header_encoding,  method: 'GET',url: '../api/posts/brand-'+_brandId});
				return promise;			
			},
			getBrandByName:function(_name){
				var promise = $http({headers:header_encoding,  method: 'GET',url: '../api/posts/brand?name='+_name});
				return promise;			
			},
			getReviewsByBrandId:function(_brandId){
				var promise = $http({ headers:header_encoding, method: 'GET',url:'../api/posts/reviews/brand-'+_brandId});
				return promise;
			},
			getQuestionsByBrandName:function(name){
				var promise = $http({headers:header_encoding,  method: 'GET',url:'../api/posts/questions/query?keyword='+name+'&topAnswer=true&pageAt='+questionsPageIndex+'&pageSize='+questionsPageSize});                              
				return promise;
			},
			addNewQuestion:function(_title,_content,_tags,isAnonymous){				
				if(_title.charAt(_title.length-2)=="?"){
					_title = _title.substr(0,_title.length-1);
				}
				if(_title.charAt(_title.length-1)!="?")
				{
					_title = _title+"?";
				}
				_title = capitaliseFirstLetter(_title);
				var _id=$("#current_login_user_id").text();
                var _postBy={
                	"id":_id
                };
				var questionData = {
					 title : _title,
					 content:_content,
					 tags:_tags,
					 anonymous:isAnonymous,
					 postBy:_postBy
				};
				var promise = $http({headers:header_encoding,  method: 'POST',url: '../api/posts/question', data: questionData  });
				return promise;
			},
			addBrandReview:function(_brandRef,_title,_content,_tags,_ratings,isAnonymous){
				var reviewData = {
						  brandRef :_brandRef,
						  title : _title,
						  content : _content,
						  tags : _tags,
						  ratings:_ratings,
						  anonymous:isAnonymous
						  
				    };
				var promise = $http({ headers:header_encoding, method: 'POST',url: '../api/posts/review',data:reviewData});
				return promise;				
			},
			updateReviewVotes:function(reviewId,_action){
				var promise = $http({ method: 'POST', url: '../api/posts/review-'+reviewId+'/rating?action='+_action});
				return promise;
			}
		};
	}])
	.controller('brandProfileDetailCtrl',
			[ '$scope', 'brandProfileDetailService', function($scope, brandProfileDetailService) {
			
			documentScope = $scope;
			$scope.categoryNames=[];
			$scope.brandName="";
			$scope.brandSortDesc="";
			$scope.brandUrl="";
			$scope.brandLongDesc="";
			$scope.brandLogo="";
			var brandId="";			
			$scope.initFunctionCall = function(){
				// getting brand content by id
				var brandProfilePromise = brandProfileDetailService.getBrandByName(getBrandNameFromUrl());				
				brandProfilePromise.success(function (data) {
					brandId = data.response.id;
					$scope.brandName = data.response.name;
					$scope.brandSortDesc = data.response.shortDescription;
					$scope.brandLongDesc = data.response.longDescription;
					if($scope.brandLongDesc==null){
						$scope.brandLongDesc="";
					}
					if(data.response.profileSmallPictureUrl==null || data.response.profileSmallPictureUrl==""){
						$scope.brandLogo="../img/ratings_reviews/default_brand.png";
					}
					else{
						$scope.brandLogo = "../"+data.response.profileSmallPictureUrl
					}
					$scope.brandUrl = data.response.url;
					// showing all tags
					var tags = data.response.tags;
					var tagsStr = "";
					if(tags!=null){
						for(i=0;i < tags.length;i++)
						{
							tagsStr=tagsStr+'<a  class="question_tags" id="question_tags_'+i+'" href="#" onclick="javascript:showQuestionByTag(\''+tags[i]+'\')">'+tags[i]+'</a>&nbsp;';
						}							
					}
					$("#content_tags_value").append(tagsStr);
					var reviews = data.response.reviews;
					var totalRating=0;					
					if(reviews!=null){
						var length = reviews.length;
						for(i=0;i<length;i++){
							showReviews(reviews[i]);
							totalRating = totalRating + parseInt(reviews[i].ratings);
						}
						var avgRatings = totalRating;
						$("#brandAvgRecommend").text(avgRatingsGet(length,totalRating));
						$("#reviews_count").text(length);
						totalCount=length;
						totalLike=totalRating;
					}
					else{
						$("#reviews_count").text(0);
					}
				})
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });			
				// getting reviews content by brandid
				
				var brandQuestionsPromise = brandProfileDetailService.getQuestionsByBrandName(getBrandNameFromUrl());				
				brandQuestionsPromise.success(function (data) {
					$("#questions_count").text(data.response.length);
					console.log("questions length is:"+data.response.length);
					var questions = setQuestionsContentToDom(data.response);
					$("#questions_search_result_content").append(questions);
					questionsPageIndex=questionsPageIndex+1;
				})
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });	
				
				// getting all tags
				//var tagsJson = getCookie("tagsArray");
				var tagsJson = $.cookie("tagsArray");
				var tagsArray = $.parseJSON(tagsJson);
				if(tagsArray == null || tagsArray == undefined || tagsArray.length <=0){
					var allTagsPromise = brandProfileDetailService.getAllTags();
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
				}
				else{
					allTagsToFilter(tagsArray);
					console.log("in else part"+tagsArray[0]);
				}
			},
			$scope.showAllReviewByBrandId = function(id){
				var brandReviewPromise = brandProfileDetailService.getReviewsByBrandId(id);
				brandReviewPromise.success(function (data) {
					var totalRating=0;
					var length = data.response.length;
					for(i=0;i < length;i++){
						totalRating = totalRating + parseInt(data.response[i].ratings);
						console.log("ratings"+i+"is:"+data.response[i].ratings);
						showReviews(data.response[i]);
					}
					var avgRatings = totalRating/length;
					$("#reviews_count").text(length);
				})
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });	
			},
			
			$scope.addBrandProfileReview = function(){
		    	var isAnonymousReview = $('#postAsAnonymousReview').is(':checked');						
				var isAnonymousFlag = isAnonymousReview;				
				var reviewpromise = brandProfileDetailService.addBrandReview(brandId,$scope.reviewTitle,$scope.reviewDesc,reviewAllTags,selectedRating,isAnonymousFlag);
				reviewpromise.success(function (data) {	
					if(data.status=="SUCCESS"){
						console.log(data.response.id);
						showReviews(data.response);
						$scope.reviewTitle="";
						$scope.reviewDesc = "";
						if(isAnonymousReview == true){
							$('#postAsAnonymousReview').click();
						}
						reviewAllTags=[];
						$(".review_selected_tags").html("");
						showSmilyEmostionBackground("0");
						$("#reviews_count").text(parseInt($("#reviews_count").text())+1);
						
						totalCount=totalCount+1;
						totalLike=totalLike+parseInt(data.response.ratings);				
						$("#brandAvgRecommend").text(avgRatingsGet(totalCount,totalLike));					
						//$("#brandAvgRecommend").text(parseInt($("#brandAvgRecommend").text())+parseInt(data.response.ratings));
						selectedRating="0";
						$("#likeReviewDiv").css("background-color","#ef7e2a");
						
					}
				 })
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });				
			},
			$scope.addNewRightQuestionButton = function(){
				if(rightAllQuestionsTags.length==0){
					$(".right_selected_categories_empty_error").show();
					$(".right_selected_categories_non_exist_tag_error").hide();
					return;
				}
				var isAnonymous = $('#postAsAnonymousRight').is(':checked');						
				var isAnonymousFlag =isAnonymous;
				
				var content = $scope.rightNewQuestionDescription;
				
				if(content!="" && content !=null ){
					content=capitaliseFirstLetter(content);
				}
				
				var title=$scope.rightNewQuestionTitle;
				var questionObjPromise = brandProfileDetailService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllQuestionsTags,isAnonymousFlag);
				questionObjPromise.success(function (data) {					 
					
					$scope.rightNewQuestionTitle="";
					$scope.rightNewQuestionDescription="";
					rightAllQuestionsTags=[];
					var title = replaceSpaceWithhyphen(data.response.title);
					window.location.href = getBaseUrlOfSite()+"question/"+data.response.docId+"/"+removeLastLetter(title);
					$(".top_selected_categories_non_exist_tag_error").hide();
				})
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });		
			},
			$scope.reviewVotesUpDown = function(reviewId,action){	
				if(action=="downvote"){
					var reviewObj = getReviewContentById(reviewId);
					var currentUserId = $("#login_user_id").text();
					if((reviewObj.votes.userUpvotes.indexOf(currentUserId)) < 0){
						return;
					}
				}				
				var reviewObjPromise = brandProfileDetailService.updateReviewVotes(reviewId,action);
				reviewObjPromise.success(function (data) {				
					$("#vote_up_link_text_"+reviewId).text(data.response.votes.total);			   
				 })
				.error(function (data) {
			          console.log("In error "+data);
			     })
			    .then(function (response) {
				        	
			    });
				if(action=="downvote"){
					$("#vote_down_link"+reviewId).css("opacity","0.1");
					$("#vote_up_link"+reviewId).css("opacity","1");
					$("#vote_down_link"+reviewId).css("cursor","default");
					$("#vote_up_link"+reviewId).css("cursor","pointer");
				}
				else{
					$("#vote_up_link"+reviewId).css("opacity","0.1");
					$("#vote_down_link"+reviewId).css("opacity","1");
					$("#vote_down_link"+reviewId).css("cursor","pointer");
					$("#vote_up_link"+reviewId).css("cursor","default");
				}
			};
	} ]);
   function showAllReviewOfBrand(id){
	   documentScope.showAllReviewByBrandId(id);
   }
   function showReviews(reviewObj){
	   
		var reviewPostUserFullName= reviewObj.postBy.profile.firstname+" "+reviewObj.postBy.profile.lastname;
	
		var date =new Date(reviewObj.createdOn);
		var reviewPostTime;
		if(date.toString()=="Invalid Date"){
			date =reviewObj.createdOn;
			reviewPostTime = date.dayOfMonth+" "+getMonthNameByNumber(date.monthOfYear)+" "+ date.year;//+" "+date.getHours()+":"+date.getMinutes()+" "+amPm;
		}
		else{
			date =new Date(reviewObj.createdOn);
			reviewPostTime = date.getUTCDate()+" "+getMonthNameByNumber(date.getUTCMonth()+1)+" "+ date.getUTCFullYear();
		}
		console.log("date is :"+date);
		var reviewpostByPicture = "";
		var reviewpostByLocation="";					
		
		if(reviewObj.postBy.profile.location == null || reviewObj.postBy.profile.location=="" || reviewObj.postBy.profile.location.displayName==null){
			reviewpostByLocation="";
		}
		
		else{				
			reviewpostByLocation = "| "+reviewObj.postBy.profile.location.displayName+" ";
		}
		
		var reviewpostByBio="";
		if(reviewObj.postBy.profile.bio==null || reviewObj.postBy.profile.bio=="" ){
			reviewpostByBio="";
		}
		else{
			reviewpostByBio = "| "+reviewObj.postBy.profile.bio+" ";
		}
		
		if(reviewObj.postBy.profile.profileSmallPictureUrl==null || reviewObj.postBy.profile.profileSmallPictureUrl==""){
			reviewpostByPicture="../img/default-user.png";
		}
		else{
			if(reviewObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("https") !=-1 || reviewObj.postBy.profile.profileSmallPictureUrl.trim().indexOf("http")!= -1){
				reviewpostByPicture = reviewObj.postBy.profile.profileSmallPictureUrl;
			}
			else{
				reviewpostByPicture = "../"+reviewObj.postBy.profile.profileSmallPictureUrl;
			}
		}
		var voteTotal=reviewObj.votes.total;
		var downVoteDisplay;
		if(voteTotal=="0"){
			downVoteDisplay="block";
		}
		else{
			downVoteDisplay="block";
		}		
		var upLinkOpacity;
		var downLinkOpacity;
		var upLinkCursor;
		var downLinkCursor;
		var currentUserId = $("#login_user_id").text();
		if(reviewObj.votes.userUpvotes.indexOf(currentUserId)  < 0){
			upLinkOpacity=1;
			downLinkOpacity=0.1;
			upLinkCursor="pointer";
			downLinkCursor="default";
		}
		else{
			upLinkOpacity=0.1;
			downLinkOpacity=1;
			upLinkCursor="default";
			downLinkCursor="pointer";
		}
		/*var voteTotal=0;
		var downVoteDisplay="block";
		upLinkOpacity=0.1;
		downLinkOpacity=1;
		upLinkCursor="default";
		downLinkCursor="pointer";*/
		
		if(reviewObj.postBy.primarySocialNetworkConnection){
			profilePictureRadius="50%";
		}
		else{
			profilePictureRadius="0%";
		}
		
		var reviewContent = reviewObj.content;
		var reviewTitle = reviewObj.title;		
		if(reviewContent==null){
			reviewContent="";
		}
		var reviewStr='<div style="width:100%;clear:both;margin-bottom:15px;margin-top: 15px;" id="'+reviewObj.id+'">'+					
			'<div class="postby_and_post_time" style="font-size:12px;">'+
			'<span ><img class="answer-post-by-picture-img" src="'+reviewpostByPicture+'"  style="width:25px;height:25px;margin-right:10px;border-radius:'+profilePictureRadius+';" /><a href="#">' +reviewPostUserFullName + '&nbsp;</a>'+reviewpostByBio+reviewpostByLocation+
			'|&nbsp;'+reviewPostTime+'</span>'+
			'</div>'+
			'<table><tr><td valign="top">'+	
			'<div class="answer-post-by-picture-content_and_vote_up_dow">'+
			'<div style="float:left;width:35px;height:55px;margin-top:15px;">'+
			'<div class="vote_up_down_content">'+
			'<div class="vote_up_content">'+
			'<div class="vote_up_link_background" style="opacity:'+upLinkOpacity+';cursor:'+upLinkCursor+';"  id="vote_up_link'+reviewObj.id+'" onclick="upVoteToReviewById(\''+reviewObj.id+'\')"></div><div class="vote_up_link_text" id="vote_up_link_text_'+reviewObj.id+'">'+voteTotal+'</div></div>'+
			'<a style="display:'+downVoteDisplay+';opacity:'+downLinkOpacity+';cursor:'+downLinkCursor+';" id="vote_down_link'+reviewObj.id+'" href="" onclick="javascript:downVoteToReviewById(\''+reviewObj.id+'\')" class="vote_down_content vote_down_link"></a></div>'+
			'</div></td><td>'+
			'<div class="review-post-content">'+
			'<div class="review_title" style="font-size:14px;font-weight:bold;padding:10px 0px 10px 0px;">'+reviewTitle+'</div>'+
			'<div class="review_content" style="font-size:14px;">'+reviewContent+'</div>'+
			'</div></td></tr></table>'+
			'</div>'+
			'<div style="width:100%;height:2px;background-color:#EEEEEE"></div>';
		$('.brand_profile_reviews_content').append(reviewStr);	   
   }
   objArr["question_answers"]=[];
   function setQuestionsContentToDom(resObj){
	   var questionStrings="";
	   var object1 = objArr["question_answers"];
   		var object2 = resObj;    	
   	
	   $.each(resObj, function(index, element) {
		   	var passingIndex = (questionsPageIndex)*questionsPageSize+index;
   			object1[passingIndex] = element;
			var border="2px solid #E0E0E0";
			var id = element.id;
			var title = element.title;
		    var content= element.content;
		    var contentString ="";
		    var answersString="";
		    if(content!=null && content!="")
  			{
		    	contentString='<div style="font-size: 14px;color:black;">'+content+'</div>';
  			}
  			if(element.answered==true)
  			{
  				answersString = '<div style="font-size:10pt;margin-left:12px;">'+
  							 '<a href="javascript:redirectToQuestionDetail(\'' +id+'\',\''+passingIndex+'\')">Answers ('+element.totalAnswerCount+')<a>'+
  							 '<a style="margin-left:5px;" href="javascript:redirectToQuestionDetail(\'' +id+'\',\'' +passingIndex+'\')">Comments ('+element.totalCommentCount+')<a>'+
  							 '</div>';
  			}
  			else{
  				answersString ='<div><a href="javascript:redirectToQuestionDetailFirstAnswer(\''+id+'\',\''+passingIndex+'\')" style="font-size:14px;margin-left:12px;">Be the first to answer</a></div>';
  			}
  			questionStrings=questionStrings+
  			'<div class="question_content" style="padding-top:10px;padding-bottom:10px;border-bottom:2px solid #E0E0E0;">'+
		    '<div><a href="" onclick="redirectToQuestionDetail(\'' +id+'\',\'' +passingIndex+'\')" style="font-weight:bold;font-size: 16px;">'+title+'</a></div>'+
		    contentString+
		    answersString+
		    '</div>';  			
       });
	   objArr["question_answers"]=object1;
	   return questionStrings;
   }
   function showAllReviews(){
	   $("#brand_profile_reviews_form_content").show();
	   $("#questions_search_result_content").hide();
	   $("#total_questions").css("color","gray");
	   $("#total_reviews").css("color","black");
	   $("#total_text_show").show();
	   showReviewsTab = true;
	   showQuestionsTab = false;
   }
   function showAllQuestions(){
	   $("#brand_profile_reviews_form_content").hide();
	   $("#questions_search_result_content").show();
	   $("#total_questions").css("color","black");
	   $("#total_reviews").css("color","gray");
	   $("#total_text_show").show();
	   showReviewsTab = false;
	   showQuestionsTab = true;
   }
   function upVoteToReviewById(reviewId){
	   var action = "upvote" 
	   documentScope.reviewVotesUpDown(reviewId,action);
   }
   function downVoteToReviewById(reviewId){
	   var action = "downvote";
	   documentScope.reviewVotesUpDown(reviewId,action);
   }   
   function likeToReview(){
	   selectedRating="1";
	   $("#likeReviewDiv").css("background-color","#FFBA52");
   }
   function setFocusOnAddReview(){
	   $("#reviewTitle").focus();
   }