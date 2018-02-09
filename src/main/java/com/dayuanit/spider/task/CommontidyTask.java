package com.dayuanit.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayuanit.spider.domain.Catalog;
import com.dayuanit.spider.domain.Commodity;
import com.dayuanit.spider.service.CommodityService;
import com.dayuanit.spider.thread.ThreadPools;
import com.dayuanit.spider.util.ApiConnector;
import com.dayuanit.spider.util.BeanUtils;
import com.dayuanit.spider.util.SleepUtils;

public class CommontidyTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CommontidyTask.class);
	
	private String url;
	
	private Catalog catalog;
	
	public CommontidyTask(String url, Catalog catalog) {
		this.url = url;
		this.catalog = catalog;
	}

	@Override
	public void run() {
		String host = "https://read.douban.com";
		
		try {
			Thread.sleep(SleepUtils.getMill());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			String bookInfoDocument = ApiConnector.get(host + url);
			
			Element bookElement = Jsoup.parse(bookInfoDocument).select("div.main").first();
			
			String bookName = bookElement.select("h1.article-title").first().text();
			logger.warn(bookName + " " + url);
			
			String author = bookElement.select("div.article-meta").select("a.author-item").first().text();
			
			String score = bookElement.select("div.article-profile-bd").select("a.book-rating").select("span.score").text();
			
			String pictureUrl = bookElement.select("div.cover.shadow-cover").select("img").first().attr("src");
			//https://img1.doubanio.com/view/ark_article_cover/retina/public/30712317.jpg?v=1487304820.0
			String picUrl[] = pictureUrl.split("\\/");
			
			String pictureName = picUrl[picUrl.length - 1].split("\\?")[0];
			
			String price = bookElement.select("span.current-price-count").text();
			price = price.replace("￥", "");
			
			String daoyan = bookElement.select("div.article-profile-intro.article-abstract.collapse-context").first().text();
			
			//出版社信息
			String press = "";
			//标签
			String lable = "";
			//ISBN
			String isbn = "";
			//出版日期
			String pubDate= "";
			
			Elements bookInfoEles = bookElement.select("div.article-profile-intro.article-profile-secondary").select("p");
			for (Element el : bookInfoEles) {
				String labTag = el.select("span.label").first().text();
				if ("出版社".equalsIgnoreCase(labTag)) {
					press = el.select("span.labeled-text").first().text();
				}
				
				if ("出版日期".equalsIgnoreCase(labTag)) {
					pubDate = el.select("span.labeled-text").first().text();
				}
				
				if ("标签".equalsIgnoreCase(labTag)) {
					lable = el.select("span.labeled-text").first().select("meta").attr("content");
				}
				
				if ("ISBN".equalsIgnoreCase(labTag)) {
					isbn = el.select("span.labeled-text").first().text();
				}
				
			}
			
			
			System.out.println(">>>>>>>>" + bookName + "-" + author + "-" + press + "-" + score + "-" + price + "-" + daoyan + "-" + pictureUrl);
			System.out.println(">>>>>" +  pubDate + "=" + lable + "=" + isbn + "=" + pictureName);
			String catalogInfo = bookElement.select("div.article-profile-intro.table-of-contents").first().text();
			
			Commodity commo = new Commodity();
			commo.setAuthor(author);
			commo.setChildCatalogNum(catalog.getCatalogNum());
			commo.setCommCatalog(catalogInfo);
			commo.setCommLabel(lable);
			commo.setCommName(bookName);
			commo.setCommPrice(price);
			commo.setIsbn(isbn);
			commo.setParentCatalogNum(catalog.getParentNum());
			commo.setPictureName(pictureName);
			commo.setPreface(daoyan);
			commo.setPress(press);
			commo.setPubData(pubDate);
			commo.setScore(score);
			
			//将商品保存数据库
			CommodityService commodityService = BeanUtils.getBean(CommodityService.class);
			commodityService.save(commo);
			
			// 将图片地址放入队列
			ThreadPools.pictureInfo.execute(new DownLoadPictureTask(pictureUrl, pictureName));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		
	}
	

}
