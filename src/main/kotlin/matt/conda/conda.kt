package matt.conda

import matt.file.commons.REGISTERED_FOLDER
import matt.file.commons.TEMP_DIR
import matt.file.sh
import matt.kjlib.shell.Shell

private val MINICONDA_FOLDER = REGISTERED_FOLDER + "miniconda"
private val MINICONDA_ENVS_FOLDER = MINICONDA_FOLDER + "envs"
private val MINICONDA_BIN_FOLDER = MINICONDA_FOLDER + "bin"

fun Shell<*>.installMinicondaIfNeccessary() {
  val minicondaInstallScript = TEMP_DIR + "miniconda".sh
  if (MINICONDA_FOLDER.doesNotExist) {
	curl(
	  "https://repo.anaconda.com/miniconda/Miniconda3-4.7.12.1-MacOSX-x86_64.sh",
	  "-o", minicondaInstallScript.abspath
	)
	bash(minicondaInstallScript.abspath, "-b", "-p", MINICONDA_FOLDER.abspath)
  }
}

typealias CondaEnv = CondaEnvTyped<*>
class CondaEnvTyped<R>(name: String, private val shell: Shell<R>) {
  private val folder = MINICONDA_ENVS_FOLDER + name
  private val binFolder = folder + "bin"
  val python = binFolder + "python"
  fun createIfDoesNotExist(version: String) {
	if (folder.doesNotExist) {
	  shell.sendCommand(
		MINICONDA_BIN_FOLDER["conda"],
		"create",
		"-y",
		"-n",
		"deephy",
		"python=$version"
	  )
	}
  }

  fun pip(vararg args: String): R = shell.sendCommand(
	binFolder["pip"], *args
  )
}

