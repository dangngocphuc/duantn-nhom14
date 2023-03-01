package com.fpoly.datn.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest extends BaseRequest{
	private long billID;
	private Date fromDate;
	private Date toDate;
	private long priceFrom;
	private long priceTo;
	private String userName;
	private long userId;
	private long month;
}
