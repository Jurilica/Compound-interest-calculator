package hr.tvz.android.kalkulatorleljak

import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import hr.tvz.android.kalkulatorleljak.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.submitButton.setOnClickListener {
            clearErrors()
            if(validateInputs())
            {
                calculateResult()
            }
        }

        setTheme()
    }

    private fun clearErrors() {
        binding.startingAmountInput.error = null
        binding.yearsInput.error = null
        binding.monthlyAdditionsInput.error = null
        binding.yearlyInterestInput.error = null
    }

    private fun validateInputs() : Boolean {
        val isValidYearsInput = validateIntegerInput(binding.yearsInput)
        val isValidStaringAmountInput = validateIntegerInput(binding.startingAmountInput)
        val isValidMonthlyAdditionInput = validateIntegerInput(binding.monthlyAdditionsInput)
        val isValidInterestRateInput = validateDecimalInput(binding.yearlyInterestInput)
        if(!isValidYearsInput
            || !isValidStaringAmountInput
            || !isValidMonthlyAdditionInput
            || !isValidInterestRateInput) {
            return false
        }

        return true
    }

    private fun validateIntegerInput(input: EditText) : Boolean {
        val value = input.text.toString()

        if(value.isBlank()) {
            input.error = getString(R.string.empty_input)
            return false
        }

        val numberValue = value.toIntOrNull()
        if(numberValue == null || numberValue < 0) {
            input.error = getString(R.string.invalid_integer)
            return false
        }

        return true
    }

    private fun validateDecimalInput(input: EditText) : Boolean {
        val value = input.text.toString()

        if(value.isBlank()) {
            input.error = getString(R.string.empty_input)
            return false
        }

        val numberValue = value.toDoubleOrNull()
        if(numberValue == null || numberValue < 0 || numberValue > 100) {
            input.error = getString(R.string.invalid_percentage)
            return false
        }

        return true
    }

    private fun calculateResult() {
        val startingAmount = binding.startingAmountInput.text.toString().toDouble()
        val numberOfYears = binding.yearsInput.text.toString().toInt()
        val monthlyAdditions = binding.monthlyAdditionsInput.text.toString().toInt()
        val yearlyInterest = binding.yearlyInterestInput.text.toString().toDouble() / 100

        val numberOfMonths = 12

        var total: Double = startingAmount
        for (i in 1..(numberOfMonths * numberOfYears)) {
            val monthlyInterest = total * yearlyInterest / numberOfMonths
            total += monthlyInterest + monthlyAdditions
        }

        val df = DecimalFormat("#,###.00")
        binding.resultOutput.text = df.format(total)
    }

    private fun setTheme() {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {}
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }

        val isNightMode = mode == Configuration.UI_MODE_NIGHT_YES
        binding.themeInput.isChecked = isNightMode

        binding.themeInput.setOnClickListener {
            if (binding.themeInput.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}