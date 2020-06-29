package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.cards.Card;

import javax.swing.*;
import java.awt.*;

/**
 * This class draw a container for the player's card description
 * @author Luca Morandini
 */
public class CardDescriptionPanel extends JPanel {

    private JTextArea cardDesc;

    public CardDescriptionPanel(Card card, Dimension size) {
        this.setOpaque(false);

        cardDesc = new JTextArea(card.getDescription());
        cardDesc.setEditable(false);
        cardDesc.setFocusable(false);
        cardDesc.setOpaque(false);
        cardDesc.setWrapStyleWord(true);
        cardDesc.setLineWrap(true);
        cardDesc.setFont(new Font("dalek", Font.PLAIN, 14));
        cardDesc.setBounds(0, 0, 1000, 60);

        this.setBounds(220, (int) size.getHeight() - 60, 1000, 60);

        this.add(cardDesc);
        this.setVisible(card != null);
    }
}
