pipeline {
  agent {
    docker {
      // Need an image with git installed that is why we stick with maven image for now though we're using gradle
      image 'maven:3.6-jdk-11'
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
    stage('Test against JDK 17') {
      steps {
        sh './gradlew testAgainstJava17'
      }
    }
    stage('javadoc') {
      steps {
        sh './gradlew javadoc'
      }
    }
    stage('asciidoc') {
      steps {
        // Note: 'deploy' here doesn't actually deploy anything
        sh './gradlew deployDocsToRepositoryRoot'
      }
    }
    stage('readme') {
      steps {
        sh './gradlew generateReadmeAndReleaseNotes'
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
        junit (testResults: '**/build/test-results/test/**.xml', allowEmptyResults: true)
    }
  }
}
