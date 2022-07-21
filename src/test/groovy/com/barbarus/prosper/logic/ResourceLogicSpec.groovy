package com.barbarus.prosper.logic

import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import spock.lang.*

class ResourceLogicSpec extends Specification {
    def "should return the correct resource type for a given profession"() {
        given:
        ResourceLogic resourceLogic = new ResourceLogic()

        expect:
        Resource resource = resourceLogic.process(profession)
        resource.type == resourceType

        where:
        profession                                     || resourceType
        new Profession(ProfessionType.GATHERER, 1.0)   || ResourceType.FOOD
        new Profession(ProfessionType.WOODWORKER, 1.0) || ResourceType.WOODEN_MATERIAL
        new Profession(ProfessionType.HERBALIST, 1.0)  || ResourceType.HERBS
        new Profession(ProfessionType.TOOLMAKER, 1.0)  || ResourceType.TOOLS
    }
}