/**
 * 
 */
package br.com.ecd.queryutil.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;

public class ReflexaotUtil {
	/**
	 * @param objeto
	 * @param annotation
	 * @return
	 */
	public static <T> T getValorPorAnotacao(Object objeto, Class<? extends Annotation> annotation) {
		if (objeto == null) {
			return null;
		}
		return getValorPorAnotacao(objeto, objeto.getClass(), annotation);

	}

	/**
	 * @param objeto
	 * @param classe
	 * @param annotation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getValorPorAnotacao(Object objeto, Class<?> classe, Class<? extends Annotation> annotation) {

		try {
			if (objeto != null) {

				Field[] fields = classe.getDeclaredFields();

				for (Field field : fields) {
					if (field.isAnnotationPresent(annotation)) {
						field.setAccessible(true);
						return (T) field.get(objeto);
					}
				}
				Method[] methods = classe.getDeclaredMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(annotation)) {
						return (T) method.invoke(objeto);
					}
				}
				Class<?> superClass = classe.getSuperclass();
				if (isNotObject(superClass)) {
					return getValorPorAnotacao(objeto, superClass, annotation);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;

	}

	/**
	 * @param entity
	 * @return
	 */
	public static <T> T getIdValue(Object entity) {
		return getValorPorAnotacao(entity, Id.class);
	}

	/**
	 * @param classe
	 * @return
	 */
	private static boolean isNotObject(Class<?> classe) {
		return Object.class != classe;
	}

	/**
	 * Define o valor da propriedade no objeto informado.
	 * 
	 * @param objeto
	 * @param nomePropriedade
	 * @param valor
	 */
	public static void setValorPropriedade(Object objeto, String nomePropriedade, Object valor) {
		try {
			PropertyUtils.setProperty(objeto, nomePropriedade, valor);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retorna o valor da propriedade informada, do objeto informado.
	 * 
	 * @param objeto
	 * @param nomePropriedade
	 * @return
	 */
	public static Object getValorPropriedade(Object objeto, String nomePropriedade) {
		Object valor = null;
		try {
			valor = PropertyUtils.getProperty(objeto, nomePropriedade);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		return valor;
	}

}
