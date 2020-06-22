package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.cards.Card;

import javax.swing.*;
import java.awt.*;

/**
 * This class draw a container for the player's card
 * @author Mattia Malacarne
 */

public class CardGamePanel extends JPanel {

    private ImageIcon cardImage;

    private JLabel cardDisplay;

    public CardGamePanel(Card myCard, Dimension winSize)
    {
        this.setOpaque(false);
        //cardDisplay = new JLabel(getCardImage(myCard.getImage()));
        cardImage = getCardImage(myCard);
        this.setBounds(0,(int) winSize.getHeight() - cardImage.getIconHeight(), cardImage.getIconWidth(),cardImage.getIconHeight());
        cardDisplay = new JLabel(cardImage);

        this.add(cardDisplay);
        if (myCard == null) this.setVisible(false);
        else this.setVisible(true);
    }

    /**
     * Load the Image for display the card in the GameScreen
     * @param card
     * @return
     */
    private ImageIcon getCardImage(Card card)
    {
        String image;
        if (card == null) image = "/cards/NoPower.png";
        else image = card.getImage();
        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        int scaleFactor = 4;
        Image scaled = icon.getImage().getScaledInstance(icon.getIconWidth()/scaleFactor, icon.getIconHeight()/scaleFactor, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

