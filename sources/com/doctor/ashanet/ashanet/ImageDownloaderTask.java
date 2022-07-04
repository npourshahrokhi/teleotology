package com.doctor.ashanet.ashanet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.widget.ImageView;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class ImageDownloaderTask {
    private static final int DELAY_BEFORE_PURGE = 30000;
    private static final int HARD_CACHE_CAPACITY = 40;
    private static final String LOG_TAG = "ImageDownloader";
    /* access modifiers changed from: private */
    public static final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(20, 0.75f, true) {
        private static final long serialVersionUID = -7190622541619388252L;

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
            if (size() <= 40) {
                return false;
            }
            ImageDownloaderTask.sSoftBitmapCache.put(eldest.getKey(), new SoftReference(eldest.getValue()));
            return true;
        }
    };
    /* access modifiers changed from: private */
    public static final ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<>(20);
    private final Handler purgeHandler = new Handler();
    private final Runnable purger = new Runnable() {
        public void run() {
            ImageDownloaderTask.this.clearCache();
        }
    };

    public void download(String url, ImageView imageView) {
        download(url, imageView, (String) null);
    }

    public void download(String url, ImageView imageView, String cookie) {
        resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap == null) {
            forceDownload(url, imageView, cookie);
            return;
        }
        cancelPotentialDownload(url, imageView);
        imageView.setImageBitmap(bitmap);
    }

    private void forceDownload(String url, ImageView imageView, String cookie) {
        if (url == null) {
            imageView.setImageDrawable((Drawable) null);
        } else if (cancelPotentialDownload(url, imageView)) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            imageView.setImageDrawable(new DownloadedDrawable(task));
            task.execute(new String[]{url, cookie});
        }
    }

    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    private void resetPurgeTimer() {
        this.purgeHandler.removeCallbacks(this.purger);
        this.purgeHandler.postDelayed(this.purger, 30000);
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
        if (bitmapDownloaderTask == null) {
            return true;
        }
        String bitmapUrl = bitmapDownloaderTask.url;
        if (bitmapUrl != null && bitmapUrl.equals(url)) {
            return false;
        }
        bitmapDownloaderTask.cancel(true);
        return true;
    }

    /* access modifiers changed from: private */
    public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                return ((DownloadedDrawable) drawable).getBitmapDownloaderTask();
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0023, code lost:
        if (r1 == null) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0025, code lost:
        r0 = r1.get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r0 == null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0032, code lost:
        sSoftBitmapCache.remove(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0037, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        r1 = sSoftBitmapCache.get(r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap getBitmapFromCache(java.lang.String r5) {
        /*
            r4 = this;
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r3 = sHardBitmapCache
            monitor-enter(r3)
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = sHardBitmapCache     // Catch:{ all -> 0x002f }
            java.lang.Object r0 = r2.get(r5)     // Catch:{ all -> 0x002f }
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x002f }
            if (r0 == 0) goto L_0x001a
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = sHardBitmapCache     // Catch:{ all -> 0x002f }
            r2.remove(r5)     // Catch:{ all -> 0x002f }
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = sHardBitmapCache     // Catch:{ all -> 0x002f }
            r2.put(r5, r0)     // Catch:{ all -> 0x002f }
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            r2 = r0
        L_0x0019:
            return r2
        L_0x001a:
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.SoftReference<android.graphics.Bitmap>> r2 = sSoftBitmapCache
            java.lang.Object r1 = r2.get(r5)
            java.lang.ref.SoftReference r1 = (java.lang.ref.SoftReference) r1
            if (r1 == 0) goto L_0x0037
            java.lang.Object r0 = r1.get()
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0
            if (r0 == 0) goto L_0x0032
            r2 = r0
            goto L_0x0019
        L_0x002f:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            throw r2
        L_0x0032:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.SoftReference<android.graphics.Bitmap>> r2 = sSoftBitmapCache
            r2.remove(r5)
        L_0x0037:
            r2 = 0
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.doctor.ashanet.ashanet.ImageDownloaderTask.getBitmapFromCache(java.lang.String):android.graphics.Bitmap");
    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private static final int IO_BUFFER_SIZE = 4096;
        private final WeakReference<ImageView> imageViewReference;
        /* access modifiers changed from: private */
        public String url;

        public BitmapDownloaderTask(ImageView imageView) {
            this.imageViewReference = new WeakReference<>(imageView);
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(String... params) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            this.url = params[0];
            HttpGet getRequest = new HttpGet(this.url);
            String cookie = params[1];
            if (cookie != null) {
                getRequest.setHeader("cookie", cookie);
            }
            try {
                HttpResponse response = client.execute(getRequest);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    Log.w(ImageDownloaderTask.LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + this.url);
                    if (client == null) {
                        return null;
                    }
                    client.close();
                    return null;
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        inputStream = entity.getContent();
                        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                        OutputStream outputStream2 = new BufferedOutputStream(dataStream, 4096);
                        try {
                            copy(inputStream, outputStream2);
                            outputStream2.flush();
                            byte[] data = dataStream.toByteArray();
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(data, 0, data.length);
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (outputStream2 != null) {
                                outputStream2.close();
                            }
                            entity.consumeContent();
                            if (client == null) {
                                return decodeByteArray;
                            }
                            client.close();
                            return decodeByteArray;
                        } catch (Throwable th) {
                            th = th;
                            outputStream = outputStream2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        entity.consumeContent();
                        throw th;
                    }
                } else {
                    if (client != null) {
                        client.close();
                    }
                    return null;
                }
            } catch (IOException e) {
                getRequest.abort();
                Log.w(ImageDownloaderTask.LOG_TAG, "I/O error while retrieving bitmap from " + this.url, e);
                if (client != null) {
                    client.close();
                }
            } catch (IllegalStateException e2) {
                getRequest.abort();
                Log.w(ImageDownloaderTask.LOG_TAG, "Incorrect URL: " + this.url);
                if (client != null) {
                    client.close();
                }
            } catch (Exception e3) {
                getRequest.abort();
                Log.w(ImageDownloaderTask.LOG_TAG, "Error while retrieving bitmap from " + this.url, e3);
                if (client != null) {
                    client.close();
                }
            } catch (Throwable th3) {
                if (client != null) {
                    client.close();
                }
                throw th3;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (bitmap != null) {
                synchronized (ImageDownloaderTask.sHardBitmapCache) {
                    ImageDownloaderTask.sHardBitmapCache.put(this.url, bitmap);
                }
            }
            if (this.imageViewReference != null) {
                ImageView imageView = (ImageView) this.imageViewReference.get();
                if (this == ImageDownloaderTask.getBitmapDownloaderTask(imageView)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public void copy(InputStream in, OutputStream out) throws IOException {
            byte[] b = new byte[4096];
            while (true) {
                int read = in.read(b);
                if (read != -1) {
                    out.write(b, 0, read);
                } else {
                    return;
                }
            }
        }
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(ViewCompat.MEASURED_STATE_MASK);
            this.bitmapDownloaderTaskReference = new WeakReference<>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return (BitmapDownloaderTask) this.bitmapDownloaderTaskReference.get();
        }
    }
}
