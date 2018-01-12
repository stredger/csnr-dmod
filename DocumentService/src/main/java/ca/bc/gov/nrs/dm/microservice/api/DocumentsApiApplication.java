package ca.bc.gov.nrs.dm.microservice.api;



import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.SwaggerFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.utils.Manifests;

@javax.annotation.Generated(value = "class com.quartech.codegen.FuseGenerator", date = "2017-04-24T09:07:23.579-07:00")


@ApplicationPath("/api/documents")

public class DocumentsApiApplication extends Application{
    
    
    
	@Inject
	private DocumentsApi _DocumentsApi; 
	
	private static final Logger LOG = LoggerFactory.getLogger(DocumentsApiApplication.class);
    private static SwaggerFeature swaggerFeature;    
/*
    @Override
public Map<String, Object> getProperties() {
    Map<String, Object> props = new HashMap<>();
    props.put("jersey.config.server.provider.classnames", 
            "org.glassfish.jersey.media.multipart.MultiPartFeature");
    return props;
}
*/
    
 @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(MultiPartFeature.class);
        return resources;
    }

    
    @Override
    public Set<Object> getSingletons() {
    	
    	if (swaggerFeature == null) {
            swaggerFeature = new SwaggerFeature();
			try {
				Manifest manifest = Manifests.getManifestFromCurrentJar(getClass());
				Map<Manifests.Attribute,String> projectInfo = Manifests.getManifestEntryMap(manifest, Manifests.PROJECT_ATTRIBUTES.class);
				swaggerFeature.setTitle(      projectInfo.get(Manifests.PROJECT_ATTRIBUTES.Title));
				swaggerFeature.setDescription(projectInfo.get(Manifests.PROJECT_ATTRIBUTES.Description));
				swaggerFeature.setLicense(    projectInfo.get(Manifests.PROJECT_ATTRIBUTES.License));
				swaggerFeature.setLicenseUrl( projectInfo.get(Manifests.PROJECT_ATTRIBUTES.LicenseUrl));
				swaggerFeature.setVersion(    projectInfo.get(Manifests.PROJECT_ATTRIBUTES.Version));
				swaggerFeature.setContact(    projectInfo.get(Manifests.PROJECT_ATTRIBUTES.Contact));
			} catch (IOException e) {
				LOG.info("Could not read the project attributes from the Manifest due to " + e.getMessage());
			}
    	}            
        
        return new HashSet<Object>(
                    Arrays.asList(
						_DocumentsApi,
						swaggerFeature,                                                
         					new LoggingFeature()
                )
        );
    }
}
