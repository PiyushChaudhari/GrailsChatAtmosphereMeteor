package com.chat.handler

import static org.atmosphere.cpr.AtmosphereResource.TRANSPORT.WEBSOCKET
import grails.converters.JSON
import grails.util.Holders
import groovy.json.JsonOutput

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.atmosphere.cpr.AtmosphereResourceEvent
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.DefaultBroadcaster
import org.atmosphere.cpr.Meteor
import org.atmosphere.websocket.WebSocketEventListener
import org.atmosphere.websocket.WebSocketEventListenerAdapter
import org.example.ChatHistory
import org.example.ShiroUser

class ChatMeteorHandler extends HttpServlet {

	def atmosphereMeteor = Holders.applicationContext.getBean("atmosphereMeteor")
	def chatService = Holders.applicationContext.getBean("chatService")
	
	@Override
	void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		println "Hello doGet 1 :::::::::::::::::::::::::::::::::::::::"
		String mapping = "/atmosphere/chat" + request.getPathInfo()
		Broadcaster b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping, true)
		Meteor m = Meteor.build(request)
		println "meteor:> "+m.transport()
		if (m.transport().equals(WEBSOCKET)) {
			m.addListener(new WebSocketEventListenerAdapter() {
				@Override
				void onDisconnect(AtmosphereResourceEvent event) {
					chatService.sendDisconnectMessage(event, request)
					println "If Disconnected: "+mapping
				}
				
				@Override
				void onConnect(WebSocketEventListener.WebSocketEvent event) {
					println "If Connected: "+mapping
				};
			})
		} else {
			m.addListener(new AtmosphereResourceEventListenerAdapter() {
				@Override
				void onDisconnect(AtmosphereResourceEvent event) {
					chatService.sendDisconnectMessage(event, request)
				}
			})
		}

		m.setBroadcaster(b)
	}

	@Override
	void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		println "Hello doPost 2 :::::::::::::::::::::::::::::::::::::::"+request.getReader().readLine().trim()
//		String mapping = "/atmosphere/chat" + request.getPathInfo();
		def jsonMap = JSON.parse(request.getReader().readLine().trim()) as Map
		println "jsonMap:> "+jsonMap
		String type = jsonMap.containsKey("type") ? jsonMap.type.toString() : null
		String message = jsonMap.containsKey("message") ? jsonMap.message.toString() : null
		String sendTo = jsonMap.containsKey("sendTo") ? jsonMap.sendTo.toString() : null
		
		String mapping = "/atmosphere/chat/" + sendTo

		if (type == null || message == null) {
			chatService.recordIncompleteMessage(jsonMap)
		} else {
			if (message.toLowerCase().contains("<script")) {
				chatService.recordMaliciousUseWarning(jsonMap)
			} else {
			
				def chatHistoryInstance = new ChatHistory()
				ChatHistory.withNewSession{ c ->
					println "receiver:> "+ ShiroUser.get(request.getPathInfo().substring(1)?.toLong())
					println "sender:> "+ ShiroUser.get(sendTo?.toLong())
					
					chatHistoryInstance.sender =  ShiroUser.get(sendTo?.toLong())
					chatHistoryInstance.receiver = ShiroUser.get(request.getPathInfo().substring(1)?.toLong())
					chatHistoryInstance.message = message?.trim()
					chatHistoryInstance.createdDate = new Date()
					chatHistoryInstance.save(flush:true)
				}
				def time = new SimpleDateFormat("hh:mm:ss a")?.format(chatHistoryInstance?.createdDate)
				def chatMap = ["createdDate":time,"message":chatHistoryInstance.message,"sender":chatHistoryInstance.sender?.username,"receiver":chatHistoryInstance?.receiver?.username] as Map
				chatService.recordChat(JSON.parse(JsonOutput.toJson(chatMap)))
				Broadcaster b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping)
				b.broadcast(JSON.parse(JsonOutput.toJson(chatMap)))
				
				mapping = "/atmosphere/chat" + request.getPathInfo();
				b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping)
				b.broadcast(JSON.parse(JsonOutput.toJson(chatMap)))
				
//				message = request?.userPrincipal?.getName()+": "+jsonMap.containsKey("message") ? jsonMap.message.toString() : null
				
//				chatService.recordChat(jsonMap)
//				Broadcaster b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping)
//				b.broadcast(jsonMap)
				
//				mapping = "/atmosphere/chat" + request.getPathInfo();
//				b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping)
//				b.broadcast(jsonMap)
				
//				message = "You: "+jsonMap.containsKey("message") ? jsonMap.message.toString() : null
//				mapping = "/atmosphere/chat" + request.getPathInfo();
//				b = atmosphereMeteor.broadcasterFactory.lookup(DefaultBroadcaster.class, mapping)
//				b.broadcast(jsonMap)
				
			}
		}
	}
}
