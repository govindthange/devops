pipeline {
  agent any

  stages {
    stage('Task1: Prepare build') {
      steps {
        // Perform build tasks here
        echo "Task1: Prepare build"
        // Add any build steps as needed
      }
    }
    stage('Task2: Send approval request') {
      steps {
        script {
          echo "Task2: Send approval request"

          // Send email with approve and reject links
          def emailBody = """
              <p>Dear Approver,</p>
              <p>Please review ${currentBuild.fullProjectName} <b style="font-family:'Courier New'"><a href="${env.BUILD_URL}/console">build ${currentBuild.displayName}</a></b> and approve or reject it.</p>
              <p>Click on the links below:</p>
              <ul>
                  <li><a href="${env.BUILD_URL}input">Approve</a></li>
                  <li><a href="${env.BUILD_URL}input">Reject</a></li>
              </ul>
              <p>Thanks,</p>
              <p>DevOps Team</p>
              <pstyle="color:blue">PS: Do not reply to this email</p>
          """
          emailext body: emailBody,
                    subject: "Approval request for ${currentBuild.fullProjectName} build ${currentBuild.displayName}",
                    to: 'govindt@winsoftech.com',
                    mimeType: 'text/html'
        }
      }
    }
    stage('Task3: Await build approval') {
      steps {
        script {
          echo "Task3: Await build approval"

          def userInput = input(
              id: 'rejectionInput',
              message: 'Please provide your decision and reason for rejection in case of rejection',
              parameters: [
                  choice(choices: 'Approve\nReject', description: 'Choose your decision', name: 'decision'),
                  string(description: 'Reason for rejection', name: 'rejectionReason')
              ]
          )
          def decision = userInput.decision
          def rejectionReason = userInput.rejectionReason

          if (decision == 'Reject') {
              // Build approval logic
              // Add any tasks for approval here
              echo "Build rejected by BSG with reason: ${rejectionReason}"

              // Send email with approve and reject links
              def emailBody = """
                  <p>Dear Team,</p>
                  <p>Note that ${currentBuild.fullProjectName} <b style="font-family:'Courier New'"><a href="${env.BUILD_URL}/console">build ${currentBuild.displayName}</a></b> was rejected with following reason.</p>
                  <p style="color:red;font-family:'Courier New';padding-left:2em;">
                      ${rejectionReason}
                  </p>
                  <p>Please resolve all the raised concerns and resubmit the build.</p>
                  <p>Thanks,</p>
                  <p>DevOps Team</p>
                  <pstyle="color:blue">PS: Do not reply to this email</p>
              """

              // Read alert email recipients from global configuration file. 
              configFileProvider(
                  [configFile(fileId: '97a84c04-2658-43b8-864c-5296267ca971', variable: 'BSG_EMAIL_FILE_CONTENT')]) {
                  def emailFileText = readFile(BSG_EMAIL_FILE_CONTENT)
                  def emailList = emailFileText.trim().split('\n').join(',')
                  echo "bsg-team-emails.txt contains: ${emailList}"

                  // Send alert email to configured list of emails
                  emailext body: emailBody,
                        subject: "BSG rejected ${currentBuild.fullProjectName} build ${currentBuild.displayName}",
                        to: emailList,
                        mimeType: 'text/html'
              }
          } else {
              // Build approval logic
              // Add any tasks for approval here
              echo "Build approved approved by BSG"
          }
        }
      }
    }
  }
}
