package cn.miaonai.NyaCatPlugins.Util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.*;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public abstract class InsecureWebSocketClient extends WebSocketClient {

    public InsecureWebSocketClient(URI serverUri) {
        super(serverUri);
        if (serverUri.toString().startsWith("wss")) {
            setupInsecureSSL();
        }
    }

    private void setupInsecureSSL() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, createInsecureTrustManagers(), new java.security.SecureRandom());

            SSLSocketFactory factory = sslContext.getSocketFactory();
            this.setSocketFactory(factory);

            // 设置不验证主机名
            SSLParameters sslParameters = sslContext.getSupportedSSLParameters();
            sslParameters.setEndpointIdentificationAlgorithm("");
            this.setSSLParameters(sslParameters);

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("SSL配置失败", e);
        }
    }

    public void setSSLParameters(SSLParameters sslParameters) {
    }

    private TrustManager[] createInsecureTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };
    }

    @Override
    protected void onSetSSLParameters(SSLParameters sslParameters) {
        sslParameters.setEndpointIdentificationAlgorithm("");
    }


    // 其他回调方法...
}