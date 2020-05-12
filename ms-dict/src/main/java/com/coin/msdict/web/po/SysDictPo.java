package com.coin.msdict.web.po;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
* @ClassName SysDict
* @Description: 字典表
* @Author kh
* @Date 2020-04-29
* @Version V1.0
*/
@ApiModel(value = "字典表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictPo extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value="主键", required=true)
    @TableId("ID")
    private String id;

    /**
     * 字典编码
     */
    @ApiModelProperty(value="字典编码", required=false)
    @TableField("DICT_CODE")
    private String dictCode;

    /**
     * 编码
     */
    @ApiModelProperty(value="编码", required=true)
    @TableField("CODE")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称", required=true)
    @TableField("NAME")
    private String name;

    /**
     * 全拼
     */
    @ApiModelProperty(value="全拼", required=true)
    @TableField("FULL_PINYIN")
    private String fullPinyin;

    /**
     * 简拼
     */
    @ApiModelProperty(value="简拼", required=true)
    @TableField("SIMPLE_PINYIN")
    private String simplePinyin;

    /**
     * 上级编码
     */
    @ApiModelProperty(value="上级编码", required=true)
    @TableField("UP_CODE")
    private String upCode;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述", required=true)
    @TableField("DESCP")
    private String descp;

    /**
     * 类别编码
     */
    @ApiModelProperty(value="类别编码", required=true)
    @TableField("TYPE_CODE")
    private String typeCode;

    /**
     * 类别名称
     */
    @ApiModelProperty(value="类别名称", required=true)
    @TableField("TYPE_NAME")
    private String typeName;

    /**
     * 顺序
     */
    @ApiModelProperty(value="顺序", required=true)
    @TableField("SEQ")
    private Integer seq;

    /**
     * 查询语句
     */
    @ApiModelProperty(value="查询语句", required=true)
    @TableField("SQL_CODE")
    private String sqlCode;

    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用", required=true)
    @TableField("IS_USE")
    private Integer isUse;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", required=true)
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", required=true)
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;



}
