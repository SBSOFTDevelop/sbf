package ru.sbsoft.shared.model;

import java.util.Date;

public class CodebaseModel extends TehnologyModel {

	private String CODE_VALUE;
	private String NAME_VALUE;
	private String NOTES;
	private Date DATE_START;
	private Date DATE_END;

	public CodebaseModel() {
	}

	public LookupInfoModel getLookupValue() {
		LookupInfoModel l = new LookupInfoModel();
		l.setID(getId());
		l.setSemanticKey(CODE_VALUE);
		l.setSemanticName(NAME_VALUE);
		return l;
	}

	public String getCODE_VALUE() {
		return CODE_VALUE;
	}

	public void setCODE_VALUE(String CODE_VALUE) {
		this.CODE_VALUE = CODE_VALUE;
	}

	public String getNAME_VALUE() {
		return NAME_VALUE;
	}

	public void setNAME_VALUE(String NAME_VALUE) {
		this.NAME_VALUE = NAME_VALUE;
	}

	public String getNOTES() {
		return NOTES;
	}

	public void setNOTES(String NOTES) {
		this.NOTES = NOTES;
	}

	public Date getDATE_START() {
		return DATE_START;
	}

	public void setDATE_START(Date DATE_START) {
		this.DATE_START = DATE_START;
	}

	public Date getDATE_END() {
		return DATE_END;
	}

	public void setDATE_END(Date DATE_END) {
		this.DATE_END = DATE_END;
	}


}
