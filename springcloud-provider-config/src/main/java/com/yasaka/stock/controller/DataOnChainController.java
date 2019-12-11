package com.yasaka.stock.controller;

import com.buddha.component.common.enums.ResultStatusEnum;
import com.buddha.component.common.utils.ResultJson;
import com.buddha.component.common.utils.StringUtils;
import com.yasaka.stock.enity.request.*;
import com.yasaka.stock.enity.response.BucketRegisterResponse;
import com.yasaka.stock.service.DataOnChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springcloud-multi-december
 * @description: 数据上链模块控制类
 * @author: zack
 * @create: 2019-12-10 14:08
 **/
@RequestMapping("dataOnChain")
@RestController
public class DataOnChainController {

    @Autowired
    private DataOnChainService dataOnChainService;


    /**
     * @Description: 批量上链数据
     * @Param: 数据桶注册
     * @return: 注册状态
     * @Author: zhang
     * @Date: 2019/12/10
     */
    @PostMapping("/batchOnChainData")
    public ResultJson<BucketRegisterResponse> batchOnChainData(@RequestBody OnChainDataRequest req) {
        try {
            BucketRegisterResponse response = dataOnChainService.batchOnChainData(req);
            return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,response);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "批量上链数据失败");
        }
    }

    /**
     * @Description: 数据桶注册
     * @Param: 数据桶注册
     * @return: 注册状态
     * @Author: zhang
     * @Date: 2019/12/10
     */
    @PostMapping("/dataBucketRegister")
    public ResultJson<BucketRegisterResponse> registerOnChain(@RequestBody BucketRegisterRequest req) {
        try {
            BucketRegisterResponse response = dataOnChainService.dataBucketRegister(req);
            return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,response);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "注册数据桶失败");
        }
    }

    /**
     * @Description: 向数据桶中添加数据项
     * @Param: 向数据桶中添加数据项
     * @return: 注册状态
     * @Author: zhang
     * @Date: 2019/12/10
     */
    @PostMapping("/dataItemCreate")
    public ResultJson dataItemCreate(@RequestBody RegisterOnChainRequest req) {
        try {
            if (StringUtils.isNull(req.getBaseData())){
                return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"上链基本数据列表为空");
            }
            if(StringUtils.isNull(req.getBucketId())){
                return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"数据桶id为空");
            }
            if (StringUtils.isNull(req.getBaseData())){

            }
            Boolean  b = dataOnChainService.dataItemCreate(req);
            if (b){
                return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,"上链基本数据成功");
            }else {
                return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "上链基本数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "上链基本数据失败");
        }
    }
    /** 
    * @Description: 公共活动注册 
    * @Param:
    * @return: 注册桶Id
    * @Author: zack 
    * @Date: 2019/12/11 
    */
    @PostMapping("/activityRegister")
    public ResultJson<BucketRegisterResponse> activityRegister(@RequestBody ActivityRegisterRequest req) {
        try {
            BucketRegisterResponse response = dataOnChainService.activityRegister(req);
            return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,response);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "注册数据桶失败");
        }
    }
    /**
    * @Description: 活动签到
    * @return:  签到成功
    * @Author: zack
    * @Date: 2019/12/11
    */
    @PostMapping("/activitySignIn")
    public ResultJson<BucketRegisterResponse> activitySignIn(@RequestBody ActivitySignRequest req) {
        try {
            if (StringUtils.isNull(req.getSignData().getParentId())){
            return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"活动id为空");
            }
            if (StringUtils.isNull(req.getSignData().getUserId())){
                return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"用户id为空");
            }
            if (StringUtils.isNull(req.getSignData().getCreateTime())){
                return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"签到/签退的时间为空");
            }
            BucketRegisterResponse response = dataOnChainService.activitySignIn(req);
            return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "活动签到失败");
        }
    }
        /**
         * @Description: 活动签退
         * @Param:
         * @return:  签退成功
         * @Author: zack
         * @Date: 2019/12/11
         */
        @PostMapping("/activitySignOut")
        public ResultJson<BucketRegisterResponse> activitySignOut(@RequestBody ActivitySignRequest req) {
            try {
                if (StringUtils.isNull(req.getSignData().getParentId())){
                    return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"活动id为空");
                }
                if (StringUtils.isNull(req.getSignData().getUserId())){
                    return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"用户id为空");
                }
                if (StringUtils.isNull(req.getSignData().getCreateTime())){
                    return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"签到/签退的时间为空");
                }
                if (StringUtils.isNull(req.getSignData().getDurationInMinutes())){
                    return new ResultJson<>(ResultStatusEnum.PARAMETER_ERROR,"本次活动在线时长为空");
                }
                BucketRegisterResponse response = dataOnChainService.activitySignOut(req);
                return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,response);
            } catch (Exception e) {
                e.printStackTrace();
                return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "活动签退失败");
            }
    }
    /**
    * @Description: 批量上链各种信息
    * @Param:
    * @return:  上线成功||失败 信息
    * @Author: zack
    * @Date: 2019/12/11
    */
    @PostMapping("/batchOnChain")
    public ResultJson<BucketRegisterResponse> batchOnChain(@RequestBody ActivityRegisterRequest req) {
        try {
            BucketRegisterResponse response = dataOnChainService.batchOnChain(req);
            return new ResultJson<>(ResultStatusEnum.COMMON_SUCCESS,response);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultJson<>(ResultStatusEnum.COMMON_FAIL, "注册数据桶失败");
        }
    }
}
