package com.dayuanit.spider.mapper;

import java.util.List;

import com.dayuanit.spider.domain.Catalog;

public interface CatalogMapper {
	
	int add(Catalog catalog);

	List<Catalog> listCatalog(Integer childNode);
}
