package com.paladin.pcs.core.db;

import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;

public interface DBSyncProcessorContainer {

	/**
	 * 获取容器ID
	 * @return
	 */
	public String getId();

	/**
	 * 添加一个处理器
	 * 
	 * @param environment
	 */
	public DBSyncProcessor addProcessor(DBSyncEnvironment environment);

	/**
	 * 移除处理器
	 * 
	 * @param environment
	 */
	public DBSyncProcessor removeProcessor(DBSyncEnvironment environment);

	/**
	 * 获取处理器
	 * 
	 * @param environment
	 */
	public DBSyncProcessor getProcessor(DBSyncEnvironment environment);
}
