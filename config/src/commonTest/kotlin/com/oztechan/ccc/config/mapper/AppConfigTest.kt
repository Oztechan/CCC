package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.entity.AppConfig as AppConfigEntity

class AppConfigTest : BaseTest() {
    @Test
    fun toModel() {
        val entity = AppConfigEntity()
        val model = entity.toModel()

        assertEquals(entity.adConfig.toModel(), model.adConfig)
        assertEquals(entity.appReview.toModel(), model.appReview)
        assertEquals(entity.appUpdate.map { it.toModel() }, model.appUpdate)
    }
}
