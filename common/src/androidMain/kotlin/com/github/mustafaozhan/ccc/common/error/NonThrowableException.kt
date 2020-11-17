/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.error

class NonThrowableException(message: String) :
    Exception(message, Throwable(message))
