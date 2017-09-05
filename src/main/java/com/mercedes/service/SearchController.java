package com.mercedes.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.util.StringUtils;
import com.mercedes.operations.aws.ConfigEnricher;
import com.mercedes.operations.aws.EntitySearcher;
import com.mercedes.operations.config.MercedesSearchConstants;
import com.merecedes.data.entity.MercedesQueryEntity;

@RestController
@ComponentScan
@EnableWebMvc
@RequestMapping("/search")
public class SearchController {

	private static final Logger logger = Logger.getLogger(SearchController.class);

	@RequestMapping(value = "/price")
	public String handler(@RequestParam("variant") String variant, @RequestParam("year") Integer year,
			@RequestParam("kmsDriven") Integer kms, @RequestParam("noOfOwners") Integer owners,

			@RequestParam("cityName") String city) {
		String response = "";
		try {
			logger.info("data " + city + variant + "\t" + year + "\t" + owners);
			MercedesQueryEntity queryEntity = new MercedesQueryEntity();
			queryEntity.setCityName(city);
			queryEntity.setKilometers(kms);
			queryEntity.setOwners(owners);
			queryEntity.setVariant(variant);
			queryEntity.setYear(year);
			validate(queryEntity);
			response = new EntitySearcher().search(queryEntity);
		} catch (Exception e) {
			logger.error("ERROR in search ", e);
			response = "{status : RESOURCE_NOT_FOUND}";
		}
		return response;
	}

	private void validate(MercedesQueryEntity queryEntity) {

		if (!StringUtils.isNullOrEmpty(queryEntity.getCityName())
				&& "Bengaluru".equalsIgnoreCase(queryEntity.getCityName())) {
			queryEntity.setCityName("Bangalore");
		}

		int roundedKms = ((queryEntity.getKilometers() + 4999) / 5000) * 5000;
		queryEntity.setKilometers(roundedKms);

	}

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	public String health() {
		logger.debug("Request health ");
		return "{200,STATUS_OK}";
	}

	@RequestMapping(value = "/enrichKeys")
	public String enrichKeys() {
		logger.debug("Enrich Keys Request ");

		Thread enrichKeysThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new ConfigEnricher().enrichMerecedesSearchKeys();
				} catch (ClientProtocolException e) {
					logger.error("ERROR in enriching keys ", e);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		enrichKeysThread.setName("ENRICH_KEYS");
		enrichKeysThread.start();
		return "{200,ENRICH_KEYS_PROCESS_STARTED}";
	}

	@RequestMapping(value = "/enrichValues")
	public String enrichValues() {
		logger.debug("Enrich Values Request ");
		Thread enrichKeysThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new ConfigEnricher().enrichMercedesSearchValues();
				} catch (ClientProtocolException e) {
					logger.error("ERROR in enriching values ", e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("ERROR in enriching values ", e);
					e.printStackTrace();
				}

			}
		});
		enrichKeysThread.setName("ENRICH_VALUES");
		enrichKeysThread.start();
		return "{200,ENRICH_VALUES_PROCESS_STARTED}";
	}

}
