package com.yasaka.stock.enity.request;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @program: springcloud-multi-december
 * @description: 用户注册信息上链请求体
 * @author: zack
 * @create: 2019-12-10 14:22
 **/
@Data
public class RegisterOnChainRequest implements Serializable {
    /**
     * 数据桶id
     */
    @NotNull(message = "数据桶id不能为空")
    private String bucketId;
    /**
     * 上链基本数据信息(一次最少1条,最大一次50条)
     */
    @Size(min = 1,max = 50)
    @NotEmpty(message = "上链基本信息为空")
    private List<OnChainData> baseData;

    @Data
    public static class OnChainData{
        /**
         * key 上链字段名
         */
        @NotNull(message = "上链字段名不能为空")
        private String key;
        /**
         * 上链字段值
         */
        @NotNull(message = "上链字段值不能为空")
        private String value;
        /**
         * // type 目前只能为 publicText（公开文本）
         */
        @NotNull(message = "公开文本值不能为空")
        private String type;
    }
}
