package com.sap.codelab.repository

import androidx.annotation.WorkerThread
import com.sap.codelab.geofence.GeofenceManager
import com.sap.codelab.model.Memo

const val DATABASE_NAME: String = "codelab"

/**
 * The repository is used to retrieve data from a data source.
 */
class Repository(
    private val database: Database,
    private val geofenceManager: GeofenceManager
) : IMemoRepository {

    @WorkerThread
    override fun saveMemo(memo: Memo) {
        val generatedId = database.getMemoDao().insert(memo)

        if (memo.reminderLatitude != null && memo.reminderLongitude != null) {
            geofenceManager.addGeofence(
                id = generatedId.toString(),
                latitude = memo.reminderLatitude!!,
                longitude = memo.reminderLongitude!!,
                radius = 200f
            )
        }
    }

    @WorkerThread
    override fun getOpen(): List<Memo> = database.getMemoDao().getOpen()

    @WorkerThread
    override fun getAll(): List<Memo> = database.getMemoDao().getAll()

    @WorkerThread
    override fun getMemoById(id: Long): Memo = database.getMemoDao().getMemoById(id)
}