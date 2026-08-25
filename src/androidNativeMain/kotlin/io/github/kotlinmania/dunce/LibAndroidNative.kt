package io.github.kotlinmania.dunce

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.errno
import platform.posix.free
import platform.posix.realpath
import platform.posix.strerror

@OptIn(ExperimentalForeignApi::class)
internal actual fun fsCanonicalize(path: String): String {
    val result = realpath(path, null)
        ?: run {
            val code = errno
            val message = strerror(code)?.toKString() ?: "errno=$code"
            throw RuntimeException("dunce.canonicalize: failed to resolve '$path': $message")
        }
    return try {
        result.toKString()
    } finally {
        free(result)
    }
}

internal actual val isWindowsPathPlatform: Boolean = false
