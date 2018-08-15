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

    stage('Install plugin') {
      steps{
        dir("workspace"){
          sh "git clone -b master --single-branch git@github.com:makingdevs/aws-sdk-grails3.git ."
          sh "./gradlew install"
        }
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
          sh './grailsw -Dgrails.env=test test-app'
        }
      }
    }

    stage('Update Assets') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        dir("web") {
          nodejs(nodeJSInstallationName: 'Node 10.1.0') {
            echo 'Updating bower'
            sh 'bower install'
          }
        }
      }
    }


    stage('Build App web') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        dir("web") {
          echo 'Building app'
          sh './grailsw -Dgrails.env=production war'
        }
      }
    }

    stage('Build App webservices') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        dir("webservices") {
          echo 'Building app'
          sh './grailsw -Dgrails.env=production war'
        }
      }
    }

    stage('Download Config'){
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        dir("configFiles"){
          sh "git clone -b ${env.BRANCH_NAME}-new --single-branch git@bitbucket.org:techmindsmx/config-modulusuno-v3.git ."
        }
      }
    }

    stage('Preparing build Image Docker'){
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        sh 'cp configFiles/application-api-production.groovy .'
        sh 'cp configFiles/application-production.groovy .'
        dir("folderDocker"){
          sh "git clone git@github.com:makingdevs/Tomcat-Docker.git ."
        }
        sh 'mv folderDocker/* .'
        sh 'mv web/build/libs/web-0.1.war .'
        sh 'mv webservices/build/libs/webservices-0.1.war .'
        sh 'mv web-0.1.war ROOT-WEB.war'
        sh 'mv webservices-0.1.war ROOT.war'
      }
    }

    stage('Build image docker web') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        script {
          docker.withTool('Docker') {
            docker.withRegistry('https://752822034914.dkr.ecr.us-east-1.amazonaws.com/web-modulusuno', 'ecr:us-east-1:techminds-aws') {
              def customImage = docker.build("web-modulusuno:${env.VERSION}", '--build-arg URL_WAR=ROOT-WEB.war --build-arg FILE_NAME_CONFIGURATION=application-production.groovy --build-arg PATH_NAME_CONFIGURATION=/root/.modulusuno/ .')
              customImage.push()
            }
          }
        }
      }
    }

    stage('Build image docker webservices') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        script {
          docker.withTool('Docker') {
            docker.withRegistry('https://752822034914.dkr.ecr.us-east-1.amazonaws.com/webservice-modulusuno', 'ecr:us-east-1:techminds-aws') {
              def customImage = docker.build("webservice-modulusuno:${env.VERSION}", '--build-arg URL_WAR=ROOT.war --build-arg FILE_NAME_CONFIGURATION=application-api-production.groovy --build-arg PATH_NAME_CONFIGURATION=/root/.modulusuno/ .')
              customImage.push()
            }
          }
        }
      }
    }

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
      cleanWs()
    }
  }
}
