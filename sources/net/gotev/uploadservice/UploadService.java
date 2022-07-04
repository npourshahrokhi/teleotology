package net.gotev.uploadservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.gotev.uploadservice.http.HttpStack;
import net.gotev.uploadservice.http.impl.HurlStack;

public final class UploadService extends Service {
    private static final String ACTION_UPLOAD_SUFFIX = ".uploadservice.action.upload";
    private static final String BROADCAST_ACTION_SUFFIX = ".uploadservice.broadcast.status";
    public static boolean EXECUTE_IN_FOREGROUND = true;
    public static HttpStack HTTP_STACK = new HurlStack();
    public static int KEEP_ALIVE_TIME_IN_SECONDS = 1;
    public static String NAMESPACE = "net.gotev";
    protected static final String PARAM_BROADCAST_DATA = "broadcastData";
    protected static final String PARAM_TASK_CLASS = "taskClass";
    protected static final String PARAM_TASK_PARAMETERS = "taskParameters";
    protected static final long PROGRESS_REPORT_INTERVAL = 166;
    private static final String TAG = UploadService.class.getSimpleName();
    protected static final int UPLOAD_NOTIFICATION_BASE_ID = 1234;
    public static int UPLOAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static volatile String foregroundUploadId = null;
    private static final Map<String, HttpUploadTask> uploadTasksMap = new ConcurrentHashMap();
    private int notificationIncrementalId = 0;
    private final BlockingQueue<Runnable> uploadTasksQueue = new LinkedBlockingQueue();
    private ThreadPoolExecutor uploadThreadPool;
    private PowerManager.WakeLock wakeLock;

    protected static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }

    protected static String getActionBroadcast() {
        return NAMESPACE + BROADCAST_ACTION_SUFFIX;
    }

    public static synchronized void stopUpload(String uploadId) {
        synchronized (UploadService.class) {
            HttpUploadTask removedTask = uploadTasksMap.get(uploadId);
            if (removedTask != null) {
                removedTask.cancel();
            }
        }
    }

    public static synchronized void stopAllUploads() {
        synchronized (UploadService.class) {
            if (!uploadTasksMap.isEmpty()) {
                for (String str : uploadTasksMap.keySet()) {
                    uploadTasksMap.get(str).cancel();
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, TAG);
        if (UPLOAD_POOL_SIZE <= 0) {
            UPLOAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
        }
        this.uploadThreadPool = new ThreadPoolExecutor(UPLOAD_POOL_SIZE, UPLOAD_POOL_SIZE, (long) KEEP_ALIVE_TIME_IN_SECONDS, TimeUnit.SECONDS, this.uploadTasksQueue);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || !getActionUpload().equals(intent.getAction())) {
            return shutdownIfThereArentAnyActiveTasks();
        }
        String str = TAG;
        Object[] objArr = new Object[4];
        objArr[0] = NAMESPACE;
        objArr[1] = Integer.valueOf(UPLOAD_POOL_SIZE);
        objArr[2] = Integer.valueOf(KEEP_ALIVE_TIME_IN_SECONDS);
        objArr[3] = EXECUTE_IN_FOREGROUND ? "enabled" : "disabled";
        Logger.info(str, String.format("Starting service with namespace: %s, upload pool size: %d, %ds idle thread keep alive time. Foreground execution is %s", objArr));
        HttpUploadTask currentTask = getTask(intent);
        if (currentTask == null) {
            return shutdownIfThereArentAnyActiveTasks();
        }
        this.notificationIncrementalId += 2;
        currentTask.setLastProgressNotificationTime(0).setNotificationId(this.notificationIncrementalId + UPLOAD_NOTIFICATION_BASE_ID);
        this.wakeLock.acquire();
        uploadTasksMap.put(currentTask.params.getId(), currentTask);
        this.uploadThreadPool.execute(currentTask);
        return 1;
    }

    private int shutdownIfThereArentAnyActiveTasks() {
        if (!uploadTasksMap.isEmpty()) {
            return 1;
        }
        stopSelf();
        return 2;
    }

    public void onDestroy() {
        super.onDestroy();
        stopAllUploads();
        this.uploadThreadPool.shutdown();
        if (EXECUTE_IN_FOREGROUND) {
            Logger.debug(TAG, "Stopping foreground execution");
            stopForeground(true);
        }
        Logger.debug(TAG, "UploadService destroyed");
    }

    /* access modifiers changed from: package-private */
    public HttpUploadTask getTask(Intent intent) {
        String taskClass = intent.getStringExtra(PARAM_TASK_CLASS);
        if (taskClass == null) {
            return null;
        }
        HttpUploadTask uploadTask = null;
        try {
            Class<?> task = Class.forName(taskClass);
            if (HttpUploadTask.class.isAssignableFrom(task)) {
                uploadTask = HttpUploadTask.class.cast(task.newInstance());
                uploadTask.init(this, intent);
            } else {
                Logger.error(TAG, taskClass + " does not extend HttpUploadTask!");
            }
            Logger.debug(TAG, "Successfully created new task with class: " + taskClass);
            return uploadTask;
        } catch (Exception exc) {
            Logger.error(TAG, "Error while instantiating new task", exc);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public synchronized boolean holdForegroundNotification(String uploadId, Notification notification) {
        boolean z = false;
        synchronized (this) {
            if (EXECUTE_IN_FOREGROUND) {
                if (foregroundUploadId == null) {
                    foregroundUploadId = uploadId;
                    Logger.debug(TAG, uploadId + " now holds the foreground notification");
                }
                if (uploadId.equals(foregroundUploadId)) {
                    startForeground(UPLOAD_NOTIFICATION_BASE_ID, notification);
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public synchronized void taskCompleted(String uploadId) {
        HttpUploadTask task = uploadTasksMap.remove(uploadId);
        if (EXECUTE_IN_FOREGROUND && task != null && task.params.getId().equals(foregroundUploadId)) {
            Logger.debug(TAG, uploadId + " now un-holded the foreground notification");
            foregroundUploadId = null;
        }
        if (uploadTasksMap.isEmpty()) {
            Logger.debug(TAG, "All tasks finished. UploadService is about to shutdown...");
            this.wakeLock.release();
            stopSelf();
        }
    }
}
