package com.kpiroom.bubble.util.date

import java.text.SimpleDateFormat
import java.util.*

val timeStamp
    get() = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())