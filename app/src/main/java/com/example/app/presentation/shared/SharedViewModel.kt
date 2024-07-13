package com.example.app.presentation.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.models.TeamInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _teams = MutableStateFlow<List<TeamInformation>>(emptyList())
    val teams: StateFlow<List<TeamInformation>> = _teams

    private val _selectedIndex = MutableStateFlow<Int?>(null)
    val selectedIndex: StateFlow<Int?> = _selectedIndex

    fun fetchTeams(teams: List<TeamInformation>) {
        _teams.value = teams
    }

    fun getTeam(): TeamInformation? {
        val index = selectedIndex.value ?: return null
        return _teams.value.getOrNull(index)
    }

    fun selectTeam(index: Int) {
        _selectedIndex.value = index
    }
}