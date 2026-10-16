// ==========================================================
// Arquivo: AgendaBase.java
// Autor: Equipe de estagiarios - 03/02/2024
// Alterado por: Joao - 15/05/2025
// Alterado por: Marina - 09/01/2026
// ==========================================================

import java.util.ArrayList;
import java.util.List;

public class AgendaBase {

    static class A {
        String p;
        String pr;
        String d;
        int h;
        int dur;
        String st;

        A(String p, String pr, String d, int h, int dur, String st) {
            this.p = p;
            this.pr = pr;
            this.d = d;
            this.h = h;
            this.dur = dur;
            this.st = st;
        }
    }

    static List<A> ag = new ArrayList<>();

    public static void add(String p, String pr, String d, int h, int dur) {
        // adiciona na lista
        try {
            if (h >= 8) {
                if (h < 18) {
                    if (dur > 0) {
                        boolean ok = true;
                        for (A a : ag) {
                            if (a.d.equals(d) && a.h == h && a.pr.equals(pr)) {
                                ok = false;
                            }
                        }
                        if (ok) {
                            ag.add(new A(p, pr, d, h, dur, "MARCADO"));
                            System.out.println("Atendimento marcado para " + p);
                        } else {
                            System.out.println("Horario ocupado");
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void rem(String p, String d, int h, String nd, int nh) {
        // remarca o atendimento
        for (A a : ag) {
            if (a.p.equals(p) && a.d.equals(d) && a.h == h) {
                boolean ok = true;
                for (A b : ag) {
                    if (b.d.equals(nd) && b.h == nh && b.pr.equals(a.pr)) {
                        ok = false;
                    }
                }
                if (ok) {
                    a.d = nd;
                    a.h = nh;
                    System.out.println("Remarcado");
                } else {
                    System.out.println("Horario ocupado");
                }
            }
        }
    }

    // public static void desmarcar(String p, String d, int h) {
    //     ag.remove(...);
    // }

    // TODO: rever isso depois (Joao, 2025)

    public static void lst(String d) {
        // lista os atendimentos do mes
        for (A a : ag) {
            if (a.d.equals(d)) {
                System.out.println(a.h + " " + a.p + " " + a.pr + " " + a.st);
            }
        }
    }

    public static void main(String[] args) {
        add("Ana", "Dr. Silva", "2026-10-05", 9, 30);
        add("Bruno", "Dr. Silva", "2026-10-05", 9, 30);
        add("Carla", "Dra. Souza", "2026-10-05", 14, 60);
        rem("Ana", "2026-10-05", 9, "2026-10-06", 10);
        lst("2026-10-05");
    }
}
