package net.gotev.uploadservice;

import android.content.Intent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import net.gotev.uploadservice.http.HttpConnection;

public class MultipartUploadTask extends HttpUploadTask {
    private static final String NEW_LINE = "\r\n";
    protected static final String PARAM_UTF8_CHARSET = "multipartUtf8Charset";
    private static final String TWO_HYPHENS = "--";
    private final Charset US_ASCII = Charset.forName("US-ASCII");
    private String boundary;
    private byte[] boundaryBytes;
    private boolean isUtf8Charset;
    private byte[] trailerBytes;

    /* access modifiers changed from: protected */
    public void init(UploadService service, Intent intent) throws IOException {
        super.init(service, intent);
        this.boundary = getBoundary();
        this.boundaryBytes = getBoundaryBytes();
        this.trailerBytes = getTrailerBytes();
        this.isUtf8Charset = intent.getBooleanExtra(PARAM_UTF8_CHARSET, false);
        if (this.params.getFiles().size() <= 1) {
            this.params.addRequestHeader("Connection", "close");
        } else {
            this.params.addRequestHeader("Connection", "Keep-Alive");
        }
        this.params.addRequestHeader("Content-Type", "multipart/form-data; boundary=" + this.boundary);
    }

    /* access modifiers changed from: protected */
    public long getBodyLength() throws UnsupportedEncodingException {
        return getRequestParametersLength() + getFilesLength() + ((long) this.trailerBytes.length);
    }

    /* access modifiers changed from: protected */
    public void writeBody(HttpConnection connection) throws IOException {
        writeRequestParameters(connection);
        writeFiles(connection);
        connection.writeBody(this.trailerBytes);
    }

    private String getBoundary() {
        return "-------AndroidUploadService" + System.currentTimeMillis();
    }

    private byte[] getBoundaryBytes() throws UnsupportedEncodingException {
        return ("\r\n--" + this.boundary + NEW_LINE).getBytes(this.US_ASCII);
    }

    private byte[] getTrailerBytes() throws UnsupportedEncodingException {
        return ("\r\n--" + this.boundary + TWO_HYPHENS + NEW_LINE).getBytes(this.US_ASCII);
    }

    private long getFilesLength() throws UnsupportedEncodingException {
        long total = 0;
        for (UploadFile file : this.params.getFiles()) {
            total += file.getTotalMultipartBytes((long) this.boundaryBytes.length, this.isUtf8Charset);
        }
        return total;
    }

    private long getRequestParametersLength() throws UnsupportedEncodingException {
        long parametersBytes = 0;
        if (!this.params.getRequestParameters().isEmpty()) {
            for (NameValue parameter : this.params.getRequestParameters()) {
                parametersBytes += (long) (this.boundaryBytes.length + parameter.getMultipartBytes(this.isUtf8Charset).length);
            }
        }
        return parametersBytes;
    }

    private void writeRequestParameters(HttpConnection connection) throws IOException {
        if (!this.params.getRequestParameters().isEmpty()) {
            for (NameValue parameter : this.params.getRequestParameters()) {
                connection.writeBody(this.boundaryBytes);
                byte[] formItemBytes = parameter.getMultipartBytes(this.isUtf8Charset);
                connection.writeBody(formItemBytes);
                this.uploadedBodyBytes += (long) (this.boundaryBytes.length + formItemBytes.length);
                broadcastProgress(this.uploadedBodyBytes, this.totalBodyBytes);
            }
        }
    }

    private void writeFiles(HttpConnection connection) throws IOException {
        for (UploadFile file : this.params.getFiles()) {
            if (this.shouldContinue) {
                connection.writeBody(this.boundaryBytes);
                byte[] headerBytes = file.getMultipartHeader(this.isUtf8Charset);
                connection.writeBody(headerBytes);
                this.uploadedBodyBytes += (long) (this.boundaryBytes.length + headerBytes.length);
                broadcastProgress(this.uploadedBodyBytes, this.totalBodyBytes);
                writeStream(file.getStream());
            } else {
                return;
            }
        }
    }
}
