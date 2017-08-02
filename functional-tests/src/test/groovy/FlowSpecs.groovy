import geb.spock.GebReportingSpec
import pages.app.HomePage
import spock.lang.Unroll

class FlowSpecs extends GebReportingSpec {

    def "Home"(){
		given: "At Home Page"
		to HomePage
		
		when: "Do nothing"
		
		then: "Still at Home Page"
		at HomePage
	}
}
