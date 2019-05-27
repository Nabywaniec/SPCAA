import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Simulation {

    //    final Problem problem = new HeatTransfer();
    final Problem problem = new Exercise4();
//        final Problem problem = new Exercise2CN();
    public static double[] solution;

    public void run(int levels, double dt, int steps, Function<Double, Double> initialState) throws Exception {
        Vertex S = new Vertex(null, null, null, "S");
        double h = 1 / Math.pow(2.0, levels - 1);
        try {
            //make a tree
            ArrayList<ArrayList<Production>> productionList = new ArrayList<>();

            CyclicBarrier barrier = new CyclicBarrier(1 + 1); //P1 inicjalization
            ArrayList<Production> firstLevel = new ArrayList<>();
            firstLevel.add(new P1(S, barrier));
            productionList.add(firstLevel);
            firstLevel.get(0).start();
            barrier.await();

            ArrayList<Production> lastLevel = firstLevel;
            ArrayList<Production> newLevel = new ArrayList<>();
            for (int i = 1; i < levels - 1; i++) { //P2 inicjalization
                barrier = new CyclicBarrier(lastLevel.size() * 2 + 1);
                for (Production parent : lastLevel) {
                    newLevel.add(new P2(parent.m_vertex.m_left, barrier));
                    newLevel.add(new P2(parent.m_vertex.m_right, barrier));
                }
                for (Production production : newLevel) {
                    production.start();
                }
                barrier.await();
                productionList.add(newLevel);
                lastLevel = newLevel;
                newLevel = new ArrayList<>();
            }

            barrier = new CyclicBarrier(lastLevel.size() * 2 + 1); //P3 inicjalization
            for (Production parent : lastLevel) {
                newLevel.add(new P3(parent.m_vertex.m_left, barrier));
                newLevel.add(new P3(parent.m_vertex.m_right, barrier));
            }
            for (Production production : newLevel) {
                production.start();
            }
            productionList.add(newLevel);
            barrier.await();

            //initial states
            for (int i = 0; i < Math.pow(2.0, levels - 1); i++) {
                newLevel.get(i).m_vertex.m_x[1] = initialState.apply(i / Math.pow(2.0, levels - 1));
                newLevel.get(i).m_vertex.m_x[2] = initialState.apply((i + 1) / Math.pow(2.0, levels - 1));
            }


            // Plot the initial state
            int n = newLevel.size();
            double[] init = new double[n + 1];

            for (int i = 0; i < n; i++) {
                init[i] = newLevel.get(i).m_vertex.m_x[1];
            }
            init[n] = newLevel.get(newLevel.size() - 1).m_vertex.m_x[2];
            plotSolution(init);

            for (int j = 0; j < steps; ++j) {
                // Run the solver
                ArrayList<Production> currentPLevel = newLevel; //A1-AN inicjalization
                ArrayList<Production> temporaryLevel = new ArrayList<>();
                barrier = new CyclicBarrier(currentPLevel.size() + 1);
                temporaryLevel.add(problem.makeA1(currentPLevel.get(0).m_vertex, 1 / Math.pow(2.0, levels - 1), dt, j * dt, barrier)); //A1
                for (int i = 1; i < currentPLevel.size() - 1; i++) { //A
                    Production production = currentPLevel.get(i);
                    temporaryLevel.add(problem.makeA(production.m_vertex, 1 / Math.pow(2.0, levels - 1), dt, j * dt, barrier));
                }
                temporaryLevel.add(problem.makeAN(currentPLevel.get(currentPLevel.size() - 1).m_vertex, 1 / Math.pow(2.0, levels - 1), dt, j * dt, barrier)); //AN
                for (Production production : temporaryLevel) {
                    production.start();
                }
                barrier.await();

                for (int i = levels - 1; i > 1; i--) { //A2 E2 inicjalization
                    currentPLevel = productionList.get(i - 1);
                    temporaryLevel = new ArrayList<>(); //A2
                    barrier = new CyclicBarrier(currentPLevel.size() + 1);
                    for (Production production : currentPLevel) {
                        temporaryLevel.add(new A2(production.m_vertex, barrier));
                    }
                    for (Production production : temporaryLevel) {
                        production.start();
                    }
                    barrier.await();
                    temporaryLevel = new ArrayList<>(); //E2
                    barrier = new CyclicBarrier(currentPLevel.size() + 1);
                    for (Production production : currentPLevel) {
                        temporaryLevel.add(new E2(production.m_vertex, barrier));
                    }
                    for (Production production : temporaryLevel) {
                        production.start();
                    }
                    barrier.await();
                }

                barrier = new CyclicBarrier(1 + 1); //Aroot inicjalization
                Aroot aroot = new Aroot(productionList.get(0).get(0).m_vertex, barrier);
                aroot.start();
                barrier.await();

                barrier = new CyclicBarrier(1 + 1); //Eroot inicjalization
                Eroot eroot = new Eroot(productionList.get(0).get(0).m_vertex, barrier);
                eroot.start();
                barrier.await();

                if (levels > 2) {
                    for (int i = 1; i < levels - 1; i++) { //BS inicjalization
                        currentPLevel = productionList.get(i - 1);
                        temporaryLevel = new ArrayList<>();
                        barrier = new CyclicBarrier(currentPLevel.size() + 1);
                        for (Production production : currentPLevel) {
                            temporaryLevel.add(new BS(production.m_vertex, barrier));
                        }
                        for (Production production : temporaryLevel) {
                            production.start();
                        }
                        barrier.await();
                    }

                    currentPLevel = productionList.get(levels - 2); //BSA
                    temporaryLevel = new ArrayList<>();
                    barrier = new CyclicBarrier(currentPLevel.size() + 1);
                    for (Production production : currentPLevel) {
                        temporaryLevel.add(new BSA(production.m_vertex, barrier));
                    }
                    for (Production production : temporaryLevel) {
                        production.start();
                    }
                    barrier.await();
                } else {
                    barrier = new CyclicBarrier(1 + 1); //BSA inicjalization
                    BSA bsa = new BSA(productionList.get(0).get(0).m_vertex, barrier);
                    bsa.start();
                    barrier.await();
                }

                // Get the solution from the leaves and plot it
                solution = new double[n + 1];
                for (int i = 0; i < n; i++) {
                    solution[i] = newLevel.get(i).m_vertex.m_x[1];
                }
                solution[n] = newLevel.get(newLevel.size() - 1).m_vertex.m_x[2];
                plotSolution(solution);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private void plotSolution(double[] values) throws InterruptedException {
        int delay = 1000;
        ResultPrinter.printResult(values);
        ResultPrinter.plot.setFixedBounds(1, 0, 2);
        TimeUnit.MILLISECONDS.sleep(delay);
    }

    public static void main(String[] args) throws Exception {
        Simulation s = new Simulation();

        int k = 5;
        double dt = 0.01;
        int steps =100;
        s.run(k, dt, steps, x -> x);
    }
}
