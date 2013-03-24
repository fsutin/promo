import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.foxnetworks.etl.adwords.configuration.GoogleOAuthConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sun.net.ssl.internal.ssl.SSLSessionContextImpl.SessionCacheVisitor;

class GoogleOAuthController {


	//https://accounts.google.com/o/oauth2/auth?from_login=0&response_type=code&scope=https://adwords.google.com/api/adwords/&redirect_uri=http://fox001.cub2k.com/fox-traffic-hub/oauth2callback&access_type=offline&approval_prompt=force&as=-42a14f911d3c1ec3&pli=1&client_id=900690408541-l38gq6inrk9203ld2qi2ohch2j5kuvj7@developer.gserviceaccount.com&authuser=0&hl=es-419
		/** Datos App*/
		private final String CLIENT_ID = "900690408541-l38gq6inrk9203ld2qi2ohch2j5kuvj7.apps.googleusercontent.com";
		private final String CLIENT_SECRET = "fMHHVqKjGN-spcBMZFCXLVMa";
		private final String USER_ID = "api.reporting@gmail.com"
		
		/** The name of the Oauth code URL parameter */
		  public static final String CODE_URL_PARAM_NAME = "code";
	
		  /** The name of the OAuth error URL parameter */
		  public static final String ERROR_URL_PARAM_NAME = "error";
	
		  /** The URL suffix of the servlet */
		  public static final String URL_MAPPING = "/oauth2callback";
	
		  /** The URL to redirect the user to after handling the callback. Consider
		   * saving this in a cookie before redirecting users to the Google
		   * authorization URL if you have multiple possible URL to redirect people to. */
		  public static final String REDIRECT_URL = "/";
	
		  /** The OAuth Token DAO implementation. Consider injecting it instead of using
		   * a static initialization. Also we are using a simple memory implementation
		   * as a mock. Change the implementation to using your database system. */
	//	  public static OAuthTokenDao oauthTokenDao = new OAuthTokenDaoMemoryImpl();
		  def oauthHandler = {
			  // Getting the "error" URL parameter
			  render "ok launchCampaignExtractorAdwordsReport !"
			  String[] error = request.getParameterValues(ERROR_URL_PARAM_NAME);
	
			  // Checking if there was an error such as the user denied access
			  if (error != null && error.length > 0) {
				response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "There was an error: \""+error[0]+"\".");
				return;
			  }
	
			  // Getting the "code" URL parameter
			  String[] code = request.getParameterValues(CODE_URL_PARAM_NAME);
	
			  // Checking conditions on the "code" URL parameter
			  if (code == null || code.length == 0) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The \"code\" URL parameter is missing");
				return;
			  }
	
			  // Construct incoming request URL
			  String requestUrl = getOAuthCodeCallbackHandlerUrl(request);
	
			  // Exchange the code for OAuth tokens
			  GoogleTokenResponse accessTokenResponse = exchangeCodeForAccessAndRefreshTokens(code[0],
				  requestUrl);
	
			  if(accessTokenResponse != null){
				  System.out.println("Token generado no nulo");
				  //GoogleOAuthConfig googleOAuthConfig = GoogleOAuthConfig.findByUserId(USER_ID)
				  if(googleOAuthConfig!= null){
					  System.out.println("Se encuentra un googleOauthConfig en la base con el mismo ClientId, se actualizara");
					  googleOAuthConfig.setAccessToken(accessTokenResponse.getAccessToken())
					  googleOAuthConfig.setRefreshToken(accessTokenResponse.getRefreshToken())
					  accessTokenResponse.getAccessToken()
					  System.out.println("se procede a actualizar");
					  googleOAuthConfig.save(flush:true)
				  }
				  else{
					  System.out.println("No existe un Config con el mismo ClientId");
					  //new GoogleOAuthConfig(accessToken: accessTokenResponse.getAccessToken(),refreshToken: accessTokenResponse.getRefreshToken(),expires: accessTokenResponse.getExpiresInSeconds(),scope: accessTokenResponse.getScope(),tokenId: accessTokenResponse.getIdToken(), tokenType: accessTokenResponse.getTokenType(),  userId: USER_ID ).save(flush:true)
					  System.out.println("Se salvo un Config nuevo.");
				  }
			  }
	
			  System.out.println("EL GRANDIOSO TOKEN !!:" + accessTokenResponse.toString());
	
			  response.sendRedirect(REDIRECT_URL);
		  }
		  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	
		  }
	
		  /**
		   * Construct the OAuth code callback handler URL.
		   *
		   * @param req the HttpRequest object
		   * @return The constructed request's URL
		   */
		  public static String getOAuthCodeCallbackHandlerUrl(HttpServletRequest req) {
			String scheme = req.getScheme() + "://";
			String serverName = req.getServerName();
			String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
			String contextPath = req.getContextPath();
			String servletPath = URL_MAPPING;
			String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
			return scheme + serverName + serverPort + contextPath + servletPath + pathInfo;
		  }
	
		  /**
		   * Exchanges the given code for an exchange and a refresh token.
		   *
		   * @param code The code gotten back from the authorization service
		   * @param currentUrl The URL of the callback
		   * @param oauthProperties The object containing the OAuth configuration
		   * @return The object containing both an access and refresh token
		   * @throws IOException
		   */
		  public GoogleTokenResponse exchangeCodeForAccessAndRefreshTokens(String code, String currentUrl)
			  throws IOException {
	
			HttpTransport httpTransport = new NetHttpTransport();
			JacksonFactory jsonFactory = new JacksonFactory();
	
			// Loading the oauth config file
	//	    oauthProperties oauthProperties = new OAuthProperties();
	
			return new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, code, currentUrl).execute();
		  }
	  
	}
	
