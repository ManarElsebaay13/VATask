package eg.com.test.vatask.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import java.util.prefs.Preferences

class MyApp  :Application(){

    lateinit var context: Context


    override fun onCreate() {
        super.onCreate()
        context = this
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
}

}


