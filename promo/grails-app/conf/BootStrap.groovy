import com.gdata.User

class BootStrap {

    def init = { servletContext ->
					
		new User(mail:'federico.sutin@globallogic.com',password:'').save(flush:true)
    }
    def destroy = {
    }
}
