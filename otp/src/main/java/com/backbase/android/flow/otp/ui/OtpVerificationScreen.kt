package com.backbase.android.flow.otp.ui

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.model.StepConfiguration
import com.backbase.android.flow.common.uicomponents.header.HeaderInfoProvider
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.OtpRouter
import com.backbase.android.flow.otp.R
import com.backbase.android.flow.otp.models.OtpChannel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_otp_verification.*
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class OtpVerificationScreen : SecureFragment(R.layout.screen_otp_verification), HeaderInfoProvider {

    private val viewModel: OtpViewModel by inject()
    private val router: OtpRouter by inject()
    private val configuration: OtpConfiguration by inject()
    private var navController: NavController? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var verificationType: OtpChannel
    private val phoneVerificationPattern = Pattern.compile(configuration.phoneVerificationPattern)
    private val recipient: String
        get() = when (verificationType) {
            OtpChannel.SMS -> "${txtCountryCode.text}${txtPhoneNumber.text}"
            OtpChannel.EMAIL -> txtEmailAddress.text.toString()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = tryFindNavController(view)
        verificationType = arguments?.get(VERIFICATION_TYPE) as OtpChannel
        handleViewModelStates()
        initViews()
    }

    private fun initViews() {
        compositeDisposable.add(txtPhoneNumber.textChanges().subscribe {
            val matcher = phoneVerificationPattern.matcher("${txtCountryCode.text}${it}")
            showSendVerificationButton(matcher.matches())
        })
        compositeDisposable.add(txtVerificationCode.textChanges().subscribe {
            btnContinue.isEnabled = it.length == configuration.verificationCodeMaxLength.toInt()
        })
        initVerificationViews()
        txtVerificationCode.filters += InputFilter.LengthFilter(configuration.verificationCodeMaxLength.toInt())
        lblResend.setOnClickListener {
            clResendArea.visibility = View.VISIBLE
            clVerificationArea.visibility = View.GONE
            txtVerificationCode.setText("")
            requestVerificationCode(recipient)
        }
        btnRequestVerificationCodeButton.setOnClickListener { requestVerificationCode(recipient) }
        btnContinue.setOnClickListener { submitVerificationCode(recipient) }
        configuration.fetchOtpEmailActionName?.let { viewModel.fetchEmail() }
    }

    private fun initVerificationViews() {
        var descriptionTextDetail = ""
        when (verificationType) {
            OtpChannel.SMS -> {
                imgVerificationIcon.setBackgroundResource(R.drawable.ic_phone)
                descriptionTextDetail = getString(R.string.otp_label_phone_number).toLowerCase()
                containerMobileNumber.visibility = View.VISIBLE
                containerEmailAddress.visibility = View.GONE
            }
            OtpChannel.EMAIL -> {
                imgVerificationIcon.setBackgroundResource(R.drawable.ic_mail)
                StepConfiguration.model?.email?.let {
                    txtEmailAddress.setText(it)
                }
                btnRequestVerificationCodeButton.isEnabled = true
                descriptionTextDetail = getString(R.string.otp_label_email_address).toLowerCase()
                containerMobileNumber.visibility = View.GONE
                containerEmailAddress.visibility = View.VISIBLE
            }
        }
        lblNote.text = getString(R.string.otp_verification_description, descriptionTextDetail)
    }

    private fun submitVerificationCode(recipient: String) {
        viewModel.submitVerificationCode(
                recipient,
                verificationType,
                txtVerificationCode.text.toString()
        )
    }

    private fun requestVerificationCode(recipient: String) {
        viewModel.requestVerificationCode(recipient, verificationType)
    }

    private fun handleViewModelStates() {
        val views = arrayOf(
                txtVerificationCode,
                txtPhoneNumber,
                lblResend
        )

        handleStates(
                viewModel.apiRequestEmail.state,
                onSuccess = { email ->
                    email?.let{ emailString ->
                        txtEmailAddress.setText(maskEmail(emailString))
                    }
                },
                onFailed = { ex ->
                    ex.message?.let {
                        hintContainer.showHintFailure(it)
                    }
                },
                onBlockUI = {
                    btnRequestVerificationCodeButton.loading = true
                },
                onUnblockUI = {
                    btnRequestVerificationCodeButton.loading = false
                },
                actionViews = views
        )


        handleStates(
                viewModel.apiRequestVerificationCode.state,
                onSuccess = {
                    showVerificationInput()
                },
                onFailed = { ex ->
                    ex.message?.let { e ->
                        hintContainer.showHintFailure(e)
                    }
                },
                onBlockUI = {
                    btnRequestVerificationCodeButton.loading = true
                },
                onUnblockUI = {
                    btnRequestVerificationCodeButton.loading = false
                },
                actionViews = views
        )

        handleStates(
                viewModel.apiSubmitVerificationCode.state,
                onSuccess = {
                    router.onOtpValidated(it)
                },
                onFailed = { ex ->
                    ex.message?.let {
                        verificationCodeInputLayout.error = it
                    }
                },
                onBlockUI = {
                    btnContinue.loading = true
                    hintContainer.removeHint()
                },
                onUnblockUI = {
                    btnContinue.loading = false
                    hintContainer.removeHint()
                    verificationCodeInputLayout.error = null
                },
                actionViews = views
        )
    }

    private fun maskEmail(email: String):String {
        if (email.isNullOrEmpty()) return email
        val lastPositionToMask = ((email.split("@")[0].length)/2)
        return "***${email.substring(lastPositionToMask, email.length)}"
    }

    private fun showSendVerificationButton(isEnabled: Boolean) {
        btnRequestVerificationCodeButton.isEnabled = isEnabled
        btnRequestVerificationCodeButton.visibility = View.VISIBLE
        clVerificationArea.visibility = View.GONE
        clResendArea.visibility = View.GONE
        btnContinue.visibility = View.GONE
    }

    private fun showVerificationInput() {
        btnRequestVerificationCodeButton.visibility = View.GONE
        clVerificationArea.visibility = View.VISIBLE
        clResendArea.visibility = View.GONE
        btnContinue.visibility = View.VISIBLE
    }

    private fun tryFindNavController(view: View): NavController? {
        try {
            return view.findNavController()
        } catch (ex: Exception) {
        }
        return null
    }

    override fun getTitle() = getString(R.string.otp_verification_screen_title)
    override fun getSubtitle() = getString(R.string.otp_verification_screen_subtitle)
}
