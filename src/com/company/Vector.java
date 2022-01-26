package com.company;

import java.util.Arrays;

public class Vector {
    public int dimY;
    public int dimX;
    public double[] dat;

    public Vector(int dimY, double... values) {
        this.dimX = 1;
        this.dimY = dimY;
        this.dat = new double[dimY];
        if(values.length == 0){
            //create a vector with all columns 0
            for(int i = 0;i<dimY;i++){
                dat[i] = 0;
            }
        }else if(values.length == dimY){
            for(int j=0; j<dimY;j++){
                dat[j] = values[j];
            }
        } else {
            System.out.println("INVALID VCT");
            //INVALID VECTOR
        }
    }

    public void scale(double a){
        for(int i=0;i<this.dat.length;i++){
            this.dat[i] *= a;
        }
    }

    public Vector add(Vector b){
        double[] lst = new double[this.dimY];
        if(this.dimY == b.dimY){
            //vectors can be added together
            for(int i=0;i<this.dimY;i++){
                lst[i] = this.dat[i] + b.dat[i];
            }
        }else{
            return null;
        }
        return new Vector(this.dimY, lst);
    }

    public void print(){
        System.out.println(Arrays.toString(this.dat));
    }



}
