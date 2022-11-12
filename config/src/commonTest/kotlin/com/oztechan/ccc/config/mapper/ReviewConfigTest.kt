package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.service.review.ReviewConfigEntity as AppReviewEntity

class ReviewConfigTest : BaseTest() {

    @Test
    fun toModel() {
        val entity = AppReviewEntity()
        val model = entity.toModel()

        assertEquals(entity.appReviewSessionCount, model.appReviewSessionCount)
        assertEquals(entity.appReviewDialogDelay, model.appReviewDialogDelay)
    }
}
