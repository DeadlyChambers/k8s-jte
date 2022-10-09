/* groovylint-disable CatchException, LineLength */
void call() {
    // Specific Step Variables
    String stepName = ''
    String masterBranch = config?.master_branch ?: 'main'

    stage(stepName) {
        try {
            if (env.BRANCH_NAME == "${masterBranch}" || env.TAG_NAME) {
                echo 'Doing the deploy step'
                sh(script:"""
                        echo 'DELETE: output vars before deploy'
                        printenv
                        """)
                    // if (beanstalkApplication) {
                    //         sh(script: """#!/bin/bash
                    //             set -e +o pipefail
                    //             cd \$_tmp
                    //             echo "Creating AppVersion ${env.elbVersionLabel}"
                    //             _config=\${${env.appEnviron},,}
                    //             _desc="AppVersion:${env.BUILD_URL} ${env.GIT_CURRENT_CMT_URL}"
                    //             _elbAppName=\${${env.elbVersionLabel},,}
                    //             _elbEnv="\${_config}-\${_elbAppName}"
                    //             echo 'aws elasticbeanstalk create-application-version'
                    //             echo '--application-name "\${_elbAppName}" --version-label "${env.elbVersionLabel}" --description "\${_desc:0:200}" --source-bundle S3Bucket="${elbS3Bucket}",S3Key="${env.elbS3Key}"'
                    //             echo '--region "${awsRegion}" --tags Key="ds:created",Value="jenkins" Key="ds:app",Value="\${_elbAppName}" Key="ds:env",Value="\${_config}" --debug;'
                    //             echo "Create beanstalk app version, and deploy"
                    //             echo 'aws elasticbeanstalk update-environment --application-name "\${_elbAppName}" --environment-name "\${_elbEnv}" --version-label "${env.elbVersionLabel}" --debug;'
                    //             echo 'aws elasticbeanstalk wait environment-updated --application-name "\${_elbAppName}" --environment-names "\${_elbEnv}" --version-label "${env.elbVersionLabel}" --debug;'

                //         """, label: "ElasticBeanstalk Deploy")
                // }
                bitbucketStatusNotify(buildState: 'SUCCESSFUL')
                env.elbVersionLabel = '6.1.21'
                env.appEnviron = 'local'
                buildName "${env.appEnviron}:${env.elbVersionLabel}.${env.BUILD_ID}"
                buildDescription("App Label : ${env.elbVersionLabel}\nCommit : ${env.GIT_COMMIT}\nEnvironment : ${env.appEnviron}\n")
            //slackSend color: "good", channel: "${slackChannel}", message: "${env.buildDesc}\n S3Url : https://${elbS3Bucket}.s3.amazonaws.com/${appName}/${env.elbVersionLabel}.zip", timestamp: "${env.runStart}"
            }
        }
        catch (Exception any) {
            bitbucketStatusNotify(buildState: 'FAILED')
            //   slackSend color: "danger", channel: "${slackChannel}", message: "Failed at Stage : ${stepName}\n ${env.buildDesc}", timestamp: "${env.runStart}"
            //   buildDescription(any.getMessage())
            throw any
        }
    }
}
