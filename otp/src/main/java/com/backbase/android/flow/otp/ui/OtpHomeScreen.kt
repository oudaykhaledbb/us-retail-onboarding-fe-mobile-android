package com.backbase.android.flow.otp.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.otp.R
import com.backbase.android.flow.otp.models.OtpChannel
import kotlinx.android.synthetic.main.screen_otp_home.*
import org.koin.android.ext.android.inject

const val VERIFICATION_TYPE = "VERIFICATION_TYPE"

class OtpHomeScreen : SecureFragment(R.layout.screen_otp_home) {
    private val viewModel: OtpViewModel by inject()
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewModelStates()
        viewModel.requestAvailableOtpChannels()
        navController = tryFindNavController(view)
        initViews()
    }

    private fun initViews() {
        btnSms.setOnClickListener {
            navigateToVerifactionScreen(OtpChannel.SMS)
        }
        btnEmail.setOnClickListener {
            navigateToVerifactionScreen(OtpChannel.EMAIL)
        }
    }

    private fun navigateToVerifactionScreen(channel: OtpChannel) {
        val bundle = bundleOf(VERIFICATION_TYPE to channel)
        navController?.navigate(R.id.action_otpHomeScreen_to_otpVerificationScreen, bundle)
    }

    private fun handleViewModelStates() {
        handleStates(
            viewModel.apiAvailableOtpChannels.state,
            onSuccess = {
                progressBar.visibility = View.GONE
                if (it.isNullOrEmpty()) {
                    homeHintContainer.showHintFailure(getString(R.string.otp_try_again_later))
                } else {
                    updateAvailableOtpChannels(it)
                }
            },
            onFailed = { ex ->
                progressBar.visibility = View.GONE
                ex.message?.let {
                    homeHintContainer.showHintFailure(it)
                }
            }
        )
    }

    private fun updateAvailableOtpChannels(channels: List<OtpChannel>) {
        channels.forEach { channel ->
            if (channels.size == 1) {
                navController?.popBackStack(R.id.otpHomeScreen, true)
                navigateToVerifactionScreen(channel)
                return
            }
            btnSms.visibility = View.VISIBLE
            btnEmail.visibility = View.VISIBLE
        }
    }

    private fun tryFindNavController(view: View): NavController? {
        try {
            return view.findNavController()
        } catch (ex: Exception) {
        }
        return null
    }
}