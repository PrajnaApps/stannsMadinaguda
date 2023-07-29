package prajna.app.utills

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Coroutines {
    fun <T : Any> ioToMainthread(
        work: suspend (() -> T?), callback: (T?) -> Unit
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val data = CoroutineScope(Dispatchers.IO).async outer@{
                return@outer work()
            }.await()
            callback(data)
        }
}