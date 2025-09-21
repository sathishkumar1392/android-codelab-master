package com.sap.codelab.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sap.codelab.model.Memo
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db: Database
    private lateinit var dao: MemoDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getMemoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun writeAndReadMemo() {
        val memo = Memo(0, "T", "D", 0, 52.0, 13.0, false)
        dao.insert(memo)

        val memos = dao.getAll()
        assertTrue(memos.isNotEmpty())
        assertEquals("T", memos[0].title)
    }
}