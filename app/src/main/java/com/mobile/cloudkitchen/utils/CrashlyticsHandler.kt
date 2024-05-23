package com.mobile.cloudkitchen.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsHandler() : Thread.UncaughtExceptionHandler {

    val runtime by lazy { Runtime.getRuntime() }

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        // Our custom logic goes here. For example calculate the memory heap
        val maxMemory = runtime.maxMemory()
        val freeMemory = runtime.freeMemory()
        val chunkedUpMemory = runtime.totalMemory() - freeMemory
        val freedUpMemory = maxMemory - chunkedUpMemory

        // Set values to Crashlytics
     //   Crashlytics.setLong("chunkedUp_memory", chunkedUpMemory)
       // Crashlytics.setLong("freedUp_memory", freedUpMemory)

        // This will make Crashlytics do its job
     //   defaultUncaughtExceptionHandler.uncaughtException(thread, ex)
    }
}