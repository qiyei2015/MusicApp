package com.qiyei.network.server.ssl;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SSLParams {

    public SSLSocketFactory sSLSocketFactory;
    public X509TrustManager trustManager;

    public SSLParams() {
    }
}
