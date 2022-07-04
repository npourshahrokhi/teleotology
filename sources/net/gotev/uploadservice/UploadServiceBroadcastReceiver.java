package net.gotev.uploadservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class UploadServiceBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && UploadService.getActionBroadcast().equals(intent.getAction())) {
            BroadcastData data = (BroadcastData) intent.getParcelableExtra("broadcastData");
            switch (data.getStatus()) {
                case ERROR:
                    onError(data.getId(), data.getException());
                    return;
                case COMPLETED:
                    onCompleted(data.getId(), data.getResponseCode(), data.getResponseBody());
                    return;
                case IN_PROGRESS:
                    onProgress(data.getId(), data.getProgressPercent());
                    onProgress(data.getId(), data.getUploadedBytes(), data.getTotalBytes());
                    return;
                case CANCELLED:
                    onCancelled(data.getId());
                    return;
                default:
                    return;
            }
        }
    }

    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadService.getActionBroadcast());
        context.registerReceiver(this, intentFilter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public void onProgress(String uploadId, int progress) {
    }

    public void onProgress(String uploadId, long uploadedBytes, long totalBytes) {
    }

    public void onError(String uploadId, Exception exception) {
    }

    public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
    }

    public void onCancelled(String uploadId) {
    }
}
