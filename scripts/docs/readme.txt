Application:          DMOD (Document Management Onboarding Demo)
Repository:           https://github.com/bcgov/csnr-dmod.git
Version:              1.0.0
Author:               George Walker 

Requirements
-------------------------------------------------------------------------------
WebADE Instances:     CSFDLV, CSFTST, CSFPRD


Description
-------------------------------------------------------------------------------
First deployment of Document Management Onboarding Demo accounts.


Prerequisites
-------------------------------------------------------------------------------
 - Dependency on WebADE.


Changelog
-------------------------------------------------------------------------------
June 2017    Initial Delivery

    
-------------------------------------------------------------------------------
1. WebADE Scripts
-------------------------------------------------------------------------------

1.1 Change to the scripts directory. (One folder up of this readme)

    cd ../

1.2 Connect to the target webade database.

    sqlplus webade/<password>@<instance>
    
    Where <instance> is one of the "WebAde Instances" defined above.
    
1.3 Create the webade entries for Document Management
    
    start dmod.1.0.0.webade.dml.sql
    commit;
	
1.4 Exit sqlplus

    exit
  
-------------------------------------------------------------------------------
2. NOTIFICATION
-------------------------------------------------------------------------------
Notify application deliveries.    