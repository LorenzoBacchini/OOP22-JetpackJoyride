package it.unibo.jetpackjoyride.graphics.impl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jetpackjoyride.input.api.Input;
import it.unibo.jetpackjoyride.input.api.InputQueue;
import it.unibo.jetpackjoyride.input.impl.InputImpl;

/**
 * This is a class to model a generic input panel.
 * This class is used to manage the input of the user.
 * It implements the KeyListener interface.
 * 
 * @see KeyListener
 * @see JPanel
 * @see InputQueue
 * @see InputImpl
 * @see Input
 * @see Input.TypeInput
 * @see InputPanel
 * 
 * @author mattia.burreli@studio.unibo.it
 */
public final class InputPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = 22468L;
    private final transient InputQueue inputHandler;
    private boolean isPressed;
    private static final String SUPPVALUE = "EI_EXPOSE_REP2";
    private static final String SUPPJUSTIFICATION = "InputHandler are meant to be the same for GameEngine, view and world";

    /**
     * Public constructor of the InputPanel class.
     * 
     * @param inputHandler
     */
    @SuppressFBWarnings(value = SUPPVALUE, justification = SUPPJUSTIFICATION)
    public InputPanel(final InputQueue inputHandler) {
        this.isPressed = false;
        this.inputHandler = inputHandler;
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setSize(0, 0);
        this.setVisible(true);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == 32 && !this.isPressed) {
            this.isPressed = true;
            this.inputHandler.addInput(new InputImpl(Input.TypeInput.UP_PRESSED, "UP_PRESSED"));
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (e.getKeyCode() == 32 && this.isPressed) {
            this.isPressed = false;
            this.inputHandler.addInput(new InputImpl(Input.TypeInput.UP_RELEASED, "UP_RELEASED"));
        }
    }
}
