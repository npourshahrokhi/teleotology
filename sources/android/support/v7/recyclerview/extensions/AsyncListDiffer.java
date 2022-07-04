package android.support.v7.recyclerview.extensions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class AsyncListDiffer<T> {
    /* access modifiers changed from: private */
    public final AsyncDifferConfig<T> mConfig;
    @Nullable
    private List<T> mList;
    /* access modifiers changed from: private */
    public int mMaxScheduledGeneration;
    @NonNull
    private List<T> mReadOnlyList = Collections.emptyList();
    private final ListUpdateCallback mUpdateCallback;

    public AsyncListDiffer(@NonNull RecyclerView.Adapter adapter, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this.mUpdateCallback = new AdapterListUpdateCallback(adapter);
        this.mConfig = new AsyncDifferConfig.Builder(diffCallback).build();
    }

    public AsyncListDiffer(@NonNull ListUpdateCallback listUpdateCallback, @NonNull AsyncDifferConfig<T> config) {
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = config;
    }

    @NonNull
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    public void submitList(final List<T> newList) {
        if (newList != this.mList) {
            final int runGeneration = this.mMaxScheduledGeneration + 1;
            this.mMaxScheduledGeneration = runGeneration;
            if (newList == null) {
                this.mUpdateCallback.onRemoved(0, this.mList.size());
                this.mList = null;
                this.mReadOnlyList = Collections.emptyList();
            } else if (this.mList == null) {
                this.mUpdateCallback.onInserted(0, newList.size());
                this.mList = newList;
                this.mReadOnlyList = Collections.unmodifiableList(newList);
            } else {
                final List<T> oldList = this.mList;
                this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
                    public void run() {
                        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                            public int getOldListSize() {
                                return oldList.size();
                            }

                            public int getNewListSize() {
                                return newList.size();
                            }

                            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
                            }

                            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
                            }
                        });
                        AsyncListDiffer.this.mConfig.getMainThreadExecutor().execute(new Runnable() {
                            public void run() {
                                if (AsyncListDiffer.this.mMaxScheduledGeneration == runGeneration) {
                                    AsyncListDiffer.this.latchList(newList, result);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void latchList(@NonNull List<T> newList, @NonNull DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        this.mList = newList;
        this.mReadOnlyList = Collections.unmodifiableList(newList);
    }
}
