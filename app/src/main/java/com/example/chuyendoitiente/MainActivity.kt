package com.example.chuyendoitiente

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Các biến cho EditText và Spinner
    private lateinit var sourceAmount: EditText
    private lateinit var destinationAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var destinationCurrency: Spinner

    // Tỷ lệ chuyển đổi giả định (tỷ giá)
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "VND" to 23000.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Liên kết giao diện với các biến Kotlin
        sourceAmount = findViewById(R.id.sourceAmount)
        destinationAmount = findViewById(R.id.destinationAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        destinationCurrency = findViewById(R.id.destinationCurrency)

        // Tạo danh sách các đồng tiền
        val currencies = arrayListOf("USD", "EUR", "VND")

        // Gán danh sách các đồng tiền vào Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceCurrency.adapter = adapter
        destinationCurrency.adapter = adapter

        // Lắng nghe sự thay đổi trong EditText và Spinner
        setupListeners()
    }

    // Thiết lập các listener để cập nhật khi có thay đổi
    private fun setupListeners() {
        // Lắng nghe sự thay đổi số tiền nguồn
        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateConversion()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Lắng nghe sự thay đổi đồng tiền nguồn
        sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Lắng nghe sự thay đổi đồng tiền đích
        destinationCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        destinationAmount.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                updateConversion()
//            }
//            override fun afterTextChanged(s: Editable?) {}
//        })


        //************************************************************
        // Lắng nghe khi người dùng nhấn vào EditText để chọn nguồn và đích
        sourceAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // EditText này là nguồn
//                sourceCurrency.isEnabled = true
//                destinationCurrency.isEnabled = false
                destinationAmount.isEnabled=false
                sourceAmount.isEnabled=true
            }
        }

        destinationAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // EditText này là đích
//                sourceCurrency.isEnabled = false
//                destinationCurrency.isEnabled = true
                sourceAmount.isEnabled=false
                destinationAmount.isEnabled=true
            }
        }
    }

    // Hàm tính toán chuyển đổi tiền tệ
    private fun updateConversion() {
        val sourceCurrencyCode = sourceCurrency.selectedItem.toString()
        val destinationCurrencyCode = destinationCurrency.selectedItem.toString()

        // Lấy số tiền nguồn
        val sourceAmountText = sourceAmount.text.toString()
        if (sourceAmountText.isEmpty()) {
            destinationAmount.setText("")
            return
        }

        // Chuyển đổi tiền tệ
        val amount = sourceAmountText.toDouble()
        val sourceRate = exchangeRates[sourceCurrencyCode] ?: 1.0
        val destinationRate = exchangeRates[destinationCurrencyCode] ?: 1.0

        // Tính toán kết quả
        val convertedAmount = amount * destinationRate / sourceRate

        // Cập nhật EditText kết quả
        destinationAmount.setText(String.format("%.2f", convertedAmount))
    }
}