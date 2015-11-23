package com.example.realm;

import android.app.Application;

import com.example.realm.db.DataService;

/**
 * Created by Andrey Kucherenko on 21.11.15.
 */
public class RealmApplication extends Application {
    public void onCreate() {
        super.onCreate();
        DataService.init(getApplicationContext());
    }
}
