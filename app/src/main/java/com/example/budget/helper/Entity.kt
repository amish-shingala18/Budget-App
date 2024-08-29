package com.example.budget.helper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class BudgetEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    var category: String,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var amount: String,
    @ColumnInfo
    var date: String,
    @ColumnInfo
    var amountStatus: Int=0
)