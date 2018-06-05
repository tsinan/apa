package com.x.apa.common.pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Basic {@code Page} implementation.
 *
 * @param <T>
 *            the type of which the page consists.
 * @author Oliver Gierke
 */
public class PageImpl<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 867755909294344406L;

	private final List<T> content = new ArrayList<T>();
	private final Pageable pageable;
	private final long total;

	/**
	 * Constructor of {@code PageImpl}.
	 *
	 * @param content
	 *            the content of this page, must not be {@literal null}.
	 * @param pageable
	 *            the paging information, can be {@literal null}.
	 * @param total
	 *            the total amount of items available
	 */
	public PageImpl(List<T> content, Pageable pageable, long total) {

		if (null == content) {
			throw new IllegalArgumentException("Content must not be null!");
		}

		this.content.addAll(content);
		this.total = total;
		this.pageable = pageable;
	}

	/**
	 * Creates a new {@link PageImpl} with the given content. This will result
	 * in the created {@link Page} being identical to the entire {@link List}.
	 *
	 * @param content
	 *            must not be {@literal null}.
	 */
	public PageImpl(List<T> content) {
		this(content, null, null == content ? 0 : content.size());
	}

	/**
	 * @see org.springframework.data.domain.Page#getNumber()
	 */
	public int getNumber() {
		return pageable == null ? 0 : pageable.getPageNumber();
	}

	/**
	 * @see org.springframework.data.domain.Page#getSize()
	 */
	public int getSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	/**
	 * @see org.springframework.data.domain.Page#getTotalPages()
	 */
	public int getTotalPages() {
		return getSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getSize());
	}

	/**
	 * @see org.springframework.data.domain.Page#getNumberOfElements()
	 */
	public int getNumberOfElements() {
		return content.size();
	}

	/**
	 * @see org.springframework.data.domain.Page#getTotalElements()
	 */
	public long getTotalElements() {
		return total;
	}

	/**
	 * @see org.springframework.data.domain.Page#hasPreviousPage()
	 */
	public boolean hasPreviousPage() {
		return getNumber() > 0;
	}

	/**
	 * @see org.springframework.data.domain.Page#isFirstPage()
	 */
	public boolean isFirstPage() {
		return !hasPreviousPage();
	}

	/**
	 * @see org.springframework.data.domain.Page#hasNextPage()
	 */
	public boolean hasNextPage() {
		return (getNumber() + 1) * getSize() < total;
	}

	/**
	 * @see org.springframework.data.domain.Page#isLastPage()
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * @see org.springframework.data.domain.Page#iterator()
	 */
	public Iterator<T> iterator() {
		return content.iterator();
	}

	/**
	 * @see org.springframework.data.domain.Page#getContent()
	 */
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	/**
	 * @see org.springframework.data.domain.Page#hasContent()
	 */
	public boolean hasContent() {
		return !content.isEmpty();
	}

	/**
	 * @see org.springframework.data.domain.Page#getSort()
	 */
	public Sort getSort() {
		return pageable == null ? null : pageable.getSort();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String contentType = "UNKNOWN";

		if (content.size() > 0) {
			contentType = content.get(0).getClass().getName();
		}

		return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageImpl<?>)) {
			return false;
		}

		PageImpl<?> that = (PageImpl<?>) obj;

		boolean totalEqual = this.total == that.total;
		boolean contentEqual = this.content.equals(that.content);
		boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);

		return totalEqual && contentEqual && pageableEqual;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
		result = 31 * result + content.hashCode();

		return result;
	}
}
