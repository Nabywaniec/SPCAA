import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@SuppressWarnings("ALL")
class Executor {

    public Vertex run() {
        Vertex S = new Vertex(null, null, null, "S");
        try {
            //[(P1)]
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();
            //[(P2)1(P2)2]
            barrier = new CyclicBarrier(2 + 1);
            P2 p2a = new P2(p1.m_vertex.m_left, barrier);
            P2 p2b = new P2(p1.m_vertex.m_right, barrier);
            p2a.start();
            p2b.start();
            barrier.await();
            //[(P2)1(P2)2]
            barrier = new CyclicBarrier(2 + 1);
            P2 p2c = new P2(p2a.m_vertex.m_left, barrier);
            P2 p2d = new P2(p2a.m_vertex.m_right, barrier);
            p2c.start();
            p2d.start();
            barrier.await();
            //[(P3)1(P3)2(P3)3(P3)4]
            barrier = new CyclicBarrier(6 + 1);
            P3 p3a = new P3(p2c.m_vertex.m_left, barrier);
            P3 p3b = new P3(p2c.m_vertex.m_right, barrier);
            P3 p3c = new P3(p2d.m_vertex.m_left, barrier);
            P3 p3d = new P3(p2d.m_vertex.m_right, barrier);
            P3 p3e = new P3(p2b.m_vertex.m_left, barrier);
            P3 p3f = new P3(p2b.m_vertex.m_right, barrier);
            p3a.start();
            p3b.start();
            p3c.start();
            p3d.start();
            p3e.start();
            p3f.start();
            barrier.await();
            //multi-frontal solver algorithm
            barrier = new CyclicBarrier(6 + 1);
            A1 a1 = new A1(p3a.m_vertex, barrier);
            A a_2 = new A(p3b.m_vertex, barrier);
            A a_3 = new A(p3c.m_vertex, barrier);
            A a_4 = new A(p3d.m_vertex, barrier);
            A a_5 = new A(p3e.m_vertex, barrier);
            AN an = new AN(p3f.m_vertex, barrier);
            a1.start();
            a_2.start();
            a_3.start();
            a_4.start();
            a_5.start();
            an.start();
            barrier.await();

            barrier = new CyclicBarrier(3 + 1);
            A2 a2_1 = new A2(p2c.m_vertex, barrier);
            A2 a2_2 = new A2(p2d.m_vertex, barrier);
            A2 a2_3 = new A2(p2b.m_vertex, barrier);
            a2_1.start();
            a2_2.start();
            a2_3.start();
            barrier.await();

            barrier = new CyclicBarrier(3 + 1);
            E2 e2_1 = new E2(p2c.m_vertex, barrier);
            E2 e2_2 = new E2(p2d.m_vertex, barrier);
            E2 e2_3 = new E2(p2b.m_vertex, barrier);
            e2_1.start();
            e2_2.start();
            e2_3.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            A2 a2_4 = new A2(p2a.m_vertex, barrier);
            a2_4.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            E2 e2_4 = new E2(p2a.m_vertex, barrier);
            e2_4.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            Aroot aroot = new Aroot(p1.m_vertex, barrier);
            aroot.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            Eroot eroot = new Eroot(p1.m_vertex, barrier);
            eroot.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            BS bs1 = new BS(p1.m_vertex, barrier);
            bs1.start();
            barrier.await();

            barrier = new CyclicBarrier(2 + 1);
            BS bs2 = new BS(p2a.m_vertex, barrier);
            BSA bsa = new BSA(p2b.m_vertex, barrier);
            bs2.start();
            bsa.start();
            barrier.await();

            barrier = new CyclicBarrier(2 + 1);
            BSA bs3 = new BSA(p2c.m_vertex, barrier);
            BSA bs4 = new BSA(p2d.m_vertex, barrier);
            bs3.start();
            bs4.start();
            barrier.await();

            return S;
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public Vertex run(int levels) {
        Vertex S = new Vertex(null, null, null, "S");
        try {
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();

            List<P2> p2List = new ArrayList<>();

            if (levels > 2) {
                barrier = new CyclicBarrier(2 + 1);
                P2 p2a = new P2(p1.m_vertex.m_left, barrier);
                P2 p2b = new P2(p1.m_vertex.m_right, barrier);
                p2List.add(p2a);
                p2List.add(p2b);
                p2a.start();
                p2b.start();
                barrier.await();
            }

            int lastIndex = 0;
            if(levels>3) {
                for (int i = 1; i < levels - 2; i++) {
                    barrier = new CyclicBarrier((int) Math.pow(2, i + 1) + 1);
                    for (int j = 0; j < (int) Math.pow(2, i); j++) {
                        P2 p2 = new P2(p2List.get(lastIndex).m_vertex.m_left, barrier);
                        P2 p2a = new P2(p2List.get(lastIndex).m_vertex.m_right, barrier);
                        lastIndex += 1;
                        p2List.add(p2);
                        p2List.add(p2a);
                        p2.start();
                        p2a.start();
                    }
                    barrier.await();
                }
            }
            if (levels == 2) {
                barrier = new CyclicBarrier(2 + 1);
                P3 p3 = new P3(p1.m_vertex.m_left, barrier);
                P3 p3a = new P3(p1.m_vertex.m_right, barrier);
                p3.start();
                p3a.start();
                barrier.await();
            } else {
                barrier = new CyclicBarrier((int)Math.pow(2,levels-1) + 1);
                for (int i = 0; i < (int) Math.pow(2, levels - 2); i++) {
                    P3 p3 = new P3(p2List.get(lastIndex).m_vertex.m_left, barrier);
                    P3 p3a = new P3(p2List.get(lastIndex).m_vertex.m_right, barrier);
                    lastIndex += 1;
                    p3.start();
                    p3a.start();
                }
                barrier.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        return S;
    }
}
