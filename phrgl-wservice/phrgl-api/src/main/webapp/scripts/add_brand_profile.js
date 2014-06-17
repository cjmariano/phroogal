var selectedRating="0";
function showSmilyEmostionBackground(param)
{	
	selectedRating = param;
	for(i=1;i<=5;i++)
	{
		$("#smilybackground_smily"+i).css('opacity','0')	
	}
	var destTo = parseInt(param);
	for(j=1;j<= destTo;j++)
	{
		$("#smilybackground_smily"+j).css('opacity','1')	
	}

}

function checkTagsAvalable(tag){
	var found = -1;
	for (i = 0;i < allTagArray.length;i++) {
	  if (allTagArray[i].toLowerCase()==tag.toLowerCase()){				  
		    found = i;
		    break;
	  }
	}
	return found;
}
var brandAllTags = new Array();
var reviewAllTags = new Array();

function setBrandTags(value){
	$("#brand_tags_non_exist_tag_error").hide();
	$("#brand_tags_empty_error").hide();
	$("#brand_tags_limit_error_alert").hide();
	$(".brand_selected_tags").show();
	if(brandAllTags.length >=2){
		$("#brand_tags_non_exist_tag_error").hide();
		$("#brand_tags_empty_error").hide();
		$("#brand_tags_category_limit_error_alert").show();
		return;
	}
	brandAllTags.push(value);
	var id = brandAllTags.length;
	$(".brand_selected_tags").append('<span  id="brand_tags'+id+'" class="tags_selected">'+value+'<a class="topic_remove" onclick="removeBrandTags(\''+value+'\',\''+id+'\')" href="">x</a></span>&nbsp;')
	$("#brandSelectedTags").attr("value","");
}
function removeBrandTags(tag,id){
	brandAllTags.pop(tag);
	$("#brand_tags_non_exist_tag_error").hide();
	$("#brand_tags_empty_error").hide();
	$("#brand_tags_category_limit_error_alert").hide();	
	$("#brand_tags"+id).remove()
	if(brandAllTags.length==0){
		$(".brand_selected_tags").hide();
	}
}
function setReviewTags(value){
	$("#review_tags_non_exist_tag_error").hide();
	$("#review_tags_empty_error").hide();
	$("#review_tags_limit_error_alert").hide();
	$(".review_selected_tags").show();
	if(reviewAllTags.length >=5){
		$("#review_tags_non_exist_tag_error").hide();
		$("#review_tags_empty_error").hide();
		$("#review_tags_limit_error_alert").show();
		return;
	}
	reviewAllTags.push(value);
	var id = reviewAllTags.length;
	$(".review_selected_tags").append('<span  id="review_tags'+id+'" class="tags_selected">'+value+'<a class="topic_remove" onclick="removeReviewTags(\''+value+'\',\''+id+'\')" href="">x</a></span>&nbsp;')
	$("#reviewSelectedTags").attr("value","");
}
function removeReviewTags(tag,id){
	reviewAllTags.pop(tag);
	$("#review_tags_non_exist_tag_error").hide();
	$("#review_tags_empty_error").hide();
	$("#review_tags_limit_error_alert").hide();	
	$("#review_tags"+id).remove()
	if(reviewAllTags.length==0){
		$(".review_selected_tags").hide();
	}
}

function setPic()
{
	document.getElementById("brandPicFile").click();	
}
var inputPic;
function readURL(input)
{
	$("#upload_image_message").hide();
	if(!checkFileType(input))
	{
		$("#upload_image_message").show();
		$("#upload_image_message").html("Select gif,jpg,jpeg,png,bmp image type");
		//$("#inputImage").val("");
	   return;
	}
	if(!checkFileSize(input))
	{
		$("#upload_image_message").show();
		$("#upload_image_message").html("file size should be less than 250 KB");
		return;
	}
	if (input.files && input.files[0])
    {
		inputPic = input.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(input.files[0]);
        reader.onload = function (e)
        {
        	$("#profile_image_container").attr("src",e.target.result);
        	//$("#profile_image_container").show();
        	$("#brandPicImg").attr("src",e.target.result);
		};
		reader.readAsDataURL(input.files[0]);    
    }	
}
function getBaseUrlOfSite(){
	var index = BASE_URL.lastIndexOf('/');
	var url = BASE_URL.substring(0,index + 1);
	return url;
}
var BASE_URL = location.protocol + "//" +"" + document.location.host + window.location.pathname;
angular.module('addBrandProfile', []).
	config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
	}])
	.service('brandProfileService',['$http', '$location', function($http, $location) {	
		return  {
			
		 	getAllTags:function(){
				var promise = $http({headers:header_encoding,  method: 'GET',url: 'api/tags'});
				return promise;
			},		
			addBrandProfile : function(brandName,brandShortDesc,brandLongDesc,brandTags,brandUrl) {
			   var brandData = {
					  name : brandName,
					  shortDescription : brandShortDesc,
					  longDescription : brandLongDesc,
					  tags : brandTags,
					  url:brandUrl
			    };
			    var promise = $http({headers:header_encoding,  method: 'POST',url: 'api/posts/brand',data:brandData});
				return promise;
			},
			getBrandByName:function(_name){
				var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/posts/brand?name='+_name});
				return promise;			
			},
			getBrandByUrl:function(_url){
				var promise = $http({  headers:header_encoding,method: 'GET',url: 'api/posts/brand?url='+_url});
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
				$http({  headers:header_encoding,method: 'POST',url: 'api/posts/review',data:reviewData}).
				  success(function (data, status, headers, config) {
					  if (data.status == 'SUCCESS') {
						  console.log("add review id is:"+data.response.id);
					  }
				  }).
				  error(function (data, status, headers, config) {
					  console.log(data);
				});
				
			},
			addBrandProfilePicture:function(id,image){
				var file_data = $("#brandPicFile").prop("files")[0];   	
				var form_data = new FormData();
				if(file_data!=undefined && file_data!=null){
					form_data.append("file", file_data);
					var url='api/posts/brand-'+id+'/profile-picture';
					$.ajax({
					    url: url,
					    cache: false,
					    async:false,
					    data: form_data,
					    processData: false,
					    contentType:false,                              
					    type: 'post',
					    success: function(result){
					 	// $(".login-user-image").attr("src",result.response.profile.profilePictureUrl);
					    }				    
					});
				}
			}
			/*addBrandProfile : function(image,brandName,brandShortDesc,brandLongDesc,brandTags) {
				var file_data = $("#brandPicFile").prop("files")[0];   	
            	var form_data = new FormData();                  		
                var promise = $http({  method: 'POST',url: 'api/brand/brand',haders: { 'Content-Type': false },transformRequest: function (data) {
		        		var formData = new FormData();
	                	formData.append("profile", angular.toJson(data.profile));
	                	formData.append("file", file_data);
	                	return formData;
	            	},data: { profile:brandProfileData,file:file_data}});
	            return promise;
		    }*/		
		};
	}])
	.controller('brandProfileCtrl',
			[ '$scope', 'brandProfileService', function($scope, brandProfileService) {
			$scope.categoryNames=[];
			$scope.file=null;
			$scope.uniqueNameError=false;
			$scope.uniqueUrlError=false;
			$scope.initFunctionCall = function(){
				var allCategoryPromise = brandProfileService.getAllTags();
				allCategoryPromise.success(function (data) {					 
					if(data.status=="SUCCESS"){
						var categoryArray = new Array();
						for(i=0;i<data.response.length;i++){
							$scope.categoryNames.push(data.response[i].name);
						}						
					}
					allTagsToFilter($scope.categoryNames);
				})
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });	
			},
			/**
			 * brand a name check exist or not
			 */
			$scope.brandNameExistCheck=function(){
				var brandExistPromiseObj = brandProfileService.getBrandByName($scope.brandName)
				brandExistPromiseObj.success(function (data) {
					if(data.status=="SUCCESS" && data.response!=null){
						$scope.uniqueNameError=true;
					}
					else{
						$scope.uniqueNameError=false;
					}
				})
				.error(function (data) {
			          console.log("In error "+data);
			          $scope.uniqueNameError=false;
				})
			    .then(function (response) {
				        	
			    });				

			},
			/**
			 * brand a url check exist or not
			 */
			$scope.brandUrlExistCheck=function(){
				var brandExistPromiseObj = brandProfileService.getBrandByUrl($scope.brandUrl)
				brandExistPromiseObj.success(function (data) {
					if(data.status=="SUCCESS" && data.response!=null){
						$scope.uniqueUrlError=true;
					}
					else{
						$scope.uniqueUrlError=false;
					}
				})
				.error(function (data) {
			          console.log("In error "+data);
			          $scope.uniqueEmailError=false;
				})
			    .then(function (response) {
				        	
			    });				

			},
			$scope.setFile = function (elem) {
		    	$scope.inputField = elem;
		    	$scope.file = elem.files[0];
		    	readURL(elem);
		    },		    
			$scope.addBrandProfile = function(){
		    	if(brandAllTags.length<=0){
		    		$("#brand_tags_empty_error").show();
		    		return;
		    	}
		    	var isAnonymousReview = $('#postAsAnonymousReview').is(':checked');						
				var isAnonymousFlag =isAnonymousReview;				
				var brandpromise = brandProfileService.addBrandProfile($scope.brandName,$scope.brandShortDesc,$scope.brandLongDesc,brandAllTags,$scope.brandUrl);
				var brandId;
				var brandName;
				brandpromise.success(function (data) {	
					if(data.status=="SUCCESS"){
						console.log(data.response.id);
						brandId  = data.response.id;
						brandName = data.response.name;
						brandProfileService.addBrandProfilePicture(brandId,inputPic);
						brandProfileService.addBrandReview(brandId,$scope.reviewTitle,$scope.reviewDesc,reviewAllTags,selectedRating,isAnonymousFlag);
						window.location.href = "brand/"+replaceSpaceWithhyphen(brandName);						
					}
				 })
				.error(function (data) {
					console.log("In error "+data);
			    })
			    .then(function (response) {
			    });
				
			};						
	} ]);
function likeToReview(){
	   selectedRating="1";
	   $("#likeReviewDiv").css("background-color","#FFBA52");
}