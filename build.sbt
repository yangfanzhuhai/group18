name := "Progress"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.easymock" % "easymock" % "3.1" withSources() withJavadoc()
)   

play.Project.playJavaSettings
