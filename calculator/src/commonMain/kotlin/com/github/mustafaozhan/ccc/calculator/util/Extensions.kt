/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.calculator.util

import com.github.mustafaozhan.ccc.calculator.model.Operators

@Suppress("ReturnCount")
infix fun <T> String.isIn(operators: Array<T>): Boolean {

    for (operator in operators) {
        if (operator is Operators) {
            if (this == operator.sign) {
                return true
            }
        }
    }
    return false
}

infix fun <T> String.notIn(operators: Array<T>): Boolean {
    return !(this isIn operators)
}
