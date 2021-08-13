package com.qiyei.network.server.ssl;



import android.content.Context;
import android.os.RemoteException;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * SSL 验证分为双向验证与单向验证
 * 单项验证有两种证书，分别为bks证书与crt证书
 */
public class SSLUtils {

    /**
     * 双向验证
     * @param caPath
     * @param clientPath
     * @param password
     * @return
     */
    public static SSLParams getSSLParams(Context context, String caPath, String clientPath, String password){
        SSLParams sslParams = new SSLParams();
        try {
            InputStream keyInput = null;
            InputStream trustInput = null;
            if (clientPath == null) {
                keyInput = context.getAssets().open("client.bks");
            } else {
                keyInput = new FileInputStream(clientPath);
            }
            if (caPath == null) {
                trustInput = context.getAssets().open("cs-root.crt");
            } else {
                trustInput = new FileInputStream(caPath);
            }
            sslParams = getSSLParamsInner(trustInput, keyInput, password);
            keyInput.close();
            trustInput.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sslParams;
    }

    /**
     * 单向校验中SSLSocketFactory X509TrustManager 参数的生成
     * 通常单向校验一般都是服务器不校验客户端的真实性，客户端去校验服务器的真实性
     * @param path 证书路径
     * @param password 证书密码
     * @return
     */
    public static SSLParams getSSLParamsSingle(String path, String password) {
        SSLParams sslParams = new SSLParams();

        try {
            //客户端信任的服务器端证书流
            InputStream bksStream = new FileInputStream(path);
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(SSLConstant.KEY_STORE_TYPE_BKS);

            try {
                //加载客户端信任的服务器证书
                trustStore.load(bksStream, password.toCharArray());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    bksStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            //生成用来校验服务器真实性的trustManager
            X509TrustManager trustManager = generateTrustManager(trustManagerFactory.getTrustManagers());

            SSLContext sslContext = SSLContext.getInstance(SSLConstant.TLS_PROTOCOL, SSLConstant.TLS_PROVIDER);
            //单向认证，客户端只校验服务端证书
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            //初始化SSLContext
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            sslParams.sSLSocketFactory = sslSocketFactory;
            sslParams.trustManager = trustManager;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslParams;
    }

    /**
     * 单向校验中,通过crt格式的证书生成SSLSocketFactory X509TrustManager 参数的生成
     * 通常在Android中，客户端用于校验服务器真实性的证书是支持BKS格式的，但是往往后台给的证书都是crt格式的
     * 当然我们可以自己生成BKS，但是想更方便一些我们也是可以直接使用crt格式的证书的
     * @param path
     * @param password
     * @return
     */
    public static SSLParams getSSLParamsSingleByCrt(String path, String password, String alias) {
        SSLParams sslParams = new SSLParams();

        try {
            //客户端信任的服务器端证书流
            InputStream crtStream = new FileInputStream(path);

            CertificateFactory cf = CertificateFactory.getInstance(SSLConstant.CERTIFICATE_TYPE);
            Certificate ca = cf.generateCertificate(crtStream);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore trustStore = KeyStore.getInstance(keyStoreType);

            try {
                trustStore.load(null, null);
                trustStore.setCertificateEntry(alias, ca);
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    crtStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            //生成用来校验服务器真实性的trustManager
            X509TrustManager trustManager = generateTrustManager(trustManagerFactory.getTrustManagers());

            SSLContext sslContext = SSLContext.getInstance(SSLConstant.TLS_PROTOCOL, SSLConstant.TLS_PROVIDER);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            //初始化SSLContext
            SSLSocketFactory sSLSocketFactory = sslContext.getSocketFactory();

            sslParams.sSLSocketFactory = sSLSocketFactory;
            sslParams.trustManager = trustManager;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslParams;
    }


    /**
     * 主机名校验方法
     * @param baseurl
     * @return
     */
    public static HostnameVerifier getHostnameVerifier(final String baseurl) {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                String host = baseurl;
                if (host != null && host.contains(hostname)) {
                    Log.i(SSLConstant.TAG,"hostnameVerifier ok");
                    return true;
                } else {
                    Log.w(SSLConstant.TAG,"hostnameVerifier waring");
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                }
            }
        };
    }

    /**
     *
     * @param trustManagers
     * @return
     */
    public static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static SSLParams getSSLParamsInner(InputStream trustInput, InputStream keyInput, String password) {
        SSLParams sslParams = new SSLParams();
        try {
            TrustManager[] trustManagers = prepareTrustManager(trustInput,"ca");
            X509TrustManager trustManager = generateTrustManager(trustManagers);
            KeyManager[] keyManagers = prepareKeyManager(keyInput, password);

            SSLContext sslContext = SSLContext.getInstance(SSLConstant.TLS_PROTOCOL, SSLConstant.TLS_PROVIDER);
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());

            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sslParams;
    }

    private static TrustManager[] prepareTrustManager(InputStream trustInput,String alias) {
        if (trustInput == null || alias == null){
            Log.e(SSLConstant.TAG,"prepareTrustManager error trustInput or alias is null,trustInput = " + trustInput + " alias = " + alias);
            return null;
        }
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance(SSLConstant.CERTIFICATE_TYPE);
            final String defaultType = KeyStore.getDefaultType();
            Log.i(SSLConstant.TAG,"prepareTrustManager KeyStore.getDefaultType()=" + defaultType);
            KeyStore keyStore = KeyStore.getInstance(defaultType);
            keyStore.load(null);
            try {
                keyStore.setCertificateEntry(alias, certificateFactory.generateCertificate(trustInput));
                if (trustInput != null) {
                    trustInput.close();
                }
            } catch (IOException e) {
                return null;
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static KeyManager[] prepareKeyManager(InputStream keyInput, String password) {
        try {
            if (keyInput == null || password == null) {
                Log.i(SSLConstant.TAG, "prepareKeyManager error, keyInput or password is null");
                return null;
            }

            KeyStore clientKeyStore = KeyStore.getInstance(SSLConstant.KEY_STORE_TYPE_BKS);
            clientKeyStore.load(keyInput, password.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            Log.i(SSLConstant.TAG, "prepareKeyManager success ");

            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager generateTrustManager(TrustManager[] trustManagers){
        X509TrustManager trustManager = null;
        try {
            if (trustManagers != null) {
                trustManager = new SafeTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        if (trustManager == null){
            trustManager = new UnSafeTrustManager();
        }

        return trustManager;
    }
}
