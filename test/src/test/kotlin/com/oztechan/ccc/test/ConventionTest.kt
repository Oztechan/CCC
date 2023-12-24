package com.oztechan.ccc.test

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFunctionDeclaration
import com.lemonappdev.konsist.api.declaration.KoPropertyDeclaration
import com.lemonappdev.konsist.api.ext.list.indexOfFirstInstance
import com.lemonappdev.konsist.api.ext.list.indexOfLastInstance
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

internal class ConventionTest {
    @Test
    fun `properties are declared before functions`() {
        Konsist
            .scopeFromProject()
            .classes()
            .assertTrue {
                println(it.name)
                val lastKoPropertyDeclarationIndex = it
                    .declarations(includeNested = false, includeLocal = false)
                    .indexOfLastInstance<KoPropertyDeclaration>()

                val firstKoFunctionDeclarationIndex = it
                    .declarations(includeNested = false, includeLocal = false)
                    .indexOfFirstInstance<KoFunctionDeclaration>()

                if (lastKoPropertyDeclarationIndex != -1 && firstKoFunctionDeclarationIndex != -1) {
                    lastKoPropertyDeclarationIndex < firstKoFunctionDeclarationIndex
                } else {
                    true
                }
            }
    }

    @Test
    fun `companion object is last declaration in the class`() {
        Konsist
            .scopeFromProject()
            .classes()
            .assertTrue {
                println(it.name)

                val companionObject = it.objects(includeNested = false).lastOrNull { obj ->
                    obj.hasModifier(KoModifier.COMPANION)
                }

                if (companionObject != null) {
                    it.declarations(includeNested = false, includeLocal = false)
                        .last() == companionObject
                } else {
                    true
                }
            }
    }

    @Test
    fun `no empty files allowed`() {
        Konsist
            .scopeFromProject()
            .files
            .assertFalse {
                println(it.name)
                it.text.isEmpty()
            }
    }

    @Test
    fun `package name must match file path`() {
        Konsist
            .scopeFromProject()
            .packages
            .assertTrue {
                println(it.name)
                it.hasMatchingPath
            }
    }

    @Test
    fun `no wildcard imports allowed`() {
        Konsist
            .scopeFromProject()
            .imports
            .assertFalse {
                println(it.name)
                it.isWildcard
            }
    }

    @Test
    fun `every file in module reside in module specific package unless it is submodule`() {
        Konsist
            .scopeFromProject()
            .files
            .assertTrue {
                it.moduleName.replace("/", ".").let { modulePackaging ->
                    println(
                        it.moduleName + "==" + it.name + "==" + it.packagee?.fullyQualifiedName + "==" + modulePackaging
                    )
                    if (modulePackaging.contains("submodule")) {
                        true
                    } else {
                        it.packagee?.fullyQualifiedName?.startsWith("com.oztechan.ccc.$modulePackaging")
                    }
                }
            }
    }
}
