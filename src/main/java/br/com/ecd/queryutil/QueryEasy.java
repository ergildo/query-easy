/**
 * 
 */
package br.com.ecd.queryutil;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import br.com.ecd.queryutil.util.QueryUtils;

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
public class QueryEasy {

	/**
	 * @param filter
	 * @param entity
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T find(QueryFilter filter, Class<T> entity, Session session) {
		Criteria criteria = createCriteria(filter, entity, session);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (T) criteria.uniqueResult();
	}

	/**
	 * @param filter
	 * @param entity
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> list(QueryFilter filter, Class<T> entity, Session session) {
		QueryUtils query = QueryUtils.getInstance();
		Criteria criteria = query.createCriteria(session, entity, filter);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * @param filter
	 * @param entity
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T count(QueryFilter filter, Class<?> entity, Session session) {
		QueryUtils query = QueryUtils.getInstance();
		Criteria criteria = query.createCriteria(session, entity, filter);
		criteria.setProjection(Projections.rowCount());
		return (T) criteria.uniqueResult();
	}

	/**
	 * @param filter
	 * @param entity
	 * @param session
	 * @return
	 */
	private static <T> Criteria createCriteria(QueryFilter filter, Class<T> entity, Session session) {
		QueryUtils query = QueryUtils.getInstance();
		Criteria criteria = query.createCriteria(session, entity, filter);
		return criteria;
	}

}
