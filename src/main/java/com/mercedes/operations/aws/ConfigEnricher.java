package com.mercedes.operations.aws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mercedes.operations.config.MercedesSearchConstants;
import com.mercedes.operations.http.HttpEnricher;
import com.merecedes.data.entity.ConfigDataset;
import com.merecedes.data.entity.MercedesData;
import com.merecedes.data.entity.MercedesQueryEntity;
import com.merecedes.data.entity.MercedesResponse;
import com.merecedes.data.entity.Model;
import com.merecedes.data.entity.Variant;

public class ConfigEnricher {
	private static final Logger logger = Logger.getLogger(ConfigEnricher.class);
	private Gson gson = new Gson();
	private Integer totalSuccessEnriched = 0;
	private Integer totalFailureEnriched = 0;

	public void enrichMerecedesSearchKeys() throws ClientProtocolException, IOException {

		HttpEnricher enricher = new HttpEnricher();

		for (int year = MercedesSearchConstants.START_YEAR; year <= MercedesSearchConstants.END_YEAR; year++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("modelYear", year + "");
			map.put("parameter", "getModelsAccordingToYear");

			StringBuffer modelNames = enricher.doPost(MercedesSearchConstants.VENDOR_URL, map);
			String[] splittedModels = modelNames.toString().split(",");
			for (String model : splittedModels) {
				if (!StringUtils.isNullOrEmpty(model) && model.toLowerCase().contains("mercedes")) {
					map.put("carName", model);
					map.put("parameter", "getVariantDetailsBasedOnBrandAndModel");
					StringBuffer carNameModelNames = enricher.doPost(MercedesSearchConstants.VENDOR_URL, map);
					JsonParser parser = new JsonParser();
					JsonElement elem = parser.parse(carNameModelNames.toString());
					JsonArray elemArr = elem.getAsJsonArray();
					for (int i = 0; i < elemArr.size(); i++) {
						Variant v = new Gson().fromJson(elemArr.get(i).getAsString(), Variant.class);
						ConfigDataset keyData = new ConfigDataset();
						Model yearModel = new Model();
						yearModel.setModel(model);
						yearModel.setYear(year);
						keyData.setVariant(v);
						keyData.setYearModel(yearModel);
						System.out.println("Key to be persisted " + keyData);
						AWSFactory.getInstance().getDynamoDBMapper().save(keyData);

					}
				}

			}
		}
	}

	public void enrichMercedesSearchValues() throws ClientProtocolException, IOException {
		HttpEnricher enricher = new HttpEnricher();

		Map<String, AttributeValue> lastKeyEvaluated = null;
		do {
			try {
				ScanRequest scanRequest = new ScanRequest().withTableName(MercedesSearchConstants.CONFIG_KEYS)
						.withLimit(10).withExclusiveStartKey(lastKeyEvaluated);

				ScanResult result = AWSFactory.getInstance().getDynamoDBClient().scan(scanRequest);
				for (Map<String, AttributeValue> item : result.getItems()) {
					try {
						Model model = null;
						Variant variant = null;
						for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {

							if (entry.getKey().toString()
									.equalsIgnoreCase(MercedesSearchConstants.YEAR_MODEL_KEY_DYNAMO)) {
								model = gson.fromJson(entry.getValue().getS(), Model.class);
							}

							if (entry.getKey().toString()
									.equalsIgnoreCase(MercedesSearchConstants.VARIANT_KEY_DYNAMO)) {
								variant = gson.fromJson(entry.getValue().getS(), Variant.class);

							}
						}
						if (model != null && model.getYear() != null && variant != null
								&& !(StringUtils.isNullOrEmpty(variant.getCarVariantId()))) {
							enrichForDifferentParams(enricher, variant.getCarVariantId(), model.getYear());

						}
					} catch (Exception e) {
						logger.error("ERROR in enriching merecedes entities for various params ", e);
					}

				}
				lastKeyEvaluated = result.getLastEvaluatedKey();
			} catch (Exception e) {
				logger.error("ERROR in enrich mercedes entity", e);
			}
		} while (lastKeyEvaluated != null);
		System.out.println("Done with generatig mercedes entity data . Success -Failure " + totalSuccessEnriched + "-"
				+ totalFailureEnriched);

	}

	private void enrichForDifferentParams(HttpEnricher enricher, String carVariantId, Integer year)
			throws ClientProtocolException, IOException {
		for (int owners = 1; owners < MercedesSearchConstants.NUM_OWNERS; owners++) {

			for (int km = 0; km < MercedesSearchConstants.KM_RANGE_MAX; km += 5000) {

				for (int city = 0; city < MercedesSearchConstants.CITY_NAMES.length; city++) {
					MercedesQueryEntity queryEntity = new MercedesQueryEntity();

					try {

						Map<String, String> urlParamsMap = new HashMap<String, String>();
						urlParamsMap.put("parameter", "getUsedCarValuation");
						urlParamsMap.put("variant", carVariantId);

						urlParamsMap.put("year", year + "");

						urlParamsMap.put("kmsDriven", km + "");

						urlParamsMap.put("noOfOwners", owners + "");
						urlParamsMap.put("CityName", MercedesSearchConstants.CITY_NAMES[city]);

						StringBuffer response = enricher.doPost(MercedesSearchConstants.API_URL, urlParamsMap);
						MercedesData priceEntity = gson.fromJson(response.toString(), MercedesData.class);

						queryEntity.setCityName(MercedesSearchConstants.CITY_NAMES[city]);
						queryEntity.setKilometers(km);
						queryEntity.setOwners(owners);
						queryEntity.setVariant(carVariantId);
						queryEntity.setYear(year);

						MercedesResponse entityToPersit = new MercedesResponse();
						entityToPersit.setQueryEntity(gson.toJson(queryEntity));
						entityToPersit.setPriceEntity(gson.toJson(priceEntity));

						AWSFactory.getInstance().getDynamoDBMapper().save(entityToPersit);
						System.out.println("Merc entity saved  " + entityToPersit);

						totalSuccessEnriched++;
					} catch (Exception e) {
						logger.error("ERROR in enriching  merecedes  entity for request entity" + queryEntity, e);
						totalFailureEnriched++;
					}

				}
			}
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		new ConfigEnricher().enrichMercedesSearchValues();
	}
}
