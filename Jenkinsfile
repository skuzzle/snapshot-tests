pipeline {
  agent {
    docker {
      image 'eclipse-temurin:11'
      args '-v /home/jenkins/.gradle:/var/gradle/.gradle -v /home/jenkins/.gnupg:/.gnupg -e GRADLE_OPTS=-Duser.home=/var/gradle'
    }
  }
  environment {
    COVERALLS_REPO_TOKEN = credentials('coveralls_repo_token_snapshot_tests')
    ORG_GRADLE_PROJECT_signingPassword = credentials('gpg_password')
    ORG_GRADLE_PROJECT_base64EncodedAsciiArmoredSigningKey  = credentials('gpg_private_key')
  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('Coverage') {
      steps {
        sh './gradlew jacocoRootReport coveralls'
      }
    }
    stage('javadoc') {
      steps {
        sh './gradlew javadoc'
      }
    }
    stage('Try Sign') {
      steps {
        sh './gradlew sign -s --info'
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
