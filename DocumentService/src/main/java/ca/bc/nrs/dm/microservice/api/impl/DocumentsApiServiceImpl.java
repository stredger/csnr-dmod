package ca.bc.nrs.dm.microservice.api.impl;

import ca.bc.gov.nrs.dm.rest.client.v1.impl.DocumentManagementServiceImpl;
import ca.bc.nrs.dm.microservice.api.DocumentsApiService;


import ca.bc.nrs.dm.microservice.model.Document;
import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;


@RequestScoped
@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public class DocumentsApiServiceImpl implements DocumentsApiService {
    
      private DocumentManagementServiceImpl dmsService;
    
      public DocumentsApiServiceImpl()
      {
          // setup the Document Management Service.
          dmsService = new DocumentManagementServiceImpl();
          // setup credentials.
          
          //dmsService.setRestTemplate();
          //dmsService.setTopLevelRestURL("topLevelRestURL");
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
