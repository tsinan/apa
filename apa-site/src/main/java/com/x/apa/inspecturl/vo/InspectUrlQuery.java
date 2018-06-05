package com.x.apa.inspecturl.vo;

/**
 * @author liumeng
 */
public class InspectUrlQuery {

	private String startTime;
	private String endTime;
	private String inspectLevel;
	private String inspectStatus;
	private String category;
	private String brandId;
	private String urlLike;

	@Override
	public String toString() {
		return "InspectUrlQuery [startTime=" + startTime + ", endTime=" + endTime + ", inspectLevel=" + inspectLevel
				+ ", inspectStatus=" + inspectStatus + ", category=" + category + ", brandId=" + brandId + ", urlLike="
				+ urlLike + ", toString()=" + super.toString() + "]";
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getInspectLevel() {
		return inspectLevel;
	}

	public void setInspectLevel(String inspectLevel) {
		this.inspectLevel = inspectLevel;
	}

	public String getInspectStatus() {
		return inspectStatus;
	}

	public void setInspectStatus(String inspectStatus) {
		this.inspectStatus = inspectStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getUrlLike() {
		return urlLike;
	}

	public void setUrlLike(String urlLike) {
		this.urlLike = urlLike;
	}

}
