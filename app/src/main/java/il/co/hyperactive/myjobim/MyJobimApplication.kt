package il.co.hyperactive.myjobim

import android.app.Application

class MyJobimApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JobRepository.initialize(this)
    }
}