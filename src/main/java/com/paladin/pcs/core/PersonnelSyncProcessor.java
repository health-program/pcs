package com.paladin.pcs.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.paladin.pcs.service.sync.SyncPersonnelService;

public class PersonnelSyncProcessor extends AbstractSyncProcessor {

	private String name;
	private PersonnelSyncConfig config;

	private String sql;
	private DataSource dataSource;
	private Date lastUpdateTime;
	private long maxUpdateTime;

	private SyncPersonnelService syncPersonnelService;

	public PersonnelSyncProcessor(String name, PersonnelSyncConfig config, Date lastUpdateTime) {
		this.name = name;
		this.config = config;
		this.dataSource = config.getDataSource();
		this.lastUpdateTime = lastUpdateTime;
		this.sql = config.getSql();
	}

	private List<PersonnelAccount> getPersonnelAccount() throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			if (connection == null) {
				throw new SQLException("无法获取到Connection，是否数据库已经启动或者配置错误！");
			}

			statement = connection.prepareStatement(sql);
			statement.setDate(1, lastUpdateTime);
			resultSet = statement.executeQuery(sql);

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

	private List<PersonnelAccount> parseResultSet(ResultSet resultSet) throws SQLException {
		List<PersonnelAccount> result = new ArrayList<>();		
		maxUpdateTime = lastUpdateTime.getTime();

		while (resultSet.next()) {
			PersonnelAccount pa = new PersonnelAccount();

			String identificationId = resultSet.getString(config.getIdentificationIdField());
			String account = resultSet.getString(config.getAccountField());
			String password = resultSet.getString(config.getPasswordField());
			Date updateTime = resultSet.getDate(config.getUpdateTimeField());
			String status = resultSet.getString(config.getStatusField());
			
			pa.setIdentificationId(identificationId);
			pa.setAccount(account);
			pa.setPassword(password);
			pa.setUpdateTime(updateTime);
			pa.setSyncTime(lastUpdateTime);
			pa.setStatus(AccountStatus.valueOf(status));
			
			result.add(pa);

			if (updateTime != null) {
				maxUpdateTime = Math.max(maxUpdateTime, updateTime.getTime());
			}
		}

		return result;
	}

	@Override
	public void doSync() {

		try {
			List<PersonnelAccount> pas = getPersonnelAccount();
			if (pas != null && pas.size() > 0) {				
				syncPersonnelService.syncPersonnelAccount(pas, name, maxUpdateTime);
				lastUpdateTime = new Date(maxUpdateTime);
			}

			
		} catch (Exception e) {
			// 
		}

	}

	public static class PersonnelSyncConfig {

		private String sql;
		private DataSource dataSource;

		private String identificationIdField;
		private String accountField;
		private String passwordField;
		private String updateTimeField;
		private String statusField;

		public String getIdentificationIdField() {
			return identificationIdField;
		}

		public void setIdentificationIdField(String identificationIdField) {
			this.identificationIdField = identificationIdField;
		}

		public String getAccountField() {
			return accountField;
		}

		public void setAccountField(String accountField) {
			this.accountField = accountField;
		}

		public String getPasswordField() {
			return passwordField;
		}

		public void setPasswordField(String passwordField) {
			this.passwordField = passwordField;
		}

		public String getUpdateTimeField() {
			return updateTimeField;
		}

		public void setUpdateTimeField(String updateTimeField) {
			this.updateTimeField = updateTimeField;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public DataSource getDataSource() {
			return dataSource;
		}

		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public String getStatusField() {
			return statusField;
		}

		public void setStatusField(String statusField) {
			this.statusField = statusField;
		}

	}

	public static enum AccountStatus {
		ENABLED,DELETE,STOP;
	}
	
	public static class PersonnelAccount {
		private String identificationId;
		private String account;
		private String password;
		private Date updateTime;
		private Date syncTime;
		private AccountStatus status;

		public String getIdentificationId() {
			return identificationId;
		}

		public void setIdentificationId(String identificationId) {
			this.identificationId = identificationId;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Date getSyncTime() {
			return syncTime;
		}

		public void setSyncTime(Date syncTime) {
			this.syncTime = syncTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public AccountStatus getStatus() {
			return status;
		}

		public void setStatus(AccountStatus status) {
			this.status = status;
		}
	}

}
