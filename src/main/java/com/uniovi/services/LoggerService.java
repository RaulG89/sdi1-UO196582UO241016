package com.uniovi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerService {

	private Logger logger;
	
	public LoggerService(Object object) {
		logger = LoggerFactory.getLogger(object.getClass());
	}
	
	public void infoLog(String message) {
		logger.info(message);
	}
	
	public void errorLog(String message) {
		logger.error(message);
	}

	public void debugLog(String message) {
		logger.debug(message);
	}
	
}
