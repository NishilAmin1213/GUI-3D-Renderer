package com.company;

import java.util.Arrays;

//static methods and attributes do not require an instance to be called

public class Matrix {
    public int dimX;
    public int dimY;
    public double[][] dat;

    public static Matrix perspective(double near, double far,double fov){
        double cot = 1/(Math.tan(Math.toRadians(fov/2)));
        double denom = near - far;
        return new Matrix(4,4, cot, 0,0,0,0,cot,0,0,0,0,(-(near+far)/denom),(-(2*far*near)/denom),0,0,-1,0);
    }

    public static Matrix rotateX(double a){
        a = Math.toRadians(a);
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Matrix(4,4, 1,0,0,0,0,cos,-sin,0,0,sin,cos,0,0,0,0,1);
    }

    public static Matrix rotateY(double a){
        a = Math.toRadians(a);
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        //System.out.println("cos "+cos + " sin "+sin);
        return new Matrix(4,4, cos, 0, sin, 0, 0,1,0,0,-sin, 0,cos,0,0,0,0,1);
    }

    public static Matrix rotateZ(double a){
        a = Math.toRadians(a);
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Matrix(4,4, cos,-sin, 0,0,sin, cos,0,0,0,0,1,0,0,0,0,1);
    }

    public static Matrix scale(double x, double y, double z){
        return new Matrix(4,4, x,0,0,0,0,y,0,0,0,0,z,0,0,0,0,1);
    }

    public static Matrix translate(double x, double y, double z){
        return new Matrix(4,4, 1,0,0,x,0,1,0,y,0,0,1,z,0,0,0,1);
    }


    public Matrix(int dimX, int dimY, double... values) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.dat = new double[dimY][dimX];
        if (values.length == 0) {
            //create an matrix with values 0
            for (int i = 0; i < dimY; i++) {
                for (int j = 0; j < dimX; j++) {
                    dat[i][j] = 0;
                }
            }
        } else if (values.length == (dimX * dimY)) {
            int x = 0;
            //create an matrix with correct values
            for (int i = 0; i < dimY; i++) {
                for (int j = 0; j < dimX; j++) {
                    dat[i][j] = values[x];
                    x++;
                }
            }
        } else{
            //INVALID MATRIX
        }
    }

    public void print(){
        System.out.println("Print Matrix");
        for(int i=0;i<this.dat.length;i++){
            System.out.println(Arrays.toString(this.dat[i]));
        };
    }

    public Matrix multiply(Matrix b){
        if(this.dimX == b.dimY){
            //Matrices can be multiplied
            double[] lst = new double[(this.dimX * b.dimY)];
            int a = 0;
            for(int row=0;row<this.dimX;row++){
                for(int col=0;col<b.dimY;col++){
                    /** EDIT: should be double not int **/
                    double tmp = 0;
                    for(int i=0;i<this.dimY;i++){
                        tmp += this.dat[row][i] * b.dat[i][col];
                    }
                    lst[a] = tmp;
                    a++;
                }
            }
            return new Matrix(b.dimX, this.dimY, lst);
        }else{
            return null;
        }
    }

    public Vector multiplyVect(Vector b){
        if(this.dimX == b.dimY){
            double[] lst = new double[b.dimY];
            //dimensions mean we can multiply these
            for(int i=0;i<b.dimY;i++){
                /** EDIT: should be double not int **/
                double tmp = 0;
                for(int j=0;j<this.dimX;j++){
                    tmp+=(b.dat[j] * this.dat[i][j]);
                }
                lst[i] = tmp;
            }
            return new Vector(b.dimY, lst);
        }else{
            return null;
        }
    }


    public Matrix add(Matrix b){
        if(this.dimY == b.dimY && this.dimX == b.dimX){
            //dimensions are the same
            double[] lst = new double[(this.dimX * b.dimY)];
            int a = 0;
            for(int i=0;i<this.dimX;i++){
                for(int j=0;j<this.dimY;j++){
                    lst[a] = this.dat[i][j] + b.dat[i][j];
                    a++;
                }
            }
            return new Matrix(this.dimY, this.dimX, lst);
        }
        return null;
    }




}
