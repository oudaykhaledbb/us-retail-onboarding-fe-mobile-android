package com.backbase.lookup.business_structure.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.backbase.lookup.R
import com.backbase.lookup.business_structure.module.BusinessModel
import kotlinx.android.synthetic.main.row_company.view.*

class CompanyRecyclerViewAdapter :
    RecyclerView.Adapter<CompanyRecyclerViewAdapter.CompanyViewHolder>() {

    private val companies = ArrayList<BusinessModel>()
    internal var selectedCompany: BusinessModel? = null

    var onSelectionChanged: ((selectedCompany: BusinessModel?) -> Unit)? = null

    fun setData(companies: List<BusinessModel>): CompanyRecyclerViewAdapter {
        this.companies.clear()
        this.companies.addAll(companies)
        notifyDataSetChanged()
        return this
    }

    inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(company: BusinessModel) {
            itemView.lblCompanyName.text = company.name
            itemView.lblCompanyStructure.text = company.structure
            itemView.lblCompanyDescription.text = company.address?.replace(",", "\n")
            setupVisibilityBasedOnContent(itemView.lblCompanyName, itemView.lblCompanyStructure, itemView.lblCompanyDescription)
            itemView.rbCompany.setOnCheckedChangeListener{_, _ -> }
            itemView.rbCompany.isChecked = company == selectedCompany
            itemView.rbCompany.setOnCheckedChangeListener{_, isChecked ->
                onSelectionChanged(isChecked, company)
            }
            itemView.setOnClickListener {
                onSelectionChanged(true, company)
            }
        }

        private fun setupVisibilityBasedOnContent(vararg textViews: TextView){
            textViews.forEach {
                it.visibility = if (it.text.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }

    }

    private fun onSelectionChanged(isChecked: Boolean, company: BusinessModel) {
        selectedCompany = if (isChecked) company else null
        notifyDataSetChanged()
        onSelectionChanged?.invoke(selectedCompany)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_company,
                parent,
                false
            )
        )

    override fun getItemCount() = companies.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(companies[position])
    }

}