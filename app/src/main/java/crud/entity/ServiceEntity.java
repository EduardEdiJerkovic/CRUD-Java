package crud.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a service entity in the system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "services")
public class ServiceEntity extends BaseEntity {

    /**
     * The name of the service.
     */
    @Column(name = "name")
    private String name;

    /**
     * List of providers associated with this service.
     */
    @ManyToMany(mappedBy = "providers", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<ProviderEntity> providers;

    /**
     * Constructs a new ServiceEntity with the given id and name.
     *
     * @param id   The unique identifier for the service.
     * @param name The name of the service.
     */
    public ServiceEntity(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
        this.providers = new ArrayList<>();
    }

    /**
     * Adds a provider to the list of providers associated with this service.
     *
     * @param provider The provider to be added.
     */
    public void addProvider(ProviderEntity provider) {
        this.providers.add(provider);
    }

    /**
     * Checks if the service contains a provider with the specified id.
     *
     * @param id The id of the provider to check for.
     * @return True if the service contains a provider with the specified id, false
     *         otherwise.
     */
    public boolean containsProvider(Long id) {
        return this.providers.stream().anyMatch(provider -> provider.getId().equals(id));
    }

    /**
     * Retrieves an optional containing the provider with the specified id, if
     * present.
     *
     * @param id The id of the provider to retrieve.
     * @return Optional containing the provider with the specified id, or empty if
     *         not found.
     */
    public Optional<ProviderEntity> getProvider(Long id) {
        return this.providers.stream()
                .filter(provider -> provider.getId().equals(id))
                .findFirst();
    }

    /**
     * Removes a provider from the list of providers associated with this service.
     *
     * @param provider The provider to be removed.
     */
    public void removeProvider(ProviderEntity provider) {
        this.providers.remove(provider);
    }
}
