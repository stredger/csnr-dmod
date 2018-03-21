package ca.bc.gov.nrs.dm.microservice.api;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

@javax.annotation.Generated(value = "class com.io.swagger.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public interface DocumentsApiService {
      public Response documentsGet(HttpHeaders headers);
      public Response documentsDownloadFile(String id, HttpHeaders headers);    
      public Response documentsPutFileMetadata(String id, String data, HttpHeaders headers);
      public Response documentsGetFile(String id, HttpHeaders headers);
      public Response documentsGetFolder(String id, HttpHeaders headers);
      public Response documentsDeleteFolder(String id, HttpHeaders headers);
      public Response documentsGetFileHistory(String id, HttpHeaders headers);
      public Response documentsPutFile(String id, String filename, Attachment file,  HttpHeaders headers);
      public Response documentsPostFile(String id, String filename, Attachment item, HttpHeaders headers);
      public Response documentsSearchGet(String fullTextWordsSearch, HttpHeaders headers);
      public Response documentsPostFolder(String id, String data, HttpHeaders headers);
      public Response documentsPutFolderMetadata(String id, String data, HttpHeaders headers);
}
