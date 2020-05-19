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
    /**
     * Default card that represent the basic game without god powers
     */
    private static final Card noPowers = new Card();

    /**
     * Getter for the default card without powers
     * @return no powers card
     */
    public static Card getNoPowers() {
        return noPowers;
    }

    private final String name;

    private final String shortDescription;

    private final String description;

    private final List<Integer> powers;

    /**
     * Constructor for cards loaded from file
     * @param name name of the card
     * @param shortDesc short description
     * @param desc full description
     * @param powers powers provided by the card
     */
    public Card(String name, String shortDesc, String desc, List<Integer> powers) {
        this.name = name;
        this.shortDescription = shortDesc;
        this.description = desc;
        this.powers = new ArrayList<>(powers);
    }

    /**
     * Constructor for the default card without powers
     */
    private Card() {
        this.name = "No cards";
        this.shortDescription = "Play without powers";
        this.description = "Play the game without using god powers";
        this.powers = new ArrayList<>();
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
        return name.equals(card.name);
    }
}
