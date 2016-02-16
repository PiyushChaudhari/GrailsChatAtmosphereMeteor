<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="grails.util.Holders" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="main"/>
	<title>${userInstance?.username}</title>
	
	<g:set var="grailsVersion" value="${Holders.grailsApplication.metadata['app.grails.version']}"/>
	<g:if test="${grailsVersion.startsWith("2.1") || grailsVersion.startsWith("2.2") || grailsVersion.startsWith("2.3")}">
		<r:require module="atmosphere-meteor-jquery"/>
		<r:layoutResources/>
	</g:if>
	<g:else>
		<asset:javascript src="atmosphere-meteor-jquery.js"/>
	</g:else>
</head>
<body>
  <div class="body">
  	 <h1 align="center">${loggedUser?.username?.toUpperCase() } Send To ${userInstance?.username?.toUpperCase()}</h1>
  	 <div id="chatArea">
  	 </div>
  	<table>
   		<tbody>
   				<tr>
    				<td style="text-align: right;">Enter Your Text:</td>
    				<td> <input type="text" id="chatText" value=""></td>
    			</tr>
    			<tr>
    				<td></td>
    				<td style="text-align: left;">
    					<input id="sendBtn" type="button" value="Send" >
    				</td>
    			</tr>
   		</tbody>
   	</table>
  	
  </div>
  
  <script type="text/javascript">
	// required for IE console logging
	if (!window.console) console = {log: function () {
	}};

	/*
	 The Jabber variable holds all JavaScript code required for communicating with the server.
	 It basically wraps the functions in atmosphere.js and jquery.atmosphere.js.
	 */
	var Jabber = {
		socket: null,
		chatSubscription: null,
		notificationSubscription: null,
		publicSubscription: null,
		transport: null,

		subscribe: function (options) {
			var defaults = {
						type: '',
						contentType: "application/json",
						shared: false,
						transport: 'websocket',
						//transport: 'long-polling',
						fallbackTransport: 'long-polling',
						trackMessageLength: true
					},
					atmosphereRequest = $.extend({}, defaults, options);
			atmosphereRequest.onOpen = function (response) {
				console.log('atmosphereOpen transport: ' + response.transport);
				//alert('onOpen');
			};
			atmosphereRequest.onReconnect = function (request, response) {
				console.log("atmosphereReconnect");
				//alert('onReconnect');
			};
			atmosphereRequest.onMessage = function (response) {
				//console.log('onMessage: ' + response.responseBody);
				Jabber.onMessage(response);
			};
			atmosphereRequest.onError = function (response) {
				console.log('atmosphereError: ' + response);
			};
			atmosphereRequest.onTransportFailure = function (errorMsg, request) {
				console.log('atmosphereTransportFailure: ' + errorMsg);
			};
			atmosphereRequest.onClose = function (response) {
				console.log('atmosphereClose: ' + response);
			};
			Jabber.chatSubscription = Jabber.socket.subscribe(atmosphereRequest);
		},

		unsubscribe: function () {
			//alert('unsubscribe');
			Jabber.socket.unsubscribe();
		},

		onMessage: function (response) {
			var data = response.responseBody;
			if ((message == '')) {
				return;
			}
			console.log("Data:> "+data);
			if(data!=''){
			}
			var message = JSON.parse(data);
			var $chat = $('#chatArea');
				$chat.append(message.sender+" "+message.createdDate+" : "+ message.message + '<br/>');
			$chat.scrollTop($chat.height());
		}
	};

	$(window).unload(function () {
		Jabber.unsubscribe();
	});

	$(document).ready(function () {
		if (typeof atmosphere == 'undefined') {
			// if using jquery.atmosphere.js
			Jabber.socket = $.atmosphere;
		} else {
			// if using atmosphere.js
			Jabber.socket = atmosphere;
		}
		
		//$('#startChatBtn').on('click', function () {
			//var chatUrl = $("#chatUrl").val();
			var atmosphereRequest = {type: 'chat',url: '${grailsLinkGenerator.link(controller: 'atmosphere', absolute: true, action : 'chat',id:params?.id)}'};
			Jabber.subscribe(atmosphereRequest);
			$('#chatText').focus();
		//});

		$('#chatText').keypress(function (event) {
			if (event.which === 13) {
				event.preventDefault();
				var data = {
					type: 'chat',
					sendTo:'${loggedUser?.id}',
					message: $(this).val()
				};
				Jabber.chatSubscription.push(JSON.stringify(data));
				$(this).val('');
			}
		});

		$('#sendBtn').on('click', function () {
			var data = {type: 'chat',sendTo:'${loggedUser?.id}',message: $('#chatText').val()};
			Jabber.chatSubscription.push(JSON.stringify(data));
			$('#chatText').val('');
			$('#chatText').focus();
		});

	});
</script>
  
</body>
</html>