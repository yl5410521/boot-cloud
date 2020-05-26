package com.alien.admin.service;

import com.alien.admin.common.vo.SearchVo;
import com.alien.admin.entity.Client;
import com.alien.base.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 客户端接口
 * @author Exrick
 */
public interface ClientService extends BaseService<Client,String> {

    /**
    * 多条件分页获取
    * @param client
    * @param searchVo
    * @param pageable
    * @return
    */
    Page<Client> findByCondition(Client client, SearchVo searchVo, Pageable pageable);

}