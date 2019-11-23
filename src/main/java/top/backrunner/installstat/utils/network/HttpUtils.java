package top.backrunner.installstat.utils.network;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

public class HttpUtils {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3965.0 Safari/537.36 Edg/80.0.334.2";

    public static String postJson(String url, HashMap<String, Object> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        // 设置编码
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), "utf-8");
        stringEntity.setContentEncoding("UTF-8");
        // 发送请求
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (response == null){
            return null;
        }
        // 获得回复
        if (response.getStatusLine().getStatusCode() != 200){
            return null;
        }
        // 提取内容并返回
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
