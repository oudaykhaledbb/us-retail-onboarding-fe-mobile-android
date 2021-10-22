package com.backbase.lookup.business_structure.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.backbase.lookup.R
import com.backbase.lookup.business_structure.module.BusinessModel
import com.backbase.lookup.business_structure.ui.CompanyRecyclerViewAdapter
import kotlinx.android.synthetic.main.screen_select_business.*

private const val TAG_BUSINESSES = "TAG_BUSINESSES"

class SelectBusinessScreen : DialogFragment() {

    private var businesses: ArrayList<BusinessModel>? = null
    internal var selectedCompany: BusinessModel? = null
    private var onCompanySelectedListener : OnCompanySelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        businesses = requireArguments().getParcelableArrayList<BusinessModel>(TAG_BUSINESSES)
        return inflater.inflate(R.layout.screen_select_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        businesses?.let { fillCompanyData(it) }
        btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        btnSelectBusiness.setOnClickListener {
            dismissAllowingStateLoss()
            onCompanySelectedListener?.onCompanySelected(selectedCompany)
        }
        imgClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun getTheme() =
        R.style.Theme_Backbase_Fullscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_FRAME,
            R.style.FullScreenDialog
        )
    }

    private fun fillCompanyData(businesses: List<BusinessModel>){
        rvCompanies.layoutManager = LinearLayoutManager(requireContext())
        rvCompanies.adapter = CompanyRecyclerViewAdapter().apply {
            setData(businesses)
            onSelectionChanged = {company: BusinessModel? ->
                btnSelectBusiness.isEnabled = selectedCompany != null
                this@SelectBusinessScreen.selectedCompany = company
            }
        }

    }

    companion object{
        fun getInstance(listener : OnCompanySelectedListener, businesses: List<BusinessModel>?) = SelectBusinessScreen().apply {
            this.onCompanySelectedListener = listener
            arguments = Bundle().apply {
                putParcelableArrayList(TAG_BUSINESSES, ArrayList(businesses?: arrayListOf()))
            }
        }
    }

}

interface OnCompanySelectedListener{
    fun onCompanySelected(business: BusinessModel?)
}