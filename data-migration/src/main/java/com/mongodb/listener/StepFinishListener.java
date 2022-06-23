package com.mongodb.listener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class StepFinishListener extends StepExecutionListenerSupport {
	private static final Logger log = LoggerFactory.getLogger(StepFinishListener.class);
	private long start;

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		long diffInMillies = Math.abs(start - new Date().getTime());
		long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		int count = stepExecution.getReadCount();
		log.info("!!! Step finished! step execution time:" + diff + " seconds, Record processed:" + count + ", "
				+ count / diff + "items/s");

		return super.afterStep(stepExecution);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.info("!!! Step start");
		start = new Date().getTime();
		super.beforeStep(stepExecution);
	}

}