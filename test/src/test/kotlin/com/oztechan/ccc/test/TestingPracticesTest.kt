package com.oztechan.ccc.test

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withoutAllAnnotationsOf
import com.lemonappdev.konsist.api.ext.provider.hasAnnotationOf
import com.lemonappdev.konsist.api.verify.assert
import kotlin.test.Test

internal class TestingPracticesTest {

    @Test
    fun `classes with 'Test' Annotation should have 'Test' suffix`() {
        Konsist
            .scopeFromTest()
            .classes()
            .filter { it.functions().any { func -> func.hasAnnotationOf<Test>() } }
            .assert { it.hasNameEndingWith("Test") }
    }

    @Test
    fun `test classes should be internal`() {
        Konsist
            .scopeFromTest()
            .classes()
            .withNameEndingWith("Test")
            .assert { it.hasInternalModifier }
    }

    @Test
    fun `test classes should have all members private besides tests`() {
        Konsist
            .scopeFromTest()
            .classes()
            .withNameEndingWith("Test")
            .properties()
            .withoutAllAnnotationsOf(Test::class)
            .assert { it.hasPrivateModifier || it.hasProtectedModifier }
    }
}
