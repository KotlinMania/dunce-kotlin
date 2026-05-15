// port-lint: source src/lib.rs (platform glue, mingw target via GetFullPathNameA)
package io.github.kotlinmania.dunce

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.windows.GetFullPathNameA

@OptIn(ExperimentalForeignApi::class)
internal actual fun fsCanonicalize(path: String): String = memScoped {
    val needed = GetFullPathNameA(path, 0u, null, null)
    if (needed == 0u) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameA failed for '$path'")
    }
    val buffer = allocArray<ByteVar>(needed.toInt())
    val written = GetFullPathNameA(path, needed, buffer, null)
    if (written == 0u || written >= needed) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameA failed for '$path'")
    }
    buffer.toKString()
}
