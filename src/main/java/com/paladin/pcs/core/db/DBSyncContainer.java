package com.paladin.pcs.core.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.paladin.data.database.CommonDataBase;
import com.paladin.data.database.DataBaseConfig;
import com.paladin.data.database.DataBaseSource;
import com.paladin.data.database.DataBaseType;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import com.paladin.pcs.model.sync.SyncTarget;
import com.paladin.pcs.service.sync.SyncTargetService;

@Component
public class DBSyncContainer implements SpringContainer {

	@Autowired
	private SyncTargetService syncTargetService;

	private Map<String, DBSyncEnvironment> syncEnvironmentMap;
	private Map<String, DBSyncProcessorContainer> syncProcessorContainerMap;

	@Override
	public boolean initialize() {
		Map<String, DBSyncProcessorContainer> processorContainerMap = SpringBeanHelper.getBeansByType(DBSyncProcessorContainer.class);

		syncProcessorContainerMap = new ConcurrentHashMap<>();
		if (processorContainerMap != null) {
			for (DBSyncProcessorContainer container : processorContainerMap.values()) {
				syncProcessorContainerMap.put(container.getId(), container);
			}
		}

		List<SyncTarget> syncTargets = syncTargetService.findAll();
		syncEnvironmentMap = new ConcurrentHashMap<>();

		for (SyncTarget target : syncTargets) {
			addSyncEnvironment(target);
		}

		return true;
	}

	public DBSyncEnvironment addSyncEnvironment(SyncTarget target) {
		String name = target.getName();
		DBSyncEnvironment environment = syncEnvironmentMap.get(name);
		if (environment == null) {
			synchronized (syncEnvironmentMap) {
				environment = syncEnvironmentMap.get(name);
				if (environment == null) {

					environment = new DBSyncEnvironment();
					environment.setName(name);
					environment.setUsername(target.getUsername());
					environment.setPassword(target.getPassword());
					environment.setPriorityLevel(target.getPriorityLevel());
					environment.setStatus(target.getStatus());

					DataBaseConfig config = new DataBaseConfig();
					config.setName(target.getName());
					config.setPassword(target.getPassword());
					config.setType(DataBaseType.MYSQL);
					config.setUrl(target.getUrl());
					config.setUsername(target.getUsername());

					try {
						DataBaseSource source = createDataBaseSource(config);
						environment.setDataBaseSource(source);
						syncEnvironmentMap.put(name, environment);

						for(DBSyncProcessorContainer container: syncProcessorContainerMap.values()) {
							container.addProcessor(environment);
						}
												
					} catch (Exception e) {
						throw new BusinessException(e.getMessage());
					}
				}
			}
		}

		return environment;
	}

	private DataBaseSource createDataBaseSource(DataBaseConfig config) {

		return new CommonDataBase(config) {

			@Override
			protected DataSource createRealDataSource() {
				DataBaseType type = config.getType();

				if (type == null)
					throw new NullPointerException("Database Type Can't Be Null");

				DruidDataSource dataSource = new DruidDataSource();
				dataSource.setUrl(config.getUrl());
				dataSource.setPassword(config.getPassword());
				dataSource.setUsername(config.getUsername());
				dataSource.setName(config.getName());
				dataSource.setMaxWait(10000);

				return dataSource;
			}

			@Override
			protected boolean initialize() {
				try {
					((DruidDataSource) realDataSource).init();
				} catch (SQLException e) {
					throw new RuntimeException("数据源初始化异常", e);
				}
				return true;
			}

			@Override
			protected boolean destroy() {
				((DruidDataSource) realDataSource).close();
				return true;
			}
		};
	}

	
	public DBSyncEnvironment removeSyncEnvironment(String name) {
		DBSyncEnvironment environment = syncEnvironmentMap.get(name);
		if (environment != null) {
			synchronized (syncEnvironmentMap) {
				environment = syncEnvironmentMap.get(name);
				if (environment != null) {
					for(DBSyncProcessorContainer container: syncProcessorContainerMap.values()) {
						container.removeProcessor(environment);
					}
					syncEnvironmentMap.remove(name);
					return environment;
				}
			}
		}
		return null;
	}
	
	public DBSyncEnvironment getSyncEnvironment(String name) {
		return syncEnvironmentMap.get(name);
	}
	
	public DBSyncProcessorContainer getSyncProcessorContainer(String id) {
		return syncProcessorContainerMap.get(id);
	}

	public static class DBSyncEnvironment {

		private String name;
		private String url;
		private String username;
		private String password;
		private Integer priorityLevel;
		private Integer status;

		private DataBaseSource dataBaseSource;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Integer getPriorityLevel() {
			return priorityLevel;
		}

		public void setPriorityLevel(Integer priorityLevel) {
			this.priorityLevel = priorityLevel;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public DataBaseSource getDataBaseSource() {
			return dataBaseSource;
		}

		public void setDataBaseSource(DataBaseSource dataBaseSource) {
			this.dataBaseSource = dataBaseSource;
		}
	}

}
