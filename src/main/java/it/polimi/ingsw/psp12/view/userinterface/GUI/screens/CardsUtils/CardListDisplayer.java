package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardsUtils;

import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.CardSelectorScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Display a single card in CardSelectorScreen
 * @author Mattia Malacarne
 */
public class CardListDisplayer extends JPanel
{
    private JButton cardImage;
    private Card card;
    private ImageIcon cardIcon;

    private CardSelectorScreen parent;

    public CardListDisplayer(Card card, CardSelectorScreen parent)
    {
        this.setLayout(null);
        this.card = card;

        this.parent = parent;

        cardIcon = loadCardImage(card.getImage());
        cardImage = new JButton(cardIcon);
        cardImage.setBorderPainted(false);
        cardImage.addActionListener(cardInfoDisplay);
        cardImage.setOpaque(false);
        cardImage.setBounds(0,0,cardIcon.getIconWidth(),cardIcon.getIconHeight());

        this.add(cardImage);
        this.setVisible(true);
    }

    public ImageIcon getImage()
    {
        return this.cardIcon;
    }

    public String getDesc()
    {
        return this.card.getDescription();
    }

    /**
     * Load the card image to render om screen
     * @param image
     * @return
     */
    private ImageIcon loadCardImage(String image)
    {
        int scaleFactor = 6;
        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        Image scaled = icon.getImage().getScaledInstance(icon.getIconWidth()/scaleFactor, icon.getIconHeight()/scaleFactor, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    ActionListener cardInfoDisplay = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parent.updateConfirmPanel(card);
        }
    };
}
