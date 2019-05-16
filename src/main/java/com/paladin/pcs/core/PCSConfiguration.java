package com.paladin.pcs.core;

import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.paladin.common.core.CommonHandlerExceptionResolver;
import com.paladin.common.core.CommonUserRealm;
import com.paladin.common.core.TontoDialect;
import com.paladin.common.service.syst.SysUserService;


@Configuration
public class PCSConfiguration {

	@Bean
	public TontoDialect getTontoDialect() {
		return new TontoDialect();
	}

	@Bean("localRealm")
	public AuthorizingRealm getLocalRealm(SysUserService sysUserService) {
		return new CommonUserRealm(sysUserService);
	}

	@Bean
	public HandlerExceptionResolver getHandlerExceptionResolver() {
		return new CommonHandlerExceptionResolver();
	}

}
