package com.dayuanit.spider.mapper;

import java.util.List;

import com.dayuanit.spider.domain.Commodity;

public interface CommodityMapper {
	
	int add(Commodity commodity);
	
	List<String> listPicName();

}