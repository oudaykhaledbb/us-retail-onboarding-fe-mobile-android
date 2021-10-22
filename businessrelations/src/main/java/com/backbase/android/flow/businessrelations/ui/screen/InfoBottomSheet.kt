package com.backbase.android.flow.businessrelations.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.findRole
import com.backbase.android.flow.businessrelations.model.fullName
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_info.*

private const val TAG_OWNER = "TAG_OWNER"

class InfoBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        requireArguments().getParcelable<Owner>(TAG_OWNER)?.apply {
            lblOwnerName.text = fullName()
            lblInfo.text = "$email\n" +
                    "+1 $phone\n" +
                    "${findRole()}\n" +
                    "$ownershipPercentage% ownership\n"
        }
    }

    companion object {
        fun newInstance(owner: Owner) = InfoBottomSheet().apply {
            arguments = Bundle().apply { putParcelable(TAG_OWNER, owner) }
        }
    }

}