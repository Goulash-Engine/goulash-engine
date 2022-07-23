package com.barbarus.prosper.core.extension

import com.barbarus.prosper.core.activity.Duration

object IntExtension

// TODO: add .once() function
fun Int.toDuration() = Duration(this)
