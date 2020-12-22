/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.fake

import com.squareup.sqldelight.db.SqlCursor

object FakeSqlCursor : SqlCursor {
    fun getSqlCursor(): SqlCursor = this
    override fun close() = Unit

    override fun getBytes(index: Int): ByteArray? = null

    override fun getDouble(index: Int): Double? = null

    override fun getLong(index: Int): Long? = null

    override fun getString(index: Int): String? = null

    override fun next(): Boolean = false
}
