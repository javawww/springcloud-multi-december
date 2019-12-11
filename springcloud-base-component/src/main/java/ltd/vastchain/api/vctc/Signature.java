package ltd.vastchain.api.vctc;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 计算上链接口所需要的签名。
 */
public class Signature {
    /**
     * AppID，默认为测试账号；在非生产环境严禁使用正式账号，否则会导致
     * 测试数据上传到区块链主网；请反复测试无误后再在正式环境中切换
     */
    public static final String APPID = "AzE5";
    /**
     * 【警告】
     * AppSecret 是绝密资料，为了安全，像下面这样直接写在程序中是不对的。
     * 1. 禁止写在程序中
     * 2. 禁止写在 Git / SVN 仓库
     * 3. 应存储在普通程序员和无关人员无法访问到的安全加固的隔离机器上
     * 4. 对于涉及金融资产、代币等需要额外安全性的情况，建议将本类放
     *    在一个和大多数无关人员物理/网络隔离的机器上，把本类的签名方法
     *    作为一个服务，通过 HTTP 接口对其他程序提供签名服务，但不对任
     *    何人公开 AppSecret。
     * 5. 泄露 AppSecret 可能会导致无关人员伪造数据并上传至区块链上。
     * 6. 请不要删除这段警告，以方便其他工程师了解。
     *
     * =========================================================
     *                                    杭州宇链科技有限公司
     */
    private static final String APPSECRET = "u4VcwCrZ0tD$ozhE";
    private static final String PREFIX = "https://v1.api.tc.vastchain.ltd";

    /**
     * 对一个宇链云 API 调用进行签名，返回签名字符串。此方法中 query 必须包含 _appId 和 _t，不能包含 _s
     * @param httpMethod GET / POST
     * @param path 以“/”开头
     * @param query 如 _appid=AzE5&_t=32093029
     * @param body 当进行 POST 请求时，要提供序列化后的请求 JSON 串
     * @return
     * @throws Exception
     */
    public static SigningResult sign(String httpMethod, String path, String query, String body) throws MalformedURLException {
        if (query == null) {
            query = "";
        }

        // 在 Query 中拼接 _appid 和 时间戳 _t
        String appendedQuery = "_appid=" + APPID + "&_t=" + new Date().getTime();

        if (query.equals("")) {
            query = appendedQuery;
        }
        else {
            query = query + "&" + appendedQuery;
        }

        // 解析 query 并进行字典序排序
        TreeMap<String, List<String>> queryList = new TreeMap<>(splitQuery(new URL(PREFIX + path + "?" + query)));

        // 使用排序后的结果拼接 query
        String sortedQuery = "", sortedEncodedQuery = "";

        for (Map.Entry<String, List<String>> entry : queryList.entrySet()) {
            if (entry.getKey().equals("_s")) break;
            if (entry.getKey().equals("_appid") && entry.getValue().size() != 1) throw new IllegalArgumentException("query could not contain _appid");
            if (entry.getKey().equals("_t") && entry.getValue().size() != 1) throw new IllegalArgumentException("query could not contain _t");

            String item = entry.getKey() + "=" + String.join("&" + entry.getKey() + "=", entry.getValue());

            for (int i = 0; i < entry.getValue().size(); ++i) {
                try {
                    entry.getValue().set(i, URLEncoder.encode(entry.getValue().get(i), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            String itemEncoded = entry.getKey() + "=" + String.join("&" + entry.getKey() + "=", entry.getValue());

            if (sortedQuery.equals("")) {
                sortedQuery = item;
                sortedEncodedQuery = itemEncoded;

            }
            else {
                sortedQuery += "&" + item;
                sortedEncodedQuery += "&" + itemEncoded;
            }
        }

        String toBeSignatured;
        httpMethod = httpMethod.toUpperCase();

        if (httpMethod.equals("POST") && body != null && !body.equals("")) {
            toBeSignatured = httpMethod + " " + path + "\n" + sortedQuery + "\n" + body;
        }
        else if(httpMethod.equals("PUT") && body != null && !body.equals("")){
            toBeSignatured = httpMethod + " " + path + "\n" + sortedQuery + "\n" + body;
        }
        else {
            toBeSignatured = httpMethod + " " + path + "\n" + sortedQuery;
        }

        String signature = HMACSHA256(toBeSignatured, APPSECRET);

        SigningResult result = new SigningResult();
        result.signature = signature;
        result.fullUri = PREFIX + path + "?" + sortedEncodedQuery + "&_s=" + signature;
        result.query = sortedQuery;

        return result;
    }

    private static Map<String, List<String>> splitQuery(URL url) {
        try {
            final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
            final String[] pairs = url.getQuery().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                query_pairs.get(key).add(value);
            }
            return query_pairs;
        }
        catch (UnsupportedEncodingException ex) { return null; }
    }

    /**
     * 生成 HMAC-SHA256 签名。
     * @param data 待签名字符串
     * @param key 密钥
     * @return
     */
    private static String HMACSHA256(String data, String key)  {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();

            return null;
        }
    }
}
