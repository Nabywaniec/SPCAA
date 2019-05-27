import java.util.concurrent.CyclicBarrier;

public class Exercise3 implements Problem{

    static private double pi2 = Math.pow(Math.PI, 2.);

    int i = 1;

    @Override
    public Production makeA1(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t ,barrier) {
            @Override
            Vertex apply(Vertex v) {
                double dx2 = Math.pow(h, 2.);
                v.m_a = new double[][]{
                        {0. ,0.       , 0.},
                        {0., 1.       , 0.},
                        {0., -1./(dx2), 1./(2*dt) + 1./dx2 - pi2/2.}};

                v.m_b = new double[] {
                        0.,
                        0.,
                        v.m_x[2]/(2.*dt) + pi2*1*h/2.

                };
                return v;
            }
        };
    }

    @Override
    public Production makeA(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t ,barrier) {
            @Override
            Vertex apply(Vertex v) {
                double dx2 = Math.pow(h, 2.);
                double xi = h * i,
                        xi1 = xi + h;

                v.m_a = new double[][]{
                        {0. ,0.                         , 0.},
                        {0., 1./(2*dt) + 1./dx2 - pi2/2., -1./(dx2)},
                        {0., -1./(dx2)                  , 1./(2.*dt) + 1./dx2 - pi2/2.}};

                v.m_b = new double[] {
                        0.,
                        v.m_x[1]/(2.*dt) + pi2*xi/2.,
                        v.m_x[2]/(2.*dt) + pi2*xi1/2.
                };
                i++;
                return v;
            }
        };
    }

    @Override
    public Production makeAN(Vertex v, double h, double dt, double t, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t ,barrier) {
            @Override
            Vertex apply(Vertex v) {
                double dx2 = Math.pow(h, 2.);
                v.m_a = new double[][]{
                        {0., 0.                          , 0.},
                        {0., 1./(2.*dt) + 1./dx2 - pi2/2., -1./(dx2)},
                        {0., -1./h                       , 1./h}};

                double xi = h * i,
                        xi1 = xi + h;

                v.m_b = new double[] {
                        0.,
                        v.m_x[1]/(2.*dt) + pi2*xi/2.,
                        1 - Math.PI
                };
                i = 1;
                return v;
            }
        };
    }
}
