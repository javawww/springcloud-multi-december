package com.yasaka.stock.enity.response;

import com.buddha.component.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: springcloud-multi-december
 * @description: 注册数据桶返回体
 * @author: zack
 * @create: 2019-12-11 11:38
 **/
@Data
public class BucketRegisterResponse implements Serializable {

    /**
     * 数据桶id
     */
    private String bucketId;
    /**
     * 错误信息
     */
    private String msg;
}
