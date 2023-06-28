package com.minato.gaecicdtest.base.model;
import com.vs.rappit.base.model.BaseModel;
import com.vs.rappit.base.annotations.Table;
import com.vs.rappit.base.annotations.Searchable;


@Table(name="Table1", keys={"sid"})
public class Table1Base extends BaseModel {

	@Searchable(index = true)
	private String field1;
	@Searchable(index = true)
	private String field2;

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField1() {
		return field1;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField2() {
		return field2;
	}



}