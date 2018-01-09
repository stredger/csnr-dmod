package pages.app
import geb.Page

import extensions.AngularJSAware

class DemoPage extends Page implements AngularJSAware {
 
	static at = { angularReady && title == "CSNR DMOD - Development Environment" }
    static url = "p/d-1/docs" 
    static content = {
        HomeLink { $("a", "ui-sref":"projects",1) }
    }
}
