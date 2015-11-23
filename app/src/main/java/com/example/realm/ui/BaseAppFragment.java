package com.example.realm.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.example.realm.R;
import com.example.realm.db.DataService;
import com.example.realm.utils.AsyncOperation;

import java.util.concurrent.Callable;

/**
 * All app fragments extends this class
 */
public abstract class BaseAppFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected abstract class AsyncOperationCallable<T> implements Callable<T> {
        private boolean mShowProgressDialog = false;
        private boolean mShowAlertDialogOnError = false;

        public AsyncOperationCallable<T> showProgressDialog() {
            mShowProgressDialog = true;
            return this;
        }

        public AsyncOperationCallable<T> showAlertDialogOnError() {
            mShowAlertDialogOnError = true;
            return this;
        }

        public boolean isShowProgressDialog() {
            return mShowProgressDialog;
        }

        public boolean isShowAlertDialogOnError() {
            return mShowAlertDialogOnError;
        }
    }

    @Override
    public void onDestroyView() {
        if (this instanceof DataService.UpdateListener) {
            DataService.unsubscribeDataUpdates((DataService.UpdateListener) this);
        }
        super.onDestroy();
    }

    protected <T> void executeAsync(final AsyncOperationCallable<T> callable,
                                    final AsyncOperation.Callback<T> callback,
                                    final AsyncOperation.Callback<Exception> errorListener) {
        AsyncOperation<T> task = new AsyncOperation<>(callable, new AsyncOperation.Callback<T>() {
            @Override
            public void onComplete(T result) {
                if (callable.isShowProgressDialog()) {
                    showProgress(false);
                }
                if (callback != null) {
                    if (isAlive()) {
                        callback.onComplete(result);
                    }
                }
            }
        }, new AsyncOperation.Callback<Exception>() {
            @Override
            public void onComplete(Exception error) {
                if (callable.isShowProgressDialog()) {
                    showProgress(false);
                }
                if (callable.isShowAlertDialogOnError()) {
                    showErrorAlertDialog(error, callable, callback, errorListener);
                }
                else {
                    if ((errorListener != null) && isAlive()) {
                        errorListener.onComplete(error);
                    }
                }
            }
        });

        if (callable.isShowProgressDialog()) {
            showProgress(true);
        }
        task.execute();
    }

    private <T> void showErrorAlertDialog(final Exception error, final AsyncOperationCallable<T> callable,
                                          final AsyncOperation.Callback<T> callback,
                                          final AsyncOperation.Callback<Exception> errorListener) {
        String errorMessage;
        if ((error.getCause() != null) && (error.getCause().getMessage() != null)) {
            errorMessage = error.getCause().getMessage();
        }
        else {
            errorMessage = error.getMessage();
            if (TextUtils.isEmpty(errorMessage)) {
                errorMessage = getString(R.string.unknown_error);
            }
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        executeAsync(callable, callback, errorListener);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ((errorListener != null) && isAlive()) {
                            errorListener.onComplete(error);
                        }
                    }
                });
        alertDialog.show();
    }

    protected void showProgress(boolean visible) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (visible) {
            mProgressDialog = ProgressDialog.show(getContext(), null, getString(R.string.loading));
        }
    }

    private boolean isAlive() {
        return !isRemoving() && !isDetached() && isAdded();
    }
}
