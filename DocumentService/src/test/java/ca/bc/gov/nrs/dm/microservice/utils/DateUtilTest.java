package ca.bc.gov.nrs.dm.microservice.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilTest {

	@Test
	public void testValidDate() {
		boolean result = DateUtil.isValidDateFormat("2018-01-01T10:01:01.000Z");
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testInvalidDate() {
		boolean result = DateUtil.isValidDateFormat("2018-01-01T10:01:01");
		
		Assert.assertFalse(result);
	}
}
