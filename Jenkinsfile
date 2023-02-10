pipeline {
  agent {
    docker {
      image 'eclipse-temurin:11'
      args '-v /home/jenkins/.gradle:/var/gradle/.gradle -v /home/jenkins/.gnupg:/.gnupg -e GRADLE_OPTS=-Duser.home=/var/gradle'
    }
  }
  environment {
    COVERALLS_REPO_TOKEN = credentials('coveralls_repo_token_snapshot_tests')
    ORG_GRADLE_PROJECT_sonatype = credentials('SONATYPE_NEXUS')
    ORG_GRADLE_PROJECT_signingPassword = credentials('gpg_password')
    ORG_GRADLE_PROJECT_base64EncodedAsciiArmoredSigningKey  = credentials('gpg_private_key')
  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('Report Coverage') {
      steps {
        sh './gradlew coveralls'
      }
    }
    stage('javadoc') {
      steps {
        sh './gradlew javadoc'
      }
    }
    stage('Deploy SNAPSHOT') {
      when {
        branch 'dev'
      }
      steps {
          sh './gradlew sign publishToSonatype'
      }
    }
  }
  post {
    always {
        archiveArtifacts(artifacts: '*.md')
        junit (testResults: '**/build/test-results/*.xml', allowEmptyResults: true)
    }
  }
}
