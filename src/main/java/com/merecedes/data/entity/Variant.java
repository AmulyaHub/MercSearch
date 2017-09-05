package com.merecedes.data.entity;

public class Variant {

	private String variantName;
	private String carVariantId;

	private String startYear;

	private String endYear;

	public String getVariantName() {
		return variantName;
	}

	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}

	public String getCarVariantId() {
		return carVariantId;
	}

	public void setCarVariantId(String carVariantId) {
		this.carVariantId = carVariantId;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	@Override
	public String toString() {
		return "Variant [variantName=" + variantName + ", carVariantId=" + carVariantId + ", startYear=" + startYear
				+ ", endYear=" + endYear + "]";
	}


}