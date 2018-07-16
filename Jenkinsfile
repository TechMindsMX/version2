pipeline {
  agent any

  environment {
    VERSION = "${UUID.randomUUID().toString().replace('-','')[0..6]}" 
  }

  stages {

    stage('Download Config for Test'){
      steps{
        dir("configFilesTest"){
          sh "mkdir -p /home/jenkins/.modulusuno"
          sh "rm -rf /home/jenkins/.modulusuno/*"
          sh "git clone -b jenkins-new --single-branch git@bitbucket.org:techmindsmx/config-modulusuno-v3.git ."
        }
        sh 'mv configFilesTest/* /home/jenkins/.modulusuno'
      }
    }

    stage('Run Migration App') {
      steps{
        dir("web"){
          sh './grailsw -Dgrails.env=test clean'
          sh './grailsw -Dgrails.env=test dbm-clear-checksums'
          sh './grailsw -Dgrails.env=test dbm-update'
        }
      }
    }

    stage('Testing App') {
      steps{
        dir("m1core"){
          sh './grailsw -Dgrails.env=test clean'
          sh './grailsw -Dgrails.env=test test-app'
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


    stage('Build App') {
      steps{
        dir("web") {
          echo 'Building app'
          sh './grailsw -Dgrails.env=test clean'
          sh './grailsw -Dgrails.env=test war'
        }
      }
    }

/*
    stage('Download Config'){
      steps{
        dir("configFiles"){
          sh "git clone -b ${env.BRANCH_NAME} --single-branch git@bitbucket.org:techmindsmx/sepomex.git ."
        }
      }
    }

    stage('Preparing build Image Docker'){
      steps{
        sh 'cp configFiles/application-PRODUCTION.yml .'
        dir("folderDocker"){
          sh "git clone git@github.com:makingdevs/Tomcat-Docker.git ."
        }
        sh 'mv folderDocker/* .'
        sh 'mv build/libs/sepomex-0.0.1-SNAPSHOT.war .'
        sh 'mv sepomex-0.0.1-SNAPSHOT.war ROOT.war'
      }
    }

    stage('Build image docker') {
      steps{
        script {
          docker.withTool('Docker') {
            docker.withRegistry('https://752822034914.dkr.ecr.us-east-1.amazonaws.com/sepomex', 'ecr:us-east-1:techminds-aws') {
              def customImage = docker.build("sepomex:${env.VERSION}", '--build-arg URL_WAR=ROOT.war --build-arg FILE_NAME_CONFIGURATION=application-PRODUCTION.yml --build-arg PATH_NAME_CONFIGURATION=/root/.sepomex/ .')
              customImage.push()
            }
          }
        }
      }
    }*/

    /*stage('Deploy Kube') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      environment {
        ENVIRONMENT = "${env.BRANCH_NAME == 'master' ? 'development' : env.BRANCH_NAME}"
      }
      steps{
        sh "ssh ec2-user@34.200.152.121 sh /home/ec2-user/deployApp.sh ${env.VERSION} ${env.ENVIRONMENT} sepomex"
      }
    }*/

  }

  post {
    always {
      junit "**/build/test-results/TEST*.xml"
      cleanWs()
    }
  }
}
