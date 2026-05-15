// port-lint: source src/lib.rs (platform glue, Wasm-JS target via Node fs.realpathSync)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.dunce

private val isNode: Boolean =
    js("typeof process !== 'undefined' && process.versions != null && process.versions.node != null")

private val realpathSyncImpl: (String) -> String =
    js(
        "(path) => {\n" +
            "  const fs = require('fs');\n" +
            "  return fs.realpathSync(path);\n" +
            "}",
    )

internal actual fun fsCanonicalize(path: String): String {
    if (!isNode) throw UnsupportedOperationException("dunce.canonicalize is only supported in a Node.js environment")
    return realpathSyncImpl(path)
}
