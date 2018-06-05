package com.x.apa.loader.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.ExportUtils;
import com.x.apa.loader.dao.SensitiveDomainDao;
import com.x.apa.loader.data.SensitiveDomain;

/**
 * @author liumeng
 */
@Component
public class SensitiveDomainServiceImpl implements SensitiveDomainService, Constant {

	@Autowired
	private SensitiveDomainDao sensitiveDomainDao;

	@Autowired
	private RawDomainService rawDoaminService;

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#querySensitiveDomains()
	 */
	@Override
	public List<SensitiveDomain> querySensitiveDomains() {
		return sensitiveDomainDao.querySensitiveDomains("", "", "");
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#querySensitiveDomainNames()
	 */
	@Override
	public Set<String> querySensitiveDomainNames() {
		Set<String> domainSet = Sets.newLinkedHashSet();
		List<SensitiveDomain> sensitiveDomains = sensitiveDomainDao.querySensitiveDomains("", "", "");
		for (SensitiveDomain sensitiveDomain : sensitiveDomains) {
			String domain = sensitiveDomain.getDomainName();
			domainSet.add(domain);
		}
		return domainSet;
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#querySensitiveDomains(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<SensitiveDomain> querySensitiveDomains(PageRequest page, String startTime, String endTime,
			String domainLike) {
		return sensitiveDomainDao.querySensitiveDomains(page, startTime, endTime, domainLike);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#exportSensitiveDomain(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public HSSFWorkbook exportSensitiveDomain(String startTime, String endTime, String domainLike) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("敏感域名");
		sheet.setColumnWidth(0, 30 * 256); // domain
		sheet.setColumnWidth(1, 30 * 256); // registration_date
		sheet.setColumnWidth(2, 30 * 256); // registrar
		sheet.setColumnWidth(3, 30 * 256); // registrant email
		sheet.setColumnWidth(4, 20 * 256); // create_time
		sheet.setColumnWidth(5, 20 * 256); // category
		sheet.setColumnWidth(6, 30 * 256); // sensitive_word_name/sensitive_word/lcs_length

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

		Cell cell2 = headRow.createCell(2);
		cell2.setCellValue("REGISTRAR");
		cell2.setCellStyle(headStyle);

		Cell cell3 = headRow.createCell(3);
		cell3.setCellValue("Registrant-Email");
		cell3.setCellStyle(headStyle);

		Cell cell4 = headRow.createCell(4);
		cell4.setCellValue("CREATE-DATE");
		cell4.setCellStyle(headStyle);

		Cell cell5 = headRow.createCell(5);
		cell5.setCellValue("CATEGORY");
		cell5.setCellStyle(headStyle);

		Cell cell6 = headRow.createCell(6);
		cell6.setCellValue("DETAIL");
		cell6.setCellStyle(headStyle);

		List<SensitiveDomain> sensitiveDomainList = sensitiveDomainDao.querySensitiveDomains(startTime, endTime,
				domainLike);
		int i = 1;
		for (SensitiveDomain sensitiveDomain : sensitiveDomainList) {
			// 设置第一行汇总数据
			Row numRow = sheet.createRow(i);

			cell0 = numRow.createCell(0);
			cell0.setCellValue(sensitiveDomain.getDomainName());
			cell0.setCellStyle(dataStyle);

			cell1 = numRow.createCell(1);
			cell1.setCellValue(DateUtils.toString_YYYY_MM_DD(sensitiveDomain.getRegistrationDate()));
			cell1.setCellStyle(dataStyle);

			cell2 = numRow.createCell(2);
			cell2.setCellValue(sensitiveDomain.getWhoisRegName());
			cell2.setCellStyle(dataStyle);

			cell3 = numRow.createCell(3);
			cell3.setCellValue(sensitiveDomain.getWhoisRegEmail());
			cell3.setCellStyle(dataStyle);

			cell4 = numRow.createCell(4);
			cell4.setCellValue(DateUtils.toString_YYYY_MM_DD(sensitiveDomain.getCreateTime()));
			cell4.setCellStyle(dataStyle);

			cell5 = numRow.createCell(5);
			String category = "";
			switch (sensitiveDomain.getCategory()) {
			case SENSITIVE_DOMAIN_CATEGORY_LEARNING:
				category = "机器识别";
				break;
			case SENSITIVE_DOMAIN_CATEGORY_PEOPLE:
				category = "人工添加";
				break;
			case SENSITIVE_DOMAIN_CATEGORY_RAWDOMAIN:
				category = "原始域名添加";
				break;
			case SENSITIVE_DOMAIN_CATEGORY_IP_REVERSE:
				category = "IP反查";
				break;
			case SENSITIVE_DOMAIN_CATEGORY_EMAIL_REVERSE:
				category = "EMAIL反查";
				break;
			}
			cell5.setCellValue(category);
			cell5.setCellStyle(dataStyle);

			cell6 = numRow.createCell(6);
			cell6.setCellValue(sensitiveDomain.getSensitiveWordName() + "/" + sensitiveDomain.getSensitiveWord() + "/"
					+ sensitiveDomain.getLcsLength());
			cell6.setCellStyle(dataStyle);

			i++;
		}
		return workbook;
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#querySensitiveDomain(java.lang.String)
	 */
	@Override
	public SensitiveDomain querySensitiveDomain(String id) {
		return sensitiveDomainDao.querySensitiveDomain(id);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#createSensitiveDomain(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public SensitiveDomain createSensitiveDomain(SensitiveDomain sensitiveDomain) {
		// 原始域名中设置为敏感域名
		if (sensitiveDomain.getCategory() == SENSITIVE_DOMAIN_CATEGORY_RAWDOMAIN) {
			rawDoaminService.changeRawDomainSensitive(sensitiveDomain.getDomainName(), DOMAIN_SENSITIVE);
		}

		SensitiveDomain exsitSensitiveDomain = sensitiveDomainDao
				.querySensitiveDomainByDomainName(sensitiveDomain.getDomainName());
		if (StringUtils.isNotBlank(exsitSensitiveDomain.getId())) {
			return exsitSensitiveDomain;
		}

		sensitiveDomain.setWhoisRegName("");
		sensitiveDomain.setWhoisRegEmail("");
		return sensitiveDomainDao.saveSensitiveDomain(sensitiveDomain);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#updateSensitiveDomain(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public int updateSensitiveDomain(SensitiveDomain sensitiveDomain) {
		return sensitiveDomainDao.updateSensitiveDomain(sensitiveDomain);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#updateSensitiveDomainWhois(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public int updateSensitiveDomainWhois(SensitiveDomain sensitiveDomain) {
		return sensitiveDomainDao.updateSensitiveDomainWhois(sensitiveDomain);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#deleteLearningSensitiveDomain()
	 */
	@Override
	public int deleteLearningSensitiveDomain() {
		return sensitiveDomainDao.deleteLearningSensitiveDomain();
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#deleteSensitiveDomain(java.lang.String)
	 */
	@Override
	public int deleteSensitiveDomain(String id) {

		SensitiveDomain exsitSensitiveDomain = sensitiveDomainDao.querySensitiveDomain(id);
		if (StringUtils.isBlank(exsitSensitiveDomain.getId())) {
			return 0;
		}
		// 原始域名中设置为非敏感域名
		if (exsitSensitiveDomain.getCategory() == SENSITIVE_DOMAIN_CATEGORY_RAWDOMAIN) {
			rawDoaminService.changeRawDomainSensitive(exsitSensitiveDomain.getDomainName(), DOMAIN_COMMON);
		}
		return sensitiveDomainDao.deleteSensitiveDomain(id);
	}

}
