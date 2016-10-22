package util;

import java.io.Serializable;

/**
 * Generic class to link two value in one structure
 * @author alan
 *
 * @param <T> Type of first value.
 * @param <S> Type of second value.
 */
public class Couple<T, S> implements Serializable
{
    private static final long serialVersionUID = 1938707619108097639L;
    
    /**
     * First value.
     */
    private T valeur1;
    
    /**
     * Second value.
     */
    private S valeur2;
    
    /**
     * Default constructor.
     */
    public Couple() {
        this.valeur1 = null;
        this.valeur2 = null;
    }
    
    /**
     * Constructor with parameters.
     * @param val1 first value.
     * @param val2 second value.
     */
    public Couple(final T val1, final S val2) {
        this.valeur1 = val1;
        this.valeur2 = val2;
    }
    
    /**
     * Setter all values.
     * @param val1 first value to set.
     * @param val2 second value to set.
     */
    public void setValeur(final T val1, final S val2) {
        this.valeur1 = val1;
        this.valeur2 = val2;
    }
    
    /**
     * Getter first value.
     * @return the T value.
     */
    public T getValeur1() {
        return this.valeur1;
    }
    
    /**
     * Setter first value.
     * @param valeur1 the new value.
     */
    public void setValeur1(final T valeur1) {
        this.valeur1 = valeur1;
    }
    
    /**
     * Getter second value.
     * @return the S value.
     */
    public S getValeur2() {
        return this.valeur2;
    }
    
    /**
     * Setter second value.
     * @param valeur2 the new value.
     */
    public void setValeur2(final S valeur2) {
        this.valeur2 = valeur2;
    }
    
    /**
     * Compares the object with another.
     * @param c the other couple to compare with.
     * @return true if c is equals to the object.
     */
    public boolean equals(final Couple<T, S> c) {
        return this.valeur1.getClass().getName().equals(c.getValeur1().getClass().getName()) && this.valeur2.getClass().getName().equals(c.getValeur2().getClass().getName()) && this.valeur1.equals(c.getValeur1()) && this.valeur2.equals(c.getValeur2());
    }
    
    /**
     * Copy the object in another instance.
     */
    @Override
    public Couple<T, S> clone() {
        final Couple<T, S> clone = new Couple<T, S>();
        clone.setValeur(this.valeur1, this.valeur2);
        return clone;
    }
    
    @Override
    public String toString() {
        String result = "";
        result = String.valueOf(result) + this.valeur1.toString() + " " + this.valeur2.toString();
        return result;
    }
}
