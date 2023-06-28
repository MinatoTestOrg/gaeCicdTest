package com.minato.gaecicdtest.base.model;

import com.vs.rappit.base.annotations.Searchable;
import com.vs.rappit.base.authentication.UserPrivilege;
import com.vs.rappit.base.annotations.Table;

@Table(name="ApplicationUser", keys={"sid"})
public class ApplicationUserBase extends UserPrivilege {





}