import org.apache.shiro.crypto.hash.Sha512Hash
import org.example.ShiroRole
import org.example.ShiroUser

class BootStrap {

    def init = { servletContext ->
		def adminRole = new ShiroRole(name: "Administrator")
		adminRole.addToPermissions("*:*")
		adminRole.save(flush:true)
		
		def user1 = new ShiroUser(username: "piyush", passwordHash: new Sha512Hash("123").toHex())
		user1.addToRoles(adminRole)
		user1.save()
		
		def user2 = new ShiroUser(username: "jenish", passwordHash: new Sha512Hash("123").toHex())
		user2.addToRoles(adminRole)
		user2.save()
		
		def user3 = new ShiroUser(username: "saurabh", passwordHash: new Sha512Hash("123").toHex())
		user3.addToRoles(adminRole)
		user3.save()
    }
    def destroy = {
    }
}
