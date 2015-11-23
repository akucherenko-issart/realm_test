package com.example.realm.utils;

import android.os.AsyncTask;

import java.util.concurrent.Callable;

/**
 * Async operation
 */
public class AsyncOperation<T> extends AsyncTask<Void, Void, Object> {

    public interface Callback<T> {
        void onComplete(T result);
    }

    Callback<T> mCompleteListener;
    Callback<Exception> mErrorListener;
    Callable<T> mCallable;

    public AsyncOperation(Callable<T> callable, Callback<T> completeListener, Callback<Exception> errorListener) {
        super();
        mCallable = callable;
        mCompleteListener = completeListener;
        mErrorListener = errorListener;
    }

    @Override
    protected final Object doInBackground(Void... arg0) {
        try {
            return mCallable.call();
        }
        catch (Exception e) {
            return e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void onPostExecute(Object result) {
        super.onPostExecute(result);

        if (result instanceof Exception) {
            if (mErrorListener != null) {
                mErrorListener.onComplete((Exception) result);
            }
            return;
        }

        try {
            if (mCompleteListener != null) {
                mCompleteListener.onComplete((T) result);
            }
        }
        catch (Exception e) {
            if (mErrorListener != null) {
                mErrorListener.onComplete(e);
            }
        }
    }
}
