package com.yasaka.stock.feign.chain;

import com.buddha.component.common.enums.ResultStatusEnum;
import com.buddha.component.common.utils.ResultJson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yasaka.stock.enity.request.OnChainDataRequest;
import com.yasaka.stock.enity.response.BucketRegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: springcloud-multi-december
 * @description: 上链对外开放接口实现
 * @author: zack
 * @create: 2019-12-12 13:48
 **/
@Service
public class DataOnChainFeignService  {

    @Autowired
    private DataOnChainFeign chainFeign;

    @HystrixCommand(fallbackMethod = "dataOnChainInfoFallback")
    public ResultJson<BucketRegisterResponse> dataOnChain(OnChainDataRequest req) {
        ResultJson<BucketRegisterResponse> bucketRegister = chainFeign.dataOnChain(req);
        return bucketRegister;
    }
    private BucketRegisterResponse dataOnChainInfoFallback(OnChainDataRequest req) throws Exception {
        throw  new Exception("暂时无法上链基本信息");
    }
}
