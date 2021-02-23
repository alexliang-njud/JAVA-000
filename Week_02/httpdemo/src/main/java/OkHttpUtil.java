import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private static OkHttpClient client;

    private static final String DEFAULT_MEDIA_TYPE = "application/json; charset=utf-8";

    private static final int CONNECT_TIMEOUT = 5;

    private static final int READ_TIMEOUT = 7;

    private static final String USER_MESSAGE = "userMessage";

    private static final String DEVELOPER_MESSAGE = "developerMessage";

    private static final String GET = "GET";

    private static final String POST = "POST";

    private static final Gson gson = new Gson();

    /**
     * 单例模式  获取类实例
     *
     * @return client
     */
    private static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    public static String doGet(String url, String callMethod) throws HttpStatusException {

        try {
            long startTime = System.currentTimeMillis();
            addRequestLog(GET, callMethod, url, null, null);

            Request request = new Request.Builder().url(url).build();
            // 创建一个请求
            Response response = getInstance().newCall(request).execute();
            int httpCode = response.code();
            String result;
            if (response.body() != null) {
                result = response.body().string();
                addResponseLog(httpCode, result, startTime);
            } else {
                throw new RuntimeException("exception in OkHttpUtil,response body is null");
            }
            return handleHttpResponse(httpCode, result, response);
        } catch (Exception ex) {
            handleHttpThrowable(ex, url);
            return StringUtils.EMPTY;
        }
    }


    public static String doPost(String url, String postBody, String mediaType, String callMethod) throws HttpStatusException {
        try {
            long startTime = System.currentTimeMillis();
            addRequestLog(POST, callMethod, url, postBody, null);

            MediaType createMediaType = MediaType.parse(mediaType == null ? DEFAULT_MEDIA_TYPE : mediaType);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(createMediaType, postBody))
                    .build();

            Response response = getInstance().newCall(request).execute();
            int httpCode = response.code();
            String result;
            if (response.body() != null) {
                result = response.body().string();
                addResponseLog(httpCode, result, startTime);
            } else {
                throw new IOException("exception in OkHttpUtil,response body is null");
            }
            return handleHttpResponse(httpCode, result, response);
        } catch (Exception ex) {
            handleHttpThrowable(ex, url);
            return StringUtils.EMPTY;
        }
    }

    public static String doPost(String url, Map<String, String> parameterMap, String callMethod) throws HttpStatusException {
        try {
            long startTime = System.currentTimeMillis();
            List<String> parameterList = new ArrayList<>();
            FormBody.Builder builder = new FormBody.Builder();
            if (parameterMap.size() > 0) {
                parameterMap.keySet().forEach(parameterName -> {
                    String value = parameterMap.get(parameterName);
                    builder.add(parameterName, value);
                    parameterList.add(parameterName + ":" + value);
                });
            }

            addRequestLog(POST, callMethod, url, null, Arrays.toString(parameterList.toArray()));

            FormBody formBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = getInstance().newCall(request).execute();
            String result;
            int httpCode = response.code();
            if (response.body() != null) {
                result = response.body().string();
                addResponseLog(httpCode, result, startTime);
            } else {
                throw new IOException("exception in OkHttpUtil,response body is null");
            }
            return handleHttpResponse(httpCode, result, response);

        } catch (Exception ex) {
            handleHttpThrowable(ex, url);
            return StringUtils.EMPTY;
        }
    }


    private static void addRequestLog(String method, String callMethod, String url, String body, String formParam) {
        logger.debug("===========================request begin================================================");
        logger.debug("URI          : {}", url);
        logger.debug("Method       : {}", method);
        if (StringUtils.isNotBlank(body)) {
            logger.debug("Request body : {}", body);
        }
        if (StringUtils.isNotBlank(formParam)) {
            logger.debug("Request param: {}", formParam);
        }
        logger.debug("---------------------------request end--------------------------------------------------");
    }

    private static void addResponseLog(int httpCode, String result, long startTime) {
        long endTime = System.currentTimeMillis();
        logger.debug("Status       : {}", httpCode);
        logger.debug("Response     : {}", result);
        logger.debug("Time         : {} ms", endTime - startTime);
        logger.debug("===========================response end================================================");
    }

    private static String handleHttpResponse(int httpCode, String result, Response response) throws HttpStatusException {
        if (response.isSuccessful()) {
            return result;
        }
        HttpStatus httpStatus = HttpStatus.valueOf(httpCode);
        if (result.contains(USER_MESSAGE) && result.contains(DEVELOPER_MESSAGE)) {
            JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
            throw new HttpStatusException(httpStatus, jsonObject.get(USER_MESSAGE).getAsString(), jsonObject.get(DEVELOPER_MESSAGE).getAsString());
        } else {
            throw new HttpStatusException(httpStatus, result,"");
        }

    }

    private static void handleHttpThrowable(Exception ex, String url) throws HttpStatusException {
        if (ex instanceof HttpStatusException) {
            throw (HttpStatusException) ex;
        }
        logger.error("OkHttp error url: " + url, ex);
        if (ex instanceof SocketTimeoutException) {
            throw new RuntimeException("request time out of OkHttp when do get:" + url);
        }
        throw new RuntimeException(ex);
    }

}
