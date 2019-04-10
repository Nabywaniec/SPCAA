import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Simulation {

    final Problem problem = new HeatTransfer();

    public void run(int k, double dt, int steps, Function<Double, Double> initialState) throws Exception {
        // build the tree with k levels (P1, P2, P3)
        // ...

        // Write the initial state into m_x in the vertices
        // ...

        // Plot the initial state
        double[] init = null; // ...
        plotSolution(init);

        for (int i = 0; i < steps; ++i) {
            // Run the solver
            // ...

            // Get the solution from the leaves and plot it
            double[] solution = null; // ...
            plotSolution(solution);
        }
    }

    private void plotSolution(double[] values) throws InterruptedException {
        int delay = 1000;
        ResultPrinter.printResult(values);
        ResultPrinter.plot.setFixedBounds(1, -1.5, 1.5);
        TimeUnit.MILLISECONDS.sleep(delay);
    }

    public static void main(String[] args) throws Exception {
        Simulation s = new Simulation();

        int k = 3;
        double dt = 0.01;
        int steps = 10;
        s.run(k, dt, steps, x -> Math.sin(2 * Math.PI * x));
    }
}
