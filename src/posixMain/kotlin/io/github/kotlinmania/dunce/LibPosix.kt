// port-lint: source src/lib.rs (platform glue, posix native targets via realpath(3))
package io.github.kotlinmania.dunce

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.errno
import platform.posix.free
import platform.posix.realpath
import platform.posix.strerror

@OptIn(ExperimentalForeignApi::class)
internal actual fun fsCanonicalize(path: String): String {
    // Pass null so realpath(3) allocates the exact buffer size needed, avoiding
    // PATH_MAX limitations for paths whose resolved form exceeds that limit.
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
