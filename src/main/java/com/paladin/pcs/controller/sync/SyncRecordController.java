package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncRecordExportCondition;
import com.paladin.pcs.model.sync.SyncRecord;
import com.paladin.pcs.service.sync.SyncRecordService;
import com.paladin.pcs.service.sync.dto.SyncRecordDTO;
import com.paladin.pcs.service.sync.dto.SyncRecordQuery;
import com.paladin.pcs.service.sync.vo.SyncRecordVO;
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
@RequestMapping("/pcs/sync/record")
public class SyncRecordController extends ControllerSupport {

    @Autowired
    private SyncRecordService syncRecordService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncRecordQuery.class)
    public String index() {
        return "/pcs/sync/sync_record_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncRecordQuery.class, paramIndex = 0)
    public Object findPage(SyncRecordQuery query) {
        return CommonResponse.getSuccessResponse(syncRecordService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncRecordService.get(id), new SyncRecordVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/pcs/sync/sync_record_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_record_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid SyncRecordDTO syncRecordDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
        SyncRecord model = beanCopy(syncRecordDTO, new SyncRecord());
		String id = UUIDUtil.createUUID();
		model.setId(id);
		if (syncRecordService.save(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncRecordService.get(id), new SyncRecordVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid SyncRecordDTO syncRecordDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = syncRecordDTO.getId();
		SyncRecord model = beanCopy(syncRecordDTO, syncRecordService.get(id));
		if (syncRecordService.update(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncRecordService.get(id), new SyncRecordVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return CommonResponse.getResponse(syncRecordService.removeByPrimaryKey(id));
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncRecordExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncRecordQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncRecordService.searchAll(query), SyncRecord.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncRecordService.searchPage(query).getData(), SyncRecord.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}