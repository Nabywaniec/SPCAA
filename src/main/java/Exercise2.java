import java.util.concurrent.CyclicBarrier;

import static java.lang.Math.*;

public class Exercise2 {


    public Production makeA1(Vertex v, double h, double dt, double t, double j, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {
                v.m_a[1][1] = 1.0;
                v.m_a[1][2] = 0.0;
                v.m_b[1] = 0.0;

                v.m_a[2][1] = -1.0 / (2.0 * h * h);
                v.m_a[2][2] = 1.0 / (2.0 * dt) + 1.0 / (2.0 * h * h) - pow(PI, 2.0) / 4.0;
                v.m_b[2] = v.m_x[1] / (2.0 * dt) + v.m_x[0] / (4.0 * h * h) - v.m_x[1] / (2.0 * h * h) + v.m_x[2] / (4.0 * h * h) + pow(PI,2.0) *
                        v.m_x[1] / 4.0 - pow(PI,2.0) * (h*j + h) / 2.0;
                return v;
            }
        };
    }


    public Production makeA(Vertex v, double h, double dt, double t, double j, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {
                v.m_a[1][1] = 1.0 / (2.0 * dt) + 1.0 / (2.0 * h * h) - pow(PI, 2.0) / 4.0;
                v.m_a[1][2] = -1.0 / ( 2.0 * h * h);
                v.m_b[1] = v.m_x[1] / (2.0 * dt) + v.m_x[0] / (4.0 * h * h) - v.m_x[1] / (2.0 * h * h) + v.m_x[2] / (4.0 * h * h) + pow(PI,2.0) *
                        v.m_x[1] / 4.0 - pow(PI,2.0) * (h*j) / 2.0;

                v.m_a[2][1] = -1.0 / (2.0 * h * h);
                v.m_a[2][2] = 1.0 / (2.0 * dt) + 1.0 / (2.0 * h * h) - pow(PI, 2.0) / 4.0;
                v.m_b[2] = v.m_x[1] / (2.0 * dt) + v.m_x[0] / (4.0 * h * h) - v.m_x[1] / (2.0 * h * h) + v.m_x[2] / (4.0 * h * h) + pow(PI,2.0) *
                        v.m_x[1] / 4.0 - pow(PI,2.0) * (h*j + h) / 2.0;
                return v;
            }
        };
    }


    public Production makeAN(Vertex v, double h, double dt, double t, double j, CyclicBarrier barrier) {
        return new ABase(v, h, dt, t, barrier) {
            @Override
            Vertex apply(Vertex v) {
                v.m_a[1][1] = 1.0 / (2.0 * dt) + 1.0 / (2.0 * h * h) - pow(PI, 2.0) / 4.0;
                v.m_a[1][2] = -1.0 / ( 2.0 * h * h);
                v.m_b[1] = v.m_x[1] / (2.0 * dt) + v.m_x[0] / (4.0 * h * h) - v.m_x[1] / (2.0 * h * h) + v.m_x[2] / (4.0 * h * h) + pow(PI,2.0) *
                        v.m_x[1] / 4.0 - pow(PI,2.0) * (h*j) / 2.0;

                v.m_a[2][1] = -1.0 / h;
                v.m_a[2][2] = 1.0 / h;
                v.m_b[2] = 1.0;
                return v;
            }
        };
    }

}
