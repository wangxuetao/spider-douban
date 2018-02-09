package com.dayuanit.spider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dayuanit.spider.domain.Catalog;
import com.dayuanit.spider.mapper.CatalogMapper;
import com.dayuanit.spider.service.CatalogService;
import com.dayuanit.spider.task.ChildCatalogTask;
import com.dayuanit.spider.thread.ThreadPools;

@Service
public class CatalogServiceImpl implements CatalogService {
	
	@Autowired
	private CatalogMapper catalogMapper;

	@Override
	public void add(Catalog catalog) {
		try {
			catalogMapper.add(catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void queryChildCatalog() {
		List<Catalog> list = catalogMapper.listCatalog(1);
		for (Catalog catalog : list) {
			ThreadPools.childCatalog.execute(new ChildCatalogTask(catalog));
		}
	}

}
