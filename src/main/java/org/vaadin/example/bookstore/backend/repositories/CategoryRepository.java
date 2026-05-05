package org.vaadin.example.bookstore.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.bookstore.backend.data.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
