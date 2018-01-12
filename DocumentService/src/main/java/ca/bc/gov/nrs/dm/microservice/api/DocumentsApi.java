package ca.bc.gov.nrs.dm.microservice.api;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
//import ca.bc.gov.nrs.dm.microservice.api.DocumentsApiService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import ca.bc.gov.nrs.dm.microservice.model.Document;
import ca.bc.gov.nrs.dm.microservice.model.History;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses; 


@Path("/")
@RequestScoped

@Api(description = "the documents API")


@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")

public class DocumentsApi  {

 
   @Inject DocumentsApiService delegate;
   
    private static final String OAUTH_BEARER = "Bearer";
    
    private String getAccessToken(HttpHeaders headers) {
    	String token = null;
    	List<String> authList = headers.getRequestHeader("Authorization");
    	
    	if(authList != null && authList.size() > 0) {
    		String bearerToken = authList.get(0).trim();
    		if(bearerToken.startsWith(OAUTH_BEARER)) {
    			token = bearerToken.substring(OAUTH_BEARER.length() + 1, bearerToken.length());
    		}
    	}
    	
    	return token;
    }
    @GET        
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class, responseContainer = "List") })
    public Response documentsGet(@Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsGet(token);
    }

    @POST
    @Path("/{id}/expire")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = void.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = void.class),
        @ApiResponse(code = 404, message = "Document not found", response = void.class) })
    public Response documentsIdExpirePost(@ApiParam(value = "id of Document to expire",required=true) 
    	@PathParam("id") String id,
    	@Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsIdExpirePost(id, token);
    }

    @GET
    @Path("/{id}")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class),
        @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    public Response documentsIdGet(@ApiParam(value = "id of Document to fetch",required=true) 
    	@PathParam("id") String id, 
    	@Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsIdGet(id, token);
    }

    @GET
    @Path("/{id}/download")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsIdDownloadGet(@ApiParam(value = "id of Document to download",required=true) 
    	@PathParam("id") String id,
    	@Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsIdDownloadGet(id, token);
    }

    
    @GET
    @Path("/{id}/history")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = History.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = History.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsIdHistoryGet(@ApiParam(value = "id of Document to get history for",required=true) 
    	@PathParam("id") String id, @Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsIdHistoryGet(id, token);
    }
    
    @GET
    @Path("/search")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = History.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsSearchGet(@ApiParam(value = "search query",required=true) 
    	@QueryParam("fullTextWordsSearch") String fullTextWordsSearch, @Context HttpHeaders headers) {
    	String token = getAccessToken(headers);
    	return delegate.documentsSearchGet(fullTextWordsSearch, token);
    }

    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
    @ApiResponse(code = 200, message = "OK", response = Document.class),
    @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    
    public Response documentsIdPut(@ApiParam(value = "id of Document to fetch",required=true)
    	@PathParam("id") String id, MultipartBody multipart, @Context HttpHeaders headers) {
        Attachment file = multipart.getAttachment("file");
        if (file == null) {
            return Response.status(400).entity("Missing file data").type(MediaType.TEXT_PLAIN).build();
        }
        else
        {   
        	String token = getAccessToken(headers);
            return delegate.documentsIdPut(id, file, token);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ "text/plain", "application/json", "text/json" })
    public Response upload(@Multipart("file") Attachment file, @Context HttpHeaders headers ){            
        //Attachment file = multipart.getAttachment("file");
        
        if (file == null) {
            return Response.status(400).entity("Missing file data").type(MediaType.TEXT_PLAIN).build();
        }
        else       
        {
        	String token = getAccessToken(headers);
            return delegate.documentsPost(file, token);
        }
    }
}
