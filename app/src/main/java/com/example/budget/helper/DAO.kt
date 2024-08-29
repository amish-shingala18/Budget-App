package com.example.budget.helper

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
}