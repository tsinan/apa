
package com.x.apa.common.util;

import java.util.Iterator;

import com.x.apa.common.pageable.Pageable;
import com.x.apa.common.pageable.Sort.Order;

/**
 * @author liumeng
 */
public class PageUtils {

	/**
	 * 填充分页信息
	 * 
	 * @param sql
	 * @param page
	 */
	public static void appendPage(StringBuilder sql, Pageable page) {
		if (page.getSort() != null) {
			sql.append(" order by "); // order
			for (Iterator<Order> orderIt = page.getSort().iterator(); orderIt.hasNext();) {
				Order order = orderIt.next();
				sql.append(order.getProperty()).append(" ").append(order.getDirection());
				if (orderIt.hasNext()) {
					sql.append(",");
				}
			}
		}
		sql.append(" limit ").append(page.getOffset()).append(" , ").append(page.getPageSize());
	}
}
