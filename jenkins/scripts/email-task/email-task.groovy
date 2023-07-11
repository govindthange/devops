pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Your build steps here
                // ...
                sh 'echo "Code to prepare build will come here"'
            }
        }
    }

    post {
        success {
            // Send email notification for success
            emailext (
                subject: "Build Successful: ${currentBuild.fullDisplayName}",
                body: "The build ${currentBuild.fullDisplayName} succeeded.",
                to: "govindt@winsoftech.com",
                replyTo: "noreply@example.com",
                attachLog: true,
                mimeType: 'text/html',
                recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                from: "govindt@winsoftech.com"
            )
        }

        failure {
            // Send email notification for failure
            emailext (
                subject: "Build Failed: ${currentBuild.fullDisplayName}",
                body: "The build ${currentBuild.fullDisplayName} failed.",
                to: "govindt@winsoftech.com",
                replyTo: "noreply@example.com",
                attachLog: true,
                mimeType: 'text/html',
                recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                from: "govindt@winsoftech.com"
            )
        }
    }
}
