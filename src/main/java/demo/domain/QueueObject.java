package demo.domain;

import java.io.Serializable;
import java.util.Date;

public class QueueObject implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date = new Date();
	
	public QueueObject() {
		
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ExampleObject [date=" + date + "]";
	}
	
	
}
