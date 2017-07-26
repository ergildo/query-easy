package br.com.ecd.queryutil;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class Main {

	public static void main(String[] args) {
		String property = "cliente.pessoa.endereco.cidade.nome";

		a("venda", property, null);

	}

	private static String a(String alias, String property, Criteria criteria) {

		int indexOf = property.indexOf(".");

		if (indexOf != -1) {
			String newProperty = property.substring(indexOf + 1);

			String atualProperty = property.substring(0, indexOf);
			String newAlias = alias.concat("_").concat(atualProperty);

			String aliasPath = alias.concat(".").concat(atualProperty);

			//criteria.createAlias(aliasPath, newAlias);

			System.out.println(aliasPath.concat(" ").concat(newAlias));
			return a(newAlias, newProperty, criteria);
		}

		String concat = alias.concat(".").concat(property);
		

		System.out.println(concat);
		return concat;
	}
}
