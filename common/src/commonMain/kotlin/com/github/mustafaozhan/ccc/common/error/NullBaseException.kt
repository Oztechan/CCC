/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.error

class NullBaseException : NetworkException(Exception("Base currency can not be null"))
