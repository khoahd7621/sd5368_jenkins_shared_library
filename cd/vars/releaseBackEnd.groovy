#!/usr/bin/env groovy
void call() {
    String name = "backend"
    String buildFolder = "backend"
    String demoRegistry = "218220206490.dkr.ecr.us-east-1.amazonaws.com"
    String awsRegion = "us-east-1"
    String eksName = "practical-devops-sd5368-eks"
    String ecrRegistryUrl = "https://218220206490.dkr.ecr.us-east-1.amazonaws.com"

//========================================================================
//========================================================================

//========================================================================
//========================================================================
    stage ('Prepare Package') {
        script {
            writeFile file: '.ci/service/deployment.yml', text: libraryResource('deploy/eks/service/deployment.yml')
        }
    }

    stage ("Deploy BackEnd To K8S") {
        script {
            sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${demoRegistry}"
            sh "export registry=${demoRegistry}; export appname=${name}; export tag=latest; \
            envsubst < .ci/service/deployment.yml > deployment.yml"
            sh "aws eks --region ${awsRegion} update-kubeconfig --name ${eksName}"
            sh "kubectl apply -f deployment.yml"
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