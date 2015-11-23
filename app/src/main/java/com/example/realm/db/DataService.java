package com.example.realm.db;

import android.content.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andrey Kucherenko on 21.11.15.
 */
public class DataService {
    public interface UpdateListener {
        void onDataUpdated();
    }

    private static final Map<Class, Set<UpdateListener>> sListeners =
            new HashMap<Class, Set<UpdateListener>>() {
                {
                    put(ModelsStore.class, Collections.newSetFromMap(new ConcurrentHashMap<UpdateListener, Boolean>()));
                }
            };

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

    public static void subscribeDataUpdates(Class clazz, UpdateListener listener) {
        Set<UpdateListener> listeners = sListeners.get(clazz);
        if (listeners != null) {
            listeners.add(listener);
        }
    }

    public static void unsubscribeDataUpdates(UpdateListener listener) {
        for (Set<UpdateListener> listeners : sListeners.values()) {
            listeners.remove(listener);
        }
    }

    public static void notifyDataUpdated(Class clazz) {
        Set<UpdateListener> listeners = sListeners.get(clazz);
        if (listeners != null) {
            for (UpdateListener listener: listeners) {
                listener.onDataUpdated();
            }
        }
    }
}
