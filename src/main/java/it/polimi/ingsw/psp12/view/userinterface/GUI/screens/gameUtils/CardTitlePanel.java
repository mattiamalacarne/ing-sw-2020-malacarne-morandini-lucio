package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.cards.Card;

import javax.swing.*;
import java.awt.*;

/**
 * This class draw a container for the player's card title
 * @author Luca Morandini
 */
public class CardTitlePanel extends JPanel {

    private JLabel cardShortDesc;

    public CardTitlePanel(Card card, Dimension size) {
        this.setOpaque(false);

        cardShortDesc = new JLabel(card.getShortDescription());
        cardShortDesc.setFont(new Font("dalek", Font.PLAIN, 20));

        this.setBounds(220, (int) size.getHeight() - 98, 300, 50);

        this.add(cardShortDesc);
        this.setVisible(card != null);
    }
}
