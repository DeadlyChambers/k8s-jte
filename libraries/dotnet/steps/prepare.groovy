/* groovylint-disable CatchException, LineLength */
void call() {
    // Specific Step Variables
    String stepName = 'DotNet Prepare'
    String appName = config?.appName ?: 'threes'
    String dateFormat = config?.dateFormat ?: "'%Y-%m-%d %H:%M'"
      container('default') {
    stage(stepName) {
        try {
            script {
                checkout scm
                bitbucketStatusNotify(buildState: 'INPROGRESS')
                echo "${stepName}"
                if (env.runStart == null) {
                    env.runStart = sh(returnStdout:true, script: "date +${dateFormat}").trim()
                }
                env.appEnviron = 'QA'
                if (env.TAG_NAME) {
                    echo "Prepare for TAG:${env.TAG_NAME}"
                //Manual process for deploying
                //env.appEnviron = input(id: 'EnvChoice', message: 'Deploy to which environment?',
                // parameters: [choice(choices: ['QA', 'Staging', 'Live'],
                // description: 'Environment to deploy?', name: 'configChoice')])
                // env.GIT_CURRENT_CMT_URL = "Commit : https://${gitUrl}/${companyName}
                // /${repoName}/commits/tag/${env.TAG_NAME}\n"
                }
                else {
                    echo 'Doing nothing in else block'
                // def gitBranch = env.BRANCH_NAME.startsWith("PR-")
                //     ? env.CHANGE_BRANCH
                //     : env.BRANCH_NAME
                //git(url: "${gitUrl}:${companyName}/${repoName}.git",
                //credentialsId: "${jenkinsSshKey}", branch: "${gitBranch}")
                //env.GIT_CURRENT_CMT_URL = "Commit :
                //https://${gitUrl}/${companyName}/${repoName}/commits/${env.GIT_COMMIT}\n"
                }
                env.buildDesc = sh(returnStdout: true, script: "echo \"${appName}\n Branch : ${env.BRANCH_NAME} ${env.BUILD_DISPLAY_NAME}\n Build : ${env.BUILD_URL}\n ${env.GIT_CURRENT_CMT_URL}\"").trim()
            }
            sh(script:"""#!/bin/bash
                set -e +o pipefail
                # export DOTNET_ROOT="/usr/local/dotnet"
                # export TOOLS="/home/ubuntu/.tools"
                # export PATH="\$DOTNET_ROOT:\$DOTNET_ROOT/tools:\$TOOLS:\$PATH"
                # cp -r \$TOOLS/.config ./
                # cp -r \$TOOLS/* ./
                """)
        }
        catch (Exception any) {
            bitbucketStatusNotify(buildState: 'FAILED')
            //   slackSend color: "danger", channel: "${slackChannel}",
            //   message: "Failed at Stage : ${stepName}\n ${env.buildDesc}",
            //   timestamp: "${env.runStart}"
            //   buildDescription(any.getMessage())
            throw any
        }
    }}}

