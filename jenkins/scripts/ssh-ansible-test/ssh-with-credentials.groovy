pipeline {
    agent any

    stages {
        stage('Run Ansible Playbook') {
            steps {
                script {
                    // SSH into the Ansible container and run ansible-playbook command
                    sh """
                        sshpass -p "changeme"  ssh admin@ansible ansible-playbook -i /test/hosts /test/hello-world.yml
                    """
                }
            }
        }
    }
}