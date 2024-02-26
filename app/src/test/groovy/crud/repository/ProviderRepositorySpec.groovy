import spock.lang.Specification
import spock.lang.AutoCleanup
import spock.lang.Subject
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import crud.entity.ProviderEntity
import crud.entity.ServiceEntity
import crud.utils.DepthLevel
import crud.repository.ProviderRepository

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ProviderRepositorySpec extends Specification {

    @Subject
    ProviderRepository providerRepository

    @AutoCleanup
    ProviderEntity createdProvider

    def setup() {
        providerRepository = new ProviderRepository(jdbcTemplate)
    }

    def "should save a new provider with services"() {
        given:
        ProviderEntity provider = new ProviderEntity(name: "Test Provider")
        ServiceEntity service1 = new ServiceEntity(name: "Service 1")
        ServiceEntity service2 = new ServiceEntity(name: "Service 2")
        provider.setServices([service1, service2])

        when:
        createdProvider = providerRepository.save(provider)

        then:
        createdProvider.id
        createdProvider.name == "Test Provider"
        createdProvider.services.size() == 2
        createdProvider.services*.name == ["Service 1", "Service 2"]
    }

    def "should find all providers with shallow depth"() {
        given:
        jdbcTemplate.update("INSERT INTO providers (name) VALUES ('Provider 1')")
        jdbcTemplate.update("INSERT INTO providers (name) VALUES ('Provider 2')")

        when:
        def providers = providerRepository.findAll(DepthLevel.SHALLOW)

        then:
        providers.size() == 2
        providers*.name == ["Provider 1", "Provider 2"]
    }

    def "should find provider by id with medium depth"() {
        given:
        jdbcTemplate.update("INSERT INTO providers (name) VALUES ('Test Provider')")
        def providerId = jdbcTemplate.queryForObject("SELECT id FROM providers WHERE name = 'Test Provider'", Long)

        when:
        def foundProvider = providerRepository.findById(providerId, DepthLevel.MEDIUM)

        then:
        foundProvider.isPresent()
        foundProvider.get().name == "Test Provider"
        foundProvider.get().services.isEmpty()
    }

    def "should update an existing provider"() {
        given:
        jdbcTemplate.update("INSERT INTO providers (name) VALUES ('Existing Provider')")
        def providerId = jdbcTemplate.queryForObject("SELECT id FROM providers WHERE name = 'Existing Provider'", Long)
        def existingProvider = providerRepository.findById(providerId, DepthLevel.SHALLOW).orElseThrow()

        when:
        existingProvider.name = "Updated Provider"
        createdProvider = providerRepository.update(existingProvider)

        then:
        createdProvider.name == "Updated Provider"
    }

    def "should delete an existing provider"() {
        given:
        jdbcTemplate.update("INSERT INTO providers (name) VALUES ('Provider to Delete')")
        def providerId = jdbcTemplate.queryForObject("SELECT id FROM providers WHERE name = 'Provider to Delete'", Long)

        when:
        providerRepository.delete(providerId)

        then:
        def deletedProvider = jdbcTemplate.query("SELECT * FROM providers WHERE id = ?", new ProviderResultExtractor(), providerId)
        deletedProvider.isEmpty()
    }
}
