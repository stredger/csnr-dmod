package ca.bc.gov.nrs.dm.microservice.utils;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceUtilTest {
	
	@Mock
	private FileSystemXmlApplicationContext context;
	
	@Mock
	private HttpHeaders headers;
	
	
	@Test
	public void testNoHeader() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
		when(headers.getRequestHeader("Authorization")).thenReturn(null);
		
		DocumentManagementService service = serviceUtil.getServiceClient(headers);
		Assert.assertNotNull(service);
		
		DocumentManagementService defaultServiceClient = serviceUtil.getServiceClient();
		Assert.assertNotNull(defaultServiceClient);
		Assert.assertEquals(service, defaultServiceClient);
	}
	
	@Test
	public void testNoBearerAuthorization() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
		
		List<String> authorizationList = new ArrayList<String>();
		authorizationList.add("test");
		authorizationList.add("12345");
		
		when(headers.getRequestHeader("Authorization")).thenReturn(authorizationList);
		DocumentManagementService service = serviceUtil.getServiceClient(headers);
		Assert.assertNotNull(service);
		
		DocumentManagementService defaultServiceClient = serviceUtil.getServiceClient();
		Assert.assertNotNull(defaultServiceClient);
		Assert.assertEquals(service, defaultServiceClient);
	}
	
	@Test
	public void testBearerWithoutToken() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
		List<String> authorizationList = new ArrayList<String>();
		authorizationList.add("Bearer");
		authorizationList.add("34567");
		
		when(headers.getRequestHeader("Authorization")).thenReturn(authorizationList);
		DocumentManagementService service = serviceUtil.getServiceClient(headers);
		Assert.assertNotNull(service);
		
		DocumentManagementService defaultServiceClient = serviceUtil.getServiceClient();
		Assert.assertNotNull(defaultServiceClient);
		Assert.assertEquals(service, defaultServiceClient);
	}
	
	@Test
	public void testUserTokenAuthorization() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
		List<String> authorizationList = new ArrayList<String>();
		authorizationList.add("Bearer 12345");
		authorizationList.add("34567");
		
		when(headers.getRequestHeader("Authorization")).thenReturn(authorizationList);
		DocumentManagementService service = serviceUtil.getServiceClient(headers);
		Assert.assertNotNull(service);
		
		DocumentManagementService defaultServiceClient = serviceUtil.getServiceClient();
		Assert.assertNotNull(defaultServiceClient);
		Assert.assertNotEquals(service, defaultServiceClient);
		
	}
	
	@Test
	public void testUserTokenAuthorizationWithWhitespace() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
		List<String> authorizationList = new ArrayList<String>();
		authorizationList.add("   Bearer    12345       ");
		authorizationList.add("34567");
		
		when(headers.getRequestHeader("Authorization")).thenReturn(authorizationList);
		DocumentManagementService service = serviceUtil.getServiceClient(headers);
		Assert.assertNotNull(service);
		
		DocumentManagementService defaultServiceClient = serviceUtil.getServiceClient();
		Assert.assertNotNull(defaultServiceClient);
		Assert.assertNotEquals(service, defaultServiceClient);
	}
	
	
	@Test
	public void testGetScopes() {

		when(context.getBean("applicationAcronym")).thenReturn("TEST");
		
		ServiceUtil serviceUtil = new ServiceUtil(context);

		try {
			String scope = serviceUtil.getScopes();
			Assert.assertTrue(scope.contains("DMS.*"));
			Assert.assertTrue(scope.contains("TEST.*"));
		}catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(expected = Exception.class)
	public void testGetScopesNull()throws Exception {
		when(context.getBean("applicationAcronym")).thenReturn(null);

		ServiceUtil serviceUtil = new ServiceUtil(context);
		serviceUtil.getScopes();
	}
	
	@Test
	public void testGetRootDir() {

		when(context.getBean("applicationRootDir")).thenReturn("ROOT");
		when(context.getBean("applicationAcronym")).thenReturn("TEST");
		
		ServiceUtil serviceUtil = new ServiceUtil(context);

		try {
			String rootDir = serviceUtil.getRootDirectory();
			Assert.assertEquals(rootDir, "/ROOT/TEST");
		}catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(expected = Exception.class)
	public void testGetRootNull()throws Exception {
		when(context.getBean("applicationRootDir")).thenReturn(null);
		when(context.getBean("applicationAcronym")).thenReturn("TEST");
		
		ServiceUtil serviceUtil = new ServiceUtil(context);
		serviceUtil.getRootDirectory();
	}
	
	@Test(expected = Exception.class)
	public void testGetRootNullAcronym()throws Exception {
		when(context.getBean("applicationRootDir")).thenReturn("ROOT");
		when(context.getBean("applicationAcronym")).thenReturn(null);
		

		ServiceUtil serviceUtil = new ServiceUtil(context);
		serviceUtil.getRootDirectory();
	}
}
