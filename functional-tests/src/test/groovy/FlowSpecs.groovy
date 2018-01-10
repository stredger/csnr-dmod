import geb.spock.GebReportingSpec

import pages.app.HomePage
import pages.app.DemoPage

import spock.lang.Unroll
import spock.lang.Title
import spock.lang.Issue

@Title("Basic Link Checker to verify that the application is up and running.")
class FlowSpecs extends GebReportingSpec {

  @Unroll
  def "Navigate Page from: #StartPage, click Link: #ClickLink, Assert Page: #AssertPage"(){
    given: "I start on the #StartPage"
		  to StartPage
    when: "I click on the #ClickLink"
       page."$ClickLink".click()
    then: "I arrive on the #AssertPage page"
	     at AssertPage
    where:
    StartPage | ClickLink               || AssertPage 
    HomePage  | "HomeLink"              || HomePage
    DemoPage  | "HomeLink"              || HomePage
  }
}
