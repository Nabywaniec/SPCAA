import java.util.concurrent.CyclicBarrier;

class A extends Production {

    A(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T) {
        System.out.println("A");
        T.m_a[1][1] = 1.0;
        T.m_a[2][1] = -1.0;
        T.m_a[1][2] = -1.0;
        T.m_a[2][2] = 1.0;
        T.m_b[1] = 0.0;
        T.m_b[2] = 0.0;
        return T;
    }
}