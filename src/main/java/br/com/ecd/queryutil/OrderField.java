package br.com.ecd.queryutil;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import br.com.ecd.queryutil.util.StringUtils;

/**
 * Define método de ordenação.
 * 
 * @author ergildo.cdias
 *
 */
public enum OrderField {
	ASC {
		@Override
		public void addOrder(Criteria criteria, String propertyName) {
			criteria.addOrder(Order.asc(propertyName));

		}
	},
	DESC {
		@Override
		public void addOrder(Criteria criteria, String propertyName) {
			criteria.addOrder(Order.desc(propertyName));
		}
	};

	public static OrderField getValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		String sort = value.toUpperCase();

		if (sort.startsWith("ASC")) {
			return ASC;
		}
		if (sort.startsWith("DESC")) {
			return DESC;
		}
		return null;
	}

	public abstract void addOrder(Criteria criteria, String propertyName);
}
