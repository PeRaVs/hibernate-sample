package org.hibernate.bugs.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="entity_1")
public class Entity1 {
    @Id
    @Column(name="id", columnDefinition="char(36)")
    private String id;

    @OneToMany(
            mappedBy="entity1",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Entity2> entity2 = new ArrayList<>();

    public String getId() {
        return id;
    }

    public Entity1 setId(String id) {
        this.id = id;
        return this;
    }

    public List<Entity2> getEntity2() {
        return entity2;
    }

    public Entity1 setEntity2(List<Entity2> entity2) {
        this.entity2 = entity2;
        return this;
    }
}
