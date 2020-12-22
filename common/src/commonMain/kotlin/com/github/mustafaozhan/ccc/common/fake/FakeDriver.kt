/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.fake

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.SqlPreparedStatement

object FakeDriver : SqlDriver {
    fun getDriver(): SqlDriver = this
    override fun close() = Unit

    override fun currentTransaction(): Transacter.Transaction? {
        TODO("Fake method Not yet implemented")
    }

    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ) = Unit

    override fun executeQuery(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ): SqlCursor = FakeSqlCursor.getSqlCursor()

    override fun newTransaction(): Transacter.Transaction {
        TODO("Fake method Not yet implemented")
    }
}
