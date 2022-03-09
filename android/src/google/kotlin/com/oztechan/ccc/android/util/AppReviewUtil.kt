/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.util

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

fun requestAppReview(
    activity: Activity
) = ReviewManagerFactory.create(activity).apply {
    requestReviewFlow().addOnCompleteListener { request ->
        if (request.isSuccessful) {
            launchReviewFlow(activity, request.result)
        }
    }
}
