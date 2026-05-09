// port-lint: source src/lib.rs (platform glue, Wasm-JS target via Node fs.realpathSync)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.dunce

private val realpathSyncImpl: (String) -> String =
    js(
        "(path) => {\n" +
            "  const fs = require('fs');\n" +
            "  return fs.realpathSync(path);\n" +
            "}",
    )

internal actual fun fsCanonicalize(path: String): String = realpathSyncImpl(path)
