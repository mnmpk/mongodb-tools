package com.mongodb.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mongodb.document.ProductLineDocument;
import com.mongodb.mapper.ProductLineMapper;
import com.mongodb.mysql.entity.ProductLine;

public class ProductLineProcessor implements ItemProcessor<ProductLine, ProductLineDocument> {

	private static final Logger log = LoggerFactory.getLogger(ProductLineProcessor.class);

	@Override
	public ProductLineDocument process(final ProductLine productLine) throws Exception {
		ProductLineDocument transformedProductLine = ProductLineMapper.INSTANCE.productLineToDocument(productLine);
		transformedProductLine.setImage("https://webimages.mongodb.com/_com_assets/cms/kuyjf3vea2hg34taa-horizontal_default_slate_blue.svg?auto=format%252Ccompress");
		log.info("Converting (" + productLine + ") into (" + transformedProductLine + ")");

		return transformedProductLine;
	}
}
