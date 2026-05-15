// port-lint: source src/lib.rs (platform glue, mingw target via GetFullPathNameW)
package io.github.kotlinmania.dunce

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.windows.GetFullPathNameW
import platform.windows.WCHARVar

/**
 * Windows path normalization using `GetFullPathNameW` (UTF-16).
 *
 * Note: `GetFullPathNameW` absolutizes and normalizes the path, but does not require the
 * path to exist and does not resolve symbolic links. This matches the behavior of
 * `std::fs::canonicalize` on Windows, which also calls through to this Win32 API.
 * If the path does not exist, the call still succeeds and returns the normalized form.
 */
@OptIn(ExperimentalForeignApi::class)
internal actual fun fsCanonicalize(path: String): String = memScoped {
    val needed = GetFullPathNameW(path, 0u, null, null)
    if (needed == 0u) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameW failed for '$path'")
    }
    val buffer = allocArray<WCHARVar>(needed.toInt())
    val written = GetFullPathNameW(path, needed, buffer, null)
    if (written == 0u || written >= needed) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameW failed for '$path'")
    }
    buffer.toKString()
}
