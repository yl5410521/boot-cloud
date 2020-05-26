package com.alien.base.entity;

import com.alien.base.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894571868870493809L;
	
	public static final String CREATE_DATE_PROPERTY_NAME = "createDate";// "创建日期"属性名称
	public static final String MODIFY_DATE_PROPERTY_NAME = "modifyDate";// "修改日期"属性名称
	public static final String ON_SAVE_METHOD_NAME = "onSave";// "保存"方法名称
	public static final String ON_UPDATE_METHOD_NAME = "onUpdate";// "更新"方法名称

	@Id
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.alien.base.utils.snowid.SnowflakeId")
	@ApiModelProperty(value = "唯一标识")
	@Column(length = 80,nullable=false)
	private String id;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;// 创建时间

	@CreatedBy
	@ApiModelProperty(value = "创建者")
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	@LastModifiedBy
	@ApiModelProperty(value = "更新者")
	@TableField(fill = FieldFill.UPDATE)
	private String modifiedBy;

	@LastModifiedDate
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private Date modifyDate;// 更新时间

	@ApiModelProperty(value = "删除标志 默认0")
	private Integer delFlag = CommonConstant.STATUS_NORMAL;


}
