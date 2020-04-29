package it.polimi.ingsw.psp12.utils;

/**
 * Generic Observer class
 * @param <T> massage sent between observers and observable classes
 * @author Michele Lucio
 */
public interface Observer<T> {

    void update(Object sender, T message);

}
