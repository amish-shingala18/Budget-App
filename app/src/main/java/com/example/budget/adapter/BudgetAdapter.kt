package com.example.budget.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.activity.AddEntryActivity
import com.example.budget.databinding.HomeSampleBinding
import com.example.budget.helper.BudgetEntity
import com.example.budget.helper.DbRoomHelper.Companion.db
import com.example.budget.helper.DbRoomHelper.Companion.initDb

class BudgetAdapter(private var list: MutableList<BudgetEntity>) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sampleBinding = HomeSampleBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_sample, parent, false)
        return BudgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        if (list[position].amountStatus==1){
            holder.sampleBinding.txtAmount.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }
        else{
            holder.sampleBinding.txtAmount.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
        }
        holder.sampleBinding.txtCategory.text = list[position].category
        holder.sampleBinding.txtTitle.text = list[position].title
        holder.sampleBinding.txtAmount.text = "₹${list[position].amount}"
        holder.sampleBinding.txtDate.text = list[position].date
        val budgetEntity = BudgetEntity(
            list[position].id,
            list[position].category,
            list[position].title,
            list[position].amount,
            list[position].date,
            list[position].amountStatus
        )
        holder.sampleBinding.imgDelete.setOnClickListener {
            initDb(holder.itemView.context)
            list.removeAt(position)
            db!!.dao().deleteData(budgetEntity)
            notifyDataSetChanged()
        }
        holder.sampleBinding.cvSample.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddEntryActivity::class.java)
            intent.putExtra("id", list[position].id)
            intent.putExtra("category", list[position].category)
            intent.putExtra("title", list[position].title)
            intent.putExtra("amount", list[position].amount)
            intent.putExtra("date", list[position].date)
            intent.putExtra("amountStatus", list[position].amountStatus)
            holder.itemView.context.startActivity(intent)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(l1: MutableList<BudgetEntity>) {
        list=l1
        notifyDataSetChanged()

    }
}