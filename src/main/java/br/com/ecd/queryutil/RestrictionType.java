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
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {

			SimpleExpression like = Restrictions.like(property, String.valueOf(value), matchMode);
			return ignoreCase ? like.ignoreCase() : like;

		}
	},
	EQ {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			SimpleExpression eq = Restrictions.eq(property, value);
			return ignoreCase ? eq.ignoreCase() : eq;
		}
	},
	NE {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			SimpleExpression ne = Restrictions.ne(property, value);
			return ignoreCase ? ne.ignoreCase() : ne;

		}
	},
	IN {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.in(property, (Object[]) value);
		}
	},
	BETWEEN {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.between(property, value, bindValue);
		}
	},
	GT {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.gt(property, value);
		}
	},
	GE {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.ge(property, value);
		}
	},
	LT {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.lt(property, value);
		}
	},
	LE {
		@Override
		public Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
				boolean ignoreCase) {
			return Restrictions.le(property, value);
		}
	};

	public abstract Criterion getCriterion(String property, Object value, Object bindValue, MatchMode matchMode,
			boolean ignoreCase);

}
