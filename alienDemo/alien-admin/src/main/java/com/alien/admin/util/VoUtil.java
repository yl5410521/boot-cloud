package com.alien.admin.util;

import cn.hutool.core.bean.BeanUtil;
import com.alien.admin.common.vo.MenuVo;
import com.alien.admin.entity.Permission;

/**
 * @author yaolin
 */
public class VoUtil {

    public static MenuVo permissionToMenuVo(Permission p){

        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
