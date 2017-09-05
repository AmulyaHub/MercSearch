package com.mercedes.operations.aws;

import com.google.gson.Gson;
import com.merecedes.data.entity.MercedesQueryEntity;
import com.merecedes.data.entity.MercedesResponse;

public class EntitySearcher {
	private Gson gson = new Gson();

	public String search(MercedesQueryEntity queryEntity) {
		String key = gson.toJson(queryEntity);
		MercedesResponse entity = new MercedesResponse();
		entity = AWSFactory.getInstance().getDynamoDBMapper().load(MercedesResponse.class, key);
		return entity.getPriceEntity();
	}

}