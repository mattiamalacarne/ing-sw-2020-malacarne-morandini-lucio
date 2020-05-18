package it.polimi.ingsw.psp12.model.cards;

import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import it.polimi.ingsw.psp12.model.power.PowerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent a card that offer powers to the player
 * @author Luca Morandini
 */
public class Card implements Serializable {
    private final int id;

    private final String name;

    private final String shortDescription;

    private final String description;

    private final List<Integer> powers;

    public Card(int id, String name, String shortDesc, String desc, List<Integer> powers) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDesc;
        this.description = desc;
        this.powers = new ArrayList<>(powers);
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the power that the card offer to the player
     * @return power provided by the card
     */
    public Power getPower() {
        // create basic power
        Power power = new BasicPower();

        // decorate the basic power with the powers provided by the card
        for (int id : powers) {
            power = PowerFactory.decorate(power, id);
        }

        return power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id;
    }
}
