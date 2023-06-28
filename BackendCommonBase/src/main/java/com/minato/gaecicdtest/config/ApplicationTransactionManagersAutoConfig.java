package com.minato.gaecicdtest.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import  com.vs.rappit.base.transaction.ITransactionManager;
import com.vs.rappit.gaelibrary.queue.connection.QueueTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.LinkedHashMap;
import java.util.Map;
import com.vs.rappit.base.dal.providers.PersistenceType;
import com.vs.rappit.gcp.firestore.connection.FSTransactionManager;
import com.vs.rappit.elasticsearch.connection.SearchTransactionManager;

@AutoConfiguration
public class ApplicationTransactionManagersAutoConfig {
	
	
	@Autowired
	private FSTransactionManager fsTransactionManager;
	@Autowired
	private SearchTransactionManager searchTransactionManager;
	@Autowired
	private QueueTransactionManager queueTransactionManager;
	
	@Bean("transactionsManager")
	public Map<PersistenceType,ITransactionManager<?>> initTransactionManagers() {
		Map<PersistenceType,ITransactionManager<?>> transactionTypes = new LinkedHashMap<>();
		transactionTypes.put(PersistenceType.DB, fsTransactionManager);
		transactionTypes.put(PersistenceType.SEARCH, searchTransactionManager);
		transactionTypes.put(PersistenceType.QUEUE, queueTransactionManager);
		return transactionTypes;
	}
}