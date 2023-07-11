pipeline {
    agent any
    
    stages {
        stage('Task 1: Build') {
            steps {
                // Build your application here
                echo 'Building...'
            }
        }
        
        stage('Task 2: Email') {
            steps {
                script {
                    // Send email with approval and rejection links
                    def emailBody = """
                        <html>
                        <body>
                            <p>Please review the build and provide your feedback:</p>
                            <ul>
                                <li><a href="${env.BUILD_URL}input/approve">Approve</a></li>
                                <li><a href="${env.BUILD_URL}input/reject">Reject</a></li>
                            </ul>
                        </body>
                        </html>
                    """
                    
                    emailext body: emailBody,
                    to: 'govindt@winsoftech.com',
                    // recipientProviders: [developers()],
                    subject: "${currentBuild.fullProjectName} Build Approval Required | ${currentBuild.fullDisplayName}",
                    mimeType: 'text/html'
                }
            }
        }
        
        stage('Task 3: Approve/Reject') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    input message: 'Please provide your feedback',
                    parameters: [
                        choice(choices: 'Approve\nReject',
                          description: 'Choose your feedback',
                          name: 'feedback')
                      ]
                }
                
                script {
                    if (params.feedback == 'Approve') {
                        echo 'Approved. Proceeding with the next step...'
                        // Execute your approve logic here
                    } else {
                        echo 'Rejected. Aborting the pipeline...'
                        // Execute your reject logic here
                        error('Build rejected.')
                    }
                }
            }
        }
    }
}
