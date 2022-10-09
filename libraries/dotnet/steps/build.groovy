/* groovylint-disable CatchException, LineLength */
void call() {
    // Specific Step Variables
    String stepName = 'DotNet Build'
    String masterBranch = config?.master_branch ?: 'main'

    String outDir = config?.source_build?.outDir ?: 'publish'
    podTemplate(containers: [
    containerTemplate(name: 'dotnet', image: 'mcr.microsoft.com/dotnet/sdk:6.0', ttyEnabled: true, command: 'cat'),
  ]) {
        node(POD_LABEL) {
            stage(stepName) {
                try {
                    if (env.BRANCH_NAME == "${masterBranch}" && env.TAG_NAME == null) {
                        // environment {
                        //     JENKINS_URL = "${env.JENKINS_URL}"
                        // // GitVersion_SemVer = ""
                        // // GitVersion_BranchName = ""
                        // // GitVersion_AssemblySemVer = ""
                        // // GitVersion_MajorMinorPatch = ""
                        // // GitVersion_Sha = ""
                        // }
                        script {
                            echo 'IN the dotnet build'
                                // sshagent(["${jenkinsSshKey}"]) {
                                //      sh(script: """#!/bin/bash
                                //      set -e +o pipefail
                                //      export GIT_SSH_COMMAND="ssh -oStrictHostKeyChecking=no"
                                //      #git fetch --unshallow
                                //      #git config --unset-all remote.origin.fetch
                                //      #git config --add remote.origin.fetch +refs/heads/master:refs/remotes/origin/master
                                //      #git config --add remote.origin.fetch +refs/heads/master:refs/remotes/origin/feature/*

                            //      """)
                            // }
                            sh(script:'''#!/bin/bash
                                set -e +o pipefail
                                #dotnet gitversion /output buildserver
                                #dotnet gitversion /showvariable FullSemVer
                                echo \"IN some method\"
                                ''')

                        //def props = readProperties file: 'gitversion.properties'
                        //env.GitVersion_SemVer = props.GitVersion_SemVer
                        //  echo "${env.GitVersion_SemVer}"
                        //Versioning in dotnet by just numbers, no pre/suffix
                        //gitversion /showvariable FullSemVer
                        //env.versionTag = sh(returnStdout:true, script: "echo \"${props.GitVersion_SemVer}\" | cut -d '-' -f2").trim()
                        // env.GitVersion_BranchName = props.GitVersion_BranchName
                        // env.GitVersion_AssemblySemVer = props.GitVersion_AssemblySemVer
                        // env.versionPatch = props.GitVersion_AssemblySemFileVer
                        // env.GitVersion_MajorMinorPatch = props.GitVersion_MajorMinorPatch
                        // env.GitVersion_Sha = props.GitVersion_Sha
                        // env.GIT_COMMIT = props.GitVersion_Sha
                        // env.elbVersionLabel = sh(returnStdout: true, script: "echo ${tagPrefix}${env.versionTag}").trim()
                        // env.elbS3Key = sh(returnStdout: true, script: "echo ${appName}/${env.elbVersionLabel}.zip").trim()
                        // env.s3url = sh(returnStdout: true, script: "echo s3://${elbS3Bucket}/${env.elbS3Key}").trim()
                        //echo "${env.elbVersionLabel} and ${env.versionTag} and s3url ${env.s3url} and ${env.GIT_COMMIT}"
                        }
                        sh(script: """#!/bin/bash
                                set -e +o pipefail
                                _tmp=\$(pwd)
                                #cd ${dotnetDir}${projectPath}
                                #rm -rf ${outDir}
                                dotnet publish "threes.csproj" -c debug -o ${outDir} -p:Version="6.1.20}"
                                cd ${outDir}
                                zip "../${env.elbVersionLabel}.zip" -r * .[^.]*
                                cd ..
                                if [[ \$(aws s3 ls ${env.s3url} | wc -l) -eq 0 ]]; then
                                    echo "Uploading to ${env.s3url}"
                                    aws s3 cp ${env.elbVersionLabel}.zip ${env.s3url} --metadata commit-id="${env.GIT_COMMIT}"
                                fi
                            """, label: 'DotNet Publish Script')
                        sshagent(["${jenkinsSshKey}"]) {
                            sh(script: """#!/bin/bash
                                    set -e +o pipefail
                                    _email=\$(git log -1 --format='%ae' ${env.GIT_COMMIT})
                                    _username=\$(git log -1 --format='%an' ${env.GIT_COMMIT})
                                    echo "\$_email \$_username"
                                    #git remote set-head origin ${masterBranch}
                                    #git config user.email \$_email
                                    #git config user.name \$_username
                                    #export GIT_SSH_COMMAND="ssh -oStrictHostKeyChecking=no"
                                    #git fetch --tags
                                    #git tag -a ${env.elbVersionLabel} -m '${gitTagMessage}'
                                    #git push origin ${env.elbVersionLabel}
                                    #echo "${env.appEnviron} ${env.elbVersionLabel} ${env.GIT_COMMIT}"
                                """)
                        }
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
  }
}

