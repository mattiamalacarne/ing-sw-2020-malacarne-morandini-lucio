package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;

public class DialogContent extends JPanel
{
    protected SetupDialog parent;
    protected GUinterface gui;

    public DialogContent(GUinterface gui)
    {
        this.gui = gui;
    }

    public void setParent(SetupDialog parent)
    {
        this.parent = parent;
    }
}
