package com.paladin.pcs.core.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;

public abstract class AbstractDBSyncProcessorContainer<T> implements DBSyncProcessorContainer<T> {

	protected Map<String, DBSyncProcessor> processorMap = new ConcurrentHashMap<>();

	public abstract T getProcessorConfig(DBSyncEnvironment environment);

	public abstract DBSyncProcessor assembleProcessorConfig(DBSyncEnvironment environment, T config);

	public void addEnvironmentHandle(DBSyncEnvironment environment) {
		addProcessor(environment);
	}

	@Override
	public void updateEnvironmentHandle(DBSyncEnvironment environment) {
		DBSyncProcessor processor = processorMap.get(environment.getName());
		if (processor != null) {
			processor.setDataSource(environment.getDataBaseSource().getRealDataSource());
			processor.setEnabled(environment.isEnabled());
		}
	}

	@Override
	public void removeEnvironmentHandle(DBSyncEnvironment environment) {
		processorMap.remove(environment.getName());
	}

	@Override
	public void setEnvironmentEnabledHandle(DBSyncEnvironment environment) {
		DBSyncProcessor processor = processorMap.get(environment.getName());
		if (processor != null) {
			processor.setEnabled(environment.isEnabled());
		}
	}

	@Override
	public DBSyncProcessor getProcessor(String name) {
		return processorMap.get(name);
	}

	@Override
	public DBSyncProcessor addProcessor(DBSyncEnvironment environment) {
		T config = getProcessorConfig(environment);
		if (config != null) {
			DBSyncProcessor processor = assembleProcessorConfig(environment, config);
			if(processor != null) {
				processorMap.put(processor.getName(), processor);
				return processor;
			}
		}
		return null;
	}

	@Override
	public DBSyncProcessor updateProcessor(DBSyncEnvironment environment) {
		T config = getProcessorConfig(environment);
		if (config != null) {
			DBSyncProcessor processor = assembleProcessorConfig(environment, config);
			if(processor != null) {
				processorMap.put(processor.getName(), processor);
				return processor;
			}
		}
		return null;
	}

	@Override
	public DBSyncProcessor removeProcessor(String name) {
		return processorMap.remove(name);
	}
}
