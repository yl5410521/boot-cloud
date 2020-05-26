package com.alien.base.controller;

import java.io.*;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alien.base.common.vo.PageVo;
import com.alien.base.common.vo.Result;
import com.alien.base.service.BaseService;
import com.alien.base.utils.PageUtil;
import com.alien.base.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public abstract class BaseController <T,ID extends Serializable> {
	//private static final
	String filePath = "Downloads";
	//private static final
	org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseController.class);


	/**
	 * 获取service
	 */
	@Autowired
	public abstract BaseService<T,ID> getService();

	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过id获取")
	public Result<T> get(@PathVariable ID id){

		T entity = getService().get(id);
		return new ResultUtil<T>().setData(entity);
	}

	@RequestMapping(value = "/getAll",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取全部数据")
	public Result<List<T>> getAll(){

		List<T> list = getService().getAll();
		return new ResultUtil<List<T>>().setData(list);
	}

	@RequestMapping(value = "/getByPage",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "分页获取")
	public Result<Page<T>> getByPage(@ModelAttribute PageVo page){

		Page<T> data = getService().findAll(PageUtil.initPage(page));
		return new ResultUtil<Page<T>>().setData(data);
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存数据")
	public Result<T> save(@ModelAttribute T entity){

		T e = getService().save(entity);
		return new ResultUtil<T>().setData(e);
	}

	@RequestMapping(value = "/update",method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "更新数据")
	public Result<T> update(@ModelAttribute T entity){

		T e = getService().update(entity);
		return new ResultUtil<T>().setData(e);
	}

	@RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "批量通过id删除")
	public Result<Object> delAllByIds(@PathVariable ID[] ids){

		for(ID id:ids){
			getService().delete(id);
		}
		return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
	}

	/**
	 * @name 本地上传文件
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadFile/{file}",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "上传文件")
	public String upload(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return "文件为空";
			}
			// 获取未见名
			String fileName = file.getOriginalFilename();
			log.info("上传文件名为"+fileName);
			// 设置文件存储路径
			String path = filePath + fileName;
			File dest = new File(path);
			// 检查是否存在目录
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}
			file.transferTo(dest);
			return "success";
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "failed";

	}
/**
 * @name 下载本地
 * @param fileName
 * @param response
 * @return
 */
@RequestMapping(value = "/downloadFile/{fileName}",method = RequestMethod.GET)
@ResponseBody
@ApiOperation(value = "下载文件")
	public String downloadFile(String fileName, HttpServletResponse response) {
		if (fileName != null) {
			File file = new File(filePath + fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					return "success";
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}

		return "failed";
	}
}
