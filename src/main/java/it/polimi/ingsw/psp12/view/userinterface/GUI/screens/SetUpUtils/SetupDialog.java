package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;


import javax.swing.*;

public class SetupDialog extends JDialog
{
    private SetupDialog me;
    public SetupDialog(JFrame main,DialogContent content, String title)
    {
        me = this;
        content.setParent(me);
        this.add(content);
        this.setSize(240,180);
        this.setResizable(false);
        this.setTitle(title);
        this.setLocationRelativeTo(main);

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setModal(true);
        this.setVisible(true);

    }

    public void close()
    {
        System.out.println("Provo a chiudere");
        me.dispose();
    }
}
