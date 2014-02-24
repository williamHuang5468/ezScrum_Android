package ntut.mobile.ezscrum.model;

import java.util.Date;

public class BurndownChartObject {
	private String _point;
	private Date _date;

	public BurndownChartObject() {}

	public BurndownChartObject(String point, Date date) {
		_point = point;
		_date = date;
	}

	public String getPoint() {
		return _point;
	}

	public Date getDate() {
		return _date;
	}

	public void setPoint(String point) {
		_point = point;
	}

	public void setDate(Date date) {
		_date = date;
	}
}