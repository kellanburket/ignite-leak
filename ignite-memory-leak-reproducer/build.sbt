lazy val mySettings = Seq(
	version := "0.2.0",
	scalaVersion := "2.11.12",
	organization := "co.mira",
	name := "mira-lib-ignite",
	retrieveManaged := true,
	libraryDependencies ++= deps,
	parallelExecution := false
)

lazy val deps = Seq(
	"org.apache.ignite" % "ignite-core" % "2.7.0" % "provided",
	"org.apache.ignite" % "ignite-scalar" % "2.7.0"
)

lazy val miraLibIgnite = (
	project in file(".")
).enablePlugins(
	JavaAppPackaging
).settings(
	mySettings
)

test in assembly := {}

assemblyMergeStrategy in assembly := {
	case PathList("META-INF", xs @ _*) => MergeStrategy.discard
	case x => MergeStrategy.first
}