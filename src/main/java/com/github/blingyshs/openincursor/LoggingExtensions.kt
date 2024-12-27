package com.github.blingyshs.openincursor

import com.intellij.openapi.diagnostic.Logger

val Any.log: Logger
    get() = Logger.getInstance(this::class.java) 