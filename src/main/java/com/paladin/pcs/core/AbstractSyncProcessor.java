package com.paladin.pcs.core;

/**
 * 如果已经在同步中，则不在进行其他同步操作
 * 
 * @author TontoZhou
 * @since 2019年5月15日
 */
public abstract class AbstractSyncProcessor implements SyncProcessor {

	private volatile boolean run = false;
	private Object lock = new Object();

	@Override
	public void sync() {
		if (run) {
			return;
		}

		synchronized (lock) {
			if (run) {
				return;
			}
			run = true;
			try {
				doSync();
			} finally {
				run = false;
			}
		}
	}

	public abstract void doSync();

}
