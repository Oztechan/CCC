/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.calculator.error

class BadSyntaxException(msg: String = "Bad Syntax") : Exception(msg)

class DivisionByZeroException(msg: String = "Division by Zero") : Exception(msg)
