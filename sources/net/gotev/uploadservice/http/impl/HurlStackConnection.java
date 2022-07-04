package net.gotev.uploadservice.http.impl;

import android.os.Build;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.NameValue;
import net.gotev.uploadservice.http.HttpConnection;

public class HurlStackConnection implements HttpConnection {
    private static final int BUFFER_SIZE = 4096;
    private static final String LOG_TAG = HurlStackConnection.class.getSimpleName();
    private HttpURLConnection mConnection;

    public HurlStackConnection(String method, String url, boolean followRedirects, boolean useCaches, int connectTimeout, int readTimeout) throws IOException {
        Logger.debug(getClass().getSimpleName(), "creating new connection");
        this.mConnection = (HttpURLConnection) new URL(url).openConnection();
        this.mConnection.setDoInput(true);
        this.mConnection.setDoOutput(true);
        this.mConnection.setConnectTimeout(connectTimeout);
        this.mConnection.setReadTimeout(readTimeout);
        this.mConnection.setUseCaches(useCaches);
        this.mConnection.setInstanceFollowRedirects(followRedirects);
        this.mConnection.setRequestMethod(method);
    }

    public void setHeaders(List<NameValue> requestHeaders, boolean isFixedLengthStreamingMode, long totalBodyBytes) throws IOException {
        if (!isFixedLengthStreamingMode) {
            this.mConnection.setChunkedStreamingMode(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.mConnection.setFixedLengthStreamingMode(totalBodyBytes);
        } else if (totalBodyBytes > 2147483647L) {
            throw new RuntimeException("You need Android API version 19 or newer to upload more than 2GB in a single request using fixed size content length. Try switching to chunked mode instead, but make sure your server side supports it!");
        } else {
            this.mConnection.setFixedLengthStreamingMode((int) totalBodyBytes);
        }
        for (NameValue param : requestHeaders) {
            this.mConnection.setRequestProperty(param.getName(), param.getValue());
        }
    }

    public void writeBody(byte[] bytes) throws IOException {
        this.mConnection.getOutputStream().write(bytes, 0, bytes.length);
    }

    public void writeBody(byte[] bytes, int lengthToWriteFromStart) throws IOException {
        this.mConnection.getOutputStream().write(bytes, 0, lengthToWriteFromStart);
    }

    public int getServerResponseCode() throws IOException {
        return this.mConnection.getResponseCode();
    }

    public byte[] getServerResponseBody() throws IOException {
        InputStream stream;
        InputStream stream2 = null;
        try {
            if (this.mConnection.getResponseCode() / 100 == 2) {
                stream = this.mConnection.getInputStream();
            } else {
                stream = this.mConnection.getErrorStream();
            }
            byte[] responseBodyAsByteArray = getResponseBodyAsByteArray(stream2);
            if (stream2 != null) {
                try {
                } catch (Exception exc) {
                    Logger.error(LOG_TAG, "Error while closing server response stream", exc);
                }
            }
            return responseBodyAsByteArray;
        } finally {
            if (stream2 != null) {
                try {
                    stream2.close();
                } catch (Exception exc2) {
                    Logger.error(LOG_TAG, "Error while closing server response stream", exc2);
                }
            }
        }
    }

    private byte[] getResponseBodyAsByteArray(InputStream inputStream) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            try {
                int bytesRead = inputStream.read(buffer, 0, buffer.length);
                if (bytesRead <= 0) {
                    break;
                }
                byteStream.write(buffer, 0, bytesRead);
            } catch (Exception e) {
            }
        }
        return byteStream.toByteArray();
    }

    public void close() {
        Logger.debug(getClass().getSimpleName(), "closing connection");
        if (this.mConnection != null) {
            try {
                this.mConnection.getInputStream().close();
            } catch (Exception e) {
            }
            try {
                this.mConnection.getOutputStream().flush();
                this.mConnection.getOutputStream().close();
            } catch (Exception e2) {
            }
            try {
                this.mConnection.disconnect();
            } catch (Exception exc) {
                Logger.error(LOG_TAG, "Error while closing connection", exc);
            }
        }
    }
}
