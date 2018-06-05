package com.x.apa.common.data;

import java.util.Date;

/**
 * @author liumeng
 */
public class BaseData {

	private String id;

	private int deleted;

	private Date createTime;
	private Date updateTime;
	private Date deleteTime;

	@Override
	public String toString() {
		return "BaseData [id=" + id + ", deleted=" + deleted + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", deleteTime=" + deleteTime + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

}
