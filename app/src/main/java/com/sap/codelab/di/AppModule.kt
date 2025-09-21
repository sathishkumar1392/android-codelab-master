package com.sap.codelab.di

import com.sap.codelab.geofence.GeofenceManager
import com.sap.codelab.view.create.CreateMemoViewModel
import com.sap.codelab.view.detail.ViewMemoViewModel
import com.sap.codelab.view.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        GeofenceManager(androidContext())
    }
}

val viewModelModule = module {
    viewModel { HomeViewModel(
        repository = get(),
        dispatcher = Dispatchers.Default
        ) }
    viewModel {
        CreateMemoViewModel(
            repository =get(),
            dispatcher = Dispatchers.Default
        )
    }
    viewModel { ViewMemoViewModel(
        repository = get(),
        dispatcher = Dispatchers.Default) }
}