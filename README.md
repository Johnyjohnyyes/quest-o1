# Atividade Avaliativa - Desenvolvimento de Sistemas

Projeto acadêmico com cinco testes automatizados independentes, desenvolvidos em Java com Selenium WebDriver e JUnit 5.

## Tecnologias utilizadas

- Java 17 ou superior
- Selenium WebDriver
- JUnit 5
- Maven
- Google Chrome

## Questões

### Questão 01
Validação de mensagem de erro e classe CSS no login. [Abrir módulo](questao-01/)

### Questão 02
Espera explícita para o carregamento dinâmico e validação do resultado. [Abrir módulo](questao-02/)

### Questão 03
Fluxo E2E de login, inclusão de três produtos e remoção de um no SauceDemo. [Abrir módulo](questao-03/)

### Questão 04
Extração e comparação dos preços de laptops exibidos no Demoblaze. [Abrir módulo](questao-04/)

### Questão 05
Rastreamento dos produtos SauceDemo com preço inferior a $20.00. [Abrir módulo](questao-05/)

## Execução

Execute todas as questões a partir da raiz:

```bash
mvn test
```

Para executar uma questão individual:

```bash
cd questao-01
mvn test
```

Substitua `questao-01` por `questao-02`, `questao-03`, `questao-04` ou `questao-05` para executar outro módulo. O Chrome é iniciado em modo headless, e o Selenium Manager gerencia o WebDriver automaticamente.