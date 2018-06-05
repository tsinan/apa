package com.x.apa.loader.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.ExportUtils;
import com.x.apa.loader.dao.RawDomainDao;

/**
 * @author liumeng
 */
@Component
public class RawDomainServiceImpl implements RawDomainService, Constant {

	@Autowired
	private RawDomainDao rawDomainDao;

	/**
	 * @see com.x.apa.loader.service.RawDomainService#queryLatestRegistrationDate()
	 */
	@Override
	public Date queryLatestRegistrationDate() {
		Date latestRegistrationDate = rawDomainDao.queryLatestRawDomains().getRegistrationDate();
		return latestRegistrationDate == null ? new Date(0) : latestRegistrationDate;
	}

	/**
	 * @see com.x.apa.loader.service.RawDomainService#queryRawDomains(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<RawDomain> queryRawDomains(PageRequest page, String startTime, String endTime, String domainLike) {
		return rawDomainDao.queryRawDomains(page, startTime, endTime, domainLike);
	}

	/**
	 * @see com.x.apa.loader.service.RawDomainService#queryRawDomains(int, int,
	 *      java.util.Date)
	 */
	@Override
	public List<RawDomain> queryRawDomains(int startPos, int size, Date date) {
		return rawDomainDao.queryRawDomains(startPos, size, date);
	}

	/**
	 * @see com.x.apa.loader.service.RawDomainService#exportRawDomain(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public HSSFWorkbook exportRawDomain(String startTime, String endTime, String domainLike) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("域名");
		sheet.setColumnWidth(0, 50 * 256); // domain
		sheet.setColumnWidth(1, 50 * 256); // registration_date

		// 获取标题/表头/数据style
		CellStyle headStyle = ExportUtils.buildHeaderStyle(workbook);
		CellStyle dataStyle = ExportUtils.buildDataStyle(workbook);

		Row headRow = sheet.createRow(0);
		headRow.setRowStyle(headStyle);

		Cell cell0 = headRow.createCell(0);
		cell0.setCellValue("DOMAIN");
		cell0.setCellStyle(headStyle);

		Cell cell1 = headRow.createCell(1);
		cell1.setCellValue("REGISTRATION-DATE");
		cell1.setCellStyle(headStyle);

		List<RawDomain> rawDomainList = rawDomainDao.queryRawDomains(startTime, endTime, domainLike);
		int i = 1;
		for (RawDomain rawDomain : rawDomainList) {
			// 设置第一行汇总数据
			Row numRow = sheet.createRow(i);

			cell0 = numRow.createCell(0);
			cell0.setCellValue(rawDomain.getDomainName());
			cell0.setCellStyle(dataStyle);

			cell1 = numRow.createCell(1);
			cell1.setCellValue(DateUtils.toString_YYYY_MM_DD(rawDomain.getRegistrationDate()));
			cell1.setCellStyle(dataStyle);

			i++;
		}
		return workbook;
	}

	/**
	 * @see com.x.apa.loader.service.RawDomainService#createRawDomain(com.x.apa.common.data.RawDomain)
	 */
	@Override
	public RawDomain createRawDomain(RawDomain rawDomain) {
		return rawDomainDao.saveRawDomain(rawDomain);
	}

	/**
	 * @see com.x.apa.loader.service.RawDomainService#changeRawDomainSensitive(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeRawDomainSensitive(String domainName, int isSensitive) {
		return rawDomainDao.changeRawDomainSensitive(domainName, isSensitive);
	}
}
