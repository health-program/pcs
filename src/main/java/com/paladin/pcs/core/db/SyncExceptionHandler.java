package com.paladin.pcs.core.db;

import java.util.Date;

public interface SyncExceptionHandler {
	
	public void exceptionHandle(String name, String model, String exception, Object content, Date syncTime);
	
}
