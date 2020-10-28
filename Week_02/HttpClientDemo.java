package springtest.utl;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientDemo {
    public final static void main(String[] args) throws Exception {
        String url = "http://localhost:8801/test";
        invoke(url);
    }

    private static void invoke(String url) throws IOException {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        HttpGet httpGet = new HttpGet(url);
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                                                    // 设置连接超时时间(单位毫秒)
                                                    .setConnectTimeout(5000)
                                                    // 设置请求超时时间(单位毫秒)
                                                    .setConnectionRequestTimeout(10000)
                                                    // socket读写超时时间(单位毫秒)
                                                    .setSocketTimeout(5000)
                                                    // 设置是否允许重定向(默认为true)
                                                    .setRedirectsEnabled(true).build();

        // 将上面的配置信息 运用到这个Get请求里
        httpGet.setConfig(requestConfig);

        httpGet.addHeader("Content-Type", "text/html");

        HttpContext context = new BasicHttpContext();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                System.out.println("Return data length is " + bytes.length);
                System.out.println("Return data content is " + new String(bytes));
            }
        } finally {
            httpClient.close();
        }

    }
}
