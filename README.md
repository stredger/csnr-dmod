# CSNR-DMOD #

-------------

# Introduction #
The CSNR-DMOD project explores integration of the NRS Document Service with OpenShift.

The prototype consists of a micro-service and an example client using the MEAN stack.

# Microservice #

The Document Microservice acts as a bridge between OpenShift and the NRS Document Service

# Example Client #

The example client was based off of work from the EPIC project.  

Development Environment
-------------------------
Install the following:
- Visual Studio 2017 Preview with Node.js extensions
- Ruby
- sass (a Ruby package)
	- Execute `gem install sass` to install

Run Locally
----------
`git clone https://github.com/bcgov/csnr-dmod.git`
`cd cnsr-dmod`
`npm install`
`grunt build && node server.js`


OpenShift Configuration
-----------------------

- process and run the build templates (Review the template and add parameter overrides as necessary)
- `oc project csnr-dmod-tools`
- `oc process -f dmod-build-template.json | oc create -f -`
- `oc project csnr-dmod-dev`
- `oc process -f dmod-deployment-template.json | oc create -f -`
- Allow the dev project to access the tools project
`oc policy add-role-to-user system:image-puller system:serviceaccount:csnr-dmod-dev:default -n csnr-dmod-tools`




