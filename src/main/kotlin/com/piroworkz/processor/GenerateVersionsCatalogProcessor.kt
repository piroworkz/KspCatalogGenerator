package com.piroworkz.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.File

class GenerateVersionsCatalogProcessor(
    private val logger: KSPLogger,
    private val libs: String,
    private val packageName: String,
    codeGenerator: CodeGenerator,
) : SymbolProcessor {

    private val catalogGenerator = CatalogGenerator(codeGenerator = codeGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        try {
            val file = File(libs)
            val catalog: Catalog = Catalog.parseFile(file)
            logger.info("Catalog: $catalog")
            catalogGenerator.generate(catalog, packageName)
        } catch (e: FileAlreadyExistsException) {
            logger.info("File already exists")
        }
        return emptyList()
    }

}

