package org.example

class ChatHistory {
	
	String message
	Date createdDate
	ShiroUser sender
	ShiroUser receiver
	
	static belongsTo = [sender:ShiroUser,receiver:ShiroUser]
	
	static constraints = {
		message(nullable: false, blank: false)
	}
}
