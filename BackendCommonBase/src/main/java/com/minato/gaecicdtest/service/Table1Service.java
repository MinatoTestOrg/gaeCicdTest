package com.minato.gaecicdtest.service;

import com.vs.rappit.base.acl.IPerimeterManager;
import com.minato.gaecicdtest.base.service.Table1BaseService;
import com.minato.gaecicdtest.model.Table1;
import com.minato.gaecicdtest.service.Table1PerimeterImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service("Table1")
public class Table1Service extends Table1BaseService<Table1> implements ITable1Service<Table1>{

		@Autowired
		private  Table1PerimeterImpl  table1PerimeterImpl;

		public Table1Service(ChangelogService changelogService) {
		super(Table1.class);	
		setChangelogService(changelogService); 
		
	}
	
	
	protected IPerimeterManager<Table1> getPerimeterManager() {
		return table1PerimeterImpl;
	}
}