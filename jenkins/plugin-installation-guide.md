# Plugin Installation

## Step 1. Configure plugins w/ version

Edit [install-plugins.sh](./install-plugins.sh) and provide plugin names like so:

```
declare -A plugins=(
  ["email-ext"]="2.83"
  ["configuration-as-code"]="1647.ve39ca_b_829b_42"
  ["config-file-provider"]="951.v0461b_87b_721b_"
)
```

## Step 2. Run `install-plugins.sh`

```
govind@thinkpad:~/projects/devops$ ./jenkins/install-plugins.sh
Installing plugin: email-ext:2.83

Security warnings:
email-ext (2.83): SECURITY-2931 XSS vulnerability in bundled email templates https://www.jenkins.io/security/advisory/2023-02-15/#SECURITY-2931
email-ext (2.83): SECURITY-2934 Stored XSS vulnerability in custom email templates https://www.jenkins.io/security/advisory/2023-02-15/#SECURITY-2934
email-ext (2.83): SECURITY-2939 Script Security sandbox bypass vulnerability https://www.jenkins.io/security/advisory/2023-02-15/#SECURITY-2939
email-ext (2.83): SECURITY-3088-1 Missing permission check https://www.jenkins.io/security/advisory/2023-05-16/#SECURITY-3088%20(1)
email-ext (2.83): SECURITY-3088-2 CSRF vulnerability https://www.jenkins.io/security/advisory/2023-05-16/#SECURITY-3088%20(2)
Jul 11, 2023 11:22:44 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (org.apache.http.NoHttpResponseException) caught when processing request to {s}->https://updates.jenkins.io:443: The target server failed to respond
Jul 11, 2023 11:22:44 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {s}->https://updates.jenkins.io:443
Done
```
---

# Configure Email-Ext Plugin

## Step 1. Go to Global Configuration
    - In the Jenkins UI, click on "Manage Jenkins" in the left sidebar
    - Then select "Configure System" from the dropdown menu.
## Step 2. Configure Email Notification Settings like so:

Configure the SMTP server settings for your email provider. Enter the SMTP server address, port number, and any required authentication credentials.

1. Scroll down to the `System Admin e-mail address`
    - Enter `gthange@yahoo.com`
2. Scroll down to `Extended E-mail Notification` section.
    - Enter `SMTP Server` as `smtp.office365.com`
    - Enter `SMTP Port` as `587`
    - Open `Advanced` settings by hitting the pivot button.
    - Select `Credentials`. For this to work, create your email account credentials in `Global credentials` like so:
        1. Open the Jenkins dashboard in your web browser.
        2. Click on `Manage Jenkins` in the left-hand sidebar.
        3. Click on `Credentials` in `Security` section.
        4. Click on `Global credentials` or a credentials domain appropriate for your setup.
        5. Click on `Add Credentials` to create a new credential.
        6. Choose the appropriate credential type based on your needs. For storing a password, you can use "Username with password" or "Secret text".
            - "Username with password": Use this type if you want to store the username and password together. Enter a unique ID, specify the username and password, and provide an optional description.
                - Enter `Username` as `admin`
                - Enter `Password` as `changeme`
                - Enter `ID` as `ansible-admin-credentials`
            - "Secret text": Use this type if you only want to store the password without a username. Enter a unique ID, specify the password, and provide an optional description.
        7. Click `OK` to save the credential.
    - Check `Use TLS` checkbox.
    - Enter `Reply-To Address` as `gthange@yahoo.com`
3. Scroll down to `E-mail Notification` section.
    - Enter `SMTP Server` as `smtp.office365.com`
    - Open `Advanced` settings by hitting the pivot button.
    - Check `Use SMTP Authentication` checkbox.
    - Enter `User Name` as `gthange@yahoo.com`
    - Enter password.
    - Check `Use TLS` checkbox.
    - Enter `SMTP Port` as `587`
    - Enter `Reply-To Address` as `gthange@yahoo.com`

## Step 3. Test Email

1. Scroll down, and check `Test configuration by sending test e-mail` checkbox.
2. Enter `Test e-mail recipient` as `gthange@yahoo.com`
3. Hit `Test Configuration` button.
4. If everything is good, you will see `Email was successfully sent` message just below it.

## Step 4. Save & Apply

# Configure Config File Provider Plugin

1. Install the `Config File Provider` plugin in Jenkins.
    - __EITHER__ run [install-plugins.sh](./install-plugins.sh) file and restart jenkins
    - __OR__ go to "Manage Jenkins" > "Manage Plugins", switch to the "Available" tab, search for "Config File Provider" plugin, select it, and click "Install without restart".
2. After installing the plugin, go to "Manage Jenkins" > "Managed files".
3. Click on "Add a new Config".
4. Choose the "Custom file" type.
5. Enter a name for the file, such as "bsg-team-emails.txt".
6. In the "Content" section, enter the email addresses of the recipients you want to receive the alert emails, each email address on a new line. For example:
    ```
    email1@example.com
    email2@example.com
    ```
7. Hit submit.