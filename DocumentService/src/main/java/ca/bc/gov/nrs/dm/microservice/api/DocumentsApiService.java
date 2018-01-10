package ca.bc.gov.nrs.dm.microservice.api;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

@javax.annotation.Generated(value = "class com.io.swagger.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public interface DocumentsApiService {
      public Response documentsGet(String accessToken);
      public Response documentsIdDownloadGet(String id, String accessToken);    
      public Response documentsIdExpirePost(String id, String accessToken);
      public Response documentsIdGet(String id, String accessToken);
      public Response documentsIdHistoryGet(String id, String accessToken);
      public Response documentsIdPut(String id, Attachment file, String accessToken);
      public Response documentsPost(Attachment item, String accessToken);
      public Response documentsSearchGet(String fullTextWordsSearch, String accessToken);
}
