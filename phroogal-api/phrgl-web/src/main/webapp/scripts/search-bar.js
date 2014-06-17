var BASE_URL  = location.protocol + "//" +"" + document.location.host + window.location.pathname;
var dropDownSelectFlag=false;
$(function(){
	if(getURLParameter('docFound')=='false'){
		$("#no_question_found_text").show();
	}	
	if($("#new_user").text()=="true"){
		$("#new_joiny_text").show()
	}
	$("#main_logo_div_container").hide();
	$("#top_header_search_question").hide();
	$('#search-control').select();	
});
$(document).ready(function () {	
	$('#search-control').typeahead([ { 			 
		name: 'question-answers',
		minLength: 2,
		items: 5,
		valueKey: 'title',
		limit: '10',
		remote: {
		   url: 'api/posts/questions/query?keyword=%QUERY',
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response.content;
		   }
		},		 
		template: '<span><p>{{title}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;padding:2px 0px">Questions and Answers</h4>'
	},
	/*{
		name: 'resources',
		minLength: 3,
		items: 5,
		valueKey: 'name',
		limit: '20',
		remote: {
		   url: 'api/posts/brands/query?keyword=%QUERY',			   
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response;
		   }
		},		 
		template: '<span><p>{{name}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;">Resources</h4>'
	}*/
	{
		name: 'tags',
		minLength: 2,
		items: 5,
		valueKey: 'name',
		limit: '10',
		remote: {
		   url: 'api/tags/query?keyword=%QUERY',			   
		   filter: function(data) {
		   	if (data.status == 'FAILURE') {
		   		return '';
		   	};
		       return data.response;
		   }
		},		 
		template: '<span><p>{{name}}</p></span>',
		engine: Hogan,
		header: '<h4 style="margin-left:15px;padding:2px 0px">Tags</h4>'
	}
	]).on('typeahead:selected', function($e, data) {
		  dropDownSelectFlag=true;
		  var index = BASE_URL.lastIndexOf('/');
		  var url = BASE_URL.substring(0,index + 1);
		  if(data.title==undefined || data.title==null){
			 	window.location.href = url+"question/tag/"+replaceSpaceWithhyphen(data.name);
		  }
		  else{
			 	window.location.href = url+"question/"+data.docId+"/"+removeLastLetter(replaceSpaceWithhyphen(data.title)); 		 				   
		  }
	});	
	$('#search-control').keyup(function(e) {
	    if (e.which === 13) {
	    	if(dropDownSelectFlag==false){
	    		searchByKeywordOnMain('search_home');
	    	}  
	    }
	    var query = $(this).val();
		$(".search-more-link").attr('href', 'javascript:searchByKeyword("search_home")');
		$(".search-more-link").find("span").text(query);
	});
	var tags = getAllTags();
	var tagsJson = JSON.stringify(tags);
	//setCookie("tagsArray",tagsJson,7);
	$.cookie('tagsArray', tagsJson);
	console.log("getting cookie value for reuse on search-bar:"+$.cookie("tagsArray"));
	$("#search-control").focus();
});
var currentLoginUserObj=null;
var tags = new Array();
function getAllTags(){
	$.ajax({  
        async:false,
        url:   'api/tags',  
        success: function(data) {
        	if(data.status=="SUCCESS"){
				for(i=0;i < data.response.length;i++){
					tags.push(data.response[i].name);						
				}
			}				
        	else{
        		tags=[];
        	}
        }        
	});
	return tags;
}
function searchByKeywordOnMain(path){
	var rowVal=$("#search-control").val();
	var keyWord = rowVal.replace(/[`%^\#",\{\}\[\]\\\/]/gi, '');
	if(keyWord=="" || keyWord==null || keyWord==undefined)
		return;
	
	if(keyWord.charAt(keyWord.length-1)=="?")
		keyWord=keyWord.substring(0,keyWord.length-1);
	
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	window.location.href = url + path+"?keyWord="+replaceSpaceWithhyphen(keyWord);			
}
function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}

angular.module('search', ['common-login']).
config(['$locationProvider', function($locationProvider) {
$locationProvider.html5Mode(true);
}])
.service('searchService',['$http', '$location', function($http, $location) {	
return {	
};
}])
.controller('searchCtrl',
		[ '$scope', 'searchService', function($scope, searchService) {
	documentScope = $scope;
	$scope.resendMail="";
	$scope.uniqueEmailError=false;
	$scope.redirectToTrendingPageUrl = addhttp(getBaseUrlOfSite()+"trending");
	$scope.redirectToDiscoverPageUrl = addhttp(getBaseUrlOfSite()+"discovertags");
} ]);

