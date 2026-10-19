# RELATÓRIO — AgendaFácil

## Integrantes

| Nome                             | Matrícula   |
| -------------------------------- | ----------- |
| GUSTAVO HENRIQUE DE SOUSA SOBRAL | 20250037249 |
| LUIZ RENAN DA SILVA PONTES       | 20250031601 |

**Linguagem escolhida:** Java

## Etapa 1 — Diagnóstico

Diagnóstico do `AgendaBase.java` original, organizado pelas cinco categorias do
checklist da Aula 2. As linhas citadas se referem ao código-base fornecido.

| Categoria                                | Anomalia encontrada (e onde)                                                                                                                            | Por que é um problema                                                                                                           |
| ---------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| **1. Adequação e lógica**                | 1.1 — Três `if` aninhados em `add()` (linhas 36–38)                                                                                                     | Aninhamento desnecessário: as três condições cabem em uma única verificação. A solução é mais complexa do que o problema exige. |
| **2. Manutenibilidade**                  | 2.1 — Nomes de métodos `add()`, `rem()` e `lst()` (linhas 33, 58 e 85)                                                                                  | Não revelam a intenção: `rem()` remarca, mas pode ser confundido com "remover".                                                 |
| **2. Manutenibilidade**                  | 2.2 — Nomes de classe, atributos e variáveis: `A`, `paciente`, `medico`, `data`, `hora`, `duracao`, `st`, `ag`, `nd`, `nh`, `ok` (linhas 13–19, 31, 58) | Nomes sem significado obrigam o leitor a deduzir o papel de cada um pelo uso.                                                   |
| **2. Manutenibilidade**                  | 2.3 — `add()` com mais de uma responsabilidade (linhas 33–56)                                                                                           | Além de adicionar, a função valida o horário, verifica conflito e imprime mensagens.                                            |
| **2. Manutenibilidade**                  | 2.4 — Violação do DRY nas linhas 40–44 e 63–67                                                                                                          | A verificação de conflito de horário está duplicada; uma mudança na regra precisa ser feita em dois lugares.                    |
| **3. Organização e estética**            | 3.1 — Método `desmarcar()` comentado (linhas 79–81) e `TODO` vencido (linha 83)                                                                         | Código morto: polui o arquivo e não diz se é algo planejado ou abandonado.                                                      |
| **3. Organização e estética**            | 3.2 — Comentários que narram o código no início de cada método (linhas 34, 59 e 86)                                                                     | Poderiam ser substituídos por um nome claro de método. O da linha 86 ainda está errado: `lst()` lista por dia, não por mês.     |
| **4. Programação defensiva e segurança** | 4.1 — `catch (Exception e)` vazio (linhas 54–55)                                                                                                        | Silencia qualquer erro, que passa despercebido.                                                                                 |
| **4. Programação defensiva e segurança** | 4.2 — Data (`data`, linha 16) armazenada como `String` livre                                                                                            | Nenhuma verificação do formato AAAA-MM-DD; uma data inválida é aceita. O tipo adequado em Java é `java.time.LocalDate`.         |
| **5. Desempenho e recursos**             | 5.1 — Dois `for` aninhados em `rem()` (linhas 60 e 63)                                                                                                  | Laços aninhados desnecessários (custo quadrático), e nenhum dos laços para (`break`) ao encontrar o que procura.                |

## Decisões de Projeto

### **Mudança de Comportamento para Entradas Hostis (Programação Defensiva)**

O sistema deixou de engolir falhas silenciosamente. No código-base, funções como
`add` imprimiam um erro genérico ("Horario ocupado") ou simplesmente não faziam
nada (bloco `catch` vazio). Agora, todas as funções públicas exigem pré-condições
rígidas na forma de cláusulas de guarda. Caso a data esteja em formato incorreto,
a duração seja inválida, ou o horário conflite ou escape do expediente (8h às
18h), o programa aborta a operação imediatamente lançando uma
`IllegalArgumentException` com uma mensagem clara sobre qual valor causou a
interrupção. O `main` foi encapsulado em blocos `try/catch` para preservar a
saída do caminho feliz, mas ativando a resposta defensiva para os conflitos.

### **Validação de Datas**

Para garantir o formato `AAAA-MM-DD` com precisão de calendário (reconhecendo
anos bissextos e impedindo dias inválidos como 32), optou-se por utilizar o
método nativo `LocalDate.parse` da biblioteca padrão do Java. Caso ocorra uma
`DateTimeParseException`, ela é convertida para uma `IllegalArgumentException`
detalhando a falha no formato, sem precisar de complexas validações manuais
via string ou regex.

### **Cálculo do Fim do Expediente**

Em vez de permitir que o sistema avaliasse apenas o horário de início em horas
inteiras, criamos a conversão sistemática para minutos na validação do expediente.
O horário de início é multiplicado por 60 e somado à duração solicitada. Isso
garante que um agendamento não termine após o fechamento da clínica às 18:00h,
tapando uma brecha lógica do sistema anterior.
