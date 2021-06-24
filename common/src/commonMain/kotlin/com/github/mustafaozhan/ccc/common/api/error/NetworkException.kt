/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api.error

internal open class NetworkException(cause: Throwable) : Throwable(cause)

internal class ModelMappingException(cause: Throwable) : NetworkException(cause)

internal class TimeoutException(cause: Throwable) : NetworkException(cause)

internal class EmptyParameterException : NetworkException(Exception("parameter can not be empty"))
