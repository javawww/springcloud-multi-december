package ltd.vastchain.api;

import com.alibaba.fastjson.JSONObject;
import com.buddha.component.common.utils.DateTimeUtils;
import com.buddha.component.common.utils.QRCodeUtil;
import com.buddha.component.common.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import ltd.vastchain.api.vctc.DataUploadItem;
import ltd.vastchain.api.vctc.Signature;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: LiuZW
 * @CreateDate: 2019/12/9/009 11:39
 * @Version: 1.0
 */
@Log4j2
public class FungibleTokenTest {

    public static void main(String[] args) throws IOException {
        //getTokenBalance();
        //getTokenEveriPay();
        //createToken();
        queryChainId();
        //issueToken();
        //queryIssueChain();
        //transferToken();
        //queryTransferChain();
        getTokenBalance();
    }

    /**
     * @MethodName: createToken
     * @Description: 创建可信积分
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 15:06
     **/
    private static void createToken() throws IOException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        JSONObject args = new JSONObject();
        items.put("type",DataUploadItem.Type_Register_Test);
        items.put("args",args);
        args.put("id","SDXToken");
        args.put("name","SDX");
        args.put("fullName","SDX.Token");
        args.put("totalSupply","10000000000");
        args.put("precision",6);
        String iconUrl = "https://imgwechat.5idjs.com/djsLogo.png";
        BufferedImage srcImage = ImageIO.read(new URL(iconUrl));
        byte[] bytes = QRCodeUtil.imageToBytes(srcImage, "png");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        base64String = encoder.encodeBuffer(bytes).trim();
        base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        base64String = "data:image/png;base64," + base64String;
        args.put("icon",base64String);
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload/", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: queryChainId
     * @Description: 获取链上 ID 接口
     * @Param: []
     * @Return: void
     * 真实响应返回：{"status":"success","version":"1",
     * "items":[{"id":"501381","chainTrxId":"1d8288c15a49e5b8bd28904708dfd33b66adb7c1d39d9a0ef5ef169edbb64e1d","chainBlockNum":"84914068"}]}
     * @Author: LiuZW
     * @Date: 2019/12/9/009 15:41
     **/
    private static void queryChainId() throws IOException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        //items.put("type",DataUploadItem.Type_Register_Test);
        items.put("type",DataUploadItem.Type_Voluntary_Activity_Register);
        //items.put("id","SDXToken");
        items.put("id","EV1575880974226");
        items.put("queryType","i2c");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload/fetchOnChainIds", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }


    /**
     * @MethodName: issueToken
     * @Description: 发行可信积分
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 15:53
     **/
    private static void issueToken() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        JSONObject args = new JSONObject();
        items.put("type",DataUploadItem.Type_Issue_Test);
        items.put("args",args);
        String id = DateTimeUtils.getOdrInternal();
        log.info("id:{{}}",id);
        args.put("id", id);
        args.put("tokenAppId", Signature.APPID);
        args.put("tokenId","SDXToken");
        args.put("userId","804");
        args.put("amount","100");
        args.put("memo","test first member");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: queryIssueChain
     * @Description: 查询发行是否成功
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 16:20
     **/
    private static void queryIssueChain() throws IOException {

        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        items.put("type",DataUploadItem.Type_Issue_Test);
        items.put("id","20191209161952624");
        items.put("queryType","i2c");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload/fetchOnChainIds", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: transferToken
     * @Description: 转账可信积分
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 16:33
     **/
    private static void transferToken() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        JSONObject args = new JSONObject();
        items.put("type",DataUploadItem.Type_Transfer_Test);
        items.put("args",args);
        String id = DateTimeUtils.getOdrInternal();
        log.info("id:{{}}",id);
        args.put("id", id);
        args.put("tokenAppId", Signature.APPID);
        args.put("tokenId","SDXToken");
        args.put("fromUserId","804");
        args.put("toUserAppId",Signature.APPID);
        args.put("toUserId","75");
        args.put("amount","100");
        args.put("memo","test first transfer");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: queryTransferChain
     * @Description: 查询转账是否成功
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 16:20
     **/
    private static void queryTransferChain() throws IOException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items",list);
        JSONObject items = new JSONObject();
        items.put("type",DataUploadItem.Type_Transfer_Test);
        items.put("id","20191209163500040");
        items.put("queryType","i2c");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload/fetchOnChainIds", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: getTokenBalance
     * @Description: 查询可信积分余额
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 11:41
     **/
    private static void getTokenBalance() {
        HashMap<String, String> map = new HashMap<>();
        //发起请求的 appId
        //map.put("_appId", "");
        //该可信积分发行时所隶属的 tokenAppId
        map.put("tokenAppId", Signature.APPID);
        //创建的可信积分的 tokenId
        map.put("tokenId", "SDXToken");
        //查询用户余额的用户所在的 userAppId
        map.put("userAppId", Signature.APPID);
        //该用户在所属的 appId 中的 id
        // （即开发者在自己业务系统中的用户 id；该 id 无需事先在宇链云上注册或使用过） userId
        map.put("userId", "804");
        /*
        query参数格式为：_appId=_appId&tokenAppId=tokenAppId&tokenId=tokenId
         */
        String target = splitQuery(map);
        try {
            Main.callAPI("GET", "/fungible-token/balance", target, null);
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName: getTokenBalance
     * @Description: 生成可信积分扣款二维码（everiPay）
     * @Param: []
     * @Return: void
     * @Author: LiuZW
     * @Date: 2019/12/9/009 11:47
     **/
    private static void getTokenEveriPay() {
        HashMap<String, String> map = new HashMap<>();
        //发起请求的 appId
        //map.put("_appId", "");
        //该可信积分发行时所隶属的 tokenAppId
        map.put("tokenAppId", Signature.APPID);
        //创建的可信积分的 tokenId
        map.put("tokenId", "SDXToken");
        //查询用户余额的用户所在的 userAppId
        map.put("userAppId", Signature.APPID);
        //该用户在所属的 appId 中的 id
        // （即开发者在自己业务系统中的用户 id；该 id 无需事先在宇链云上注册或使用过） userId
        map.put("userId", "804");
        //为本二维码可扣款的最大金额。超过该金额，则扣款将一定失败。
        map.put("maxAmount", "100");
        //当前付款码链上id，纯随机的32位数字和字母，对同一笔支付应该是不变的
        map.put("uuid", "");
        /*
        query参数格式为：_appId=_appId&tokenAppId=tokenAppId&tokenId=tokenId
         */
        String target = splitQuery(map);
        try {
            Main.callAPI("GET", "/fungible-token/everiPay", target, null);
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }


    /**
     * @MethodName: splitQuery
     * @Description: GET请求拼接参数
     * @Param: [map]
     * @Return: java.lang.String
     * @Author: LiuZW
     * @Date: 2019/12/9/009 14:16
     **/
    private static String splitQuery(Map<String, String> map) {

        StringBuffer query = new StringBuffer();
        String target = "";
        map.forEach((k, v) -> {
            if (StringUtils.isNotNull(v)) {
                query.append("&").append(k).append("=").append(v);
            } else {
                log.info("key对应的value值为空字符串或null,key:{{}}", k);
            }
        });
        if (query.length() > 0) {
            target = query.substring(1, query.length());
        }
        log.info("target:{{}}", target);
        return target;
    }
}
