node('maven') {
	stage ('Compile microservice')
	{
		openshiftBuild(buildConfig: 'document-microservice', showBuildLogs: 'true')
	}
	
	stage ('Compile front end')
	{
		openshiftBuild(buildConfig: 'dmod', showBuildLogs: 'true')
	}

    stage('checkout csnr-dmod src onto docker slave')
    {
	  git url: 'https://github.com/bcgov/csnr-dmod.git'
    } 
}