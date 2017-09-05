package com.mercedes.operations.config;

public class MercedesSearchConstants {
	public static final String VENDOR_URL = "https://www.cardekho.com/sellyourusedcars.do";
	public static final String API_URL = "https://www.cardekho.com/searchUsedCar.do";

	public static final Integer START_YEAR = 1980;
	public static final Integer END_YEAR = 2017;

	public static final Integer NUM_OWNERS = 4;
	public static final Integer KM_RANGE_MAX = 100000;

	public static final String[] CITY_NAMES = { "Mumbai", "Bangalore", "Delhi" };
	public static final String YEAR_MODEL_KEY_DYNAMO = "yearModel";
	public static final String VARIANT_KEY_DYNAMO = "variant";
	public static final String CONFIG_KEYS = "config_keys";
	
}
