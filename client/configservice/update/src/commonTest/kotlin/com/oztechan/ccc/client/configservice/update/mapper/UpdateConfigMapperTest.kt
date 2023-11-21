package com.oztechan.ccc.client.configservice.update.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as AppUpdateRCModel

internal class UpdateConfigMapperTest {

    @Test
    fun toUpdateConfigModel() {
        val rcModel = AppUpdateRCModel(1, 2)
        val model = rcModel.toUpdateConfigModel()

        assertEquals(rcModel.updateLatestVersion, model.updateLatestVersion)
        assertEquals(rcModel.updateForceVersion, model.updateForceVersion)
    }
}
