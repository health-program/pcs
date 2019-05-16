package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncPersonnelExportCondition;
import com.paladin.pcs.model.sync.SyncPersonnel;
import com.paladin.pcs.service.sync.SyncPersonnelService;
import com.paladin.pcs.service.sync.dto.SyncPersonnelQuery;
import com.paladin.pcs.service.sync.vo.SyncPersonnelVO;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.framework.core.ControllerSupport;
import com.paladin.framework.core.query.QueryInputMethod;
import com.paladin.framework.core.query.QueryOutputMethod;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.web.response.CommonResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/pcs/sync/personnel")
public class SyncPersonnelController extends ControllerSupport {

    @Autowired
    private SyncPersonnelService syncPersonnelService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncPersonnelQuery.class)
    public String index() {
        return "/pcs/sync/sync_personnel_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncPersonnelQuery.class, paramIndex = 0)
    public Object findPage(SyncPersonnelQuery query) {
        return CommonResponse.getSuccessResponse(syncPersonnelService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncPersonnelService.get(id), new SyncPersonnelVO()));
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_personnel_detail";
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncPersonnelExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncPersonnelQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncPersonnelService.searchAll(query), SyncPersonnel.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncPersonnelService.searchPage(query).getData(), SyncPersonnel.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}