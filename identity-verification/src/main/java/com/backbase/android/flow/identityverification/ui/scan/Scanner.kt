package com.backbase.android.flow.identityverification.ui.scan

import androidx.fragment.app.DialogFragment
import com.jumio.core.data.document.ScanSide

const val ARG_SCAN_SIDE = "ARG_SCAN_SIDE"
const val ARG_SCAN_DOCUMENT = "ARG_SCAN_DOCUMENT"
const val ARG_SCAN_PROGRESS = "ARG_SCAN_PROGRESS"

fun getScanFragment(
    scanSide: String?,
    document: String?,
    progressText: String?
): DialogFragment {

    return if (ScanSide.valueOf(scanSide!!) == ScanSide.FACE){
        FaceScanScreen.newInstance(scanSide, document, progressText)
    } else{
        DocumentScanScreen.newInstance(scanSide, document, progressText)
    }
}