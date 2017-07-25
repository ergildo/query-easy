package br.com.ecd.queryutil.util;

import java.beans.Introspector;
import java.lang.reflect.Field;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

import br.com.ecd.queryutil.OrderField;
import br.com.ecd.queryutil.QueryFilter;
import br.com.ecd.queryutil.RestrictionType;
import br.com.ecd.queryutil.annotation.JoinFilter;
import br.com.ecd.queryutil.annotation.QueryField;

/**
 * Este utilitário visa facilitar a criação de queries com o hibernate. Por meio
 * de filtros pré definidos ele gera automáticamente as queries implementando,
 * além do filtro de consulta, ordenação e paginação sob demanda. Tudo isso de
 * forma transparente. Otimiza a criação de queries abstraindo a complexidade e
 * reduzindo significativamente a quantidade de código.
 * 
 * @author ergildo.cdias
 *
 */
public class QueryUtils {

	/**
	 * Cria {@link Criteria} para o filtro informado
	 * 
	 * @param session
	 * @param entity
	 * @param filter
	 * @return
	 */
	public static Criteria createCriteria(Session session, Class<?> entity, QueryFilter filter) {

		if (session == null) {
			throw new IllegalArgumentException("Session não pode ser null");
		}

		if (filter == null) {
			throw new IllegalArgumentException("Filter não pode ser null");
		}

		String alias = getAliasByClass(entity);

		Criteria criteria = session.createCriteria(entity, alias);

		addRestritions(criteria, alias, filter);

		addOrder(filter, criteria);

		addPaginator(filter, criteria);

		return criteria;
	}

	private static String getAliasByClass(Class<?> entity) {
		return Introspector.decapitalize(entity.getSimpleName());
	}

	private static void addPaginator(QueryFilter filter, Criteria criteria) {
		criteria.setMaxResults(filter.getMaxResult());
		criteria.setFirstResult(filter.getFirstResult());
	}

	private static void addOrder(QueryFilter filter, Criteria criteria) {
		String sortField = filter.getSortField();

		if (StringUtils.isNotEmpty(sortField)) {
			OrderField sortOrder = filter.getSortOrder();
			if (sortOrder != null) {
				sortOrder.addOrder(criteria, sortField);
			}

		}
	}

	/**
	 * Adiciona restricões a consulta({@link Criteria})
	 * 
	 * @param criteria
	 * @param alias
	 * @param filter
	 * @return criteria
	 */
	public static Criteria addRestritions(Criteria criteria, String alias, Object filter) {
		if (filter == null) {
			throw new IllegalArgumentException("Filter não pode ser null");
		}
		try {
			Class<? extends Object> clazz = filter.getClass();

			for (Field field : clazz.getDeclaredFields()) {

				field.setAccessible(true);
				Object value = field.get(filter);
				if (value == null) {
					continue;
				}

				if (field.isAnnotationPresent(JoinFilter.class)) {

					JoinFilter joinFilter = field.getAnnotation(JoinFilter.class);

					String propertyName = joinFilter.property();
					String property = getAlias(alias, propertyName);
					criteria.createAlias(property, propertyName, joinFilter.joinType());
					addRestritions(criteria, propertyName, value);

				}

				if (field.isAnnotationPresent(QueryField.class) && value != null) {

					QueryField queryField = field.getAnnotation(QueryField.class);

					String propertyName = getAlias(alias, queryField.property());

					String bindField = queryField.bindField();

					Object bindValue = getFieldValue(filter, bindField);

					RestrictionType type = queryField.restriction();

					MatchMode matchMode = queryField.matchMode();

					boolean ignoreCase = queryField.ignoreCase();

					Criterion criterion = type.getCriterion(propertyName, value, bindValue, matchMode, ignoreCase);
					criteria.add(criterion);
				}

			}

			return criteria;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param alias
	 * @param propertyName
	 * @return
	 */
	private static String getAlias(String alias, String propertyName) {
		return alias.concat(".").concat(propertyName);
	}

	/**
	 * @param filtro
	 * @param property
	 * @return
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	private static Object getFieldValue(Object filtro, String property)
			throws IllegalAccessException, NoSuchFieldException {
		if (StringUtils.isEmpty(property)) {
			return null;
		}
		Field field = filtro.getClass().getDeclaredField(property);
		field.setAccessible(true);
		return field.get(filtro);
	}
}
