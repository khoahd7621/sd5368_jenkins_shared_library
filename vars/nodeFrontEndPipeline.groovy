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
            stage ('Build Frontend') {
                when {
                    allOf {
                        // Condition Check
                        anyOf {
                            // Branch Event: Nornal Flow
                            anyOf {
                                branch 'main'
                                branch 'PR-*'
                            }
                            // Manual Run: Only if checked.
                            allOf {
                                changeset "**/frontend/**"
                            }
                        }
                    }
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
