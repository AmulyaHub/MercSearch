package com.merecedes.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "mercedes_data")

public class MercedesResponse {
	private String queryEntity;
	private String priceEntity;

	@DynamoDBHashKey(attributeName = "queryEntity")
	public String getQueryEntity() {
		return queryEntity;
	}

	public void setQueryEntity(String queryEntity) {
		this.queryEntity = queryEntity;
	}

	@DynamoDBAttribute(attributeName = "priceEntity")
	public String getPriceEntity() {
		return priceEntity;
	}

	public void setPriceEntity(String priceEntity) {
		this.priceEntity = priceEntity;
	}

	@Override
	public String toString() {
		return "MercedesResponse [queryEntity=" + queryEntity + ", priceEntity=" + priceEntity + "]";
	}

}
