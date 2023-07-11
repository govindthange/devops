pipeline {
    agent any

    stages {
        stage('Run Ansible Playbook') {
            steps {
                script {
                    // SSH into the Ansible container and run ansible-playbook command
                    withCredentials([usernamePassword(credentialsId: 'ansible-admin-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh """
                            sshpass -p "${PASSWORD}" ssh "${USERNAME}@ansible" ansible-playbook -i /test/hosts /test/hello-world.yml
                        """
                    }
                }
            }
        }
    }
}