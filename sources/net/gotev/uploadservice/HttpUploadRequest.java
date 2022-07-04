package net.gotev.uploadservice;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public abstract class HttpUploadRequest {
    private static final String LOG_TAG = HttpUploadRequest.class.getSimpleName();
    private final Context context;
    protected final TaskParameters params = new TaskParameters();

    /* access modifiers changed from: protected */
    public abstract Class getTaskClass();

    public HttpUploadRequest(Context context2, String uploadId, String serverUrl) {
        if (context2 == null) {
            throw new IllegalArgumentException("Context MUST not be null!");
        }
        this.context = context2;
        if (uploadId == null || uploadId.isEmpty()) {
            Logger.debug(LOG_TAG, "null or empty upload ID. Generating it");
            this.params.setId(UUID.randomUUID().toString());
        } else {
            Logger.debug(LOG_TAG, "setting provided upload ID");
            this.params.setId(uploadId);
        }
        this.params.setUrl(serverUrl);
        Logger.debug(LOG_TAG, "Created new upload request to " + this.params.getUrl() + " with ID: " + this.params.getId());
    }

    public final String startUpload() throws IllegalArgumentException, MalformedURLException {
        validate();
        Intent intent = new Intent(getContext(), UploadService.class);
        initializeIntent(intent);
        intent.setAction(UploadService.getActionUpload());
        getContext().startService(intent);
        return this.params.getId();
    }

    /* access modifiers changed from: protected */
    public void initializeIntent(Intent intent) {
        intent.putExtra("taskParameters", this.params);
        Class taskClass = getTaskClass();
        if (taskClass == null) {
            throw new RuntimeException("The request must specify a task class!");
        }
        intent.putExtra("taskClass", taskClass.getName());
    }

    public HttpUploadRequest setNotificationConfig(UploadNotificationConfig config) {
        this.params.setNotificationConfig(config);
        return this;
    }

    public HttpUploadRequest setAutoDeleteFilesAfterSuccessfulUpload(boolean autoDeleteFiles) {
        this.params.setAutoDeleteSuccessfullyUploadedFiles(autoDeleteFiles);
        return this;
    }

    /* access modifiers changed from: protected */
    public void validate() throws IllegalArgumentException, MalformedURLException {
        if (this.params.getUrl() == null || "".equals(this.params.getUrl())) {
            throw new IllegalArgumentException("Request URL cannot be null or empty");
        } else if (this.params.getUrl().startsWith("http://") || this.params.getUrl().startsWith("https://")) {
            new URL(this.params.getUrl());
        } else {
            throw new IllegalArgumentException("Specify either http:// or https:// as protocol");
        }
    }

    public HttpUploadRequest addHeader(String headerName, String headerValue) {
        this.params.addRequestHeader(headerName, headerValue);
        return this;
    }

    public HttpUploadRequest setBasicAuth(String username, String password) {
        this.params.addRequestHeader("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), 0));
        return this;
    }

    public HttpUploadRequest addParameter(String paramName, String paramValue) {
        this.params.addRequestParameter(paramName, paramValue);
        return this;
    }

    public HttpUploadRequest addArrayParameter(String paramName, String... array) {
        for (String value : array) {
            this.params.addRequestParameter(paramName, value);
        }
        return this;
    }

    public HttpUploadRequest addArrayParameter(String paramName, List<String> list) {
        for (String value : list) {
            this.params.addRequestParameter(paramName, value);
        }
        return this;
    }

    public HttpUploadRequest setMethod(String method) {
        this.params.setMethod(method);
        return this;
    }

    public HttpUploadRequest setCustomUserAgent(String customUserAgent) {
        this.params.setCustomUserAgent(customUserAgent);
        return this;
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    public HttpUploadRequest setMaxRetries(int maxRetries) {
        this.params.setMaxRetries(maxRetries);
        return this;
    }

    public HttpUploadRequest setUsesFixedLengthStreamingMode(boolean fixedLength) {
        this.params.setUsesFixedLengthStreamingMode(fixedLength);
        return this;
    }
}
