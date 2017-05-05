package com.nrsdm.client.api;

import com.nrsdm.client.model.Document;
import com.nrsdm.client.model.History;
import com.nrsdm.client.api.DocumentsApiService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.swagger.annotations.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import java.util.List;

@Path("/")
@RequestScoped

@Api(description = "the documents API")


@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")

public class DocumentsApi  {

  @Context SecurityContext securityContext;

  @Inject DocumentsApiService delegate;


    @GET
    
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class, responseContainer = "List") })
    public Response documentsGet() {
    	return delegate.documentsGet(securityContext);
    }

    @POST
    @Path("/{id}/expire")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = void.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = void.class),
        @ApiResponse(code = 404, message = "Document not found", response = void.class) })
    public Response documentsIdExpirePost(@ApiParam(value = "id of Document to expire",required=true) @PathParam("id") Integer id) {
    	return delegate.documentsIdExpirePost(id, securityContext);
    }

    @GET
    @Path("/{id}")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class),
        @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    public Response documentsIdGet(@ApiParam(value = "id of Document to fetch",required=true) @PathParam("id") Integer id) {
    	return delegate.documentsIdGet(id, securityContext);
    }

    @GET
    @Path("/{id}/history")
    
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = History.class, responseContainer = "List", tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = History.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Document not found", response = History.class, responseContainer = "List") })
    public Response documentsIdHistoryGet(@ApiParam(value = "id of Document to get history for",required=true) @PathParam("id") Integer id) {
    	return delegate.documentsIdHistoryGet(id, securityContext);
    }

    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Document.class),
        @ApiResponse(code = 404, message = "Document not found", response = Document.class) })
    public Response documentsIdPut(@ApiParam(value = "id of Document to fetch",required=true) @PathParam("id") Integer id, @ApiParam(value = "" ,required=true) Document item) {
    	return delegate.documentsIdPut(id, item, securityContext);
    }

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "text/plain", "application/json", "text/json" })
    @ApiOperation(value = "", notes = "", response = Document.class, tags={ "Document" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Document created", response = Document.class) })
    public Response documentsPost(@ApiParam(value = "" ,required=true) Document item) {
    	return delegate.documentsPost(item, securityContext);
    }
}
