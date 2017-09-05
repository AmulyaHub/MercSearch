package com.mercedes.operations.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class AWSFactory {
	private static AWSFactory instance = null;

	private static final Logger logger = LoggerFactory.getLogger(AWSFactory.class);
	private static DynamoDBMapper mapper = null;
	private static AmazonDynamoDB dynamoDBClient = null;
	private static final String ACCESS_KEY = "AKIAJKB4FQ63ZI6QGBBQ";
	private static final String SECRET_KEY = "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";

	private AWSFactory() {
	}

	public static AWSFactory getInstance() {
		if (instance == null) {
			synchronized (AWSFactory.class) {
				if (instance == null) {
					instance = new AWSFactory();
					BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2)
							.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
					// AmazonDynamoDBClientBuilder.standard()
					// .withRegion(Regions.US_WEST_2)
					// .build();
					// AmazonDynamoDB dynamoDBClient = new
					// AmazonDynamoDBClient(new ProfileCredentialsProvider());
					mapper = new DynamoDBMapper(dynamoDBClient);

				}
			}
		}
		return instance;
	}

	public DynamoDBMapper getDynamoDBMapper() {
		return mapper;

	}

	public AmazonDynamoDB getDynamoDBClient() {
		return dynamoDBClient;

	}
}
