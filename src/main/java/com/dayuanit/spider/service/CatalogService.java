package com.dayuanit.spider.service;

import com.dayuanit.spider.domain.Catalog;

public interface CatalogService {
	
	void add(Catalog catalog);
	
	void queryChildCatalog();

}
