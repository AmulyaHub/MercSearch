package com.merecedes.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.gson.Gson;

@DynamoDBTable(tableName = "config_keys")

public class ConfigDataset {

	private Model yearModel;
	private Variant variant;

	@DynamoDBHashKey(attributeName = "yearModel")

	public String getYearModel() {
		return new Gson().toJson(yearModel);
	}

	public void setYearModel(Model yearModel) {
		this.yearModel = yearModel;
	}

	@DynamoDBRangeKey(attributeName = "variant")
	public String getVariant() {
		return new Gson().toJson(variant);
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	@Override
	public String toString() {
		return "ConfigDataset [yearModel=" + yearModel + ", variant=" + variant + "]";
	}

}
