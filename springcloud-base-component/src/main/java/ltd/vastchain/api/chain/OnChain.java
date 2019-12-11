package ltd.vastchain.api.chain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buddha.component.common.utils.RandomUtil;
import lombok.extern.log4j.Log4j2;
import ltd.vastchain.api.Main;
import ltd.vastchain.api.VCTCException;
import ltd.vastchain.api.chain.entity.ChainIdMsg;
import ltd.vastchain.api.vctc.DataUploadBody;
import ltd.vastchain.api.vctc.DataUploadItem;
import ltd.vastchain.api.vctc.argsModels.VoluntaryActivityRegisterModel;
import okhttp3.MediaType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static ltd.vastchain.api.Main.callAPI;

/**
 * @program: buddha-djs
 * @description: 数据上链
 * @author: zack
 * @create: 2019-12-09 15:15
 **/
@Log4j2
public class OnChain {
    private static MediaType mediaType = MediaType.parse("application/json");

    public static void main(String[] args) throws VCTCException {
        //用户注册上链
        //registerChain();
        // 获取链上id
        // getChainIdInfo();
        //数据上链
      // dataChain();
        //公共活动注册
        //activityRegister();
        //活动签到 ||
        //eventSign();
        //活动签退
        eventSignOut();
        //  用户批量上链
//        ArrayList<ChainIdMsg> chainIdMsgs = new ArrayList<>();
//        ChainIdMsg idMsg = new ChainIdMsg();
//        String activityId = "EV" + new Date().getTime() + "";
//        idMsg.setId(activityId);
//        idMsg.setMobile("123456789");
//        idMsg.setNickName("shanchun");
//        idMsg.setParentId("bucket3");
//        idMsg.setUserId(9663);
//        idMsg.setPassWard("123zxc");
//        ChainIdMsg idMsg2 = new ChainIdMsg();
//        String activity2 = "AK" + new Date().getTime() + "";
//        idMsg2.setId(activity2);
//        idMsg2.setMobile("987456123");
//        idMsg2.setNickName("shanchun");
//        idMsg2.setParentId("bucket3");
//        idMsg2.setUserId(9664);
//        idMsg2.setPassWard("123xxx");
//        chainIdMsgs.add(idMsg);
//        chainIdMsgs.add(idMsg2);
//        userChain(chainIdMsgs);
    }

    private static void activityRegister() throws VCTCException {
        JSONObject jsonObject = new JSONObject();
        String activityId = "EV" + new Date().getTime();
        jsonObject.put("id",activityId);
        log.info("活动id:{}",activityId);
        jsonObject.put("createTime",new Date().getTime());
        jsonObject.put("title","窈窕淑女");
        jsonObject.put("desc","君子好逑");
        jsonObject.put("organizationId","12142122");
        JSONObject jsonObject1 = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject1.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", "voluntary-activity-register");
        item.put("args", jsonObject);
        list.add(item);
        callAPI("POST", "/common-chain-upload/", "", jsonObject1.toJSONString());
    }

    private static void eventSignOut() throws VCTCException {
        JSONArray array = new JSONArray();
        array.add("1.4352");
        array.add("5.33521");
        JSONObject pwd = new JSONObject();
        pwd.put("signerName","李思");
        pwd.put("gps",array);
        JSONObject jsonObject = new JSONObject();
        String time = new Date().getTime() + "";
        String str = RandomUtil.getRandomStrByLength(4);
        String s = str + time;
        jsonObject.put("id",str);
        jsonObject.put("parentId","EV1576028353235");//EV1576028353235
        jsonObject.put("createTime",time);
        jsonObject.put("durationInMinutes","59");
        jsonObject.put("memo","测试");
        jsonObject.put("userId","52262");
        jsonObject.put("x", pwd);


        JSONObject jsonObjects = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObjects.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", "voluntary-activity-signOut");//退签
        item.put("args", jsonObject);
        list.add(item);
        callAPI("POST", "/common-chain-upload/", "", jsonObjects.toJSONString());
    }

    private static void eventSign() throws VCTCException {
        JSONArray array = new JSONArray();
        array.add("1.4352");
        array.add("5.33521");
        JSONObject pwd = new JSONObject();
        pwd.put("signerName","李思");
        pwd.put("gps",array);
        JSONObject jsonObject = new JSONObject();
        String time = new Date().getTime() + "";
        String str = RandomUtil.getRandomStrByLength(4);
        String s = str + time;
        jsonObject.put("id",str);
        jsonObject.put("parentId","EV1576028353235");//EV1575972176742
        jsonObject.put("createTime",time);
        jsonObject.put("memo","描述");
        jsonObject.put("userId","52262");
        jsonObject.put("x", pwd);


        JSONObject jsonObjects = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObjects.put("items", list);
        JSONObject item = new JSONObject();
       // item.put("type", "data-bucket-register");//签到
        item.put("type", "voluntary-activity-signIn");//签到
        item.put("args", jsonObject);
        list.add(item);
        callAPI("POST", "/common-chain-upload/", "", jsonObjects.toJSONString());
    }

    private static void dataChain() throws VCTCException {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("key", "zhansan");
        object.put("type", "publicText");
        object.put("value", "邀請");
        array.add(object);
        JSONObject chainIdMsg = new JSONObject();
        String activityId = "EV" + System.currentTimeMillis();
        chainIdMsg.put("id",activityId);
        System.out.println("parentId:"+activityId);
        chainIdMsg.put("parentId",activityId);
        chainIdMsg.put("data",array);



        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", "data-bucket-register");
        item.put("args", chainIdMsg);
        list.add(item);
        callAPI("POST", "/common-chain-upload/", "", jsonObject.toJSONString());
    }

    public static void userChain(ArrayList<ChainIdMsg> chainId) throws VCTCException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items", list);
        for (ChainIdMsg chainIdMsg : chainId) {
            JSONObject item = new JSONObject();
            item.put("type", DataUploadItem.Type_Voluntary_Activity_Register);
            item.put("args", chainIdMsg);
            list.add(item);
        }
//        JSONObject item = new JSONObject();
//        item.put("type",DataUploadItem.Type_Voluntary_Activity_Register);
//        item.put("args",idMsg);
//        JSONObject item1 = new JSONObject();
//        item1.put("type",DataUploadItem.Type_Voluntary_Activity_Register);
//        item1.put("args",idMsg2);
//        list.add(item);
//        list.add(item1);
        callAPI("POST", "/common-chain-upload/", "", jsonObject.toJSONString());
    }

    /**
     * @Description: 获取链上id信息
     * @Param:
     * @return:
     * @Author: zack
     * @Date: 2019/12/9
     */
    private static void getChainIdInfo() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        jsonObject.put("items", list);
        JSONObject items = new JSONObject();
        //items.put("type",DataUploadItem.Type_Register_Test);
       // items.put("type", DataUploadItem.Type_Voluntary_Activity_Register);
        items.put("type", "data-bucket-register");
        //items.put("id","SDXToken");
        items.put("id", "EV1575956487117");
        items.put("queryType", "i2c");
        list.add(items);
        try {
            Main.callAPI("POST", "/common-chain-upload/fetchOnChainIds", "", jsonObject.toJSONString());
        } catch (VCTCException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 用户注册上链
     * @Author: zack
     * @Date: 2019/12/9
     */
    public static void registerChain() throws VCTCException {
        String activityId1 = "EV" + new Date().getTime() + "";// + new Date().getTime();
        log.info("activityId为:>>>>>>>>>>>>>>>{}", activityId1);
        VoluntaryActivityRegisterModel modelRegister = new VoluntaryActivityRegisterModel() {{
            id = activityId1;
            createTime = Long.toString(new Date().getTime());
            title = "一起打扫大街吧";
            desc = "今天我们隆重推出新项目：打扫大街";
            organization = "张三省青少年基金会1";
            organizationId = "zssqsnjjh";
            openTime = "1556592183531";
            closeTime = "1556592193531";
            district = "张三省李四市王五区";
            address = "红桃街777号";
            memo = "现场有惊喜礼品";
            categories = new String[]{"扫街"};
            x = new HashMap<String, Object>() {{ // x 中存储机密信息
                put("x_secret", "一般人我不告诉他"); // 使用 x_ 开头代表自定义的 key
            }};
        }};
        String activityId2 = "AK" + new Date().getTime() + "";// + new Date().getTime();
        System.out.println(activityId2);
        VoluntaryActivityRegisterModel modelRegister2 = new VoluntaryActivityRegisterModel() {{
            id = activityId2;
            createTime = Long.toString(new Date().getTime());
            title = "今天不怎么冷了";
            desc = "今天不怎么冷了,出去冬泳把";
            organization = "李思省青少年基金会1";
            organizationId = "zssqsnjjh";
            openTime = "15565921835132";
            closeTime = "1556592193531";
            district = "李思省李四市王五区";
            address = "红桃街777号";
            memo = "现场有惊喜礼品";
            categories = new String[]{"扫街"};
            x = new HashMap<String, Object>() {{ // x 中存储机密信息
                put("x_secret", "一般人我不告诉他"); // 使用 x_ 开头代表自定义的 key
            }};
        }};

        callAPI("POST", "/common-chain-upload/", "", JSON.toJSONString(new DataUploadBody(
                new DataUploadItem[]{
                        new DataUploadItem(DataUploadItem.Type_Voluntary_Activity_Register, modelRegister),
                        new DataUploadItem(DataUploadItem.Type_Voluntary_Activity_Register, modelRegister2)
                })));
    }
}
