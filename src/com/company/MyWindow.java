package com.company;

import com.company.Matrix;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MyWindow extends JFrame {

    public Vector calcFaceNorm(Vector v1, Vector v2, Vector v3){
        double ax = v1.dat[0] - v2.dat[0];
        double ay = v1.dat[1] - v2.dat[1];
        double az = v1.dat[2] - v2.dat[2];
        double bx = v1.dat[0] - v3.dat[0];
        double by = v1.dat[1] - v3.dat[1];
        double bz = v1.dat[2] - v3.dat[2];
        double rx = ay*bz - az*by;
        double ry = az*bx - ax*bz;
        double rz = ax*by - ay*bx;
        double len = Math.sqrt(((rx*rx)+(ry*ry)+(rz*rz)));
        rx /= len;
        ry /= len;
        rz /= len;
        return new Vector(3, rx, ry, rz);
    }

    public void fillTriangle(Graphics g, double x1, double y1, double x2, double y2, double x3, double y3){
        if(y1>y2){
            fillTriangle(g, x2, y2, x1, y1, x3, y3);
            return;
        }
        if(y2>y3){
            fillTriangle(g, x1,y1, x3, y3, x2, y2);
            return;
        }
        if(y1>y3){
            fillTriangle(g, x3, y3, x2, y2, x1, y1);
            return;
        }


        //fill triangle procedure
        double m1 = (x2-x1)/(y2-y1+1);
        double m2 = (x3-x2)/(y3-y2+1);
        double m3 = (x3-x1)/(y3-y1+1);
        double startX = x1;
        double endX = x1;
        for(int y = (int)y1;y<y2+1;y++,startX += m1, endX += m3){
            g.fillRect((int) Math.min(startX, endX),y,(int)(Math.max(startX, endX) - Math.min(startX, endX)),1);
        }
        startX = x2;
        endX = x1 + (y2-y1+1)*m3;
        for(int y=(int)y2;y<y3+1;y++, startX += m2,endX += m3){
            g.fillRect((int) Math.min(startX, endX),y,(int)(Math.max(startX, endX) - Math.min(startX, endX)),1);
        }

    }
    Vector[] norms= new Vector[6];
    int[][] faces = new int[][]{{1,2,3,4},{8,7,6,5},{8,4,3,7},{5,1,4,8},{7,3,2,6},{6,2,1,5}};
    Color[] colors = new Color[]{Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED, Color.GREEN};
    Vector[] verts = new Vector[] { //place this object around the orign then move it to view,
            // this means when we rotate it, the whole cube isnt moved
            new Vector(4, -1, -1, 1, 1), //1
            new Vector(4, -1, 1, 1, 1), //2
            new Vector(4, 1, 1, 1, 1), //3
            new Vector(4, 1, -1, 1, 1), //4
            new Vector(4, -1, -1, -1, 1), //5
            new Vector(4, -1, 1, -1, 1),  //6
            new Vector(4, 1, 1, -1, 1),  //7
            new Vector(4, 1, -1, -1, 1)}; //8


    /** EDIT: changed z offset to -5  **/
    Matrix modelView = Matrix.translate(0,0,-5);
    Matrix projection = Matrix.perspective(1,10,45);


    double angle = 0;

    public MyWindow(){
        this.setSize(600,600);
        this.setTitle("3D RENDERER by Nishil Amin");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        //Matrix.translate(0, 0, 0).multiply(Matrix.rotateZ(45)).print();

        for(int i=0;i<faces.length;i++){
            Vector v1 = verts[faces[i][0]-1];
            Vector v2 = verts[faces[i][1]-1];
            Vector v3 = verts[faces[i][2]-1];
            norms[i] = calcFaceNorm(v1,v2,v3);
        }

        JPanel canvas = new JPanel(){
            @Override
            public void paint(Graphics g){
                /** EDIT: clear the window **/
                ArrayList<Triangle> triangles = new ArrayList<>();

                g.clearRect(0, 0, getWidth(), getHeight());

                //do projection here
                Vector[] projectedVct = new Vector[8];
                //model view project
                /** EDIT: added in rotation around y axis **/
                Matrix mv = modelView.multiply(Matrix.rotateX(angle).multiply(Matrix.rotateY(angle)));
                Matrix mvp = projection.multiply(mv);
                for  (int i=0;i<verts.length;i++) {

                    //multiply the matrix transformation by the vector to store in projectedVCT
                    projectedVct[i] = mvp.multiplyVect(verts[i]);//(returns a vector which is stored)
                    //scale the vector to put it in clip coords (correct position of the screen)
                    projectedVct[i].scale(1 / projectedVct[i].dat[3]); //(changes the vector itself)

                    /** EDIT small change to scaling into screen coordinates **/ //get X and Y coords to display vertex
                    int x = (int) ((projectedVct[i].dat[0]) * 300 + 300);
                    int y = (int) (300 - (projectedVct[i].dat[1]) * 300);
                    g.setColor(Color.BLACK);
                    /** EDIT changed width and height **/
                    g.drawOval(x - 2, y - 2, 5, 5);
                }
                for(int j=0;j<faces.length;j++) {
                    Vector v1 = projectedVct[faces[j][0]-1];
                    Vector v2 = projectedVct[faces[j][1]-1];
                    Vector v3 = projectedVct[faces[j][2]-1];
                    Vector v4 = projectedVct[faces[j][3]-1];
                    Triangle t = new Triangle(v1, v2, v3,colors[j]);
                    Triangle t1 = new Triangle(v1, v3, v4,colors[j]);
                        if(t.isFront()){
                            triangles.add(t);
                    }
                    if(t1.isFront()){
                        triangles.add(t1);
                    }
                    //fillTriangle(g,v1.dat[0]*300+300,300-v1.dat[1]*300,v2.dat[0]*300+300,300-v2.dat[1]*300,v3.dat[0]*300+300,300-v3.dat[1]*300);
                    //fillTriangle(g,v1.dat[0]*300+300,300-v1.dat[1]*300,v4.dat[0]*300+300,300-v4.dat[1]*300,v3.dat[0]*300+300,300-v3.dat[1]*300);

                    /*for(int k=0;k<faces[j].length;k++){
                        g.drawLine((int) ((projectedVct[faces[j][k]-1].dat[0]) * 300 + 300), (int) (300 - (projectedVct[faces[j][k]-1].dat[1]) * 300),
                                (int) ((projectedVct[faces[j][(k+1)%4]-1].dat[0]) * 300 + 300), (int) (300 - (projectedVct[faces[j][(k+1)%4]-1].dat[1]) * 300));
                    }
                    */

                }
                Collections.sort(triangles);
                System.out.println("start");
                for(Triangle t : triangles){
                    System.out.println(t);
                    g.setColor(t.c);
                    fillTriangle(g,t.x1,t.y1,t.x2,t.y2,t.x3,t.y3);

                }
            }
        };
        content.add(canvas, BorderLayout.CENTER);


        /** EDIT added timer for redraw, also updates angle every frame **/
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                angle += 1;
                canvas.repaint();
            }
        }, 0, 30);

        this.setVisible(true);
    }
}
