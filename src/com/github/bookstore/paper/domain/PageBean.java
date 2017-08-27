package com.github.bookstore.paper.domain;

import java.util.List;

public class PageBean<T> {
	
	private int pc ;//当前页码page code
//	private int tp; //总页数 total pages
	private int tr;//总记录数total records
	private int ps; //每页记录数 page size//业务数据
	private List<T> beanlist; //当前页的记录
	
	private String	url; //它就是url后的条件
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	/**
	 * 计算总页数
	 * @return
	 */
	public int getTp() {
		int tp =tr/ps;
		return tr%ps==0 ? tp: tp+1;
	}
	/*public void setTp(int tp) {
		this.tp = tp;
	}*/
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public List<T> getBeanlist() {
		return beanlist;
	}
	public void setBeanlist(List<T> beanlist) {
		this.beanlist = beanlist;
	}
	
	
	
	
}
