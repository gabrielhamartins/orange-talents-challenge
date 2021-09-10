# Construindo uma API REST para cadastro de usuários e carros.

## Introdução

Hoje vamos fazer uma API que cadastra usuários e carros num banco de dados através de
Requests HTTP, além de atribuir um carro para um determinado usuário e retornar os
cadastros. Um dos nossos desafios vai ser consumir a API da FIPE para resgatar o valor do carro
na tabela de forma automatizada. Também vamos criar um sistema de rodízio baseado no
último número do ano de lançamento do veículo. Bora ver?

## Tecnologias que vão nos auxiliar no processo

Para este sistema, iremos usar Java com Spring + Hibernate como stacks de tecnologia. O
Spring Framework nos proporciona diversas ferramentas de integração e microsservices, no
entanto, pode ser meio trabalhoso configurar o projeto. Pensando nisso, foi criado o Spring
Boot, como uma convenção de configuração que facilita a criação do projeto. Para o nosso
sistema, iremos utilizar o Spring Initializr, que cria e configura automaticamente um projeto
Spring, com as seguintes dependências (Ao longo desse post serão descritos o funcionamento
delas): Spring Web, Spring BootDevTools, Lombok, OpenFeign, Spring Data JPA, PostgreSQL
Driver.

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/1-springInitializer.PNG)

```
Figura 1 - Tela de configuração do Spring Initializr
```
## Importando o projeto e configurando o banco de dados

Após baixar o zip gerado pelo Spring Initializr, é necessário apenas extrair o projeto e importar
na IDE (no caso, está sendo utilizado o IntelliJ). A primeira configuração que iremos fazer, é dar
a permissão necessária para que o Java consiga acessar e editar o banco de dados, que neste
caso foi usado o PostgreSQL (Por isso a dependência do PostgreSQL Driver no projeto).


Assim, no arquivo application.properties, teremos as seguintes configurações:
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/2-applicationProperties.PNG)
```
Figura 2 - Configuração do PostgreSQL no application.properties
```
Ou seja, foi criado localmente um banco de dados localizado na porta 5432 com o nome
“orange-talents-challenge” e dada a permissão para o Java acessar e editar ele dentro deste
projeto.

## Criando o modelo de dados do projeto

Como se trata de um sistema para cadastrar usuários e carros, vamos precisar criar esses
modelos no banco de dados. Mas, como integramos o projeto com o banco, podemos fazer
isso diretamente no código:


### 1 – Modelo para o usuário
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/3-userModel.PNG)
```
Figura 3 – Modelo de dado para o Usuário
```
Nesta classe temos os seguintes atributos principais: Nome, Email, CPF e Data de Nascimento.
A anotação @Data é da dependência Lombok que adicionamos no projeto. Essa anotação nos
poupa de escrever getters e setters na classe, além de construtores (Propriedades que são
necessárias em modelos de dados). A anotação @Entity faz parte do Hibernate. Com essa
anotação ele comunica para o banco de dados que, caso não exista, é necessário criar uma
tabela com esse modelo de dados. Assim, não precisamos ficar escrevendo comandos
específicos (como CREATE TABLE, SELECT) diretamente no banco ou no meio do código. As
outras anotações são apenas orientações de como o banco de dados irá tratar os dados (como
autoincrementar 1 no campo id) e suas relações com outros modelos (O @ManyToMany diz
para o banco que um usuário pode ter vários carros e um modelo de carro pode pertencer a
vários usuários).


### 2 – Modelo para o carro
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/4-carModel.PNG)
```
Figura 4 - Modelo de dado para o Carro
```
A criação do modelo de dados para o carro é análoga ao que foi explicado para o usuário.


##### 3 – Modelo para o registro
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/5-RegisterModel.PNG)
```
Figura 5 - Modelo de dados para o registro
```
Esta classe é responsável por guardar os carros que são registrados para um usuário,
atribuindo um id para a relação “usuário tem carro”, facilitando assim algumas operações.

## Criando os repositórios

Como estamos trabalhando com o Hibernate para integrar o banco de dados, precisamos criar
as interfaces que irão fazer o controle dos dados. Dessa forma, não precisaremos usar
manualmente os comandos de INSERT, por exemplo, no banco de dados. Basta fazer com que
a interface estenda a JpaRepository e tudo se resume em um comando:
modelRepository.save(Model). Ou seja, caso precisemos salvar a instância de um carro no
banco de dados, basta fazermos um carRepository.save(car).

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/6-carRepository.PNG)
```
Figura 6 - Interface de Repositório para o Carro
```
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/7-registerRepository.PNG)
```
Figura 7 - Interface de Repositório para o Registro
```
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/8-userRepository.PNG)
```
Figura 8 - Interface de repositório para o Usuário
```
## Criando os controladores

A ideia da API REST é que, caso seja feita uma requisição do tipo “POST” num endereço
determinado, passando um JSON como argumento, o sistema receba essa requisição e consiga
passar esses dados para o banco de dados.
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/9-userJson.PNG)
```
Figura 9 - Exemplo de JSON para cadastro de Usuário
```
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/10-dbExample.PNG)
```
Figura 10 - Exemplo abstrato de conversão de JSON para tabela no BD
```
Dessa forma, precisamos criar alguns controladores que irão receber estes métodos (GET –
Buscar dados, POST – Cadastrar dados, PUT – Atualizar dados, DELETE – Deletar dados).

As classes que servirão como controladores precisam ter a anotação do Spring Web
“@RestController”, para que o framework entenda que se trata de um. Além disso, para este
projeto, adicionei o “@RequestMapping(value = ‘/api’). Assim, o endereço padrão para
receber requisições nesta API será o [http://localhost:8080/api,](http://localhost:8080/api,) por se tratar de um projeto
local.


### 1 – Exemplo de POST no usuário.
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/11-userPost.PNG)
```
Figura 11 - Exemplo de método POST para Usuário
```
Graças ao Spring Web, isso é tudo que precisa ter no método para salvar um dado que veio
como JSON na requisição. No exemplo do usuário, o @PostMapping(Value = “/user”) faz com
que toda requisição do tipo POST que seja feita no endereço [http://localhost:8080/api/user](http://localhost:8080/api/user)
caia neste método. O próprio framework irá transformar aquele texto JSON num objeto Java e
basta chamar o método save do repositório para que o usuário seja guardado no banco de
dados.

### 2 – Exemplo de GET no usuário.
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/12-userGet.PNG)
```
Figura 12 - Exemplo do método GET para o Usuário
```
A ideia para o método GET é a mesma do POST, com a diferença que o sistema irá buscar um
determinado dado no banco de dados, transformar num objeto Java e devolver para o cliente
que fez a requisição um texto no formato JSON que foi exemplificado.

Note que os retornos dos métodos GET passam pela função checkRotation. É ela a responsável
por verificar se o carro que foi devolvido para o cliente está num dia de rodízio. Vamos
explorar essa função.

## Criando o método para verificar o dia de rodízio

O dia de rodízio do carro é uma variável que está atrelada ao último digito do ano de
lançamento do carro, desta forma, não é necessário que o usuário insira essa informação
quando for adicionar um carro. Para isto foi criada esta função:

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/13-carGetRotationDay.PNG)
```
Figura 13 - Função que adiciona o dia de rodízio do carro
```
Além disso, pode ser que seja interessante para o usuário saber apenas se o carro está em dia
de rodízio ou não. Para poupá-lo de fazer a comparação manualmente, criamos o atributo
isRotating no modelo do carro que retorna Verdadeiro ou Falso, dependendo do dia atual.
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/14-checkIsRotating.PNG)
```
Figura 14 - Trecho de código que retorna ao cliente a condição de rodízio do carro
```
## Consumindo a API da FIPE para obter o valor de tabela de um carro.

De acordo com a documentação da API, nós temos um tipo de retorno padrão para listagem
dos objetos (marca, modelo e ano) com “nome” e “codigo”, e caso precisemos de um carro
específico, o retorno muda. No entanto, como precisamos apenas do valor do carro, não
vamos pegar todos os dados disponíveis. Uma das vantagens do framework que iremos usar
para consumir essa API, o Open Feign, é que nós podemos resumir o retorno na seguinte
classe:

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/15-fipeResponseModel.PNG)
```
Figura 15 - Modelo de dados para retorno da API da FIPE
```
Assim, caso o framework verifique que há um atributo do tipo “nome”, por exemplo, na
resposta JSON da API, ela vai popular este atributo no objeto, caso contrário, ficará como
“null”. Para fazermos as requisições, tudo que precisamos novamente é de uma interface com
a anotação @FeignClient e a URL para a qual desejamos fazer as requisições:
![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/16-fipeClient.PNG)
```
Figura 16 - Interface do FeignClient que consome a API da FIPE
```
Com isso, podemos adicionar o método getCarValue dentro do POST de um veículo antes de
ele ser armazenado no banco de dados. Vamos ver como esse método funciona:

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/17-carGetValue.PNG)
```
Figura 17 - Método que busca e insere o valor do carro
```
Para conseguirmos o valor da tabela FIPE pela API, precisamos acessar o seguinte endereço:
https://parallelum.com.br/fipe/api/v1/carros/marcas/{idDaMarca}/modelos/{idDoModelo}/an
os/idDoAno

No entanto, nós não armazenamos os Ids dos atributos. Assim, esse método compara o nome
dos que vão ser cadastrados com os nomes existentes na API até encontrar os respectivos Ids e
no final faz a requisição completa que retorna o valor.

Dessa forma, temos a API 100% funcional e consumindo a API da FIPE corretamente.

## Demonstrando o funcionamento da API

Iremos utilizar o software Postman para efetuar as requisições na nossa api. Primeiramente,
iremos fazer 5 POSTs no endereço [http://localhost:8080/api/user](http://localhost:8080/api/user) seguindo o seguinte modelo:

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/18-jsonUser.PNG)
```
Figura 18 - Exemplo de requisição POST para Usuário
```

Analogamente no endereço [http://localhost:8080/api/car,](http://localhost:8080/api/car,) seguindo o seguinte modelo:

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/19-jsonCar.PNG)
```
Figura 19 - Exemplo de requisição POST para Carro
```
Para atribuir um carro a um usuário seguiremos o seguinte modelo:

[http://localhost:8080/api/register?userId=1&carId=](http://localhost:8080/api/register?userId=1&carId=)

Após atribuirmos os carros com id 1 e 2 para o usuário com id 1, veja como se comporta o GET
no endereço [http://localhost:8080/api/user/](http://localhost:8080/api/user/)

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/20-getUserId.PNG)
```
Figura 20 - Exemplo de GET no usuário com id = 1
```
Repare que é retornada uma lista com os dois carros que foram atribuídos a este usuário
específico e que os carros já contam com o dia do rodízio, o valor e a condição de rodízio. No
caso do carro com id = 1, o atributo “rotating” retornou Verdadeiro pois a data do sistema
estava setada para uma segunda-feira.

Essa API está disponível em nuvem pelo Heroku usando o Swagger-UI para termos uma interface de navegação amigável.

![](https://github.com/gahamartins/Desafio-Orange-Talents/blob/main/images/21-heroku.PNG)
```
Figura 21 - Tela inicial da API no Heroku usando Swagger
```

**Código-Fonte** : https://github.com/gahamartins/desafio-orange-talents

**Heroku** : https://apirest-orangetalents.herokuapp.com/swagger-ui/

**Gabriel H. A. Martins**

https://www.linkedin.com/in/gabrielhamartins


