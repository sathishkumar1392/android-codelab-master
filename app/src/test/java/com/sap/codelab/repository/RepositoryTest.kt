package com.sap.codelab.repository

import com.sap.codelab.geofence.GeofenceManager
import com.sap.codelab.model.Memo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private val dao: MemoDao = mockk()
    private val database: Database = mockk {
        every { getMemoDao() } returns dao
    }
    private val geofenceManager: GeofenceManager = mockk(relaxed = true)

    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(database, geofenceManager)
    }

    @Test
    fun `saveMemo inserts into db and adds geofence`() {
        val memo = Memo(
            id = 0,
            title = "T",
            description = "D",
            reminderDate = 0,
            reminderLatitude = 52.0,
            reminderLongitude = 13.0,
            isDone = false
        )
        every { dao.insert(memo) } returns 1L

        repository.saveMemo(memo)

        verify { dao.insert(memo) }
        verify { geofenceManager.addGeofence("1", 52.0, 13.0, 200f) }
    }
}
