package org.vaadin.example.bookstore.backend.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, message = "Category name must be at least two characters")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    /*
     * Vaadin DataProviders rely on properly implemented equals and hashcode
     * methods.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == null) {
            return false;
        }
        if (obj instanceof Category) {
            return id.equals(((Category) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }

        return Objects.hash(id);
    }
}
