package android.support.v4.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    private boolean mCreatingLoader;
    @NonNull
    private final LifecycleOwner mLifecycleOwner;
    @NonNull
    private final LoaderViewModel mLoaderViewModel;

    public static class LoaderInfo<D> extends MutableLiveData<D> implements Loader.OnLoadCompleteListener<D> {
        @Nullable
        private final Bundle mArgs;
        private final int mId;
        private LifecycleOwner mLifecycleOwner;
        @NonNull
        private final Loader<D> mLoader;
        private LoaderObserver<D> mObserver;

        LoaderInfo(int id, @Nullable Bundle args, @NonNull Loader<D> loader) {
            this.mId = id;
            this.mArgs = args;
            this.mLoader = loader;
            this.mLoader.registerListener(id, this);
        }

        /* access modifiers changed from: package-private */
        @NonNull
        public Loader<D> getLoader() {
            return this.mLoader;
        }

        /* access modifiers changed from: protected */
        public void onActive() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v(LoaderManagerImpl.TAG, "  Starting: " + this);
            }
            this.mLoader.startLoading();
        }

        /* access modifiers changed from: protected */
        public void onInactive() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v(LoaderManagerImpl.TAG, "  Stopping: " + this);
            }
            this.mLoader.stopLoading();
        }

        /* access modifiers changed from: package-private */
        @MainThread
        @NonNull
        public Loader<D> setCallback(@NonNull LifecycleOwner owner, @NonNull LoaderManager.LoaderCallbacks<D> callback) {
            LoaderObserver<D> observer = new LoaderObserver<>(this.mLoader, callback);
            observe(owner, observer);
            if (this.mObserver != null) {
                removeObserver(this.mObserver);
            }
            this.mLifecycleOwner = owner;
            this.mObserver = observer;
            return this.mLoader;
        }

        /* access modifiers changed from: package-private */
        public void markForRedelivery() {
            LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
            LoaderObserver<D> observer = this.mObserver;
            if (lifecycleOwner != null && observer != null) {
                removeObserver(observer);
                observe(lifecycleOwner, observer);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isCallbackWaitingForData() {
            if (hasActiveObservers() && this.mObserver != null && !this.mObserver.hasDeliveredData()) {
                return true;
            }
            return false;
        }

        public void removeObserver(@NonNull Observer<D> observer) {
            super.removeObserver(observer);
            this.mLifecycleOwner = null;
            this.mObserver = null;
        }

        /* access modifiers changed from: package-private */
        @MainThread
        public void destroy() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v(LoaderManagerImpl.TAG, "  Destroying: " + this);
            }
            this.mLoader.cancelLoad();
            this.mLoader.abandon();
            LoaderObserver<D> observer = this.mObserver;
            if (observer != null) {
                removeObserver(observer);
                observer.reset();
            }
            this.mLoader.unregisterListener(this);
            this.mLoader.reset();
        }

        public void onLoadComplete(@NonNull Loader<D> loader, @Nullable D data) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v(LoaderManagerImpl.TAG, "onLoadComplete: " + this);
            }
            if (Looper.myLooper() == Looper.getMainLooper()) {
                setValue(data);
                return;
            }
            if (LoaderManagerImpl.DEBUG) {
                Log.w(LoaderManagerImpl.TAG, "onLoadComplete was incorrectly called on a background thread");
            }
            postValue(data);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.print(prefix);
            writer.print("mId=");
            writer.print(this.mId);
            writer.print(" mArgs=");
            writer.println(this.mArgs);
            writer.print(prefix);
            writer.print("mLoader=");
            writer.println(this.mLoader);
            this.mLoader.dump(prefix + "  ", fd, writer, args);
            if (this.mObserver != null) {
                writer.print(prefix);
                writer.print("mCallbacks=");
                writer.println(this.mObserver);
                this.mObserver.dump(prefix + "  ", writer);
            }
            writer.print(prefix);
            writer.print("mData=");
            writer.println(getLoader().dataToString(getValue()));
            writer.print(prefix);
            writer.print("mStarted=");
            writer.println(hasActiveObservers());
        }
    }

    static class LoaderObserver<D> implements Observer<D> {
        @NonNull
        private final LoaderManager.LoaderCallbacks<D> mCallback;
        private boolean mDeliveredData = false;
        @NonNull
        private final Loader<D> mLoader;

        LoaderObserver(@NonNull Loader<D> loader, @NonNull LoaderManager.LoaderCallbacks<D> callback) {
            this.mLoader = loader;
            this.mCallback = callback;
        }

        public void onChanged(@Nullable D data) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v(LoaderManagerImpl.TAG, "  onLoadFinished in " + this.mLoader + ": " + this.mLoader.dataToString(data));
            }
            this.mCallback.onLoadFinished(this.mLoader, data);
            this.mDeliveredData = true;
        }

        /* access modifiers changed from: package-private */
        public boolean hasDeliveredData() {
            return this.mDeliveredData;
        }

        /* access modifiers changed from: package-private */
        @MainThread
        public void reset() {
            if (this.mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v(LoaderManagerImpl.TAG, "  Resetting: " + this.mLoader);
                }
                this.mCallback.onLoaderReset(this.mLoader);
            }
        }

        public String toString() {
            return this.mCallback.toString();
        }

        public void dump(String prefix, PrintWriter writer) {
            writer.print(prefix);
            writer.print("mDeliveredData=");
            writer.println(this.mDeliveredData);
        }
    }

    static class LoaderViewModel extends ViewModel {
        private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
            @NonNull
            public <T extends ViewModel> T create(@NonNull Class<T> cls) {
                return new LoaderViewModel();
            }
        };
        private SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat<>();

        LoaderViewModel() {
        }

        @NonNull
        static LoaderViewModel getInstance(ViewModelStore viewModelStore) {
            return (LoaderViewModel) new ViewModelProvider(viewModelStore, FACTORY).get(LoaderViewModel.class);
        }

        /* access modifiers changed from: package-private */
        public void putLoader(int id, @NonNull LoaderInfo info) {
            this.mLoaders.put(id, info);
        }

        /* access modifiers changed from: package-private */
        public <D> LoaderInfo<D> getLoader(int id) {
            return this.mLoaders.get(id);
        }

        /* access modifiers changed from: package-private */
        public void removeLoader(int id) {
            this.mLoaders.remove(id);
        }

        /* access modifiers changed from: package-private */
        public boolean hasRunningLoaders() {
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                if (this.mLoaders.valueAt(index).isCallbackWaitingForData()) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void markForRedelivery() {
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                this.mLoaders.valueAt(index).markForRedelivery();
            }
        }

        /* access modifiers changed from: protected */
        public void onCleared() {
            super.onCleared();
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                this.mLoaders.valueAt(index).destroy();
            }
            this.mLoaders.clear();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            if (this.mLoaders.size() > 0) {
                writer.print(prefix);
                writer.println("Loaders:");
                String innerPrefix = prefix + "    ";
                for (int i = 0; i < this.mLoaders.size(); i++) {
                    LoaderInfo info = this.mLoaders.valueAt(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(this.mLoaders.keyAt(i));
                    writer.print(": ");
                    writer.println(info.toString());
                    info.dump(innerPrefix, fd, writer, args);
                }
            }
        }
    }

    LoaderManagerImpl(@NonNull LifecycleOwner lifecycleOwner, @NonNull ViewModelStore viewModelStore) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mLoaderViewModel = LoaderViewModel.getInstance(viewModelStore);
    }

    /* JADX INFO: finally extract failed */
    @MainThread
    @NonNull
    private <D> Loader<D> createAndInstallLoader(int id, @Nullable Bundle args, @NonNull LoaderManager.LoaderCallbacks<D> callback) {
        try {
            this.mCreatingLoader = true;
            Loader<D> loader = callback.onCreateLoader(id, args);
            if (!loader.getClass().isMemberClass() || Modifier.isStatic(loader.getClass().getModifiers())) {
                LoaderInfo<D> info = new LoaderInfo<>(id, args, loader);
                if (DEBUG) {
                    Log.v(TAG, "  Created new loader " + info);
                }
                this.mLoaderViewModel.putLoader(id, info);
                this.mCreatingLoader = false;
                return info.setCallback(this.mLifecycleOwner, callback);
            }
            throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + loader);
        } catch (Throwable th) {
            this.mCreatingLoader = false;
            throw th;
        }
    }

    @MainThread
    @NonNull
    public <D> Loader<D> initLoader(int id, @Nullable Bundle args, @NonNull LoaderManager.LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("initLoader must be called on the main thread");
        } else {
            LoaderInfo<D> info = this.mLoaderViewModel.getLoader(id);
            if (DEBUG) {
                Log.v(TAG, "initLoader in " + this + ": args=" + args);
            }
            if (info == null) {
                return createAndInstallLoader(id, args, callback);
            }
            if (DEBUG) {
                Log.v(TAG, "  Re-using existing loader " + info);
            }
            return info.setCallback(this.mLifecycleOwner, callback);
        }
    }

    @MainThread
    @NonNull
    public <D> Loader<D> restartLoader(int id, @Nullable Bundle args, @NonNull LoaderManager.LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("restartLoader must be called on the main thread");
        } else {
            if (DEBUG) {
                Log.v(TAG, "restartLoader in " + this + ": args=" + args);
            }
            destroyLoader(id);
            return createAndInstallLoader(id, args, callback);
        }
    }

    @MainThread
    public void destroyLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("destroyLoader must be called on the main thread");
        } else {
            if (DEBUG) {
                Log.v(TAG, "destroyLoader in " + this + " of " + id);
            }
            LoaderInfo info = this.mLoaderViewModel.getLoader(id);
            if (info != null) {
                info.destroy();
                this.mLoaderViewModel.removeLoader(id);
            }
        }
    }

    @Nullable
    public <D> Loader<D> getLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo<D> info = this.mLoaderViewModel.getLoader(id);
        if (info != null) {
            return info.getLoader();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void markForRedelivery() {
        this.mLoaderViewModel.markForRedelivery();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mLifecycleOwner, sb);
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        this.mLoaderViewModel.dump(prefix, fd, writer, args);
    }

    public boolean hasRunningLoaders() {
        return this.mLoaderViewModel.hasRunningLoaders();
    }
}
