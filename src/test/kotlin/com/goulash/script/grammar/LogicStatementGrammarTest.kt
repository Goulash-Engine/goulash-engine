package com.goulash.script.grammar

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.goulash.script.domain.ScriptStatement
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class LogicStatementGrammarTest {
    private val logicStatementGrammar = LogicStatementGrammar()

    @Test
    fun `should parse statement with filter and no filter mutation operation (oneline)`() {
        val scriptData =
            "actors[state.health>1]::urge(eat).plus(1);actors::urge(eat).plus(2);".trim()

        val statements: List<ScriptStatement> = logicStatementGrammar.parseToEnd(scriptData)

        assertThat(statements).hasSize(2)
        assertThat(statements[0].mutationType).isEqualTo("urge")
        assertThat(statements[0].filter).isEqualTo("state.health>1")
        assertThat(statements[0].mutationTarget).isEqualTo("eat")
        assertThat(statements[0].mutationOperation).isEqualTo("plus")
        assertThat(statements[0].mutationOperationArgument).isEqualTo("1")
        assertThat(statements[1].mutationType).isEqualTo("urge")
        assertThat(statements[1].filter).isEqualTo("")
        assertThat(statements[1].mutationTarget).isEqualTo("eat")
        assertThat(statements[1].mutationOperation).isEqualTo("plus")
        assertThat(statements[1].mutationOperationArgument).isEqualTo("2")
    }
}
