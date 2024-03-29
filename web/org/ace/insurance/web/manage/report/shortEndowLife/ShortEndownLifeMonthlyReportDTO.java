package org.ace.insurance.web.manage.report.shortEndowLife;

import java.util.Date;

public class ShortEndownLifeMonthlyReportDTO {

	private String insuredPerson;
	private int age;
	private String policyTerm;
	private String policyNo;
	private String address;
	private double suminsured;
	private double premium;
	private String paymentType;
	private double commission;
	private String receiptNo;
	private Date paymentDate;
	private String agentName;
	private String liscenseNo;

	public ShortEndownLifeMonthlyReportDTO(String insuredPerson, int age, String policyTerm, String policyNo, String residentAddress, String district, String province,
			double suminsured, double premium, String paymentType, double commission, String receiptNo, Date paymentDate, String agentName, String liscenseNo) {
		this.insuredPerson = insuredPerson;
		this.age = age;
		this.policyTerm = policyTerm;
		this.policyNo = policyNo;
		this.suminsured = suminsured;
		this.premium = premium;
		this.paymentType = paymentType;
		this.commission = commission;
		this.receiptNo = receiptNo;
		this.paymentDate = paymentDate;
		this.agentName = agentName;
		this.liscenseNo = liscenseNo;
		this.address = residentAddress + "," + district + "," + province;

	}

	public String getInsuredPerson() {
		return insuredPerson;
	}

	public int getAge() {
		return age;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public String getAddress() {
		return address;
	}

	public double getSuminsured() {
		return suminsured;
	}

	public double getPremium() {
		return premium;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public double getCommission() {
		return commission;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getLiscenseNo() {
		return liscenseNo;
	}

}
