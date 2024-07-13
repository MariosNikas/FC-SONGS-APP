package com.example.app.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.GetTeamsUseCase
import com.example.app.models.TeamInformation
import com.example.app.models.VideoInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpashScreenViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase,
) : ViewModel() {

    lateinit var onTeamsFetched: (List<TeamInformation>) -> Unit
    private val _isReady = MutableStateFlow<Boolean>(false)
    val isReady: StateFlow<Boolean> = _isReady

    init{
        viewModelScope.launch {
            val teams = getTeamsUseCase()
            onTeamsFetched(teams)
            _isReady.value = true
        }
    }
}