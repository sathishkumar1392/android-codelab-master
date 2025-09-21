package com.sap.codelab.view.create

import androidx.lifecycle.ViewModel
import com.sap.codelab.model.Memo
import com.sap.codelab.repository.IMemoRepository
import com.sap.codelab.repository.Repository
import com.sap.codelab.utils.coroutines.ScopeProvider
import com.sap.codelab.utils.extensions.empty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 *
 */
class CreateMemoViewModel(
    private val repository: IMemoRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var memo = Memo(
        id=0,
        title = String.empty(),
        description = String.empty(),
        reminderDate = 0,
        reminderLatitude = null,
        reminderLongitude = null,
        isDone = false)

    /**
     * Saves the memo in it's current state.
     */
    fun saveMemo() {
        ScopeProvider.application.launch(dispatcher) {
            repository.saveMemo(memo)
        }
    }

    /**
     * Call this method to update the memo. This is usually needed when the user changed his input.
     */
    fun updateMemo(title: String,
                   description: String,
                   latitude: Double,
                   longitude: Double
    ) {
        memo = Memo(
            title = title,
            description = description,
            id = 0,
            reminderDate = 0,
            reminderLatitude = latitude,
            reminderLongitude = longitude,
            isDone = false
        )
    }

    /**
     * @return true if the title and content are not blank; false otherwise.
     */
    fun isMemoValid(): Boolean {
        return memo.title.isNotBlank() &&
                memo.description.isNotBlank() &&
                memo.reminderLatitude != null &&
                memo.reminderLongitude != null
    }

    /**
     * @return true if the memo text is blank, false otherwise.
     */
    fun hasTextError() = memo.description.isBlank()

    /**
     * @return true if the memo title is blank, false otherwise.
     */
    fun hasTitleError() = memo.title.isBlank()

    fun updateLocation(latitude: Double, longitude: Double) {
        memo = memo.copy(
            reminderLatitude = latitude,
            reminderLongitude = longitude
        )
    }
}