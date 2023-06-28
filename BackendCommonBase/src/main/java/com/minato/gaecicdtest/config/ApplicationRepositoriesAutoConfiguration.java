package com.minato.gaecicdtest.config;

import com.vs.rappit.gcp.firestore.BaseGCPFSDal;
import com.vs.rappit.elasticsearch.BaseElasticSearchDal;
import com.vs.rappit.base.logger.Logger;
import com.vs.rappit.base.logger.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.vs.rappit.gcs.CloudStorage;
import com.vs.rappit.base.factory.StorageFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration; 

import com.vs.rappit.base.dal.providers.PersistenceType;

@AutoConfiguration
public class ApplicationRepositoriesAutoConfiguration {

private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRepositoriesAutoConfiguration .class);

	
	@Autowired
	private BaseGCPFSDal<?> baseGcpFsDal;	
	@Autowired
	private BaseElasticSearchDal<?> baseElasticSearchDal;
	
	@Bean("DB")
	public BaseGCPFSDal<?> dbRepository() {
		LOGGER.info("Entering registeringProvider ...");
		return baseGcpFsDal;
	}
	@Bean("SEARCH")
	public BaseElasticSearchDal<?> searchRepository() {
		LOGGER.info("Entering registeringProvider ...");
		return baseElasticSearchDal;
	}
}
