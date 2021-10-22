package com.backbase.android.flow.identityverification.ui.scan

class JumioErrorCode(private val errorCode: String, val errorDescription: String ) {

    lateinit var decisionSource: DecisionSource
    lateinit var recoveryStatus: RecoveryStatus

    init {
        setupDecisionSource()
        setupRecoveryStatus()
    }

    private fun setupRecoveryStatus() {
        decisionSource = when(errorCode.toCharArray()[0].toLowerCase()){
            'a' -> DecisionSource.USER
            'b' -> DecisionSource.SYSTEM
            'c' -> DecisionSource.SYSTEM
            'e' -> DecisionSource.USER
            'f' -> DecisionSource.SYSTEM
            'g' -> DecisionSource.USER
            'h' -> DecisionSource.SYSTEM
            'i' -> DecisionSource.SYSTEM
            'j' -> DecisionSource.SYSTEM
            else -> DecisionSource.SYSTEM
        }
    }

    private fun setupDecisionSource() {
        recoveryStatus = when(errorCode.toCharArray()[0].toLowerCase()){
            'a' -> RecoveryStatus.RECOVERABLE
            'b' -> RecoveryStatus.NON_RECOVERABLE
            'c' -> RecoveryStatus.NON_RECOVERABLE
            'e' -> RecoveryStatus.RECOVERABLE
            'f' -> RecoveryStatus.NON_RECOVERABLE
            'g' -> RecoveryStatus.RECOVERABLE
            'h' -> RecoveryStatus.NON_RECOVERABLE
            'i' -> RecoveryStatus.NON_RECOVERABLE
            'j' -> RecoveryStatus.NON_RECOVERABLE
            else -> RecoveryStatus.NON_RECOVERABLE
        }
    }
}

enum class DecisionSource { USER, SYSTEM }
enum class RecoveryStatus { RECOVERABLE, NON_RECOVERABLE }