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
import crud.mapper.ProviderResultExtractor;
import crud.utils.DepthLevel;

/**
 * Repository class for managing ProviderEntity entities in the database.
 */
@Repository
public class ProviderRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to inject JdbcTemplate.
     *
     * @param jdbcTemplate The JdbcTemplate to be used for database operations.
     */
    @Autowired
    public ProviderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new ProviderEntity to the database.
     *
     * @param provider The ProviderEntity to be saved.
     * @return The saved ProviderEntity with generated ID.
     */
    @Transactional
    public ProviderEntity save(ProviderEntity provider) {
        // Step 1: Insert ProviderEntity and get its generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO providers (name) VALUES (?)",
                    new String[] { "id" });
            ps.setString(1, provider.getName());
            return ps;
        }, keyHolder);

        Long providerId = keyHolder.getKey().longValue();

        // Step 2: Update join table with existing ServiceEntities
        for (ServiceEntity service : provider.getServices()) {
            jdbcTemplate.update(
                    "INSERT INTO provider_service (provider_id, service_id) VALUES (?, ?)",
                    providerId,
                    service.getId());
        }

        provider.setId(providerId);
        return provider;
    }

    /**
     * Retrieves all providers from the database with the specified depth.
     *
     * @param depth The depth level of the retrieval (SHALLOW, MEDIUM).
     * @return List of ProviderEntity objects.
     */
    public List<ProviderEntity> findAll(DepthLevel depth) {
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

        return jdbcTemplate.query(sql, new ProviderResultExtractor());
    }

    /**
     * Retrieves a provider by ID from the database with the specified depth.
     *
     * @param id    The ID of the provider to be retrieved.
     * @param depth The depth level of the retrieval (SHALLOW, MEDIUM, DEEP).
     * @return An Optional containing the retrieved ProviderEntity, or empty if not
     *         found.
     */
    public Optional<ProviderEntity> findById(Long id, DepthLevel depth) {
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
            return Optional.ofNullable(jdbcTemplate.query(sql, new ProviderResultExtractor(), id).get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Updates an existing ProviderEntity in the database.
     *
     * @param provider The updated ProviderEntity.
     * @return The updated ProviderEntity.
     */
    @Transactional
    public ProviderEntity update(ProviderEntity provider) {
        // Step 1: Update ProviderEntity name
        jdbcTemplate.update(
                "UPDATE providers SET name = ? WHERE id = ?",
                provider.getName(),
                provider.getId());

        // Step 2: Retrieve current services associated with the provider
        List<Long> currentServiceIds = jdbcTemplate.queryForList(
                "SELECT service_id FROM provider_service WHERE provider_id = ?",
                Long.class,
                provider.getId());

        // Step 3: Identify services to be removed
        List<Long> servicesToRemove = currentServiceIds.stream()
                .filter(serviceId -> !provider.containsService(serviceId))
                .collect(Collectors.toList());

        // Step 4: Identify services to be added
        List<Long> servicesToAdd = provider.getServices().stream()
                .filter(service -> !currentServiceIds.contains(service.getId()))
                .map(ServiceEntity::getId)
                .collect(Collectors.toList());

        // Step 5: Remove services
        for (Long serviceId : servicesToRemove) {
            jdbcTemplate.update(
                    "DELETE FROM provider_service WHERE provider_id = ? AND service_id = ?",
                    provider.getId(),
                    serviceId);
        }

        // Step 6: Add new services
        for (Long serviceId : servicesToAdd) {
            jdbcTemplate.update(
                    "INSERT INTO provider_service (provider_id, service_id) VALUES (?, ?)",
                    provider.getId(),
                    serviceId);
        }

        return provider;
    }

    /**
     * Deletes a provider by marking it as deleted in the database.
     *
     * @param id The ID of the provider to be deleted.
     */
    public void delete(Long id) {
        String sql = "UPDATE providers SET isDeleted = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Retrieves SQL query for fetching a shallow ProviderEntity by ID from the
     * database.
     *
     * @return The SQL query.
     */
    private String getFindByIdShallowSql() {
        return "SELECT id, name FROM providers WHERE id = ? AND isDeleted = 0";
    }

    /**
     * Retrieves SQL query for fetching a medium-depth ProviderEntity by ID from the
     * database.
     *
     * @return The SQL query.
     */
    private String getFindByIdMediumSql() {
        return "SELECT " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name, " +
                "    s.id AS service_id, " +
                "    s.name AS service_name " +
                "FROM " +
                "    providers p " +
                "JOIN " +
                "    service_providers sp ON p.id = sp.provider_id " +
                "JOIN " +
                "    services s ON sp.service_id = s.id " +
                "WHERE " +
                "    p.id = ? " +
                "    AND p.isDeleted = 0" +
                "    AND s.isDeleted = 0";
    }

    /**
     * Retrieves SQL query for fetching a deep ProviderEntity by ID from the
     * database.
     *
     * @return The SQL query.
     */
    private String getFindByIdDeepSql() {
        return "SELECT " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name, " +
                "    s.id AS service_id, " +
                "    s.name AS service_name, " +
                "    ps.id AS provider_service_id, " +
                "    ps.name AS provider_service_name " +
                "FROM " +
                "    providers p " +
                "JOIN " +
                "    provider_service sp ON p.id = sp.provider_id " +
                "JOIN " +
                "    services s ON sp.service_id = s.id " +
                "JOIN " +
                "    provider_service ps ON p.id = ps.provider_id " +
                "WHERE " +
                "    s.id = ? " +
                "    AND p.isDeleted = 0" +
                "    AND s.isDeleted = 0" +
                "    AND ps.isDeleted = 0";
    }

    /**
     * Retrieves SQL query for fetching all shallow ProviderEntities from the
     * database.
     *
     * @return The SQL query.
     */
    private String getAllShallowSql() {
        return "SELECT id, name FROM providers WHERE isDeleted = 0";
    }

    /**
     * Retrieves SQL query for fetching all medium-depth ProviderEntities from the
     * database.
     *
     * @return The SQL query.
     */
    private String getAllMediumSql() {
        return "SELECT " +
                "    p.id AS provider_id, " +
                "    p.name AS provider_name, " +
                "    s.id AS service_id, " +
                "    s.name AS service_name " +
                "FROM " +
                "    providers p " +
                "JOIN " +
                "    service_providers sp ON p.id = sp.provider_id " +
                "JOIN " +
                "    services s ON sp.service_id = s.id " +
                "WHERE " +
                "    AND p.isDeleted = 0" +
                "    AND s.isDeleted = 0";
    }

}
