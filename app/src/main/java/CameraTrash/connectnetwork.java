package CameraTrash;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.net.URLEncoder;


/**
 * 需要添加依赖
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 *     <groupId>com.squareup.okhttp3</groupId>
 *     <artifactId>okhttp</artifactId>
 *     <version>4.12.0</version>
 * </dependency>
 */

public class connectnetwork {
    public static final String API_KEY = "FZSmISbxNoGvvw9vqETHuSOM";
    public static final String SECRET_KEY = "BtlDBMpX4hkM2UXEqJDAZXM3MI2bebDd";

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().readTimeout(300, TimeUnit.SECONDS).build();

    public static String cn(String image) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // image 可以通过 getFileContentAsBase64("C:\fakepath\2025-08-04 15-58-43屏幕截图.png") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
        RequestBody body = RequestBody.create(mediaType, "image=" + getFileContentAsBase64(image, true));
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return response.body().string();

    }

    /**
     * 获取文件base64编码
     *
     * @param path      文件路径
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */
    static String getFileContentAsBase64(String path, boolean urlEncode) throws IOException {
        byte[] b = Files.readAllBytes(Paths.get(path));
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     * @throws JSONException JSON解析异常
     */
    static String getAccessToken() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}