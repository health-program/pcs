package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncTaskExportCondition;
import com.paladin.pcs.model.sync.SyncTask;
import com.paladin.pcs.service.sync.SyncTaskService;
import com.paladin.pcs.service.sync.dto.SyncTaskQuery;
import com.paladin.pcs.service.sync.dto.SyncTaskDTO;
import com.paladin.pcs.service.sync.vo.SyncTaskVO;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.framework.core.ControllerSupport;
import com.paladin.framework.core.query.QueryInputMethod;
import com.paladin.framework.core.query.QueryOutputMethod;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.web.response.CommonResponse;
import com.paladin.framework.utils.uuid.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

@Controller
@RequestMapping("/pcs/sync/task")
public class SyncTaskController extends ControllerSupport {

    @Autowired
    private SyncTaskService syncTaskService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncTaskQuery.class)
    public String index() {
        return "/pcs/sync/sync_task_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncTaskQuery.class, paramIndex = 0)
    public Object findPage(SyncTaskQuery query) {
        return CommonResponse.getSuccessResponse(syncTaskService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncTaskService.get(id), new SyncTaskVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/pcs/sync/sync_task_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_task_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid SyncTaskDTO syncTaskDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		
        SyncTask model = beanCopy(syncTaskDTO, new SyncTask());
		String id = UUIDUtil.createUUID();
		model.setId(id);
		model.setCreateTime(new Date());
		if (syncTaskService.save(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncTaskService.get(id), new SyncTaskVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid SyncTaskDTO syncTaskDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = syncTaskDTO.getId();
		SyncTask model = beanCopy(syncTaskDTO, syncTaskService.get(id));
		if (syncTaskService.update(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncTaskService.get(id), new SyncTaskVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return CommonResponse.getResponse(syncTaskService.removeByPrimaryKey(id));
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncTaskExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncTaskQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncTaskService.searchAll(query), SyncTask.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncTaskService.searchPage(query).getData(), SyncTask.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}