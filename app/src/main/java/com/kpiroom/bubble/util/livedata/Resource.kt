package com.kpiroom.bubble.util.livedata

import com.kpiroom.bubble.util.exceptions.ErrorHelper

class Resource<T>(val data: T? = null, val error: Throwable? = null)