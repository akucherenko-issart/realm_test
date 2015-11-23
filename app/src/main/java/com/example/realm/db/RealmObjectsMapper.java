package com.example.realm.db;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Andrey Kucherenko on 23.11.15.
 */
public abstract class RealmObjectsMapper<M, R extends RealmObject> {
    public abstract M fromRealmObject(R modelRealm);
    public abstract R toRealmObject(M model);

    public List<M> fromRealmResults(RealmResults<R> realmResults) {
        List<M> results = new ArrayList<>(realmResults.size());
        for (R realmObject: realmResults) {
            results.add(this.fromRealmObject(realmObject));
        }
        return results;
    }
}
