#!/usr/bin/env groovy
void call() {
    String name = "backend"
    String buildFolder = "backend"
    String baseImage = "node"
    String baseTag = "lts-buster"
    String demoRegistry = "218220206490.dkr.ecr.us-east-1.amazonaws.com"
    String awsRegion = "us-east-1"
    String ecrRegistryUrl = "https://218220206490.dkr.ecr.us-east-1.amazonaws.com"
    String awsCredential = 'aws-credentials'

//========================================================================
//========================================================================

//========================================================================
//========================================================================

    stage('Prepare Package') {
        script {
            writeFile file: '.ci/Dockerfile', text: libraryResource('node/Dockerfile')
        }
    }

    stage('SonarQube analysis') {
        echo "Run SonarQube Analysis"
    }

    stage("Build Solution") {
        echo "Build Solution"
        docker.build("ecr-khoahd7621-devops-${name}:${BUILD_NUMBER}", " -f ./.ci/Dockerfile \
        --build-arg BASEIMG=${baseImage} --build-arg IMG_VERSION=${baseTag} ${WORKSPACE}/src/${buildFolder}") 
    }

    stage("Push Docker Images") {
        echo "Push Docker Images"
        docker.withRegistry(ecrRegistryUrl, "ecr:${awsRegion}:${awsCredential}") {
            sh "docker tag ecr-khoahd7621-devops-${name}:${BUILD_NUMBER} ${demoRegistry}/ecr-khoahd7621-devops-${name}:${BUILD_NUMBER}"
            sh "docker push ${demoRegistry}/ecr-khoahd7621-devops-${name}:${BUILD_NUMBER}"
            sh "docker tag ${demoRegistry}/ecr-khoahd7621-devops-${name}:${BUILD_NUMBER} ${demoRegistry}/ecr-khoahd7621-devops-${name}:latest"
            sh "docker push ${demoRegistry}/ecr-khoahd7621-devops-${name}:latest"
        }
    }
}

//========================================================================
// node CI
// Version: v1.0
// Updated:
//========================================================================
//========================================================================
// Notes:
//
//
//========================================================================