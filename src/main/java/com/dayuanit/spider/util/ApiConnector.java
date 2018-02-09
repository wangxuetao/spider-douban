package com.dayuanit.spider.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http工具类，爬取的时候，注意请求头的设置，以及cookie的设置，这很重要，是决定爬虫成功的重要因素，
 * 关于请求头，最好使用浏览器的请求头，自己使用浏览器登录豆瓣，看看有哪些请求头，只需要将其放到Httpclient类
 * 中即可。
 * 
 * 大猿软件 dayuanit.com
 * 
 * @Description:
 * @author 王夫子
 */
public final class ApiConnector {
	private static final Logger logger = LoggerFactory.getLogger(ApiConnector.class);

	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;

//	private static Header USER_AGENT = new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3124.10 Safari/537.36");
	private static Header USER_AGENT = new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20200101 Firefox/56.0");
	private static Header UpgradeInsecureRequests = new BasicHeader("Upgrade-Insecure-Requests", "1");
	private static Header Referer = new BasicHeader("Referer", "https://read.douban.com/");
	private static Header Host = new BasicHeader("Host", "read.douban.com");
	private static Header Accept = new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	private static Header AcceptEncoding = new BasicHeader("Accept-Encoding", "gzip, deflate, br");
	private static Header AcceptLanguage = new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8");
	
	//注意 这里要写上你自己的cookie哦~
	private static Header Cookie = new BasicHeader("Cookie", "写上你自己的cookie");
	
	
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 60000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 400;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 30000;

	static SSLConnectionSocketFactory sslsf = null;

	static {
		try {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();

			sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		} catch (Exception e) {
			e.printStackTrace();
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();

		connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

		connManager.setDefaultSocketConfig(socketConfig);
		// Create message constraints
//		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(500)
//				.setMaxLineLength(10000).build();
		// Create connection configuraton
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
//				.setMessageConstraints(messageConstraints).build();
		.build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		httpclient = HttpClients.custom().setConnectionManager(connManager)
				.setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
				.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
//				.setProxy(new HttpHost("117.90.1.208", 9000)).build();
				.build();
	}

	public static String get(String url) {
		return get(url, null, "UTF-8");
	}

	@SuppressWarnings("deprecation")
	public static String get(String url, List<NameValuePair> pairs, String encode) {
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0) {
			for (NameValuePair entry : pairs) {
				if (i == 0 && !url.contains("?")) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try {
					sb.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.warn("encode http get params error, value is {}", value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		logger.info("[HttpUtils Get] begin invoke:" + url.toString());
		
		HttpGet get = new HttpGet(url.toString());
		
		get.setConfig(requestConfig);
		
		get.addHeader(USER_AGENT);
		get.addHeader(UpgradeInsecureRequests);
		get.addHeader(Referer);
		get.addHeader(Host);
		get.addHeader(Accept);
		get.addHeader(AcceptEncoding);
		get.addHeader(AcceptLanguage);
		get.addHeader(Cookie);

		try {
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(get);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					get.abort();
					logger.error("[HttpUtils Get] error, url : {}  , params : {},  status :{}", url, pairs, statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, encode);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error("[HttpUtils Get]get response error, url:{}", url.toString(), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("[HttpUtils Get]Debug url:{} , response string :{},time={}", url.toString(), responseString,
					s2 - s1);
		} catch (Exception e) {
			logger.error("[HttpUtils Get] error, url : {}  , params : {}, response string : {} ,error : {}", url, pairs,
					"", e.getMessage(), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}
}
