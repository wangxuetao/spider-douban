package com.dayuanit.spider.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dayuanit.spider.domain.Commodity;
import com.dayuanit.spider.mapper.CommodityMapper;
import com.dayuanit.spider.service.CommodityService;

@Service
public class CommodityServiceImpl implements CommodityService {
	
	@Autowired
	private CommodityMapper commodityMapper;

	@Override
	public void save(Commodity commo) {
		try {
			commodityMapper.add(commo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void checkImg() {
		List<String> list = commodityMapper.listPicName();
		System.out.println(">>>>>>>" + list.size());
		
		for (String picName : list) {
			File file = new File("/book-img/" + picName);
			if (!file.exists()) {
				System.out.println(">>>>" + picName);
			} else {
				//System.out.println("exist:" + picName);
			}
			
		}
	}

}
