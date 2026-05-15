// port-lint: source src/lib.rs (platform glue, Android/JVM target via java.io.File)
package io.github.kotlinmania.dunce

import java.io.File

internal actual fun fsCanonicalize(path: String): String = File(path).canonicalPath
