package com.oztechan.ccc.common.mapper

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.db.sql.Currency as CurrencyEntity

@Suppress("OPT_IN_USAGE")
class CurrencyTest {

    private val entity = CurrencyEntity("Dollar", "United State Dollar", "$", 12.3, 1)

    @Test
    fun toModel() {
        val model = entity.toModel()

        assertEquals(entity.name, model.name)
        assertEquals(entity.longName, model.longName)
        assertEquals(entity.symbol, model.symbol)
        assertEquals(entity.rate, model.rate)
        assertEquals(true, model.isActive)
    }

    @Test
    fun toModelIsActive() {
        val entityDeActive = CurrencyEntity("Dollar", "United State Dollar", "$", 12.3, 0)

        assertEquals(true, entity.toModel().isActive)
        assertEquals(false, entityDeActive.toModel().isActive)
    }

    @Test
    fun toModelList() {
        val entityEuro = CurrencyEntity("Euro", "Euro", "", 321.321, 0)
        val currencyList = listOf(entity, entityEuro)

        val modelList = currencyList.toModelList()

        currencyList.zip(modelList) { first, second ->
            assertEquals(first.name, second.name)
            assertEquals(first.longName, second.longName)
            assertEquals(first.symbol, second.symbol)
            assertEquals(first.rate, second.rate)
            assertEquals(first.isActive == 1.toLong(), second.isActive)
        }
    }

    @Test
    fun mapToModel() {
        val currencyList = listOf(entity)

        val currencyListFlow = flowOf(currencyList)

        runTest {
            currencyListFlow.mapToModel().firstOrNull()?.firstOrNull()?.let {
                assertEquals(entity.name, it.name)
                assertEquals(entity.longName, it.longName)
                assertEquals(entity.symbol, it.symbol)
                assertEquals(entity.rate, it.rate)
                assertEquals(true, it.isActive)
            }
        }
    }
}
