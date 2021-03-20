package com.github.mustafaozhan.ccc.client.model

open class Tracking(trackDetail: String, type: RemoveAdType) : Throwable("$trackDetail $type")
class RestorePurchaseTracking(type: RemoveAdType) : Tracking(" Restore purchase", type)
class NewPurchaseTracking(type: RemoveAdType) : Tracking("New purchase", type)
