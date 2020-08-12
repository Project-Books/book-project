package com.karankumar.bookproject.ui.components.toggle;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * A click event to be used for the SwitchToggle listener
 */
@DomEvent("click")
public class SwitchToggleClickEvent extends ComponentEvent<SwitchToggle> {
    private int x;
    private int y;

    public SwitchToggleClickEvent(SwitchToggle source,
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
