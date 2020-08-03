package com.karankumar.bookproject.ui.components;

import com.karankumar.bookproject.ui.components.PaperToggle;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;


/**
 * A click event to be used for the PaperToggle listener
 */
@DomEvent("click")
public class PaperToggleClickEvent extends ComponentEvent<PaperToggle> {

    private int x, y;

    public PaperToggleClickEvent(PaperToggle source,
                      boolean fromClient,
                      @EventData("event.offsetX") int x,
                      @EventData("event.offsetY") int y) {
        super(source, fromClient);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
