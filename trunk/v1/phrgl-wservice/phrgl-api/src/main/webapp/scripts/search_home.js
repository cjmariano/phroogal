var BASE_URL = location.protocol + "//" + document.location.host
		+ window.location.pathname;
$(document).ready(function (){
	//auto expend to textarea
	expandTextarea("middelAddNewQuestionTitle");
	expandTextarea("rightAddNewQuestionTitle");
	expandTextarea("rightAddNewQuestionContent");
	
	//header logo hide show
	$("#main_logo_div_container").show();
	$("#top_header_search_question").show();
	
	// TE Jquery plugin
	$('.TEeditor').jqte();
	var jqteStatus = false;
	$('.TEeditor').jqte({"status" : jqteStatus});
	
	var height=$(window).height(); 
	var containerHeight=height-90+"px";
	$("#search_link_frame").css("height",containerHeight);
	$("#search_link_frame").css("width",$(window).width()+"px");
	$("#search_link_frame").hide();
	$(".search-home-content").show();
	$("#search_link_frame").attr("src","");		
	$("#search_home_control").val(getKeywordValueFromUrl());
	
	//setSearchResultContent(getKeywordValueFromUrl());	
	// showing pagination
	$(".main-container").scroll(function () {     	
		if($(".main-container").scrollTop() + $(window).height() > $(".show_all_question_search").height()){
			if(processSearchQuestionFlag==false && viewAllCategory == false){
				documentScope.showAllSearchQuestionByKeyword();
   		 	}
			if(processExternalLinksFlag==false && viewAllExternalLinks == false){
				GetExternalLinksRecords();
			}
        }
	});
});
function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var selectKeyWordBrand;
var searchKeyword;
var pageIndex = 0;
var pageCount=1;
var pageSize=10;
var trendingPageSize=5;
var trendingPageIndex=0;
var relatedPageSize=5;
var relatedPageIndex=0;
var processSearchQuestionFlag=true;

/*function showBrandsWithTopReview(data){
	var ratings_review="ratings_review";
	objArr['ratings_review'] = data.response;
	var obj = data.response;
    if(obj!=null && obj.length >0){
    	var categoryLinks='<li class="ratings_review_link">'+
		'<a class="select_category_link ratings_review_link_a" href="javascript:leftCategoriesLinks(\''+ratings_review+'\')">Resources</a>'+
		'<div class="seprator_link"></div></li>';		
    	$('.catogory_links').append(categoryLinks);
    	var displayAll = "block";
    	if(obj.length <=5){
    		displayAll="none";
    	}
    	var categoryText='<span style="font-size:14.5pt;font-weight: bold;color:#003366">Resources'+ 
    	'<a href="javascript:leftCategoriesLinks(\'' + ratings_review + '\')"  style="color:#FFA824;margin-left:125px;">view all &#62;&#62;</a></span>'+
        '<div class="categoryText_seprator"></div>';    	
    	$('.ratings_review_searchCategory').append(categoryText);
    	var showCount=5;
    	var str = showCurrentBrandByObj(obj,showCount);
    	$('.ratings_review_searchCategory').append(str);
    }
    else{
    	var categoryText='<span style="font-size:14.5pt;font-weight: bold;color:#003366">Resources<a href="javascript:leftCategoriesLinks(\'' + ratings_review + '\')"  style="color:#FFA824;margin-left:20px;"></a></span>'+
        '<div class="seprator" style="background-color:#EEEEEE;width:100%;height:2px;margin-top:5px;margin-bottom: 5px;"></div>'+
        '<div class="add_new_review" style="padding:10px;color:black;font-size:16px;font-size:14px;">Want to rate or review a financial product, resource, tool or brand?'+ 
        '<button type="button" style="margin-left:10px;" class="btn btn-warning pull-right" onclick="addNewReviewToBrand()">Add a Resource</button></div>';
        '</div>';		
    	$('.ratings_review_searchCategory').append(categoryText);
    }	
}*/
var previousPageResoure=null;
var nextPageResoure=null;
function showExternalLinkQuestion()
{
	var reviewed_websites ="reviewed_websites";
	var reviewed_websites_data;
	var url="api/external-resource/query?keyword="+getKeywordValueFromUrl()+"&start=1";
	$.ajax({  
       // async:false,
        url:url,  
        success: function(msg) {
        	nextPageResoure=msg.response.nextPageResoure;
        	reviewed_websites_data=msg;
        	var resObj = reviewed_websites_data.response.results;
        	objArr['reviewed_websites'] =resObj;
        	if(resObj!=null && resObj!=undefined){
        		//objArr['reviewed_websites']=resObj;
        		$('.reviewed_websites_searchCategory').append('<span style="font-size:14.5pt;font-weight: bold;color:#003366">External Websites<a href="javascript:leftCategoriesLinks(\'' +reviewed_websites +'\')" style="color:#FFA824;margin-left:20px;">view all &#62;&#62;</a></span>');
	        	$('.reviewed_websites_searchCategory').append('<div class="categoryText_seprator"></div>');
	     		$('.reviewed_websites_searchCategory').append('<div class="reviewed_websites_content">');	
	        	var count=0;
	     		$.each(resObj, function(index, element) {	        		
	        		if(count < 5){
	        			var url=addhttp(element.externalLink);
		         		$('.reviewed_websites_content').append('<a target="_target"  href="'+url+'" style="font-size:16px;font-weight:bold;">'+element.title+'</a>');
		         		$('.reviewed_websites_content').append('<div class="reviewed_websites_content_source" style="color:#00802A;font-size:10pt;">'+element.source+'</div>');
		         		$('.reviewed_websites_content').append('<div class="reviewed_websites_content_snippet" style="margin-left:12px;">'+element.snippet+'</div><br/>');
		         		count=count+1;
	     		    }
	        		$('.search-home-middle-content_externle_websites').append('<div style="text-align:left;"><a  style="font-size:16px;font-weight:bold;" target="_target"  href="'+url+'">'+element.title+'</a><div class="reviewed_websites_content_source" style="color:#00802A;font-size:10pt;" >'+element.source+'</div><div class="reviewed_websites_content_snippet" style="margin-left:12px;">'+element.snippet+'</div></div><br/>');
		     	});
        	}
        }        
	});
}
function getKeywordValueFromUrl(){
	var val =  getUrlParamFromNamePass("keyWord");
	val=val.replace(/%92/g,"'");
	val=val.replace(/%27/g,"'");
	 val=val.replace(/-/g,' ');
	 if(val.indexOf('#_=_') > 0){
		 val = val.substring(0,val.length-4);
	 }
	 return val;
}
function getUrlParamFromNamePass( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return null;
  else
    return results[1];
}
function setMidTagsOnQuestionResultSearch(value){
	$(".question_search_tag_middle").show();
	$(".question_search_tags_empty_error_middle").hide();
	if(questionsResultSearchNewQuestion.length >=5){
		$(".middle_question_tags_category_limit_error_alert").show();
		$(".middle_selected_categories_empty_error").hide();
		$(".middle_selected_categories_non_exist_tag_error").hide();
		return;
	}
	questionsResultSearchNewQuestion.push(value);
	var id = questionsResultSearchNewQuestion.length;
	$(".question_search_tag_middle").append('<span  id="question_result_search_tags'+id+'" class="question_tags_category">'+value+'<a class="topic_remove" onclick="removeTagsOnQuestionResultSearch(\''+value+'\',\''+id+'\')" href="">x</a></span>&nbsp;')
	$("#searchQuestionAddNewTag").attr("value","");
}

function removeTagsOnQuestionResultSearch(tag,id){
	questionsResultSearchNewQuestion.pop(tag);
	$("#question_result_search_tags_error_alert").remove();
	$("#question_result_search_tags"+id).remove()
	if(questionsResultSearchNewQuestion.length==0){
		$(".question_search_tag_middle").hide();
	}
}
function  middleDetailsContentClick (){	
	if(currentLoginUserObj!=null){
		var jqteStatus = true;
		$('.TEeditor').jqte({"status" : jqteStatus});
		$(".jqte_editor").focus();
		$(".jqte_editor").css('font-size','14px');
	}
	else{
		$("#middleDetailsContent").blur();
		showLoginByQuestionRedirect();
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
var currentLoginUserObj="";
var questionsResultSearchNewQuestion = new Array();
	angular.module('search-home', ['common-login'])
//	config(['$locationProvider', function($locationProvider) {
//	$locationProvider.html5Mode(true);
//	}])
	.service('searchService',['$http', '$location', function($http, $location) {	
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
			getQuestionsByKeywordSearch:function(keyword){				
				var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/posts/questions/query?keyword='+keyword+'&topAnswer=true&pageAt='+pageIndex+'&pageSize='+pageSize});
				return promise;
			},
			
			getRelatedQuestionsByKeyword:function(keyword){				
				var promise = $http({ headers:header_encoding, method: 'GET',url: 'api/posts/questions/query?keyword='+keyword+'&showSimilar=true&pageAt='+relatedPageIndex+'&pageSize='+relatedPageSize});
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
	.controller('searchCtrl',
			[ '$scope', 'searchService', function($scope, searchService) {
		var user_id=$("#user_id").text();
	
		$scope.categoryNames=[];
		documentScope = $scope;
		$scope.resendMail="";
		$scope.uniqueEmailError=false;
		var userObj = null;
		if($("#user_id").text()!=undefined && $("#user_id").text()!=null && $("#user_id").text()!=""){
			userObj= getUserProfileById($("#user_id").text().trim());
		}
		currentLoginUserObj =userObj; 

		$scope.middleNewQuestionTitle=getKeywordValueFromUrl();
		$scope.searchKeyword=getKeywordValueFromUrl();
		$scope.rightAddNewQuestionEnableEditor = false;
		$scope.middleAddNewQuestionEnableEditor = false;
		$(".add_new_question").css("display","block");
		$scope.initFunctionCall = function(){
			
			// getting search results of questions by keyword
	        this.showAllSearchQuestionByKeyword();
	        
	        // setting external results
	    	this.showAllTrendingQuestions();
	    	var reviewed_websites ="reviewed_websites";
	    	$('.catogory_links').append('<li class="reviewed_websites_link">'); 
	    	$('.reviewed_websites_link').append('<a class="select_category_link reviewed_websites_link_a" href="javascript:leftCategoriesLinks(\'' +reviewed_websites + '\')">External Websites</a><div class="seprator_link"></div>');
	    	showExternalLinkQuestion();
	    	
	        var tagsJson = $.cookie("tagsArray");
			var tagsArray = $.parseJSON(tagsJson);
			if(tagsArray == null || tagsArray == undefined || tagsArray.length <=0){
				var allCategoryPromise = searchService.getAllTags();
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
		},
		$scope.showAllSearchQuestionByKeyword=function(){
			processSearchQuestionFlag=true;
			var questionsPromise = searchService.getQuestionsByKeywordSearch($scope.searchKeyword);
			questionsPromise.success(function(data){
        		if (data.status == "SUCCESS") {
        			if(data.response.totalElements>1){
        				$(".question_answers_searchCategory").show();
        				setSearchQuestionsToDom(data.response);
        				if(data.response.firstPage==true){
        					setPartialQuestions(data.response);
        				}
        				pageIndex = pageIndex + 1;
        			}
        			else{
    	        		$(".search-home-right-content_add_new_question_box").hide();
    	        		$(".add_new_question").show();
    	        		$(".middle_add_new_question_link_show").show();
    	        		$("#middelAddNewQuestionTitle").val(getKeywordValueFromUrl());
    	        		$(".question_answers_searchCategory").hide();
    	        		$(".question_answers_searchCategory_add_new_question").hide();
    	        		$(".add_new_question_answer_catogry_text_list").show();
    	        		$(".question-detail-right-content_related_question_box").show();
    	        		documentScope.showAllRelatedQuestions();	        		
        	        }
        			if(data.response.lastPage==false){
						processSearchQuestionFlag=false;
					}
					else{
						processSearchQuestionFlag=true;
					}
        		}
        	})
        	.error(function(data) {
        		console.log("In error " + data);
        	})
        	.then(function(response) {
        	});
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
			
			var questionObjPromise = searchService.addNewQuestion($scope.rightNewQuestionTitle,content,rightAllCategories,isAnonymousFlag);
			questionObjPromise.success(function (data) {					 
				console.log("in success"+data);
				if(data.status=="SUCCESS"){
					$(".right_selected_categories").empty();
					$(".right_selected_categories").hide();
					$(".right_questin_add_right-controls").show();
					$(".right_selected_categories_non_exist_tag_error").hide();
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
		          $scope.middleNewDescription="";
				  $scope.middleNewQuestion="";
				  removeLoaderImageOnButton('right_add_new_question_button');
		     })
		    .then(function (response) {			        	
		    });			
				
		},		
		$scope.addNewMiddleQuestionButton = function(){
			if(currentLoginUserObj==null){
				   showLoginByQuestionRedirect();
			     	return;
		    }
			if(midAllCategories.length==0){				
				$("#question_tags_alert_message_middle").text(TAGS_LOWER_LIMIT_MESSAGE);
				$("#question_tags_alert_message_middle").show();
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
			var questionObjPromise = searchService.addNewQuestion($("#middelAddNewQuestionTitle").val(),content,midAllCategories,isAnonymousFlag);
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
		          $scope.middleNewDescription="";
				  $scope.middleNewQuestion="";
				  removeLoaderImageOnButton('center_add_new_question_button');
		     })
		    .then(function (response) {			        	
		    });			
		},
		$scope.addUserTag = function(tagVal,user_id) {
			var userObjPromise = searchService.addTagByUserId(tagVal,user_id);
			userObjPromise.success(function (data) {
			})
			.error(function (data) {
				console.log("In error "+data);		          
			})
		    .then(function (response) {
		    });
	    },
		$scope.showAllRelatedQuestions = function(){
			var allRelatedQuestionsPromise = searchService.getRelatedQuestionsByKeyword($scope.searchKeyword);
			allRelatedQuestionsPromise.success(function (data) {					 
				if (data.status == "SUCCESS") {
        			if(data.response.totalElements>1){
        				setRelatedQuestions(data.response,searchKeyword);
        				relatedPageIndex = relatedPageIndex + 1;
        			}
        			else{
        				$(".related_questions_content_links").append('<div style="color:black;font-weight:bold;">No related questions found</div>');
        			}
        		}				
			 })
			.error(function (data) {
		          console.log("In error "+data);
		     })
		    .then(function (response) {
			        	
		    });			
		},
		$scope.showAllTrendingQuestions = function(){
			var allTrendingQuestionsPromise = searchService.getTrendingQuestion();
			allTrendingQuestionsPromise.success(function (data) {					 
				if(data.status=="SUCCESS"){
					setTrendingQuestions(data.response,searchKeyword);
					trendingPageIndex=trendingPageIndex+1;
				}				
			 })
			.error(function (data) {
		          console.log("In error "+data);
		     })
		    .then(function (response) {
			        	
		    });
			
		},
		$scope.addNewReviewToBrand = function(){
			if(currentLoginUserObj==null){
				   showLoginByQuestionRedirect();
			     	return;
		    }
			var index = BASE_URL.lastIndexOf('/');
			var url = BASE_URL.substring(0,index + 1);				
			window.location.href = url+"/addreview"
		},
		// showing login popup on page load
		$scope.initLoginPopBox=function(){
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
	
	function setRelatedQuestions(responseObj, title) {
		var obj=responseObj.content;
	    for (i = 0; i < obj.length; i++) {
	        var display = "block";
	        if (obj[i].title == title) {
	            display = "none"
	        }
	        var urlRed = formQuestionDetailRedirectUrl(obj[i].link);
	         $(".related_questions_content_links").append('<p class="trending_questions_results_p" style="font-size: 16px;display:' + display + '"><a href="' + urlRed + '"target="_self" >' + obj[i].title + '</a></p>');
	    }
	    if (responseObj.firstPage == true && obj.length <= 1) {
	        if (obj.length == 1 && title != "tag" && obj[0].title == title) {
	            $(".related_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>');
	        }
	        if (obj.length == 0) {
	            $(".related_questions_content_links").append('<p style="font-size:14px;color:black;font-weight:bold;">No question found</p>');
	        }
	    }
	    if (responseObj.lastPage == false) {
	        $("#more_related_questions").show();
	    } 
	    else {
	        $("#more_related_questions").hide();
	    }
	}
    var processExternalLinksFlag = false;
    function GetExternalLinksRecords() {
    	if(processExternalLinksFlag==true){
    		return;
    	}
        if (nextPageResoure!=null) {
        	processExternalLinksFlag=true;
        	$("#page_loader_image").show();
        	setTimeout("sendRequestToGetExternalLinksPage()", 100);           
        }
    }
    function sendRequestToGetExternalLinksPage(){
		$.ajax({
		 	async:false,
		     url:  nextPageResoure,                              
		     success: OnExternalLinksSuccess,
		     failure: function (response) {
		    	 console.log(response);
		     },
		     error: function (response) {
		    	 console.log(response);
		     }
		});
    }
    function OnExternalLinksSuccess(data) {
    	if(data.status=="SUCCESS"){
    		nextPageResoure=data.response.nextPageResoure;
    		setExternalLinksContentToPage(data.response.results);
    		processExternalLinksFlag=false;
    		$("#page_loader_image").hide();
        }
    	else{
    		nextPageResoure=null;
    		processExternalLinksFlag=true;
    	}
    }
    function setExternalLinksContentToPage(obj){
    	$.each(obj, function(index, element) {
    		var url=addhttp(element.externalLink);
    		$('.search-home-middle-content_externle_websites').append('<div style="text-align:left;"><a  target="_target"  href="'+url+'" style="font-size:16px;font-weight:bold;">'+element.title+'</a><div class="reviewed_websites_content_source" style="color:#00802A;font-size:10pt;" >'+element.source+'</div><div class="reviewed_websites_content_snippet" style="margin-left:12px;">'+element.snippet+'</div></div><br/>');
     	});
    }
	// search result html content to transfer here? 
    var objArr = new Array();
    var initExternalLinkClick = false;
    var initQuestionAllContent = false;
    var initRatingReviewsClick = false;
    
	function leftCategoriesLinks(linkName)
	{
		$(".search-home-middle-content").hide();
		$(".show_all_question_search").show();				
		$(".show_all_catogory").show();
		
		$(".question_answers_link_a").css('background-color','white');
		$(".ratings_review_link_a").css('background-color','white');
		$(".reviewed_websites_link_a").css('background-color','white');		
		$("."+linkName+"_link_a").css('background-color','#FFA824');
		//$(".search-home-middle-content_by_category").empty();	
		
		if(linkName=="question_answers"){
			$(".search-home-middle-content_by_category_div").show();
			$(".search-home-middle-content_externle_websites").hide();
			$(".search-home-middle-content_review_ratings_div").hide();
			viewAllCategory = false;
			viewAllExternalLinks=true;
			if(initQuestionAllContent==false)
			{
				var resObj = objArr[linkName];
				setQuestionsContentToDom(resObj);
			}
		}
		if(linkName=="reviewed_websites"){
			$(".search-home-middle-content_externle_websites").show();
			$(".search-home-middle-content_by_category_div").hide();
			$(".search-home-middle-content_review_ratings_div").hide();
			if(initExternalLinkClick == false)
			{
				initExternalLinkClick = true;
				//showExternalLinkQuestion();
			}
			viewAllExternalLinks=false;
			
		}
		if(linkName=="ratings_review"){
			$(".search-home-middle-content_review_ratings_div").show();
			$(".search-home-middle-content_externle_websites").hide();
			$(".search-home-middle-content_by_category_div").hide();
			if(initRatingReviewsClick == false)
			{
				initRatingReviewsClick = true;
				viewAllReviewCategory = false;
				var resObj = objArr[linkName];
				var resStr = showCurrentBrandByObj(resObj,10);
				$(".search-home-middle-content_review_ratings").append(resStr);
			}
		}
	}
	function setPartialQuestions(responseObj){
		var obj=responseObj.content;
		var question_answers="question_answers";	
		$(".search-home-right-content_add_new_question_box").show();
		$(".add_new_question_answer_catogry_text_list").hide();
		$(".question_answers_searchCategory").show();
		$(".question_answers_searchCategory_add_new_question").show();	        	
		
		objArr['question_answers']=obj;
    	$('.catogory_links').append('<li class="question_answers_link">'); 
    	$('.question_answers_link').append('<a class="select_category_link question_answers_link_a" href="javascript:leftCategoriesLinks(\'' + question_answers + '\')">Questions&nbsp;&amp;&nbsp;Answers</a><div class="seprator_link"></div>');
    	
    	$('.question_answers_searchCategory').append('<span style="font-size:14.5pt;font-weight: bold;color:#003366">Questions&nbsp;&amp;&nbsp;Answers<a href="javascript:leftCategoriesLinks(\'' +question_answers +'\')" style="color:#FFA824;margin-left:20px;">view all &#62;&#62;</a></span>');
    	$('.question_answers_searchCategory').append('<div class="seprator" style="background-color:#EEEEEE;width:100%;height:2px;margin-top:5px;margin-bottom: 5px;"></div>');
 		var questionsStr="";
		$.each(obj, function(index, element) {
			if(index < 5){
				var border="1px solid #E0E0E0";
				var id = element.docId;
				var title = element.title;
			    var content= element.content;
			    var link = element.link;
			    var urlRed=formQuestionDetailRedirectUrl(link);
			    var contentStr="";
			    var answerStr="";
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
   		    }    
        });
		$(".question_answers_searchCategory").append(questionsStr);
	}
	function setSearchQuestionsToDom(responseObj){
		var obj=responseObj.content;
		var questionsStr="";
		$.each(obj, function(index, element) {
    		var border="1px solid #E0E0E0";
			var id = element.docId;
			var title = element.title;
		    var content= element.content;
		    var link = element.link;
		    var urlRed=formQuestionDetailRedirectUrl(link);
		    var contentStr="";
		    var answerStr="";
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
		$("#showing-all-search-questions").append(questionsStr);
		initQuestionAllContent=true;
	}
	var viewAllCategory=true;
	var viewAllReviewCategory=true;
	var viewAllExternalLinks=true;
	function showAllCategories()
	{
		viewAllCategory = true;
		viewAllReviewCategory=true;
		$(".question_answers_link_a").css('background-color','white');
		$(".ratings_review_link_a").css('background-color','white');
		$(".reviewed_websites_link_a").css('background-color','white');
		
		$(".search-home-middle-content").show();
		$(".show_all_question_search").hide();
		$(".show_all_catogory").hide();
	}
	function setFocusOnAddNewQuestionRight(){
    	$("#rightAddNewQuestionTitle").focus();
    }
    function addNewReviewToBrand(){
    	var index = BASE_URL.lastIndexOf('/');
    	var url = BASE_URL.substring(0,index + 1);				
    	window.location.href = url+"addreview";
    }
    var currentShowingIndex = 5;
    function showingAllRelatedQuestions(){
 	   var length = $('.related_questions_results_p').length;
 	   if(currentShowingIndex >= length)
 	   {
 		   $(".showing_all_related_questions").hide()
 		   return;
 	   }
 	   else{ 
 		  for(i=currentShowingIndex;i<(currentShowingIndex+5);i++){
 			  if(i >= length){
 				  $(".showing_all_related_questions").hide()
 				  break;
 			  }
 			  else{
 				  $('#related_question_link_p'+i).show();
 			  }
 		  }
 		  currentShowingIndex=currentShowingIndex+5;
 	   }
    }
