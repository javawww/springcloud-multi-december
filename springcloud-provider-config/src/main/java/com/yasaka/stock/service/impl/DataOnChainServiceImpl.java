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
     * 批量上链数据
     *
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
                response.setMsg(res.status);
                return response;
            } else {
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
     *
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse dataBucketRegister(BucketRegisterRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        JSONObject args = new JSONObject();
        //必选，此 bucket 的 id，长度不超过 32 位，只能由大小写字母或数字组成
        String id = getRandomNum(8);
        args.put("id",id);
        /**注册数据桶*/
        JSONObject bucket = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        bucket.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Data_Bucket_Register);
        item.put("args", args);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", bucket.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                //返回对应桶id
                response.setBucketId(id);
                response.setMsg(res.status);
                return response;
            }else {
                //注册桶信息
                response.setMsg(res.msg);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 上链基本数据信息
     *
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse dataItemCreate(RegisterOnChainRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        JSONObject args = new JSONObject();
        //必选，此 bucket 的 id，长度不超过 32 位，只能由大小写字母或数字组成
        args.put("id", getRandomNum(8));
        args.put("parentId", req.getBucketId());
        args.put("data", req.getBaseData());
        /**向指定桶中添加数据*/
        JSONObject bucket = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        bucket.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Data_Item_Create);
        item.put("args", args);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", bucket.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                response.setMsg(res.status);
                return response;
            }else {
                response.setMsg(res.status);
                return response;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return null;
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
        String signId = getRandomNum(8);
        if (StringUtils.isNull(req.getId())){
            req.setId(signId);
        }
        JSONObject activity = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        activity.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", DataUploadItemEnum.Type_Voluntary_Activity_Register);
        item.put("args", req);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", activity.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                //返回活动id
                response.setBucketId(signId);
                response.setMsg(res.status);
                return response;
            } else {
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
     *
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse activitySignIn(ActivitySignRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        if (activitySign(req,DataUploadItemEnum.Type_Voluntary_Activity_SignIn ,response)) return response;
        return null;
    }



    /**
     * 公共活动签退
     *
     * @param req
     * @return
     */
    @Override
    public BucketRegisterResponse activitySignOut(ActivitySignRequest req) {
        BucketRegisterResponse response = new BucketRegisterResponse();
        if (activitySign(req,DataUploadItemEnum.Type_Voluntary_Activity_SignOut, response)) return response;
        return null;
    }
    /**
     * 活动签到 || 签退 实体
     * @param req
     * @param type
     * @param response
     * @return
     */
    private boolean activitySign(ActivitySignRequest req,String type, BucketRegisterResponse response) {
        //签到id
        String signId = getRandomNum(8);
        if (StringUtils.isNull(req.getId())) {
            req.setId(signId);
        }
        //组装签到数据
        JSONObject signIn = new JSONObject();
        ArrayList<JSONObject> list = new ArrayList<>();
        signIn.put("items", list);
        JSONObject item = new JSONObject();
        item.put("type", type);
        item.put("args", req);
        list.add(item);
        try {
            APIResponse res = callAPI("POST", "/common-chain-upload/", "", signIn.toJSONString());
            if (res.status.equals("success") || res.status.equals("pending")) {
                //返回活动id
                response.setBucketId(signId);
                response.setMsg(res.status);
                return true;
            } else {
                //返回错误信息
                response.setMsg(res.msg);
                return true;
            }
        } catch (VCTCException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取随机id
     * @return
     */
    public String getRandomNum(int len){
        //生成随机id
      return RandomUtil.getRandomStrByLength(len)+System.currentTimeMillis();
    }
}
