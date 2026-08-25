package io.github.kotlinmania.dunce

private val isNode: Boolean
    get() = js(
        "typeof process !== 'undefined' && process.versions != null && process.versions.node != null",
    ) as Boolean

private fun nodeRealpathSync(path: String): String? =
    js(
        "(function(path){ try { var rq = (new Function('return typeof require === \"function\" ? require : null'))(); if (!rq) return null; return rq('fs').realpathSync(path).toString(); } catch (e) { return null; } })(path)",
    ) as String?

internal actual fun fsCanonicalize(path: String): String {
    if (!isNode) throw UnsupportedOperationException("dunce.canonicalize is only supported in a Node.js environment")
    return nodeRealpathSync(path)
        ?: throw RuntimeException("dunce.canonicalize: Node fs.realpathSync is unavailable")
}

internal actual val isWindowsPathPlatform: Boolean = false
