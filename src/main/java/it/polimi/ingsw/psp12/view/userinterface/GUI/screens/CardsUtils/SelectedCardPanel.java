package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardsUtils;

import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardSelectorScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Render info about the selected Card
 * @author Mattia Malacarne
 */
public class SelectedCardPanel extends JPanel {

    /** This label render an image whene the user select a card **/
    private JLabel cardConfirmImage;

    /** Display on screen the card description **/
    private JLabel cardConfirmDescription;

    /** Reference to selector screen for use the GUI **/
    private CardSelectorScreen parent;

    /** Used for sent the Card to the server **/
    private JButton confirmCardBtn;

    private Card selectedCard;

    public SelectedCardPanel(Card card, CardSelectorScreen parent)
    {
        this.parent = parent;
        this.selectedCard = card;

        this.setLayout(new GridLayout(3,1));
        this.setPreferredSize(new Dimension(200, parent.getHeight() - 300));
        cardConfirmImage = new JLabel(loadCardImage(card.getImage()));
        cardConfirmDescription = new JLabel(card.getDescription());

        confirmCardBtn = new JButton("Confirm");
        confirmCardBtn.addActionListener(confirmCard);


        this.add(cardConfirmImage);
        this.add(cardConfirmDescription);
        this.add(confirmCardBtn);

        this.setVisible(true);
    }

    private ImageIcon loadCardImage(String image)
    {
        int scaleFactor = 4;
        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        Image scaled = icon.getImage().getScaledInstance(icon.getIconWidth()/scaleFactor, icon.getIconHeight()/scaleFactor, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void updateMe(Card card)
    {
        cardConfirmImage.setIcon(loadCardImage(card.getImage()));
        cardConfirmDescription.setText(card.getDescription());
        selectedCard = card;
    }

    ActionListener confirmCard = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parent.setCard(selectedCard);
        }
    };
}
