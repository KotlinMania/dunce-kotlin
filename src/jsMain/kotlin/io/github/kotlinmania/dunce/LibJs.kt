// port-lint: source src/lib.rs (platform glue, JS target via Node fs.realpathSync)
package io.github.kotlinmania.dunce

private val isNode: Boolean get() =
    js("typeof process !== 'undefined' && process.versions != null && process.versions.node != null") as Boolean

internal actual fun fsCanonicalize(path: String): String {
    if (!isNode) throw UnsupportedOperationException("dunce.canonicalize is only supported in a Node.js environment")
    val fs: dynamic = js("require('fs')")
    return (fs.realpathSync(path) as Any).toString()
}
