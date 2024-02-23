package crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import crud.entity.ServiceEntity;

@Repository
public class ServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ServiceEntity save(ServiceEntity serviceEntity) {
        String sql = "INSERT INTO service (name) VALUES (?)";
        jdbcTemplate.update(sql, serviceEntity.getName());
        return serviceEntity;
    }

    public List<ServiceEntity> findAll() {
        String sql = "SELECT * FROM service";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ServiceEntity.class));
    }

    public Optional<ServiceEntity> findById(Long id) {
        String sql = "SELECT * FROM service WHERE id = ?";
        Optional<ServiceEntity> service = Optional.of(jdbcTemplate.queryForObject(
                sql, ServiceEntity.class, id));

        return service;
    }

    public ServiceEntity update(ServiceEntity serviceEntity) {
        String sql = "UPDATE service SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, serviceEntity.getName(), serviceEntity.getId());
        return serviceEntity;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM service WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
