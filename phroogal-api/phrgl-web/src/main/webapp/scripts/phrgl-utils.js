$(document).ready(function (){
	
	// setting about us frame src	
	$("#phrgl-aboutus-frame").attr("src","//www.youtube.com/embed/qSiP_rQFKkU?rel=0&showinfo=0");	
	
	if (window.location.hash == '#_=_'){
	    history.replaceState 
	        ? history.replaceState(null, null, window.location.href.split('#')[0])
	        : window.location.hash = '';
	}
	if(currentLoginUserObj==null){
		$("#messageModelButton_div").show();
		$("#modal-footer-questionAdd").hide();
	}
	else{
		$("#messageModelButton_div").hide();
		$("#modal-footer-questionAdd").show();
	}	
	$('#midSearchCategory').keyup(function(e) {
		if (e.which === 188) {
	    	var val = $('#midSearchCategory').val();
	    	val=val.trim();
    		if(val=="," || val=="" || val==null || val==undefined){
    			$('#midSearchCategory').val("");
	    		return;
	    	}
	    	var found = checkTagsAvalable($('#midSearchCategory').val().substr(0,$('#midSearchCategory').val().length-1));
	    	if(found>=0){
	    		setMidCategories(allTagArray[found]);
	    		$('#midSearchCategory').val("");
	    	}
	    	else{
	    		$('#midSearchCategory').val($('#midSearchCategory').val().substr(0,$('#midSearchCategory').val().length-1));
	    		var _id= $("#user_id").text();
	    		var val = cammelCaseOfString($('#midSearchCategory').val());
	    		var checkFlag=tagWithSpecialCharacters(val);
	    		if(checkFlag){
	    			$("#question_tags_alert_message_middle").show();
	    			$("#question_tags_alert_message_middle").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
	    			return;
	    		}
	    		if(midAllCategories.length<5){
	    			documentScope.addUserTag(val,_id);
	    		}
	    		setMidCategories(val);	    		
	    	}
	    }
	    if (e.which === 13) {
	    	var val = cammelCaseOfString($('#midSearchCategory').val());
	    	val=val.trim();
	    	if(val=="" || val==null || val==undefined){
	    		return;
	    	}
	    	var _id= $("#user_id").text();
	    	var found=checkTagsAvalable(val);
	    	if(found < 0){
	    		var checkFlag=tagWithSpecialCharacters(val);
	    		if(checkFlag){
	    			$("#question_tags_alert_message_middle").show();
	    			$("#question_tags_alert_message_middle").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
	    			return;
	    		}
	    		if(midAllCategories.length<5){
	    			documentScope.addUserTag(val,_id);
	    		}
	    		setMidCategories(val);
	    	}
	    	else{
	    		setMidCategories(allTagArray[found]);
	    	}	    	
	    	$('#midSearchCategory').val("");	   
	    }
	});
	$('#rightSelectedCategory').keyup(function(e) {	
		if (e.which === 188) {
    		var val = $('#rightSelectedCategory').val();
    		val=val.trim();
    		if(val=="," || val=="" || val==null || val==undefined){
    			$('#rightSelectedCategory').val("");
	    		return;
	    	}	    
	    	var found=checkTagsAvalable($('#rightSelectedCategory').val().substr(0,$('#rightSelectedCategory').val().length-1));
	    	if(found>=0){
	    		setRightCategories(allTagArray[found]);
	    		$('#rightSelectedCategory').val("");	    		
	    	}
	    	else{
	    		$('#rightSelectedCategory').val($('#rightSelectedCategory').val().substr(0,$('#rightSelectedCategory').val().length-1));
	    		var _id= $("#user_id").text();
	    		var val = cammelCaseOfString($('#rightSelectedCategory').val());
	    		var checkFlag=tagWithSpecialCharacters(val);
	    		if(checkFlag){
	    			$("#question_tags_alert_message").show();
	    			$("#question_tags_alert_message").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
	    			return;
	    		}
	    		if(rightAllCategories.length<5){
	    			documentScope.addUserTag(val,_id);
	    		}
	    		setRightCategories(val);	    		
	    	}
	    }
	    if (e.which === 13) {
	    	var val = cammelCaseOfString($('#rightSelectedCategory').val());
	    	val=val.trim();
	    	if(val=="" || val==null || val==undefined){
	    		return;
	    	}
	    	var _id= $("#user_id").text();
	    	var found=checkTagsAvalable(val);
	    	if(found < 0){
	    		var checkFlag=tagWithSpecialCharacters(val);
	    		if(checkFlag){
	    			$("#question_tags_alert_message").show();
	    			$("#question_tags_alert_message").html(TAGS_CANT_ADD_SPECIAL_SYMBOL);
	    			return;
	    		}
	    		if(rightAllCategories.length<5){
	    			documentScope.addUserTag(val,_id);
	    		}
	    		setRightCategories(val);
	    	}
	    	else{
	    		setRightCategories(allTagArray[found]);
	    	}	    	
	    	$('#rightSelectedCategory').val("");	   
	    }
	});
	
	
// check login user
	$("textarea, input").focus(function(e){
		if(currentLoginUserObj==null && $(this)[0].id!="search-control" && $(this)[0].id!="search_home_control" && $(this)[0].id!="rememberMe" && $(this)[0].id!="login_email_box" && $(this)[0].id!="login_password_box" 
			&& $(this)[0].id!="inputFirstName" && $(this)[0].id!="inputLastName" && $(this)[0].id!="inputEmail" && $(this)[0].id!="inputPassword" && $(this)[0].id!="inputPasswordConfirm" && $(this)[0].id!="agree_terms_condition" && $(this)[0].id!="userAddTagInput" && $(this)[0].id!="inputImage")
		{
			$(this).blur();
			showLoginByQuestionRedirect();
	    }
	});
	$('#rightAddNewQuestionTitle').keyup(function(e) {
		if($("#rightAddNewQuestionTitle").val().length>=149){
			$("#modal-footer-question").hide();
			$("#modal-footer-answer").hide();
			$("#messageModelButton_div").show()
			$("#modal-footer-questionAdd").hide();
			showErrorMessage("Rephrase into a question and add details in question details section");
		}
	});	
	$('#middelAddNewQuestionTitle').keyup(function(e) {
		if($("#middelAddNewQuestionTitle").val().length>=149){
			$("#modal-footer-question").hide();
			$("#modal-footer-answer").hide();
			$("#messageModelButton_div").show()
			$("#modal-footer-questionAdd").hide();
			showErrorMessage("Rephrase into a question and add details in question details section");
		}
	});
	// hiding auto complete and datepicker on scroll of page
	$("#main-container-content").scroll(function () {    
		$( "#rightSelectedCategory" ).autocomplete('close');
		$( "#middleSelectedCategory" ).autocomplete('close');
		$("#midSearchCategory").autocomplete('close');
		$("#top_question_category_search").autocomplete('close');
		$("#userAddTagInput").autocomplete('close');
		$("#datepicker").datepicker("hide");
		$("#datepicker").datepicker().blur();
    });
});
function setCookie(c_name, value, exdays) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
    document.cookie = c_name + "=" + c_value;
}

function getCookie(c_name) {
    var i, x, y, ARRcookies = document.cookie.split(";");
    for (i = 0; i < ARRcookies.length; i++) {
        x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
        y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
        x = x.replace(/^\s+|\s+$/g, "");
        if (x == c_name) {
            return unescape(y);
        }
    }
}
var right_type=new Array(".gif",".jpg",".jpeg",".png",".bmp");
function checkFileType(input){
	var right_typeLen=right_type.length;    
	var fileName=input.value;
	var name=fileName.toLowerCase();
    var postfixLen=name.length;
    var len4=name.substring(postfixLen-4,postfixLen);
    var len5=name.substring(postfixLen-5,postfixLen);
    for (i=0;i<right_typeLen;i++)
    {
        if((len4==right_type[i])||(len5==right_type[i]))
        {
            return true;
        }
    }
    return false;	
}
var fSExt = new Array('Bytes', 'KB', 'MB', 'GB');
function checkFileSize(input){
	sizeinbytes = input.files[0].size;
	fSize = sizeinbytes; 
	var fSizeKB = parseInt(sizeinbytes/1024);
	if(fSizeKB > 250){
		return false;
	}
	else{
		return true;
	}    	
}
function removeLastLetter(string)
{
    return string.substring(0,string.length-1);
}
function replaceSpaceWithhyphen(string)
{
	string = string.trim();
	string = string.replace(/[`%~!@#$%^&*()_|+\-=?;:",<>\{\}\[\]\\\/]/gi, '-');
	string = string.replace('.','dot')
	return string.replace(/\s+/g,'-');
}
function replacehyphenWithSpace(string)
{
	string = string.replace('dot','.')
	return string.replace(/-/g,' ');
}
function expandTextarea(id) {
    var element = $('#'+id).get(0);	    
        element.addEventListener('keyup', function() {
        this.style.overflow = 'hidden';
        this.style.height = 0;
        this.style.height = this.scrollHeight + 'px';
    }, false);
}
// getting url parameters
function getURLParameter(name) {
	return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}
// month name map with number value
function getMonthNameByNumber(num){		
	var month_names_short=['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return month_names_short[parseInt(num)]
}
// check tags exist or not in all tags
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
// login and  signin form methods

function showLoginByQuestionRedirect(){
	  //$("#loginSignupFormModal_close").hide();
	  $("#signupFormModal_close").hide();
	  $('#login-signup-form_link').click();
	  $("#login-to-access-message").show();
 }
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
	  $("#signup_message").hide();
	  $("#signup_message").html("");
	  loginCommonScope.uniqueEmailError = false;
	  $(".sError").hide();
	  loginCommonScope.sFirstName="";
	  loginCommonScope.sLastName="";
	  loginCommonScope.sEmail="";
	  loginCommonScope.sConfirmPassword="";
	  loginCommonScope.sPassword=""; 	  
	  // reset login fields
	  loginCommonScope.email="";
	  loginCommonScope.password="";
	  $("#login_signup_message").hide();
 }
 function resetForgotFrom(){
	 loginCommonScope.forgotEmail="";    	  
 }
 
// url check http if not return with add http in url
 
 function addhttp(url) {
     if (!/^(f|ht)tps?:\/\//i.test(url)) {
        url = "http://" + url;
     }
     return url;
  }
 // capitlise first letter of string
 function capitaliseFirstLetter(string)
 {
	string = leftTrim(string);
    return string.charAt(0).toUpperCase() + string.slice(1);
 }
 // remove last letter of string
 function removeLastLetter(string)
 {
    return string.substring(0,string.length-1);
 }
 
 function leftTrim(string){
	 return string.replace(/^\s+/,"");
 }
 
 function openLinkToNewTab(string){
	 $('body').append('<div id="demoTextElm"></div>');
	 $("#demoTextElm").html(string);
	 var element = $("#demoTextElm");
     elements = element.find( "*" );
     for( i=0; i < elements.length; i++ ){
         if(elements[i].tagName=="a" ||  elements[i].tagName=="A")
         {
         	$(elements[i]).attr("target","_blank");
   	   	}
  	}
    var htmlStr=$("#demoTextElm").html();
    $("#demoTextElm").remove();
    return htmlStr;
 } 
 function formQuestionDetailRedirectUrl(link){
	 var urlRed =addhttp(getBaseUrlOfSite()+link);
	 return urlRed;
 }
 
 // redirec to url by ok press
 
 var newAddedQuestionLink="";
 function redirectToQuestinByOk()
 {
	 window.location.href = getBaseUrlOfSite()+newAddedQuestionLink;  
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
 
 function addUserTag(tag,_id){
	 var url=getBaseUrlOfSite()+'api/tags/user-'+_id;
	 var tagsData ={ "name":tag };
	 $.ajax({
		 url:url,
		 type:'POST',
	     data:tagsData,
	     contentType: "application/json; charset=utf-8",
	     dataType: "json",
	     success: function(data){
	    	 alert("Success:"+data);
		 },
		 error: function(data) {
				alert("Error:"+data);
		 }
	  });
 }
 function cammelCaseOfString(str){
	 str.trim();
	 var sp = str.split(' ');
	 var wl=0;
	 var f ,r;
	 var word = new Array();
	 for (i = 0 ; i < sp.length ; i ++ ) {
		 f = sp[i].substring(0,1).toUpperCase();
		 r = sp[i].substring(1);
		 word[i] = f+r;
	 }
	 newstring = word.join(' ');
	 return newstring;
 }
 
 function toUpperIWord(obj) {
	 var sp = obj.value.split(' ');
	 var wl=0;
	 var f ,r;
	 var word = new Array();
	 for (i = 0 ; i < sp.length ; i ++ ) {
	 //f = sp[i].substring(0,1).toUpperCase();
	 //r = sp[i].substring(1);
		 if(sp[i].length==1 && sp[i]=="i"){
			 word[i] = sp[i].toUpperCase();
		 }
		 else{
			 word[i]=sp[i]; 
		 }
		 //word[i] = f+r;
	 }
	 newstring = word.join(' ');
	 obj.value=newstring;
	 return true;
 }
 function toUpperIWordReturnString(str) {	
	 var sp = str.split(' ');
	 var wl=0;
	 var f ,r;
	 var word = new Array();
	 for (i = 0 ; i < sp.length ; i ++ ) {
		 if(sp[i].length==1 && sp[i]=="i"){
			 word[i] = sp[i].toUpperCase();
		 }
		 else{
			 word[i]=sp[i]; 
		 }
	 }
	 newstring = word.join(' ');
	 //obj.value=newstring;
	 return newstring;
 }
 function reverseOfString(str){
	 var s= '', L= str.length;
	 while(L){
	  s+= str[--L];
	 }
	 return s;
 }
 function addLeadingZero(n)
 {
	 return n<10 ? '0'+n : n;
 }
 
 var allTagArray =[];
	function allTagsToFilter(tags){	
		allTagArray=tags;
		$( "#midSearchCategory" ).autocomplete({
			autoFocus: true,
			source: function( request, response ) {
			var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			response( $.grep( tags, function( item ){
				return matcher.test(item);
			}) );
			},			
			select: function( event, ui ){
				$( "#midSearchCategory" ).val(ui.item.value);
				if(event.keyCode!=13){
					setMidCategories(ui.item.value);
				}
				return false;
			}
		});		
		$( "#rightSelectedCategory" ).autocomplete({
			autoFocus: true,
			source: function( request, response ) {
			var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			response( $.grep( tags, function( item ){
				return matcher.test( item );
			}) );
			},
			select: function( event, ui ){
				$( "#rightSelectedCategory" ).val(ui.item.value);
				if(event.keyCode!=13){
					setRightCategories(ui.item.value);
				}
				return false;
			}
		});
		$( "#top_question_category_search" ).autocomplete({
			autoFocus: true,
			source: function( request, response ) {
			var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			response( $.grep( tags, function( item ){
				return matcher.test( item );
			}) );
			},
			select: function( event, ui ) {			
				$( "#top_question_category_search" ).val(ui.item.value);
				if(event.keyCode!=13){
					questionCategoryAddNew(ui.item.value);
				}
				return false;
			}			
		});
		$( "#userAddTagInput" ).autocomplete({
			autoFocus: true,
			source: function( request, response ) {
			var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			response( $.grep( tags, function( item ){
				return matcher.test(item);
			}) );
			},			
			select: function( event, ui ){
				$( "#userAddTagInput" ).val(ui.item.value);
				if(event.keyCode!=13){
					documentScope.addUserTag();
				}
				return false;
			}
		});	
	}
	var midAllCategories = new Array();
	var rightAllCategories = new Array();
	
	function setRightCategories(value){
		if(rightAllCategories.length >=5){		
			$("#question_tags_alert_message").text(TAGS_UPPER_LIMIT_MESSAGE);
			$("#question_tags_alert_message").show();
			return;
		}
		else{
			$(".right_selected_categories").show();
			rightAllCategories.push(value);
			var id = rightAllCategories.length;
			$(".right_selected_categories").append('<span  id="right_question_tags_category'+id+'" class="question_tags_category">'+value+'<a class="topic_remove" onclick="removeRightCategory(\''+value+'\',\''+id+'\')" href="">x</a></span>&nbsp;')
			$("#rightSelectedCategory").attr("value","");
			$("#question_tags_alert_message").hide();
		}
	}
	function removeRightCategory(category,id){
		//rightAllCategories.pop(category);
		rightAllCategories.splice(rightAllCategories.indexOf(category),1); 
		$("#right_question_tags_category"+id).remove();
		if(rightAllCategories.length==0){
			$(".right_selected_categories").html("");
		}
		$("#question_tags_alert_message").hide();
	}
	
	
	function setMidCategories(value){
		if(midAllCategories.length >=5){
			$("#question_tags_alert_message_middle").text(TAGS_UPPER_LIMIT_MESSAGE);
			$("#question_tags_alert_message_middle").show();
			return;
		}
		else{
			$(".middle_selected_categories").show();
			midAllCategories.push(value);
			var id = midAllCategories.length;
			$(".middle_selected_categories").append('<span  id="mid_question_tags_category'+id+'" class="question_tags_category">'+value+'<a class="topic_remove" onclick="removeMidCategory(\''+value+'\',\''+id+'\')" href="#">x</a></span>&nbsp;')
			$("#midSearchCategory").attr("value","");
			$("#question_tags_alert_message_middle").hide();
		}
	}
	function removeMidCategory(category,id){
		//midAllCategories.pop(category);
		midAllCategories.splice(midAllCategories.indexOf(category),1); 
		$("#mid_question_tags_category"+id).remove()
		if(midAllCategories.length==0){
			$(".middle_selected_categories").html("");
		}
		$("#question_tags_alert_message_middle").hide();
	}
	function removeCommentsReplyButtonDisabled(id){
		$("#"+id).prop("disabled", false);
		$("#"+id).css('background-image','linear-gradient(to bottom, #FFFFFF, #E6E6E6)');
	}
	function setLoaderImageOnButton(id){
		var baseUrl=getBaseUrlOfSite();
		var imageUrl=baseUrl+"/img/loader.gif";
		$("#"+id).attr('disabled','disabled');
		//$("#"+id).html('Add Question');
		$("#"+id).css('background-image', 'url(' + imageUrl + ')');
		$("#"+id).css('background-position', '96%');
		$("#"+id).css('background-repeat', 'no-repeat');
	}
	function removeDisabledPropertyFromButton(id){
		$("#"+id).prop("disabled", false);
		$("#"+id).css('background-image','linear-gradient(to bottom, #0088CC, #0044CC)');
	}
	function removeLoaderImageOnButton(id){
		$("#"+id).prop("disabled", false);
		//$("#"+id).html('Add Question');
		$("#"+id).css('background-image','linear-gradient(to bottom, #FFC671, #FFA824)');
	}
	function tagWithSpecialCharacters(str){
		str=str.toLowerCase();
		if(str.indexOf(".com")>=0 && str.indexOf(".gov")>=0 ){
			return true;
		}
		str = str.replace('.com','a').replace('.gov','b');		
		if(/^[-()a-zA-Z0-9- ]*$/.test(str) == false){			
				return true;			   
		}
		else{
			return false;
		}
	}
	function showTwitterUserAddEmailForm(){
		  $('#add_email_form_link').click();
	}
    function updateTwitterLoginUserEmailAdress(){
    	var val=$("#inputAddEmail").val();
    	var firstName = $("#inputAddTwitterFirstName").val();
    	var lastName = $("#inputAddTwitterLastName").val();
    	if(val==null || val=="" || val==undefined){
    		$("#add_email_message").show();
    		$("#add_email_message").html("Please enter value in email field.");
    		return;
    	}
    	var userProfileData = [{
			"operation" : "REPLACE",
             "property":"profile.firstname",
             "value":firstName
         	},
         	{
			 "operation" : "REPLACE",
             "property":"profile.lastname",
             "value":lastName
         	},
         	{
			 "operation" : "REPLACE",
             "property":"profile.email",
             "value":val
         	}
        ];
    	$.ajax({ 
    		url: getBaseUrlOfSite()+'api/users/user-'+$("#current_login_user_id").text(),
    		type: "PATCH",
    	    contentType: "application/json",
    	    data: JSON.stringify(userProfileData),
    	    processData: false,
	        dataType: "json",
	        success: function(data)
	        { 	  
	        	$("#inputAddEmail").val("");
	        	$("#inputAddTwitterFirstName").val("");
	        	$("#inputAddTwitterLastName").val("");
	        	$("#addEmailFormModel").hide();
				$('#addEmailFormModel').modal('hide');
				$("#add_email_message").hide();
				$("#current_login_user_profile_email").text(val);
				location.reload();
		    }
    	});	   	
    }    
    function checkLoginUserEmailExist(id){
    	var checkFlag=false;
    	var usr;
    	$.ajax({  
	        async:false,
	        url: getBaseUrlOfSite()+'api/users/user-'+id,		        
	        success: function(data) { 	        	
	        	if(data.status == 'SUCCESS'){
	        		usr = data.response.profile;
	        		var userInfo=data.response;
	        		// showing admin  links on dropdown
	        		if(userInfo.grantedAuthorities[0]!="ADMIN"){
	        			$("#admin_li").hide();
	        			$("#admin_li_seprator").hide();
	        		}
	        		$('#authName').html(cammelCaseOfString(usr.firstname));
					if(userInfo.primarySocialNetworkConnection!=null && (usr.email==null|| usr.email=="")){
						checkFlag=true;
						documentScope.addTwitterFirstName=usr.firstname;
						documentScope.addTwitterLastName=usr.lastname;
					}
					else{
						checkFlag=false; 
					}
			    }      
    	    }
        });	
    	return checkFlag;
    }
    function tabsSpinnerShow(elm){
    	var tabTitle=replaceSpaceWithhyphen(elm.text());
		elm.attr("id",tabTitle+"-a");
    	var currentWidth=parseInt(elm.width());
		elm.css("width",currentWidth+16+"px");
		console.log("width is"+currentWidth);
		var baseUrl=getBaseUrlOfSite();
		var imageUrl=baseUrl+"img/loader.gif";
		$("#"+tabTitle+"-a").css('background-image', 'url(' + imageUrl + ')');
		$("#"+tabTitle+"-a").css('background-position', '96%');
		$("#"+tabTitle+"-a").css('background-repeat', 'no-repeat');
    }
    function tabsSpinnerHide(id){
    	$("#"+id).css('background-image','none');
    	var currentWidth=parseInt($("#"+id).width());
    	$("#"+id).css("width",currentWidth-16+"px");
    }
    function returnMatchedIndexOfArray(arr,val) {
        for(var i = 0; i < arr.length; i++) {
            if(arr[i].name == val) {
                return arr[i];
            }
        }
    }
    // redirect user profile page by user id
    function redirectToUserProfilePage(id) {
    	if (currentLoginUserObj == null) {
    	    showLoginByQuestionRedirect();
    	    return;
    	}
        if (id == null || id == "" || id == undefined) {
            return;
        }
        window.location.href = getBaseUrlOfSite() + "profile?user=" + id;
    }
    // declaring heders for request
    var header_encoding = {	"Accept-Encoding":"gzip"};
