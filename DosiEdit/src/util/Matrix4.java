package util;

import java.nio.FloatBuffer;

public class Matrix4
{
    private static int SIZE;
    private double[][] coef;
    
    static {
        Matrix4.SIZE = 4;
    }
    
    public Matrix4() {
        this.coef = new double[4][4];
        this.init();
    }
    
    public Matrix4(final double m11, final double m12, final double m13, final double m14, final double m21, final double m22, final double m23, final double m24, final double m31, final double m32, final double m33, final double m34, final double m41, final double m42, final double m43, final double m44) {
        this.coef = new double[4][4];
        this.coef[0][0] = m11;
        this.coef[0][1] = m12;
        this.coef[0][2] = m13;
        this.coef[0][3] = m14;
        this.coef[1][0] = m21;
        this.coef[1][1] = m22;
        this.coef[1][2] = m23;
        this.coef[1][3] = m24;
        this.coef[2][0] = m31;
        this.coef[2][1] = m32;
        this.coef[2][2] = m33;
        this.coef[2][3] = m34;
        this.coef[3][0] = m41;
        this.coef[3][1] = m42;
        this.coef[3][2] = m43;
        this.coef[3][3] = m44;
    }
    
    public Matrix4(final double[][] values) {
        this.coef = new double[4][4];
        this.coef = values;
    }
    
    public Matrix4(final FloatBuffer buff) {
        this.coef = new double[4][4];
        if (buff.array().length != Matrix4.SIZE * Matrix4.SIZE) {
            this.init();
        }
        else {
            for (int i = 0; i < Matrix4.SIZE; ++i) {
                for (int j = 0; j < Matrix4.SIZE; ++j) {
                    this.coef[j][i] = buff.get(i * Matrix4.SIZE + j);
                }
            }
        }
    }
    
    public void init() {
        for (int i = 0; i < Matrix4.SIZE; ++i) {
            for (int j = 0; j < Matrix4.SIZE; ++j) {
                this.coef[i][j] = 0.0;
            }
        }
    }
    
    public void setIdentity() {
        this.init();
        for (int i = 0; i < Matrix4.SIZE; ++i) {
            this.coef[i][i] = 1.0;
        }
    }
    
    public static Matrix4 getTranslationMatrix(final Vector3d vect) {
        final Matrix4 mat = new Matrix4();
        mat.setIdentity();
        for (int i = 0; i < 3; ++i) {
            mat.coef[i][3] = vect.getComponent(i);
        }
        return mat;
    }
    
    public static Matrix4 getTranslationMatrix(final Vector3d vect, final double factor) {
        final Matrix4 mat = new Matrix4();
        mat.setIdentity();
        for (int i = 0; i < 3; ++i) {
            mat.coef[i][3] = vect.getComponent(i) * factor;
        }
        return mat;
    }
    
    public static Matrix4 getScaleMatrix(final double s) {
        final Matrix4 mat = new Matrix4();
        for (int i = 0; i < 3; ++i) {
            mat.coef[i][i] = s;
        }
        mat.coef[3][3] = 1.0;
        return mat;
    }
    
    public double[][] getValues() {
        return this.coef;
    }
    
    public double getValue(final int row, final int column) {
        return this.coef[row][column];
    }
    
    public double getTrace() {
        return this.coef[0][0] + this.coef[1][1] + this.coef[2][2] + this.coef[3][3];
    }
    
    public Matrix4 mult(final Matrix4 mult) {
        final Matrix4 temp = new Matrix4();
        temp.init();
        for (int i = 0; i < Matrix4.SIZE; ++i) {
            for (int j = 0; j < Matrix4.SIZE; ++j) {
                double som = 0.0;
                for (int k = 0; k < Matrix4.SIZE; ++k) {
                    som += this.coef[i][k] * mult.coef[k][j];
                }
                temp.coef[i][j] = som;
            }
        }
        return temp;
    }
    
    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        for (int i = 0; i < Matrix4.SIZE; ++i) {
            for (int j = 0; j < Matrix4.SIZE; ++j) {
                str.append(String.valueOf(this.coef[i][j]) + " ");
            }
            str.append(" | ");
        }
        return str.toString();
    }
    
    public FloatBuffer toFloatBuffer() {
        final FloatBuffer temp = FloatBuffer.allocate(Matrix4.SIZE * Matrix4.SIZE);
        temp.put(0, (float)this.coef[0][0]);
        temp.put(1, (float)this.coef[1][0]);
        temp.put(2, (float)this.coef[2][0]);
        temp.put(3, (float)this.coef[3][0]);
        temp.put(4, (float)this.coef[0][1]);
        temp.put(5, (float)this.coef[1][1]);
        temp.put(6, (float)this.coef[2][1]);
        temp.put(7, (float)this.coef[3][1]);
        temp.put(8, (float)this.coef[0][2]);
        temp.put(9, (float)this.coef[1][2]);
        temp.put(10, (float)this.coef[2][2]);
        temp.put(11, (float)this.coef[3][2]);
        temp.put(12, (float)this.coef[0][3]);
        temp.put(13, (float)this.coef[1][3]);
        temp.put(14, (float)this.coef[2][3]);
        temp.put(15, (float)this.coef[3][3]);
        return temp;
    }
}
