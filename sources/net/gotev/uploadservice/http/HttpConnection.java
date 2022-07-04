package net.gotev.uploadservice.http;

import java.io.IOException;
import java.util.List;
import net.gotev.uploadservice.NameValue;

public interface HttpConnection {
    void close();

    byte[] getServerResponseBody() throws IOException;

    int getServerResponseCode() throws IOException;

    void setHeaders(List<NameValue> list, boolean z, long j) throws IOException;

    void writeBody(byte[] bArr) throws IOException;

    void writeBody(byte[] bArr, int i) throws IOException;
}
