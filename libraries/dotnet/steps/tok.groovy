/* groovylint-disable UnnecessaryGString */
void call() {
    container('no-priv') {
        stage('Tok') {
            echo 'Running Step Tok'
            sh(script:
            """#!/bin/bash
               set -e +o pipefail
               podman info""")
        }
    }
}
