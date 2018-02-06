import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
		{
			ca.bc.gov.nrs.dm.microservice.api.impl.DocumentsApiServiceImplTest.class,
			ca.bc.gov.nrs.dm.microservice.utils.ServiceUtilTest.class
		})
public class ServicesTestSuite {

}
