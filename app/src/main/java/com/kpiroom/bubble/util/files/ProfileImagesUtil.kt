package com.kpiroom.bubble.util.files

import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.date.timeStamp

fun getCurrentProfileImageName(uuid: String): String = str(
    R.string.template_image_with_timestamp,
    uuid,
    timeStamp
)