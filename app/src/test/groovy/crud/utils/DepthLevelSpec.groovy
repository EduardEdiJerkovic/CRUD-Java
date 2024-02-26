import spock.lang.Specification
import crud.utils.DepthLevel

class DepthLevelSpec extends Specification {

    def "should return correct string value for each enum"() {
        expect:
        DepthLevel.SHALLOW.getValue() == "shallow"
        DepthLevel.MEDIUM.getValue() == "medium"
        DepthLevel.DEEP.getValue() == "deep"
    }

    def "should convert string value to corresponding DepthLevel enum"() {
        expect:
        DepthLevel.fromString("shallow") == DepthLevel.SHALLOW
        DepthLevel.fromString("medium") == DepthLevel.MEDIUM
        DepthLevel.fromString("deep") == DepthLevel.DEEP

        // Case-insensitive
        DepthLevel.fromString("ShAlLoW") == DepthLevel.SHALLOW
    }

    def "should throw exception for unknown string value"() {
        when:
        DepthLevel.fromString("unknown")

        then:
        thrown(IllegalArgumentException) // Expected exception
    }

    def "should check equality with string, ignoring case"() {
        expect:
        DepthLevel.SHALLOW.equalsString("shallow")
        DepthLevel.MEDIUM.equalsString("medium")
        DepthLevel.DEEP.equalsString("deep")

        // Case-insensitive
        DepthLevel.SHALLOW.equalsString("ShAlLoW")
    }
}
