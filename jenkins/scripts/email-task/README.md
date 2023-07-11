To test the pipeline script in Jenkins, you can create a new Jenkins job and configure it to use the pipeline script. Here's a step-by-step guide:

## Step 1. Access the Jenkins UI

1. Open a web browser.
2. navigate to the Jenkins UI.

## Step 2. Create a New Job

1. Click on "New Item" in the Jenkins dashboard.
2. Enter a name for the job (e.g., "PipelineTest") and select "Pipeline" as the job type.
3. Click "OK" to create the job.

## Step 3. Configure the Pipeline Script

1. In the job configuration, scroll down to the "Pipeline" section.
2. Select the "Pipeline script" option.
3. Copy and paste your pipeline script into the text area. Use `../scripts/email-task.groovy` file content.

4. Click "Save" to save the job configuration.

## Step 4. Build the Job

1. On the job's main page, click on "Build Now" to initiate a build of the pipeline.
2. Monitor the Build: After the build starts, you can monitor its progress by clicking on the build number under the "Build History" section of the job's main page.
3. Verify Email Notification: Once the build completes (either successfully or with a failure), check your email inbox. You should receive an email notification according to the configured email settings in the pipeline script.
