package com.paladin.pcs.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paladin.framework.web.response.CommonResponse;
import com.paladin.pcs.core.db.DBSyncContainer;

@Controller
@RequestMapping("/pcs/sync")
public class SyncController {
	@Autowired
	private DBSyncContainer dBSyncContainer;

	@RequestMapping(value = "/model/get", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object findAll() {
		return CommonResponse.getSuccessResponse(dBSyncContainer.getModels());
	}

}
