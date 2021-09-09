pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-11'
      args '-v /home/jenkins/.m2:/var/maven/.m2 -v /home/jenkins/.gnupg:/.gnupg -e MAVEN_CONFIG=/var/maven/.m2 -e MAVEN_OPTS=-Duser.home=/var/maven'
    }
  }
  environment {
    COVERALLS_REPO_TOKEN = credentials('coveralls_repo_token_snapshot_tests')
    GPG_SECRET = credentials('gpg_password')
  }
  stages {
    stage ('Test Spring-Boot Compatibility') {
      steps {
        script {
            def versionsAsString = sh(script: 'mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=compatible-spring-boot-versions -q -DforceStdout', returnStdout:true).trim()
            def versionsArray = versionsAsString.split(',')
            versionsArray.each {
                stage("Verify against Spring-Boot ${it}") {
                    sh "mvn -B clean verify -Dversion.spring-boot=${it.trim()}"
                }
            }
        }
      }
    }
    stage('Build Final') {
      steps {
        sh 'mvn -B clean verify'
      }
    }
    stage('Coverage') {
      steps {
        sh 'mvn -B jacoco:report jacoco:report-integration coveralls:report -DrepoToken=$COVERALLS_REPO_TOKEN'
      }
    }
    stage('javadoc') {
      steps {
        sh 'mvn -B javadoc:javadoc'
      }
    }
    stage('Deploy SNAPSHOT') {
      when {
        branch 'dev'
      }
      steps {
        sh 'mvn -B -Prelease -DskipTests -Dgpg.passphrase=${GPG_SECRET} deploy'
      }
    }
  }
  post {
    always {
        archiveArtifacts(artifacts: '*.md')
        junit (testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
    }
  }
}
