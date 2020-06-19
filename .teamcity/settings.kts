import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.mavenArtifact
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {

    vcsRoot(TeamcityExample)

    buildType(Triggered)
    buildType(Deploy)

    params {
        param("nexus.url", "http://localhost:8081/repository/maven-releases/")
        param("env.SSO_PASS", "admin")
        param("env.SSO_USER", "admin")
        param("foo.version", "1.1")
    }
}

object Deploy : BuildType({
    name = "Deploy"

    enablePersonalBuilds = false
    type = BuildTypeSettings.Type.DEPLOYMENT
    maxRunningBuilds = 1

    vcs {
        root(TeamcityExample)
    }

    steps {
        maven {
            goals = "clean deploy"
            runnerArgs = "-Drevision=%foo.version% -Dnexus.url=%nexus.url%"
            mavenVersion = bundled_3_5()
            userSettingsSelection = "maven-settings.xml"
        }
    }
})

object Triggered : BuildType({
    name = "Triggered"

    steps {
        script {
            scriptContent = """echo "OK""""
        }
    }

    triggers {
        mavenArtifact {
            groupId = "com.jetbrains.teamcity.example"
            artifactId = "foo"
            version = "[1.0,)"
            repoUrl = "%nexus.url%"
            repoId = "nexus"
            userSettingsSelection = "maven-settings.xml"
        }
    }
})

object TeamcityExample : GitVcsRoot({
    name = "teamcity-example"
    url = "https://github.com/jet-test/teamcity-example.git"
    branch = "refs/heads/maven-artifact-dependency-trigger"
})
