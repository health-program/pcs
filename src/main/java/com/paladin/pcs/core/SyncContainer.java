package com.paladin.pcs.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.paladin.data.database.CommonDataBase;
import com.paladin.data.database.DataBaseConfig;
import com.paladin.data.database.DataBaseSource;
import com.paladin.data.database.DataBaseType;
import com.paladin.framework.core.exception.BusinessException;

@Component
public class SyncContainer {

	private Map<String, DataBaseSource> dataBaseSourceMap = new HashMap<>();
	private Map<String, SyncTarget> syncTargetMap = new HashMap<>();

	public List<DataBaseSource> getDataBaseSources() {
		return new ArrayList<>(dataBaseSourceMap.values());
	}

	public List<SyncTarget> getSyncTargets() {
		return new ArrayList<>(syncTargetMap.values());
	}

	public DataBaseSource getDataBaseSource(String name) {
		return dataBaseSourceMap.get(name);
	}

	
	
	
	public void addSyncTarget(SyncTarget target) {
		String name = target.getName();
		DataBaseSource source = dataBaseSourceMap.get(name);
		if (source == null) {
			synchronized (dataBaseSourceMap) {
				source = dataBaseSourceMap.get(name);
				if (source == null) {
					DataBaseConfig config = new DataBaseConfig();
					config.setName(target.getName());
					config.setPassword(target.getPassword());
					config.setType(DataBaseType.MYSQL);
					config.setUrl(target.getUrl());
					config.setUsername(target.getUsername());

					try {
						source = createDataBaseSource(config);
					} catch (Exception e) {
						throw new BusinessException(e.getMessage());
					}
					dataBaseSourceMap.put(name, source);
					syncTargetMap.put(name, target);
				}
			}
		}
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

}
