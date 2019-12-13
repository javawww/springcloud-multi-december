/**
 * 宇链区块链可信云（Vastchain Blockchain Trusted Cloud） Java 代码示例
 *
 * © 2019，杭州宇链科技有限公司，保留所有权利。
 */
package ltd.vastchain.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ltd.vastchain.api.vctc.*;
import ltd.vastchain.api.vctc.argsModels.VoluntaryActivitySignModel;
import okhttp3.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class Main {
    private static MediaType mediaType = MediaType.parse("application/json");

    public static void main(String[] args) throws VCTCException {
        log.info("55555");
	    // GET 命令测试
//        callAPI("GET", "/", "a=3&f=%20&b=5", "");

        // ======== 接下来我们来批量上传三条数据 ==========

        // ======== 第一条：注册一次志愿活动信息 ==========
        // 先按照文档将信息组合成一个 JSON
        // 这里我们已经帮忙定义了相关的类，省去了麻烦，直接用即可
        String activityId = "Es"+new Date().getTime()+"";// + new Date().getTime();
//        VoluntaryActivityRegisterModel modelRegister = new VoluntaryActivityRegisterModel() {{
//            id = activityId;
//            createTime = Long.toString(new Date().getTime());
//            title = "一起打扫大街吧";
//            desc = "今天我们隆重推出新项目：打扫大街";
//            organization = "李思省青少年基金会1";
//            organizationId = "zssqsnjjh";
//            openTime = "1556592183531";
//            closeTime = "1556592193531";
//            district = "李思省李四市王五区";
//            address = "红桃街777号";
//            memo = "现场有惊喜礼品";
//            categories = new String[] { "扫街" };
//            x = new HashMap<String, Object>() {{ // x 中存储机密信息
//               put("x_secret", "一般人我不告诉他"); // 使用 x_ 开头代表自定义的 key
//            }};
//        }};

        // ======== 第二条：上传一条签到数据 ==========
        // 先按照文档将信息组合成一个 JSON
        // 这里我们已经帮忙定义了相关的类，省去了麻烦，直接用即可
        VoluntaryActivitySignModel modelSignIn = new VoluntaryActivitySignModel() {{
            id = "testSignIn" + new Date().getTime();
            createTime = Long.toString(new Date().getTime());
            userId = "user1s";
            memo = "";
            parentId = activityId;
            x = new HashMap<String, Object>() {{ // x 中存储机密信息
                put("signerName", "李思"); // 签到姓名
                put("gps", new String[]{ "1.4325", "7.35343"}); // 签到姓名
            }};
        }};

        // ======== 第三条：上传一条签退数据 ==========
        // 先按照文档将信息组合成一个 JSON
        // 这里我们已经帮忙定义了相关的类，省去了麻烦，直接用即可
        VoluntaryActivitySignModel modelSignOut = new VoluntaryActivitySignModel() {{
            id = "testSignIn" + new Date().getTime();
            createTime = Long.toString(new Date().getTime());
            userId = "user0";
            memo = "";
            parentId = activityId;
            x = new HashMap<String, Object>() {{ // x 中存储机密信息（可选）
                put("signerName", "张三"); // 签到姓名（可选）
                put("gps", new String[]{ "1.4343", "2.35343"}); // 签到 GPS（可选）
            }};
            durationInMinutes = "423"; // 此处请务必填写正确，为签退时间和签到时间（毫秒）的差值
            actualCalculatedHours = "1"; // 实际入账的时数
        }};

        // 批量提交数据（每次最多可以提交 50 条数据。对于历史数据，为了提高效率，建议批量提交）
        // 如果上传数据重复，只要 id 相同，链上不会重复记录，所以不用担心偶尔因为特殊原因造成的重试出现重复数据
        callAPI("POST", "/common-chain-upload/", "", JSON.toJSONString(new DataUploadBody(new DataUploadItem[] {
                new DataUploadItem(DataUploadItem.Type_Voluntary_Activity_SignIn, modelSignIn)
        })));
//          ,
//        new DataUploadItem(DataUploadItem.Type_Voluntary_Activity_SignIn, modelSignIn),
//                new DataUploadItem(DataUploadItem.Type_Voluntary_Activity_SignOut, modelSignOut)
        // 接口：创建商户号
//        createMerchante();

        // 接口：商户登录
//        loginToken();

        // 接口：商户支付参数
//        callPaymentParams();

        // 接口：生成付款二维码所需 prepayId
//        callPaymentQRCode();

        // 接口： 获取支付二维码
//        wechatPayNative();

        // 接口：添加分润接受方
//        addProfitReceiver();

        // 接口： 执行分润
//        profitSharing();

        // 接口： 获取分润接收方列表
//        profitsharingreceiverList();

        // 接口： 退款
//        refundPay();

        // 接口：微信小程序支付参数
//        wechatPayParam();

        // 接口:创建微信 App 支付参数
//        wechatPayApp();

        // 测试
//        testJsonToObject();
    }

    private static void testJsonToObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status","success");
        jsonObject.put("version","1");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id","SM8516922504");
        jsonObject.put("data",jsonObject2);
        System.out.println(jsonObject.toJSONString());
        //
        String jsonStr = jsonObject.toJSONString();
        JSONObject jsonObject1 = JSON.parseObject(jsonStr);
        JSONObject jsonObject3 = JSON.parseObject(jsonObject1.get("data").toString());
        System.out.println(jsonObject3.getString("id"));
    }

    // 接口:创建微信 App 支付参数
    // 返回结果：
    private static void wechatPayApp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prepayId","PP9721086462");
        jsonObject.put("enableProfitSharing",true);
        try {
            callAPI("POST", "/submerchant-pay/wechatPayApp", "",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：微信小程序支付参数
    //    // 返回值：
    //    // {"status":"success","args":{"appId":"wx62c9d762ff83c31a","timeStamp":"1572427216",
    //    // "nonceStr":"42Jrz0TZ3XpAe7Fa","package":"prepay_id=wx3017201694212545733809b61803943800",
    //    // "signType":"MD5","paySign":"6CD88CF2B2A7AE099D87DC6DE1DA5A9A"},"version":"1"}
    private static void wechatPayParam() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prepayId","PP9525488179");
        jsonObject.put("openId","oq2em5OxY6Rgswpi_xDAcU_obZQo");
        try {
            callAPI("POST", "/submerchant-pay/wechatPay", "",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：退款
    // 返回值：
    private static void refundPay() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","PP1498723961");
        try {
            callAPI("POST", "/submerchant-pay/refund", "loginToken=1d10cb56d4560aa2a5942f0ce53c9156",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：创建商户号
    // 返回结果  {"status":"success","version":"1","data":{"id":"SM8516922504"}}
    private static void createMerchante() {
        JSONObject sObject = new JSONObject();
        sObject.put("displayName","TestDjs_A");
        sObject.put("pw","123456");
        sObject.put("disabled",false);
        sObject.put("appId","AzE5");
        sObject.put("parentMerchantId","PM0329160132");
        JSONObject pObject = new JSONObject();
        pObject.put("type","subMerchant");
        pObject.put("parameters",sObject);
        APIResponse apiRes = null;
        try {
            apiRes = callAPI("POST", "/merchant/", "",pObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(apiRes,true));
    }

    // 接口： 获取分润接收方列表
    // 返回结果：{"status":"success","version":"1","data":
    // [{"id":"PSR0761558369","type":"PERSONAL_WECHATID","account":"wygnm789",
    // "name":"仝照美","relation_type":"USER","timestamp":"2019-10-28T07:11:30.646Z"}]}
    private static void profitsharingreceiverList() {
        try {
            callAPI("GET", "/submerchant-pay/profitsharingreceiver", "loginToken=b0e89a32aba7aa3fe7462b601035e719",null);
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口： 执行分润
    // 返回结果：{"status":"success","version":"1"}
    private static void profitSharing() {
        JSONObject _jsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","PSR0761558369");
        jsonObject.put("type","amount");
        jsonObject.put("value","0.02");
        jsonObject.put("description","测试分润仝");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id","PSR5916740801");
        jsonObject2.put("type","amount");
        jsonObject2.put("value","0.01");
        jsonObject2.put("description","测试分润刘");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject2);


        _jsonObject.put("sharingPlan",jsonArray);
        System.out.println(_jsonObject.toJSONString());
        try {
            callAPI("POST", "/submerchant-pay/profitsharing/PP9783413519", "loginToken=b0e89a32aba7aa3fe7462b601035e719",_jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：添加分润接受方
    // 返回结果  {"status":"success","version":"1","id":"PSR0761558369"}  个人：仝照美
    // 返回结果  {"status":"success","version":"1","id":"PSR5916740801"}  个人：刘志炜
    private static void addProfitReceiver() {
        // 个人零钱
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","PERSONAL_WECHATID");
//        jsonObject.put("relation_type","USER");
//        jsonObject.put("account","wygnm789");
//        jsonObject.put("name","仝照美");
        // 个人零钱
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","PERSONAL_WECHATID");
//        jsonObject.put("relation_type","USER");
//        jsonObject.put("account","liuzhiwei5210");
//        jsonObject.put("name","刘志炜");
        // 企业商户
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","MERCHANT_ID");
        jsonObject.put("relation_type","PARTNER");
        jsonObject.put("account","1521608671");
        jsonObject.put("name","康宇超艺（深圳）科技有限公司");
        System.out.println(jsonObject.toJSONString());
        try {
            callAPI("POST", "/submerchant-pay/profitsharingreceiver", "loginToken=b0e89a32aba7aa3fe7462b601035e719",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口： 获取支付二维码
    // 返回结果
//    {"status":"success","args":{"return_code":"SUCCESS","return_msg":"OK","appid":"wx4889efd89ecbc71e",
//    "mch_id":"1532807991","sub_mch_id":"1559874331","nonce_str":"xzm7QPT6UuCUVVZ5","sign":"CC8317C2BC4118E2B22726847B8317F5",
//    "result_code":"SUCCESS","prepay_id":"wx281440323559774a606627e51422254600","trade_type":"NATIVE",
//    "code_url":"weixin://wxpay/bizpayurl?pr=r4Z0IDq","sub_appid":"wx62c9d762ff83c31a"},"version":"1"}
    private static void wechatPayNative() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prepayId","PP9783413519");
        try {
            callAPI("POST", "/submerchant-pay/wechatPayNative", "",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：生成付款二维码所需 prepayId
    // 返回结果  {"status":"success","version":"1","prepayId":"PP9783413519"}
    private static void callPaymentQRCode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subMerchantId","SM8118776400");
        jsonObject.put("totalAmount","0.10");
        jsonObject.put("orderId",null);
        jsonObject.put("extraInfo",null);
        System.out.println(jsonObject.toJSONString());
        try {
            callAPI("POST", "/submerchant-pay", "",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    // 接口：商户支付参数
    // 返回结果  {"status":"success","version":"1"}
    private static void callPaymentParams() {
        JSONObject sObject = new JSONObject();
        sObject.put("profitSharing",true);
        sObject.put("wechatAppId","wx62c9d762ff83c31a");
        sObject.put("wechatMchId","1559874331");
        sObject.put("notifyCallbackUrl","http://127.0.0.1/notify/callback");
        JSONObject pObject = new JSONObject();
        pObject.put("id","SM8118776400");
        pObject.put("paymentChannel","WechatNative");
        pObject.put("parameters",sObject);
        System.out.println(pObject.toJSONString());
        try {
            callAPI("PUT", "/merchant/paymentParams", "",pObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }


    // 返回结果 {"status":"success","version":"1","data":{"displayName":"TestDjsShrVc","userId":"SM8118776400",
    // "roles":["index","subMerchant"],"loginToken":"b0e89a32aba7aa3fe7462b601035e719"}}
    private static void loginToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","SM8118776400"); // loginToken  a86914e425e631364a4485de66d5c638
        jsonObject.put("pw","123456");
        try {
            callAPI("POST", "/merchant/login", "",jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    public static APIResponse callAPI(String method, String path, String query, String body) throws VCTCException {
        try {
            SigningResult result = Signature.sign(method, path, query, body);

            OkHttpClient client = new OkHttpClient();
            Request.Builder request = new Request.Builder();

            if (method.equals("GET")) {
                request = request.get();
            }else if(method.equals("PUT")){
                request = request.put(RequestBody.create(mediaType,body));
            }
            else {
                request = request.post(RequestBody.create(mediaType,body));
            }

            Request build = request
                    .url(result.fullUri)
                    .build();

            Response response = client.newCall(build).execute();
            //System.out.println(response.body().string());
            final APIResponse res = JSON.parseObject(response.body().string(), APIResponse.class);

            if (res.status.equals("success") || res.status.equals("pending")) {
                return res; // 成功
            }

            throw new VCTCException(res.error, res.msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
