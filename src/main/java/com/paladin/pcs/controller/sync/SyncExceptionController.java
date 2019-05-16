package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncExceptionExportCondition;
import com.paladin.pcs.model.sync.SyncException;
import com.paladin.pcs.service.sync.SyncExceptionService;
import com.paladin.pcs.service.sync.dto.SyncExceptionQuery;
import com.paladin.pcs.service.sync.dto.SyncExceptionDTO;
import com.paladin.pcs.service.sync.vo.SyncExceptionVO;

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

import javax.validation.Valid;

@Controller
@RequestMapping("/pcs/sync/exception")
public class SyncExceptionController extends ControllerSupport {

    @Autowired
    private SyncExceptionService syncExceptionService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncExceptionQuery.class)
    public String index() {
        return "/pcs/sync/sync_exception_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncExceptionQuery.class, paramIndex = 0)
    public Object findPage(SyncExceptionQuery query) {
        return CommonResponse.getSuccessResponse(syncExceptionService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncExceptionService.get(id), new SyncExceptionVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/pcs/sync/sync_exception_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_exception_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid SyncExceptionDTO syncExceptionDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
        SyncException model = beanCopy(syncExceptionDTO, new SyncException());
		String id = UUIDUtil.createUUID();
		model.setId(id);
		if (syncExceptionService.save(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncExceptionService.get(id), new SyncExceptionVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid SyncExceptionDTO syncExceptionDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = syncExceptionDTO.getId();
		SyncException model = beanCopy(syncExceptionDTO, syncExceptionService.get(id));
		if (syncExceptionService.update(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncExceptionService.get(id), new SyncExceptionVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return CommonResponse.getResponse(syncExceptionService.removeByPrimaryKey(id));
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncExceptionExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncExceptionQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncExceptionService.searchAll(query), SyncException.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncExceptionService.searchPage(query).getData(), SyncException.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}