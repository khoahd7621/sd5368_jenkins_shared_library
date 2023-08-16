#!/usr/bin/env groovy
void call(Map pipelineParams) {

    pipeline {

        agent any

        options {
            disableConcurrentBuilds()
            disableResume()
            timeout(time: 5, unit: 'MINUTES')
        }
        
        stages {
            stage('Initial Build') {
                when {
                    expression {
                        return !fileExists("${env.WORKSPACE}/initial-build-done")
                    }
                }
                steps {
                    script {
                        // Build both backend and frontend here
                        backend()
                        frontend()

                        // Create a file to indicate initial build is done
                        writeFile file: "${env.WORKSPACE}/initial-build-done", text: ""
                    }
                }
            }

            stage('Build Backend') {
                when {
                    changeset "**/src/backend/**"
                }
                steps {
                    script {
                        backend()
                    }
                }
            }

            stage('Build Frontend') {
                when {
                    changeset "**/src/frontend/**"
                }
                steps {
                    script {
                        frontend()
                    }
                }
            }
        }

        post {
            cleanup {
                cleanWs()
            }
        }
    }
}
//========================================================================
// Demo CI
// Version: v1.0
// Updated:
//========================================================================
//========================================================================
// Notes:
//
//
//========================================================================
