package net.gotev.uploadservice;

import android.content.Context;
import java.io.FileNotFoundException;
import java.util.List;

public class BinaryUploadRequest extends HttpUploadRequest {
    public BinaryUploadRequest(Context context, String uploadId, String serverUrl) {
        super(context, uploadId, serverUrl);
    }

    public BinaryUploadRequest(Context context, String serverUrl) {
        this(context, (String) null, serverUrl);
    }

    /* access modifiers changed from: protected */
    public Class getTaskClass() {
        return BinaryUploadTask.class;
    }

    public BinaryUploadRequest setFileToUpload(String path) throws FileNotFoundException {
        this.params.getFiles().clear();
        this.params.addFile(new UploadFile(path));
        return this;
    }

    public BinaryUploadRequest setNotificationConfig(UploadNotificationConfig config) {
        super.setNotificationConfig(config);
        return this;
    }

    public BinaryUploadRequest setAutoDeleteFilesAfterSuccessfulUpload(boolean autoDeleteFiles) {
        super.setAutoDeleteFilesAfterSuccessfulUpload(autoDeleteFiles);
        return this;
    }

    public BinaryUploadRequest addHeader(String headerName, String headerValue) {
        super.addHeader(headerName, headerValue);
        return this;
    }

    public BinaryUploadRequest setBasicAuth(String username, String password) {
        super.setBasicAuth(username, password);
        return this;
    }

    public BinaryUploadRequest setMethod(String method) {
        super.setMethod(method);
        return this;
    }

    public BinaryUploadRequest setCustomUserAgent(String customUserAgent) {
        super.setCustomUserAgent(customUserAgent);
        return this;
    }

    public BinaryUploadRequest setMaxRetries(int maxRetries) {
        super.setMaxRetries(maxRetries);
        return this;
    }

    public BinaryUploadRequest setUsesFixedLengthStreamingMode(boolean fixedLength) {
        super.setUsesFixedLengthStreamingMode(fixedLength);
        return this;
    }

    public HttpUploadRequest addParameter(String paramName, String paramValue) {
        logDoesNotSupportParameters();
        return this;
    }

    public HttpUploadRequest addArrayParameter(String paramName, String... array) {
        logDoesNotSupportParameters();
        return this;
    }

    public HttpUploadRequest addArrayParameter(String paramName, List<String> list) {
        logDoesNotSupportParameters();
        return this;
    }

    private void logDoesNotSupportParameters() {
        Logger.error(getClass().getSimpleName(), "This upload method does not support adding parameters");
    }
}
