package com.dayuanit.spider.domain;

import java.util.Date;

public class Catalog {

	private Integer id;
	private String catalogName;
	private String catalogNum;
	private Integer childNode;
	private String parentNum;
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCatalogNum() {
		return catalogNum;
	}

	public void setCatalogNum(String catalogNum) {
		this.catalogNum = catalogNum;
	}

	public Integer getChildNode() {
		return childNode;
	}

	public void setChildNode(Integer childNode) {
		this.childNode = childNode;
	}

	public String getParentNum() {
		return parentNum;
	}

	public void setParentNum(String parentNum) {
		this.parentNum = parentNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
