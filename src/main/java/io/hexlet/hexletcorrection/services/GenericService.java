package io.hexlet.hexletcorrection.services;

import java.util.Set;

public interface GenericService<T> {
    Set<T> findAll();
    
    Set<T> findByName(String name);
    
    T findById(Long id);
    
    T save(T entity);
    
    void delete(Long id);
}