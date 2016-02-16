package org.example

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class ChatController {

	def atmosphereMeteor
	def chatService
	LinkGenerator grailsLinkGenerator
	
    def index() { 
		println "index:> "+ SecurityUtils.getSubject()?.principal
		if (!atmosphereMeteor.broadcasterFactory) {
			throw new RuntimeException("atmosphereMeteor.broadcasterFactory is null")
		}
		if (!atmosphereMeteor.framework) {
			throw new RuntimeException("atmosphereMeteor.framework is null")
		}
		def chatUsers = ShiroUser?.createCriteria()?.list{
			ne("username", SecurityUtils.getSubject()?.principal)
		}
		render(view: "index",model:['chatUsers':chatUsers])
	}
	
	def triggerPublic() {
		chatService.triggerPublic()
		render "success"
	}
	
	def startChat(){
		def userInstance = ShiroUser?.get(params?.id)
		['userInstance':userInstance,grailsLinkGenerator:grailsLinkGenerator,loggedUser:ShiroUser.findByUsername(SecurityUtils.getSubject()?.principal)]
	}
	
//	@MessageMapping("/hello/{world}")
//	@SendTo("/topic/hello")
//	protected String hello(@DestinationVariable String world,@Payload Object mymap) {
//		println "world:> "+world
//		println "mymap:> "+mymap
//		return "hello from controller, ${world} ${mymap} !"
//	}
	
//	@MessageMapping("/hello")
//	@SendTo("/topic/hello")
//	protected String hello(String world) {
//		println "loginName:> "+ SecurityUtils.getSubject()?.getPrincipal()
//		return world
//	}
}
