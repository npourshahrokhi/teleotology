package net.gotev.uploadservice.http.impl;

import java.io.IOException;
import net.gotev.uploadservice.http.HttpConnection;
import net.gotev.uploadservice.http.HttpStack;

public class HurlStack implements HttpStack {
    private int mConnectTimeout;
    private boolean mFollowRedirects;
    private int mReadTimeout;
    private boolean mUseCaches;

    public HurlStack() {
        this.mFollowRedirects = true;
        this.mUseCaches = false;
        this.mConnectTimeout = 15000;
        this.mReadTimeout = 30000;
    }

    public HurlStack(boolean followRedirects, boolean useCaches, int connectTimeout, int readTimeout) {
        this.mFollowRedirects = followRedirects;
        this.mUseCaches = useCaches;
        this.mConnectTimeout = connectTimeout;
        this.mReadTimeout = readTimeout;
    }

    public HttpConnection createNewConnection(String method, String url) throws IOException {
        return new HurlStackConnection(method, url, this.mFollowRedirects, this.mUseCaches, this.mConnectTimeout, this.mReadTimeout);
    }
}
