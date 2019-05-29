package com.paladin.pcs.service.sync;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.paladin.pcs.core.db.SyncExceptionHandler;
import com.paladin.pcs.model.sync.SyncException;
import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.utils.JsonUtil;
import com.paladin.framework.utils.uuid.UUIDUtil;

@Service
public class SyncExceptionService extends ServiceSupport<SyncException> implements SyncExceptionHandler {

	@Override
	public void exceptionHandle(String name, String model, String ex, Object content, Date syncTime) {

		SyncException exception = new SyncException();
		exception.setId(UUIDUtil.createUUID());
		exception.setSyncTarget(name);
		exception.setSyncModel(model);
		exception.setSyncTime(syncTime);

		String contentStr = null;
		if (content != null) {
			if (content instanceof String) {
				contentStr = (String) content;
			} else {
				contentStr = JsonUtil.getJson(content);
			}
		}

		exception.setSyncContent(substring(contentStr, 500));
		exception.setException(substring(ex, 500));
		exception.setCreateTime(new Date());

		save(exception);
	}

	private String substring(String str, int max) {
		return str != null ? (str.length() <= max ? str : str.substring(0, max)) : null;
	}
}