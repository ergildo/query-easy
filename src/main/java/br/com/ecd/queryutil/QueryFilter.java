package br.com.ecd.queryutil;

/**
 * Fitro de consulta personaliza.
 * 
 * @author ergildo.cdias
 *
 */
public class QueryFilter {

	private String sortField;
	private OrderField sortOrder;
	private int firstResult;
	private int maxResult;

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public OrderField getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(OrderField sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

}
