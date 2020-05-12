package com.coin.msdict.web.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* @ClassName SysDictDto
* @Description: 字典表
* @Author kh
* @Date 2020-04-29
* @Version V1.0
*/
@ApiModel(value = "字典表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value="主键", required=true)
    private String id;

    /**
     * 编码
     */
    @ApiModelProperty(value="编码", required=true)
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称", required=true)
    private String name;

    /**
     * 全拼
     */
    @ApiModelProperty(value="全拼", required=true)
    private String fullPinyin;

    /**
     * 简拼
     */
    @ApiModelProperty(value="简拼", required=true)
    private String simplePinyin;

    /**
     * 上级编码
     */
    @ApiModelProperty(value="上级编码", required=true)
    private String upCode;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述", required=true)
    private String descp;

    /**
     * 类别编码
     */
    @ApiModelProperty(value="类别编码", required=true)
    private String typeCode;

    /**
     * 类别名称
     */
    @ApiModelProperty(value="类别名称", required=true)
    private String typeName;

    /**
     * 顺序
     */
    @ApiModelProperty(value="顺序", required=true)
    private Integer seq;

    /**
     * 查询语句
     */
    @ApiModelProperty(value="查询语句", required=true)
    private String sqlCode;

    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用", required=true)
    private Integer isUse;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", required=true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", required=true)
    private LocalDateTime updateTime;


}
