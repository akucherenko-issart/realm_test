package com.example.realm.db.model;

import com.example.realm.db.RealmObjectsMapper;
import com.example.realm.model.Model;

import io.realm.RealmObject;

/**
 * Created by Andrey Kucherenko on 21.11.15.
 */
public class ModelRealm extends RealmObject {
    public static final RealmObjectsMapper<Model, ModelRealm> sMapper = new RealmObjectsMapper<Model, ModelRealm>() {
        @Override
        public Model fromRealmObject(ModelRealm modelRealm) {
            return new Model(modelRealm.getUuid());
        }

        @Override
        public ModelRealm toRealmObject(Model model) {
            ModelRealm modelRealm = new ModelRealm();
            modelRealm.setUuid(model.uuid);
            return modelRealm;
        }
    };

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
