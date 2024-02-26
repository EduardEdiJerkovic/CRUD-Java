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
 * ResultSetExtractor implementation for extracting a list of ServiceEntity
 * objects from a ResultSet.
 */
public class ServiceResultExtractor implements ResultSetExtractor<List<ServiceEntity>> {

    /**
     * Extracts data from the ResultSet and constructs a list of ServiceEntity
     * objects.
     *
     * @param rs The ResultSet containing the data.
     * @return List of ServiceEntity objects.
     * @throws SQLException If a SQL exception occurs during data extraction.
     */
    @Override
    public List<ServiceEntity> extractData(ResultSet rs) throws SQLException {
        Map<Long, ServiceEntity> services = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("service_id");

            ServiceEntity service;
            if (services.containsKey(id)) {
                service = services.get(id);
            } else {
                service = new ServiceEntity(rs.getLong("service_id"), rs.getString("service_name"));
                services.put(service.getId(), service);
            }

            ProviderEntity provider;

            try {
                provider = new ProviderEntity(rs.getLong("provider_id"), rs.getString("provider_name"));
                if (!service.containsProvider(provider.getId())) {
                    service.addProvider(provider);
                }
            } catch (SQLException e) {
                continue;
            }

            try {
                var serviceProvider = new ServiceEntity(rs.getLong("provider_service_id"),
                        rs.getString("provider_service_name"));

                var optionalProvider = service.getProvider(provider.getId());
                if (optionalProvider.isPresent()) {
                    provider = optionalProvider.get();
                }

                provider.addService(serviceProvider);
            } catch (SQLException e) {
                continue;
            }
        }

        return (List<ServiceEntity>) services.values();
    }
}
