package com.nrsdm.client.api;

import com.nrsdm.client.api.*;
import com.nrsdm.client.model.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.nrsdm.client.model.Document;
import com.nrsdm.client.model.History;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")
public interface DocumentsApiService {
      public Response documentsGet(SecurityContext securityContext);
      public Response documentsIdExpirePost(Integer id, SecurityContext securityContext);
      public Response documentsIdGet(Integer id, SecurityContext securityContext);
      public Response documentsIdHistoryGet(Integer id, SecurityContext securityContext);
      public Response documentsIdPut(Integer id, Document item, SecurityContext securityContext);
      public Response documentsPost(Document item, SecurityContext securityContext);
}
