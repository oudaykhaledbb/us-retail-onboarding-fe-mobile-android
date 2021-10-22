package com.backbase.android.flow.productselector.core

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RepeatRule : TestRule {
    override fun apply(statement: Statement, description: Description): Statement {
        val repeat: Repeat = description.getAnnotation(Repeat::class.java)
        val times: Int = repeat.value
        return RepeatStatement(statement, times)
    }
}

private class RepeatStatement(private val statement: Statement, private val repeat: Int) : Statement() {

    @Throws(Throwable::class) override fun evaluate() {
        for (i in 0 until repeat) {
            statement.evaluate()
        }
    }
}

@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE_PARAMETER)
annotation class Repeat(val value: Int = 1)
