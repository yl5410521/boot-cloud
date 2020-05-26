package com.alien.admin.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yaolin
 */
@Data
@AllArgsConstructor
public class RedisVo {

    private String key;

    private String value;
}
