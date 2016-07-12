import sbt._

/**
  * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
  * @author <a href="mailto:Young.Gu@lifcosys.com">Young Gu</a>
  */
object Dependencies {

  val javaslang = "io.javaslang" % "javaslang" % "2.0.1"
  val commonsIo = "commons-io" % "commons-io" % "2.4"
  val jodd = "org.jodd" % "jodd-lagarto" % "3.7.1"
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.21"
  val junitInterface =   "com.novocode" % "junit-interface" % "0.11"

  def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")

  def provided(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")

  def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")

  def it(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "it")

  def runtime(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")

  def container(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")
}
