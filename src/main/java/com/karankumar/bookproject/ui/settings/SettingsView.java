package com.karankumar.bookproject.ui.settings;

import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
public class SettingsView extends HorizontalLayout {
    private static boolean darkModeOn = false;
    private static final String enable = "Enable dark mode";
    private static final String disable = "Disable dark mode";
    private static final Button toggle;

    static {
        toggle = new Button(enable, click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                darkModeOn = false;
            } else {
                themeList.add(Lumo.DARK);
                darkModeOn = true;
            }
            updateDarkModeButtonText();
        });
    }

    public SettingsView() {
        if (darkModeOn) {
            updateDarkModeButtonText();
        }

        VerticalLayout verticalLayout = new VerticalLayout(toggle);
        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private static void updateDarkModeButtonText() {
        if (toggle != null) {
            toggle.setText(darkModeOn ? disable : enable);
        }
    }
}
