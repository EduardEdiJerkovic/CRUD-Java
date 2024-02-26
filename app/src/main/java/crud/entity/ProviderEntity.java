package crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a provider entity in the system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "providers")
public class ProviderEntity extends BaseEntity {

    /**
     * The name of the provider.
     */
    @Column(name = "name")
    private String name;

    /**
     * List of services associated with this provider.
     */
    @ManyToMany(mappedBy = "services", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "provider_service", joinColumns = @JoinColumn(name = "provider_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceEntity> services;

    /**
     * Constructs a new ProviderEntity with the given id and name.
     *
     * @param id   The unique identifier for the provider.
     * @param name The name of the provider.
     */
    public ProviderEntity(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
        this.services = new ArrayList<>();
    }

    /**
     * Adds a service to the list of services associated with this provider.
     *
     * @param service The service to be added.
     */
    public void addService(ServiceEntity service) {
        this.services.add(service);
    }

    /**
     * Checks if the provider contains a service with the specified id.
     *
     * @param id The id of the service to check for.
     * @return True if the provider contains a service with the specified id, false
     *         otherwise.
     */
    public boolean containsService(Long id) {
        return this.services.stream().anyMatch(service -> service.getId().equals(id));
    }

    /**
     * Retrieves an optional containing the service with the specified id, if
     * present.
     *
     * @param id The id of the service to retrieve.
     * @return Optional containing the service with the specified id, or empty if
     *         not found.
     */
    public Optional<ServiceEntity> getService(Long id) {
        return this.services.stream()
                .filter(service -> service.getId().equals(id))
                .findFirst();
    }

    /**
     * Removes a service from the list of services associated with this provider.
     *
     * @param service The service to be removed.
     */
    public void removeService(ServiceEntity service) {
        this.services.remove(service);
    }
}
