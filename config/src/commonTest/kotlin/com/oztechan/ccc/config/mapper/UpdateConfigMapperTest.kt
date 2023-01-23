package com.oztechan.ccc.config.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.service.update.UpdateConfig as AppUpdateRCModel

class UpdateConfigMapperTest {

    @Test
    fun toUpdateConfigModel() {
        val rcModel = AppUpdateRCModel(1, 2)
        val model = rcModel.toUpdateConfigModel()

        assertEquals(rcModel.updateLatestVersion, model.updateLatestVersion)
        assertEquals(rcModel.updateForceVersion, model.updateForceVersion)
    }
}
