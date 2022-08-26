package org.hibernate.bugs.entities;

import jakarta.persistence.*;

@Entity
@Table(name="entity_2")
public class Entity2 {
    @Id
    @Column(columnDefinition = "char(36)")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name="entity1", referencedColumnName = "id")
    private Entity1 entity1;

    public String getId() {
        return id;
    }

    public Entity2 setId(String id) {
        this.id = id;
        return this;
    }

    public Entity1 getEntity1() {
        return entity1;
    }

    public Entity2 setEntity1(Entity1 entity1) {
        this.entity1 = entity1;
        return this;
    }
}
