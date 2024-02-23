package crud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private List<ServiceEntity> services;

    public ProviderEntity(String name, List<ServiceEntity> services) {
        this.name = name;
        this.services = services;
    }

    public void addService(ServiceEntity service) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addService'");
    }

    public void removeService(ServiceEntity service) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeService'");
    }
}
