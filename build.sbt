name := "Progress"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.easymock" % "easymock" % "3.1" withSources() withJavadoc(),
  "org.mongodb" % "mongo-java-driver" % "2.11.3" withSources() withJavadoc(),
  "com.google.code.gson" % "gson" % "2.2.4" withSources() withJavadoc()
)   

play.Project.playJavaSettings
