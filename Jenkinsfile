node {
    wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {
        withEnv(["LC_ALL=en_US.UTF-8", "-Dfile.encoding=UTF-8"]) {
            release()
        }
    } 
}

def release() {
    def RELEASE_TO_ARTIFACTS = params.RELEASE_TO_ARTIFACTS
    def RELEASE_TO_REPO = params.RELEASE_TO_REPO
    def RELEASE_TO_APPCENTER = params.RELEASE_TO_APPCENTER
    def NOTIFY_TESTERS = params.NOTIFY_TESTERS
    def RELEASE_TYPE = params.RELEASE_TYPE
    def BRANCH = params.BRANCH
    def CLEAR_GRADLE_CACHE = params.CLEAR_GRADLE_CACHE
    def RELEASED = false

    try {
        stage("Checkout") {
            checkout([
                $class: "GitSCM", 
                branches: [[name: "*/${BRANCH}"]], 
                doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                extensions: scm.extensions, 
                userRemoteConfigs: scm.userRemoteConfigs
            ])
            sh "git checkout ${BRANCH}"
            sh "git reset --hard origin/${BRANCH}"
        }  

        stage('build') {
            def script = "clean assembleRelease"
            if(CLEAR_GRADLE_CACHE) {
                script += " --refresh-dependencies"
                echo "gradle dependency cache cleared"
            } 

            gradlew script
        } 
 
        stage('sign APK') {
            signAndroidApks (
                keyStoreId: "alpha-flow-release-keystore",
                keyAlias: "alphaflowkey",
                apksToSign: "**/*-unsigned.apk",
                skipZipalign: true
            )
        } 

        stage('upload APK to App Center') {
            if (RELEASE_TO_APPCENTER) {
                withCredentials([string(credentialsId: "appcenter", variable: "APPCENTER_TOKEN")]) {
                    script { gradlew "publishToAppCenter -PappcenterToken=${APPCENTER_TOKEN} -PreleaseType=${RELEASE_TYPE} -PshouldNotify=${NOTIFY_TESTERS}"}
                }
                RELEASED = true
            }
        }   

        stage("Tag") {
            if (RELEASED) {
                String appVersionName = getAppVersionName() 
                echo "git tag ${appVersionName}"
                try {
                    sh "git tag v${appVersionName}"
                    sh "git push --tags"
                }catch (Exception err) {
                    echo err.toString()
                } 
            }
        }

        currentBuild.result = 'SUCCESS'
    } catch(Exception err) {
        echo err.toString()
        currentBuild.result = 'FAILED'
    } 
}

def abort() {
    echo "Pipeline ABORTED"
    currentBuild.result = 'ABORTED'
}

def gradlew(String command) {
    sh "./gradlew $command"
}

String getAppVersionName() {
    return sh(
            returnStdout: true,
            script: /grep versionName .\/build.gradle | tr -d \'\'\', | awk '{print $3}'/
    ).trim()
}