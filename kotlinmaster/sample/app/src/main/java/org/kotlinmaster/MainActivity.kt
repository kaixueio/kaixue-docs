package org.kotlinmaster

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class MainActivity : AppCompatActivity() {
    protected abstract fun test()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        GlobalScope.launch {
            val a = suspendingGetImage(imageId)
            avatarIv.setImageBitmap(image)
        }
    }

    suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {

    }
}
