package br.com.ecd.queryutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.criterion.MatchMode;

import br.com.ecd.queryutil.RestrictionType;

/**
 * Define um campo de consulta, esta anotação tem as seguintes propriedades.
 * 
 * @author ergildo.cdias
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface QueryField {

	/**
	 * Define o nome da propriedade, quando não informado nome da propriedade
	 * anotada deve ser equivalente ao nome da propriedade na entidade.
	 */
	String property() default "";

	/**
	 * Define o tipo de restrição (EQ, NE, LIKE...). O valor padrão é EQ.
	 */
	RestrictionType restriction() default RestrictionType.EQ;

	/**
	 * Define o campo vinculado. Utilizado para definir dois parâmentro na mesma
	 * restrição, por exemplo: restrições do tipo BETWEEN precisa do valor
	 * inicial e final, neste caso use esta propriedade para definir qual campo
	 * armazena o valor final.
	 */
	String bindField() default "";

	/**
	 * Define o método de comparação (START, END, ANYWHERE). O valor padrão é
	 * START.
	 */
	MatchMode matchMode() default MatchMode.START;

	/**
	 * Define se o método de comparação é case sentive ou não.
	 */
	boolean ignoreCase() default false;

}
