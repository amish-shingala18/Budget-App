package com.example.budget.helper

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Insert
    fun insertData(entity: BudgetEntity)
    @Update
    fun updateData(entity: BudgetEntity)
    @Delete
    fun deleteData(entity: BudgetEntity)
    @Query("SELECT * FROM budget_table")
    fun getData(): MutableList<BudgetEntity>
    @Query("SELECT * FROM budget_table WHERE date BETWEEN :fromDate AND :toDate")
    fun getDate(fromDate: String, toDate: String): MutableList<BudgetEntity>


    //SELECT Date,SUM(Deposit_Amount) AS Deposit_Amount,Date FROM Deposit WHERE (Date BETWEEN @From AND @ToDate) GROUP BY Date
//    @Query("SELECT SUM(amount) FROM budget_table WHERE amountStatus==1 AND date BETWEEN :fromDate AND :toDate")
    @Query("SELECT SUM(amount) FROM budget_table WHERE amountStatus==1 AND date BETWEEN :fromDate AND :toDate")
    fun expenseRead(fromDate: String, toDate: String):String
//    @Query("SELECT date, SUM(amount) AS amount FROM budget_table WHERE date BETWEEN :fromDate AND :toDate GROUP BY date")
//    fun expenseRead(fromDate: String, toDate: String):String
    @Query("SELECT SUM(amount) FROM budget_table WHERE amountStatus==2 AND date BETWEEN :fromDate AND :toDate")
    fun incomeRead(fromDate: String, toDate: String):String
}