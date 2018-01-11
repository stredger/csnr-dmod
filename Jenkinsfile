node('maven') {

    stage('checkout') {
       echo "checking out source"
       echo "Build: ${BUILD_ID}"
       checkout scm
    }

    stage('code quality check') {
            SONARQUBE_PWD = sh (
             script: 'oc env dc/sonarqube --list | awk  -F  "=" \'/SONARQUBE_ADMINPW/{print $2}\'',
             returnStdout: true
              ).trim()
           echo "SONARQUBE_PWD: ${SONARQUBE_PWD}"

           SONARQUBE_URL = sh (
               script: 'oc get routes -o wide --no-headers | awk \'/sonarqube/{ print match($0,/edge/) ?  "https://"$2 : "http://"$2 }\'',
               returnStdout: true
                  ).trim()
           echo "SONARQUBE_URL: ${SONARQUBE_URL}"

           dir('sonar-runner') {
            sh 'chmod +x ./gradlew'
            sh returnStdout: true, script: "./gradlew sonarqube -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.verbose=true --stacktrace --info  -Dsonar.sources=.."
        }
    }
}

node('master') {

    stage ('Build microservice')
    {
	openshiftBuild(buildConfig: 'document-microservice', showBuildLogs: 'true')
	openshiftTag destStream: 'document-microservice', verbose: 'true', destTag: '$BUILD_ID', srcStream: 'document-microservice', srcTag: 'latest'
    }

    stage ('Dev Deploy microservice')
    {
	openshiftTag destStream: 'document-microservice', verbose: 'true', destTag: 'dev', srcStream: 'document-microservice', srcTag: 'latest'
        openshiftVerifyDeployment depCfg: 'document-microservice', namespace: 'csnr-dmod-dev ', replicaCount: 1, verbose: 'false'
    }
	
    stage ('Build front end')
    {
	openshiftBuild(buildConfig: 'dmod', showBuildLogs: 'true')
	openshiftTag destStream: 'dmod', verbose: 'true', destTag: '$BUILD_ID', srcStream: 'dmod', srcTag: 'latest'
    }
    
    stage ('Dev Deploy front end')
    {
	openshiftTag destStream: 'dmod', verbose: 'true', destTag: 'dev', srcStream: 'dmod', srcTag: 'latest'
	openshiftVerifyDeployment depCfg: 'dmod', namespace: 'csnr-dmod-dev ', replicaCount: 1, verbose: 'false'
    }

   stage('validation') {
          dir('functional-tests'){
                // sh './gradlew --debug --stacktrace phantomJsTest'
		sh 'sleep 3s'
      }
   }

   stage('deploy-test') {
      input "Deploy to test?"
   }

   stage('deploy-prod') {
      input "Deploy to prod?"
   }

}


