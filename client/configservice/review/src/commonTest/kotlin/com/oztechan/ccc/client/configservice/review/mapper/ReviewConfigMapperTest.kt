package com.oztechan.ccc.client.configservice.review.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as AppReviewRCModel

internal class ReviewConfigMapperTest {

    @Test
    fun toReviewConfigModel() {
        val rcModel = AppReviewRCModel()
        val model = rcModel.toReviewConfigModel()

        assertEquals(rcModel.appReviewSessionCount, model.appReviewSessionCount)
        assertEquals(rcModel.appReviewDialogDelay, model.appReviewDialogDelay)
    }
}
