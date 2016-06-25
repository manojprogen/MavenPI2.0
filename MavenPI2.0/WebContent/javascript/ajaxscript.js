var AjaxTCR = {};

AjaxTCR.comm = {
/* readyState constants as defined by w3c */
 UNSENT : 0,
 OPEN :   1,
 SENT : 2,
 LOADING : 3,
 DONE : 4,

/* Default Request Content Type */
DEFAULT_CONTENT_TYPE : "application/x-www-form-urlencoded",

/* Default timeout in ms */
DEFAULT_TIMEOUT : false,

/* Default number of retries if retrying requests */
DEFAULT_RETRIES : false,

/* Default time to revisit our progress callback if monitoring progress */
DEFAULT_PROGRESS_INTERVAL : 1000,

DEFAULT_REQUEST_SIGNATURE : "X-Signature",

/* the request id counter */
 _requestID : 0,

/* request counter shows outstanding requests */
_requestsOutstanding : 0,

/* the statuses for possible network errors */
/* 3507 = library error flag */
 _networkErrorStatus : new Array(0, 408, 504, 3507, 12002, 12007, 12029, 12030, 12031, 12152),


 /*****************************************  GETTERS/SETTERS ***********************************/

setDefault : function(option, value){
	AjaxTCR.comm[option] = value;
},

getDefault : function(option){
	return AjaxTCR.comm[option]
},

 /*
  * _createXHR - private wrapper function from chapter 3
  */
 _createXHR : function() {

 						  try { return new XMLHttpRequest(); } catch(e) {}
                          try { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); } catch (e) {}
                          try { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); } catch (e) {}
                          try { return new ActiveXObject("Msxml2.XMLHTTP"); } 	  catch (e) {}
                          try { return new ActiveXObject("Microsoft.XMLHTTP"); }  catch (e) {}

                          return null;
		 				 },

/*
 * sendRequest(url, object of communications options) - public method to create an Ajax request
 *
 */
 sendRequest : function (url,options) {

	var request = new Object();

	/* increment our requestId number */
	request.requestID = ++AjaxTCR.comm._requestID;

	/* basic communication defaults */
	request.method = "GET";
    request.async = true;
	request.preventCache = false;
	request.requestContentType = AjaxTCR.comm.DEFAULT_CONTENT_TYPE;
	request.requestContentTransferEncoding = "";
	request.payload = "";

	/* header management */
	request.headers = new Array();
	request.supressHeaders = false;

	/* standard callbacks */
	request.onSuccess = function(){};
	request.onFail = function(){};

	/* callbacks associated with readyState changes */
	request.onCreate = null;
	request.onOpen = null;
	request.onSent = null;
	request.onLoading = null;
	request.onReceived = null;

	/* communication status flags */
    request.abort = false;
	request.inProgress = true;
	request.received = false;

	/* progress management */
	request.showProgress = false;
	request.progressInterval = AjaxTCR.comm.DEFAULT_PROGRESS_INTERVAL;
	request.onProgress = function (){};
	request.progressTimerID = null;

	/* timeout parameters */
	request.timespent = 0;
	request.timeout = AjaxTCR.comm.DEFAULT_TIMEOUT;
	request.onTimeout = function(){};
	request.timeoutTimerID = null;

    /*  retry parameters */
	request.retries = AjaxTCR.comm.DEFAULT_RETRIES;
	request.retryCount = 1;
	request.onRetry = function (){};

	/* sequencing */
	request.inQueue = false;
	request.responseQueueID = 0;
	request.enforceOrder = false;

	/* cache management */

	request.cacheResponse = false;
	request.fromCache = false;

	/* Prefetch */
	request.onPrefetch = function(){};
	request.isPrefetch = false;

    /* payload serialization */
	request.serializeForm = null;
	request.hasFile = false;

    /* output handling */
	request.outputTarget = null;
	request.useRaw = true;

	/* transmission type */
	request.oneway = false;

	/* authentication */
 	request.username = null;
 	request.password = null;

 	/* security */
 	request.requestSignature = AjaxTCR.comm.DEFAULT_REQUEST_SIGNATURE;
 	request.signRequest = null;
 	request.signedResponse = false;


    /* apply options defined by user */
    for (option in options)
      request[option] = options[option];

	if (request.isPrefetch)
		request.cacheResponse = true;

	/* Serialize the given form if request.serialize is set */
	if (request.serializeForm)
	{
		/* Serialize given form */
		var newPayload = AjaxTCR.data.serializeForm(request.serializeForm,request.requestContentType);

		/* check to see if we have a fileupload situation */
		if (typeof(newPayload) == "string" && newPayload == "fileupload")
			request.hasFile = true;
		else
		{
			/* Check to see if payload exists */
			if (request.payload)
			{
				/* If payload is an object, use serializeObject otherwise append to end of the new payload */
				if (typeof(request.payload) == "object")
					newPayload = AjaxTCR.data.serializeObject(newPayload, request.payload, request.requestContentType);
				else if (request.requestContentType == AjaxTCR.comm.DEFAULT_CONTENT_TYPE)
					newPayload += "&" + request.payload;
			}

			request.payload = newPayload;

			/* Get all values into string format */
			if (request.requestContentType == "application/json")
	  			request.payload = AjaxTCR.data.encodeJSON(request.payload);
			else if (request.requestContentType == "text/xml")
				request.payload = AjaxTCR.data.serializeXML(request.payload);

			/* Encode it in base64 if that's set */
			if (request.requestContentTransferEncoding == "base64")
				request.payload = AjaxTCR.data.encode64(request.payload);
		}
	}

  	/* If there is a file, we need to handle differently */
	if (request.hasFile)
		AjaxTCR.comm._sendFormWithFile(request);
	else
	{
		/* address payload depending on method */
	    if (request.method.toUpperCase() == "GET")
	      request.url = url + "?" + request.payload;
		else
		  request.url = url;

		if (request.method.toUpperCase() == "POST")
	      request.postBody = request.payload;
		else
		  request.postBody = null;


		/* Add a queueID if necessary */
		if (request.enforceOrder)
			request.responseQueueID = AjaxTCR.comm.queue._responseQueue.maxID++;

		var cachedResponse = null;
		/* Check if the item is in the cache first */
		if (request.cacheResponse)
		{
			/* Check to see if we have a key for our cache */
			if (request.cacheKey == undefined)
				request.cacheKey = request.url;

			cachedResponse = AjaxTCR.comm.cache.get(request.cacheKey);
			if (cachedResponse)
				AjaxTCR.comm.cache._handleCacheResponse(request, cachedResponse);
		}

	    /* invoke the request */
		if (!cachedResponse)
			AjaxTCR.comm._makeRequest(request);
	}

    /* return object for local control */
    return request;
 },


 /*
  * abortRequest(requestObj)
  *
  * Public method that will abort any passed request object and clean up
  * any timers for showing requqest state
  *
  */
 abortRequest : function(request) {
 	                                 /* set the abort flag */
 	                                 request.abort = true;

									 /* clear inProgress flag */
 	                                 request.inProgress = false;

									 /* abort the request */
								     request.xhr.abort();

									 /* decrement outstand request count */
									 AjaxTCR.comm._requestsOutstanding--;

									 /* clear any timeout timers */
									 clearTimeout(request.timeoutTimerID);
									 request.timeoutTimerID = null;

									 /* stop showing progress */
		                             if (request.progressTimerID)
									 {
										clearTimeout(request.progressTimerID);
										request.progressTimerID = null;
									 }

									/* Remove Progress Indicators */
									if (request.statusIndicator)
										AjaxTCR.comm._removeProgressStatus(request.statusIndicator);

								   },

/*
 * _makeRequest(requestObj)
 *
 * Private method that actually creates XHR, binds callbacks and sends request
 *
 */

_makeRequest : function (request) {

									/* make basic XHR */
									request.xhr = AjaxTCR.comm._createXHR();
									if (!request.xhr)
      								  { /* raise exception */
	   									return;
	 								  }
									/* Increment total requests */
									AjaxTCR.comm.stats._commResults.totalRequests++;

									/* Display status and start Progress Callback */
									if (!request.oneway)
										AjaxTCR.comm._initSend(request);

									/* Call back for ready state 0 if set */
									if (request.onCreate)
									  request.onCreate(request);

	                              	/* open the request */
	                                request.xhr.open(request.method, request.url, request.async, request.username, request.password);

									/* clear an abort flag in case this is a retry */
									request.abort = false;

									/* set headers indicating we did this with Ajax and what
									   our transaction id is */
									if (!request.supressHeaders)
									  {
									   request.xhr.setRequestHeader("X-Requested-By","XHR");
									   request.xhr.setRequestHeader("X-Request-Id",request.requestID);
									  }

									/* set header(s) for POST */
									if (request.method.toUpperCase() == "POST")
									  {
									   request.xhr.setRequestHeader("Content-Type", request.requestContentType);
									   if (request.requestContentTransferEncoding != "")
										 request.xhr.setRequestHeader("Content-Transfer-Encoding", request.requestContentTransferEncoding);
									  }

									/* Prevent Caching if set */
									if (request.preventCache)
										request.xhr.setRequestHeader("If-Modified-Since", "Wed, 15 Nov 1995 04:58:08 GMT");

									/* set user defined headers */
									request.headerObj = {};
									for (var i=0; i<request.headers.length;i++)
									{
										if (request.headers[i].name.toUpperCase() == "COOKIE")
											document.cookie = request.headers[i].value;
										else if(request.headerObj[request.headers[i].name] === undefined)
											request.headerObj[request.headers[i].name] = request.headers[i].value;
										else
											request.headerObj[request.headers[i].name] =  request.headers[i].value + "," + request.headerObj[request.headers[i].name];
									}

									for (var header in request.headerObj)
										request.xhr.setRequestHeader(header, request.headerObj[header]);

									/* Set signature header */
 									 if (request.signRequest)
 									 	request.xhr.setRequestHeader(request.requestSignature, request.signRequest);

									if (!request.oneway)
									{
										/* bind the success callback */
										request.xhr.onreadystatechange = function () {AjaxTCR.comm._handleReadyStateChange(request);};

										/* set a timeout if set */
										if (request.async && request.timeout &&  request.timeoutTimerID == null)
		  									 request.timeoutTimerID = window.setTimeout( function(){AjaxTCR.comm._timeoutRequest(request);}, request.timeout);

	    							}

									/* send the request */
									request.xhr.send(request.postBody);
								 },
 /*
  * initSend(requestObject)
  *
  * Private method called to init items that should be initialized no matter what communication technique is used.
  *
  */

 _initSend : function(request){
									/* Set Progress Indicators */
									if (request.statusIndicator)
										request.statusIndicator.element = AjaxTCR.comm._setProgressStatus(request.statusIndicator);

									/* set the time spent in case this is a retry */
									request.timespent = 0;

									/* set progress indicator to show something nearly every second */
									if (request.showProgress && request.progressTimerID == null)
										 request.progressTimerID = window.setTimeout( function(){AjaxTCR.comm._progressRequest(request);}, request.progressInterval);

									/* Record start time of request */
									request.startTime = (new Date()).getTime();

									/* increment requests outstanding */
									AjaxTCR.comm._requestsOutstanding++;

						},

 /*
  * sendFormWithFile(requestObject)
  *
  * Private method called to submit form if there is a file in it.
  *
  */

 _sendFormWithFile : function(request) {
 										 /* Display status and start Progress Callback */
										 AjaxTCR.comm._initSend(request);

										 /* Increment total requests */
										 AjaxTCR.comm.stats._commResults.totalRequests++;

 										 var iframeID = "AjaxTCRIframe_" + request.requestID;

										 /* IE does not handle document.createElement("iframe"); */
										 if(window.ActiveXObject)
											var iframe = document.createElement('<iframe id="' + iframeID + '" name="' + iframeID + '" />');
										 else
										 {
 										 	var iframe = document.createElement("iframe");
											iframe.id = iframeID;
										    iframe.name = iframeID;
										 }

										 iframe.style.height = "1px";
										 iframe.style.visibility = "hidden";

										 document.body.appendChild(iframe);

										 var callback = function(){AjaxTCR.comm._handleIFrameResponse(request, iframe);};

										 /* IE does not recognize iframe.onload */
										 if(window.attachEvent)
										 	iframe.attachEvent('onload', callback);
										 else
											iframe.addEventListener('load', callback, false);


										 request.serializeForm.target = iframe.id;
										 request.serializeForm.submit();
									   },


_handleIFrameResponse : function(response, iframe){
													response.xhr = {};
													response.xhr.responseText = null;
													response.xhr.status = 200;


													if (iframe.contentWindow.document.body)
														response.xhr.responseText = iframe.contentWindow.document.body.innerHTML;

													if (iframe.contentWindow.document.XMLDocument)
														response.xhr.responseXML = iframe.contentWindow.document.XMLDocument;
													else
														response.xhr.responseXML = iframe.contentWindow.document;

													AjaxTCR.comm._handleResponse(response);

													/* This line causes the page to not stop spinning */
													//document.body.removeChild(iframe);
											      },

 /*
  * timeoutRequest(requestObject)
  *
  * Private method called if timeout set after 5000 ms or a user defined time.
  * Uses public abortRequest method to clean up timers and invokes any special timeout
  * callback that may be defined.
  *
  */

 _timeoutRequest : function(request) {
                                      /* make sure it is a proper time to abort */
                                      if (request.xhr.readyState != AjaxTCR.comm.DONE &&
									      request.xhr.readyState != AjaxTCR.comm.UNSENT)
                                       {
									   	/* abort the request */
		                                AjaxTCR.comm.abortRequest(request);

										/* Increment total timeouts */
										AjaxTCR.comm.stats._commResults.totalTimeouts++;

										/* do we need to retry? */
										if (request.retries)
										  AjaxTCR.comm._retryRequest(request);
										else
										{
											if (request.statusIndicator)
												AjaxTCR.comm._setErrorStatus(request.statusIndicator);
										  request.onTimeout(request);  /* invoke any timeout callback */
										  AjaxTCR.comm.queue._checkRequestQueue(request);
										}
                                       }
                                     },

/*
  * progressRequest(requestObject)
  *
  * Private method called each second if showProgress is set.
  * Updates public timespent variable and invokes any special progress
  * callback that may be defined.
  *
  */

_progressRequest: function(request){
									if (!request.abort && !request.received)
   									  {

										request.timespent =  Math.round((request.timespent + (parseInt(request.progressInterval) / 1000)) * 1000) / 1000;
										request.onProgress(request);
										/* Yes it is ridiculous that we have to clear the timeout that we are in a callback for, but such is IE */
										clearTimeout(request.progressTimerID);
										request.progressTimerID = null;
										request.progressTimerID = window.setTimeout( function(){AjaxTCR.comm._progressRequest(request);}, request.progressInterval);
									  }
								   },

_retryRequest : function (request) {
	 									   /* up our retry count */
										   request.retryCount++;

										   /* make sure we aren't done retrying */
										   if (request.retryCount <= request.retries)
										   {
											  if (request.statusIndicator)
												AjaxTCR.comm._setRetryStatus(request.statusIndicator);

											 /* Increment total retries */
										     AjaxTCR.comm.stats._commResults.totalRetries++;
										     AjaxTCR.comm._makeRequest(request);
											 request.onRetry(request);
										   }
										   else /* stop trying and perform callback */
										   {
										    if (request.statusIndicator)
												AjaxTCR.comm._setErrorStatus(request.statusIndicator);
											request.onTimeout(request);
											AjaxTCR.comm.queue._checkRequestQueue(request);
										   }
										 },

/*
  * handleResponse(requestObject)
  *
  * Private method called after response comes back no matter what method of communication
  */
_handleResponse : function(response){
										/* Record end time of request */
										response.endTime = (new Date()).getTime();

										/* set a received flag to ensure you don't perform a progress callback after received. */
										response.received = true;

										 /* clear any timeouts */
		                                 if (response.timeoutTimerID)
										 {
		                                   clearTimeout(response.timeoutTimerID);
										   response.timeoutTimerID = null;
										 }

			                             /* clear our progress indicator */
			                             if (response.progressTimerID)
										 {
										   clearTimeout(response.progressTimerID);
										   response.progressTimerID = null;
										 }

										 /* decrement outstand request count */
										 AjaxTCR.comm._requestsOutstanding--;

										 /* Cache Response */
										if (!response.fromCache && response.cacheResponse && response.xhr.status == 200  && !response.fail)
											AjaxTCR.comm.cache.add(response.cacheKey, response.xhr.responseText);

										/* Remove Progress Indicators */
										if (response.statusIndicator)
										{
											AjaxTCR.comm._removeProgressStatus(response.statusIndicator);
											AjaxTCR.comm._removeRetryStatus(response.statusIndicator);
										}

										/* Check to see if we need to wait for another request */
										/* Otherwise just handle callbacks */
										 if (response.enforceOrder)
										   AjaxTCR.comm.queue._handleQueue(response);
										 else
										   AjaxTCR.comm._handleCallbacks(response);

										 /* If Request Queue is being used, send next request */
										AjaxTCR.comm.queue._checkRequestQueue(response);
									},
/*
  * handleCallbacks(requestObject)
  *
  * Private method called after response comes in to run numerous callbacks.
  * Checks http and response status to see which callbacks should be employed.
  */

_handleCallbacks : function(response) {
										 /* Danger: Firefox problems so we try-catch here */
										 try { response.httpStatus = response.xhr.status; response.httpStatusText = response.xhr.statusText} catch(e) {response.httpStatus=3507;response.httpStatusText="Unknown Loss";}

										 /* clear inProgress flag */
 	                                 	 response.inProgress = false;

										 /* Check if user wants to automatically consume output */
										 if (response.outputTarget && response.useRaw)
										 {
											var outputTarget = response.outputTarget;
											if (outputTarget && typeof(outputTarget) == "string")
												outputTarget = document.getElementById(outputTarget);

										 	if (response.fail)
 												outputTarget.innerHTML = response.fail;
 											else
 										 		outputTarget.innerHTML = response.xhr.responseText;
										 }


										 /* check to see if the user wants a specific callback for this request */
										 if (response["on" + response.httpStatus])
 										 	response["on" + response.httpStatus](response);

										 /* see if it is one of our retry statuses */
										 if (response.retries)
										 {
											for (var i=0;i<AjaxTCR.comm._networkErrorStatus.length;i++)
											{
												if (response.httpStatus == AjaxTCR.comm._networkErrorStatus[i])
												{
													AjaxTCR.comm._retryRequest(response);
													return;
												}
											}
										 }


										/* call either success or fail callback */
										if (response.httpStatus == 200)
										{
											/*if they specified expected content type, we check for that.*/
										    if (response.fail)
 												AjaxTCR.comm._handleFail(response, response.fail);
 										    else if (response.responseContentType)
										    {
												var responseContentType = response.xhr.getResponseHeader("Content-Type");
												responseContentType = responseContentType.substring(0, responseContentType.indexOf(";"));
												if (responseContentType != response.responseContentType)
										            AjaxTCR.comm._handleFail(response, "Wrong Content-Type: " + responseContentType );
												else if (response.responseContentType == "text/xml" && (response.xhr.responseXML == null || response.xhr.responseXML.childNodes.length == 0 || response.xhr.responseXML.childNodes[0].nodeName == "parsererror"))
													AjaxTCR.comm._handleFail(response, "Invalid XML Data");
												else
													AjaxTCR.comm._handleSuccess(response);
											}
											else
												AjaxTCR.comm._handleSuccess(response);
										}
										else
										 	AjaxTCR.comm._handleFail(response, response.httpStatus + " " + response.httpStatusText);




									    /* clear out the response */
										response = null;
                               	      },

 /*
  * handleFail(requestObject)
  *
  * Private method to call fail callback
  *
  */

 _handleFail : function(response, message) {
											/* Increment total fails */
										    AjaxTCR.comm.stats._commResults.totalFails++;

											/* Save fail details */
											var fail = {};
											fail.url = response.url;
											fail.status = response.httpStatus;
											fail.message = message;
											AjaxTCR.comm.stats._commResults.requestFails.push(fail);

											if (request.statusIndicator)
												AjaxTCR.comm._setErrorStatus(request.statusIndicator);

											response.onFail(response, message);
										   },

/*
  * handleSuccess(requestObject)
  *
  * Private method to handle success responses
  *
  */

 _handleSuccess : function(response) {
										/* Increment total success */
									    AjaxTCR.comm.stats._commResults.totalSuccess++;
										if (response.isPrefetch)
											response.onPrefetch(response);
										else
											response.onSuccess(response);
									 },

 /*
  * handleReadyStatueChange(requestObject)
  *
  * Private method to check for response status, clear any timeouts and invoke success callback
  *
  */

 _handleReadyStateChange : function(response) {
		         					  /* check if abort flag is set, if so bail out */
		                              if (response.abort)
		                                 return;

									   /* Check each readyState */
									  if (response.xhr.readyState == AjaxTCR.comm.OPEN && response.onOpen)
									  	 response.onOpen(response);
								      else if (response.xhr.readyState == AjaxTCR.comm.SENT && response.onSent)
									  	 response.onSent(response);
									  else if (response.xhr.readyState == AjaxTCR.comm.LOADING && response.onLoading)
									  {
										 //update status
										 if (response.statusIndicator)
										 	AjaxTCR.comm._setReceivingDataStatus(response.statusIndicator);
									  	 response.onLoading(response);
									  }
 	                                  else if (response.xhr.readyState == AjaxTCR.comm.DONE)
									  {
										if (response.signedResponse)
 										{
 											var signature = response.xhr.getResponseHeader("Content-MD5");
 											var verifySignature = AjaxTCR.data.encodeMD5(response.xhr.responseText);
 											if (signature != verifySignature)
 												response.fail = "Response Packet Compromised.";
 										}

 										if (response.onReceived && !response.fail)
											response.onReceived(response);

	                                    AjaxTCR.comm._handleResponse(response);
									  }

									 },

_setStatus : function(statusIndicator){
								if (statusIndicator.target)
								{
									if (typeof(statusIndicator.target) == "string")
										statusIndicator.target = document.getElementById(statusIndicator.target);

								    if (statusIndicator.type == "text")
								    {
								        var statusDiv = document.createElement("div");
										if (statusIndicator.sending)
											statusDiv.innerHTML = statusIndicator.sending.text;
										else
								        	statusDiv.innerHTML = statusIndicator.text;

								        if (statusIndicator.className)
											statusDiv.className = statusIndicator.className;
										statusIndicator.target.appendChild(statusDiv);
										statusIndicator.element = statusDiv;
									}
								    else if (statusIndicator.type == "image")
								    {
								        var statusImg = document.createElement("img");
								        statusImg.id = "progressBar";
										if (statusIndicator.border)
								        	statusImg.border=statusIndicator.border;
										if (statusIndicator.className)
											statusImg.className = statusIndicator.className;
										if (statusIndicator.sending)
											statusImg.src = statusIndicator.sending.imgSrc;
										else
											statusImg.src = statusIndicator.imgSrc;
								        statusIndicator.target.appendChild(statusImg);
										statusIndicator.element = statusImg;
								    }

									if (statusIndicator.toggleTargetVisibility)
										statusIndicator.target.style.visibility = "visible";

								 }
							},

_setReceivingDataStatus : function(statusIndicator){
								if (statusIndicator.progress && statusIndicator.progress.receiving)
								{
									if (statusIndicator.progress.type == "text")
								        statusIndicator.progress.element.innerHTML = statusIndicator.progress.receiving.text;
									else if (statusIndicator.progress.type == "image")
								        statusIndicator.progress.element.src = statusIndicator.progress.receiving.imgSrc;
								}
							},
_setProgressStatus : function(statusIndicator){
								if (statusIndicator.progress)
									return AjaxTCR.comm._setStatus(statusIndicator.progress);

							},
_setRetryStatus : function(statusIndicator){
								if (statusIndicator.retry)
								{
									AjaxTCR.comm._removeRetryStatus(statusIndicator);
									AjaxTCR.comm._setStatus(statusIndicator.retry);
								}

							},


_setErrorStatus : function(statusIndicator){
								if (statusIndicator.retry)
									AjaxTCR.comm._removeRetryStatus(statusIndicator);

								if (statusIndicator.error)
									AjaxTCR.comm._setStatus(statusIndicator.error);

							},

_removeStatus : function(statusIndicator){
								if (statusIndicator.element)
								{
									if (statusIndicator.toggleTargetVisibility)
										statusIndicator.target.style.visibility = "hidden";

									statusIndicator.element.parentNode.removeChild(statusIndicator.element);
									statusIndicator.element = null;
								}
							},

_removeProgressStatus : function (statusIndicator){
									if (statusIndicator.progress)
										AjaxTCR.comm._removeStatus(statusIndicator.progress);
								},

_removeRetryStatus : function (statusIndicator){
									if (statusIndicator.retry)
										AjaxTCR.comm._removeStatus(statusIndicator.retry);
								},

_removeErrorStatus : function (statusIndicator){
									if (statusIndicator.error)
										AjaxTCR.comm._removeStatus(statusIndicator.error);
								}
};


/*************************************  AjaxTCR.comm.cache *****************************/
AjaxTCR.comm.cache = {

/****************************************** Private Properties ****************************************************/

/* The cache object */
_cache : new Array(),

/* Caching Options w/defaults */
_cacheOptions : {
	/* The max number of items to store in the cache */
	size : 100,
	/* The default algorithm for removing items.  The choices are LRU, FIFO, and LFU */
	algorithm: "LRU",
	/* The default number of minutes an item can stay in the cache.  Set to -1 for forever */
	expires: 60
},

/*************************************  Public Cache Methods *****************************/

/*
 * add
 *
 * Public method to add a key/value pair to the cache
 *
 */
add : function(key, val){
	if (AjaxTCR.comm.cache._cache.length >= AjaxTCR.comm.cache._cacheOptions.size)
	{
		var algorithm = AjaxTCR.comm.cache._cacheOptions.algorithm;
		//we need to remove an item before adding another one.
		if ( algorithm == "FIFO")
			AjaxTCR.comm.cache._cache.splice(0, 1);
		else if (algorithm == "LFU")
		{
			var removeIndex = -1;
			for (var i=0;i<AjaxTCR.comm.cache._cache.length;i++)
			{
				if (removeIndex == -1 || AjaxTCR.comm.cache._cache[removeIndex].totalAccessed > AjaxTCR.comm.cache._cache[i].totalAccessed)
					removeIndex = i;
			}

			AjaxTCR.comm.cache._cache.splice(removeIndex,1);
		}
		else if (algorithm == "LRU")
		{
			var removeIndex = -1;
			for (var i=0;i<AjaxTCR.comm.cache._cache.length;i++)
			{
				if (removeIndex == -1 || AjaxTCR.comm.cache._cache[removeIndex].lastAccessed > AjaxTCR.comm.cache._cache[i].lastAccessed)
					removeIndex = i;
			}

			AjaxTCR.comm.cache._cache.splice(removeIndex,1);
		}
	}

	var item = AjaxTCR.comm.cache._createCacheItem(key, val);
	AjaxTCR.comm.cache._cache.push(item);
},

/**
 * clear - Public method that resets the caches
 *
 */
clear : function(){
	AjaxTCR.comm.cache._cache = new Array();
},

/*
 * get
 *
 * Public method to fetch an object based on the key
 *
 */
get: function(key){
	var cacheObject = null;
	/* Search for item */
	for (var i=0;i<AjaxTCR.comm.cache._cache.length;i++)
	{
		if (AjaxTCR.comm.cache._cache[i].key == key)
		{
			cacheObject = AjaxTCR.comm.cache._cache[i];
			break;
		}
	}

	if (cacheObject)
	{
		/* Update the properties */
		cacheObject.lastAccessed = new Date();
		cacheObject.totalAccessed++;

		/* Ensure it hasn't expired */
		if (AjaxTCR.comm.cache._cacheOptions.expires != -1)
		{
			var timeAdded = cacheObject.added;
			var now = new Date();
			now.setMinutes(now.getMinutes() - AjaxTCR.comm.cache._cacheOptions.expires);
			if (now > timeAdded)
			{
				AjaxTCR.comm.cache.remove(key);
				cacheObject = null;
			}
		}
	}

	if (cacheObject)
		return cacheObject.value;
	else
		return null;
},


getAll : function(){
	return AjaxTCR.comm.cache._cache;
},

getSize : function(){
	return AjaxTCR.comm.cache._cache.length;
},

/*
 * remove
 *
 * Public method to remove an item from the cache based on the key
 *
 */
remove : function(key){
	for (var i=0;i<AjaxTCR.comm.cache._cache.length;i++)
	{
		if (AjaxTCR.comm.cache._cache[i].key == key)
		{
			AjaxTCR.comm.cache._cache.splice(i,1);
			break;
		}
	}
},

setOptions : function(cacheOptions){
	/* apply options defined by user */
    for (option in cacheOptions)
    	AjaxTCR.comm.cache._cacheOptions[option] = cacheOptions[option];
 },



/*************************************  Private Cache Methods *****************************/

_handleCacheResponse : function(response, responseText){
	response.xhr = {};
	response.xhr.responseText = response.responseText = responseText;
	response.xhr.responseXML = response.responseXML = null;

	response.xhr.status = response.httpStatus = 200;
	response.xhr.statusText = response.httpStatusText = "OK";

	response.fromCache = true;
	AjaxTCR.comm._handleResponse(response);
},


/**
 * createCacheItem - Private method that creates a cache object based on the given key and val
 *
 */
_createCacheItem : function(key, val){
	var cacheObject = {};
	cacheObject.key = key;
	cacheObject.value = val;
	cacheObject.lastAccessed = new Date();
	cacheObject.added = new Date();
	cacheObject.totalAccessed = 1;
	return cacheObject;
}


};

/*************************************  AjaxTCR.comm.queue *****************************/
AjaxTCR.comm.queue = {

/****************************************** Private Properties ****************************************************/

/* The responseQueue Object */
 _responseQueue : {  queue: new Array(), currentIndex: 0, maxID: 0},

 /* The requestQueue Array */
 _requestQueue : new Array(),

 /* The requestQueue counter */
 _requestQueueID: 0,

 /* The number of active requests when using request Queue */
 requestQueueConcurrentRequests : 1,

/****************************************** Public Queue Methods ****************************************************/

/**
 * add - Public method to add a request to the request queue.
 *
 * @param url - the url to add to the request queue
 * @param options - the options to use for the call
 * @param priority - the priority level of the entry
 */
add : function(url, options, priority) {
	if (options)
		options.inQueue = true;
	else
		options = {inQueue:true};

	if (!priority)
		options.priority = "normal";
	else
		options.priority = priority.toLowerCase();

	/* Add Id */
	options.requestQueueID = ++AjaxTCR.comm.queue._requestQueueID;

	/* See if we should send it or add it to the queue */
	if (AjaxTCR.comm.stats.getRequestCount("active") >=  AjaxTCR.comm.queue.requestQueueConcurrentRequests)
	{
		var request = {url: url, options: options};
		if (options.priority == "next")
			AjaxTCR.comm.queue._requestQueue.unshift(request);
		else if (priority && priority == "faster")
		{
			var set = false;
			for (var i=0;i<AjaxTCR.comm.queue._requestQueue.length;i++)
			{
				if (AjaxTCR.comm.queue._requestQueue[i].options.priority == "normal")
				{
					AjaxTCR.comm.queue._requestQueue.splice(i, 0, request);
					set = true;
					break;
				}
			}
			/* If nothing is normal, add to the end */
			if (!set)
				AjaxTCR.comm.queue._requestQueue.push(request);
		}
		else
			AjaxTCR.comm.queue._requestQueue.push(request);
	}
	else
		AjaxTCR.comm.sendRequest(url, options);

	return options.requestQueueID;
},

/**
 * clearRequestQueue - A public method that clears out the request queue of any pending requests
 *
 */
clear: function()	{
	AjaxTCR.comm.queue._requestQueue.length = 0;
},

/**
 * get - Returns the item from the queue
 *
 *  @param ID - ID of request that you wish to check
 *  @return  The object that is stored in the queue
 *
 */
get : function(ID)	{
	for (var i=0;i<AjaxTCR.comm.queue._requestQueue.length;i++)
	{
		if ( AjaxTCR.comm.queue._requestQueue[i].options.requestQueueID == ID)
			return AjaxTCR.comm.queue._requestQueue[i];
	}

	return null;
},

/**
 * getAll - returns the request queue
 *
 * @return the request queue
 */
getAll : function(){
	return AjaxTCR.comm.queue._requestQueue;
},

/**
 * getPosition - Returns the position in the queue.
 *
 *  @param ID - ID of request that you wish to check
 *  @return  Returns -1 if not in queue.  Starts at 0.
 *
 */
getPosition : function(ID)	{
	for (var i=0;i<AjaxTCR.comm.queue._requestQueue.length;i++)
	{
		if ( AjaxTCR.comm.queue._requestQueue[i].options.requestQueueID == ID)
			return i;
	}

	return -1;
},

/**
 * getSize - returns the length request queue
 *
 * @return the request queue length
 */
getSize : function(){
	return AjaxTCR.comm.queue._requestQueue.length;
},

/**
 * remove - A public method that removes the option with the given requestQueueID
 *
 *  @param ID - ID of request to be removed from queue
 *
 */
remove : function(ID)	{
	for (var i=0;i<AjaxTCR.comm.queue._requestQueue.length;i++)
	{
		if ( AjaxTCR.comm.queue._requestQueue[i].options.requestQueueID == ID)
		{
			var ret = AjaxTCR.comm.queue._requestQueue[i];
			AjaxTCR.comm.queue._requestQueue.splice(i, 1);
			return ret;
		}
	}

	return false;
},


/****************************************** Private Queue Methods ****************************************************/

/*
 * _checkRequestQueue(requestObject)
 *
 * A private method that looks to see if a request queue is in use and sends the next
 * request off for processing if there is one
 *
 */
_checkRequestQueue : function(response){
	/* If Request Queue is being used, send next request */
	if (response.inQueue && AjaxTCR.comm.queue._requestQueue.length > 0)
	{
		var nextRequest = AjaxTCR.comm.queue._requestQueue.shift();
		AjaxTCR.comm.sendRequest(nextRequest.url, nextRequest.options);
	}
},

/*
  * handleQueue(requestObject)
  *
  * Private method called after response comes in if request is in queue.
  * Adds response to queue and then calls callbacks for any that are able to move forward
  */

_handleQueue: function(response){
	/* add response into queue */
	AjaxTCR.comm.queue._responseQueue.queue[response.responseQueueID] = response;

	/* loop thru queue handling any received requests up to current point  */
	while (AjaxTCR.comm.queue._responseQueue.queue[AjaxTCR.comm.queue._responseQueue.currentIndex] != undefined)
	{
	  AjaxTCR.comm._handleCallbacks(AjaxTCR.comm.queue._responseQueue.queue[AjaxTCR.comm.queue._responseQueue.currentIndex]);
	  AjaxTCR.comm.queue._responseQueue.currentIndex++;
	}
}


};

/*************************************  AjaxTCR.comm.stats *****************************/

AjaxTCR.comm.stats = {

 /* Collect data across all requests */
 _commResults : {
 totalRequests : 0,
 totalTimeouts : 0,
 totalRetries : 0,
 totalSuccess : 0,
 totalFails : 0,
 requestFails : new Array()},


collect : function (url)	{
	var sendConnectionStats = function(){
		var results = AjaxTCR.comm.stats.get();
		if (results.totalRequests > 0)
		{
			var payload = AjaxTCR.data.encodeJSON(results);
			AjaxTCR.comm.sendRequest(url, {method:"POST",payload:payload,requestContentType:"application/json",oneway:true});
		}
	};

	if(window.attachEvent)
 		window.attachEvent('onunload', sendConnectionStats);
 	else
		window.addEventListener('unload', sendConnectionStats, false);

},


get : function()	{
	return AjaxTCR.comm.stats._commResults;
},


/**
 * getRequestCount - Public method acting as simple getter for the count of requests currently out
 *
 * @return - the number of outstanding requests
 */
getRequestCount : function(type) {
	if (type == "queued")
		return AjaxTCR.comm.queue.getSize();
	else if (type == "active")
		return AjaxTCR.comm._requestsOutstanding;
	else
		return (AjaxTCR.comm.queue.getSize() + AjaxTCR.comm._requestsOutstanding);
}


};

/*************************************  AjaxTCR.comm.cookie *****************************/

AjaxTCR.comm.cookie = {
get : function(name){
	var fullname = name + "=";
	var cookies = document.cookie.split(';');
	for(var i=0;i < cookies.length;i++)
	{
		var cookieNV = cookies[i];
	    while (cookieNV.charAt(0)==' ')
											cookieNV = cookieNV.substring(1);
	    if (cookieNV.indexOf(fullname) == 0)
											return cookieNV.substring(fullname.length);
	}
	return null;
}

};



AjaxTCR.data = {

/*
 * encodeValue(str)
 *
 * Public method to encode passed string values to make the URL safe.
 * Strictly encodes to x-www-urlencoded format vs. native methods.
 */

encodeValue : function(val) {
                             var encodedVal;

                             if (!encodeURIComponent)
                               {
                                encodedVal = escape(val);
                                /* fix the omissions */
	                            encodedVal = encodedVal.replace(/@/g, '%40');
	                            encodedVal = encodedVal.replace(/\//g, '%2F');
	                            encodedVal = encodedVal.replace(/\+/g, '%2B');
                               }
                            else
                               {
                                encodedVal = encodeURIComponent(val);
	                            /* fix the omissions */
	                            encodedVal = encodedVal.replace(/~/g, '%7E');
	                            encodedVal = encodedVal.replace(/!/g, '%21');
	                            encodedVal = encodedVal.replace(/\(/g, '%28');
	                            encodedVal = encodedVal.replace(/\)/g, '%29');
	                            encodedVal = encodedVal.replace(/'/g, '%27');
                               }

                            /* clean up the spaces and return */
                            return encodedVal.replace(/\%20/g,'+');
                           } ,

/*
 * encodeAsHTML(str)
 *
 * Public method to convert tag characters to &lt; and &gt; and change \n to <br />
 */
encodeAsHTML : function(str) {
								var convertedString = str.replace(/<([^>]*)>/g, "&lt;$1&gt;")
								convertedString = convertedString.replace(/\n/g, "<br/>");
								return convertedString;
						   },

/*
 * encode64(str)
 *
 * Public method to encode passed string values into base64.
 */
encode64 : function(inputStr)
{
   var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
   var outputStr = "";
   var i = 0;

   while (i<inputStr.length)
   {
      var byte1 = inputStr.charCodeAt(i++);
      var byte2 = inputStr.charCodeAt(i++);
      var byte3 = inputStr.charCodeAt(i++);

      var enc1 = byte1 >> 2;
      var enc2 = ((byte1 & 3) << 4) | (byte2 >> 4);

	  var enc3, enc4;
	  if (isNaN(byte2))
		enc3 = enc4 = 64;
	  else
	  {
      	enc3 = ((byte2 & 15) << 2) | (byte3 >> 6);
		if (isNaN(byte3))
         	enc4 = 64;
		else
	      	enc4 = byte3 & 63;
	  }

      outputStr +=  b64.charAt(enc1) + b64.charAt(enc2) + b64.charAt(enc3) + b64.charAt(enc4);
   }

   return outputStr;
},

/*
 * decode64(str)
 *
 * Public method to decode passed string values from base64.
 */
decode64 : function(inputStr)
{
   var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
   var outputStr = "";
   var i = 0;
   inputStr = inputStr.replace(/[^A-Za-z0-9\+\/\=]/g, "");

   while (i<inputStr.length)
   {
      var dec1 = b64.indexOf(inputStr.charAt(i++));
      var dec2 = b64.indexOf(inputStr.charAt(i++));
      var dec3 = b64.indexOf(inputStr.charAt(i++));
      var dec4 = b64.indexOf(inputStr.charAt(i++));

      var byte1 = (dec1 << 2) | (dec2 >> 4);
      var byte2 = ((dec2 & 15) << 4) | (dec3 >> 2);
      var byte3 = ((dec3 & 3) << 6) | dec4;

      outputStr += String.fromCharCode(byte1);
      if (dec3 != 64)
	  	outputStr += String.fromCharCode(byte2);
      if (dec4 != 64)
         outputStr += String.fromCharCode(byte3);
   }

   return outputStr;
},

/*
 * serializeForm(form, encoding, trigger, evt)
 *
 * Public method to create a serialized payload based on the contents of a form and the specified encoding value.
 * trigger and evt are optional and used to firm up the accuracy between what the browser would send and what we send.
 */

serializeForm : function(form, encoding, trigger, evt) {
														 if (typeof(form) == "string")
														 {
															var formObject = document.forms[form];
															if (formObject == null)
																formObject = document.getElementById(form);

															form = formObject;
														 }
														 var x=0,y=0;
														 if (trigger && trigger.type == "image" && trigger.name)
														 {
														 	if (window.event)
															{
																x = window.event.offsetX;
																y = window.event.offsetY;
															}
															else if (evt.target)
															{
																var coords = {x: 0, y: 0 };
																var elmt = trigger;
																while (elmt)
																{
																	coords.x += elmt.offsetLeft;
																	coords.y += elmt.offsetTop;
																	elmt = elmt.offsetParent;
																}

																x = evt.clientX + window.scrollX - coords.x - 1 ;
																y = evt.clientY + window.scrollY - coords.y - 1;
															}
														 }

														 var formValues = AjaxTCR.data._beginEncode(encoding);
														 for (var i =0; i < form.elements.length; i++)
														 {
														  var currentField = form.elements[i];
														  var fieldName = currentField.name;
														  var fieldType = currentField.type;

														  /* Disabled and unnamed fields are not sent by browsers so ignore them */
														  if ((!currentField.disabled) && fieldName)
														    {
															 switch (fieldType)
															  {
															   case "text":
															   case "password":
															   case "hidden":
															   case "textarea": formValues = AjaxTCR.data._encode(formValues, fieldName, currentField.value, encoding);
														                            break;
															   case "radio":
															   case "checkbox": if (currentField.checked)
																		formValues = AjaxTCR.data._encode(formValues, fieldName, currentField.value, encoding);
														                            break;
														       case 'select-one':
															   case 'select-multiple': for (var j=0; j< currentField.options.length; j++)
																	            if (currentField.options[j].selected)
														                                    {
																			formValues = AjaxTCR.data._encode(formValues, fieldName, (currentField.options[j].value) ? currentField.options[j].value : currentField.options[j].text , encoding);
														                                    }
																	            break;
															   case "file": if (currentField.value)
																	    return "fileupload";
																	else
																	     formValues = AjaxTCR.data._encode(formValues, fieldName, currentField.value, encoding);
																        break;
															   case "submit": if (currentField == trigger)
																  	     formValues = AjaxTCR.data._encode(formValues, fieldName, currentField.value, encoding);
																	  break;
															   default: continue;  /* everything else like fieldset you don't want */
															  }
														   }

														 }

														 if (trigger && trigger.type == "image" && trigger.name)
														 {
															/* this is where we need to insert the trigger image information */
															formValues = AjaxTCR.data._encode(formValues, trigger.name + ".x", x, encoding);
															formValues = AjaxTCR.data._encode(formValues, trigger.name + ".y", y, encoding);
															formValues = AjaxTCR.data._encode(formValues, trigger.name, trigger.value, encoding);
														 }

														 /* Clean up payload string */
														 formValues = AjaxTCR.data._completeEncoding(formValues, encoding);

														 return formValues;
														},

/*
 * serializeObject(payload, obj, encoding)
 *
 * Public method to create/modify a serialized object based on an object
 * payload is current object that will be appended to.
 * obj is the object to add to the payload
 * encoding is the encoding method
 */

serializeObject : function(payload, obj, encoding){
													/* Take care of any necessary bits if payload isn't empty */
													payload = AjaxTCR.data._continueEncoding(payload, encoding);

													/* encode each value in the object */
												    for (var key in obj)
												        payload = AjaxTCR.data._encode(payload, key, obj[key], encoding);

													/* Clean up payload string */
													payload = AjaxTCR.data._completeEncoding(payload, encoding);
												    return payload;
												},

/*
 * serializeObject(payload, fieldName, fieldValue encoding)
 *
 * private method to encode one name/value pair and add it to a payload.
 * payload is current object that will be appended to.  It can be null.
 * fieldName and fieldValue are the name/value pair.
 * encoding is the encoding method
 */
_encode : function(payload, fieldName, fieldValue, encoding){
																switch(encoding)
																{
															    	case "application/json":
															    	   	  payload[fieldName] = fieldValue;
																	  break;
															    	case "application/x-www-form-urlencoded":
															    	   	  payload+=AjaxTCR.data.encodeValue(fieldName)+"="+AjaxTCR.data.encodeValue(fieldValue)+"&"
																	  break;
																	case "text/plain":
															    	   	  payload+=fieldName.replace(/,/g, "%2C") + "=" + fieldValue.replace(/,/g, "%2C") +","
																	  break;
																	case "text/xml":
																		var node = payload.createElement(fieldName);
																		node.appendChild(payload.createTextNode(fieldValue));
																		payload.lastChild.appendChild(node);

																	  break;
																}

															    return payload;
															},

/*
 * beginEncode(encoding)
 *
 * private method to create the base for the encoding object
 */
_beginEncode : function(encoding){
																switch(encoding)
																{
															    	case "application/json":
															    	  if (payload == null)
																    	payload = {};

																  	  break;
															    	case "application/x-www-form-urlencoded":
															    	  if (payload == null)
																    	payload = "";

																	  break;
																	case "text/plain":
															    	  if (payload == null)
																    	payload = "";

																	  break;
																	case "text/xml":
																		if (payload == null)
																		{
																			var payload = AjaxTCR.data._createXMLDocument();
																			if (window.navigator.userAgent.indexOf('Opera') == -1)
																			{
																			  var xmlStmt = payload.createProcessingInstruction("xml"," version=\"1.0\" encoding=\"UTF-8\" ");
																		      payload.appendChild(xmlStmt);
																			}

																			var root = payload.createElement("payload");
																			payload.appendChild(root);
																		}


																	  break;
																}

															    return payload;
															},

/*
 * completeEncoding(payload, encoding)
 *
 * private method to clean up payload string when finished encoding.
 */
_completeEncoding : function (payload, encoding ){
													 /* Trim off the end & but avoid edge case problems with an empty form */
													 if ((encoding == "application/x-www-form-urlencoded" || encoding == "text/plain") && payload.length > 0)
													   payload = payload.substring(0,payload.length-1);


													 return payload;
												 },
/*
 * continueEncoding(payload, encoding)
 *
 * private method to get payload ready to be appended to.
 */
_continueEncoding : function(payload, encoding){
												if (payload != "")
												  {
													if (encoding == "application/x-www-form-urlencoded")
														payload += "&";
													else if (encoding == "text/plain")
														payload += ",";
												  }
												return payload;
											   },

/*
 * createXMLDocument()
 *
 * Finds the best native or ActiveX object to use for an XML Document
 */
_createXMLDocument : function(){
								var xmlDoc = null;
								if (window.ActiveXObject)
								  {
								  	var versions = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XMLDOM"];

								    for (var i=0;i<versions.length;i++)
								     {
									   try
									    {
										 xmlDoc = new ActiveXObject(versions[i]);
										 break;
									    }
									   catch(err){}
								      }
								  }
								else
									xmlDoc = document.implementation.createDocument("", "", null);

								return xmlDoc;
							  },

/*
 * serializeXML(xmlObject)
 *
 * Returns the string version of the given XML Object
 */
serializeXML : function(xmlObject){
							var xmlString = "";

							if (typeof XMLSerializer != "undefined")
						  		xmlString = (new XMLSerializer()).serializeToString(xmlObject);
							else if (xmlObject.xml)
								xmlString = xmlObject.xml;

							return xmlString;
						},



decodeJSON : function(jsonString){
						            var j;

									if (jsonString.length > 1 && jsonString.substring(0,2) == "/*")
 										jsonString = jsonString.substring(2,jsonString.lastIndexOf("*/"));

						            if (/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/.
						                    test(jsonString)) {

						              try {
						                    j = eval('(' + jsonString + ')');
						                } catch (e) {
						                    throw new SyntaxError('parseJSON');
						                }
						            } else {
						                throw new SyntaxError('parseJSON');
						            }

									return j;
						    	},

encodeJSON : function(o){
							var useHasOwn = {}.hasOwnProperty ? true : false;

						    var pad = function(n) {
						        return n < 10 ? '0' + n : n;
						    };

						    var m = {
						        '\b': '\\b',
						        '\t': '\\t',
						        '\n': '\\n',
						        '\f': '\\f',
						        '\r': '\\r',
						        '"' : '\\"',
						        '\\': '\\\\'
						    };

					        if(typeof o == 'undefined' || o === null)
					            return 'null';
					        else if(o instanceof Array)
							{
					            var a = ['['], b, i, l = o.length, v;
					            for (i = 0; i < l; i += 1) {
					                v = o[i];
					                switch (typeof v) {
					                    case 'undefined':
					                    case 'function':
					                    case 'unknown':
					                        break;
					                    default:
					                        if (b) {
					                            a.push(',');
					                        }
					                        a.push(v === null ? "null" : AjaxTCR.data.encodeJSON(v));
					                        b = true;
					                }
					            }
					            a.push(']');
					            return a.join('');
					        }
							else if(o instanceof Date)
							{
					            return '"' + o.getFullYear() + '-' +
					                pad(o.getMonth() + 1) + '-' +
					                pad(o.getDate()) + 'T' +
					                pad(o.getHours()) + ':' +
					                pad(o.getMinutes()) + ':' +
					                pad(o.getSeconds()) + '"';
					        }
							else if(typeof o == 'string')
							{
								var s = o;
					            if (/["\\\x00-\x1f]/.test(s))
								{
					            	return '"' + s.replace(/([\x00-\x1f\\"])/g, function(a, b)
									{
						                var c = m[b];
						                if(c){
						                    return c;
						                }
						                c = b.charCodeAt();
					                	return '\\u00' +
					                    	Math.floor(c / 16).toString(16) +
					                    (c % 16).toString(16);
					            	}) + '"';
					        	}
					        	return '"' + s + '"';
					        }
							else if(typeof o == 'number')
							{
					            return isFinite(o) ? String(o) : "null";
					        }
							else if(typeof o == 'boolean')
							{
					            return String(o);
					        }
							else
							{
					            var a = ['{'], b, i, v;
					            for (var i in o) {
					                if(!useHasOwn || o.hasOwnProperty(i)) {
					                    v = o[i];
					                    switch (typeof v) {
					                    case 'undefined':
					                    case 'function':
					                    case 'unknown':
					                        break;
					                    default:
					                        if(b)
												a.push(',');

					                        a.push(AjaxTCR.data.encodeJSON(i), ':', v === null ? "null" : AjaxTCR.data.encodeJSON(v));
					                        b = true;
					                    }
					                }
					            }
					            a.push('}');
					            return a.join('');
					        }
    },

/*
 * encodeMD5(string)
 *
 * public method to get md5 encode a string
 * Based on Version 2.1 Copyright (C) Paul Johnston 1999 - 2002.
 * Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
 * Distributed under the BSD License
 * See http://pajhome.org.uk/crypt/md5 for more info.
 */
encodeMD5 : function(str){
	var hexcase = 0;  /* hex output format. 0 - lowercase; 1 - uppercase        */
	var b64pad  = ""; /* base-64 pad character. "=" for strict RFC compliance   */
	var chrsz   = 8;  /* bits per input character. 8 - ASCII; 16 - Unicode      */

	var len = str.length * chrsz;

	var x = Array();
  	var mask = (1 << chrsz) - 1;
  	for(var i = 0; i < len; i += chrsz)
    	x[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (i%32);


	/* append padding */
  	x[len >> 5] |= 0x80 << ((len) % 32);
  	x[(((len + 64) >>> 9) << 4) + 14] = len;

  	var a =  1732584193;
  	var b = -271733879;
  	var c = -1732584194;
  	var d =  271733878;

  	for(var i = 0; i < x.length; i += 16)
  	{
    	var olda = a;
    	var oldb = b;
    	var oldc = c;
    	var oldd = d;

	    a = md5_ff(a, b, c, d, x[i+ 0], 7 , -680876936);
	    d = md5_ff(d, a, b, c, x[i+ 1], 12, -389564586);
	    c = md5_ff(c, d, a, b, x[i+ 2], 17,  606105819);
	    b = md5_ff(b, c, d, a, x[i+ 3], 22, -1044525330);
	    a = md5_ff(a, b, c, d, x[i+ 4], 7 , -176418897);
	    d = md5_ff(d, a, b, c, x[i+ 5], 12,  1200080426);
	    c = md5_ff(c, d, a, b, x[i+ 6], 17, -1473231341);
	    b = md5_ff(b, c, d, a, x[i+ 7], 22, -45705983);
	    a = md5_ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
	    d = md5_ff(d, a, b, c, x[i+ 9], 12, -1958414417);
	    c = md5_ff(c, d, a, b, x[i+10], 17, -42063);
	    b = md5_ff(b, c, d, a, x[i+11], 22, -1990404162);
	    a = md5_ff(a, b, c, d, x[i+12], 7 ,  1804603682);
	    d = md5_ff(d, a, b, c, x[i+13], 12, -40341101);
	    c = md5_ff(c, d, a, b, x[i+14], 17, -1502002290);
	    b = md5_ff(b, c, d, a, x[i+15], 22,  1236535329);

	    a = md5_gg(a, b, c, d, x[i+ 1], 5 , -165796510);
	    d = md5_gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
	    c = md5_gg(c, d, a, b, x[i+11], 14,  643717713);
	    b = md5_gg(b, c, d, a, x[i+ 0], 20, -373897302);
	    a = md5_gg(a, b, c, d, x[i+ 5], 5 , -701558691);
	    d = md5_gg(d, a, b, c, x[i+10], 9 ,  38016083);
	    c = md5_gg(c, d, a, b, x[i+15], 14, -660478335);
	    b = md5_gg(b, c, d, a, x[i+ 4], 20, -405537848);
	    a = md5_gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
	    d = md5_gg(d, a, b, c, x[i+14], 9 , -1019803690);
	    c = md5_gg(c, d, a, b, x[i+ 3], 14, -187363961);
	    b = md5_gg(b, c, d, a, x[i+ 8], 20,  1163531501);
	    a = md5_gg(a, b, c, d, x[i+13], 5 , -1444681467);
	    d = md5_gg(d, a, b, c, x[i+ 2], 9 , -51403784);
	    c = md5_gg(c, d, a, b, x[i+ 7], 14,  1735328473);
	    b = md5_gg(b, c, d, a, x[i+12], 20, -1926607734);

	    a = md5_hh(a, b, c, d, x[i+ 5], 4 , -378558);
	    d = md5_hh(d, a, b, c, x[i+ 8], 11, -2022574463);
	    c = md5_hh(c, d, a, b, x[i+11], 16,  1839030562);
	    b = md5_hh(b, c, d, a, x[i+14], 23, -35309556);
	    a = md5_hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
	    d = md5_hh(d, a, b, c, x[i+ 4], 11,  1272893353);
	    c = md5_hh(c, d, a, b, x[i+ 7], 16, -155497632);
	    b = md5_hh(b, c, d, a, x[i+10], 23, -1094730640);
	    a = md5_hh(a, b, c, d, x[i+13], 4 ,  681279174);
	    d = md5_hh(d, a, b, c, x[i+ 0], 11, -358537222);
	    c = md5_hh(c, d, a, b, x[i+ 3], 16, -722521979);
	    b = md5_hh(b, c, d, a, x[i+ 6], 23,  76029189);
	    a = md5_hh(a, b, c, d, x[i+ 9], 4 , -640364487);
	    d = md5_hh(d, a, b, c, x[i+12], 11, -421815835);
	    c = md5_hh(c, d, a, b, x[i+15], 16,  530742520);
	    b = md5_hh(b, c, d, a, x[i+ 2], 23, -995338651);

	    a = md5_ii(a, b, c, d, x[i+ 0], 6 , -198630844);
	    d = md5_ii(d, a, b, c, x[i+ 7], 10,  1126891415);
	    c = md5_ii(c, d, a, b, x[i+14], 15, -1416354905);
	    b = md5_ii(b, c, d, a, x[i+ 5], 21, -57434055);
	    a = md5_ii(a, b, c, d, x[i+12], 6 ,  1700485571);
	    d = md5_ii(d, a, b, c, x[i+ 3], 10, -1894986606);
	    c = md5_ii(c, d, a, b, x[i+10], 15, -1051523);
	    b = md5_ii(b, c, d, a, x[i+ 1], 21, -2054922799);
	    a = md5_ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
	    d = md5_ii(d, a, b, c, x[i+15], 10, -30611744);
	    c = md5_ii(c, d, a, b, x[i+ 6], 15, -1560198380);
	    b = md5_ii(b, c, d, a, x[i+13], 21,  1309151649);
	    a = md5_ii(a, b, c, d, x[i+ 4], 6 , -145523070);
	    d = md5_ii(d, a, b, c, x[i+11], 10, -1120210379);
	    c = md5_ii(c, d, a, b, x[i+ 2], 15,  718787259);
	    b = md5_ii(b, c, d, a, x[i+ 9], 21, -343485551);

	    a = safe_add(a, olda);
	    b = safe_add(b, oldb);
	    c = safe_add(c, oldc);
	    d = safe_add(d, oldd);
	}

  	var binarray = Array(a, b, c, d);
  	var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
  	var str = "";
  	for(var i = 0; i < binarray.length * 4; i++)
  	{
    	str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) + hex_tab.charAt((binarray[i>>2] >> ((i%4)*8  )) & 0xF);
  	}

	return str;

	/*
	 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
	 * to work around bugs in some JS interpreters.
	 */
	function safe_add(x, y)
	{
	  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
	  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	  return (msw << 16) | (lsw & 0xFFFF);
	}

	/*
	 * Bitwise rotate a 32-bit number to the left.
	 */
	function bit_rol(num, cnt)
	{
	  return (num << cnt) | (num >>> (32 - cnt));
	}

	/*
	 * These functions implement the four basic operations the algorithm uses.
	 */
	function md5_cmn(q, a, b, x, s, t)
	{
	  return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s),b);
	}
	function md5_ff(a, b, c, d, x, s, t)
	{
	  return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
	}
	function md5_gg(a, b, c, d, x, s, t)
	{
	  return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
	}
	function md5_hh(a, b, c, d, x, s, t)
	{
	  return md5_cmn(b ^ c ^ d, a, b, x, s, t);
	}
	function md5_ii(a, b, c, d, x, s, t)
	{
	  return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
	}

}

};

AjaxTCR.util = {};
AjaxTCR.util.DOM = {
	enableDOMShorthand : true,

	/**
	 * Find elements by class, will be overriden by native if found
	 * If startNode is specified, starts the search there, otherwise starts at document.
	 *
	 * @param 	classToFind	the string class name to search for
	 * @param	startNode	the DOM node to start the search at.  Default is the document node.
	 * @return 	array of elements that match the given class name.
	 */
	getElementsByClassName : function(classToFind,startNode){
								  if (document.getElementsByClassName)
								    return document.getElementsByClassName(classToFind,startNode);

								  /* find all the elements within a particular document or in the whole document */
								  var elements;
								  if (startNode)
								   elements = startNode.getElementsByTagName("*");
								  else
								   elements = document.getElementsByTagName("*");

								  var classElements = new Array();
								  var classCount = 0;

								  var pattern = new RegExp("(^|\\s)"+classToFind+"(\\s|$)");

								  /* look over the elements and find those who match the class passed */
								  for (var i = 0; i < elements.length; i++)
								    if (pattern.test(elements[i].className) )
								        classElements[classCount++] = elements[i];

								  return classElements;
							},

	/**
	 * Returns element or element array specified by given string or strings.
	 *
	 * @param	element(s) 	strings to search for element.
	 * @param 	startNode	the DOM node to start the search at.  Default is the document node.
	 * @return	if single string, it returns the element.  Otherwise it returns an array of elements
	 */
	getElementsById : function(){
									var elements = new Array();
									var startNode = document;
									var length = arguments.length;
									if (typeof(arguments[length-1]) == "object" && arguments[length-1] != document)
									{
										startNode = arguments[length-1];
										length--;
										var allElements = startNode.getElementsByTagName("*");
										for (var j=0; j<allElements.length; j++)
										{
											for (var i=0;i<length;i++)
											{
								   				if (allElements[j].getAttribute("id") == arguments[i])
												{
								        			elements.push(allElements[j]);
													break;
												}
											}
										}
									}
									else
									{
										if (arguments[length-1] == document)
											length--;

										for (var i=0; i<length; i++)
										{
											var elm = document.getElementById(arguments[i]);
											if (elm != null)
									    		elements.push(elm);
										}
									}

									if (elements.length == 1)
										return elements[0];
									else if (elements.length > 0)
										return elements;
									else
										return null;
								},

	/**
	 * Modified version of getElementById to return single node match.
	 * If startNode is not set to document, it starts the search at the node
	 * If deepSearch is set to true, it does not use getElementById, but instead loops through the whole structure.
	 *
	 * @param id 			the string to match with the id attribute
	 * @param startNode		the DOM node to start searching in the document
	 * @param deepSearch	true if wanted to search node by node instead of document.getElementById
	 */
	getElementById : function(id, startNode, deepSearch){
								if (!startNode)
									startNode = document;

								if (startNode == document && !deepSearch)
									return document.getElementById(id);
								else
								{
									var allElements = startNode.getElementsByTagName("*");
									for (var j=0; j<allElements.length; j++)
									{
										if (allElements[j].getAttribute("id") == id)
										{
									   		return allElements[j];
											break;
										}
									}
								}



	},


	/**
	 *
	 * @param 	selector		string indicating the selection to match
	 * @param 	treeRoot		DOM element to start search.  Default is the document node
	 * @return 	array of matching elements
	 *
	 */
	getElementsBySelector : function(selector,treeRoot){
								var matches = new Array();
								var parents = new Array();
								var savematches = new Array();
								if (treeRoot)
								{
									if (treeRoot.length)
									{
										for (var i=0;i<treeRoot.length;i++)
											parents.push(treeRoot[i]);
									}
									else
										parents.push(treeRoot);
								}
								else
									parents.push(document);

								selector = selector.replace(/([>\+,])/g, " $1 ").replace(/[\s]+/g," ");

								var selectors = selector.split(" ");
								while (selectors.length > 0)
								{
										var curSelector = selectors.shift();
										if (curSelector == "")
											continue;

										/* check for expressions */
										var options = {};
										switch(curSelector.charAt(0))
										{
											case(">"):
												options.type = "childOnly";
											break;
											case("+"):
												options.type = "nextSibling";
											break;
											case ("~"):
												options.type = "futureSibling";
											break;
											case(","):
												while(matches.length > 0)
													savematches.push(matches.shift());

												parents.length = 0;
												if (treeRoot)
													parents.push(treeRoot);
												else
													parents.push(document);

												continue;
											break;
										}

										if (options.type)
										{
											if (curSelector.length == 1)
												curSelector = selectors.shift();
											else
												curSelector = curSelector.substring(1);
										}

										/* Check to see if we already looped though.  If so, we have a different starting point */
										if (matches.length)
										{
											parents.length = 0;
											while(matches.length > 0)
												parents.push(matches.shift());
										}


										/* Check for Pseudo-classes */
										if (curSelector.indexOf(":") > -1)
										{
											var newSelector = curSelector.substring(0, curSelector.indexOf(":"));
											var optionsType = curSelector.substring(curSelector.indexOf(":")+1);

											curSelector = newSelector;
											options.type = optionsType.toLowerCase();

											if (options.type.indexOf("nth-child") == 0)
											{
												options.childNumber = options.type.substring(10,options.type.length-1);
												options.type = "nth-child";
											}
											else if (options.type.indexOf("not") == 0)
											{
												//use optionsType to preserve case
												options.notString = optionsType.substring(4,options.type.length-1).replace(/^\s+|\s+$/g,"");
												options.type = "not";
												var notSelector = curSelector;
												if (notSelector == "*")
													notSelector = "";
												if (/^[:#\[\.].*/.test(options.notString))
													options.notSelector = notSelector + options.notString;
												else
													options.notSelector = notSelector + " " + options.notString;

												options.notObjects = AjaxTCR.util.DOM.getElementsBySelector(options.notSelector, parents);
											}
										}

										/* Check for Attributes */
										if (curSelector.indexOf("[") > -1)
										{
											var tokens = curSelector.split("[");
											curSelector = tokens[0];
											options.type = "attribute";
											options.attribute = tokens[1].substring(0,tokens[1].length-1).toLowerCase();
										}

										if (curSelector == "")
											curSelector = "*";

										/* Inspect class selectors */
										if (curSelector.indexOf(".") > -1)
										{
											/* Cases:
											 * p.class1
											 * .class2
											 * div.class1.class2
											 */
											var classNames = curSelector.split(".");
											var elementName = classNames.shift();
											/* First get the element at the beginning if necessary */
											if (elementName != "")
											{
												for (var j=0;j<parents.length;j++)
												{
													var elms = AjaxTCR.util.DOM._getElementsByTagName(parents[j],elementName,options);
													for (var k=0;k<elms.length;k++)
													{
														if (checkFilter(elms[k], parents[j], options))
															matches.push(elms[k]);
													}
												}
											}
											else if (classNames.length > 0)
											{
												/* if no element is specified, use getElementsByClassName for the first class */
												var firstClass = classNames.shift();
												for (var j=0;j<parents.length;j++)
												{
													var elms = AjaxTCR.util.DOM.getElementsByClassName(firstClass, parents[j]);
													for (var k=0;k<elms.length;k++)
													{
														if (checkFilter(elms[k],parents[j],options))
															matches.push(elms[k]);
													}
												}
											}

											/* Now get the (rest of the) classes */
											for (var j=matches.length-1;j>=0;j--)
											{
												for (var k=0;k<classNames.length;k++)
												{
													var pattern = new RegExp("(^|\\s)"+classNames[k]+"(\\s|$)");
													if (!pattern.test(matches[j].className))
													{
														matches.splice(j,1);
														break;
													}
												}
											}
										}

										/* Inspect id selectors */
										else if (curSelector.indexOf("#") > -1)
										{
											/* Cases:
											 * p#id1
											 * #id2
											 */
											var idNames = curSelector.split("#");
											var elementName = idNames[0];
											var id = idNames[1];

											/* First get the element at the beginning if necessary */
											if (elementName != "")
											{
												for (var j=0;j<parents.length;j++)
												{
													var elms = AjaxTCR.util.DOM._getElementsByTagName(parents[j],elementName,options);
													for (var k=0;k<elms.length;k++)
													{
														if (elms[k].id == id && checkFilter(elms[k], parents[j], options))
															matches.push(elms[k]);
													}
												}
											}
											else
											{
												for (var j=0;j<parents.length;j++)
												{
													var elms = AjaxTCR.util.DOM.getElementsById(id, parents[j]);
													if (checkFilter(elms, parents[j], options))
														matches.push(elms);
												}
											}
										}
										/* Simple tagname selects */
										else
										{
											for (var j=0;j<parents.length;j++)
											{
												var elms =AjaxTCR.util.DOM._getElementsByTagName(parents[j],curSelector,options);
												for (var k=0;k<elms.length;k++)
												{
													if (checkFilter(elms[k], parents[j], options))
														matches.push(elms[k]);
												}
											}
										}
								}


								function checkFilter(element, parent, options)
								{
									var valid = false;

									if (element == null)
										return false;
									else if (!options.type)
										return true;

									//handle the case of the parent element being the document
									if (parent == document)
									{
										var allElms = document.getElementsByTagName("*");
										for (var i=0;i<allElms.length;i++)
										{
											if( checkFilter(element, allElms[i], options))
											{
												valid = true;
												break;
											}
										}

										return valid;
									}


									if (options.type == "childOnly")
										valid = (element.parentNode == parent);
									else if (options.type == "nextSibling")
									{
										var elm = parent.nextSibling;
										while (elm != null && elm.nodeType != 1)
											elm = elm.nextSibling;
										valid = (elm == element);
									}
									else if (options.type == "futureSibling")
									{
										var elm = parent.nextSibling;
										while (elm != null)
										{
											if (elm == element)
											{
												valid = true;
												break;
											}
											elm = elm.nextSibling;
										}
									}
									else if (options.type == "first-child")
									{
										var elm = parent.firstChild;
										while (elm != null && elm.nodeType != 1)
											elm = elm.nextSibling;
										valid = (elm == element);
									}
									else if (options.type == "last-child")
									{
										var elm = parent.lastChild;
										while (elm != null && elm.nodeType != 1)
											elm = elm.previousSibling;
										valid = (elm == element);
									}
									else if (options.type == "only-child")
									{
										var elm = parent.firstChild;
										while (elm != null && elm.nodeType != 1)
											elm = elm.nextSibling;

										if (elm == element)
										{
											var elm = parent.lastChild;
											while (elm != null && elm.nodeType != 1)
												elm = elm.previousSibling;
										}

										valid = (elm == element);
									}
									else if (options.type == "nth-child")
									{
										var count = 0;
										var elm = parent.firstChild;
										while (elm != null  && count < options.childNumber)
										{
											if (elm.nodeType == 1)
												count++;

											if (count == options.childNumber)
												break;

											elm = elm.nextSibling;
										}

										valid = (elm == element);
									}
									else if (options.type == "empty")
										valid = (element.childNodes.length == 0);
									else if (options.type == "enabled")
										valid = (!element.disabled);
									else if (options.type == "disabled")
										valid = (element.disabled);
									else if (options.type == "checked")
										valid = (element.checked);
									else if (options.type == "selected")
										valid = (element.selected);
									else if (options.type == "attribute")
									{
										var pattern = /^\s*([\w-]+)\s*([!*$^~=]*)\s*(['|\"]?)(.*)\3/;
										var attRules = pattern.exec(options.attribute);

										if (attRules[2] == "")
											valid = element.getAttribute(attRules[1]);
										else if (attRules[2] == "=")
											valid = (element.getAttribute(attRules[1]) && element.getAttribute(attRules[1]).toLowerCase() == attRules[4].toLowerCase());
										else if (attRules[2] == "^=")
											valid = (element.getAttribute(attRules[1]) && element.getAttribute(attRules[1]).toLowerCase().indexOf(attRules[4].toLowerCase()) == 0);
										else if (attRules[2] == "*=")
											valid = (element.getAttribute(attRules[1]) && element.getAttribute(attRules[1]).toLowerCase().indexOf(attRules[4].toLowerCase()) > -1);
										else if (attRules[2] == "$=")
										{
											var att =element.getAttribute(attRules[1]);
											if (att)
												valid =  (att.toLowerCase().substring(att.length - attRules[4].length) == attRules[4].toLowerCase());
										}
									}
									else if (options.type == "not")
									{
										valid = true;
										for (var j=0;j<options.notObjects.length;j++)
										{
											if (options.notObjects[j] == element)
											{
												valid = false;
												break;
											}
										}
									}


									return valid;
								}

								//get the results in the correct order
								if (savematches.length)
								{
									while(matches.length > 0)
										savematches.push(matches.shift());
									while(savematches.length > 0)
										matches.push(savematches.shift());
								}
								return matches;
							},
/**
 * Custom getElementsByTagName that takes various options into consideration before returning the values
 *
 * @param parentElm	element to begin the search at
 * @param tag		string to match tagName to
 * @param options
 */
_getElementsByTagName : function(parentElm, tag, options){
	var matches = new Array();
	if (!options.type)
		return parentElm.getElementsByTagName(tag);


	if (options.type == "nextSibling")
	{
		var elm = parentElm.nextSibling;
		while (elm && elm.nodeType != 1)
			elm = elm.nextSibling;

		if (checkTagMatch(elm, tag))
			matches.push(elm);
	}
	else if (options.type == "futureSibling")
	{
		var elm = parentElm.nextSibling;
		while (elm)
		{
			if (checkTagMatch(elm, tag))
			{
				matches.push(elm);
				//break;
			}
			elm = elm.nextSibling;
		}
	}
	else
		matches = parentElm.getElementsByTagName(tag);

	function checkTagMatch(element, tag)
	{
		return (element && element.tagName && (tag == "*" || element.tagName.toUpperCase() == tag.toUpperCase()));
	}

	return matches;
}
};

AjaxTCR.util.event = {
/**
 * addWindowLoadEvent - simple method to allow for safe addition of window.onload called functions.  Assumes everyone else plays nicely though.
 *
 * @param newFunction - function to call upon page load
 */

addWindowLoadEvent: function(newFunction) {

	var oldFunction = window.onload;
	if (typeof window.onload != "function")
	  {
	   window.onload = newFunction;
	  }
	else
	  {
  	   window.onload = function () {
	     if (oldFunction)
		   {
		   	oldFunction();
		   }
		 newFunction();
	    };
	  }
}

};


AjaxTCR.util.misc = {
	/**
	 * generateUID : Generates a unique value.  If 'prefix' is set to -1, only returns the numerical value.
	 * 				 If prefix isn't set, it sets it to "AjaxTCR".
	 *
	 * @param prefix the string value to prepend to the uniquevalue
	 * @return the string consisting of the prefix and the uniquevalue
	 */
	generateUID : function(prefix){
		if (prefix == "-1")
			prefix = "";
		else if (!prefix)
			prefix = "AjaxTCR";

		var uniquevalue = new Date().getTime().toString() + Math.floor(Math.random()*100);
		return prefix + uniquevalue;

	}
};

if (AjaxTCR.util.DOM.enableDOMShorthand)
{
   if (typeof($id) == "undefined") $id = AjaxTCR.util.DOM.getElementsById;
   if (typeof($class) == "undefined") $class = AjaxTCR.util.DOM.getElementsByClassName;
   if (typeof($selector) == "undefined") $selector = AjaxTCR.util.DOM.getElementsBySelector;
}



