#!groovy

def writeConfigFile(platformName, workspace) {
    echo "DEBUG: Creating config file"
    def data = """DriverName=${platformName}
    AndroidCapabilities      ={ 'deviceName':'emulator-5554', 'platformVersion':'14.0', 'appPackage':'com.saucelabs.mydemoapp.android', 'appActivity':'com.saucelabs.mydemoapp.android.view.activities.SplashActivity', 'noReset':'false'}
    iOSCapabilities          = { 'deviceName':'', 'platformVersion':'18.2', 'udid':'29EA159B-7E7F-4323-A1FD-6E2AB17E4CBD', 'bundleId':'com.saucelabs.mydemo.app.ios', 'noReset':'false'}
    IsJenkinsRun=true
    WaitTime=10"""
    writeFile(file: "${workspace}/config/config.properties", text: data)
    echo "DEBUG: Config file is created"
}

pipeline {
    agent any

    environment {
        REPORT_DIR = 'TestReport/Report_Folder'
    }

    parameters {
        choice(
            name: 'PLATFORM',
            choices: ["Android", "iOS"],
            description: 'Required * Select PLATFORM to run test on'
        )
        choice(
             name: 'SUITE',
             choices: ["SampleAndroidSuite", "SampleIOSSuite"],
             description: 'Required * Select SUITE to run test on'
        )

    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/bijitbiswas/AutomationProject-Appium-TestNG', branch: 'master'
            }
        }

        stage('Generate configuration file') {
            steps{
                script{
                    writeConfigFile(params.PLATFORM, env.WORKSPACE)
                }
            }
        }

        stage('Build & Run Tests') {
            steps {
                sh "mvn clean test -Dsurefire.suiteXmlFiles=MobileTestSuites/${params.SUITE}.xml"
            }
        }

        stage('Publish Extent HTML Report') {
            steps {
                script {
                    def reportPath = "${env.REPORT_DIR}/${params.SUITE}.html"
                    if (!fileExists(reportPath)) {
                        error "❌ Report not found at: ${reportPath}"
                    }
                    sh '''
                    echo "Report Directory:"
                    ls -la ${REPORT_DIR}

                    echo "HTML Files:"
                    find ${REPORT_DIR} -name "*.html"
                    '''
                }
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: "${env.REPORT_DIR}",
                    reportFiles: "${params.SUITE}.html",
                    reportName: 'Extent HTML Report'
                ])
            }
        }
    }

    post {
        always {
            echo 'DEBUG: Cleaning up workspace...'
            cleanWs()
        }

        success {
            echo 'DEBUG: ✅ Build and test execution completed successfully!'
        }

        failure {
            echo 'DEBUG: ❌ Build or tests failed. Check console output and reports.'
        }
    }
}