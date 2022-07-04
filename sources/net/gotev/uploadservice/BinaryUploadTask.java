package net.gotev.uploadservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import net.gotev.uploadservice.http.HttpConnection;

public class BinaryUploadTask extends HttpUploadTask {
    /* access modifiers changed from: protected */
    public long getBodyLength() throws UnsupportedEncodingException {
        return this.params.getFiles().get(0).length();
    }

    /* access modifiers changed from: protected */
    public void writeBody(HttpConnection connection) throws IOException {
        writeStream(this.params.getFiles().get(0).getStream());
    }
}
