package com.example.realm.db;

import com.example.realm.model.Model;

import java.util.List;

/**
 * Words CRUD
 */
public interface ModelsStore {
    List<Model> requestAllModels();
    void addModel(Model model);
    void clear();
}
