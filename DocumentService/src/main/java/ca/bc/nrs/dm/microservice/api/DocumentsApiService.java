package ca.bc.nrs.dm.microservice.api;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import ca.bc.nrs.dm.microservice.model.Document;
import ca.bc.nrs.dm.microservice.model.History;
import javax.servlet.ServletContext;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class com.io.swagger.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public interface DocumentsApiService {
      public Response documentsGet(SecurityContext securityContext, ServletContext servletContext);
      public Response documentsIdDownloadGet(String id, SecurityContext securityContext);    
      public Response documentsIdDeletePost(String id, SecurityContext securityContext);
      public Response documentsIdGet(String id, SecurityContext securityContext);
      public Response documentsIdHistoryGet(String id, SecurityContext securityContext);
      public Response documentsIdPut(String id, Attachment file, SecurityContext securityContext);
      public Response documentsPost(Attachment item, SecurityContext securityContext);
}
