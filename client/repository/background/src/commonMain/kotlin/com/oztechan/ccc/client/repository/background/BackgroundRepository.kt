package com.oztechan.ccc.client.repository.background

interface BackgroundRepository {
    fun shouldSendNotification(): Boolean
}
