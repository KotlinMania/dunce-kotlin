// port-lint: source src/lib.rs
package io.github.kotlinmania.dunce

/**
 * Filesystem paths in Windows are a total mess. This package normalizes paths to the most
 * compatible (but still correct) format, so that you don't have to worry about the mess.
 *
 * In Windows the regular/legacy paths (`C:\foo`) are supported by all programs, but have
 * lots of bizarre restrictions for backwards compatibility with MS-DOS.
 *
 * And there are Windows NT UNC paths (`\\?\C:\foo`), which are more robust and with fewer
 * gotchas, but are rarely supported by Windows programs. Even Microsoft's own!
 *
 * This package converts paths to legacy format whenever possible, but leaves UNC paths as-is
 * when they can't be unambiguously expressed in a simpler way. This allows legacy programs
 * to access all paths they can possibly access, and UNC-aware programs to access all paths.
 *
 * The [simplified] function operates on path strings regardless of the current platform:
 * it strips the `\\?\` prefix whenever the result is unambiguous, and leaves all other paths
 * unmodified. It is safe to call on any platform.
 *
 * Parsing is based on <https://msdn.microsoft.com/en-us/library/windows/desktop/aa365247(v=vs.85).aspx>
 *
 * [Project homepage](https://lib.rs/crates/dunce).
 */

/**
 * Takes any path, and when possible, converts Windows UNC paths to regular paths.
 * If the path can't be converted, it's returned unmodified.
 *
 * On non-Windows-shaped strings this is a no-op.
 *
 * `\\?\C:\Windows` will be converted to `C:\Windows`,
 * but `\\?\C:\COM` will be left as-is (due to a reserved filename).
 *
 * Use this to pass arbitrary paths to programs that may not be UNC-aware.
 *
 * It's generally safe to pass UNC paths to legacy programs, because
 * these paths contain a reserved prefix, so will gracefully fail
 * if used with legacy APIs that don't support UNC.
 *
 * This function does not perform any I/O.
 *
 * To check if a path remained as UNC, use `path.startsWith("\\\\")`.
 */
public fun simplified(path: String): String =
    if (isSafeToStripUnc(path)) path.substring(4) else path

/**
 * Like the platform's filesystem `canonicalize`, but on Windows it outputs the most
 * compatible form of a path instead of UNC.
 *
 * Throws if the underlying filesystem call fails.
 */
public fun canonicalize(path: String): String = simplified(fsCanonicalize(path))

/** Alias of [canonicalize]. */
public fun realpath(path: String): String = canonicalize(path)

internal expect fun fsCanonicalize(path: String): String

internal fun windowsCharLen(s: String): Int = s.length

internal fun isValidFilename(fileName: String): Boolean {
    val bytes = fileName.encodeToByteArray()
    if (bytes.size > 255 && windowsCharLen(fileName) > 255) {
        return false
    }
    if (bytes.isEmpty()) {
        return false
    }
    for (byte in bytes) {
        val u = byte.toInt() and 0xFF
        if (u in 0..31) return false
        if (u == '<'.code || u == '>'.code || u == ':'.code || u == '"'.code ||
            u == '/'.code || u == '\\'.code || u == '|'.code || u == '?'.code || u == '*'.code
        ) {
            return false
        }
    }
    val last = bytes[bytes.size - 1].toInt() and 0xFF
    if (last == ' '.code || last == '.'.code) {
        return false
    }
    return true
}

internal val RESERVED_NAMES: Array<String> = arrayOf(
    "AUX", "NUL", "PRN", "CON", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8",
    "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9",
)

internal fun isReserved(fileName: String): Boolean {
    val stem = fileStem(fileName) ?: return false
    val trimmed = rightTrim(stem)
    val byteLen = trimmed.encodeToByteArray().size
    if (byteLen > 4) return false
    for (reserved in RESERVED_NAMES) {
        if (trimmed.length == reserved.length && trimmed.equals(reserved, ignoreCase = true)) {
            return true
        }
    }
    return false
}

internal fun isSafeToStripUnc(path: String): Boolean {
    if (path.length < 6) return false
    if (path[0] != '\\' || path[1] != '\\' || path[2] != '?' || path[3] != '\\') return false
    val drive = path[4]
    if (!isAsciiLetter(drive)) return false
    if (path[5] != ':') return false
    if (path.length > 6 && path[6] != '\\' && path[6] != '/') return false

    var i = 6
    while (i < path.length) {
        val ch = path[i]
        if (ch == '\\' || ch == '/') {
            i++
            continue
        }
        var j = i
        while (j < path.length && path[j] != '\\' && path[j] != '/') {
            j++
        }
        val component = path.substring(i, j)
        if (component == "." || component == "..") {
            return false
        }
        if (!isValidFilename(component) || isReserved(component)) {
            return false
        }
        i = j
    }

    val byteLen = path.encodeToByteArray().size
    if (byteLen > 260 && windowsCharLen(path) > 260) {
        return false
    }
    return true
}

/** Trim '.' and ' ' from the right. */
internal fun rightTrim(s: String): String = s.trimEnd(' ', '.')

/**
 * Returns the file stem of the final path component, or `null` if there is no
 * meaningful file name (path terminates in `..`, equals `.`, or is empty).
 *
 * The stem is:
 * - `null`, if there is no file name;
 * - the entire file name if there is no embedded `.`;
 * - the entire file name if the file name begins with `.` and has no other `.`s within;
 * - otherwise, the portion of the file name before the final `.`.
 */
private fun fileStem(name: String): String? {
    if (name.isEmpty()) return null
    if (name == "." || name == "..") return null
    val firstDot = name.indexOf('.')
    if (firstDot < 0) return name
    val lastDot = name.lastIndexOf('.')
    if (firstDot == 0 && firstDot == lastDot) return name
    return name.substring(0, lastDot)
}

private fun isAsciiLetter(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z'
