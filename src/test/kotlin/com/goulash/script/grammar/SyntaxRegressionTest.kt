package com.goulash.script.grammar

import assertk.assertions.contains

/**
 * This test is a whitebox test that asserts functionality from script data to the
 * effects on the context.
 */
internal class SyntaxRegressionTest {
    private val containerScriptGrammar = ContainerScriptGrammar()
    private val activityScriptGrammar = ActivityScriptGrammar()
}
