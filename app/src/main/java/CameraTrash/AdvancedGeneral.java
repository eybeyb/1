package CameraTrash;

import android.util.Log;

import com.baidu.ai.aip.utils.Base64Util;
import com.baidu.ai.aip.utils.FileUtil;
import com.baidu.ai.aip.utils.HttpUtil;
import okhttp3.*;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

import java.io.*;

import java.net.URLEncoder;

/**
 * 通用物体和场景识别
 */
public class AdvancedGeneral {

    // 修改方法签名，接收文件路径参数
    public static String advancedGeneral(String filePath) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 使用传入的文件路径
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.4d406ed116d2527f2db669c41c3749fe.2592000.1756883555.282335-44097859";

            String result = HttpUtil.post(url, accessToken, param);
            Log.d("result", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 需要添加依赖
     * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
     * <dependency>
     *     <groupId>com.squareup.okhttp3</groupId>
     *     <artifactId>okhttp</artifactId>
     *     <version>4.12.0</version>
     * </dependency>
     */

    class Sample {

        public final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().readTimeout(300, TimeUnit.SECONDS).build();

        public  void main(String []args) throws IOException{
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token?client_id=8mKllTTd2xWFFWa6kNQBe4hF&client_secret=q2sGrrB9hnqqGvDMOQvpmBmPargOP4W2&grant_type=client_credentials")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();

        }


    }
}
