package br.com.ecd.queryutil;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.sql.JoinType;

import br.com.ecd.queryutil.annotation.JoinFilter;
import br.com.ecd.queryutil.annotation.QueryField;
import br.com.ecd.queryutil.util.StringUtils;

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
public class QueryGenerator {

	private Map<String, String> joins;

	public static QueryGenerator getInstance() {
		return new QueryGenerator();
	}

	private QueryGenerator() {

	}

	/**
	 * Cria {@link Criteria} para o filtro informado
	 * 
	 * @param session
	 * @param entity
	 * @param filter
	 * @return
	 */
	public Criteria createCriteria(Session session, Class<?> entity, QueryFilter filter) {

		try {
			if (session == null) {
				throw new IllegalArgumentException("Session não pode ser null");
			}

			if (filter == null) {
				throw new IllegalArgumentException("Filter não pode ser null");
			}

			clearJoins();

			String alias = getAliasByClass(entity);

			Criteria criteria = session.createCriteria(entity, alias);

			addRestritions(criteria, alias, filter);

			addOrder(filter, alias, criteria);

			addPaginator(filter, criteria);

			return criteria;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void clearJoins() {
		joins = new HashMap<String, String>();
	}

	private String getAliasByClass(Class<?> entity) {
		return Introspector.decapitalize(entity.getSimpleName());
	}

	private void addPaginator(QueryFilter filter, Criteria criteria) {
		criteria.setMaxResults(filter.getMaxResult());
		criteria.setFirstResult(filter.getFirstResult());
	}

	private void addOrder(QueryFilter filter, String alias, Criteria criteria)
			throws NoSuchFieldException, SecurityException {
		String sortField = filter.getSortField();

		if (StringUtils.isNotEmpty(sortField)) {
			OrderField sortOrder = filter.getSortOrder();
			if (sortOrder != null) {

				String orderProperty = resolveProperty(alias, sortField, criteria);

				sortOrder.addOrder(criteria, orderProperty);
			}

		}
	}

	private String resolveProperty(String alias, String property, Criteria criteria) {

		int indexOf = property.indexOf(".");

		if (indexOf != -1) {

			String newProperty = property.substring(indexOf + 1);

			String atualProperty = property.substring(0, indexOf);

			String newAlias = createAlias(alias, criteria, atualProperty, JoinType.LEFT_OUTER_JOIN);

			return resolveProperty(newAlias, newProperty, criteria);
		}

		String resolveProperty = getPropertyPath(alias, property);

		return resolveProperty;
	}

	private String createAlias(String alias, Criteria criteria, String property, JoinType joinType) {
		String newAlias = alias.concat("_").concat(property);

		if (joins.containsKey(newAlias)) {
			return newAlias;
		}

		String aliasPath = getPropertyPath(alias, property);

		criteria.createAlias(aliasPath, newAlias, joinType);

		joins.put(newAlias, aliasPath);

		return newAlias;
	}

	private String getPropertyPath(String alias, String property) {
		return alias.concat(".").concat(property);
	}

	/**
	 * Adiciona restricões a consulta({@link Criteria})
	 * 
	 * @param criteria
	 * @param alias
	 * @param filter
	 * @return criteria
	 */
	public Criteria addRestritions(Criteria criteria, String alias, Object filter) {
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

				String fieldName = field.getName();

				if (field.isAnnotationPresent(JoinFilter.class)) {

					JoinFilter joinFilter = field.getAnnotation(JoinFilter.class);

					String newAlias = createAlias(alias, criteria, StringUtils.nvl(joinFilter.property(), fieldName),
							joinFilter.joinType());

					addRestritions(criteria, newAlias, value);

				}

				if (field.isAnnotationPresent(QueryField.class) && value != null) {

					QueryField queryField = field.getAnnotation(QueryField.class);

					String propertyName = getPropertyPath(alias, StringUtils.nvl(queryField.property(), fieldName));

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
	 * @param filtro
	 * @param property
	 * @return
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	private Object getFieldValue(Object filtro, String property) throws IllegalAccessException, NoSuchFieldException {
		if (StringUtils.isEmpty(property)) {
			return null;
		}
		Field field = filtro.getClass().getDeclaredField(property);
		field.setAccessible(true);
		return field.get(filtro);
	}
}
