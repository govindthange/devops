// Approval/rejection flow by inspecting build logs
pipeline {
    agent any

    stages {
        stage('Task1: Build') {
            steps {
                // Execute the build task here
                // ...
                sh 'echo "Task1: Build executed"'
            }
        }

        stage('Task2: Email') {
            steps {
                script {
                    // Send email with approve and reject links
                    def emailSubject = "${currentBuild.fullProjectName} Build Approval Required | ${currentBuild.fullDisplayName}"
                    def emailBody = """
                        <h3>Approval Required</h3>
                        <p>Please review and take action.</p>
                        <p>Click the links below to approve or reject:</p>
                        <ul>
                            <li><a href="${env.BUILD_URL}approve">Approve</a></li>
                            <li><a href="${env.BUILD_URL}reject">Reject</a></li>
                        </ul>
                    """

                    emailext body: emailBody,
                    mimeType: 'text/html',
                    subject: emailSubject,
                    to: 'govindt@winsoftech.com'
                }
            }
        }

        stage('Task3: Approve') {
            when {
                beforeAgent true
                expression {
                    // Check if the approval link was clicked by inspecting the build log for the presence of the approve link.
                    def approvalLink = env.BUILD_URL + 'approve'
                    return currentBuild.rawBuild.getLog(1000).contains(approvalLink)
                }
            }
            steps {
                // Execute the approve task here
                // ...
                sh 'echo "Task3: Approve executed"'
            }
        }

        stage('Task3: Reject') {
            when {
                beforeAgent true
                expression {
                    // Check if the rejection link was clicked by inspecting the build log for the presence of the reject link.
                    def rejectionLink = env.BUILD_URL + 'reject'
                    return currentBuild.rawBuild.getLog(1000).contains(rejectionLink)
                }
            }
            steps {
                // Execute the reject task here
                // ...
                sh 'echo "Task3: Reject executed"'
            }
        }
    }
}
