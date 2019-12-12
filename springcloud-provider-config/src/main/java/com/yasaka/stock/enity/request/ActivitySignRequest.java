package com.yasaka.stock.enity.request;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @program: springCloud-multi-december
 * @description: 活动签到请求体
 * @author: zack
 * @create: 2019-12-11 13:44
 **/
@Data
public class ActivitySignRequest implements Serializable {
    /**
     * 可选，此次签到/签退的 id，长度不超过 32 位，只能由大小写字母或数字组成，在同一个 appId 中不能重复
     */
    private String  id;
    /**
     * 必选，活动的id
     */
    @NotNull(message ="活动id不能为空" )
    private String  parentId;
    /**
     * 必选，签到用户的 userId，同一个用户 id 必须相同
     */
    @NotNull(message ="userId不能为空" )
    private String  userId;
     /**
     * x 属性下面的内容为银行级别加密
     */
    private Map  x;
    /**
     * 必选，签到/签退的时间（UNIX 毫秒时间戳）
     */
    @NotNull(message ="签到/签退的时间不能为空" )
    private String  createTime;
    /**
     * 签退时必选，本次活动在线时长（以毫秒为单位，必须等于签退和签到的 `createTime` 时间的差值）
     */
    private String  durationInMinutes;
    /**
     * 可选，备注
     */
    private String  memo;
}
