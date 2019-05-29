package com.paladin.pcs.service.sync;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.paladin.framework.common.BaseModel;
import com.paladin.pcs.core.db.DBSyncContainer;
import com.paladin.pcs.core.db.DBSyncProcessor;
import com.paladin.pcs.model.sync.SyncTask;

@Service
public class SyncScheduleTaskService {

	@Autowired
	private DBSyncContainer dBSyncContainer;
	@Autowired
	private SyncTaskService syncTaskService;

	@Scheduled(cron = "0 0 0/1 * * ?") 
	public void executeTask() {
		List<SyncTask> tasks = syncTaskService.findAll();
		for (SyncTask task : tasks) {
			if (canExecute(task)) {
				String name = task.getName();
				String model = task.getModel();
				DBSyncProcessor processor = dBSyncContainer.getSyncProcessor(name, model);
				processor.sync();
			}
		}
	}

	public boolean canExecute(SyncTask task) {
		Integer enabled = task.getEnabled();
		if (enabled != null && enabled == BaseModel.BOOLEAN_YES) {

			Integer year = task.getYear();
			Integer month = task.getMonth();
			Integer day = task.getDay();
			Integer hour = task.getHour();

			Calendar calendar = Calendar.getInstance();

			if (year != null) {
				if (calendar.get(Calendar.YEAR) != year) {
					return false;
				}
			}

			if (month != null) {
				if ((calendar.get(Calendar.MONTH) + 1) != month) {
					return false;
				}
			}

			if (day != null) {
				if (calendar.get(Calendar.DAY_OF_MONTH) != day) {
					return false;
				}
			}

			if (hour != null) {
				if (calendar.get(Calendar.HOUR_OF_DAY) != hour) {
					return false;
				}
			}

			return true;

		}

		return false;
	}
}
