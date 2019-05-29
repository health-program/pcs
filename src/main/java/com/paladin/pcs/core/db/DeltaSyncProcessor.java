package com.paladin.pcs.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public abstract class DeltaSyncProcessor<T> extends DBSyncProcessor {

	private String sql;
	private Date lastSyncTime;

	private SyncExceptionHandler syncExceptionHandler;
	private SyncRecordHandler syncRecordHandler;
	private SyncDataHandler<T> syncDataHandler;
	
	public DeltaSyncProcessor(String name, String model, String sql, Date lastSyncTime) {
		super(name, model);
		this.sql = sql;
		this.lastSyncTime = lastSyncTime == null ? new Date(0) : lastSyncTime;
	}

	@Override
	public void doSync() {
		long start = System.currentTimeMillis();
		try {
			List<T> ts = queryData();
			if (ts != null && ts.size() > 0) {
				for (T t : ts) {
					try {
						syncDataHandler.saveData(t);
					} catch (Exception e) {
						exceptionHandle(getName(), getModel(), e.getMessage(), t, lastSyncTime);
					}
				}
			}
			syncRecordHandler.recordHandle(this, lastSyncTime, System.currentTimeMillis() - start);
			lastSyncTime = getMaxUpdateTime(ts);
			syncDataHandler.updateSyncTime(this, lastSyncTime);
		} catch (Exception e) {
			exceptionHandle(getName(), getModel(), e.getMessage(), null, lastSyncTime);
		}

	}

	public List<T> queryData() throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			if (connection == null) {
				throw new SQLException("无法获取到Connection，是否数据库已经启动或者配置错误！");
			}

			statement = connection.prepareStatement(sql);
			statement.setDate(1, new java.sql.Date(lastSyncTime.getTime()));
			resultSet = statement.executeQuery();

			return parseResultSet(resultSet);
		} finally {
			if (resultSet != null)
				resultSet.close();

			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
		}
	}

	public abstract Date getMaxUpdateTime(List<T> ts);

	public abstract List<T> parseResultSet(ResultSet resultSet) throws SQLException;

	public void exceptionHandle(String name, String model, String exception, Object content, Date syncTime) {
		syncExceptionHandler.exceptionHandle(name, model, exception, content, syncTime);
	}

	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
	
	public SyncExceptionHandler getSyncExceptionHandler() {
		return syncExceptionHandler;
	}

	public void setSyncExceptionHandler(SyncExceptionHandler syncExceptionHandler) {
		this.syncExceptionHandler = syncExceptionHandler;
	}

	public SyncDataHandler<T> getSyncDataHandler() {
		return syncDataHandler;
	}

	public void setSyncDataHandler(SyncDataHandler<T> syncDataHandler) {
		this.syncDataHandler = syncDataHandler;
	}

	public SyncRecordHandler getSyncRecordHandler() {
		return syncRecordHandler;
	}

	public void setSyncRecordHandler(SyncRecordHandler syncRecordHandler) {
		this.syncRecordHandler = syncRecordHandler;
	}


}
