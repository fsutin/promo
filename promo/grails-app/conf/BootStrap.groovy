import com.google.OAuthToken;

class BootStrap {

    def init = { servletContext ->
		//0		3600			NULL		
		
		OAuthToken token = new OAuthToken(userId:'federico.sutin@globallogic.com',accessToken:'ya29.AHES6ZQtp3X8TldgnsLzEMXL2uZjakmw9EK-Sm7sMoB6GqQ',refreshToken:'1/AaDEVGI5M8iud3qodMHLFHSGsRmw7zjp8e3njIPQPoM1/AaDEVGI5M8iud3qodMHLFHSGsRmw7zjp8e3njIPQPoM',scope:'https://www.googleapis.com/auth/drive', tokenType: 'Bearer', expireTime: 3600).save()
    }
    def destroy = {
    }
}
