package com.minato.gaecicdtest.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vs.rappit.base.dal.providers.SearchOptions;
import com.vs.rappit.base.dal.providers.PersistenceType;
import com.vs.rappit.base.logic.BaseBusinessLogic;
import com.vs.rappit.base.exception.ValidationError;
import com.vs.rappit.base.listener.BaseApplicationConfiguration;
import com.vs.rappit.base.service.changelog.ChangelogBLBaseImpl;
import com.vs.rappit.base.tasks.Task;
import com.vs.rappit.base.tasks.TaskStatus;
import com.vs.rappit.base.tasks.logic.TaskService;
import com.vs.rappit.base.util.Constants;
import com.vs.rappit.base.dal.Filter;
import com.vs.rappit.base.dal.SimpleFilter;
import com.vs.rappit.base.dal.Sort;
import com.vs.rappit.base.dal.Sort.Direction;
import com.vs.rappit.base.util.ErrorCode;
import com.vs.rappit.base.exception.EntityReferenceExistsException;
import com.vs.rappit.dataimport.constants.ImportTaskConstants;
import org.apache.commons.lang3.StringUtils;
import com.minato.gaecicdtest.base.model.Table1Base;
import java.util.List;

import com.vs.rappit.base.dal.providers.DBOptions;

import com.vs.rappit.base.dal.providers.DeleteOptions;

public class Table1BaseService<T extends Table1Base> extends BaseBusinessLogic<T>
		implements ITable1BaseService<T> {
		
	private ChangelogBLBaseImpl changelogService;
	
	@Autowired
	private BaseApplicationConfiguration baseApplicationConfiguration;
	
	@Autowired
	private TaskService taskService;
	
	public Table1BaseService(Class<T> modelClass) {
		super(modelClass);
		addPersistenceOption(DBOptions.DELETE_OPTION, DeleteOptions.MARK_AS_DELETE);
		addPersistenceOption(SearchOptions.SEARCH_INDEX_NAME, getTableName());
	}

	@Override
	public PersistenceType[] getOtherPersistenceTypes() {
		return new PersistenceType[] {PersistenceType.SEARCH};
	}
	@Override
	public final void onBeforeSave(PersistenceType type, T modelObj) {
		switch (type) {
			case DB:
				onBeforeSaveDB(modelObj);
				break;
						case SEARCH:
				onBeforeSaveSearch(modelObj);
				break;
			default:
				break;
		}
		super.onBeforeSave(type, modelObj);
	}

	public void onBeforeSaveDB(T modelObj) {
		isObjectExists(modelObj,false);
	}

	@Override
	public final void onBeforeUpdate(PersistenceType type, T modelObj) {
		switch (type) {
			case DB:
				onBeforeUpdateDB(modelObj);
				break;
			default:
				break;
		}
		super.onBeforeUpdate(type, modelObj);
	}

	public void onBeforeUpdateDB(T modelObj) {
		isObjectExists(modelObj,true);
	}
	
	public void isObjectExists(T modelObj, boolean isUpdate) {
	}

	@Override
	public final void onBeforeDelete(PersistenceType type, T modelObj) {
		switch (type) {
		case DB:
			onBeforeDeleteDB(modelObj);
			break;
		default:
			break;
		}
		super.onBeforeDelete(type, modelObj);
	}

	public void onBeforeDeleteDB(T modelObj) {
		isReferenceExists(modelObj);
	}

	public void isReferenceExists(T modelObj) {
		List<Filter> filters = new ArrayList<>();
		filters.add(new SimpleFilter(Filter.GLOBAL_SEARCH, modelObj.getSid()));
		List<String> projectedFields = new ArrayList<>(1);
		projectedFields.add("sid");
		addPersistenceOption(SearchOptions.GLOBAL_SEARCH, true);
		List<?> results = getAllByPage(PersistenceType.SEARCH, filters, null, 0, 0, projectedFields);
		removePersistenceOption(SearchOptions.GLOBAL_SEARCH);
		if (results.size() > 1) {
			throw new EntityReferenceExistsException(ErrorCode.ENTITY_REFERENCE_EXISTS, new String[] {});
		}
	}
	
	
	@Override
	public final void onAfterSave(PersistenceType type, Object modelObj) {
		super.onAfterSave(type, modelObj);
		switch (type) {
			case DB:
				onAfterSaveDB((T)modelObj);
				break;			
			default:
				break;
		}		
	}

	public void onAfterSaveDB(T modelObj) {
	  changelogService.createChangeLog("Table1", modelObj.getSid(), Constants.SAVED, modelObj);
	}
	
	@Override
	public final void onAfterUpdate(PersistenceType type, Object modelObj) {		
		switch (type) {
			case DB:
				onAfterUpdateDB((T)modelObj);
				break;			
			default:
				break;
		}	
		super.onAfterUpdate(type, modelObj);	
	}

	public void onAfterUpdateDB(T modelObj) {
	  changelogService.createChangeLog("Table1", modelObj.getSid(), Constants.UPDATED, modelObj);
	}
	
	@Override
	public final void onAfterDelete(PersistenceType type, Object modelObj) {		
		switch (type) {
			case DB:
				onAfterDeleteDB((T)modelObj);
				break;			
			default:
				break;
		}
		super.onAfterDelete(type, modelObj);		
	}

	public void onAfterDeleteDB(T modelObj) {
	  changelogService.createChangeLog("Table1", modelObj.getSid(), Constants.DELETED, modelObj);
	}	
	
	@Override
	public List<String> onBeforeGeneratedValidation() {
		// TODO Auto-generated method stub
	return null;
	}
	@Override
	public void onAfterGeneratedValidation(List<ValidationError> validationErrors) {
		// TODO Auto-generated method stub
	}
		public void onBeforeSaveSearch(T modelObj) {}
	
	public void setChangelogService(ChangelogBLBaseImpl changelogService) {
		this.changelogService=changelogService;
	}

	
	
}