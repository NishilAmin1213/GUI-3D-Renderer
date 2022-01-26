package com.company;

import java.awt.*;

public class Triangle implements Comparable<Triangle> {
    // Declare Variables for the 3 triangle coordinates and the color of the triangle
    double x1;
    double y1;
    double z1;
    double x2;
    double y2;
    double z2;
    double x3;
    double y3;
    double z3;
    Color c;

    // Constructor for the Triangle Class
    public Triangle(Vector v1,Vector v2, Vector v3, Color col){
        // Uses the passed in vector coordinates to initialise all the variables required to identify a triangle
        x1 = v1.dat[0]*300+300;
        y1 = 300-v1.dat[1]*300;
        z1 = v1.dat[2];
        x2 = v2.dat[0]*300+300;
        y2 = 300-v2.dat[1]*300;
        z2 = v2.dat[2];
        x3 = v3.dat[0]*300+300;
        y3 = 300-v3.dat[1]*300;
        z3 = v3.dat[2];
        c = col;
    }

    @Override
    public String toString() {
        // Simple Output Method to Debug and Test the Triangle
        return "depth: "+this.depth()+" color: "+this.c;
    }

    public double depth(){
        // Return the Depth of the triangle in 3D Space
        return -((z1+z2+z3)/3);
    }

    public boolean isFront(){

        double e1x = x2 - x1;
        double e1y = y2 - y1;
        double e2x = x3 - x1;
        double e2y = y3 - y1;
        double crossP = (e1x*e2y)-(e1y*e2x);
        if(crossP > 0){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public int compareTo(Triangle o) {
        // Compare the Depth of 2 Triangles in 3D Space
        //return 1, 0 or -1 depending on >, < or = the object o
        if(this.depth() > o.depth()){
            return -1;
        }else if(this.depth() < o.depth()){
            return 1;
        }else{
            return 0;
        }
    }
}
