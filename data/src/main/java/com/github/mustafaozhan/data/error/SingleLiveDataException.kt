/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.error

class SingleLiveDataException(
    message: String = "Multiple observers registered."
) : Throwable(message)
