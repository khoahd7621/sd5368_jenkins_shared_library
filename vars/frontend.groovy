#!/usr/bin/env groovy
void call() {
    String name = "frontend"
    String buildFolder = "frontend"
    String demoImage = "node"
    String baseTag = "lts-buster"
    String repoRegistry = "218220206490.dkr.ecr.us-east-1.amazonaws.com"
    String awsRegion = "us-east-1"
    String ecrRegistryUrl = "https://218220206490.dkr.ecr.us-east-1.amazonaws.com"

//========================================================================
//========================================================================

//========================================================================
//========================================================================

    stage('Prepare Package') {
        script {
            writeFile file: '.ci/Dockerfile', text: libraryResource('node/Dockerfile')
        }
    }

    stage("Build Solution") {
        echo "Build Solution"
        docker.build("practical_devops_sd5368_${name}:${BUILD_NUMBER}", " -f ./.ci/Dockerfile \
        --build-arg BASEIMG=${demoImage} --build-arg IMG_VERSION=${baseTag} ${WORKSPACE}/src/${buildFolder}") 
    }

    stage("Push Docker Images") {
        echo "Push Docker Images"
        script {
            sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${repoRegistry}"
            sh "docker tag practical_devops_sd5368_${name}:${BUILD_NUMBER} ${repoRegistry}/practical_devops_sd5368_${name}:${BUILD_NUMBER}"
            sh "docker push ${repoRegistry}/practical_devops_sd5368_${name}:${BUILD_NUMBER}"
            sh "docker tag ${repoRegistry}/practical_devops_sd5368_${name}:${BUILD_NUMBER} ${repoRegistry}/practical_devops_sd5368_${name}:latest"
            sh "docker push ${repoRegistry}/practical_devops_sd5368_${name}:latest"
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