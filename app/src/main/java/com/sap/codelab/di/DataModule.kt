package com.sap.codelab.di

import androidx.room.Room
import com.sap.codelab.repository.DATABASE_NAME
import com.sap.codelab.repository.IMemoRepository
import com.sap.codelab.repository.Repository
import org.koin.dsl.module
import kotlin.jvm.java

val databaseModule = module {
    single {
            Room.databaseBuilder(
                get(),
                com.sap.codelab.repository.Database::class.java,
                DATABASE_NAME
            )
                .build()
    }

    // Provide DAO
    single { get<com.sap.codelab.repository.Database>().getMemoDao() }
}

val repositoryModule  = module {
    single<IMemoRepository> {
        Repository(get(),get())
    }
}