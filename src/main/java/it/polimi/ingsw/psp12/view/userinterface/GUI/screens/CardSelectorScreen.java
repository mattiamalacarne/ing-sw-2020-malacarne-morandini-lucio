package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardsUtils.CardListDisplayer;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardsUtils.SelectedCardPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class is used in the GUI for choose the cardset or the player card
 * @author Mattia Malacarne
 */

public class CardSelectorScreen extends Screen
{

    /** Display more info about the selected card **/
    private SelectedCardPanel selectedCardPanel;

    private JLabel background;

    /**
     * Create the screen with the list of cards, for every screen the user can choose only one card
     * @param gui
     * @param cards
     */
    public CardSelectorScreen(GUinterface gui, List<Card> cards) {
        super(gui);
        this.setPreferredSize(new Dimension(screenX, screenY));

        this.setLayout(new BorderLayout());

        JPanel cardList = new JPanel();
        cardList.setLayout(new GridLayout(2,5));

        // Add cards to the screen
        for (Card card: cards)
        {
            cardList.add(new CardListDisplayer(card, this));
        }

        selectedCardPanel = new SelectedCardPanel(cards.get(0), this);

        this.add(cardList, BorderLayout.CENTER);
        this.add(selectedCardPanel, BorderLayout.EAST);
        this.setVisible(true);
    }

    /**
     * Display card image and description on the right
     * @param card
     */
    public void updateConfirmPanel(Card card)
    {
        selectedCardPanel.updateMe(card);
    }

    /**
     * Send decsion to server
     * @param card
     */
    public void setCard(Card card)
    {

        gui.sendCardToServer(card);
        try {
            gui.loadNewStatusScreen(GUIStatus.WAIT_CARD_SELECTION, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }
}
