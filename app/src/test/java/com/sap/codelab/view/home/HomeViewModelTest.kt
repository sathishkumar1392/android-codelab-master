package com.sap.codelab.view.home

import com.sap.codelab.model.Memo
import com.sap.codelab.repository.IMemoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: IMemoRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAllMemos updates state flow`() = runTest {
        val memos = listOf(Memo(1, "Title", "Desc", 0, 52.0, 13.0, false))
        coEvery { repository.getAll() } returns memos

        viewModel.loadAllMemos()
        advanceUntilIdle()

        assertEquals(memos, viewModel.memos.value)
    }
}