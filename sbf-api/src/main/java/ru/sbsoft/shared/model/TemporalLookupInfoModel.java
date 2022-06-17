package ru.sbsoft.shared.model;

import java.math.BigDecimal;

public class TemporalLookupInfoModel extends LookupInfoModel {

	public TemporalLookupInfoModel() {
	}

	public TemporalLookupInfoModel(BigDecimal ID, BigDecimal semanticID, String semanticKey, String semanticName) {
		super(ID, semanticKey, semanticName);
		setSemanticID(semanticID);
	}
}
