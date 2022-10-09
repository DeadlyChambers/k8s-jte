/* groovylint-disable CatchException, LineLength */
void call() {
    // Specific Step Variables
    String  stepName = 'DotNet Unit Test'
    String masterBranch = config?.master_branch ?: 'main'
     podTemplate(containers: [
    containerTemplate(name: 'dotnet', image: 'mcr.microsoft.com/dotnet/sdk:6.0', ttyEnabled: true, command: 'cat'),
  ]) {
        node(POD_LABEL) {
    stage(stepName) {
        try {
            if (env.BRANCH_NAME != "${masterBranch}" && env.TAG_NAME == null) {
                echo "${stepName}"
                echo "Config:${env.appEnviron}"
                //dotnet test p:CollectCoverage=true /p:CoverletOutputFormat=cobertura
                //or --collect:"XPlat Code Coverage"
                def Boolean temp = true
                if (temp == true) {
                    sh(script:"""#!/bin/bash
                                set -e +o pipefail
                                _tmp=\$(pwd)
                                dotnet build

                                cd \$_tmp
                                """)
                    //dotcover vsTestCaseFilter: '*tests*'
                    //export CODACY_PROJECT_TOKEN=${codeacyProjectToken}
                    //./get.sh report -r ${dotnetDir}/${resultsDir}/*.xml
                    } else {
                    sh(script: """#!/bin/bash
                                set -e +o pipefail
                                _tmp=\$(pwd)
                                dotnet build
                                # dotnet dotcover test ${testSnapshotArgs}
                                #dotnet dotcover test ${testReportArgs}
                                #mv dotCover.Output* ${resultsDir}
                                cd \$_tmp
                                #export CODACY_PROJECT_TOKEN=${codeacyProjectToken}
                                #./get.sh report -r ${dotnetDir}/${resultsDir}/*.xml
                                """, label: 'Dotnet Test Coverage')
                //archiveArtifacts artifacts: "${dotnetDir}/${resultsDir}/**/*.*, ", onlyIfSuccessful: true
                }
                bitbucketStatusNotify(buildState: 'SUCCESSFUL')
            }
        }
        catch (Exception any) {
            bitbucketStatusNotify(buildState: 'FAILED')
            //   slackSend color: "danger", channel: "${slackChannel}", message: "Failed at Stage : ${stepName}\n ${env.buildDesc}", timestamp: "${env.runStart}"
            //   buildDescription(any.getMessage())
            throw any
        }
    }}}
}
