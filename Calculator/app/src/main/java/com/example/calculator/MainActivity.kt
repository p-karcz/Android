package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.calculator.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setListeners()
        setObservers()
        setContentView(binding.root)
    }

    private fun setObservers() {
        observeResult()
        observeToast()
        observeCurrentOperation()
    }

    private fun setListeners() {
        setNumbersListeners()
        setOperationsListeners()
        setLongClick()
    }

    private fun setNumbersListeners() {
        binding.button0.setOnClickListener {
            viewModel.numberClicked('0')
        }
        binding.button1.setOnClickListener {
            viewModel.numberClicked('1')
        }
        binding.button2.setOnClickListener {
            viewModel.numberClicked('2')
        }
        binding.button3.setOnClickListener {
            viewModel.numberClicked('3')
        }
        binding.button4.setOnClickListener {
            viewModel.numberClicked('4')
        }
        binding.button5.setOnClickListener {
            viewModel.numberClicked('5')
        }
        binding.button6.setOnClickListener {
            viewModel.numberClicked('6')
        }
        binding.button7.setOnClickListener {
            viewModel.numberClicked('7')
        }
        binding.button8.setOnClickListener {
            viewModel.numberClicked('8')
        }
        binding.button9.setOnClickListener {
            viewModel.numberClicked('9')
        }
    }

    private fun setOperationsListeners() {
        binding.buttonPlus.setOnClickListener {
            viewModel.operationClicked(Operation.PLUS)
        }
        binding.buttonMinus.setOnClickListener {
            viewModel.operationClicked(Operation.MINUS)
        }
        binding.buttonMultiply.setOnClickListener {
            viewModel.operationClicked(Operation.MULTIPLY)
        }
        binding.buttonDivide.setOnClickListener {
            viewModel.operationClicked(Operation.DIVIDE)
        }
        binding.buttonDot.setOnClickListener {
            viewModel.dotClicked()
        }
        binding.buttonEq.setOnClickListener {
            viewModel.eqClicked()
        }
        binding.buttonErase.setOnClickListener {
            viewModel.eraseClicked()
        }
    }

    private fun setLongClick() {
        binding.buttonErase.setOnLongClickListener {
            viewModel.eraseLongClicked()
        }
    }

    private fun observeResult() {
        lifecycleScope.launchWhenStarted {
            viewModel.result.collectLatest {
                binding.result.text = it
            }
        }
    }

    private fun observeCurrentOperation() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentOperation.collectLatest {
                resetButtonsBackground()
                when (it) {
                    Operation.PLUS -> binding.buttonPlus.setBackgroundColor(
                        getColor(R.color.design_default_color_secondary_variant)
                    )
                    Operation.MINUS -> binding.buttonMinus.setBackgroundColor(
                        getColor(R.color.design_default_color_secondary_variant)
                    )
                    Operation.MULTIPLY -> binding.buttonMultiply.setBackgroundColor(
                        getColor(R.color.design_default_color_secondary_variant)
                    )
                    Operation.DIVIDE -> binding.buttonDivide.setBackgroundColor(
                        getColor(R.color.design_default_color_secondary_variant)
                    )
                }
            }
        }
    }

    private fun resetButtonsBackground() {
        with (binding) {
            this.buttonPlus.setBackgroundColor(getColor(R.color.design_default_color_primary))
            this.buttonMinus.setBackgroundColor(getColor(R.color.design_default_color_primary))
            this.buttonMultiply.setBackgroundColor(getColor(R.color.design_default_color_primary))
            this.buttonDivide.setBackgroundColor(getColor(R.color.design_default_color_primary))
        }
    }

    private fun observeToast() {
        lifecycleScope.launchWhenStarted {
            viewModel.toast.collectLatest {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}