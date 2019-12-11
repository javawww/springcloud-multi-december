package com.yasaka.stock.enity.request;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springcloud-multi-december
 * @description: 活动注册信息请求体
 * @author: zack
 * @create: 2019-12-11 12:26
 **/
@Data
public class ActivityRegisterRequest implements Serializable {
    /**
     * 活动数据
     */
    private ActivityRegisterData data;

    @Data
    public static class ActivityRegisterData {
        /**
         * 必选，在项目方数据库中能唯一找到项目的活动 id，请确保该 id 在同一个 `appId`
         * 中不重复且可查询到项目情况，只能由大小写字母或数字组成，长度不超过 32 位
         */
        private String activityId;
        /**
         * 可选，活动的创建时间 (精确到毫秒的 UNIX 时间戳)，请以数值形式传递
         */
        private String createTime;
        /**
         * 可选，活动名称
         */
        private String title;
        /**
         * 可选，活动描述
         */
        private String desc;
        /**
         * 可选，活动发起组织的名称
         */
        private String organization;
        /**
         * 必选，活动发起组织的唯一 ID
         */
        private String organizationId;
        /**
         * 可选，活动开始时间 (毫秒 UNIX 时间戳)
         */
        private String openTime;
        /**
         * 可选，活动结束时间 (毫秒 UNIX 时间戳)
         */
        private String closeTime;
        /**
         * 可选，活动区域
         */
        private String district;
        /**
         * 可选，活动地点
         */
        private String address;
        /**
         * // 可选，活动备注
         */
        private String memo;
        /**
         * 可选，活动类别，请以数组方式提供
         */
        private String[] categories;
        /**
         * 可选，下面内容为加密内容
         * 如果想加密上述字段中的可选字段，请直接将其移动到 x 属性中（id 和 organizationId 不允许加密）
         */
        private Map x;

    }
}
