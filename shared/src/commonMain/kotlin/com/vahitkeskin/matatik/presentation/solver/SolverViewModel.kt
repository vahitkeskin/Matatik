package com.vahitkeskin.matatik.presentation.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vahitkeskin.matatik.core.domain.engine.MathSolverEngine
import com.vahitkeskin.matatik.core.domain.engine.UnsupportedProblemException
import com.vahitkeskin.matatik.core.domain.parser.MathParseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Çözücü ekranının MVI ViewModel'i.
 *
 * Reaktif bir durum makinesi olarak çalışır: [SolverIntent] alır, [SolverState]
 * yayınlar ve tek seferlik olaylar için [SolverEffect] üretir.
 * CAS motoru platform-bağımsızdır; Android, iOS ve Desktop'ta aynı şekilde çalışır.
 */
class SolverViewModel(
    private val engine: MathSolverEngine = MathSolverEngine()
) : ViewModel() {

    private val _state = MutableStateFlow(SolverState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SolverEffect>(extraBufferCapacity = 8)
    val effects: Flow<SolverEffect> = _effects

    fun onIntent(intent: SolverIntent) {
        when (intent) {
            is SolverIntent.InputChanged -> _state.update {
                it.copy(inputText = intent.value, errorMessage = null)
            }
            is SolverIntent.ChangeLanguage -> _state.update {
                it.copy(language = intent.language)
            }
            is SolverIntent.LoadExample -> _state.update {
                it.copy(inputText = intent.expression, errorMessage = null, solution = null)
            }
            SolverIntent.Clear -> _state.update {
                it.copy(inputText = "", solution = null, errorMessage = null)
            }
            SolverIntent.Solve -> solve()
        }
    }

    private fun solve() {
        val current = _state.value
        if (current.inputText.isBlank()) {
            emitError(current.strings.errorEmpty)
            return
        }
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val strings = _state.value.strings
            try {
                val solution = engine.solve(current.inputText)
                _state.update { it.copy(isLoading = false, solution = solution, errorMessage = null) }
                _effects.emit(SolverEffect.Solved)
            } catch (e: MathParseException) {
                _state.update { it.copy(isLoading = false, solution = null, errorMessage = strings.errorParse) }
                _effects.emit(SolverEffect.ShowError(strings.errorParse))
            } catch (e: UnsupportedProblemException) {
                _state.update { it.copy(isLoading = false, solution = null, errorMessage = strings.errorUnsupported) }
                _effects.emit(SolverEffect.ShowError(strings.errorUnsupported))
            }
        }
    }

    private fun emitError(message: String) {
        _state.update { it.copy(errorMessage = message) }
        viewModelScope.launch { _effects.emit(SolverEffect.ShowError(message)) }
    }
}
