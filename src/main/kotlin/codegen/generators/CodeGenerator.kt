package codegen.generators

import codegen.KtFile
import java.nio.file.Path

interface CodeGenerator {
    fun generate(file: KtFile) // stdout target
    fun generate(destination: Path, file: KtFile) // filesystem target
}