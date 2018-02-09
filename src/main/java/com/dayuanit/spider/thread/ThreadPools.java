package com.dayuanit.spider.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPools {
	
	//二级目录线程池
	public static final ExecutorService childCatalog = Executors.newSingleThreadExecutor();
	
	//商品详情线程池 注意线程数量的控制，据本人亲测，线程为2最安全，超过2的，必禁IP~
	public static final ExecutorService commodityInfo = Executors.newFixedThreadPool(2);
	
	//图片线程池
	public static final ExecutorService pictureInfo = Executors.newFixedThreadPool(2);

}
