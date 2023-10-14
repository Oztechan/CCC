package com.oztechan.ccc.client.core.persistence.fakes

import kotlin.random.Random

object Fakes {
    const val KEY = "key"
    val mockFloat = Random.nextFloat()
    val mockBoolean = Random.nextBoolean()
    val mockInt = Random.nextInt()
    val mockString = Random.nextInt().toString()
    val mockLong = Random.nextLong()
}
