# query-util
Este utilitário visa facilitar a criação de queries com o hibernate. Por meio de filtros pré definidos ele gera automáticamente as queries implementando, além do filtro de consulta, ordenação e paginação sob demanda. Tudo isso de forma transparente. Otimiza a criação de queries abstraindo a complexidade e reduzindo significativamente a quantidade de código. 

# Utilização
A utilização é muito simples, basta extender a classe QueryFilter e definir os campos da consulta através da anotação @QueryField e @JoinFilter.

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
### property
 Define o nome da propriedade, quando não informado nome da propriedade anotada deve ser equivalente ao nome da propriedade na entidade.
 
 ### joinType
 Tipo de Join(INNER_JOIN, LEFT_JOIN...).





