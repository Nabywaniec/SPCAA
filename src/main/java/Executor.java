import org.math.plot.utils.Array;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
            P2 p2_1 = new P2(p1.m_vertex.m_left, barrier);
            P2 p2_2 = new P2(p1.m_vertex.m_right, barrier);
            p2_1.start();
            p2_2.start();
            barrier.await();

            barrier = new CyclicBarrier(2 + 1);
            P2 p2_3 = new P2(p2_1.m_vertex.m_left, barrier);
            P2 p2_4 = new P2(p2_1.m_vertex.m_right, barrier);
            p2_3.start();
            p2_4.start();
            barrier.await();

            //[(P3)1(P3)2(P3)3(P3)4(P3)5(P3)6]
            barrier = new CyclicBarrier(6 + 1);
            P3 p3_1 = new P3(p2_3.m_vertex.m_left, barrier);
            P3 p3_2 = new P3(p2_3.m_vertex.m_right, barrier);
            P3 p3_3 = new P3(p2_4.m_vertex.m_left, barrier);
            P3 p3_4 = new P3(p2_4.m_vertex.m_right, barrier);
            P3 p3_5 = new P3(p2_2.m_vertex.m_left, barrier);
            P3 p3_6 = new P3(p2_2.m_vertex.m_right, barrier);
            p3_1.start();
            p3_2.start();
            p3_3.start();
            p3_4.start();
            p3_5.start();
            p3_6.start();
            barrier.await();


            //[(A1)(A)1(A)2(A)3(A)4(AN)
            barrier = new CyclicBarrier(6+1);
            A1 localMat1 = new A1(p3_1.m_vertex, barrier);
            A localMat2 = new A(p3_2.m_vertex, barrier);
            A localMat3 = new A(p3_3.m_vertex, barrier);
            A localMat4 = new A(p3_4.m_vertex, barrier);
            A localMat5 = new A(p3_5.m_vertex, barrier);
            AN localMat6 = new AN(p3_6.m_vertex, 1.0/6.0,  barrier);

            localMat1.start(); localMat2.start(); localMat3.start();

            localMat4.start(); localMat5.start(); localMat6.start();
            barrier.await();

            //[(A2)1(A2)2(A2)3]
            barrier = new CyclicBarrier(3+1);
            A2 mergedMat1 = new A2(p2_2.m_vertex, barrier);
            A2 mergedMat2 = new A2(p2_3.m_vertex, barrier);
            A2 mergedMat3 = new A2(p2_4.m_vertex, barrier);
            mergedMat1.start(); mergedMat2.start(); mergedMat3.start();
            barrier.await();
            // PLEASE CONTINUE HERE .

            barrier = new CyclicBarrier(3+1);
            E2 gauss1 = new E2(p2_2.m_vertex, barrier);
            E2 gauss2 = new E2(p2_3.m_vertex, barrier);
            E2 gauss3 = new E2(p2_4.m_vertex, barrier);

            gauss1.start(); gauss2.start(); gauss3.start();
            barrier.await();

            barrier = new CyclicBarrier(1+1);
            A2 mergeGauss = new A2(p2_1.m_vertex, barrier);
            mergeGauss.start();
            barrier.await();


            barrier = new CyclicBarrier(1+1);
            E2 gaussFromMergeGauss = new E2(p2_1.m_vertex, barrier);
            gaussFromMergeGauss.start();
            barrier.await();

            barrier = new CyclicBarrier(1+1);
            Aroot aRoot = new Aroot(p1.m_vertex, barrier);
            aRoot.start();
            barrier.await();
//
//
            //eroot
            barrier = new CyclicBarrier(1+1);
            Eroot eroot = new Eroot(p1.m_vertex, barrier);
            eroot.start();
            barrier.await();


            //BS
            barrier = new CyclicBarrier(1+1);
            BS bs1 = new BS(eroot.m_vertex, barrier);
            bs1.start();
            barrier.await();

            // BS -> BS i BSA
            barrier = new CyclicBarrier(2+1);
            BS bs2 = new BS(bs1.m_vertex.m_left, barrier);
            BSA bsa1 = new BSA(bs1.m_vertex.m_right, barrier);
            bs2.start(); bsa1.start();
            barrier.await();

            // BS -> BS i BSA
            barrier = new CyclicBarrier(2+1);
            BSA bsa2= new BSA(bs2.m_vertex.m_left, barrier);
            BSA bsa3 = new BSA(bs2.m_vertex.m_right, barrier);
            bsa2.start(); bsa3.start();
            barrier.await();


            return S;
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }





    public Vertex run(int levels) {
        Vertex S = new Vertex(null, null, null, "S");

        try {

            //TREE CONSTRUCTION
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();

            ArrayList<ArrayList<P2>> p2 = new ArrayList<>();
            ArrayList<P3> p3 = new ArrayList<>();

            if(levels == 2){
                barrier = new CyclicBarrier(2 + 1);
                P3 p3a = new P3(p1.m_vertex.m_left, barrier);
                P3 p3b = new P3(p1.m_vertex.m_right, barrier);
                p3a.start();
                p3b.start();
                barrier.await();
                p3.add(p3a);
                p3.add(p3b);

                barrier = new CyclicBarrier(2+1);
                A1 localMat1 = new A1(p3.get(0).m_vertex, barrier);
                AN localMat2 = new AN(p3.get(1).m_vertex, 1.0/2.0, barrier);
                localMat1.start();
                localMat2.start();
                barrier.await();

            }else {

                barrier = new CyclicBarrier(2 + 1);
                P2 p2_1 = new P2(p1.m_vertex.m_left, barrier);
                P2 p2_2 = new P2(p1.m_vertex.m_right, barrier);
                p2_1.start();
                p2_2.start();
                barrier.await();

                ArrayList<P2> p2Childs = new ArrayList<>();
                p2Childs.add(p2_1);
                p2Childs.add(p2_2);

                p2.add(p2Childs);

                //P2 for level 4 and more

                for (int i = 0; i <= levels - 4; i++) {
                    int p2Number = 2 * p2.get(p2.size() - 1).size();

                    ArrayList<P2> toAddChilds = p2.get(i);
                    ArrayList<P2> p2Child = new ArrayList<>();
                    barrier = new CyclicBarrier(p2Number + 1);
                    for (int j = 0; j < toAddChilds.size(); j++) {
                        P2 p2level2OrMore_1 = new P2(toAddChilds.get(j).m_vertex.m_left, barrier);
                        P2 p2level2OrMore_2 = new P2(toAddChilds.get(j).m_vertex.m_right, barrier);
                        p2level2OrMore_1.start();
                        p2level2OrMore_2.start();
                        p2Child.add(p2level2OrMore_1);
                        p2Child.add(p2level2OrMore_2);
                    }
                    p2.add(p2Child);
                    barrier.await();
                }

                // P3
                ArrayList<P2> toAddP3 = p2.get(p2.size() - 1);
                int p3Number = toAddP3.size() * 2;

                barrier = new CyclicBarrier(p3Number + 1);
                for (int j = 0; j < toAddP3.size(); j++) {
                    P3 p3_1 = new P3(toAddP3.get(j).m_vertex.m_left, barrier);
                    P3 p3_2 = new P3(toAddP3.get(j).m_vertex.m_right, barrier);
                    p3_1.start();
                    p3_2.start();
                    p3.add(p3_1);
                    p3.add(p3_2);
                }
                barrier.await();
                // MERGOWANIE

                barrier = new CyclicBarrier(p3Number+1);
                A1 localMatStart = new A1(p3.get(0).m_vertex, barrier);
                AN localMatEnd = new AN(p3.get(p3Number-1).m_vertex,1.0/ (double) p3Number, barrier);

                localMatStart.start();
                for(int i=1; i< p3Number-1; i++) {
                    A localMatX = new A(p3.get(i).m_vertex, barrier);
                    localMatX.start();
                }
                localMatEnd.start();
                barrier.await();


                int a2Number = p3Number/2;
                for (int i = p2.size() - 1; i >= 0; i--) {

                    barrier = new CyclicBarrier(a2Number + 1);
                    for (int j = 0; j < p2.get(i).size(); j++) {
                        A2 a2 = new A2(p2.get(i).get(j).m_vertex, barrier);
                        a2.start();
                    }
                    barrier.await();


                    barrier = new CyclicBarrier(a2Number + 1);
                    for (int j = 0; j < p2.get(i).size(); j++) {
                        E2 e2 = new E2(p2.get(i).get(j).m_vertex, barrier);
                        e2.start();
                    }
                    barrier.await();

                    a2Number/=2.0;
                }


            }


            barrier = new CyclicBarrier(1+1);
            Aroot aRoot = new Aroot(p1.m_vertex, barrier);
            aRoot.start();
            barrier.await();
//
//
            //eroot
            barrier = new CyclicBarrier(1+1);
            Eroot eroot = new Eroot(p1.m_vertex, barrier);
            eroot.start();
            barrier.await();

            if(levels == 2){
                barrier = new CyclicBarrier(1+1);
                BSA bsa1 = new BSA(eroot.m_vertex, barrier);
                bsa1.start();
                barrier.await();
            }else {

                ArrayList<BS> prevBS = new ArrayList<>();

                barrier = new CyclicBarrier(1+1);
                BS bs1 = new BS(eroot.m_vertex, barrier);
                bs1.start();
                prevBS.add(bs1);
                barrier.await();

                int bsLastLvlNumber = p2.get(p2.size()-1).size()/2;
                int bsLvls = (int)(Math.log(bsLastLvlNumber) / Math.log(2))+1;
                int bsNumberAtLvl = 2;

                for(int i = 1; i < bsLvls; i++){
                    ArrayList<BS> tempBS = new ArrayList<>();
                    barrier = new CyclicBarrier(bsNumberAtLvl+1);

                    for(int j=0; j < bsNumberAtLvl/2; j++){
                        BS bsX1 = new BS(prevBS.get(j).m_vertex.m_left, barrier);
                        BS bsX2 = new BS(prevBS.get(j).m_vertex.m_right, barrier);
                        bsX1.start();
                        bsX2.start();
                        tempBS.add(bsX1);
                        tempBS.add(bsX2);
                    }

                    barrier.await();
                    bsNumberAtLvl*=2;
                    prevBS = tempBS;
                }



                int bsaNumber = bsNumberAtLvl;
                barrier = new CyclicBarrier(bsaNumber+1);

                for(int j=0; j < bsaNumber/2; j++){
                    BSA bsaX1 = new BSA(prevBS.get(j).m_vertex.m_left, barrier);
                    BSA bsaX2 = new BSA(prevBS.get(j).m_vertex.m_right, barrier);
                    bsaX1.start();
                    bsaX2.start();
                }
                barrier.await();


            }

        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        return S;
    }
}
