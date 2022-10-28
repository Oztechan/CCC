package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.update.UpdateConfigEntity as AppUpdateEntity

class AppUpdateTest : BaseTest() {

    @Test
    fun toModel() {
        val entity = AppUpdateEntity(1, 2)
        val model = entity.toModel()

        assertEquals(entity.updateLatestVersion, model.updateLatestVersion)
        assertEquals(entity.updateForceVersion, model.updateForceVersion)
    }
}
