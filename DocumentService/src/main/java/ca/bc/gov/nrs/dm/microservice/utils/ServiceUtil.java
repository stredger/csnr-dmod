package ca.bc.gov.nrs.dm.microservice.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementService;
import ca.bc.gov.nrs.dm.rest.client.v1.impl.DocumentManagementServiceImpl;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.TokenService;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.impl.TokenServiceImpl;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.resource.AccessToken;

public class ServiceUtil {
	private static ServiceUtil INSTANCE = new ServiceUtil();
	private static final Logger LOG = LoggerFactory.getLogger(ServiceUtil.class);
	
	public static final String OAUTH_BEARER = "Bearer";
	public static final String DMS_SCOPES = "DMS.*"; 
	public static final String READ_WRITE_SCOPES = "UPDATE_FILES";
	public static final String READ_SCOPES = "READ_FILES";
    public static final int MAX_ENTRY = 1000;
    public static final int PAGE = 1;
    public static final String READ_WRITE = "RW";
    public static final String READ = "R";
    
    
	private DocumentManagementService dmsServiceClient = null;
    private String applicationAcronym = null;
    private String applicationRootDir = null;
    
    private String serviceId = null;
    private String serviceSecret = null;
    private String defaultDocumentVisibility = null;
    private String defaultOCIOClassification = null;
    private String documentManagementTopLevelRestURL = null;
    private String tokenUrl = null;
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails = null;
    
    private Map<String, String> securityScopes = null;

    private ServiceUtil() {
 
    	FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(new String[]{"//tmp/oauth.xml"});
    	serviceId = (String) applicationContext.getBean("serviceId");
	    serviceSecret = (String) applicationContext.getBean("serviceSecret");
	    
	    defaultDocumentVisibility = (String) applicationContext.getBean("defaultDocumentVisibility");
	    defaultOCIOClassification = (String)applicationContext.getBean("defaultOCIOClassification");
	    
	    applicationAcronym = (String) applicationContext.getBean("applicationAcronym");
	    applicationRootDir = (String) applicationContext.getBean("applicationRootDir");
	    
	    oAuth2ProtectedResourceDetails = (OAuth2ProtectedResourceDetails) applicationContext.getBean("documentManagementUserResource");

	    // URLS
	    documentManagementTopLevelRestURL = (String) applicationContext.getBean("documentManagementTopLevelRestUrl");
	    tokenUrl = (String) applicationContext.getBean("tokenUrl");
	    
	    securityScopes = new HashMap<String, String>();
	    String readWriteSecurity = applicationAcronym + "." + ServiceUtil.READ_WRITE_SCOPES;
	    String readSecurity = applicationAcronym + "." + ServiceUtil.READ_SCOPES;
	    securityScopes.put(READ_WRITE, readWriteSecurity);
	    securityScopes.put(READ, readSecurity);
    	
	    applicationContext.close();
    }
    
    public  ServiceUtil(FileSystemXmlApplicationContext applicationContext) {
    	serviceId = (String) applicationContext.getBean("serviceId");
	    serviceSecret = (String) applicationContext.getBean("serviceSecret");
	    
	    applicationAcronym = (String) applicationContext.getBean("applicationAcronym");
	    applicationRootDir = (String) applicationContext.getBean("applicationRootDir");
	    
	    oAuth2ProtectedResourceDetails = (OAuth2ProtectedResourceDetails) applicationContext.getBean("documentManagementUserResource");

	    // URLS
	    documentManagementTopLevelRestURL = (String) applicationContext.getBean("documentManagementTopLevelRestUrl");
	    tokenUrl = (String) applicationContext.getBean("tokenUrl");
    }
    
    public static ServiceUtil getInstance() {
    	return INSTANCE;
    }
    
    public String getRootDirectory() throws Exception {
    	if(applicationRootDir == null || applicationRootDir.trim().isEmpty()) {
    		throw new Exception("Application Root Dir is not properly setup!");
    	}
    	
    	if(applicationAcronym == null || applicationAcronym.trim().isEmpty()) {
    		throw new Exception("Application Acronym is not properly setup!");
    	}
    	
    	StringBuffer rootDir = new StringBuffer("/")
    			.append(applicationRootDir.trim())
    			.append("/")
    			.append(applicationAcronym.trim());
    	
    	return rootDir.toString();
    }
    
    public String getScopes() throws Exception {
    	if(applicationAcronym == null || applicationAcronym.trim().isEmpty()) {
    		throw new Exception("Application Acronym is not properly setup!");
    	}
    	
    	StringBuffer scopes = new StringBuffer(DMS_SCOPES)
    			.append(",")
    			.append(applicationAcronym.trim())
    			.append(".*");
    	
    	return scopes.toString();
    }
    
    public String getSecurityScopes(String permission) throws Exception {
    	if(applicationAcronym == null || applicationAcronym.trim().isEmpty()) {
    		throw new Exception("Application Acronym is not properly setup!");
    	}
    	
    	return securityScopes.get(permission);
    }
    
    public String getApplicationAcronym() {
    	return this.applicationAcronym;
    }
    
    public String getDefaultDocumentVisibility() {
    	return this.defaultDocumentVisibility;
    }
    
    public String getDefaultOCIOClassification() {
    	return this.defaultOCIOClassification;
    }
    
    public DocumentManagementService getServiceClient(HttpHeaders headers) {
		String accessToken = getAccessToken(headers);
		DocumentManagementService service = null;
		
		if(accessToken != null) {
			AccessToken token = new AccessToken();
    		token.setAccessToken(accessToken);
    		service = createDMSClientService(token);
		} else {
			service = getServiceClient();
		}
		return service;
	}
    
    
    public DocumentManagementService getServiceClient() {
		if(dmsServiceClient == null) {
	        try {
	        	
	        	TokenService tokenService = new TokenServiceImpl(serviceId, serviceSecret, null, tokenUrl);
		        AccessToken token = tokenService.getToken(getScopes());
		        
		        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuth2ProtectedResourceDetails);
		        OAuth2ClientContext context = restTemplate.getOAuth2ClientContext();
		        context.setAccessToken(new DefaultOAuth2AccessToken(token.getAccessToken()));

		        dmsServiceClient = new DocumentManagementServiceImpl(false);
		        ((DocumentManagementServiceImpl) dmsServiceClient).setRestTemplate(restTemplate);
		        ((DocumentManagementServiceImpl) dmsServiceClient).setTopLevelRestURL(documentManagementTopLevelRestURL);
		        
	        } catch (Exception ex) {
	            LOG.error("getServiceClient", ex);
	        }
		}
		
		return dmsServiceClient;
	}

	private String getAccessToken(HttpHeaders headers) {
    	String token = null;
    	List<String> authList = headers.getRequestHeader("Authorization");
    	
    	if(authList != null && authList.size() > 0) {
    		String bearerToken = authList.get(0).trim();
    		if(bearerToken.startsWith(OAUTH_BEARER) && bearerToken.trim().length() > OAUTH_BEARER.length()) {
    			token = bearerToken.substring(OAUTH_BEARER.length() + 1, bearerToken.length());
    			token = token.trim();
    		}
    	}
    	
    	return token;
    }
	
	private DocumentManagementService createDMSClientService(AccessToken token) {
		  OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuth2ProtectedResourceDetails);
		  OAuth2ClientContext context = restTemplate.getOAuth2ClientContext();
		  context.setAccessToken(new DefaultOAuth2AccessToken(token.getAccessToken()));
		
		  DocumentManagementService clientService = new DocumentManagementServiceImpl(false);
		  ((DocumentManagementServiceImpl) clientService).setRestTemplate(restTemplate);
		  ((DocumentManagementServiceImpl) clientService).setTopLevelRestURL(documentManagementTopLevelRestURL);
	
	  return clientService;
	}
	
	
	
}
