package com.landis.eoswallet.net.manage;

import android.content.Context;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.base.constant.Constant;
import com.landis.eoswallet.net.api.ApiService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManage {

    public static ApiService apiService;
    private static OkHttpClient okHttpClient;

    public static void init() {
        apiService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(getOkHttp())
                .callFactory(new CallFactoryProxy(okHttpClient) {
                    @Nullable
                    @Override
                    protected HttpUrl getNewUrl(String baseUrlName, Request request) {
                        if (baseUrlName.equals("json_bin")) {
                            String oldUrl = request.url().toString();
                            String newUrl = oldUrl.replace(Constant.BASE_URL, Constant.JSON_BIN_BASE_URL);
                            LogUtils.d("新的url"+newUrl);
                            return HttpUrl.get(URI.create(newUrl));
                        }
                        return null;
                    }
                })
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }


    private static OkHttpClient getOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(HttpInterceptor.requestBuilderInterceptor())
                    .proxy(Proxy.NO_PROXY)
                    .retryOnConnectionFailure(true)
                    .readTimeout(20, TimeUnit.SECONDS);
            clientBuilder.sslSocketFactory(getSLLContext().getSocketFactory());
            SSLParams sslSocketFactoryBase = getSslSocketFactoryBase(null, null, null);
            clientBuilder.sslSocketFactory(sslSocketFactoryBase.sSLSocketFactory, sslSocketFactoryBase.trustManager);
            clientBuilder.hostnameVerifier((hostname, session) -> true);
            okHttpClient = clientBuilder.build();
        }
        return okHttpClient;
    }

    /**
     * 信任所有证书
     *
     * @return
     */
    public static SSLContext getSLLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    public static SSLContext getSLLContext(Context context) {
        SSLContext sslContext = null;
        try {
            String crt = "-----BEGIN CERTIFICATE-----\n" +
                    "-----END CERTIFICATE-----\n";
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

            InputStream cerInputStream = new ByteArrayInputStream(crt.getBytes());
            Certificate ca = cf.generateCertificate(cerInputStream);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    //    public static Picasso getPicasso(Context c) {
//        return HttpsPicasso.PicassoHolder.getInstance(getOkHttp(c), c);
//    }
    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }


    private static SSLParams getSslSocketFactoryBase(X509TrustManager trustManager, InputStream bksFile, String password, InputStream... certificates) {
        SSLParams sslParams = new SSLParams();
        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            X509TrustManager manager;
            if (trustManager != null) {
                //优先使用用户自定义的TrustManager
                manager = trustManager;
            } else if (trustManagers != null) {
                //然后使用默认的TrustManager
                manager = chooseTrustManager(trustManagers);
            } else {
                //否则使用不安全的TrustManager
                manager = UnSafeTrustManager;
            }
            // 创建TLS类型的SSLContext对象， that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
            // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
            sslContext.init(keyManagers, new TrustManager[]{manager}, null);
            // 通过sslContext获取SSLSocketFactory对象
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = manager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientKeyStore, password.toCharArray());
            return kmf.getKeyManagers();
        } catch (Exception e) {
        }
        return null;
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            // 创建一个默认类型的KeyStore，存储我们信任的证书
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certStream : certificates) {
                String certificateAlias = Integer.toString(index++);
                // 证书工厂根据证书文件的流生成证书 cert
                Certificate cert = certificateFactory.generateCertificate(certStream);
                // 将 cert 作为可信证书放入到keyStore中
                keyStore.setCertificateEntry(certificateAlias, cert);
                try {
                    if (certStream != null) certStream.close();
                } catch (IOException e) {
                }
            }
            //我们创建一个默认类型的TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书
            tmf.init(keyStore);
            //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书
            return tmf.getTrustManagers();
        } catch (Exception e) {
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    /**
     * 为了解决客户端不信任服务器数字证书的问题，网络上大部分的解决方案都是让客户端不对证书做任何检查，
     * 这是一种有很大安全漏洞的办法
     */
    public static X509TrustManager UnSafeTrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };
}
