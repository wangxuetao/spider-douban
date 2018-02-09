package com.dayuanit.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dayuanit.spider.domain.Catalog;
import com.dayuanit.spider.service.CatalogService;
import com.dayuanit.spider.service.CommodityService;
import com.dayuanit.spider.util.ApiConnector;
import com.dayuanit.spider.util.BeanUtils;

/**
 * 爬虫启动类
 * 
 * 大猿软件 dayuanit.com
 * 
 * @Description:
 * @author 王夫子
 */
public class Test {

	public static void main(String[] args) {

		getCatalog2();

		runTask();

	}

	/**
	 * 获取电子书一级导航栏 和 二级导航栏
	 */
	public static void getCatalog2() {

		String host = "https://read.douban.com";
		String url = "/kind/100";

		CatalogService catalogService = BeanUtils.getBean(CatalogService.class);

		String htmlDocument = ApiConnector.get(host + url);
		Elements eles = Jsoup.parse(htmlDocument).select("ul").select("li.nav-item.foldable");

		for (Element el : eles) {
			String parentCatalogName = el.select("a").first().text();

			String parentUrl = el.select("a").first().attr("href");
			String parentCatalogNum = parentUrl.split("\\/")[2];

			System.out.println("一级目录" + parentCatalogName + " " + parentCatalogNum);

			// 保存一级目录
			Catalog parentCatalog = new Catalog();
			parentCatalog.setCatalogName(parentCatalogName);
			parentCatalog.setCatalogNum(parentCatalogNum);
			parentCatalog.setChildNode(0);
			parentCatalog.setParentNum("-1");

			catalogService.add(parentCatalog);

			Elements childEles = el.select("li.sub-nav-item");
			for (Element childEle : childEles) {
				String childCatalogName = childEle.text().split(" ")[0];

				String childUrl = childEle.select("a").attr("href");
				String childCatalogNum = childUrl.split("\\/")[2];

				System.out.println("二级目录:" + childCatalogName + "" + childCatalogNum);

				Catalog childCatalog = new Catalog();
				childCatalog.setCatalogName(childCatalogName);
				childCatalog.setCatalogNum(childCatalogNum);
				childCatalog.setChildNode(1);
				childCatalog.setParentNum(parentCatalog.getCatalogNum());

				catalogService.add(childCatalog);

			}

		}

	}

	/**
	 * 获取每个二级目录的商品和图片，注意爬取的频率控制，不可贪得无厌，否则后果很严重
	 */
	public static void runTask() {
		CatalogService servicee = BeanUtils.getBean(CatalogService.class);
		servicee.queryChildCatalog();
	}

	/**
	 * 检查图片是否下载完全，和爬虫无关，是我自测用的。
	 */
	public static void checkImg() {
		CommodityService commodityService = BeanUtils.getBean(CommodityService.class);
		commodityService.checkImg();
	}

	/**
	 * 此方法废弃
	 */
	public static void getCatalog() {

		String host = "https://read.douban.com";
		String parentCatalogUrl = "/ebooks/?dcs=book-nav&dcm=douban";
		String htmlDocument = ApiConnector.get(host + parentCatalogUrl);
		System.out.println(htmlDocument);

		Elements els = Jsoup.parse(htmlDocument).select("ul.list.kinds-list.tab-panel").select("li");

		for (Element el : els) {
			System.out.println(el.text());

			String catalogName = el.text();

			// /kind/100
			String childCatalogUrl = el.select("a").first().attr("href");

			// 保存一级目录
			Catalog parentCatalog = new Catalog();
			parentCatalog.setCatalogName(catalogName);
			parentCatalog.setCatalogNum(childCatalogUrl.split("\\/")[2]);
			parentCatalog.setChildNode(0);
			parentCatalog.setParentNum("-1");

			CatalogService catalogService = BeanUtils.getBean(CatalogService.class);
			// catalogService.add(parentCatalog);

			// 请求二级目录地址
			String docuemnt = ApiConnector.get(host + childCatalogUrl);
			Elements childElements = Jsoup.parse(docuemnt).select("ul.sub-nav-list").select("li");

			for (Element childEle : childElements) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				String childCatalogName = childEle.text();

				String childUrl = childEle.select("a").attr("href");

				System.out.println("二级目录的地址:" + childUrl);

				// 保存二级目录
				Catalog childCatalog = new Catalog();
				childCatalog.setCatalogName(childCatalogName);
				childCatalog.setCatalogNum(childUrl.split("\\/")[2]);
				childCatalog.setChildNode(1);
				childCatalog.setParentNum(parentCatalog.getCatalogNum());

				// catalogService.add(childCatalog);
			}

		}
	}

}
