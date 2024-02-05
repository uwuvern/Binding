package me.ashydev.bindable.bindable.ranged;

public interface IMinMax<T extends Number> {

    T getMin();
    T getMax();

    void setMin(T min);
    void setMax(T max);

    T getDefaultMin();
    T getDefaultMax();

    void setDefaultMin(T min);
    void setDefaultMax(T max);

    float normalized();

    boolean hasDefinedRange();


}
