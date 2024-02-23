package crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import crud.entity.ProviderEntity;

@Repository
public class ProviderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProviderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProviderEntity save(ProviderEntity serviceEntity) {
        String sql = "INSERT INTO provider (name) VALUES (?)";
        jdbcTemplate.update(sql, serviceEntity.getName());
        return serviceEntity;
    }

    public List<ProviderEntity> findAll() {
        String sql = "SELECT * FROM provider";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProviderEntity.class));
    }

    public Optional<ProviderEntity> findById(Long id) {
        String sql = "SELECT * FROM provider WHERE id = ?";
        Optional<ProviderEntity> provider = Optional.of(jdbcTemplate.queryForObject(
                sql, ProviderEntity.class, id));

        return provider;
    }

    public ProviderEntity update(ProviderEntity providerEntity) {
        String sql = "UPDATE provider SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, providerEntity.getName(), providerEntity.getId());
        return providerEntity;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM provider WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
