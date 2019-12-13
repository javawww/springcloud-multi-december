package com.yasaka.stock.feign.chain;


import com.buddha.component.common.utils.ResultJson;
import com.yasaka.stock.enity.request.OnChainDataRequest;
import com.yasaka.stock.enity.response.BucketRegisterResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: springCloud-multi-december
 * @description: 数据上链对外开放接口
 * @author: zack
 * @create: 2019-12-12 13:40
 **/
@FeignClient("springcloud-provider-config")
interface DataOnChainFeign {

    /**
     * 基本数据上链
     * @param req
     * @return
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/dataOnChain/batchOnChainData",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultJson<BucketRegisterResponse> dataOnChain(@RequestBody OnChainDataRequest req);
}
