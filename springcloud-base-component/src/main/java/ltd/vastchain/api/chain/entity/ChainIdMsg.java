package ltd.vastchain.api.chain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: buddha-djs
 * @description: 获取链上id信息
 * @author: zack
 * @create: 2019-12-09 16:26
 **/
@ApiModel
@Data
public class ChainIdMsg implements Serializable  {
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "密码")
    private String passWard;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "上链类型")
    private String type;
    @ApiModelProperty(value = "必选，所在的桶（bucket）的 id")
    private String parentId;
    @ApiModelProperty(value = "任何一个上链项目都有一个 id 属性，用于上链之后的状态查询和其他相关操作")
    private String id;
}
