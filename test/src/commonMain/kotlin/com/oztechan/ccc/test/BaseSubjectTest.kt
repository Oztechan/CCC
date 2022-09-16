package com.oztechan.ccc.test

abstract class BaseSubjectTest<T> : BaseTest() {
    protected abstract val subject: T
}
