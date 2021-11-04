package com.example.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainViewModel : ViewModel() {

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    private val _currentOperation = MutableStateFlow<Operation?>(null)
    val currentOperation = _currentOperation.asStateFlow()

    private val _result = MutableStateFlow("")
    val result = _result.asStateFlow()

    private var cachedResult: String = ""

    fun numberClicked(number: Char) {
        if (_result.value.isNotEmpty()) {
            _result.value = _result.value + number
        } else {
            _result.value = number.toString()
        }
    }

    fun operationClicked(operation: Operation) {
        if (_result.value.isEmpty()) {
            setCurrentOperation(operation)
            return
        }

        if (_currentOperation.value != null && cachedResult.isNotEmpty()) {
            _result.value = deleteZeros(_result.value)
            callOperation(_currentOperation.value!!)
        }

        cachedResult = deleteZeros(_result.value)
        _result.value = ""

        setCurrentOperation(operation)
    }

    fun dotClicked() {
        if (_result.value.contains('.')) return
        _result.value += '.'
    }

    fun eqClicked() {
        if (_currentOperation.value != null) {
            if (_result.value.isEmpty()) {
                _result.value = cachedResult
            } else if (cachedResult.isNotEmpty()){
                _result.value = deleteZeros(_result.value)
                callOperation(_currentOperation.value!!)
                _currentOperation.value = null
            }
        }
        _currentOperation.value = null
        _result.value = deleteZeros(_result.value)
        cachedResult = ""
    }

    fun eraseClicked() {
        if (_result.value.isNotEmpty()) {
            _result.value = deleteZeros(_result.value.take(_result.value.length - 1))
        }
    }

    fun eraseLongClicked(): Boolean {
        _currentOperation.value = null
        cachedResult = ""
        _result.value = ""
        return true
    }

    private fun setCurrentOperation(operation: Operation) {
        when (operation) {
            Operation.PLUS -> _currentOperation.value = Operation.PLUS
            Operation.MINUS -> _currentOperation.value = Operation.MINUS
            Operation.MULTIPLY -> _currentOperation.value = Operation.MULTIPLY
            Operation.DIVIDE -> _currentOperation.value = Operation.DIVIDE
        }
    }

    private fun callOperation(operation: Operation) {
        when(operation) {
            Operation.PLUS -> add()
            Operation.MINUS -> subtract()
            Operation.MULTIPLY -> multiply()
            Operation.DIVIDE -> divide()
        }
    }

    private fun deleteZeros(newResult: String): String {
        if (newResult.length == 1 && newResult[0] == '-') return ""
        if (newResult.length == 1 && newResult[0] == '0') return newResult
        var newTrimmedResult = newResult.trimStart('0')
        if (newTrimmedResult.isEmpty()) return "0"
        if (newTrimmedResult.isNotEmpty() && newTrimmedResult[0] == '.') {
            newTrimmedResult = "0$newTrimmedResult"
        }
        return if (newTrimmedResult.contains('.')) {
            newTrimmedResult.trimEnd('0').trimEnd('.')
        } else {
            newTrimmedResult
        }
    }

    private fun add() {
        val resultDouble = _result.value.toDouble()
        val cachedResultDouble = cachedResult.toDouble()
        _result.value = deleteZeros(BigDecimal(cachedResultDouble + resultDouble).toPlainString())
    }

    private fun subtract() {
        val resultDouble = _result.value.toDouble()
        val cachedResultDouble = cachedResult.toDouble()
        _result.value = deleteZeros(BigDecimal(cachedResultDouble - resultDouble).toPlainString())
    }

    private fun multiply() {
        val resultDouble = _result.value.toDouble()
        val cachedResultDouble = cachedResult.toDouble()
        _result.value = deleteZeros(BigDecimal(cachedResultDouble * resultDouble).toPlainString())
    }

    private fun divide() {
        val resultDouble = _result.value.toDouble()
        val cachedResultDouble = cachedResult.toDouble()
        if (resultDouble == 0.0) {
            _result.value = ""
            cachedResult = ""
            viewModelScope.launch {
                _toast.emit("Do not divide by 0")
            }
        } else {
            _result.value = deleteZeros(BigDecimal(cachedResultDouble / resultDouble).toPlainString())
        }
    }
}