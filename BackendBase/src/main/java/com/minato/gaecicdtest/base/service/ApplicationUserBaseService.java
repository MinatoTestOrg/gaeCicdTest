package com.minato.gaecicdtest.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import com.vs.rappit.base.dal.providers.SearchOptions;
import com.vs.rappit.base.dal.providers.PersistenceType;
import com.vs.rappit.base.logic.BaseBusinessLogic;
import com.vs.rappit.base.mail.model.EmailAddress;
import com.vs.rappit.base.model.wrapper.UserPrivilegePerimeter;
import com.vs.rappit.base.dal.Filter;
import com.vs.rappit.base.dal.SimpleFilter;
import com.vs.rappit.base.dal.Sort;
import com.vs.rappit.base.dal.Sort.Direction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.vs.rappit.base.authentication.logic.IAppUserPrivilegeCache;
import com.vs.rappit.base.authentication.logic.AppUserPrivilegeCache;
import com.vs.rappit.base.cache.CacheManager;
import com.vs.rappit.base.service.changelog.ChangelogBLBaseImpl;
import com.vs.rappit.base.util.Constants;
import com.vs.rappit.base.exception.AlreadyExistsException;
import com.vs.rappit.base.util.ErrorCode;
import com.vs.rappit.base.exception.ValidationError;

import com.minato.gaecicdtest.base.model.ApplicationUserBase;
import com.minato.gaecicdtest.base.service.IApplicationUserBaseService;
import com.minato.gaecicdtest.model.Roles;
import com.vs.rappit.base.appconfiguration.AppConfigurationCache;
import com.vs.rappit.base.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import com.vs.rappit.base.dal.providers.DBOptions;

import com.vs.rappit.base.dal.providers.DeleteOptions;

public abstract class ApplicationUserBaseService<T extends ApplicationUserBase> extends BaseBusinessLogic<T> implements IApplicationUserBaseService<T>
{
	
	@Autowired
	private AppUserPrivilegeCache<T> userCache;
	
	@Autowired
	private AppConfigurationCache appConfigCache;
	
	private ChangelogBLBaseImpl changelogService;
	
	public ApplicationUserBaseService(Class<T> modelClass) {
		super(modelClass);
		addPersistenceOption(DBOptions.DELETE_OPTION, DeleteOptions.PERMANENT_DELETE);
		addPersistenceOption(SearchOptions.SEARCH_INDEX_NAME, getTableName());

	}

	@Override
	public PersistenceType[] getOtherPersistenceTypes() {
		return new PersistenceType[] {PersistenceType.SEARCH};
	}
	
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

	public void onBeforeSaveSearch(T modelObj) {}

	public void onBeforeSaveDB(T modelObj) {
		if(modelObj.getEmail() != null) {
			modelObj.setEmailInLowerCase(modelObj.getEmail().toLowerCase());
		}
		isObjectExists(modelObj,false);
		setRoles(modelObj);
	}
	
	@Override
	public final void onAfterSave(PersistenceType type, Object modelObj) {
		super.onAfterSave(type, modelObj);
		switch (type) {
			case DB:
				onAfterSaveDB((T) modelObj);
				break;			
			default:
				break;
		}		
	}

	public void onAfterSaveDB(T modelObj) {
		changelogService.createChangeLog("ApplicationUser", modelObj.getSid().toString(), Constants.SAVED, modelObj);
		invalidateCache(modelObj);
	}

	public final void onBeforeUpdate(PersistenceType type, T modelObj) {
		switch (type) {
			case DB:
				onBeforeUpdateDB(modelObj);
				break;
			case SEARCH:
				onBeforeUpdateSearch(modelObj);
				break;
			default:
				break;
		}
		super.onBeforeUpdate(type, modelObj);
	}

	public void onBeforeUpdateSearch(T modelObj) {}

	public void onBeforeUpdateDB(T modelObj) {
		if(modelObj.getEmail() != null) {
			modelObj.setEmailInLowerCase(modelObj.getEmail().toLowerCase());
		}
		isObjectExists(modelObj,true);
		setRoles(modelObj);
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
		changelogService.createChangeLog("ApplicationUser", modelObj.getSid().toString(), Constants.UPDATED, modelObj);
		invalidateCache(modelObj);
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
		changelogService.createChangeLog("ApplicationUser", modelObj.getSid().toString(), Constants.DELETED, modelObj);
		invalidateCache(modelObj);
	}

	private void invalidateCache(T modelObj) {
		userCache.invalidate(modelObj.getEmail());
	}
	
	protected void setRoles(T modelObj) {
		List<String> userRoles = new ArrayList<>();
				if(modelObj.isDevAdmin()){
			userRoles.add(Roles.DEVADMIN.getRoleName());
		}
		modelObj.setUserRoles(userRoles);
	}
	
	public List<EmailAddress> getUsersByRole(UserPrivilegePerimeter rolePerimeterInfo, Integer page, Integer pageSize) {
		List<Filter> filters = new ArrayList<>();
		Map<String, List<Object>> perimeters = rolePerimeterInfo.getPerimeters();
		boolean perimeterApplicable = perimeters != null && perimeters.size() > 0;
		if (perimeterApplicable) {
			perimeters.forEach((perimeterKey, perimeterValue) -> {
				filters.add(new SimpleFilter(perimeterKey, perimeterValue, Filter.Operator.IN));
			});
			if (filters.isEmpty()) {
				return Collections.emptyList();
			}
		}
		filters.add(new SimpleFilter(rolePerimeterInfo.getRoleShortName(), true));
		List<Sort> sorts = new ArrayList<>(1);
		sorts.add(new Sort("email", Direction.ASC));
		List<String> projectedFields = new ArrayList<>(3);
		projectedFields.add("email");
		projectedFields.add("firstName");
		projectedFields.add("lastName");
		List<T> responseList = (List<T>)getAllByPage(PersistenceType.SEARCH, filters, sorts,page, pageSize, projectedFields);
		if (CollectionUtils.isEmpty(responseList)) {
			return Collections.emptyList();
		}
		List<EmailAddress> emailInfoList = new ArrayList<>(responseList.size());
		responseList.forEach(data -> {
			emailInfoList.add(new EmailAddress(data.getEmail(), data.getFirstName()
					+ (StringUtils.isNotBlank(data.getLastName()) ? data.getLastName() : StringUtils.EMPTY)));
		});
		return emailInfoList;
	}
	
	
	
	public void setChangelogService(ChangelogBLBaseImpl changelogService) {
		this.changelogService=changelogService;
	}
	
	
	protected void isObjectExists(T modelObj, boolean isUpdate) {
		T existing = getByField("emailInLowerCase", modelObj.getEmailInLowerCase());
		if (isUpdate) {
			if (existing == null || existing.getSid().equals(modelObj.getSid())) {
				return;
			}
			throw new AlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "ApplicationUser", modelObj.getEmail());

		} else {
			if (existing == null) {
				return;
			}
			throw new AlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "ApplicationUser", modelObj.getEmail());
		}
	}
	
	@Override
	public List<String> onBeforeGeneratedValidation() {
		return null;
	}

	@Override
	public void onAfterGeneratedValidation(List<ValidationError> validationErrors) {}
}
