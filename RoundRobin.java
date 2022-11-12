import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

class RoundRobin {

        private  class Proceso {
            String pcb;
            int TRC;
            String estado;
            int posic;

            int numCom;
            int ini;


            int TEP;
            public Proceso() {
                numCom = 1;
                TEP = 0;

            }

            public int getTEP() {
                return TEP;
            }

            public void setTEP(int TEP) {
                this.TEP = TEP;
            }

            public void AddNumcom (){
                numCom+=1;
            }
        }

        int n;
        int quantum;
        String archivo;
        Proceso[] procesos;

        int TEP = 0;
        ArrayList<String> buffer;

        int t = 0;

        public RoundRobin (int n, int quantum, String archivo) {
            this.n = n;
            this.quantum = quantum;
            this.archivo = archivo;
            this.procesos = new Proceso[n];
            this.buffer = new ArrayList<>();

            Proceso p;
            for (int i = 0; i < this.n; i++) {
                p = new Proceso();
                p.pcb = "P" + (i + 1);
                //p.id = "0" + Integer.toString(i+1);
                //p.TRC = (int) (Math.random() * 49) +1;
                p.TRC = (int) (Math.random() * 49) +1;
                p.estado = "N";
//                p.posic = i+1;
                procesos[i] = p;
            }
            titulo();
            colaListo();
            procesarCola();
            System.out.println("T.E.P.= " + CalcularTEP());
            System.out.println(this.archivo + " guardado correctamente");

        }

        public static void main(String[] args) {
          new RoundRobin(Integer.parseInt(args[0]), Integer.parseInt(args[1]),"jatest.txt");
           //new RoundRobin(2, 1,"jatest.txt");
        }

        private void titulo() {
            buffer.add("Programa de la Segunda Unidad: \n");
            buffer.add("\nAlgoritmo de planificación Round Robin\n");
            buffer.add("N(Nuevo) L(Listo) E(Ejecución) T(Terminado)\n");
            buffer.add("Cantidad de procesos: " + this.n + "\n");
            buffer.add("Quantum: " + this.quantum + "\n");
            guardar();
        }

        private void guardar() {
            buffer.add("\nProceso       ");
            for(Proceso p : procesos) {
               buffer.add(" | " + p.pcb);
            }

            buffer.add("\nT.R.C.        ");
            for(Proceso p : procesos) {
                buffer.add(" | " + p.TRC);
            }
            buffer.add("\nEstado        ");
            for(Proceso p : procesos) {
                buffer.add(" | " + p.estado);
            }
//            buffer.add("\nPosic");
//            for(Proceso p : procesos) {
//                buffer.add(" | " + p.posic);
//            }
            buffer.add("\nConmutaciones:");
            for(Proceso p : procesos) {
                buffer.add(" | " + p.numCom);
            }
            buffer.add("\n");
            guardarArchivo();
        }

        private void colaListo() {
            for (int i = 0; i < this.n; i++) {
                this.procesos[i].estado = "L";
            }
            guardar();
        }

        private void procesarCola() {
            while (procesosNoTerminados()) {
                for (Proceso p : procesos) {
                    trabajarProceso(p);
                }
            }
            reposicionarCola();
            guardar();
        }

        private void reposicionarCola() {
            int posicion = 0;
            for (Proceso p : procesos) {
                if (p.estado.equals("T")) {
                    p.posic = 0;
                } else {
                    posicion += 1;
                    p.posic = posicion;
                }
            }
        }

        private boolean procesosNoTerminados() {
            for (Proceso p : procesos) {
                if (!"T".equals(p.estado)) {
                    return true;
                }
            }
            return false;
        }

        private void trabajarProceso(Proceso p) {

            if (p.TRC > quantum) {
                p.ini = t;
                t += quantum;
                p.TRC -= quantum;
                p.estado = "E";
                p.AddNumcom();
                guardar();
                p.estado = (p.TRC == 0) ? "T" : "L";
            } else if (!"T".equals(p.estado)) {
                p.ini = t;
                t+= p.TRC;
                p.TRC = 0;
                p.estado = "E";
                reposicionarCola();
                guardar();
                p.estado = "T";
            }

             p.TEP = (p.numCom > 1) ? p.ini-quantum : p.ini;

        }

        public double CalcularTEP () {
   //            int s = 0;
//
//            for (Proceso p : procesos) {
//                s+= p.TEP;
//                }
//            return s/n;

            return Arrays.stream(procesos).mapToInt(p -> p.TEP).
                    average().orElseThrow();
        }

        private void guardarArchivo() {
            try {
                String buf = String.join("", buffer);
                System.out.println(buf);
                FileWriter fw = new FileWriter(this.archivo, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(buf);
                pw.close();
                buffer.clear();
            } catch (Exception e) {
                System.out.println("Error al guardar " + this.archivo);
            }
        }
}
