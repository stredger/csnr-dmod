package ca.bc.gov.nrs.dm.microservice.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import ca.bc.gov.nrs.dm.microservice.model.Message;
import ca.bc.gov.nrs.dm.rest.v1.resource.AbstractFolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FileResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FilesResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FolderContentResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RevisionsResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses; 


@Path("/")
@RequestScoped

@Api(description = "The Documents API")


@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")

public class DocumentsApi  {
	
   @Inject 
   DocumentsApiService delegate;
       
    @GET        
    @Produces({"application/json"})
    @ApiOperation(value = "Get all the folders and files in the application directory in DMS", 
    	notes = "Folders and Files Data", 
    	response = FolderContentResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = FolderContentResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class)})
    public Response documentsGet(@Context HttpHeaders headers) {
    	return delegate.documentsGet(headers);
    }
    
    @GET
    @Path("/files/{id}/download")
    @Produces({"application/json"})
    @ApiOperation(value = "Stream the content of the file for the specified id.",
    	notes = "File content")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsDownloadFile(
    	@ApiParam(value = "id of the file",required=true) 
    	@PathParam("id") String id,
    	@Context HttpHeaders headers) {
    	return delegate.documentsDownloadFile(id, headers);
    }

    @PUT
    @Path("/files/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update the metadata information of the file", 
    	notes = "File Data", response = FileResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = FileResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsPutFileMetadata(@ApiParam(value = "id of Document to update",required=true) 
    	@PathParam("id") String id,
    	@ApiParam(value = "the data information in json format",required=true) String data,
    	@Context HttpHeaders headers) {
    	return delegate.documentsPutFileMetadata(id, data, headers);
    }
    
    @GET
    @Path("/files/{id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieves the metadata information of the file for the specified id", 
    	notes = "File Data", response = FileResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = FileResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsGetFile(
    	@ApiParam(value = "id of file to fetch",required=true) 
    	@PathParam("id") String id, 
    	@Context HttpHeaders headers) {
    	return delegate.documentsGetFile(id, headers);
    }


    @GET
    @Path("/files/{id}/history")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve the history of the file specified by the id", 
    	notes = "File Data", response = RevisionsResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = RevisionsResource.class, responseContainer = "List"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsGetFileHistory(
    	@ApiParam(value = "id of file to get history for",required=true) 
    	@PathParam("id") String id, @Context HttpHeaders headers) {
    	return delegate.documentsGetFileHistory(id, headers);
    }
    
    @GET
    @Path("/search")
    @Produces({ "application/json"})
    @ApiOperation(value = "Search files that contains the input search query", 
    	notes = "File Data", response = FilesResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = FilesResource.class, responseContainer = "List"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsSearchGet(@ApiParam(value = "search query",required=true) 
    	@QueryParam("fullTextWordsSearch") String fullTextWordsSearch, @Context HttpHeaders headers) {
    	return delegate.documentsSearchGet(fullTextWordsSearch, headers);
    }

    @PUT
    @Path("files/{id}/content")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ "application/json"})
    @ApiOperation(value = "Update the content of the file", 
    	notes = "File Data", response = FileResource.class)
    @ApiResponses(value = { 
	    @ApiResponse(code = 200, message = "OK", response = FileResource.class),
	    @ApiResponse(code = 400, message = "Missing file Data"),
	    @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsPutFile(@ApiParam(value = "id of Document to fetch",required=true)
    	@PathParam("id") String id, 
    	@ApiParam(value = "The file to replace",required=true) MultipartBody multipart, 
    	@Context HttpHeaders headers) {
        Attachment file = multipart.getAttachment("file");
        if (file == null) {
            return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Missing file Data").type(MediaType.TEXT_PLAIN).build();
        } else {   
        	String filename = multipart.getAttachment("name").getObject(String.class);
            return delegate.documentsPutFile(id, filename, file, headers);
        }
    }

    @POST
    @Path("files/content")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json"})
    @ApiOperation(value = "Uploads the content of the file into the root application directory", 
		notes = "File Data", response = FileResource.class)
    @ApiResponses(value = { 
    	    @ApiResponse(code = 200, message = "OK", response = FileResource.class),
    	    @ApiResponse(code = 400, message = "Missing file Data"),
    	    @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response uploadFileToRoot(@ApiParam(value = "The file to upload",required=true)
		MultipartBody multipart, @Context HttpHeaders headers){            
    	Attachment file = multipart.getAttachment("file");
    	if (file == null) {
            return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Missing file Data").type(MediaType.TEXT_PLAIN).build();
        } else {
        	String filename = multipart.getAttachment("name").getObject(String.class);
            return delegate.documentsPostFile(null, filename, file, headers);
        }
    }
    
    
    
    @POST
    @Path("/folders/{id}/files/content")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json"})
    @ApiOperation(value = "Uploads the content of the file into the folder specified by id", 
		notes = "File Data", response = FileResource.class)
    @ApiResponses(value = { 
	    @ApiResponse(code = 200, message = "OK", response = FileResource.class),
	    @ApiResponse(code = 400, message = "Missing file Data"),
	    @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response uploadFileToFolder(
    	@ApiParam(value = "id parent folder",required=true)
        @PathParam("id") String id,
    	@ApiParam(value = "The file to upload",required=true)
		MultipartBody multipart, @Context HttpHeaders headers){            
    	Attachment file = multipart.getAttachment("file");
    	if (file == null) {
            return Response.status(400).entity("Missing file data").type(MediaType.TEXT_PLAIN).build();
        } else {
        	String filename = multipart.getAttachment("name").getObject(String.class);
            return delegate.documentsPostFile(id, filename, file, headers);
        }
    }
    
    @Path("/folders/{id}")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Creates a subfolder under into the specified folder id", 
    	notes = "Folder Data", response = AbstractFolderResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AbstractFolderResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response createChildFolder(
    		@ApiParam(value = "id parent folder",required=true)
        	@PathParam("id") String id,
    		@ApiParam(value = "the data information in json format, must contain a name of the child folder to create",required=true) String data,
    		@Context HttpHeaders headers){            
    
        return delegate.documentsPostFolder(id, data, headers);
    }
    
    @GET
    @Path("/folders/{id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieves the metadata information of the folder for the specified id", 
    	notes = "Folder Data", response = AbstractFolderResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AbstractFolderResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsGetFolder(
    	@ApiParam(value = "id of folder to fetch",required=true) 
    	@PathParam("id") String id, 
    	@Context HttpHeaders headers) {
    	return delegate.documentsGetFolder(id, headers);
    }
    
    @DELETE
    @Path("/folders/{id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Delete the folder for the specified id", 
    	notes = "Folder Data")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Message.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsDeleteFolder(
    	@ApiParam(value = "id of folder to delete",required=true) 
    	@PathParam("id") String id, 
    	@Context HttpHeaders headers) {
    	return delegate.documentsDeleteFolder(id, headers);
    }
    
    @Path("/folders")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Creates a folder under the root application directory", 
		notes = "Folder Data", response = AbstractFolderResource.class)
    public Response createFolder(
    		@ApiParam(value = "the data information in json format, must contain a name of the child folder to create",required=true) String data,
    		@Context HttpHeaders headers){            
    
        return delegate.documentsPostFolder(null, data, headers);
    }
    
    

    @PUT
    @Path("/folders/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update the metadata information of the folder", 
    	notes = "Folder Data", response = AbstractFolderResource.class)
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AbstractFolderResource.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Message.class) })
    public Response documentsPutFolderMetadata(@ApiParam(value = "id of Document to update",required=true) 
    	@PathParam("id") String id,
    	@ApiParam(value = "the data information in json format",required=true) String data,
    	@Context HttpHeaders headers) {
    	return delegate.documentsPutFolderMetadata(id, data, headers);
    }
    
    
}
