import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AgendaBase {

    static class Agendamento {
        String paciente;
        String medico;
        String data;
        int hora;
        int duracao;
        String status;

        Agendamento(String paciente, String medico, String data, int hora, int duracao, String status) {
            this.paciente = paciente;
            this.medico = medico;
            this.data = data;
            this.hora = hora;
            this.duracao = duracao;
            this.status = status;
        }
    }

    private static final int DURACAO_MINIMA = 15;
    private static final int DURACAO_MAXIMA = 120;
    private static final int INICIO_EXPEDIENTE = 8;
    private static final int FIM_EXPEDIENTE = 18;

    static List<Agendamento> agendamentos = new ArrayList<>();

    /**
     * @brief Verifica se um médico possui disponibilidade em um determinado dia e
     *        horário.
     * @param data   Data do atendimento no formato AAAA-MM-DD.
     * @param hora   Hora de início do atendimento.
     * @param medico Nome do médico.
     * @return true se o horário estiver livre, false caso contrário.
     */
    public static Boolean verificarDisponibilidade(String data, int hora, String medico) {
        for (Agendamento a : agendamentos) {
            if (a.data.equals(data) && a.hora == hora && a.medico.equals(medico)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @brief Valida as regras de negócio de formato de data, duração e expediente.
     * @param data    Data do agendamento (deve ser AAAA-MM-DD).
     * @param hora    Hora de início.
     * @param duracao Duração do atendimento em minutos.
     * @throws IllegalArgumentException Se alguma regra de negócio for violada.
     */
    public static void validarAgendamento(String data, int hora, int duracao) {
        /*
         * DECISÃO DE PROJETO: Utilizamos o LocalDate.parse da biblioteca padrão do Java
         * para validar rigorosamente a existência da data (ex: anos bissextos) e o
         * formato AAAA-MM-DD, em vez de criar validações manuais com expressões
         * regulares.
         */
        try {
            LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida: " + data + ". O formato correto é AAAA-MM-DD.");
        }

        if (duracao < DURACAO_MINIMA || duracao > DURACAO_MAXIMA) {
            throw new IllegalArgumentException(
                    "Duração inválida: " + duracao + "min. Deve ser entre " + DURACAO_MINIMA + " e " + DURACAO_MAXIMA
                            + " minutos.");
        }
        if (duracao % DURACAO_MINIMA != 0) {
            throw new IllegalArgumentException("Duração inválida: " + duracao + "min. Deve ser múltiplo de 15.");
        }

        int fimEmMinutos = (hora * 60) + duracao;
        int limiteExpedienteEmMinutos = FIM_EXPEDIENTE * 60;

        if (hora < INICIO_EXPEDIENTE || fimEmMinutos > limiteExpedienteEmMinutos) {
            throw new IllegalArgumentException(
                    "O agendamento ultrapassa o expediente da clínica (" + INICIO_EXPEDIENTE + "h às " + FIM_EXPEDIENTE
                            + "h).");
        }
    }

    /**
     * @brief Adiciona um novo agendamento no sistema.
     * @param paciente Nome do paciente.
     * @param medico   Nome do médico.
     * @param data     Data no formato AAAA-MM-DD.
     * @param hora     Hora de início.
     * @param duracao  Duração em minutos.
     * @throws IllegalArgumentException Se a data, horário, duração forem inválidos
     *                                  ou houver conflito.
     */
    public static void adicionar(String paciente, String medico, String data, int hora, int duracao) {
        validarAgendamento(data, hora, duracao);

        if (!verificarDisponibilidade(data, hora, medico)) {
            throw new IllegalArgumentException(
                    "Conflito de agenda: O médico " + medico + " já tem atendimento marcado neste horário.");
        }

        agendamentos.add(new Agendamento(paciente, medico, data, hora, duracao, "MARCADO"));
        System.out.println("Atendimento marcado para " + paciente);
    }

    private static Agendamento buscar(String paciente, String data, int hora) {
        for (Agendamento a : agendamentos) {
            if (a.paciente.equals(paciente) && a.data.equals(data) && a.hora == hora) {
                return a;
            }
        }
        return null;
    }

    /**
     * @brief Remarca um atendimento existente para uma nova data e horário.
     * @param paciente Nome do paciente.
     * @param data     Data atual do agendamento (AAAA-MM-DD).
     * @param hora     Hora atual do agendamento.
     * @param novaData Nova data desejada (AAAA-MM-DD).
     * @param novaHora Novo horário desejado.
     * @throws IllegalArgumentException Se o agendamento original não for
     *                                  encontrado, as novas entradas forem
     *                                  inválidas ou houver conflito.
     */
    public static void remarcar(String paciente, String data, int hora, String novaData, int novaHora) {
        Agendamento a = buscar(paciente, data, hora);
        if (a == null) {
            throw new IllegalArgumentException("Agendamento original não encontrado para remarcação.");
        }

        validarAgendamento(novaData, novaHora, a.duracao);

        if (!verificarDisponibilidade(novaData, novaHora, a.medico)) {
            throw new IllegalArgumentException("Horário ocupado para o médico " + a.medico + " na nova data.");
        }

        a.data = novaData;
        a.hora = novaHora;
        System.out.println("Remarcado");
    }

    /**
     * @brief Desmarca (remove) um atendimento do sistema.
     * @param paciente Nome do paciente.
     * @param data     Data do agendamento (AAAA-MM-DD).
     * @param hora     Hora de início.
     * @throws IllegalArgumentException Se o agendamento não for encontrado.
     */
    public static void desmarcar(String paciente, String data, int hora) {
        Agendamento a = buscar(paciente, data, hora);
        if (a == null) {
            throw new IllegalArgumentException("Agendamento não encontrado para desmarcação.");
        }
        agendamentos.remove(a);
        System.out.println("Atendimento de " + paciente + " desmarcado.");
    }

    /**
     * @brief Lista todos os atendimentos marcados para uma data específica.
     * @param data Data para a qual os agendamentos serão listados (AAAA-MM-DD).
     */
    public static void listar(String data) {
        for (Agendamento a : agendamentos) {
            if (a.data.equals(data)) {
                System.out.println(a.hora + " " + a.paciente + " " + a.medico + " " + a.status);
            }
        }
    }

    public static void main(String[] args) {
        try {
            adicionar("Ana", "Dr. Silva", "2026-10-05", 9, 30);
            adicionar("Bruno", "Dr. Silva", "2026-10-05", 9, 30);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            adicionar("Carla", "Dra. Souza", "2026-10-05", 14, 60);
            remarcar("Ana", "2026-10-05", 9, "2026-10-06", 10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        listar("2026-10-05");
    }
}
