pipeline {
    agent any

    environment {
        SSH_KEY = credentials('525fe1d2-630f-48f6-8c9c-d0af6c0f47ff')
        SERVER_IP = '103.216.116.98:24700'
        DEPLOY_PATH = '/opt/deploy/myapp'
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'git@github.com:DuyBeatrix/Book_Rating_BE.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

         stage('Deploy to Server') {
            steps {
                script {
                    sh """
                    ssh -i ~/.ssh/id_rsa jenkins@${SERVER_IP} "mkdir -p ${DEPLOY_PATH}"
                    scp -i ~/.ssh/id_rsa target/*.jar jenkins@${SERVER_IP}:${DEPLOY_PATH}
                    ssh -i ~/.ssh/id_rsa jenkins@${SERVER_IP} "pkill -f 'java -jar' || true"
                    ssh -i ~/.ssh/id_rsa jenkins@${SERVER_IP} "nohup java -jar ${DEPLOY_PATH}/*.jar > ${DEPLOY_PATH}/app.log 2>&1 &"
                    """
                }
            }
         }
    }
}