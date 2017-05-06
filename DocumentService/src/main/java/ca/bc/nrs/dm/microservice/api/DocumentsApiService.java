package ca.bc.nrs.dm.microservice.api;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import ca.bc.nrs.dm.microservice.model.Document;
import ca.bc.nrs.dm.microservice.model.History;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class com.io.swagger.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public interface DocumentsApiService {
      public Response documentsGet(SecurityContext securityContext);
      public Response documentsIdExpirePost(Integer id, SecurityContext securityContext);
      public Response documentsIdGet(Integer id, SecurityContext securityContext);
      public Response documentsIdHistoryGet(Integer id, SecurityContext securityContext);
      public Response documentsIdPut(Integer id, Document item, SecurityContext securityContext);
      public Response documentsPost(Attachment item, SecurityContext securityContext);
}
