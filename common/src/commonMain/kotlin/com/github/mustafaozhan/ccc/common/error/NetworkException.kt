/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.error

open class NetworkException(cause: Throwable) : Throwable(cause)

class ModelMappingException(cause: Throwable) : NetworkException(cause)

class TimeoutException(cause: Throwable) : NetworkException(cause)

class EmptyParameterException : NetworkException(Exception("parameter can not be empty"))

class BackendCanBeDownException : NetworkException(Exception("Backend can be down!"))
