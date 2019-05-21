package com.paladin.pcs.core.db;

import javax.sql.DataSource;

public abstract class DBSyncProcessor {

	protected DataSource dataSource;
	
	private String name;
	private String model;

	public DBSyncProcessor(String name, String model) {
		this.name = name;
		this.model = model;
	}

	private volatile boolean running = false;
	private volatile boolean enabled = true;

	private Object lock = new Object();

	public void sync() {
		if (running || !enabled) {
			return;
		}

		synchronized (lock) {
			if (running || !enabled) {
				return;
			}
			running = true;
			try {
				doSync();
			} finally {
				running = false;
			}
		}
	}


	public boolean isRunning() {
		return running;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public abstract void doSync();

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getName() {
		return name;
	}

	public String getModel() {
		return model;
	}

}
