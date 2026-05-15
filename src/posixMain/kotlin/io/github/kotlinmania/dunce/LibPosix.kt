// port-lint: source src/lib.rs (platform glue, posix native targets via realpath(3))
package io.github.kotlinmania.dunce

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.PATH_MAX
import platform.posix.errno
import platform.posix.realpath
import platform.posix.strerror

@OptIn(ExperimentalForeignApi::class)
internal actual fun fsCanonicalize(path: String): String = memScoped {
    val buffer = allocArray<ByteVar>(PATH_MAX + 1)
    val result = realpath(path, buffer)
        ?: run {
            val code = errno
            val message = strerror(code)?.toKString() ?: "errno=$code"
            throw RuntimeException("dunce.canonicalize: failed to resolve '$path': $message")
        }
    result.toKString()
}
