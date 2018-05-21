pipeline {
  agent any

  tools {
    gradle "Gradle 3.3"
  }

  environment {
    VERSION = "${UUID.randomUUID().toString().replace('-','')[0..6]}" 
  }

  stages {

    stage('Preparing the configuration') {
      steps{
        dir("config") {
          echo 'Setting repo of configuration TODO'
        }
      }
    }

    stage('Update Assets') {
      steps{
        dir("web") {
          nodejs(nodeJSInstallationName: 'Node 10.1.0') {
            echo 'Updating bower'
            sh 'bower install'
          }
        }
      }
    }

    stage('Test App') {
      steps{
        dir("m1core") {
          echo 'Testing app'
          sh './grailsw -Dgrails.env=test clean'
          sh './grailsw -Dgrails.env=test clean -unit'
        }
      }
    }

    stage('Build App') {
      when {
        expression {
          env.BRANCH_NAME in ["master", "stage", "production","feature/633"]
        }
      }
      steps{
        dir("web") {
          echo 'Building app'
          sh './grailsw -Dgrails.env=test clean'
          sh './grailsw -Dgrails.env=test war'
        }
      }
    }

    /*stage('Transfer Jar'){
      when {
        expression {
          env.BRANCH_NAME in ["master", "stage", "production"]
        }
      }
      steps{
        echo 'Transferring the jar'
        sh "scp ${env.WORKSPACE}/build/libs/app.jar centos@54.210.224.219:/home/centos/wars/emailer/stage/app.jar"
      }
    }

    stage('Deploy App'){
      when {
        expression {
          env.BRANCH_NAME in ["master", "stage", "production"]
        }
      }
      environment {
        ENVIRONMENT = "${env.BRANCH_NAME == 'master' ? 'development' : env.BRANCH_NAME}"
      }
      steps{
        echo 'Execute sh to build and deploy in Kubernetes'
        sh "ssh centos@54.210.224.219 sh /home/centos/deployEmailer.sh ${env.VERSION} ${env.ENVIRONMENT}"
      }
    }*/

  }

  post {
    always {
      cleanWs()
    }
  }
}
