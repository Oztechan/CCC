/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.model

import com.github.mustafaozhan.data.error.NetworkException

class NullBaseException : NetworkException(Exception("Base currency can not be null"))
