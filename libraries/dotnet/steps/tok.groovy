/* groovylint-disable UnnecessaryGString */
void call() {
    container('jnlp') {
        stage('Tok') {
            echo 'Running Step Tok'
            sh(script:
            """#!/bin/bash
               set -e +o pipefail
               docker info""")
        }
    }
}
