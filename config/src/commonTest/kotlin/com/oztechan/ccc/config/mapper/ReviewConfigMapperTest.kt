package com.oztechan.ccc.config.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as AppReviewRCModel

class ReviewConfigMapperTest {

    @Test
    fun toReviewConfigModel() {
        val rcModel = AppReviewRCModel()
        val model = rcModel.toReviewConfigModel()

        assertEquals(rcModel.appReviewSessionCount, model.appReviewSessionCount)
        assertEquals(rcModel.appReviewDialogDelay, model.appReviewDialogDelay)
    }
}
