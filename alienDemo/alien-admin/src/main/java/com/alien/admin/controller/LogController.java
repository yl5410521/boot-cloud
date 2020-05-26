package com.alien.admin.controller;
import com.alien.admin.common.vo.SearchVo;
import com.alien.admin.entity.Log;
import com.alien.admin.entity.elasticsearch.EsLog;
import com.alien.admin.service.LogService;
import com.alien.admin.service.elasticsearch.EsLogService;
import com.alien.base.common.vo.PageVo;
import com.alien.base.common.vo.Result;
import com.alien.base.controller.BaseController;
import com.alien.base.service.BaseService;
import com.alien.base.utils.PageUtil;
import com.alien.base.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * @author yaolin
 */
@Slf4j
@RestController
@Api(value = "log",tags = "日志管理API")
@RequestMapping("/alien/log")
@Transactional
public class LogController  {

    @Value("${alien.logRecord.es}")
    private Boolean esRecord;

    @Autowired
    private EsLogService esLogService;

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/getAllByPage",method = RequestMethod.GET)
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                       @RequestParam String key,
                                       @ModelAttribute SearchVo searchVo,
                                       @ModelAttribute PageVo pageVo){

        if(esRecord){
            Page<EsLog> es = esLogService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
            return ResultUtil.data(es);
        }else{
            Page<Log> log = logService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
            return ResultUtil.data(log);
        }
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id : ids){
            if(esRecord){
                esLogService.deleteLog(id);
            }else{
                logService.delete(id);
            }
        }
        return ResultUtil.success("删除成功");
    }

    @RequestMapping(value = "/delAll",method = RequestMethod.DELETE)
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll(){

        if(esRecord){
            esLogService.deleteAll();
        }else{
            logService.deleteAll();
        }
        return ResultUtil.success("删除成功");
    }

}