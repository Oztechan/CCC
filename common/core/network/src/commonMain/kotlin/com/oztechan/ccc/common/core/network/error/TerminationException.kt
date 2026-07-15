package com.oztechan.ccc.common.core.network.error

import kotlinx.coroutines.CancellationException

// Maps coroutine cancellation into the project's error taxonomy. Extends CancellationException (not
// Throwable) so structured concurrency still recognises it as cancellation and unwinds cleanly.
internal class TerminationException(cause: Throwable) : CancellationException(cause.message)
