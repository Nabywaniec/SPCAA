import java.util.concurrent.CyclicBarrier;

class AN extends Production {

    private double h;

    AN(Vertex Vert, double h, CyclicBarrier Barrier) {
        super(Vert, Barrier);
        this.h=h;
    }

    Vertex apply(Vertex T) {
        System.out.println("AN");

        T.m_a[1][1]=1.0;
        T.m_a[2][1]=-1.0;
        T.m_a[1][2]=-1.0;
        T.m_a[2][2]=1.0;
        T.m_b[1]=0.0;
        T.m_b[2]=h;

        return T;
    }
}