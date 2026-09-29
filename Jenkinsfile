pipeline {
    agent {
        docker {
            image 'gradle:jdk21'
            args '-v /home/jenkins/caches/snapshot-tests/.gradle:/tmp/gradle-user-home:rw -v /home/jenkins/.gnupg:/.gnupg:ro'
        }
    }
    environment {
        CI = 'true'
        COVERALLS_REPO_TOKEN = credentials('coveralls_repo_token_snapshot_tests')
        GRADLE_CACHE = '/tmp/gradle-user-home'
        GRADLE_USER_HOME = '/tmp/gradle-home'
        ORG_GRADLE_PROJECT_sonatype = credentials('SONATYPE_NEXUS')
        ORG_GRADLE_PROJECT_signingPassword = credentials('gpg_password')
        ORG_GRADLE_PROJECT_base64EncodedAsciiArmoredSigningKey = credentials('gpg_private_key')
    }
    stages {
        stage('Load Gradle Cache from host') {
            steps {
                sh './.jenkins/load-gradle-cache.sh'
            }
        }
        stage('Quickcheck') {
            steps {
                withGradle {
                    sh './gradlew quickCheck'
                }
            }
        }
        stage('build-logic tests') {
            steps {
                withGradle {
                    sh './gradlew build-logic:check'
                }
            }
        }
        stage('Unit-tests') {
            steps {
                withGradle {
                    sh './gradlew test coveralls'
                }
            }
        }
        stage('asciidoc') {
            steps {
                withGradle {
                    sh './gradlew asciidoctor'
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
    }
    post {
        success {
            sh './.jenkins/store-gradle-cache.sh'
        }
        always {
            archiveArtifacts(artifacts: '*.md')
            junit(testResults: '**/build/test-results/test/**.xml', allowEmptyResults: true)
        }
    }
}
