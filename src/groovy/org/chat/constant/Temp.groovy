package org.chat.constant

import grails.converters.JSON
import groovy.json.JsonOutput

import java.text.SimpleDateFormat

class Temp {

	static main(args) {
		println org.chat.constant.Constant.createMapping(org.chat.constant.Constant.CHAT_DEFAULT_MAPPING, org.chat.constant.Constant.CHAT_PARAM)
		println org.chat.constant.Constant.createMapping(org.chat.constant.Constant.CHAT_MAPPING, org.chat.constant.Constant.CHAT_PARAM)
		println org.chat.constant.Constant.createMapping(org.chat.constant.Constant.NOTIFICATION_MAPPING, org.chat.constant.Constant.CHAT_PARAM)
		println org.chat.constant.Constant.createMapping(org.chat.constant.Constant.PUBLIC_MAPPING, org.chat.constant.Constant.CHAT_PARAM)
		
		def chatHistoryInstance = [:]
		chatHistoryInstance.sender = "piyush"
		chatHistoryInstance.receiver = "saurabh"
		chatHistoryInstance.message = "hello test"
		chatHistoryInstance.createdDate = new Date()
		println "Gson:> "+(JsonOutput.toJson(chatHistoryInstance))
		println "Gson:> "+(JSON.parse(JsonOutput.toJson(chatHistoryInstance)))
		println new SimpleDateFormat("hh:mm:ss a").format(chatHistoryInstance.createdDate);
	}

}
