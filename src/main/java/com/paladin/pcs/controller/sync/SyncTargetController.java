package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncTargetExportCondition;
import com.paladin.pcs.model.sync.SyncTarget;
import com.paladin.pcs.service.sync.SyncDataService;
import com.paladin.pcs.service.sync.SyncTargetService;
import com.paladin.pcs.service.sync.dto.SyncTargetQuery;
import com.paladin.pcs.service.sync.dto.SyncTargetDTO;
import com.paladin.pcs.service.sync.vo.SyncTargetVO;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.framework.core.ControllerSupport;
import com.paladin.framework.core.query.QueryInputMethod;
import com.paladin.framework.core.query.QueryOutputMethod;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.web.response.CommonResponse;

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

import javax.validation.Valid;

@Controller
@RequestMapping("/pcs/sync/target")
public class SyncTargetController extends ControllerSupport {

    @Autowired
    private SyncTargetService syncTargetService;
    
    @Autowired
    private SyncDataService syncDataService;
    
    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncTargetQuery.class)
    public String index() {
        return "/pcs/sync/sync_target_index";
    }
    
    @RequestMapping(value = "/find/all", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object findAll() {
        return CommonResponse.getSuccessResponse(syncTargetService.findAll());
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncTargetQuery.class, paramIndex = 0)
    public Object findPage(SyncTargetQuery query) {
        return CommonResponse.getSuccessResponse(syncTargetService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncTargetService.get(id), new SyncTargetVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/pcs/sync/sync_target_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_target_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid SyncTargetDTO syncTargetDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String name = syncTargetDTO.getName();
        SyncTarget model = beanCopy(syncTargetDTO, new SyncTarget());
		if (syncDataService.saveSyncTarget(model)) {
			return CommonResponse.getSuccessResponse(beanCopy(syncTargetService.get(name), new SyncTargetVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid SyncTargetDTO syncTargetDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String name = syncTargetDTO.getName();
		SyncTarget model = beanCopy(syncTargetDTO, syncTargetService.get(name));
		if (syncDataService.updateSyncTarget(model)) {
			return CommonResponse.getSuccessResponse(beanCopy(syncTargetService.get(name), new SyncTargetVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return CommonResponse.getResponse(syncDataService.removeSyncTarget(id));
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncTargetExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncTargetQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncTargetService.searchAll(query), SyncTarget.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncTargetService.searchPage(query).getData(), SyncTarget.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}