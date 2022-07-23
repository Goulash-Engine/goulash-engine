package com.barbarus.prosper.logic

import com.barbarus.prosper.actor.logic.ConditionLogic
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.factories.ClanFactory
import spock.lang.Specification

class ConditionLogicSpec extends Specification {
    def "should simulate exhaustion conditions by the rest urge"() {
        given:
        ConditionLogic conditionLogic = new ConditionLogic()
        Clan clan = new ClanFactory().poorGathererClan()
        clan.urges.getUrges().put("rest", urgeToRest.doubleValue())
        conditionLogic.process(clan)

        expect:
        clan.conditions.contains(expectedCondition)

        where:
        urgeToRest || expectedCondition
        100.0      || "unconscious"
        81.0       || "blacking out"
        51.0       || "exhausted"
        31.0       || "tired"
        21.0       || "weary"
    }

    def "should simulate health conditions impairment by health state"() {
        given:
        ConditionLogic conditionLogic = new ConditionLogic()
        Clan clan = new ClanFactory().poorGathererClan()
        clan.state.health = givenHealth
        conditionLogic.process(clan)

        expect:
        clan.conditions.contains(expectedCondition)

        where:
        givenHealth || expectedCondition
        0.0         || "dead"
        9.0         || "dying"
        19.0        || "severely sick"
        39.0        || "very sick"
        49.0        || "sick"
        69.0        || "dizzy"
        89.0        || "unwell"
    }
}