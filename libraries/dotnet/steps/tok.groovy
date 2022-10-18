/* groovylint-disable UnnecessaryGString */
void call() {
    container('no-priv') {
        stage('Tok') {
            echo 'Running Step Tok'
            checkout scm
            sh(script:
            """#!/bin/bash
               set -e +o pipefail
               ls -la
               podman build --target test --build-arg APP_VER=6.0.6 --build-arg D_CONFIG=Release . -t ThreeS.Api/6.0.6
               """)
        }
    }
}
