// port-lint: source src/lib.rs (platform glue, Wasm-WASI target)
package io.github.kotlinmania.dunce

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal actual fun fsCanonicalize(path: String): String =
    runCatching { SystemFileSystem.resolve(Path(path)).toString() }
        .getOrElse { cause ->
            throw RuntimeException(
                "dunce.canonicalize: failed to resolve '$path': ${cause.message ?: "unknown error"}",
                cause,
            )
        }
