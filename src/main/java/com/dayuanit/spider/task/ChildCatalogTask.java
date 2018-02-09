package com.dayuanit.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayuanit.spider.domain.Catalog;
import com.dayuanit.spider.thread.ThreadPools;
import com.dayuanit.spider.util.ApiConnector;
import com.dayuanit.spider.util.SleepUtils;

public class ChildCatalogTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ChildCatalogTask.class);
	
	private Catalog catalog;
	
	public ChildCatalogTask(Catalog catalog) {
		this.catalog = catalog;
	}
	
	@Override
	public void run() {
		String host = "https://read.douban.com";
		String url = "/kind/" + catalog.getCatalogNum();
		String query = "";
		
		for (int start = 60; start < 90; start+=20) {
			
			try {
				
				//随机睡眠，目的是避免豆瓣关进小黑屋~
				Thread.sleep(SleepUtils.getMill());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			query = "?start=" + start + "&sort=hot&promotion_only=False";
			
			String commoditysDocument = ApiConnector.get(host + url + query);
			
			Elements eles = Jsoup.parse(commoditysDocument).select("ul.list-lined.ebook-list.column-list").select("li");
			for (Element el : eles) {
				String bookInfoUrl = el.select("div.info").select("div.title").select("a").first().attr("href");
				System.out.println(">>>>>>" + bookInfoUrl);
				
				ThreadPools.commodityInfo.execute(new CommontidyTask(bookInfoUrl, catalog));
			}
			
		}
		
	}

}
