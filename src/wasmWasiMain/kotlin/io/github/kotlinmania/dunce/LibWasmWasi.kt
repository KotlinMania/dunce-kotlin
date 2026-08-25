package io.github.kotlinmania.dunce

internal actual fun fsCanonicalize(path: String): String = path

internal actual val isWindowsPathPlatform: Boolean = false
