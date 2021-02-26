# SME Onboarding Journey for Android

## Project setup
After cloning the app for the first time, you will not be able to build it.
This is due to the fact that you have to setup your Gradle credentials.

### Set up Gradle credentials
1. Navigate to ~/.gradle/, note the ~ symbol.
2. If it does not already exist, create a file called gradle.properties (touch gradle.properties on the command line)
3. Log in to Repo/Artifactory and click on your name in the top right.
4. Type in your password to get access to your encrypted password. Copy your encrypted password.
5. Add the following lines to the gradle.properties file:

```
repoUrl=https://repo.backbase.com/android
repoFlowUrl=https://repo.backbase.com/android-flow-production
artifactsRepoUrl=https://artifacts.backbase.com/android 
artifactsRetailRepoUrl=https://artifacts.backbase.com/android-retail
artifactsDesignRepoUrl=https://artifacts.backbase.com/design-android
artifactsFlowRepoUrl=https://artifacts.backbase.com/android-flow-development

mvnUser=<your username>
mvnPass=<your encrypted repo.backbase.com password>
mvnArtifactsPass=<your encrypted artifacts.backbase.com password>
```
You may need to restart Android Studio for it to recognize the new gradle.properties file
