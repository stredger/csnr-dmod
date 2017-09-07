# CSNR-DMOD #

-------------

# Introduction #
The CSNR-DMOD project explores integration of the NRS Document Service with OpenShift.

The prototype consists of a micro-service and an example client using the MEAN stack.

# Microservice #

The Document Microservice acts as a bridge between OpenShift and the NRS Document Service


# Example Client #

The example client was based off of work from the EPIC project.  

Development Environment: Node.js
--------------------------------
The Node.js development is used to make changes to the Example Client.

Install the following:
- Visual Studio 2017 Preview with Node.js extensions
- Ruby
- sass (a Ruby package)
	- Execute `gem install sass` to install

Run Locally
----------
`git clone https://github.com/bcgov/csnr-dmod.git`
`cd cnsr-dmod`
`npm install -g kerberos -python=\Python27` 
`npm install -python=<Python Location>`  (Since the application uses gyp, you will need to specify a location where a version of Python > 2.5 and < 3.0 is installed; 2.7 is recommended)
`bower install --config.interactive=false --allow-root`
`grunt buildProd`
`node server.js`

Development Environment: Java
----------------------------

Install the following:
- Java JDK 1.7 or newer  (JDK 1.8 is known to work well.)
- Maven
- A Java IDE such as Netbeans or Eclipse

Create an archive of the following artifacts, which will be added to an OpenShift secret.  The files must be in the same directory structure as used for a Maven repository.  For example, for nrs-common-model 1.2.0.6, the directory would be:
    
ca\bc\gov\nrs\common\nrs-common-model\1.2.0.6\nrs-common-model-1.2.0.6.jar

An easy way to obtain this directory structure is to copy from your local Maven repository.

Complete set of artifacts required:

| Artifact | version |
| -------- | ------- |
| nrs-common-model | 1.2.0.6 |
| nrs-common-parent | 1.2.0.6 |
| nrs-common-rest-client | 1.2.0.6 |
| nrs-common-rest-common | 1.2.0.6 |
| nrs-common-service-api | 1.2.0.6 |
| nrs-common-service-api | 1.2.0.6 |
| nrs-common-util        | 1.2.0.6 |
| nrs-dm-model           | 1.2.0.4 |
| nrs-dm-parent          | 1.2.0.4 |
| nrs-dm-rest-client     | 1.2.0.4 |
| nrs-dm-rest-common     | 1.2.0.4 |
| nrs-dm-rest-common     | 1.2.0.4 |
| nrs-dm-service         | 1.2.0.4 |
| webade-oauth2-assembly-distribution | 1.2.0.4 |
| webade-oauth2-ear | 1.2.0.4 |
| webade-oauth2-assembly-database | 1.2.0.4 |
| webade-oauth2-common | 1.2.0.4 |
| webade-oauth2-integration-test | 1.2.0.4 |
| webade-oauth2-parent | 1.2.0.4 |
| webade-oauth2-persistence | 1.2.0.4 | 
| webade-oauth2-principal | 1.2.0.4 |
| webade-oauth2-principal | 1.2.0.4 | 
| webade-oauth2-rest-admin-client | 1.2.0.4 |
| webade-oauth2-rest-token-client | 1.2.0.4 |
| webade-oauth2-services | 1.2.0.4 |
| webade-oauth2-test-support | 1.2.0.4 |
| webade-oauth2-user-service | 1.2.0.4 | 
| oracle ridc | 11.1.1 | 

OpenShift Configuration
-----------------------

Secrets
-------
The Document Microservice has a dependency on certain libraries which are not stored in the Open Source repository.  These files are stored in the NRS Artifactory.

Two secrets will need to be created containing these files.

The first secret is for the Oracle RIDC library.  

The secret should contain a file called "oracle.zip" containing all of the files within the "oracle" folder in the local-maven-repo.  Be sure to follow the instructions above to provision this directory if it is empty.

The second secret contains the WebADE libraries, NRS common files and NRS Document Management libraries.  It will be a file called "ca.zip", containing all of the files within the "ca" folder in the local-maven-repo.

Once the .zip files are available, execute the following commands, as a user with admin access to the OpenShift project:

`oc project csnr-dmod-tools`
`oc secrets new oracle-libs oracle.zip`
`oc secrets new nrs-libs ca.zip`

If you are developing locally, simply store the contents of the two zip folders within the local-maven-repo directory.  Note that the jar files in that directory should not be checked into the Git repository.

Build Template
--------------
The Build template is used to provision the Tools project.  Be sure to review the contents of the template and substitute appropriate values for the template parameters when the template is processed.

If no changes to the default parameters are required, the template can be processed and used to create OpenShift objects with the following commands:

`oc project csnr-dmod-tools`
`oc process -f dmod-build-template.json | oc create -f -` 

Nexus
-----
Complex Java projects can be slow to execute.  Build times of more than 30 minutes are normal, and sometimes a build can take more than several hours.  To speed up the build, a local nexus can be used.

The following commands can be used to configure a local nexus:

`oc new-app sonatype/nexus`

`oc expose svc/nexus`

`oc volumes dc/nexus --add --name nexus-volume-1 --type persistentVolumeClaim --mount-path /sonatype-work/ --claim-name nexus-pv --claim-size 5G --overwrite`


You can then create a Node.js proxy repository, and put the URL for that proxy repository into the NPM_MIRROR environment variable in any Node.js applications.


Deployment Template
-------------------

- `oc project csnr-dmod-dev`
- Allow the dev project to access the tools project
`oc policy add-role-to-user system:image-puller system:serviceaccount:csnr-dmod-dev:default -n csnr-dmod-tools`
- Process and create the Deployment Template
- `oc process -f dmod-deployment-template.json | oc create -f -`


### Getting Help or Reporting an Issue
To report bugs/issues/features requests, please file an [issue](https://github.com/bcgov/csnr-dmod/issues/).

### How to Contribute
If you would like to contribute, please see our [contributing](contributing.md) guidelines.

Please note that this project is released with a [Contributor Code of Conduct](code_of_conduct.md). By participating in this project you agree to abide by its terms.

### Licence
	Copyright 2017 Province of British Columbia

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at 

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

This repository is maintained by (http://www2.gov.bc.ca/gov/content/governments/organizational-structure/ministries-organizations/ministries/forest-and-natural-resource-operations). 





