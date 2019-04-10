import java.util.concurrent.CyclicBarrier;

class P3 extends Production {
    P3(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T) {
        System.out.println("p3");
        Vertex T1 = new Vertex(null, null, T, "node");
        Vertex T2 = new Vertex(null, null, T, "node");
        T.set_left(T1);
        T.set_right(T2);
        T.set_label("int");
        return T;
    }
}