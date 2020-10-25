package com.webservice.view;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;

public class PaymentView {

    @ApiModelProperty(notes = "Total payment amount.", example = "0.00")
    private BigDecimal totalAmount;

    @ApiModelProperty(notes = "Integration with customer table.")
    private String customerCode;

    @ApiModelProperty(notes = "Integration with reservation table.")
    private String reservationCode;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}

}
