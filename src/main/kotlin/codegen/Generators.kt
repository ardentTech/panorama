package codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

internal fun generateFile(comment: String, contents: List<TypeSpec>, packageName: String, fileName: String): FileSpec {
    require(contents.isNotEmpty())
    return FileSpec.builder(packageName, fileName)
        .addFileComment(comment)
        .addTypes(contents)
        .build()
}