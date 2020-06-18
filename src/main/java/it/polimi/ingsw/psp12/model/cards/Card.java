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

    private final String image;

    /**
     * Constructor for cards loaded from file
     * @param name name of the card
     * @param shortDesc short description
     * @param desc full description
     * @param powers powers provided by the card
     * @param image address to the image resource
     */
    public Card(String name, String shortDesc, String desc, List<Integer> powers, String image) {
        this.name = name;
        this.shortDescription = shortDesc;
        this.description = desc;
        this.powers = new ArrayList<>(powers);
        this.image = image;
    }

    /**
     * Constructor for the default card without powers
     */
    private Card() {
        this.name = "No cards";
        this.shortDescription = "Play without powers";
        this.description = "Play the game without using god powers";
        this.powers = new ArrayList<>();
        this.image = "/cards/NoPower.png";
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

    /**
     * Returns the the string address to the image resource of the card
     * @return address to the image resource of the card
     */
    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return name.equals(card.name);
    }
}
