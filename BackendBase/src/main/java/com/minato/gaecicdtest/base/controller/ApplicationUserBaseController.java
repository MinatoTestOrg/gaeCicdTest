package com.minato.gaecicdtest.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.vs.rappit.base.dal.providers.PersistenceType;
import com.vs.rappit.base.model.PaginationRequest;
import com.vs.rappit.base.model.PaginationResponse;
import com.vs.rappit.base.model.Primary;
import com.vs.rappit.jersey.base.webservice.BaseWebService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import com.minato.gaecicdtest.base.model.ApplicationUserBase;
import com.minato.gaecicdtest.base.service.IApplicationUserBaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.vs.rappit.jersey.webservice.mapper.DatatableJson;


public class ApplicationUserBaseController<SERVICE extends IApplicationUserBaseService<M>, M extends ApplicationUserBase>
		extends BaseWebService<SERVICE, M> {
	
	public ApplicationUserBaseController(SERVICE logic) {
		super(logic);
	}
	
	@PreAuthorize("hasAccess('/applicationusers/user-details')")
	@GetMapping("user-details")
	public M getCurrentUser() {
		return logic.getCurrentUserWithMenu();
	}

	@PreAuthorize("hasAccess('/applicationusers/')")
	@PostMapping
	public M createAppUser(@RequestBody M modelObj) {
		M existingObj = super.getById(new Primary(modelObj.getEmail()));
		if (existingObj == null) {
			return super.save(modelObj);
		} else {
			return super.update(modelObj);
		}
	}

	@PreAuthorize("hasAccess('/applicationusers/')")
	@PutMapping
	public M updateAppUser(@RequestBody M modelObj) {
		return super.update(modelObj);
	}

	@PreAuthorize("hasAccess('/applicationusers/{sid}')")
	@GetMapping(path = "/{sid}", produces = "application/json")
	public M getApplicationUserBySid(@PathVariable("sid") Primary sid) {
		return super.getById(sid);
	}

	@PreAuthorize("hasAccess('/applicationusers/datatable')")
	@PostMapping("/datatable")
	public PaginationResponse getAllAppUsers(@RequestBody DatatableJson datatableJson) {
		PaginationRequest dataTable = convertToPaginationRequest(datatableJson);
		return logic.getAllByPage(PersistenceType.SEARCH, dataTable);
	}
	
	@PreAuthorize("hasAccess('/applicationusers/{ids}')")
	@DeleteMapping("/{ids}")
	public ResponseEntity deleteApplicationUsers(@PathVariable("ids") String ids) {
		boolean isDeleted = super.delete(ids);
		if (isDeleted)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PreAuthorize("hasAccess('/applicationusers/autosuggest')")
	@GetMapping(path = "/autosuggest", produces = "application/json")
	public List<Object> autoSuggestService(@RequestParam("query") String searchText,@RequestParam(name = "sortColumn", required = false) String sortColumn,@RequestParam(name = "sortOrder", required = false) String sortOrder,@RequestParam("pgNo") int pgNo,@RequestParam("pgLen") int length) {
		return super.autosuggest(searchText,sortColumn,sortOrder,pgNo,length);
	}
	
	
	

}