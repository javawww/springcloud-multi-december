package com.yasaka.stock.enity.response;

import com.buddha.component.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: springCloud-multi-december
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
     * 错误信息 status":"pending","version":"1"
     * pending 代表已进入上链队列，处于「待定」状态
     */
    private String msg;
}
