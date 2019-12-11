package com.yasaka.stock.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buddha.component.common.enums.DataUploadItemEnum;
import com.buddha.component.common.enums.ResultStatusEnum;
import com.buddha.component.common.utils.RandomUtil;
import com.buddha.component.common.utils.ResultJson;
import com.buddha.component.common.utils.StringUtils;
import com.yasaka.stock.enity.request.*;
import com.yasaka.stock.enity.response.BucketRegisterResponse;
import com.yasaka.stock.service.DataOnChainService;
import ltd.vastchain.api.VCTCException;
import ltd.vastchain.api.vctc.APIResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

import static ltd.vastchain.api.Main.callAPI;

/**
 * @program: springCloud-multi-december
 * @description: 数据上链实现类
 * @author: zack
 * @create: 2019-12-10 14:11
 **/
@Service
public class DataOnChainServiceImpl implements DataOnChainService {

    /**
     * 基本信息上链
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse batchOnChainData(OnChainDataRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        JSONObject batchOnChain = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        batchOnChain.put("items", list);
        for (OnChainDataRequest.BatchOnChainData data : req.getData()) {
            JSONObject item = new JSONObject();
            item.put("type", data.getType());
            item.put("args", data.getArgs());
            list.add(item);
        }
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", batchOnChain.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                response.setMsg("批量上传成功");
                return response;
            }else {
                //返回错误信息
                response.setMsg(res.msg);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据桶注册
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse dataBucketRegister(BucketRegisterRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        JSONObject args = new JSONObject();
        //生成随机id
        String str = RandomUtil.getRandomStrByLength(4);
        String id = str + System.currentTimeMillis();
        args.put("id",id);//必选，此 bucket 的 id，长度不超过 32 位，只能由大小写字母或数字组成
        /**注册数据桶*/
        JSONObject bucket = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        bucket.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Data_Bucket_Register);//数据桶注册
        item.put("args", args);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", bucket.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                response.setBucketId(id);//返回对应桶id
             return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上链基本数据信息
     * @param req
     * @return
     */
    @Override
    public Boolean dataItemCreate(RegisterOnChainRequest req) {

//        JSONArray array = new JSONArray();
//        JSONObject object = new JSONObject();
//        object.put("key", "zhansan");
//        object.put("type", "publicText");
//        object.put("value", "邀請");
//        array.add(object);
//        JSONObject chainIdMsg = new JSONObject();
//        String activityId = "EV" + new Date().getTime();
//        chainIdMsg.put("id",activityId);
//        System.out.println("parentId:"+activityId);
//        chainIdMsg.put("parentId",activityId);
//        chainIdMsg.put("data",array);


        JSONObject args = new JSONObject();
        String str = RandomUtil.getRandomStrByLength(4); //生成随机id
        String id = str + System.currentTimeMillis();
        args.put("id",id);//必选，此 bucket 的 id，长度不超过 32 位，只能由大小写字母或数字组成
        args.put("parentId",req.getBucketId());
        args.put("data",req.getBaseData());
        /**向指定桶中添加数据*/
        JSONObject bucket = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        bucket.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Data_Item_Create);//向数据桶中添加数据
        item.put("args", args);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", bucket.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                return true;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 公共活动注册
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse activityRegister(ActivityRegisterRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        //设置活动id
        String signId = RandomUtil.getRandomStrByLength(4)+System.currentTimeMillis();
        if (StringUtils.isNull(req.getData().getActivityId())){
            req.getData().setActivityId(signId);
        }
        JSONObject activity = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        activity.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", "voluntary-activity-register");
        item.put("args", req.getData());
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", activity.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                //返回活动id
                response.setBucketId(signId);
                return response;
            }else {
                //返回错误信息
                response.setMsg(res.msg);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公共活动签到
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse activitySignIn(ActivitySignRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        //签到id
        String signId = RandomUtil.getRandomStrByLength(4)+System.currentTimeMillis();
        if (StringUtils.isNull(req.getSignData().getId())){
            req.getSignData().setId(signId);
        }
        //组装签到数据
        JSONObject signIn = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        signIn.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Voluntary_Activity_SignIn);
        item.put("args", req.getSignData());
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", signIn.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                //返回活动id
                response.setBucketId(signId);
                return response;
            }else {
                //返回错误信息
                response.setMsg(res.msg);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公共活动签退
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse activitySignOut(ActivitySignRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        //签到id
        String signOutId = RandomUtil.getRandomStrByLength(4)+System.currentTimeMillis();
        if (StringUtils.isNull(req.getSignData().getId())){
            req.getSignData().setId(signOutId);
        }
        JSONObject signOut = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        signOut.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", "voluntary-activity-signOut");//退签
        item.put("args", req.getSignData());
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", signOut.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                response.setMsg("退签成功");
                return response;
            }else {
                //返回错误信息
                response.setMsg(res.msg);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;

    }


}
