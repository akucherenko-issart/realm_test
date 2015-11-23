package com.example.realm.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.realm.R;
import com.example.realm.db.DataService;
import com.example.realm.model.Model;
import com.example.realm.ui.BaseAppFragment;
import com.example.realm.ui.adapters.ModelsListAdapter;
import com.example.realm.utils.AsyncOperation;

import java.util.List;
import java.util.UUID;

/**
 * Fragment for display models list
 */
public class ModelsFragment extends BaseAppFragment {

    private ModelsListAdapter mAdapter;

    public ModelsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_models, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_models);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mAdapter == null) {
            mAdapter = new ModelsListAdapter();
            updateData();
        }
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_models, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                addRandomItem();
                return true;
            case R.id.action_delete_item:
                clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clear() {
        AsyncOperationCallable<Void> callable = new AsyncOperationCallable<Void>() {
            @Override
            public Void call() throws Exception {
                DataService.getModelsStore().clear();
                return null;
            }
        }
                .showAlertDialogOnError()
                .showProgressDialog();

        executeAsync(callable, new AsyncOperation.Callback<Void>() {
            @Override
            public void onComplete(Void result) {
                updateData();
            }
        }, null);
    }

    private void addRandomItem() {
        AsyncOperationCallable<Void> callable = new AsyncOperationCallable<Void>() {
            @Override
            public Void call() throws Exception {
                DataService.getModelsStore().addModel(new Model(UUID.randomUUID().toString()));
                return null;
            }
        }
        .showAlertDialogOnError();

        executeAsync(callable, new AsyncOperation.Callback<Void>() {
            @Override
            public void onComplete(Void result) {
                updateData();
            }
        }, null);
    }

    private void updateData() {
        AsyncOperationCallable<List<Model>> callable = new AsyncOperationCallable<List<Model>>() {
            @Override
            public List<Model> call() throws Exception {
                return DataService.getModelsStore().requestAllModels();
            }
        }
        .showAlertDialogOnError();

        executeAsync(callable, new AsyncOperation.Callback<List<Model>>() {
            @Override
            public void onComplete(List<Model> result) {
                mAdapter.setModels(result);
            }
        }, null);
    }
}
