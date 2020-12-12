/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.calculator.util

class Stack<T> {
    private val _stack = arrayListOf<T>()
    private var top = -1
    fun push(item: T) {
        _stack.add(item)
        top++
    }

    fun pop(): T = _stack.removeAt(top--)

    fun peek(): T = _stack[top]

    fun isEmpty() = _stack.isEmpty()

    fun size() = top + 1

    fun clear() {
        _stack.clear()
        top = -1
    }
}
