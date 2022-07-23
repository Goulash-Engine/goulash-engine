package com.barbarus.prosper.core.extension

import com.barbarus.prosper.core.activity.Duration

object IntExtension

fun Int.toDuration() = Duration(this)
