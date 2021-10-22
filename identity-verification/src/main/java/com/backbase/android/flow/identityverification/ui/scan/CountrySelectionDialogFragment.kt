package com.backbase.android.flow.identityverification.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.backbase.android.flow.common.fragment.SecureDialogFragment
import com.backbase.android.flow.identityverification.R
import com.backbase.android.flow.identityverification.ui.CountriesCode
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jakewharton.rxbinding3.widget.textChanges
import com.jumio.nv.custom.NetverifyCountry
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_fragment_country_selection.*

class CountrySelectionDialogFragment : SecureDialogFragment() {

    private var selectedNetverifyCountry: NetverifyCountry? = null
    private var title: String? = null
    private var countriesList = ArrayList<NetverifyCountry>()
    private var onRadioButtonSelectedListener: ((NetverifyCountry) -> Unit)? = null

    fun setTitle(title: String?): CountrySelectionDialogFragment {
        this.title = title
        if (isAdded) lblTitle.text = title
        return this
    }

    fun setCountriesList(countriesList: ArrayList<NetverifyCountry>): CountrySelectionDialogFragment {
        this.countriesList = countriesList
        if (isAdded)(rvCountries.adapter as CountrySelectionRecyclerViewAdapter).notifyDataSetChanged()
        return this
    }

    fun setSelection(selectedNetverifyCountry: NetverifyCountry?): CountrySelectionDialogFragment {
        this.selectedNetverifyCountry = selectedNetverifyCountry
        return this
    }

    fun setOnRadioButtonSelectedListener(onRadioButtonSelectedListener: ((NetverifyCountry) -> Unit)): CountrySelectionDialogFragment {
        this.onRadioButtonSelectedListener = {
            onRadioButtonSelectedListener?.invoke(it)
            dismissAllowingStateLoss()
        }
        if (isAdded)(rvCountries.adapter as CountrySelectionRecyclerViewAdapter).onRadioButtonSelectedListener = this.onRadioButtonSelectedListener
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_country_selection, container, false)
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCountriesList()
        lblTitle.text = title
        (rvCountries.adapter as CountrySelectionRecyclerViewAdapter)
            .setupSearchText(txtSearch)
            .onRadioButtonSelectedListener = onRadioButtonSelectedListener
    }

    private fun setupCountriesList() {
        rvCountries.layoutManager = LinearLayoutManager(requireContext())
        rvCountries.adapter = CountrySelectionRecyclerViewAdapter(countriesList)
            .setSelection(this.selectedNetverifyCountry)
    }

}

private class CountrySelectionRecyclerViewAdapter(
    private var originalData: ArrayList<NetverifyCountry>
) :
    RecyclerView.Adapter<CountrySelectionRecyclerViewAdapter.ViewHolder>() {

    private var NetverifyCountrys = ArrayList<NetverifyCountry>()
    var onRadioButtonSelectedListener: ((NetverifyCountry) -> Unit)? = null
    private var selectedNetverifyCountry: NetverifyCountry? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        this.NetverifyCountrys.addAll(originalData)
    }


    fun setSelection(selectedNetverifyCountry: NetverifyCountry?): CountrySelectionRecyclerViewAdapter {
        this.selectedNetverifyCountry = selectedNetverifyCountry
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_country_selection, parent, false)
        return ViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return NetverifyCountrys.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(NetverifyCountrys[position])
    }

    fun onCountrySelected(NetverifyCountry: NetverifyCountry) {
        onRadioButtonSelectedListener?.invoke(NetverifyCountry)
    }

    fun setupSearchText(editText: EditText): CountrySelectionRecyclerViewAdapter {
        compositeDisposable.add(editText.textChanges().subscribe {
            this.NetverifyCountrys.clear()
            if (it.isNullOrEmpty()) {
                this.NetverifyCountrys.addAll(originalData)
            } else {
                originalData.forEach { opt ->
                    if (opt.countryName().toLowerCase().contains(it.toString().toLowerCase())) {
                        this.NetverifyCountrys.add(opt)
                    }
                }
            }
            notifyDataSetChanged()
        })
        return this
    }

    private class ViewHolder(
        itemView: View,
        val adapter: CountrySelectionRecyclerViewAdapter
    ) : RecyclerView.ViewHolder(itemView) {

        private var lblLabel: TextView = itemView.findViewById(R.id.lblLabel)
        private var radioButton: MaterialRadioButton = itemView.findViewById(R.id.radioButton)

        fun bind(NetverifyCountry: NetverifyCountry) {
            lblLabel.text = NetverifyCountry.countryName()
            radioButton.isSelected = adapter.selectedNetverifyCountry == NetverifyCountry
            lblLabel.setOnClickListener {
                onRadioButtonSelected(NetverifyCountry)
            }
        }

        private fun onRadioButtonSelected(
            NetverifyCountry: NetverifyCountry
        ) {
            adapter.selectedNetverifyCountry = NetverifyCountry
            adapter.notifyDataSetChanged()
            adapter.onCountrySelected(NetverifyCountry)
        }

    }

}

fun NetverifyCountry.countryName(): String {
    return CountriesCode.data[this.isoCode].toString()
}