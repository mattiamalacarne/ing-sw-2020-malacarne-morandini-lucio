package it.polimi.ingsw.psp12.model.cards;

import java.util.List;

/**
 * Class that represent a card loaded from the XML file "Cards.xml"
 * @author Michele Lucio
 */
public class LoadedCard {

    private String name;

    private String shortDescription;

    private String description;

    private List<Integer> powers;

    private String image;

    public LoadedCard() {
    }

    public LoadedCard(String name, String shortDescription, String description, List<Integer> powers, String image) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.powers = powers;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getPowers() {
        return powers;
    }

    public void setPowers(List<Integer> powers) {
        this.powers = powers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
