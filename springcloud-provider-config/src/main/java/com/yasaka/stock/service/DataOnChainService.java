package com.yasaka.stock.service;

import com.buddha.component.common.utils.ResultJson;
import com.yasaka.stock.enity.request.*;
import com.yasaka.stock.enity.response.BucketRegisterResponse;

/**
 * @program: springCloud-multi-december
 * @description: 数据上链接口
 * @author: zack
 * @create: 2019-12-10 14:11
 **/
public interface DataOnChainService {

    /**
     * 数据桶注册
     * @param req
     * @return
     */
    BucketRegisterResponse dataBucketRegister(BucketRegisterRequest req);

    /**
     * 上链基本数据信息
     * @param req
     * @return
     */
    BucketRegisterResponse dataItemCreate(RegisterOnChainRequest req);

    /**
     * 公共活动注册
     * @param req
     * @return
     */
    BucketRegisterResponse activityRegister(ActivityRegisterRequest req);


    /**
     * 公共活动签到
     * @param req
     * @return
     */
    BucketRegisterResponse activitySignIn(ActivitySignRequest req);


    /**
     * 公共活动签退
     * @param req
     * @return
     */
    BucketRegisterResponse activitySignOut(ActivitySignRequest req);


    /**
     * 批量上链信息
     * @param req
     * @return
     */
    BucketRegisterResponse batchOnChainData(OnChainDataRequest req);
}
