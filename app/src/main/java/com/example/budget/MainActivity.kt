

package com.example.budget

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
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

@Suppress("SENSELESS_COMPARISON")
class MainActivity : AppCompatActivity() {
    private var toDate: String=""
    private var fromDate: String=""
    private val calendar = Calendar.getInstance()
    private lateinit var binding : ActivityMainBinding
    private var readList = mutableListOf<BudgetEntity>()
    private var filterList = listOf<BudgetEntity>()
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
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initClick(){
//        val sdf = SimpleDateFormat("dd/MM/yyyy")
//        fromDate = sdf.format(System.currentTimeMillis())
//        binding.txtFromDate.text = fromDate
//
//        toDate = sdf.format(System.currentTimeMillis())
//        binding.txtToDate.text = toDate
        binding.txtFromDate.text = "Select Date"
        binding.txtToDate.text = "Select Date"

        binding.txtApply.setOnClickListener {
            initDb(this)
            readList=db!!.dao().getDate(fromDate,toDate)
            amountStatus()
            budgetAdapter.dataSetChanged(readList)
        }
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEntryActivity::class.java))
        }
        binding.cvFromDate.setOnClickListener {
            fromDatePicker()
        }
        binding.cvToDate.setOnClickListener {
            toDatePicker()
        }
        binding.svCategory.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterList = readList.filter { budgetEntity ->
                    budgetEntity.category.lowercase(Locale.getDefault()).
                    contains(newText.lowercase(Locale.getDefault()))
                }
                budgetAdapter.search(filterList)
                return false
            }
        })
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
            binding.txtFromDate.text = fromDate
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    private fun toDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            toDate = dateFormat.format(selectedDate.time)
            binding.txtToDate.text = toDate
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        initDb(this)
        readList=db!!.dao().getData()
        val expenseAmount = db!!.dao().getExpense()
        binding.txtExpenseAmount.text = "₹${expenseAmount}"
        if (expenseAmount == null) {
            binding.txtExpenseAmount.text = "₹0"
        }
        val incomeAmount = db!!.dao().getIncome()
        binding.txtIncomeAmount.text = "₹${incomeAmount}"
        if (incomeAmount == null) {
            binding.txtIncomeAmount.text = "₹0"
        }
        budgetAdapter.dataSetChanged(readList)
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun amountStatus() {
        val expenseAmount = db!!.dao().expenseRead(fromDate, toDate)
        binding.txtExpenseAmount.text = "₹${expenseAmount}"
        if (expenseAmount == null) {
            binding.txtExpenseAmount.text = "₹0"
        }
        val incomeAmount = db!!.dao().incomeRead(fromDate, toDate)
        binding.txtIncomeAmount.text = "₹${incomeAmount}"
        if (incomeAmount == null) {
            binding.txtIncomeAmount.text = "₹0"
        }
    }
}