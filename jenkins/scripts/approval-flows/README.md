# Approach I

The pipeline has three stages: Task 1: Build, Task 2: Email, and Task 3: Approve/Reject.

In Task 2: Email, an email is sent using the emailext plugin. The email body includes HTML markup with links for approval and rejection. The links are dynamically generated using the BUILD_URL environment variable to provide the appropriate URL.

In Task 3: Approve/Reject, the pipeline pauses and waits for user input. The user is presented with a choice parameter to approve or reject the build. The timeout is set to 30 minutes, but you can adjust it as needed.

Depending on the user's choice, either the Approve or Reject branch is executed. You can include your specific logic within these branches.

# Approach II

Task1: Build: Represents the initial build task. You can replace the sh 'echo "Task1: Build executed"' line with your actual build commands.

Task2: Email: Sends an email notification containing the approval and rejection links. Update the to field with the appropriate email address.

Task3: Approve: Checks if the approval link was clicked by inspecting the build log for the presence of the approve link. If the link is found, the Task3: Approve stage is executed.

Task3: Reject: Checks if the rejection link was clicked by inspecting the build log for the presence of the reject link. If the link is found, the Task3: Reject stage is executed.