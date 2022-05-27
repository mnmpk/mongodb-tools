package com.mongodb.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mongodb.document.OfficeDocument;
import com.mongodb.mapper.OfficeMapper;
import com.mongodb.mysql.entity.Office;

public class OfficeProcessor implements ItemProcessor<Office, OfficeDocument> {

	private static final Logger log = LoggerFactory.getLogger(OfficeProcessor.class);

	@Override
	public OfficeDocument process(final Office office) throws Exception {
		OfficeDocument transformedOffice = OfficeMapper.INSTANCE.officeToDocument(office);
		log.info("Converting (" + office + ") into (" + transformedOffice + ")");

		return transformedOffice;
	}
}
