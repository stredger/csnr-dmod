package ca.bc.gov.nrs.dm.microservice.api.impl;

import ca.bc.gov.nrs.dm.model.v1.DefaultFileMetadata.DocType;
import ca.bc.gov.nrs.dm.microservice.api.DocumentsApiService;

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
import ca.bc.gov.nrs.dm.rest.v1.resource.ACLResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.AbstractFolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.DefaultEngagementFileMetadataResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.EngagementFolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FileResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FilesResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FolderContentResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RevisionsResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.SecurityMetadataResource;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.Oauth2ClientException;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.TokenService;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.impl.TokenServiceImpl;
import ca.bc.gov.webade.oauth2.rest.v1.token.client.resource.AccessToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

// import ca.bc.gov.webade.oauth2.rest.test.client.AuthorizationCodeService;
//import ca.bc.gov.webade.oauth2.rest.test.client.impl.AuthorizationCodeServiceImpl;
//import ca.bc.gov.webade.oauth2.rest.test.resource.AuthorizationCode;


@RequestScoped
@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public class DocumentsApiServiceImpl implements DocumentsApiService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DocumentsApiServiceImpl.class);

    private static DocumentManagementService dmsService = null;

    private String authorizeUrl = "";
    private String tokenUrl = "";
    private static final String SCOPES = "DMS.*";
    private static String redirectUri = "http://www.redirecturi.com";
    private static final String USER_SCOPES = "DMS.CLIENT_USER";
    private static String siteMinderUserId = "NOT\\USED";
    private static String documentManagementTopLevelRestURL;
    private static String oauth2ResourceName;
    private static Gson gson;
    private static final String OAUTH_BEARER = "Bearer ";
    private static String serviceId;
    private static String serviceSecret;
    private static String serviceAccountUsername;
    private static String serviceAccountPassword;
    private static String serviceAccountGuid;
    private static String baseRemotePath;


    public static final String ROOT_FOLDER_NAME = "DMOD";
    public static final String ROOT_FOLDER = "/NRS";
    public static final String SOURCE_FOLDER = "DMAPI_TEST";
    public static final String PARENT_FOLDER = ROOT_FOLDER + "/" + SOURCE_FOLDER;
    public static final String APPLICATION_ROOT_FOLDER = PARENT_FOLDER + "/" + ROOT_FOLDER_NAME;


    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails = null;

    protected String dmsServiceOAuthToken = "";

    public DocumentsApiServiceImpl() {
        // read values from the secret.
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(new String[]{"//tmp/oauth.xml"});

        // credentials
        serviceId = (String) applicationContext.getBean("serviceId");
        serviceSecret = (String) applicationContext.getBean("serviceSecret");

        oAuth2ProtectedResourceDetails = (OAuth2ProtectedResourceDetails) applicationContext.getBean("documentManagementUserResource");

        // URLS
        documentManagementTopLevelRestURL = (String) applicationContext.getBean("documentManagementTopLevelRestUrl");
        authorizeUrl = (String) applicationContext.getBean("authorizeUrl");
        tokenUrl = (String) applicationContext.getBean("tokenUrl");

        // Remote info
        baseRemotePath = (String) applicationContext.getBean("baseRemotePath");

        try {
            // create an instance of the service.
            dmsService = buildDMSClientService(serviceId, serviceSecret, "", "", "");
            setupFolders();
        } catch (Oauth2ClientException ex) {
            LOG.error(null, ex);
        }

        // create a Gson object for use by the various services.  
        gson = new Gson();

    }


    /**
     * Setup the folders used by the application.
     */
    protected void setupFolders() {

        AbstractFolderResource rootFolder = null;
        try {
            rootFolder = dmsService.getFolderByPath(APPLICATION_ROOT_FOLDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rootFolder == null) {
            // create the root folder.
            try {
                AbstractFolderResource parentFolder = dmsService.getFolderByPath(PARENT_FOLDER);

                EngagementFolderResource newFolder = new EngagementFolderResource();

                SecurityMetadataResource securityMetadata = new SecurityMetadataResource();
                securityMetadata.setGeneralVisibility("ExternallyVisible");
                {
                    List<ACLResource> staffVisibility = new ArrayList<ACLResource>();
                    ACLResource alc = new ACLResource();
                    alc.setPermission("RW");
                    alc.setScope("DMS.STAFF_USER_UPDATE");
                    staffVisibility.add(alc);
                    alc.setPermission("R");
                    alc.setScope("DMS.STAFF_USER_READ");
                    staffVisibility.add(alc);
                    securityMetadata.setStaffVisibility(staffVisibility);
                }

                newFolder.setParentFolderID(parentFolder.getItemID());
                newFolder.setSecurityMetadata(securityMetadata);
                newFolder.getDefaultFileMetadata().setSecurityMetadata(securityMetadata);
                newFolder.getDefaultFileMetadata().setOCIOSecurityClassification("CONFIDENTIAL");

                DefaultEngagementFileMetadataResource efm = new DefaultEngagementFileMetadataResource();
                efm.setEngagementID("DMOD");
                efm.setSourceSystem(SOURCE_FOLDER);

                newFolder.setDefaultEngagementFileMetadata(efm);

                rootFolder = dmsService.createEngagementFolder(parentFolder, newFolder);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public DocumentManagementService buildDMSClientService(String serviceId, String serviceSecret, String serviceAccountUsername, String serviceAccountPassword, String serviceAccountGuid) throws Oauth2ClientException {
        System.out.println(serviceId);
        System.out.println(serviceSecret);
        System.out.println(serviceAccountUsername);
        System.out.println(serviceAccountPassword);
        System.out.println(serviceAccountGuid);

        Long ownerOrganizationId = null;
        TokenService tokenService = new TokenServiceImpl(serviceId, serviceSecret, null, tokenUrl);
        AccessToken token = tokenService.getToken(SCOPES);

        /*

        AuthorizationCodeService authorizationCodeService = new AuthorizationCodeServiceImpl(serviceId, authorizeUrl);

        AuthorizationCode authorizationCode = authorizationCodeService.getAuthorizationCode(SCOPES, null, redirectUri, AuthorizationCodeService.SITEMINDER_USER_TYPE_INTERNAL, serviceAccountUsername, serviceAccountGuid, serviceId, serviceSecret, "code");

        System.out.println("Authorization code is " + authorizationCode.getCode());

        AccessToken token = tokenService.getToken(SCOPES, authorizationCode.getCode(), null);
*/
        DocumentManagementService clientService = createDMSClientService(token);
        dmsServiceOAuthToken = OAUTH_BEARER + token.getAccessToken();
        return clientService;
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

    @Override
    public Response documentsGet(SecurityContext securityContext, ServletContext servletContext) {
        // setup dms
        String jsonString = "";

        try {
            // get available documents.
            AbstractFolderResource folderContents = dmsService.getFolderByPath(APPLICATION_ROOT_FOLDER);
            FolderContentResource results = dmsService.browseFolder(folderContents, 1, 1000);
            jsonString = gson.toJson(results);

        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        }
        return Response.ok().entity(jsonString).build();
    }

    @Override
    public Response documentsIdDownloadGet(String id, SecurityContext securityContext) {
        // first get the meta data for the file.                
        try {
            FileResource fileResource;
            fileResource = dmsService.getFileByID(id);
            java.io.File temp = java.io.File.createTempFile("temp-", ".tmp",new File("/uploads"));
            byte[] data = dmsService.getFileContent(id);
            // write the bytes to the temporary file.
            ByteArrayInputStream bis = new ByteArrayInputStream(data);

            return Response.ok(bis, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .build();
        } catch (DocumentManagementException ex) {
            Logger.getLogger(DocumentsApiServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ForbiddenAccessException ex) {
            Logger.getLogger(DocumentsApiServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentsApiServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        // save the data to a temporary file.
        return Response.serverError().build();
    }

    @Override
    public Response documentsIdExpirePost(String id, SecurityContext securityContext) {
        // delete a document
        String jsonString = "";
        // get the file.        
        FileResource fileResource;
        try {
            FileResource fr = dmsService.getFileByID(id);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            fr.setExpireyDate(dateFormat.format(date));
            dmsService.updateFileMetadata(fr);
        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        } catch (ValidationException ex) {
            LOG.error(null, ex);
        }
        return Response.ok().entity("").build();
    }

    @Override
    public Response documentsIdGet(String id, SecurityContext securityContext) {
        // get the meta data
        String jsonString = "";
        // get the file.        
        FileResource fileResource;
        try {
            fileResource = dmsService.getFileByID(id);
            jsonString = gson.toJson(fileResource);
        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        }
        return Response.ok().entity(jsonString).build();
    }

    @Override
    public Response documentsIdHistoryGet(String id, SecurityContext securityContext) {
        String jsonString = "";
        // get the file.        
        FileResource fileResource;
        try {
            fileResource = dmsService.getFileByID(id);
            // get the history.
            RevisionsResource revisions = dmsService.getRevisions(fileResource, 1, 1000);
            jsonString = gson.toJson(revisions);
        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        }

        return Response.ok().entity(jsonString).build();
    }

    // update the meta data.    
    @Override
    public Response documentsIdPut(String id, Attachment file, SecurityContext securityContext) {
        // replace a file        
        String jsonString = "";
        // get the file.        
        FileResource fileResource;
        try {
            fileResource = dmsService.getFileByID(id);

            InputStream fileStream = file.getObject(InputStream.class);
            java.io.File temp = java.io.File.createTempFile("temp-", ".tmp",new File("/uploads"));
            // write the bytes to the temporary file.
            java.nio.file.Files.copy(fileStream, temp.toPath());

            FileResource updateFile = dmsService.checkinFile(id, temp.getAbsolutePath(),
                    "NRSDocument", "Created by DMOD",
                    "Public", "TRAN-102901",
                    null, null, null, "ExternallyVisible", null, null, null, null);
            // cleanup the temporary file.
            temp.delete();

            jsonString = gson.toJson(fileResource);

        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        } catch (IOException ex) {
            LOG.error(null, ex);
        }
        return Response.ok().entity(jsonString).build();
    }

    @Override
    public Response documentsPost(Attachment file, SecurityContext securityContext) {
        String jsonString = "";
        // accept the file upload.

        try {
            // get the folder where files will be stored.
            AbstractFolderResource destinationFolder = dmsService.getFolderByPath(APPLICATION_ROOT_FOLDER);
            EngagementFolderResource engagementFolder = dmsService.getEngagementFolderByID(destinationFolder.getItemID());

            InputStream fileStream = file.getObject(InputStream.class);
            java.io.File temp = java.io.File.createTempFile("temp-", ".tmp",new File("/uploads"));

            // write the bytes to the temporary file.
            java.nio.file.Files.copy(fileStream, temp.toPath(), REPLACE_EXISTING);
            FileResource newFile = dmsService.createFile(temp.getAbsolutePath(), engagementFolder, DocType.NRSDocument.name(), "Created by DMOD",
                    "Public", "TRAN-102901",
                    null, null, null, "ExternallyVisible", null, null, null, null);

            jsonString = gson.toJson(newFile.getItemID());

            // cleanup
            temp.delete();

        } catch (IOException ex) {
            LOG.error(null, ex);
        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        }

        // process the fileStream.      
        return Response.ok().entity(jsonString).build();
    }

    @Override
    public Response documentsSearchGet(String fullTextWordsSearch, SecurityContext securityContext) {
        String jsonString = "";
        // get the file.        
        AbstractFolderResource folderResource;
        FilesResource searchFiles = null;
        try {
            // change to the dmod folder.
            folderResource = dmsService.getFolderByPath(APPLICATION_ROOT_FOLDER);
            // do the search

            searchFiles = dmsService.searchFiles(folderResource, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null, fullTextWordsSearch, true, null, null, null, null, null, null, null, 1, 30, null);
            jsonString = gson.toJson(searchFiles);
        } catch (DocumentManagementException ex) {
            LOG.error(null, ex);
        } catch (ForbiddenAccessException ex) {
            LOG.error(null, ex);
        }

        return Response.ok().entity(jsonString).build();
    }
}
