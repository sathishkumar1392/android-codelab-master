package com.sap.codelab.view.createMemoViewModelTest

import com.sap.codelab.repository.IMemoRepository
import com.sap.codelab.view.create.CreateMemoViewModel
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CreateMemoViewModelTest {

    private val repository: IMemoRepository = mockk(relaxed = true)
    private lateinit var viewModel: CreateMemoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CreateMemoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isMemoValid returns false when title or description is blank`() {
        viewModel.updateMemo("", "", 52.0, 13.0)

        assertFalse(viewModel.isMemoValid())
    }

    @Test
    fun `saveMemo calls repository`() = runTest {
        viewModel.updateMemo("Title", "Desc", 52.0, 13.0)
        viewModel.saveMemo()

        coVerify { repository.saveMemo(any()) }
    }
}