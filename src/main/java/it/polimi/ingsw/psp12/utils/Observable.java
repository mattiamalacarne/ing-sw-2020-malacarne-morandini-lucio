package it.polimi.ingsw.psp12.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Observable class
 * @param <T> massage sent between observers and observable classes
 * @author Michele Lucio
 */
public abstract class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer){
        observers.remove(observer);
    }

    public void notifyObservers(T message){
        for (Observer<T> observer: observers) {
            observer.update(this, message);
        }
    }

}
