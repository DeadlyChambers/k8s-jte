/* groovylint-disable UnnecessaryGString */
void call() {
    container('no-priv') {
        stage('Tok') {
            echo 'Running Step Tok'
            sh(script:
            """#!/bin/bash
               set -e +o pipefail
               podman build --target test --build-arg APP_VER=6.0.6 --build-arg D_CONFIG=Release . -t ThreeS.Api/6.0.6 --progress=plain
               """)
        }
    }
}
