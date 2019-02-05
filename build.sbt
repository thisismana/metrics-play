import scala.util.Properties

organization := "de.welt"
name := "metrics-play"
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

crossScalaVersions := Seq("2.12.8", "2.13.0-M5")

val PlayVersion = "2.7.0"

val metricsPlayVersion = "0.7.0"

val dropwizardVersion = "4.0.5"

version in ThisBuild := PlayVersion + "_" + Properties.envOrElse("BUILD_NUMBER", "0-SNAPSHOT")


scalacOptions := Seq("-unchecked", "-deprecation")

testOptions in Test += Tests.Argument("junitxml", "console")

parallelExecution in Test := false

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
    "io.dropwizard.metrics" % "metrics-core" % dropwizardVersion,
    "io.dropwizard.metrics" % "metrics-json" % dropwizardVersion,
    "io.dropwizard.metrics" % "metrics-jvm" % dropwizardVersion,
    "io.dropwizard.metrics" % "metrics-logback" % dropwizardVersion,
    "com.typesafe.play" %% "play" % PlayVersion % Provided,
    "org.joda" % "joda-convert" % "2.1.1",

    //Test
    "com.typesafe.play" %% "play-test" % PlayVersion % Test,
    "com.typesafe.play" %% "play-specs2" % PlayVersion % Test
)
scalacOptions := Seq("-unchecked", "-deprecation", "-target:jvm-1.8", "-Xcheckinit", "-encoding", "utf8", "-feature")

pomExtra :=
  <licenses>
    <license>
      <name>Apache-2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
      <comments>A business-friendly OSS license</comments>
    </license>
  </licenses>
    <scm>
      <url>https://github.com/thisismana/metrics-play</url>
      <connection>scm:git:git@github.com:thisismana/metrics-play.git</connection>
    </scm>
    <developers>
      <developer>
        <id>thisismana</id>
        <name>Matthias Naber</name>
        <url>https://github.com/thisismana</url>
      </developer>
    </developers>

bintrayRepository := s"metrics-play"
bintrayOrganization := Some("welt")
bintrayVcsUrl := Some("git@github.com:thisismana/play-metrics.git")