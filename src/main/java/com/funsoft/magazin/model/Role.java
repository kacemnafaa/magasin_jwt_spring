package com.funsoft.magazin.model;

import javax.persistence.*;

@Entity
@Table(name="roles")

public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    private Erole name;

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Erole getName() {
        return name;
    }

    public void setName(Erole name) {
        this.name = name;
    }
}
