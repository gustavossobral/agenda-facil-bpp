// ==========================================================
// Arquivo: AgendaBase.java
// Autor: Equipe de estagiarios - 03/02/2024
// Alterado por: Joao - 15/05/2025
// Alterado por: Marina - 09/01/2026
// ==========================================================

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

    private static final int INICIO_EXPEDIENTE = 8;
    private static final int FIM_EXPEDIENTE = 18;

    static List<Agendamento> agendamentos = new ArrayList<>();

    public static Boolean verificarDisponibilidade(String data, int hora, String medico) {
        for (Agendamento a : agendamentos) {
            if (a.data.equals(data) && a.hora == hora && a.medico.equals(medico)) {
                return false;
            }
        }
        return true;
    }

    public static Boolean validarAgendamento(int hora, int duracao) {
        return hora >= INICIO_EXPEDIENTE && hora < FIM_EXPEDIENTE && duracao > 0;
    }

    public static void adicionar(String paciente, String medico, String data, int hora, int duracao) {
        try {
            if (validarAgendamento(hora,duracao)) {
                boolean disponivel = verificarDisponibilidade(data, hora, medico);
                if (disponivel) {
                    agendamentos.add(new Agendamento(paciente, medico, data, hora, duracao, "MARCADO"));
                    System.out.println("Atendimento marcado para " + paciente);
                } else {
                    System.out.println("Horario ocupado");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível realizar o agendamento");
        }
    }

    private static Agendamento buscar(String paciente, String data, int hora) {
        for (Agendamento a : agendamentos) {
            if (a.paciente.equals(paciente) && a.data.equals(data) && a.hora == hora) {
                return a;
            }
        }
        return null;
    }

    public static void remarcar(String paciente, String data, int hora, String novaData, int novaHora) {
        Agendamento a = buscar(paciente, data, hora);
        if (a == null) {
            System.out.println("Agendamento nao encontrado");
            return;
        }
        if (verificarDisponibilidade(novaData, novaHora, a.medico)) {
            a.data = novaData;
            a.hora = novaHora;
            System.out.println("Remarcado");
        } else {
            System.out.println("Horario ocupado");
        }
    }

     public static void desmarcar(String paciente, String data, int hora) {
         Agendamento a = buscar(paciente, data, hora);
         if (a == null) {
             System.out.println("Agendamento nao encontrado");
         } else {
             agendamentos.remove(a);
         }
     }


    public static void listar(String data) {
        for (Agendamento a : agendamentos) {
            if (a.data.equals(data)) {
                System.out.println(a.hora + " " + a.paciente + " " + a.medico + " " + a.status);
            }
        }
    }

    public static void main(String[] args) {
        adicionar("Ana", "Dr. Silva", "2026-10-05", 9, 30);
        adicionar("Bruno", "Dr. Silva", "2026-10-05", 9, 30);
        adicionar("Carla", "Dra. Souza", "2026-10-05", 14, 60);
        remarcar("Ana", "2026-10-05", 9, "2026-10-06", 10);
        listar("2026-10-05");
    }
}
