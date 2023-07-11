FROM jenkins/jenkins

USER root
RUN echo 'root:changeme' | chpasswd

# Install dependencies
RUN apt-get update && \
    apt-get install -y net-tools && \
    apt-get install -y iputils-ping && \
    apt-get update && apt-get install -y openssh-client sshpass

# Download the Jenkins plugin CLI
RUN curl -SL https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/2.12.13/jenkins-plugin-manager-2.12.13.jar -o /usr/share/jenkins/ref/jenkins-plugin-cli.jar

# Set the file permissions
RUN chmod 755 /usr/share/jenkins/ref/jenkins-plugin-cli.jar

# Expose the plugin CLI JAR file
VOLUME /usr/share/jenkins/ref/

# Start Jenkins
# ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/jenkins.sh"]