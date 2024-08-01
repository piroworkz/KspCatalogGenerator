package com.piroworkz.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo

class CatalogGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(
        catalog: Catalog,
        packageName: String,
    ) {
        val fileSpec: FileSpec = FileSpec
            .builder(packageName = packageName, fileName = "GeneratedVersionsCatalog")
            .buildLibsProperty()
            .buildLibraries(catalog.libraries)
            .buildPlugins(catalog.plugins)
            .build()
        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = true)
    }

    private fun FileSpec.Builder.buildLibsProperty(): FileSpec.Builder {
        addProperty(
            PropertySpec.builder(
                name = "libs",
                type = ClassName("org.gradle.api.artifacts", "VersionCatalog"),
                modifiers = listOf(KModifier.INTERNAL)
            ).apply {
                receiver(ClassName("org.gradle.api", "Project"))
                getter(libsPropertyGetter())
                build()
            }.build()
        )
        return this
    }

    private fun FileSpec.Builder.buildLibraries(libraries: List<String>): FileSpec.Builder {
        libraries.forEach { name ->
            val propertySpec = PropertySpec.builder(
                name = name,
                type = ClassName("org.gradle.api.provider", "Provider")
                    .parameterizedBy(
                        ClassName(
                            "org.gradle.api.artifacts",
                            "MinimalExternalModuleDependency"
                        )
                    ),
                modifiers = listOf(KModifier.INTERNAL)
            )
                .receiver(ClassName("org.gradle.api.artifacts", "VersionCatalog"))
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement(
                            "return findLibrary(\"$name\").get()",
                        ).build()
                ).build()
            addProperty(propertySpec)
        }
        return this
    }

    private fun FileSpec.Builder.buildPlugins(pluginNames: List<String>): FileSpec.Builder {
        pluginNames.forEach { name ->
            val propertySpec = PropertySpec.builder(
                name = name,
                type = ClassName("org.gradle.api.provider", "Provider")
                    .parameterizedBy(ClassName("org.gradle.plugin.use", "PluginDependency")),
                modifiers = listOf(KModifier.INTERNAL)
            )
                .receiver(ClassName("org.gradle.api.artifacts", "VersionCatalog"))
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return findPlugin(\"$name\").get()").build()
                ).build()
            addProperty(propertySpec)
        }
        return this
    }

    private fun libsPropertyGetter() = FunSpec.getterBuilder()
        .addStatement(
            "return extensions.getByType(%T::class.java).named(\"libs\")",
            ClassName("org.gradle.api.artifacts", "VersionCatalogsExtension"),
        ).build()
}
