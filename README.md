# query-easy
Este utilitário visa facilitar a criação de queries com o hibernate. Por meio de filtros pré definidos ele gera automáticamente as queries implementando, além do filtro de consulta, ordenação e paginação sob demanda. Tudo isso de forma transparente. Otimiza a criação de queries abstraindo a complexidade e reduzindo significativamente a quantidade de código. 

# Utilização
A utilização é muito simples, basta extender a classe QueryFilter e definir os campos da consulta através da anotação @QueryField e @JoinFilter. Uma vez definido os filtros de consulta, utilizes os métodos disponíveis na classe QueryEasy para gerar as consultas. 

## @QueryField
Define um campo de consulta, esta anotação tem as seguintes propriedades:

### property
 Define o nome da propriedade, quando não informado nome da propriedade anotada deve ser equivalente ao nome da propriedade na entidade.

### restriction
Define o tipo de restrição (EQ, NE, LIKE...). O valor padrão é EQ.

### bindField
Define o campo vinculado. Utilizado para definir dois parâmentro na mesma restrição, por exemplo: restrições do tipo BETWEEN precisa do valor inicial e final, neste caso use esta propriedade para definir qual campo armazena o valor final.

### matchMode
Define o método de comparação (START, END, ANYWHERE). O valor padrão é START.

### ignoreCase
Define se o método de comparação é case sentive ou não.

## @JoinFilter
Define um filtro de junção. Utilizado para definir filtro de propriedade de um relaciomento. A propriedade anotada deve ser outro filtro, opcionalmente, este filtro pode ou não extender a classe QueryFilter.

### property
 Define o nome da propriedade, quando não informado nome da propriedade anotada deve ser equivalente ao nome da propriedade na entidade.  
 
 ### joinType
 Tipo de Join(INNER_JOIN, LEFT_JOIN...).
 
 ## Exemplo de utilização
 ### Entidade de Pessoa
 ```
 @Entity
@Table
public class Pessoa {
	private Integer id;
	private String nome;
	private Date cadastro;
	@OneToOne
	@JoinColumn(name = "endereco")
	private Endereco endereco;
	
	//Getters and Setters...
}

 ```
 ### Entidade Endereco
 
 ``` 
 public class Endereco {
	private Integer id;
    private String descricao;
    
    //Getters and Setters...
}
 
 ```
 ### Filtro de consulta para entidade Pessoa
  ```
public class PessoaFilter extends QueryFilter {
        
	@QueryField(restriction = RestrictionType.LIKE, matchMode = MatchMode.START, ignoreCase = true)
	private String nome;

	@JoinFilter(property = "endereco", joinType = JoinType.LEFT_OUTER_JOIN)
	private EnderecoFilter endereco;

	@QueryField(property = "cadastro", restriction = RestrictionType.BETWEEN, bindField = "dataFinal")
	private Date dataInicial;

	private Date dataFinal;	
	
	//Getters and Setters...

}
 ```
Veja que a entidade Pessoa possui o relacionamento com a entidade Endereco, neste caso, utiliza-se a anotação @JoinFilter para a junção dos dois filtros. Um detalhe interessante é que o filtro EnderecoFilter não extende a classe QueryFilter, pois, neste caso é opcional. Porém, o filtro principal deve obrigadoriamente extender a classe QueryFilter. 
  
 ### Filtro da entidade Endereço
 
  ```
public class EnderecoFilter {
	@QueryField
	private String descricao;
	
	//Getters and Setters...
}
 ```
### DAO da entidade Pessoa
 ```
public class PessoaDAO {
        /**
	 *Retorna um registro único conforme os parâmentro definido no filro.
	 */
        public Pessoa consultar(PessoaFilter filter) {
		Session session = getSession();
		return QueryEasy.find(filter, Pessoa.class, session);
	}

	/**
	 * Lista os registro conforme os parâmentro definido no filro.
	 */
	public List<Pessoa> listar(PessoaFilter filter) {
		Session session = getSession();
		return QueryEasy.list(filter, Pessoa.class, session);
	}

	/**
	 * Retorna o total de registro da consulta.
	 */
	public Long listarRowCount(PessoaFilter filter) {
		Session session = getSession();
		return QueryEasy.count(filter, Pessoa.class, session);
	}	
}
  ```
  
Conforme podemos ver nos exemplos acima, uma vez definidos os filtros da consulta, basta utilizar a classe QueryEasy para geras as consultas. Simples assim! 







