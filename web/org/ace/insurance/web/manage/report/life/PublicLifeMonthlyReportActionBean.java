package org.ace.insurance.web.manage.report.life;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;

import org.ace.insurance.common.ErrorCode;
import org.ace.insurance.common.Utils;
import org.ace.insurance.common.utils.DateUtils;
import org.ace.insurance.report.TLF.MonthlyIncomeReportCriteria;
import org.ace.insurance.report.life.service.interfaces.ILifePolicyReportService;
import org.ace.insurance.system.common.branch.Branch;
import org.ace.insurance.system.common.salesPoints.SalesPoints;
import org.ace.insurance.user.User;
import org.ace.insurance.web.manage.report.shortEndowLife.ShortEndownLifeMonthlyReportDTO;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.SelectEvent;

@ViewScoped
@ManagedBean(name = "PublicLifeMonthlyReportActionBean")
public class PublicLifeMonthlyReportActionBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{LifePolicyReportService}")
	private ILifePolicyReportService lifePolicyReportService;

	public void setLifePolicyReportService(ILifePolicyReportService lifePolicyReportService) {
		this.lifePolicyReportService = lifePolicyReportService;
	}

	private MonthlyIncomeReportCriteria criteria;
	private List<ShortEndownLifeMonthlyReportDTO> publicLifeMonthlyReportList;
	private User user;

	@PostConstruct
	private void init() {
		user = (User) getParam("LoginUser");
		resetCriteria();
	}

	public void resetCriteria() {
		Date today = new Date();
		criteria = new MonthlyIncomeReportCriteria();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		int month = DateUtils.getMonthFromDate(today) - 1;
		int year = DateUtils.getYearFromDate(today);
		criteria.setStartDate(Utils.getStartDate(year, month));
		criteria.setEndDate(Utils.getEndDate(year, month));
		criteria.setSalePointName(null);
		publicLifeMonthlyReportList = new ArrayList<ShortEndownLifeMonthlyReportDTO>();
	}

	public void filter() {
		publicLifeMonthlyReportList = lifePolicyReportService.findPublicLifePolicyMonthlyReport(criteria);
	}

	public void exportExcel() {
		ExternalContext ec = getFacesContext().getExternalContext();
		ec.responseReset();
		ec.setResponseContentType("application/vnd.ms-excel");
		String fileName = "Endowment Life Monthly Report.xlsx";
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try (OutputStream op = ec.getResponseOutputStream();) {
			PublicLifeMonthlyReportExcel publicLifeMonthlyExcel = new PublicLifeMonthlyReportExcel();
			publicLifeMonthlyExcel.generate(op, publicLifeMonthlyReportList, criteria);
			getFacesContext().responseComplete();
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export PublicLifeMonthlyReportExcel.xlsx", e);
		}
	}

	public MonthlyIncomeReportCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(MonthlyIncomeReportCriteria criteria) {
		this.criteria = criteria;
	}

	public List<ShortEndownLifeMonthlyReportDTO> getPublicLifeMonthlyReportList() {
		return publicLifeMonthlyReportList;
	}

	public List<Branch> getBranchList() {
		return user.getAccessBranchList();
	}

	public void returnSalesPoints(SelectEvent event) {
		SalesPoints salePoint = (SalesPoints) event.getObject();
		criteria.setSalePointId(salePoint.getId());
		criteria.setSalePointName(salePoint.getName());
	}
}
