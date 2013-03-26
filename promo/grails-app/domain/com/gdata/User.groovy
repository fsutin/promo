package com.gdata

class User {

	String mail
	String password
	
    static constraints = {
		mail nullable:false,unique:true,blank:false
		password nullable:false,blank:false
    }
}
