package com.yasaka.stock.enity.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @program: springCloud-multi-december
 * @description: 批量上链请求体
 * @author: zack
 * @create: 2019-12-11 15:33
 **/
@Data
public class OnChainDataRequest implements Serializable {
    /**
     * 上链任意类型信息
     */
    private List<BatchOnChainData> data;

    @Data
    public static class BatchOnChainData<T> {
        /**
         * 上链动作（action）的具体参数，针对不同的上链动作，
         * 参数的字段定义是不同的，但是，任何项目都有一个 id
         */
        @NotEmpty(message = "上链参数集合为空")
        private T args;
        /**
         * 上链的动作（action）类别，
         * 如 voluntary-activity-register 代表公共活动注册
         */
        @NotNull(message = "上链类型为空")
        private String type;
    }
}
