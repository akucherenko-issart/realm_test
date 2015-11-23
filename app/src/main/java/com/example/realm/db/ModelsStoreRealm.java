package com.example.realm.db;

import com.example.realm.db.model.ModelRealm;
import com.example.realm.model.Model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Words CRUD implementation
 */
public class ModelsStoreRealm implements ModelsStore {

    @Override
    public List<Model> requestAllModels() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ModelRealm> resultsRealm = realm.where(ModelRealm.class).findAll();
        List<Model> results = ModelRealm.sMapper.fromRealmResults(resultsRealm);
        realm.close();
        return results;
    }

    @Override
    public void addModel(Model model) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(ModelRealm.sMapper.toRealmObject(model));
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void clear() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.clear(ModelRealm.class);
        realm.commitTransaction();
        realm.close();
    }
}
