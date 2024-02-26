package crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a base entity in the system.
 */
@Getter
@Setter
public class BaseEntity {

    /**
     * The unique identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Indicates whether the entity is deleted.
     */
    @Column(name = "name")
    private boolean isDeleted = false;

    /**
     * Constructs a new BaseEntity with the default values.
     */
    protected BaseEntity() {
        this.isDeleted = false;
    }
}
