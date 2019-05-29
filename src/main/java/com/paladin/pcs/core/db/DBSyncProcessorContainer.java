package com.paladin.pcs.core.db;

import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;

public interface DBSyncProcessorContainer<T> {

	/**
	 * 获取容器ID
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 获取容器名称（中文）
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 新增环境处理
	 * 
	 * @param environment
	 */
	public void addEnvironmentHandle(DBSyncEnvironment environment);

	/**
	 * 更新环境处理
	 * 
	 * @param environment
	 */
	public void updateEnvironmentHandle(DBSyncEnvironment environment);

	/**
	 * 移除环境处理
	 * 
	 * @param environment
	 */
	public void removeEnvironmentHandle(DBSyncEnvironment environment);

	/**
	 * 设置环境状态处理
	 * 
	 * @param environment
	 */
	public void setEnvironmentEnabledHandle(DBSyncEnvironment environment);

	/**
	 * 获取处理器
	 * 
	 * @param name
	 */
	public DBSyncProcessor getProcessor(String name);
	
	/**
	 * 新增处理器
	 * @param config
	 * @return
	 */
	public DBSyncProcessor addProcessor(DBSyncEnvironment environment);
	
	/**
	 * 更新处理器
	 * @param config
	 * @return
	 */
	public DBSyncProcessor updateProcessor(DBSyncEnvironment environment);
	
	/**
	 * 删除处理器
	 * @param name
	 * @return
	 */
	public DBSyncProcessor removeProcessor(String name);
	


}
