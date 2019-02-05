node('large') {

    stage('Git') {
        checkout scm
    }

    stage('Test') {
        try {
            wrap([$class: 'AnsiColorBuildWrapper', colorMapName: 'xterm']) {
                sh './sbt clean test'
            }
        } finally {
            junit 'target/test-reports/*.xml'
        }
    }


    if (env.BRANCH_NAME == 'welt') {

        stage('Publish') {
            withCredentials([[$class: 'StringBinding', credentialsId: 'BINTRAY_API_KEY_CI_WELTN24', variable: 'BINTRAY_PASS']]) {
                // provide BINTRAY_{USER,PASS} as of https://github.com/sbt/sbt-bintray/blob/master/notes/0.5.0.markdown
                env.BINTRAY_USER = "ci-weltn24"
                wrap([$class: 'AnsiColorBuildWrapper', colorMapName: 'xterm']) {
                    sh './sbt publish'
                }
                slackSend channel: 'section-tool-2', message: "Successfully published a new Metrics release: ${env.BUILD_URL}"
            }
        }

    }
}