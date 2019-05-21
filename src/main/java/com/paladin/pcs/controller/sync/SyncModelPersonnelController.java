package com.paladin.pcs.controller.sync;

import com.paladin.pcs.controller.sync.dto.SyncModelPersonnelExportCondition;
import com.paladin.pcs.model.sync.SyncModelPersonnel;
import com.paladin.pcs.service.sync.SyncModelPersonnelService;
import com.paladin.pcs.service.sync.dto.SyncModelPersonnelQuery;
import com.paladin.pcs.service.sync.dto.SyncModelPersonnelDTO;
import com.paladin.pcs.service.sync.vo.SyncModelPersonnelVO;

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
@RequestMapping("/pcs/sync/model/personnel")
public class SyncModelPersonnelController extends ControllerSupport {

    @Autowired
    private SyncModelPersonnelService syncModelPersonnelService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SyncModelPersonnelQuery.class)
    public String index() {
        return "/pcs/sync/sync_model_personnel_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = SyncModelPersonnelQuery.class, paramIndex = 0)
    public Object findPage(SyncModelPersonnelQuery query) {
        return CommonResponse.getSuccessResponse(syncModelPersonnelService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return CommonResponse.getSuccessResponse(beanCopy(syncModelPersonnelService.get(id), new SyncModelPersonnelVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/pcs/sync/sync_model_personnel_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/pcs/sync/sync_model_personnel_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid SyncModelPersonnelDTO syncModelPersonnelDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
        SyncModelPersonnel model = beanCopy(syncModelPersonnelDTO, new SyncModelPersonnel());
		String id = UUIDUtil.createUUID();
		model.setName(id);
		if (syncModelPersonnelService.save(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncModelPersonnelService.get(id), new SyncModelPersonnelVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid SyncModelPersonnelDTO syncModelPersonnelDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = syncModelPersonnelDTO.getName();
		SyncModelPersonnel model = beanCopy(syncModelPersonnelDTO, syncModelPersonnelService.get(id));
		if (syncModelPersonnelService.update(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(syncModelPersonnelService.get(id), new SyncModelPersonnelVO()));
		}
		return CommonResponse.getFailResponse();
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return CommonResponse.getResponse(syncModelPersonnelService.removeByPrimaryKey(id));
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SyncModelPersonnelExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SyncModelPersonnelQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, syncModelPersonnelService.searchAll(query), SyncModelPersonnel.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, syncModelPersonnelService.searchPage(query).getData(), SyncModelPersonnel.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}