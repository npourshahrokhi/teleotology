package net.gotev.uploadservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import net.gotev.uploadservice.BroadcastData;
import net.gotev.uploadservice.http.HttpConnection;

public abstract class HttpUploadTask implements Runnable {
    private static final int BUFFER_SIZE = 4096;
    private static final String LOG_TAG = HttpUploadTask.class.getSimpleName();
    private HttpConnection connection;
    private long lastProgressNotificationTime;
    private NotificationCompat.Builder notification;
    private int notificationId;
    private NotificationManager notificationManager;
    protected TaskParameters params = null;
    protected UploadService service;
    protected boolean shouldContinue = true;
    protected long totalBodyBytes;
    protected long uploadedBodyBytes;

    /* access modifiers changed from: protected */
    public abstract long getBodyLength() throws UnsupportedEncodingException;

    /* access modifiers changed from: protected */
    public abstract void writeBody(HttpConnection httpConnection) throws IOException;

    /* access modifiers changed from: protected */
    public void init(UploadService service2, Intent intent) throws IOException {
        this.notificationManager = (NotificationManager) service2.getSystemService("notification");
        this.notification = new NotificationCompat.Builder(service2);
        this.service = service2;
        this.params = (TaskParameters) intent.getParcelableExtra("taskParameters");
    }

    public final void run() {
        createNotification();
        int attempts = 0;
        int errorDelay = 1000;
        while (attempts <= this.params.getMaxRetries() && this.shouldContinue) {
            attempts++;
            try {
                upload();
                break;
            } catch (Exception exc) {
                if (!this.shouldContinue) {
                    break;
                } else if (attempts > this.params.getMaxRetries()) {
                    broadcastError(exc);
                } else {
                    Logger.info(LOG_TAG, "Error in uploadId " + this.params.getId() + " on attempt " + attempts + ". Waiting " + (errorDelay / 1000) + "s before next attempt. " + exc.getMessage());
                    SystemClock.sleep((long) errorDelay);
                    errorDelay *= 10;
                    if (errorDelay > 600000) {
                        errorDelay = 600000;
                    }
                }
            }
        }
        if (!this.shouldContinue) {
            broadcastCancelled();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void upload() throws Exception {
        Logger.debug(LOG_TAG, "Starting upload task with ID " + this.params.getId());
        try {
            this.totalBodyBytes = getBodyLength();
            if (this.params.isCustomUserAgentDefined()) {
                this.params.addRequestHeader("User-Agent", this.params.getCustomUserAgent());
            }
            this.connection = UploadService.HTTP_STACK.createNewConnection(this.params.getMethod(), this.params.getUrl());
            this.connection.setHeaders(this.params.getRequestHeaders(), this.params.isUsesFixedLengthStreamingMode(), getBodyLength());
            writeBody(this.connection);
            int serverResponseCode = this.connection.getServerResponseCode();
            Logger.debug(LOG_TAG, "Server responded with HTTP " + serverResponseCode + " to upload with ID: " + this.params.getId());
            if (this.shouldContinue) {
                broadcastCompleted(serverResponseCode, this.connection.getServerResponseBody());
            }
        } finally {
            this.connection.close();
        }
    }

    /* access modifiers changed from: protected */
    public void onSuccessfulUpload() {
    }

    public final void cancel() {
        this.shouldContinue = false;
    }

    /* access modifiers changed from: protected */
    public final HttpUploadTask setLastProgressNotificationTime(long lastProgressNotificationTime2) {
        this.lastProgressNotificationTime = lastProgressNotificationTime2;
        return this;
    }

    /* access modifiers changed from: protected */
    public final HttpUploadTask setNotificationId(int notificationId2) {
        this.notificationId = notificationId2;
        return this;
    }

    /* access modifiers changed from: protected */
    public final void writeStream(InputStream stream) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int bytesRead = stream.read(buffer, 0, buffer.length);
            if (bytesRead > 0 && this.shouldContinue) {
                this.connection.writeBody(buffer, bytesRead);
                this.uploadedBodyBytes += (long) bytesRead;
                broadcastProgress(this.uploadedBodyBytes, this.totalBodyBytes);
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void broadcastProgress(long uploadedBytes, long totalBytes) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.lastProgressNotificationTime + 166) {
            setLastProgressNotificationTime(currentTime);
            Logger.debug(LOG_TAG, "Broadcasting upload progress for " + this.params.getId() + " Uploaded bytes: " + uploadedBytes + " out of " + totalBytes);
            this.service.sendBroadcast(new BroadcastData().setId(this.params.getId()).setStatus(BroadcastData.Status.IN_PROGRESS).setUploadedBytes(uploadedBytes).setTotalBytes(totalBytes).getIntent());
            updateNotificationProgress((int) uploadedBytes, (int) totalBytes);
        }
    }

    /* access modifiers changed from: protected */
    public final void broadcastCompleted(int responseCode, byte[] serverResponseBody) {
        boolean successfulUpload = responseCode / 100 == 2;
        if (successfulUpload) {
            if (this.params.isAutoDeleteSuccessfullyUploadedFiles() && !this.params.getFiles().isEmpty()) {
                for (UploadFile uploadFile : this.params.getFiles()) {
                    deleteFile(uploadFile.file);
                }
            }
            onSuccessfulUpload();
        }
        Logger.debug(LOG_TAG, "Broadcasting upload completed for " + this.params.getId());
        this.service.sendBroadcast(new BroadcastData().setId(this.params.getId()).setStatus(BroadcastData.Status.COMPLETED).setResponseCode(responseCode).setResponseBody(serverResponseBody).getIntent());
        if (successfulUpload) {
            updateNotificationCompleted();
        } else {
            updateNotificationError();
        }
        this.service.taskCompleted(this.params.getId());
    }

    /* access modifiers changed from: protected */
    public final void broadcastCancelled() {
        Logger.debug(LOG_TAG, "Broadcasting cancellation for upload with ID: " + this.params.getId());
        this.service.sendBroadcast(new BroadcastData().setId(this.params.getId()).setStatus(BroadcastData.Status.CANCELLED).getIntent());
        updateNotificationError();
        this.service.taskCompleted(this.params.getId());
    }

    /* access modifiers changed from: protected */
    public final boolean deleteFile(File fileToDelete) {
        boolean deleted = false;
        try {
            deleted = fileToDelete.delete();
            if (!deleted) {
                Logger.error(LOG_TAG, "Unable to delete: " + fileToDelete.getAbsolutePath());
            } else {
                Logger.info(LOG_TAG, "Successfully deleted: " + fileToDelete.getAbsolutePath());
            }
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Error while deleting: " + fileToDelete.getAbsolutePath() + " Check if you granted: android.permission.WRITE_EXTERNAL_STORAGE", exc);
        }
        return deleted;
    }

    private void broadcastError(Exception exception) {
        Logger.info(LOG_TAG, "Broadcasting error for upload with ID: " + this.params.getId() + ". " + exception.getMessage());
        this.service.sendBroadcast(new BroadcastData().setId(this.params.getId()).setStatus(BroadcastData.Status.ERROR).setException(exception).getIntent());
        updateNotificationError();
        this.service.taskCompleted(this.params.getId());
    }

    private void createNotification() {
        if (this.params.getNotificationConfig() != null) {
            this.notification.setContentTitle(this.params.getNotificationConfig().getTitle()).setContentText(this.params.getNotificationConfig().getInProgressMessage()).setContentIntent(this.params.getNotificationConfig().getPendingIntent(this.service)).setSmallIcon(this.params.getNotificationConfig().getIconResourceID()).setGroup(UploadService.NAMESPACE).setProgress(100, 0, true).setOngoing(true);
            Notification builtNotification = this.notification.build();
            if (this.service.holdForegroundNotification(this.params.getId(), builtNotification)) {
                this.notificationManager.cancel(this.notificationId);
            } else {
                this.notificationManager.notify(this.notificationId, builtNotification);
            }
        }
    }

    private void updateNotificationProgress(int uploadedBytes, int totalBytes) {
        if (this.params.getNotificationConfig() != null) {
            this.notification.setContentTitle(this.params.getNotificationConfig().getTitle()).setContentText(this.params.getNotificationConfig().getInProgressMessage()).setContentIntent(this.params.getNotificationConfig().getPendingIntent(this.service)).setSmallIcon(this.params.getNotificationConfig().getIconResourceID()).setGroup(UploadService.NAMESPACE).setProgress(totalBytes, uploadedBytes, false).setOngoing(true);
            Notification builtNotification = this.notification.build();
            if (this.service.holdForegroundNotification(this.params.getId(), builtNotification)) {
                this.notificationManager.cancel(this.notificationId);
            } else {
                this.notificationManager.notify(this.notificationId, builtNotification);
            }
        }
    }

    private void setRingtone() {
        if (this.params.getNotificationConfig().isRingToneEnabled()) {
            this.notification.setSound(RingtoneManager.getActualDefaultRingtoneUri(this.service, 2));
            this.notification.setOnlyAlertOnce(false);
        }
    }

    private void updateNotificationCompleted() {
        if (this.params.getNotificationConfig() != null) {
            this.notificationManager.cancel(this.notificationId);
            if (!this.params.getNotificationConfig().isAutoClearOnSuccess()) {
                this.notification.setContentTitle(this.params.getNotificationConfig().getTitle()).setContentText(this.params.getNotificationConfig().getCompletedMessage()).setContentIntent(this.params.getNotificationConfig().getPendingIntent(this.service)).setAutoCancel(this.params.getNotificationConfig().isClearOnAction()).setSmallIcon(this.params.getNotificationConfig().getIconResourceID()).setGroup(UploadService.NAMESPACE).setProgress(0, 0, false).setOngoing(false);
                setRingtone();
                this.notificationManager.notify(this.notificationId + 1, this.notification.build());
            }
        }
    }

    private void updateNotificationError() {
        if (this.params.getNotificationConfig() != null) {
            this.notificationManager.cancel(this.notificationId);
            this.notification.setContentTitle(this.params.getNotificationConfig().getTitle()).setContentText(this.params.getNotificationConfig().getErrorMessage()).setContentIntent(this.params.getNotificationConfig().getPendingIntent(this.service)).setAutoCancel(this.params.getNotificationConfig().isClearOnAction()).setSmallIcon(this.params.getNotificationConfig().getIconResourceID()).setGroup(UploadService.NAMESPACE).setProgress(0, 0, false).setOngoing(false);
            setRingtone();
            this.notificationManager.notify(this.notificationId + 1, this.notification.build());
        }
    }
}
