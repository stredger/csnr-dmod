package ca.bc.gov.nrs.dm.microservice.api;

import ca.bc.gov.nrs.dm.microservice.model.Document;
import ca.bc.gov.nrs.dm.microservice.model.History;

//import ca.bc.gov.nrs.dm.microservice.api.DocumentsApiService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType; 
import javax.ws.rs.core.SecurityContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;


import io.swagger.annotations.*;
import javax.servlet.ServletContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.*; 


@Path("/")
@RequestScoped

@Api(description = "the documents API")


@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")

public class DocumentsApi  {

  @Context SecurityContext securityContext;
  @Context ServletContext servletContext;

  @Inject DocumentsApiService delegate;

    @GET        
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class, responseContainer = "List") })
    public Response documentsGet() {
    	return delegate.documentsGet(securityContext, servletContext);
    }

    @POST
    @Path("/{id}/expire")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = void.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = void.class),
        @ApiResponse(code = 404, message = "Document not found", response = void.class) })
    public Response documentsIdExpirePost(@ApiParam(value = "id of Document to expire",required=true) @PathParam("id") String id) {
    	return delegate.documentsIdExpirePost(id, securityContext);
    }

    @GET
    @Path("/{id}")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class),
        @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    public Response documentsIdGet(@ApiParam(value = "id of Document to fetch",required=true) @PathParam("id") String id) {
    	return delegate.documentsIdGet(id, securityContext);
    }

    @GET
    @Path("/{id}/download")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsIdDownloadGet(@ApiParam(value = "id of Document to download",required=true) @PathParam("id") String id) {
    	return delegate.documentsIdDownloadGet(id, securityContext);
    }

    
    @GET
    @Path("/{id}/history")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = History.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = History.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsIdHistoryGet(@ApiParam(value = "id of Document to get history for",required=true) @PathParam("id") String id) {
    	return delegate.documentsIdHistoryGet(id, securityContext);
    }
    
    @GET
    @Path("/search")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = History.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsSearchGet(@ApiParam(value = "search query",required=true) @QueryParam("fullTextWordsSearch") String fullTextWordsSearch) {
    	return delegate.documentsSearchGet(fullTextWordsSearch, securityContext);
    }

    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
    @ApiResponse(code = 200, message = "OK", response = Document.class),
    @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    
    public Response documentsIdPut(@ApiParam(value = "id of Document to fetch",required=true) @PathParam("id") String id, MultipartBody multipart) {
        Attachment file = multipart.getAttachment("file");
        if (file == null) {
            return Response.status(400).entity("Missing file data").type(MediaType.TEXT_PLAIN).build();
        }
        else
        {            
            return delegate.documentsIdPut(id, file, securityContext);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ "text/plain", "application/json", "text/json" })
    public Response upload(@Multipart("file") Attachment file){            
        //Attachment file = multipart.getAttachment("file");
        
        if (file == null) {
            return Response.status(400).entity("Missing file data").type(MediaType.TEXT_PLAIN).build();
        }
        else       
        {
            return delegate.documentsPost(file, securityContext);
        }
    }
}
