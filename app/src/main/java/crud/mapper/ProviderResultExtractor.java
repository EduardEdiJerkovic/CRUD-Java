package crud.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import crud.entity.ProviderEntity;
import crud.entity.ServiceEntity;

/**
 * ResultSetExtractor implementation for extracting a list of ProviderEntity
 * objects from a ResultSet.
 */
public class ProviderResultExtractor implements ResultSetExtractor<List<ProviderEntity>> {

    /**
     * Extracts data from the ResultSet and constructs a list of ProviderEntity
     * objects.
     *
     * @param rs The ResultSet containing the data.
     * @return List of ProviderEntity objects.
     * @throws SQLException If a SQL exception occurs during data extraction.
     */
    @Override
    public List<ProviderEntity> extractData(ResultSet rs) throws SQLException {
        Map<Long, ProviderEntity> providers = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("provider_id");

            ProviderEntity provider;
            if (providers.containsKey(id)) {
                provider = providers.get(id);
            } else {
                provider = new ProviderEntity(rs.getLong("provider_id"), rs.getString("provider_name"));
                providers.put(provider.getId(), provider);
            }

            ServiceEntity service;

            try {
                service = new ServiceEntity(rs.getLong("service_id"), rs.getString("service_name"));
                if (!provider.containsService(service.getId())) {
                    provider.addService(service);
                }
            } catch (SQLException e) {
                continue;
            }

            try {
                var serviceProvider = new ProviderEntity(rs.getLong("provider_service_id"),
                        rs.getString("provider_service_name"));

                var optionalService = provider.getService(service.getId());
                if (optionalService.isPresent()) {
                    service = optionalService.get();
                }

                service.addProvider(serviceProvider);
            } catch (SQLException e) {
                continue;
            }
        }

        return (List<ProviderEntity>) providers.values();
    }
}
