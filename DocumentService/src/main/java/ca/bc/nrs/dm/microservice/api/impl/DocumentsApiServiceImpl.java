package ca.bc.nrs.dm.microservice.api.impl;

import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementService;
import ca.bc.gov.nrs.dm.rest.client.v1.impl.DocumentManagementServiceImpl;
import ca.bc.nrs.dm.microservice.api.DocumentsApiService;

//import com.google.code.gson;

import ca.bc.nrs.dm.microservice.model.Document;
import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;



import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementException;
import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementService;
import ca.bc.gov.nrs.dm.rest.client.v1.ForbiddenAccessException;
import ca.bc.gov.nrs.dm.rest.client.v1.ValidationException;
import ca.bc.gov.nrs.dm.rest.client.v1.impl.DocumentManagementServiceImpl;
//import ca.bc.gov.nrs.dm.rest.v1.resource.AbstractFolderResource;
//import ca.bc.gov.nrs.dm.rest.v1.resource.FolderContentResource;
import ca.bc.gov.nrs.dm.service.v1.DMServiceResponse;
import ca.bc.gov.nrs.dm.service.v1.FileService;
import ca.bc.gov.nrs.dm.service.v1.FolderService;
//import ca.bc.gov.webade.oauth2.rest.test.client.AuthorizationCodeService;
//import ca.bc.gov.webade.oauth2.rest.test.client.impl.AuthorizationCodeServiceImpl;
//import ca.bc.gov.webade.oauth2.rest.test.resource.AuthorizationCode;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.Oauth2ClientException;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.TokenService;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.impl.TokenServiceImpl;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.resource.AccessToken;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;


@RequestScoped
@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public class DocumentsApiServiceImpl implements DocumentsApiService {
    
      private static DocumentManagementService dmsService = null;      
    
      private String authorizeUrl = "";
      private String tokenUrl = "";
      private static final String SCOPES = "DMS.*"; 
      private static String redirectUri = "http://www.redirecturi.com";
      private static final String USER_SCOPES ="DMS.CLIENT_USER";
      private static String siteMinderUserId = "NOT\\USED";
      private static String documentManagementTopLevelRestURL;
      private static String oauth2ResourceName;   
      
      private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails = null;
      
      public void setOAuth2ProtectedResourceDetails(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
        this.oAuth2ProtectedResourceDetails = oAuth2ProtectedResourceDetails;
      }
      
      public DocumentsApiServiceImpl()
      {
        // read values from the secret.
        Gson gson = new Gson();
        //secrets = gson.fromJson(yourJson, Response.class);
        
          
        String siteMinderUserGuid;
        String individualUserId;
        String individualSecret;
        Long ownerOrganizationId;  
        
        siteMinderUserGuid = "";
        individualUserId = "";
        individualSecret = "";
        ownerOrganizationId = 5L;  
        
          try {
              // setup the Document Management Service.
              dmsService = buildDMSClientBusinessUser( siteMinderUserGuid, individualUserId, individualSecret, ownerOrganizationId);
          } catch (Oauth2ClientException ex) {
              Logger.getLogger(DocumentsApiServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      private DocumentManagementService buildDMSClientBusinessUser(String siteMinderUserGuid, String individualUserId, String individualSecret, Long ownerOrganizationId ) throws Oauth2ClientException
	{

		String smartFormsTestClient = "";
		String smartFormsTestSecret = "";
	        /*
		AuthorizationCodeService authorizationCodeService;
                authorizationCodeService = new AuthorizationCodeServiceImpl(smartFormsTestClient, authorizeUrl);
		TokenService tokenService = new TokenServiceImpl(smartFormsTestClient, smartFormsTestSecret, null, tokenUrl);
		
		// Getting the application code
		AuthorizationCode authorizationCode = authorizationCodeService.getAuthorizationCode(SCOPES, ownerOrganizationId, redirectUri, 
		AuthorizationCodeService.SITEMINDER_USER_TYPE_BUSINESS, siteMinderUserId, siteMinderUserGuid, null, null, "code");
*/
		//then we use this code to get the token 
		//AccessToken token = tokenService.getToken(USER_SCOPES, authorizationCode.getCode(), redirectUri);
		AccessToken token = new AccessToken();
		DocumentManagementService clientService = createDMSClientService(token);
		
		return clientService;
	}
      
        
      
	private DocumentManagementService createDMSClientService( AccessToken token )
	{                
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuth2ProtectedResourceDetails);
		OAuth2ClientContext context = restTemplate.getOAuth2ClientContext();
		context.setAccessToken(new DefaultOAuth2AccessToken(token.getAccessToken()));

		DocumentManagementService clientService = new DocumentManagementServiceImpl(false);
		((DocumentManagementServiceImpl) clientService).setRestTemplate(restTemplate);
		((DocumentManagementServiceImpl) clientService).setTopLevelRestURL(documentManagementTopLevelRestURL);

		return clientService;
	}

      
      @Override
      public Response documentsGet(SecurityContext securityContext) {
      // do some magic!
      return Response.ok().entity("magic!").build();
  }
      @Override
      public Response documentsIdExpirePost(Integer id, SecurityContext securityContext) {
      // do some magic!
      return Response.ok().entity("magic!").build();
  }
      @Override
      public Response documentsIdGet(Integer id, SecurityContext securityContext) {
      
            //    FileResource myFile = dmsService.getFileByID(id);
            // return the file as binary data.
      return Response.ok().entity("magic!").build();

  }
      @Override
      public Response documentsIdHistoryGet(Integer id, SecurityContext securityContext) {
      // do some magic!
      return Response.ok().entity("magic!").build();
  }
      @Override
      public Response documentsIdPut(Integer id, Document item, SecurityContext securityContext) {
      // do some magic!
      return Response.ok().entity("magic!").build();
  }
      @Override
      public Response documentsPost(Attachment file, SecurityContext securityContext) {
      // accept the file upload.
      InputStream fileStream = file.getObject(InputStream.class);
        // process the fileStream.      
      return Response.ok().entity("OK").build();
  }
}
