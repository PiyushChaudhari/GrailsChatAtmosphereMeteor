package org.chat.constant

class Constant {

	final static String CHAT_PARAM = "*"
	final static String CHAT_DEFAULT_MAPPING = "/atmosphere/"
	final static String CHAT_MAPPING = "/atmosphere/chat/"
	final static String NOTIFICATION_MAPPING = "/atmosphere/notification/"
	final static String PUBLIC_MAPPING = "/atmosphere/public/"
	
	public static String createMapping(String mapping,String param){
		return mapping + param
	}
}
