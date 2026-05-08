// port-lint: source src/lib.rs (platform glue, JS target via Node fs.realpathSync)
package io.github.kotlinmania.dunce

private fun nodeRequire(name: String): dynamic = js("require")(name)

internal actual fun fsCanonicalize(path: String): String {
    val fs: dynamic = nodeRequire("fs")
    return fs.realpathSync(path).toString()
}
