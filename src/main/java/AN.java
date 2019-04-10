import java.util.concurrent.CyclicBarrier;

class AN extends Production {

    AN(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T) {
        System.out.println("AN");
        T.m_a[1][1] = 1.0;
        T.m_a[2][1] = -1.0;
        T.m_a[1][2] = -1.0;
        T.m_a[2][2] = 1.0;
        T.m_b[1] = 0.0;
        T.m_b[2] = 1.0 / 6.0;
        return T;
    }
}