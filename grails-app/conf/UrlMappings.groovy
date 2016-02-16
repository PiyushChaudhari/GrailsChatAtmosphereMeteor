class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

		"/"(controller:"auth",action:"signIn")
//        "/"(view:"/index")
        "500"(view:'/error')
	}
}
