package com.sap.codelab.repository

import android.app.Application
import com.sap.codelab.di.appModule
import com.sap.codelab.di.databaseModule
import com.sap.codelab.di.repositoryModule
import com.sap.codelab.di.viewModelModule
import com.sap.codelab.geofence.GeofenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Extension of the Android Application class.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize the database
        initKoinDi()

        // re-register existing geofences on app start
        CoroutineScope(Dispatchers.IO).launch {
            val repository: IMemoRepository = getKoin().get()
            val openMemos = repository.getOpen()
            openMemos.forEach { memo ->
                if (memo.reminderLatitude != null && memo.reminderLongitude != null) {
                    getKoin().get<GeofenceManager>().addGeofence(
                        id = memo.id.toString(),
                        latitude = memo.reminderLatitude!!,
                        longitude = memo.reminderLongitude!!,
                        radius = 200f
                    )
                }
            }
        }
    }

    private fun initKoinDi() {
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(listOf(
                databaseModule,
                repositoryModule,
                appModule,
                viewModelModule
            ))
        }
    }

}