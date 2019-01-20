package com.kpiroom.bubble.os

import android.app.Application

class BubbleApp : Application() {

    companion object {

        lateinit var app: BubbleApp

    }

    override fun onCreate() {
        super.onCreate()

        app = this
    }
}