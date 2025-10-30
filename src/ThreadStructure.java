class MyThread extends Thread {
    public MyThread(ThreadGroup grup, String nume, int prioritate) {
        super(grup, nume);
        setPriority(prioritate);
    }

    public void run() {
        System.out.println("Thread: " + getName() +
                ", Grup: " + getThreadGroup().getName() +
                ", Prioritate: " + getPriority());
        try {
            Thread.sleep(300); // simulăm o activitate mică
        } catch (InterruptedException e) {}
    }
}

public class ThreadStructure {
    public static void main(String[] args) {

        // --------- Grupul principal ---------
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();

        // --- Subgrupuri ---------
        ThreadGroup g2 = new ThreadGroup(mainGroup, "G2");
        ThreadGroup g4 = new ThreadGroup(g2, "G4");
        ThreadGroup g1 = new ThreadGroup(g4, "G1");
        ThreadGroup g3 = new ThreadGroup(mainGroup, "G3");

        // --- Fire din G1 ---
        MyThread tha = new MyThread(g1, "Tha", 1);
        MyThread thb = new MyThread(g1, "Thb", 3);
        MyThread thc = new MyThread(g1, "Thc", 8);
        MyThread thd = new MyThread(g1, "Thd", 3);

        // --- Fire din G2 (în afară de G4) ---
        MyThread thA = new MyThread(g2, "ThA", 1);

        // --- Fire din G3 ---
        MyThread th1_g3 = new MyThread(g3, "Th1", 4);
        MyThread th2_g3 = new MyThread(g3, "Th2", 3);
        MyThread th3_g3 = new MyThread(g3, "Th3", 5);

        // --- Fire din Main (în afară de grupuri) ---
        MyThread th1_main = new MyThread(mainGroup, "Th1", 3);
        MyThread th2_main = new MyThread(mainGroup, "Th2", 6);

        // --- Afișare structură inițială ---
        System.out.println("\n=== Structura grupurilor (metoda list()) ===");
        mainGroup.list();

        System.out.println("\n=== Structura personalizată a grupurilor ===");
        printGroupStructure(mainGroup, 0);

        // --- Pornirea firelor ---
        tha.start();
        thb.start();
        thc.start();
        thd.start();
        thA.start();
        th1_g3.start();
        th2_g3.start();
        th3_g3.start();
        th1_main.start();
        th2_main.start();

        try {
            Thread.sleep(1000); // așteptăm finalizarea firelor
        } catch (InterruptedException e) {}

        // --- Afișare structură după pornire ---
        System.out.println("\n=== Structura grupurilor după execuție ===");
        printGroupStructure(mainGroup, 0);
    }

    // --- Metodă recursivă pentru afișarea structurii grupurilor ---
    private static void printGroupStructure(ThreadGroup group, int indent) {
        String indentStr = " ".repeat(indent * 2);
        System.out.println(indentStr + "Grup: " + group.getName() + " (maxPri=" + group.getMaxPriority() + ")");

        // Listăm firele din acest grup
        Thread[] threads = new Thread[group.activeCount()];
        int count = group.enumerate(threads, false);
        for (int i = 0; i < count; i++) {
            System.out.println(indentStr + "  └─ Thread: " + threads[i].getName() +
                    " [prioritate=" + threads[i].getPriority() + "]");
        }

        // Listăm subgrupurile recursiv
        ThreadGroup[] subgroups = new ThreadGroup[group.activeGroupCount()];
        int subgroupCount = group.enumerate(subgroups, false);
        for (int i = 0; i < subgroupCount; i++) {
            printGroupStructure(subgroups[i], indent + 1);
        }
    }
}
