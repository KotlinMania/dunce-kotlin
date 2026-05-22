// port-lint: source src/lib.rs (platform glue, JS target via Node fs.realpathSync)
package io.github.kotlinmania.dunce

private val isNode: Boolean get() =
    js("typeof process !== 'undefined' && process.versions != null && process.versions.node != null") as Boolean

private fun nodeRealpathSync(path: String): String? =
    js(
        "(function(path) { try { var r = eval('typeof require === \"function\" ? require : null'); " +
            "return r ? r('fs').realpathSync(path).toString() : null; } catch (e) { return null; } })(path)",
    ) as String?

internal actual fun fsCanonicalize(path: String): String {
    if (!isNode) throw UnsupportedOperationException("dunce.canonicalize is only supported in a Node.js environment")
    return nodeRealpathSync(path)
        ?: throw RuntimeException("dunce.canonicalize: Node fs.realpathSync is unavailable")
}

internal actual val isWindowsPathPlatform: Boolean = false
