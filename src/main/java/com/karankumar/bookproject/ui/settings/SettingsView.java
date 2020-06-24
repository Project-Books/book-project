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
    public SettingsView() {
        final String enable = "Enable dark mode";
        final String disable = "Disable dark mode";

        Button toggle = new Button(enable, click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();


            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                click.getSource().setText(enable);
            } else {
                themeList.add(Lumo.DARK);
                click.getSource().setText(disable);
            }
        });

//        toggle.addClickListener(click -> {
//            clik
//        });

        VerticalLayout verticalLayout = new VerticalLayout(toggle);
        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }
}
