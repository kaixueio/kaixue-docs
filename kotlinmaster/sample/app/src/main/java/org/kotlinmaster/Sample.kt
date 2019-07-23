package org.kotlinmaster

import android.app.Activity

class Sample {
    fun main() {
        var activity: Activity = NewActivity()
        if (activity is NewActivity) {
        }
    }
}

