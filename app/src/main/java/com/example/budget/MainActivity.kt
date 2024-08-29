package com.example.budget

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budget.activity.AddEntryActivity
import com.example.budget.adapter.BudgetAdapter
import com.example.budget.databinding.ActivityMainBinding
import com.example.budget.helper.BudgetEntity
import com.example.budget.helper.DbRoomHelper.Companion.db
import com.example.budget.helper.DbRoomHelper.Companion.initDb
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var toDate: String=""
    private var fromDate: String=""
    private val calendar = Calendar.getInstance()
    private lateinit var binding : ActivityMainBinding
    private var readList = mutableListOf<BudgetEntity>()
    private lateinit var budgetAdapter: BudgetAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initClick()
        initAdapter()
    }
    private fun initClick(){
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEntryActivity::class.java))
        }
        binding.cvFromDate.setOnClickListener {
            fromDatePicker()
        }
        binding.cvToDate.setOnClickListener {
            toDatePicker()
        }
    }
    private fun initAdapter(){
        budgetAdapter = BudgetAdapter(readList)
        binding.rvBudget.adapter = budgetAdapter
    }
    private fun fromDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            fromDate = dateFormat.format(selectedDate.time)
            binding.fromDate.text = fromDate
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    private fun toDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            toDate= dateFormat.format(selectedDate.time)
            binding.toDate.text = toDate
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    override fun onResume() {
        initDb(this)
        readList=db!!.dao().getData()
        budgetAdapter.dataSetChanged(readList)
        super.onResume()
    }
}