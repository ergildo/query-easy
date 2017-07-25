package br.com.ecd.queryutil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

/**
 * Tipo de restriçoes de consultas.
 * 
 * @author ergildo.cdias
 *
 */
public enum RestrictionType {
	LIKE {
		@Override
		public Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {

			SimpleExpression like = Restrictions.like(propertyName, String.valueOf(value), matchMode);

			return ignoreCase ? like.ignoreCase() : like;

		}
	},
	EQ {
		@Override
		public Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			SimpleExpression eq = Restrictions.eq(propertyName, value);
			return ignoreCase ? eq.ignoreCase() : eq;
		}
	},
	NE {
		@Override
		public Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			SimpleExpression ne = Restrictions.ne(propertyName, value);
			return ignoreCase ? ne.ignoreCase() : ne;

		}
	},
	IN {
		@Override
		public Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.in(propertyName, (Object[]) value);
		}
	},
	BETWEEN {
		@Override
		public Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.between(propertyName, value, bindValue);
		}
	};

	public abstract Criterion getCriterion(String propertyName, Object value, Object bindValue, MatchMode matchMode,
			boolean ignoreCase);

}
