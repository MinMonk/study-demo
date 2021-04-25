package com.monk.demo;

import java.io.Serializable;
import java.util.List;

/**
 * 内容摘要：分页对象
 */
public class Page implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 返回状态，status=0为正常状态，其他值为错误，前台grid弹出错误提示框，提示msg中的信息
	 */
	private String status;
	/**
	 * 返回信息
	 */
	private String msg;
	/**
	 * 总记录数
	 */
	private long total;
	
	/**
	 * 当前页中存放的记录
	 */
    private List<?> rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
