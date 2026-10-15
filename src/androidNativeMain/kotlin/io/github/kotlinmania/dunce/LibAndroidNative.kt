package io.github.kotlinmania.dunce

import io.github.kotlinmania.libc.unix.realpath

internal actual fun fsCanonicalize(path: String): String =
    realpath(path, null) ?: throw RuntimeException("dunce.canonicalize: failed to resolve '$path'")

internal actual val isWindowsPathPlatform: Boolean = false
