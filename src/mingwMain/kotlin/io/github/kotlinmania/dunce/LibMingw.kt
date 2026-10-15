package io.github.kotlinmania.dunce

import io.github.kotlinmania.windowssys.windows.win32.storage.filesystem.GetFullPathNameW

/**
 * Windows path normalization using `GetFullPathNameW` (UTF-16).
 *
 * Note: `GetFullPathNameW` absolutizes and normalizes the path, but does not require the
 * path to exist and does not resolve symbolic links. This matches the behavior of
 * `std::fs::canonicalize` on Windows, which also calls through to this Win32 API.
 * If the path does not exist, the call still succeeds and returns the normalized form.
 */
internal actual fun fsCanonicalize(path: String): String {
    val name = (path + "\u0000").map { it.code.toUShort() }.toUShortArray()
    val needed = GetFullPathNameW(name, 0u, null)
    if (needed == 0u) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameW failed for '$path'")
    }
    val buffer = UShortArray(needed.toInt())
    val written = GetFullPathNameW(name, needed, buffer)
    if (written == 0u || written >= needed) {
        throw RuntimeException("dunce.canonicalize: GetFullPathNameW failed for '$path'")
    }
    val chars = CharArray(written.toInt()) { buffer[it].toInt().toChar() }
    return chars.concatToString()
}

internal actual val isWindowsPathPlatform: Boolean = true
