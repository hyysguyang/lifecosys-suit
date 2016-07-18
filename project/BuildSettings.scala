import sbt.Keys._
import sbt._

/**
  * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
  * @author <a href="mailto:Young.Gu@lifcosys.com">Young Gu</a>
  */
object BuildSettings {
  val VERSION = "0.1"

  val lifecycle =
    addCommandAlias("install", ";scalariformFormat;compile;test") ++
      addCommandAlias("testing", ";clean;scalariformFormat;compile;test")

  val basicSettings = Defaults.coreDefaultSettings ++ lifecycle ++ Seq(
    version := VERSION,
    homepage := Some(new URL("https://lifecosys.com/developer/lifecosys-suit")),
    organization := "com.lifecosys",
    organizationHomepage := Some(new URL("https://lifecosys.com")),
    description := "UI friendly template.",
    licenses +=("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    startYear := Some(2016),
    scalaVersion := "2.11.6",
    credentials += Credentials(Path.userHome / ".bintray" / ".credentials"),
    javaOptions += s"-Xms128m -Xmx512m -source 1.8 -target 1.8 -Xlint",
    crossPaths := false,
    autoScalaLibrary := false
  )

  lazy val projectBuildSettings = basicSettings ++ formattingSettings

  import com.lifecosys.sbt.JavaCodeFormatterPlugin.JavaCodeFormatterKeys._

  val formattingSettings = List(
    eclipseFormatterFile in javaCodeFormatter := Some(file("codingstyle/org.eclipse.jdt.core.prefs.properties"))
  )

}
