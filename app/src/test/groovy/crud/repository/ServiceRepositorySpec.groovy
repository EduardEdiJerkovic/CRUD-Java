import spock.lang.Specification
import spock.lang.AutoCleanup
import spock.lang.Subject
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import crud.entity.ProviderEntity
import crud.entity.ServiceEntity
import crud.utils.DepthLevel
import crud.repository.ServiceRepository

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ServiceRepositorySpec extends Specification {

    @Subject
    ServiceRepository serviceRepository

    @AutoCleanup
    ServiceEntity createdService

    def setup() {
        serviceRepository = new ServiceRepository(jdbcTemplate)
    }

    def "should save a new service with providers"() {
        given:
        ServiceEntity service = new ServiceEntity(name: "Test Service")
        ProviderEntity provider1 = new ProviderEntity(name: "Provider 1")
        ProviderEntity provider2 = new ProviderEntity(name: "Provider 2")
        service.setProviders([provider1, provider2])

        when:
        createdService = serviceRepository.save(service)

        then:
        createdService.id
        createdService.name == "Test Service"
        createdService.providers.size() == 2
        createdService.providers*.name == ["Provider 1", "Provider 2"]
    }

    def "should find all services with shallow depth"() {
        given:
        jdbcTemplate.update("INSERT INTO services (name) VALUES ('Service 1')")
        jdbcTemplate.update("INSERT INTO services (name) VALUES ('Service 2')")

        when:
        def services = serviceRepository.findAll(DepthLevel.SHALLOW)

        then:
        services.size() == 2
        services*.name == ["Service 1", "Service 2"]
    }

    def "should find service by id with medium depth"() {
        given:
        jdbcTemplate.update("INSERT INTO services (name) VALUES ('Test Service')")
        def serviceId = jdbcTemplate.queryForObject("SELECT id FROM services WHERE name = 'Test Service'", Long)

        when:
        def foundService = serviceRepository.findById(serviceId, DepthLevel.MEDIUM)

        then:
        foundService.isPresent()
        foundService.get().name == "Test Service"
        foundService.get().providers.isEmpty()
    }

    def "should update an existing service"() {
        given:
        jdbcTemplate.update("INSERT INTO services (name) VALUES ('Existing Service')")
        def serviceId = jdbcTemplate.queryForObject("SELECT id FROM services WHERE name = 'Existing Service'", Long)
        def existingService = serviceRepository.findById(serviceId, DepthLevel.SHALLOW).orElseThrow()

        when:
        existingService.name = "Updated Service"
        createdService = serviceRepository.update(existingService)

        then:
        createdService.name == "Updated Service"
    }

    def "should delete an existing service"() {
        given:
        jdbcTemplate.update("INSERT INTO services (name) VALUES ('Service to Delete')")
        def serviceId = jdbcTemplate.queryForObject("SELECT id FROM services WHERE name = 'Service to Delete'", Long)

        when:
        serviceRepository.delete(serviceId)

        then:
        def deletedService = jdbcTemplate.query("SELECT * FROM services WHERE id = ?", new ServiceResultExtractor(), serviceId)
        deletedService.isEmpty()
    }
}
