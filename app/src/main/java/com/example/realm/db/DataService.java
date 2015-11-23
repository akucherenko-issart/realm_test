package com.example.realm.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andrey Kucherenko on 21.11.15.
 */
public class DataService {
    private static final ModelsStore sModesStore = new ModelsStoreRealm();

    public static ModelsStore getModelsStore() {
        return sModesStore;
    }

    public static void init(Context context) {
        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder(context)
                        .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
