// port-lint: source lib.rs (platform glue, JVM target via java.io.File)
package io.github.kotlinmania.dunce

import java.io.File

internal actual fun fsCanonicalize(path: String): String = File(path).canonicalPath

internal actual val isWindowsPathPlatform: Boolean = false
