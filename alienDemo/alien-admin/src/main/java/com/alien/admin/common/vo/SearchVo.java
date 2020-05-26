package com.alien.admin.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yaolin
 */
@Data
public class SearchVo implements Serializable {

    private String startDate;

    private String endDate;
}