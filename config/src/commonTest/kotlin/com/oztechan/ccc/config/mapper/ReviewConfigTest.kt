package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.service.review.ReviewConfig as AppReviewRCModel

class ReviewConfigTest : BaseTest() {

    @Test
    fun toReviewConfigModel() {
        val rcModel = AppReviewRCModel()
        val model = rcModel.toReviewConfigModel()

        assertEquals(rcModel.appReviewSessionCount, model.appReviewSessionCount)
        assertEquals(rcModel.appReviewDialogDelay, model.appReviewDialogDelay)
    }
}
