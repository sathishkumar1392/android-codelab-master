package com.sap.codelab.view.detail

import com.sap.codelab.model.Memo
import com.sap.codelab.repository.IMemoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ViewMemoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: IMemoRepository
    private lateinit var viewModel: ViewMemoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = ViewMemoViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadMemo fetches memo by id`() = runTest {
        val expected = Memo(
            id = 1,
            title = "T",
            description = "D",
            reminderDate = 0,
            reminderLatitude = 52.0,
            reminderLongitude = 13.0,
            isDone = false
        )
        coEvery { repository.getMemoById(1) } returns expected
        viewModel.loadMemo(1)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(expected, viewModel.memo.value)
    }
}