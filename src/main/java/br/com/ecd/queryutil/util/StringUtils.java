/**
 * 
 */
package br.com.ecd.queryutil.util;

/**
 * @author ergildo.cdias
 *
 */
public class StringUtils {

	/**
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

}
