package org.broadleafcommerce.vendor.cybersource.service.tax.message;

import java.util.ArrayList;
import java.util.List;

import org.broadleafcommerce.util.money.Money;
import org.broadleafcommerce.vendor.cybersource.service.message.CyberSourceBillingRequest;
import org.broadleafcommerce.vendor.cybersource.service.message.CyberSourceRequest;
import org.broadleafcommerce.vendor.cybersource.service.type.CyberSourceServiceType;

public class CyberSourceTaxRequest extends CyberSourceRequest {

private static final long serialVersionUID = 1L;
	
	protected String currency;
	protected List<CyberSourceTaxItemRequest> itemRequests = new ArrayList<CyberSourceTaxItemRequest>();
	protected CyberSourceBillingRequest billingRequest;
	protected Money grandTotal;
	protected Boolean useGrandTotal = Boolean.FALSE;
	protected java.lang.String nexus;
	protected java.lang.String noNexus;
	protected java.lang.String orderAcceptanceCity;
	protected java.lang.String orderAcceptanceCounty;
	protected java.lang.String orderAcceptanceCountry;
	protected java.lang.String orderAcceptanceState;
	protected java.lang.String orderAcceptancePostalCode;
	protected java.lang.String orderOriginCity;
	protected java.lang.String orderOriginCounty;
	protected java.lang.String orderOriginCountry;
	protected java.lang.String orderOriginState;
	protected java.lang.String orderOriginPostalCode;
    
	public CyberSourceTaxRequest() {
		super(CyberSourceServiceType.TAX);
	}

	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public List<CyberSourceTaxItemRequest> getItemRequests() {
		return itemRequests;
	}
	
	public void setItemRequests(List<CyberSourceTaxItemRequest> itemRequests) {
		this.itemRequests = itemRequests;
	}
	
	public Money getGrandTotal() {
		return grandTotal;
	}
	
	public void setGrandTotal(Money grandTotal) {
		this.grandTotal = grandTotal;
	}
	
	public Boolean getUseGrandTotal() {
		return useGrandTotal;
	}
	
	public void setUseGrandTotal(Boolean useGrandTotal) {
		this.useGrandTotal = useGrandTotal;
	}
	
	public java.lang.String getNexus() {
		return nexus;
	}
	
	public void setNexus(java.lang.String nexus) {
		this.nexus = nexus;
	}
	
	public java.lang.String getNoNexus() {
		return noNexus;
	}
	
	public void setNoNexus(java.lang.String noNexus) {
		this.noNexus = noNexus;
	}
	
	public java.lang.String getOrderAcceptanceCity() {
		return orderAcceptanceCity;
	}
	
	public void setOrderAcceptanceCity(java.lang.String orderAcceptanceCity) {
		this.orderAcceptanceCity = orderAcceptanceCity;
	}
	
	public java.lang.String getOrderAcceptanceCounty() {
		return orderAcceptanceCounty;
	}
	
	public void setOrderAcceptanceCounty(java.lang.String orderAcceptanceCounty) {
		this.orderAcceptanceCounty = orderAcceptanceCounty;
	}
	
	public java.lang.String getOrderAcceptanceCountry() {
		return orderAcceptanceCountry;
	}
	
	public void setOrderAcceptanceCountry(java.lang.String orderAcceptanceCountry) {
		this.orderAcceptanceCountry = orderAcceptanceCountry;
	}
	
	public java.lang.String getOrderAcceptanceState() {
		return orderAcceptanceState;
	}
	
	public void setOrderAcceptanceState(java.lang.String orderAcceptanceState) {
		this.orderAcceptanceState = orderAcceptanceState;
	}
	
	public java.lang.String getOrderAcceptancePostalCode() {
		return orderAcceptancePostalCode;
	}
	
	public void setOrderAcceptancePostalCode(java.lang.String orderAcceptancePostalCode) {
		this.orderAcceptancePostalCode = orderAcceptancePostalCode;
	}
	
	public java.lang.String getOrderOriginCity() {
		return orderOriginCity;
	}
	
	public void setOrderOriginCity(java.lang.String orderOriginCity) {
		this.orderOriginCity = orderOriginCity;
	}
	
	public java.lang.String getOrderOriginCounty() {
		return orderOriginCounty;
	}
	
	public void setOrderOriginCounty(java.lang.String orderOriginCounty) {
		this.orderOriginCounty = orderOriginCounty;
	}
	
	public java.lang.String getOrderOriginCountry() {
		return orderOriginCountry;
	}
	
	public void setOrderOriginCountry(java.lang.String orderOriginCountry) {
		this.orderOriginCountry = orderOriginCountry;
	}
	
	public java.lang.String getOrderOriginState() {
		return orderOriginState;
	}
	
	public void setOrderOriginState(java.lang.String orderOriginState) {
		this.orderOriginState = orderOriginState;
	}
	
	public java.lang.String getOrderOriginPostalCode() {
		return orderOriginPostalCode;
	}
	
	public void setOrderOriginPostalCode(java.lang.String orderOriginPostalCode) {
		this.orderOriginPostalCode = orderOriginPostalCode;
	}

	public CyberSourceBillingRequest getBillingRequest() {
		return billingRequest;
	}
	
	public void setBillingRequest(CyberSourceBillingRequest billingRequest) {
		this.billingRequest = billingRequest;
	}
	
}