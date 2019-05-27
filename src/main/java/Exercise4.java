import java.util.concurrent.CyclicBarrier;

public class Exercise4 implements Problem {

    static private double pi2 = Math.pow(Math.PI, 2.);

    int i = 1;

    @Override
    public Production makeA1(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {

                double u1 = v.m_x[0];
                double u2 = 0.75 * v.m_x[0] + 0.25 * v.m_x[1];
                double u3 = 0.75 * v.m_x[1] + 0.25 * v.m_x[2];
                double u4 = v.m_x[2];

                double dx2 = Math.pow(h, 2.);
                v.m_a = new double[][]{
                        {0., 0., 0.},
                        {0., 1., 0.},
                        {0., -1. / (2 * dx2), 1. / (2 * dt) + 1. / (2 * dx2) - pi2 / 4}};

                v.m_b = new double[]{
                        0.,
                        0.,
                        u3 / (2 * dt) + u2 / (4 * dx2) - u2 / (2 * dx2) + u4 / (4 * dx2) + pi2 * u3 / 4 - pi2 * h * 1

                };
                return v;
            }
        };
    }

    @Override
    public Production makeA(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {
                double dx2 = Math.pow(h, 2.);
                double xi = h * i,
                        xi1 = xi + h;

                double ui_1
                double ui = 0.75 * v.m_x[0] + 0.25 * v.m_x[1];
                double  = 0.75 * v.m_x[1] + 0.25 * v.m_x[2];
                double u4 = v.m_x[2];

                v.m_a = new double[][]{
                        {0., 0., 0.},
                        {0., 1. / (2 * dt) + 1. / (2 * dx2) - pi2 / 4, -1. / (2 * dx2)},
                        {0., -1. / (2 * dx2), 1. / (2 * dt) + 1. / (2 * dx2) - pi2 / 4}};

                v.m_b = new double[]{
                        0.,
                        ui / (2 * dt) + Simulation.solution[i-1] / (4 * dx2) - ui / (2 * dx2) +  / (4 * dx2) + pi2 * ui / 4 - pi2 * h * xi,
                         / (2 * dt) + ui / (4 * dx2) -  / (2 * dx2) + u4 / (4 * dx2) + pi2 *  / 4 - pi2 * h * xi1
                };
                i++;
                return v;
            }
        };
    }

    @Override
    public Production makeAN(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {
                double dx2 = Math.pow(h, 2.);

                double u1 = v.m_x[0];
                double u2 = 0.75 * v.m_x[0] + 0.25 * v.m_x[1];
                double u3 = 0.75 * v.m_x[1] + 0.25 * v.m_x[2];
                double u4 = v.m_x[2];
                v.m_a = new double[][]{
                        {0., 0., 0.},
                        {0., 1. / (2 * dt) + 1. / (2 * dx2) - pi2 / 4, -1. / (2 * dx2)},
                        {0., -1. / h, 1. / h}};

                double xi = h * i,
                        xi1 = xi + h;

                v.m_b = new double[]{
                        0.,
                        u2 / (2 * dt) + u1 / (4 * dx2) - u2 / (2 * dx2) + u3 / (4 * dx2) + pi2 * u2 / 4 - pi2 * h * xi,
                        1 - Math.PI
                };
                i = 1;
                return v;
            }
        };
    }
}
