package com.paladin.pcs.core.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.paladin.framework.core.exception.BusinessException;

import com.paladin.pcs.core.db.DeltaSyncProcessor;
import com.paladin.pcs.model.sync.SyncModelPersonnel;

public class PersonnelSyncProcessor extends DeltaSyncProcessor<PersonnelAccount> {

	private String identificationIdField = "identificationId";
	private String accountField = "account";
	private String passwordField = "password";
	private String updateTimeField = "updateTime";
	private String statusField = "status";

	public PersonnelSyncProcessor(String name, String model, SyncModelPersonnel syncModelPersonnel) {
		super(name, model, syncModelPersonnel.getSearchSql(), syncModelPersonnel.getLastSyncTime());
		
		
		
		this.identificationIdField = syncModelPersonnel.getIdentificationIdField();
		this.accountField = syncModelPersonnel.getAccountField();
		this.passwordField = syncModelPersonnel.getPasswordField();
		this.updateTimeField = syncModelPersonnel.getUpdateTimeField();
		this.statusField = syncModelPersonnel.getStatusField();
	}

	@Override
	public List<PersonnelAccount> parseResultSet(ResultSet resultSet) throws SQLException {
		List<PersonnelAccount> result = new ArrayList<>();

		while (resultSet.next()) {
			PersonnelAccount pa = null;
			String identificationId = null;

			try {
				identificationId = resultSet.getString(identificationIdField);
				String account = resultSet.getString(accountField);
				String password = resultSet.getString(passwordField);
				Date updateTime = resultSet.getDate(updateTimeField);
				String status = resultSet.getString(statusField);

				checkNull(identificationId, "身份证件号码不能为空");
				checkNull(account, "账号不能为空");
				checkNull(password, "密码不能为空");
				checkNull(updateTime, "更新时间不能为空");
				checkNull(status, "账号状态不能为空");

				pa = new PersonnelAccount();
				pa.setIdentificationId(identificationId);
				pa.setAccount(account);
				pa.setPassword(password);
				pa.setUpdateTime(updateTime);
				pa.setSyncTime(getLastSyncTime());
				pa.setStatus(status);
				pa.setSyncTarget(getName());

				result.add(pa);
			} catch (Exception e) {
				exceptionHandle(getName(), getModel(), e.getMessage(), pa != null ? pa : identificationId, getLastSyncTime());
			}
		}

		return result;
	}

	private void checkNull(Object value, String message) {
		if (value == null) {
			throw new BusinessException(message);
		}
	}
	
	@Override
	public Date getMaxUpdateTime(List<PersonnelAccount> ts) {
		long max = getLastSyncTime().getTime();

		if (ts != null && ts.size() > 0) {
			for (PersonnelAccount t : ts) {
				Date time = t.getUpdateTime();
				if (time != null) {
					max = Math.max(max, time.getTime());
				}
			}
		}

		return new Date(max);
	}

	@Override
	public String getModel() {
		return "personnel";
	}

	public void updateConfig(SyncModelPersonnel syncModelPersonnel) {
		this.setSql(syncModelPersonnel.getSearchSql());
		this.setLastSyncTime(syncModelPersonnel.getLastSyncTime());
		
		this.identificationIdField = syncModelPersonnel.getIdentificationIdField();
		this.accountField = syncModelPersonnel.getAccountField();
		this.passwordField = syncModelPersonnel.getPasswordField();
		this.updateTimeField = syncModelPersonnel.getUpdateTimeField();
		this.statusField = syncModelPersonnel.getStatusField();
	}

}
