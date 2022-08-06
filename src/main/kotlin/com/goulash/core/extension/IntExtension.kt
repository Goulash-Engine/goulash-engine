package com.goulash.core.extension

import com.goulash.core.activity.Duration

object IntExtension

// TODO: add .once() function
fun Double.toDuration() = Duration(this)
