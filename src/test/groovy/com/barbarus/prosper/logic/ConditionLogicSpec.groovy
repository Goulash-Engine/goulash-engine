package com.barbarus.prosper.logic

import com.barbarus.prosper.actor.logic.ConditionLogic
import com.barbarus.prosper.core.domain.DemoActor
import com.barbarus.prosper.factories.ActorFactory
import spock.lang.Specification

class ConditionLogicSpec extends Specification {
    def "should simulate exhaustion conditions by the rest urge"() {
        given:
        ConditionLogic conditionLogic = new ConditionLogic()
        DemoActor actor = new ActorFactory().poorActor()
        actor.urges.stopUrge("rest")
        actor.urges.increaseUrge("rest", urgeToRest.doubleValue())
        conditionLogic.process(actor)

        expect:
        actor.conditions.contains(expectedCondition)

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
        DemoActor actor = new ActorFactory().poorActor()
        actor.state.health = givenHealth
        conditionLogic.process(actor)

        expect:
        actor.conditions.contains(expectedCondition)

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
