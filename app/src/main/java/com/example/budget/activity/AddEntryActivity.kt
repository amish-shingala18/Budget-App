package com.example.budget.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budget.R
import com.example.budget.databinding.ActivityAddEntryBinding
import com.example.budget.helper.BudgetEntity
import com.example.budget.helper.DbRoomHelper.Companion.db
import com.example.budget.helper.DbRoomHelper.Companion.initDb
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEntryActivity : AppCompatActivity() {
    private var getId:Int=-1
    private var amountStatus = 1
    private val calendar = Calendar.getInstance()
    private var entryCurrentDate = ""
    private lateinit var binding : ActivityAddEntryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getUpdateData()
        initClick()
    }
    @SuppressLint("SetTextI18n")
    private fun initClick(){
        Log.e("TAG", "initClick: $getId")
        if(getId!=-1) {
            binding.txtEntry.text = "Update Entry"
            binding.btnSubmit.text = "Update"
        }
        budgetStatus()
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnSubmit.setOnClickListener {
            addOrUpdateData()
        }
        binding.txtDateLayout.setOnClickListener {
            datePicker()
        }
    }
    private fun addOrUpdateData(){
        val title = binding.edtTitle.text.toString()
        val category = binding.edtCategory.text.toString()
        val amount = binding.edtAmount.text.toString()
        val date = binding.txtDate.text.toString()
        if(category.isEmpty()){
            binding.txtCategoryLayout.error = "Please enter category"
        }
        else if(title.isEmpty()){
            binding.txtTitleLayout.error = "Please enter title"
        }
        else if(amount.isEmpty()) {
            binding.txtAmountLayout.error = "Please enter amount"
        }
        else{
            val budgetEntity = BudgetEntity(
                id=getId,
                category=category,
                title=title,
                amount=amount,
                date=date,
                amountStatus=amountStatus
            )
            if(getId==-1){
                initDb(this)
                db!!.dao().insertData(budgetEntity)
            }
            else{
                initDb(this)
                db!!.dao().updateData(budgetEntity)
            }
        }
    }
    private fun datePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            entryCurrentDate = dateFormat.format(selectedDate.time)
            binding.txtDate.text = entryCurrentDate
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    private fun budgetStatus() {
        binding.rgStatus.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbExpense -> {
                    amountStatus = 1
                }
                R.id.rbIncome -> {
                    amountStatus = 2
                }
            }
        }
    }
    private fun getUpdateData(){
        val intent=intent
        getId = intent.getIntExtra("id",-1)
        val category = intent.getStringExtra("category")
        val title = intent.getStringExtra("title")
        val amount = intent.getStringExtra("amount")
        val date = intent.getStringExtra("date")
        amountStatus = intent.getIntExtra("amountStatus", 1)
        if(amountStatus==1){
            binding.rbExpense.isChecked = true
        }
        else {
            binding.rbIncome.isChecked = true
        }
        binding.edtCategory.setText(category)
        binding.edtTitle.setText(title)
        binding.edtAmount.setText(amount)
        binding.txtDate.text = date
    }
}