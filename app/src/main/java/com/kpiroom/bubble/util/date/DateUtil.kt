package com.kpiroom.bubble.util.date

import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.str
import java.text.SimpleDateFormat
import java.util.*

val timeStamp
    get() = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

fun msToDateStr(milliseconds: Long, dateFormat: String = str(R.string.date_format)): String =
    SimpleDateFormat(dateFormat).format(
        Calendar.getInstance().apply {
            timeInMillis = milliseconds
        }.time
    )