package com.loc.newsapp.presentation.bookmark

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
): ViewModel() {
    private val _state = mutableStateOf(BookMarkState())
    val state:MutableState<BookMarkState> = _state

    init {
        getArticles()
    }

    private fun getArticles(){
        newsUseCases.selectArticles().onEach {
            _state.value = _state.value.copy(articles = it.reversed())
        }.launchIn(viewModelScope)
    }
}