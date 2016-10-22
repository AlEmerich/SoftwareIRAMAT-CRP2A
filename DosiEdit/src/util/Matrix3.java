package util;

import java.nio.FloatBuffer;

public class Matrix3
{
    private static int SIZE;
    private double[][] coef;
    
    static {
        Matrix3.SIZE = 3;
    }
    
    public Matrix3() {
        this.coef = new double[3][3];
        this.init();
    }
    
    public Matrix3(final double m11, final double m12, final double m13, final double m21, final double m22, final double m23, final double m31, final double m32, final double m33) {
        this.coef = new double[3][3];
        this.coef[0][0] = m11;
        this.coef[1][0] = m12;
        this.coef[2][0] = m13;
        this.coef[0][1] = m21;
        this.coef[1][1] = m22;
        this.coef[2][1] = m23;
        this.coef[0][2] = m31;
        this.coef[1][2] = m32;
        this.coef[2][2] = m33;
    }
    
    public Matrix3(final double[][] values) {
        this.coef = new double[3][3];
        this.coef = values;
    }
    
    public Matrix3(final Vector3d v1, final Vector3d v2, final Vector3d v3) {
        this.coef = new double[3][3];
        this.coef[0][0] = v1.getX();
        this.coef[1][0] = v1.getY();
        this.coef[2][0] = v1.getZ();
        this.coef[0][1] = v2.getX();
        this.coef[1][1] = v2.getY();
        this.coef[2][1] = v2.getZ();
        this.coef[0][2] = v3.getX();
        this.coef[1][2] = v3.getY();
        this.coef[2][2] = v3.getZ();
    }
    
    public Matrix3(final FloatBuffer buff) {
        this.coef = new double[3][3];
        if (buff.array().length != Matrix3.SIZE * Matrix3.SIZE) {
            this.init();
        }
        else {
            for (int i = 0; i < Matrix3.SIZE; ++i) {
                for (int j = 0; j < Matrix3.SIZE; ++j) {
                    this.coef[j][i] = buff.get(i * Matrix3.SIZE + j);
                }
            }
        }
    }
    
    public final void init() {
        for (int i = 0; i < Matrix3.SIZE; ++i) {
            for (int j = 0; j < Matrix3.SIZE; ++j) {
                this.coef[i][j] = 0.0;
            }
        }
    }
    
    public double[][] getValues() {
        return this.coef;
    }
    
    public double getValue(final int row, final int column) {
        return this.coef[row][column];
    }
    
    public double getTrace() {
        return this.coef[0][0] + this.coef[1][1] + this.coef[2][2];
    }
    
    public void setIdentity() {
        this.init();
        for (int i = 0; i < Matrix3.SIZE; ++i) {
            this.coef[i][i] = 1.0;
        }
    }
    
    public Matrix3 mult(final Matrix3 mult) {
        final Matrix3 temp = new Matrix3();
        temp.init();
        for (int i = 0; i < Matrix3.SIZE; ++i) {
            for (int j = 0; j < Matrix3.SIZE; ++j) {
                double som = 0.0;
                for (int k = 0; k < Matrix3.SIZE; ++k) {
                    som += this.coef[i][k] * mult.coef[k][j];
                }
                temp.coef[i][j] = som;
            }
        }
        return temp;
    }
    
    public Vector3d mult(final Vector3d vect) {
        final Vector3d temp = new Vector3d();
        temp.init();
        for (int i = 0; i < Matrix3.SIZE; ++i) {
            double som = 0.0;
            for (int k = 0; k < Matrix3.SIZE; ++k) {
                som += this.coef[i][k] * vect.getComponent(k);
            }
            temp.setComponent(i, som);
        }
        return temp;
    }
    
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < Matrix3.SIZE; ++i) {
            for (int j = 0; j < Matrix3.SIZE; ++j) {
                str.append(this.coef[i][j]).append(" ");
            }
            str.append(" | ");
        }
        return str.toString();
    }
    
    public FloatBuffer toFloatBuffer() {
        final FloatBuffer temp = FloatBuffer.allocate(Matrix3.SIZE * Matrix3.SIZE);
        temp.put(0, (float)this.coef[0][0]);
        temp.put(1, (float)this.coef[1][0]);
        temp.put(2, (float)this.coef[2][0]);
        temp.put(3, (float)this.coef[0][1]);
        temp.put(4, (float)this.coef[1][1]);
        temp.put(5, (float)this.coef[2][1]);
        temp.put(6, (float)this.coef[0][2]);
        temp.put(7, (float)this.coef[1][2]);
        temp.put(8, (float)this.coef[2][2]);
        return temp;
    }
}
