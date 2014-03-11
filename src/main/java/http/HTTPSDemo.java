package http;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HTTPSDemo {


    private static final String SERVER = "https://www.renrendai.com/getUnreadMailsCount.action?_=1394516687653";


    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost httpPost = new HttpPost(SERVER);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "vip"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            System.out.println("executing request" + httpPost.getRequestLine());

            // 执行请求
            MessageResult entity = httpClient.execute(httpPost, new ResponseHandler<MessageResult>() {
                @Override
                public MessageResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    Gson gson = new Gson();
                    return gson.fromJson(EntityUtils.toString(response.getEntity()), MessageResult.class);
                }
            });// 获得响应实体
            System.out.println("----------------------------------------");
            System.out.println(entity);
            if (entity != null) {
                System.out.println("Response content length: " + entity.getTotalCount());
            }

            doQueryOpenLoad();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    static class MessageResult {
        int totalCount;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    static void doQueryOpenLoad() throws IOException {
        Executor executor = Executor.newInstance(HttpClientBuilder.create().build());//用一个Executor实例处理多个关联的请求
        Content content = Request.Get("http://www.renrendai.com/lend/loanList!json.action?pageIndex=2&_=1394520651672").execute().returnContent();
        System.out.println(content);

    }


}
