package crud.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import crud.entity.ProviderEntity;
import crud.entity.ServiceEntity;
import crud.mapper.ServiceResultExtractor;
import crud.utils.DepthLevel;

/**
 * Repository class for managing operations related to services in the database.
 */
@Repository
public class ServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new service to the database.
     *
     * @param service The service entity to be saved.
     * @return The saved service entity with updated information, including
     *         generated ID.
     */
    @Transactional
    public ServiceEntity save(ServiceEntity service) {
        // Step 1: Insert ServiceEntity and get its generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO services (name) VALUES (?)",
                    new String[] { "id" });
            ps.setString(1, service.getName());
            return ps;
        }, keyHolder);

        Long serviceId = keyHolder.getKey().longValue();

        // Step 2: Update join table with existing ProviderEntities
        for (ProviderEntity provider : service.getProviders()) {
            jdbcTemplate.update(
                    "INSERT INTO provider_service (provider_id, service_id) VALUES (?, ?)",
                    provider.getId(),
                    serviceId);
        }

        service.setId(serviceId);
        return service;
    }

    /**
     * Retrieves all services from the database based on the specified depth level.
     *
     * @param depth The depth level for fetching related entities.
     * @return A list of service entities.
     */
    public List<ServiceEntity> findAll(DepthLevel depth) {
        String sql;

        switch (depth) {
            case SHALLOW:
                sql = getAllShallowSql();
                break;
            case MEDIUM:
                sql = getAllMediumSql();
                break;
            default:
                throw new IllegalArgumentException("Not supported or invalid depth parameter");
        }

        return jdbcTemplate.query(sql, new ServiceResultExtractor());
    }

    /**
     * Retrieves a service by its ID from the database based on the specified depth
     * level.
     *
     * @param id    The ID of the service to be retrieved.
     * @param depth The depth level for fetching related entities.
     * @return An optional containing the service entity if found, otherwise empty.
     */
    public Optional<ServiceEntity> findById(Long id, DepthLevel depth) {
        String sql;

        switch (depth) {
            case SHALLOW:
                sql = getFindByIdShallowSql();
                break;
            case MEDIUM:
                sql = getFindByIdMediumSql();
                break;
            case DEEP:
                sql = getFindByIdDeepSql();
                break;
            default:
                throw new IllegalArgumentException("Not supported or invalid depth parameter");
        }
        try {
            return Optional.ofNullable(jdbcTemplate.query(sql, new ServiceResultExtractor(), id).get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Updates an existing service in the database.
     *
     * @param service The updated service entity.
     * @return The updated service entity with new information.
     */
    @Transactional
    public ServiceEntity update(ServiceEntity service) {
        // Step 1: Update ServiceEntity name
        jdbcTemplate.update(
                "UPDATE services SET name = ? WHERE id = ?",
                service.getName(),
                service.getId());

        // Step 2: Retrieve current providers associated with the service
        List<Long> currentProviderIds = jdbcTemplate.queryForList(
                "SELECT provider_id FROM provider_service WHERE service_id = ?",
                Long.class,
                service.getId());

        // Step 3: Identify providers to be removed
        List<Long> providersToRemove = currentProviderIds.stream()
                .filter(providerId -> !service.containsProvider(providerId))
                .collect(Collectors.toList());

        // Step 4: Identify providers to be added
        List<Long> providersToAdd = service.getProviders().stream()
                .filter(provider -> !currentProviderIds.contains(provider.getId()))
                .map(ProviderEntity::getId)
                .collect(Collectors.toList());

        // Step 5: Remove providers
        for (Long providerId : providersToRemove) {
            jdbcTemplate.update(
                    "DELETE FROM provider_service WHERE provider_id = ? AND service_id = ?",
                    providerId,
                    service.getId());
        }

        // Step 6: Add new providers
        for (Long providerId : providersToAdd) {
            jdbcTemplate.update(
                    "INSERT INTO provider_service (provider_id, service_id) VALUES (?, ?)",
                    providerId,
                    service.getId());
        }

        return service;
    }

    /**
     * Deletes a service by marking it as deleted in the database.
     *
     * @param id The ID of the service to be deleted.
     */
    public void delete(Long id) {
        String sql = "UPDATE services SET isDeleted = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Retrieves a shallow view of a service by its ID from the database.
     * Shallow view includes basic information such as ID and name.
     *
     * @param id The ID of the service to be retrieved.
     * @return A SQL query for fetching the shallow view of a service.
     */
    private String getFindByIdShallowSql() {
        return "SELECT id, name FROM services WHERE id = ? AND isDeleted = 0";
    }

    /**
     * Retrieves a medium view of a service by its ID from the database.
     * Medium view includes service and provider details.
     *
     * @param id The ID of the service to be retrieved.
     * @return A SQL query for fetching the medium view of a service.
     */
    private String getFindByIdMediumSql() {
        return "SELECT " +
                "    s.id AS service_id, " +
                "    s.name AS service_name, " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name " +
                "FROM " +
                "    services s " +
                "JOIN " +
                "    provider_service ps ON s.id = ps.service_id " +
                "JOIN " +
                "    providers p ON ps.provider_id = p.id " +
                "WHERE " +
                "    s.id = ? " +
                "    AND s.isDeleted = 0" +
                "    AND p.isDeleted = 0";
    }

    /**
     * Retrieves a deep view of a service by its ID from the database.
     * Deep view includes service, provider, and provider-service details.
     *
     * @param id The ID of the service to be retrieved.
     * @return A SQL query for fetching the deep view of a service.
     */
    private String getFindByIdDeepSql() {
        return "SELECT " +
                "    s.id AS service_id, " +
                "    s.name AS service_name, " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name, " +
                "    ps.id AS provider_service_id, " +
                "    ps.name AS provider_service_name " +
                "FROM " +
                "    services s " +
                "JOIN " +
                "    provider_service ps ON s.id = ps.service_id " +
                "JOIN " +
                "    providers p ON ps.provider_id = p.id " +
                "WHERE " +
                "    s.id = ? " +
                "    AND s.isDeleted = 0" +
                "    AND p.isDeleted = 0" +
                "    AND ps.isDeleted = 0";
    }

    /**
     * Retrieves a shallow view of all services from the database.
     * Shallow view includes basic information such as ID and name.
     *
     * @return A SQL query for fetching the shallow view of all services.
     */
    private String getAllShallowSql() {
        return "SELECT id, name FROM services WHERE isDeleted = 0";
    }

    /**
     * Retrieves a medium view of all services from the database.
     * Medium view includes service and provider details.
     *
     * @return A SQL query for fetching the medium view of all services.
     */
    private String getAllMediumSql() {
        return "SELECT " +
                "    s.id AS service_id, " +
                "    s.name AS service_name, " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name " +
                "FROM " +
                "    services s " +
                "JOIN " +
                "    provider_service ps ON s.id = ps.service_id " +
                "JOIN " +
                "    providers p ON ps.provider_id = p.id " +
                "WHERE " +
                "    s.isDeleted = 0" +
                "    AND p.isDeleted = 0";
    }
}
