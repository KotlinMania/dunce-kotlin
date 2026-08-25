package io.github.kotlinmania.dunce

import java.io.File

internal actual fun fsCanonicalize(path: String): String = File(path).canonicalPath

internal actual val isWindowsPathPlatform: Boolean = false
