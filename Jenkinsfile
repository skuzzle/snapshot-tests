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
    BUILD_CACHE = credentials('build_cache')
    ORG_GRADLE_PROJECT_sonatype = credentials('SONATYPE_NEXUS')
    ORG_GRADLE_PROJECT_signingPassword = credentials('gpg_password')
    ORG_GRADLE_PROJECT_base64EncodedAsciiArmoredSigningKey  = credentials('gpg_private_key')
  }
  stages {
    stage('Build') {
      steps {
        withGradle {
          sh './gradlew build'
        }
      }
    }
    stage('Report Coverage') {
      steps {
        withGradle {
          sh './gradlew coveralls'
        }
      }
    }
    stage('Test against JDK 17') {
      steps {
        withGradle {
          sh './gradlew testAgainstJava17'
        }
      }
    }
    stage('javadoc') {
      steps {
        withGradle {
          sh './gradlew javadoc'
        }
      }
    }
    stage('asciidoc') {
      steps {
        withGradle {
        // Note: 'deploy' here doesn't actually deploy anything
          sh './gradlew deployDocsToRepositoryRoot'
        }
      }
    }
    stage('readme') {
      steps {
        withGradle {
          sh './gradlew generateReadmeAndReleaseNotes'
        }
      }
    }
    stage('Deploy SNAPSHOT') {
      when {
        expression {
            return env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == '2.0-dev';
        }
      }
      steps {
        withGradle {
          sh './gradlew sign publishToSonatype'
        }
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
