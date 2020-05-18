package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.power.cardPower.*;

/**
 * Class that decorates a power with the decorator indicated by the provided id
 * @author Luca Morandini
 */
public class PowerFactory {
    /**
     * Decorates a power with a decorator indicated by the provided id
     * @param power power to be decorated
     * @param powerId id of the decorator that has to decorate the power
     * @return decorated power
     */
    public static Power decorate(Power power, int powerId) {
        switch (powerId) {
            case 0: return new SwapWorkersDecorator(power);
            case 1: return new MoveAgainDecorator(power);
            case 2: return new NextCannotMoveUpDecorator(power);
            case 3: return new DomeAnyLevelDecorator(power);
            case 4: return new BuildAgainDecorator(power);
            case 5: return new BuildUpAgainDecorator(power);
            case 6: return new PushAwayWorkerDecorator(power);
            case 7: return new JumpToWinDecorator(power);
            case 8: return new BuildBeforeMoveAgainDecorator(power);
            default: return power;
        }
    }
}
