package codegen

import lexicon.LexiconDoc
import java.nio.file.Path

// nothing from KP propagates further than here
// this bridges ATP and KP
// TODO define formal interface
object LexiconCodeGenerator {

    fun generate(doc: LexiconDoc): Result<String> {
        // write to stdout
        return Result.success("package.File")
    }

    fun generate(doc: LexiconDoc, path: Path): Result<String> {
        // write to fs
        return Result.success("path.to.package.File")
    }

}
