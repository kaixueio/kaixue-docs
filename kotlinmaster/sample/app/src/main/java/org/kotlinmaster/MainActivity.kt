package org.kotlinmaster

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

abstract class MainActivity : AppCompatActivity() {
    protected abstract fun test()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        GlobalScope.launch {
        }
        GlobalScope.async {
        }
    }

    suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {

    }
}
