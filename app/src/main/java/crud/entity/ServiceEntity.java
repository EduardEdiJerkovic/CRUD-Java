package crud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private List<ProviderEntity> providers;

    public ServiceEntity(String name, List<ProviderEntity> providers) {
        this.name = name;
        this.providers = providers;
    }

    public void addProvider(ProviderEntity provider) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProvider'");
    }

    public void removeProvider(ProviderEntity provider) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProvider'");
    }
}
