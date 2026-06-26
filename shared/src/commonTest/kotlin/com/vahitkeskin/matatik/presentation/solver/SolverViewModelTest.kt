package com.vahitkeskin.matatik.presentation.solver

import com.vahitkeskin.matatik.core.localization.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SolverViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `gecerli girdi cozum uretir`() = runTest {
        val vm = SolverViewModel()
        vm.onIntent(SolverIntent.InputChanged("2x + 3 = 7"))
        vm.onIntent(SolverIntent.Solve)
        val state = vm.state.value
        assertNotNull(state.solution)
        assertEquals("x = 2", state.solution?.finalAnswerLatex)
        assertNull(state.errorMessage)
    }

    @Test
    fun `gecersiz girdi hata mesaji uretir`() = runTest {
        val vm = SolverViewModel()
        vm.onIntent(SolverIntent.InputChanged("2 @ 3"))
        vm.onIntent(SolverIntent.Solve)
        val state = vm.state.value
        assertNull(state.solution)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `dil degisimi metinleri gunceller`() = runTest {
        val vm = SolverViewModel()
        vm.onIntent(SolverIntent.ChangeLanguage(Language.EN))
        assertEquals("Solve", vm.state.value.strings.solveButton)
    }

    @Test
    fun `temizleme durumu sifirlar`() = runTest {
        val vm = SolverViewModel()
        vm.onIntent(SolverIntent.InputChanged("2x = 4"))
        vm.onIntent(SolverIntent.Solve)
        vm.onIntent(SolverIntent.Clear)
        val state = vm.state.value
        assertEquals("", state.inputText)
        assertNull(state.solution)
    }
}
