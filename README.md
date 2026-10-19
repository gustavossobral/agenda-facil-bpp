# AgendaFácil - Versão Refatorada

O AgendaFácil é o sistema de agendamento de atendimentos de uma clínica. Este
projeto consiste na refatoração do código legado original, visando aplicar boas
práticas de organização, modularização, documentação e programação defensiva.
Os dados vivem em memória e não há dependência de banco de dados ou interface gráfica.

## Pré-requisitos

- Java Development Kit (JDK) 8 ou superior instalado.

### Como compilar e executar

Abra o terminal no diretório onde o arquivo AgendaBase.java está localizado e
execute os seguintes comandos:

1. **Compilar:**

```bash
javac AgendaBase.java
```

2. **Executar:**

```bash
java AgendaBase
```

## Como gerar a documentação (API)

O projeto utiliza o padrão Doxygen/Javadoc para os contratos das funções públicas.
Para gerar a página HTML com a documentação da API, execute o comando abaixo no
terminal:

```bash
javadoc -d docs AgendaBase.java
```

A documentação será gerada dentro da pasta docs/. Abra o arquivo docs/index.html
no seu navegador para visualizá-la.

## Integrantes

- Gustavo Henrique de Sousa Sobral - 20250037249
- Luiz Renan da Silva Pontes - 20250031601
