package net.gotev.uploadservice;

import android.content.Context;
import android.content.Intent;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public class MultipartUploadRequest extends HttpUploadRequest {
    private boolean isUtf8Charset;

    public MultipartUploadRequest(Context context, String uploadId, String serverUrl) {
        super(context, uploadId, serverUrl);
        this.isUtf8Charset = false;
    }

    public MultipartUploadRequest(Context context, String serverUrl) {
        this(context, (String) null, serverUrl);
    }

    /* access modifiers changed from: protected */
    public void initializeIntent(Intent intent) {
        super.initializeIntent(intent);
        intent.putExtra("multipartUtf8Charset", this.isUtf8Charset);
    }

    /* access modifiers changed from: protected */
    public Class getTaskClass() {
        return MultipartUploadTask.class;
    }

    /* access modifiers changed from: protected */
    public void validate() throws IllegalArgumentException, MalformedURLException {
        super.validate();
        if (this.params.getFiles().isEmpty()) {
            throw new IllegalArgumentException("You have to add at least one file to upload");
        }
    }

    public MultipartUploadRequest addFileToUpload(String path, String parameterName, String fileName, String contentType) throws FileNotFoundException, IllegalArgumentException {
        this.params.addFile(new UploadFile(path, parameterName, fileName, contentType));
        return this;
    }

    public MultipartUploadRequest addFileToUpload(String path, String parameterName, String fileName) throws FileNotFoundException, IllegalArgumentException {
        return addFileToUpload(path, parameterName, fileName, (String) null);
    }

    public MultipartUploadRequest addFileToUpload(String path, String parameterName) throws FileNotFoundException, IllegalArgumentException {
        return addFileToUpload(path, parameterName, (String) null, (String) null);
    }

    public MultipartUploadRequest setNotificationConfig(UploadNotificationConfig config) {
        super.setNotificationConfig(config);
        return this;
    }

    public MultipartUploadRequest setAutoDeleteFilesAfterSuccessfulUpload(boolean autoDeleteFiles) {
        super.setAutoDeleteFilesAfterSuccessfulUpload(autoDeleteFiles);
        return this;
    }

    public MultipartUploadRequest addHeader(String headerName, String headerValue) {
        super.addHeader(headerName, headerValue);
        return this;
    }

    public MultipartUploadRequest setBasicAuth(String username, String password) {
        super.setBasicAuth(username, password);
        return this;
    }

    public MultipartUploadRequest addParameter(String paramName, String paramValue) {
        super.addParameter(paramName, paramValue);
        return this;
    }

    public MultipartUploadRequest addArrayParameter(String paramName, String... array) {
        super.addArrayParameter(paramName, array);
        return this;
    }

    public MultipartUploadRequest addArrayParameter(String paramName, List<String> list) {
        super.addArrayParameter(paramName, list);
        return this;
    }

    public MultipartUploadRequest setMethod(String method) {
        super.setMethod(method);
        return this;
    }

    public MultipartUploadRequest setCustomUserAgent(String customUserAgent) {
        super.setCustomUserAgent(customUserAgent);
        return this;
    }

    public MultipartUploadRequest setMaxRetries(int maxRetries) {
        super.setMaxRetries(maxRetries);
        return this;
    }

    public MultipartUploadRequest setUsesFixedLengthStreamingMode(boolean fixedLength) {
        super.setUsesFixedLengthStreamingMode(fixedLength);
        return this;
    }

    public MultipartUploadRequest setUtf8Charset() {
        this.isUtf8Charset = true;
        return this;
    }
}
