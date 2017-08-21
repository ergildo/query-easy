package br.com.ecd.queryutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.sql.JoinType;

/**
 * Define um filtro de junção. Utilizado para definir filtro de propriedade de
 * um relaciomento. A propriedade anotada deve ser outro filtro, opcionalmente,
 * este filtro pode ou não extender a classe QueryFilter.
 * 
 * @author ergildo.cdias
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface JoinFilter {

	/**
	 * Define o nome da propriedade, quando não informado nome da propriedade
	 * anotada deve ser equivalente ao nome da propriedade na entidade.
	 */
	String property() default "";

	/**
	 * Tipo de Join(INNER_JOIN, LEFT_JOIN...). O valor INNER_JOIN.
	 */
	JoinType joinType() default JoinType.LEFT_OUTER_JOIN;
}
