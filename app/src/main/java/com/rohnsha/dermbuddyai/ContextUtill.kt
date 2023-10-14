package com.rohnsha.dermbuddyai

import android.content.Context

object ContextUtill {

    object ContextUtils {
        private lateinit var appContext: Context

        fun initialize(context: Context) {
            appContext = context.applicationContext
        }

        fun getApplicationContext(): Context {
            return appContext
        }
    }

}