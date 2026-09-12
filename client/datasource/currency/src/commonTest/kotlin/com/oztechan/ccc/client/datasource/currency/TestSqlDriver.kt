package com.oztechan.ccc.client.datasource.currency

import app.cash.sqldelight.db.SqlDriver

/**
 * An in-memory driver with the schema applied, so tests exercise real SQL instead of mocks.
 * SQLDelight 2.x generates final query classes, which cannot be mocked on Kotlin/Native.
 */
internal expect fun createTestSqlDriver(): SqlDriver
