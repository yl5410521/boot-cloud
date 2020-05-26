package com.alien.admin.controller.client;

import com.alien.admin.common.vo.SearchVo;
import com.alien.base.common.vo.PageVo;
import com.alien.base.common.vo.Result;
import com.alien.base.controller.BaseController;
import com.alien.base.utils.PageUtil;
import com.alien.base.utils.ResultUtil;
import com.alien.admin.entity.Client;
import com.alien.admin.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Exrick
 */
@Slf4j
@RestController
@Api(value="client",tags = "客户端管理接口")
@RequestMapping("/alien/client")
@Transactional
public class ClientController extends BaseController<Client, String> {

    @Autowired
    private ClientService clientService;

    @Override
    public ClientService getService() {
        return clientService;
    }

    @RequestMapping(value = "/getName/{clientId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取网站基本信息")
    public Result<String> getName(@PathVariable String clientId){

        Client client = clientService.get(clientId);
        return new ResultUtil<String>().setData(client.getName());
    }


    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Result<Page<Client>> getByCondition(@ModelAttribute Client client,
                                               @ModelAttribute SearchVo searchVo,
                                               @ModelAttribute PageVo pageVo){

        Page<Client> page = clientService.findByCondition(client, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<Client>>().setData(page);
    }

    @RequestMapping(value = "/getSecretKey", method = RequestMethod.GET)
    @ApiOperation(value = "生成随机secretKey")
    public Result<String> getSecretKey(){

        String secretKey = UUID.randomUUID().toString().replaceAll("-", "");
        return new ResultUtil<String>().setData(secretKey);
    }
}
