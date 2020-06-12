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
        cardImage = getCardImage(null);
        this.setBounds(0,(int) winSize.getHeight() - cardImage.getIconHeight(), cardImage.getIconWidth(),cardImage.getIconHeight());
        cardDisplay = new JLabel(cardImage);

        this.add(cardDisplay);
        this.setVisible(true);
    }

    /**
     * Load the Image for display the card in the GameScreen
     * @param cardPath
     * @return
     */
    private ImageIcon getCardImage(String cardPath)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/cards/Apollo.png"));
        int scaleFactor = 4;
        Image scaled = icon.getImage().getScaledInstance(icon.getIconWidth()/scaleFactor, icon.getIconHeight()/scaleFactor, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

