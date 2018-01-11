name := "kamon-akka"

version := "1.0"

scalaVersion := "2.11.11"

fork in run := true

libraryDependencies ++= Seq(
  "ch.qos.logback"             % "logback-core"         % "1.1.7",
  "ch.qos.logback"             % "logback-classic"      % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "com.typesafe.scala-logging" %% "scala-logging"       % "3.1.0",

  "com.typesafe.akka"   %% "akka-stream"      % "2.5.8",
  "com.typesafe.akka"   %% "akka-slf4j"       % "2.5.8",
  "io.kamon"            %% "kamon-core"       % "0.6.7",
  "io.kamon"            %% "kamon-akka-2.5"   % "0.6.8",
  "io.kamon"            %% "kamon-scala"      % "0.6.7",
  "io.kamon"            %% "kamon-autoweave"  % "0.6.5"

  //  "io.kamon" %% "kamon-akka-2.5"       % "1.0.0-RC7",
  //  "io.kamon" %% "kamon-akka"           % "0.6.3",
  //  "io.kamon" %% "kamon-scala-future"   % "1.0.0-RC7",
)
