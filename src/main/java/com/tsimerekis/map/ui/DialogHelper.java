package com.tsimerekis.map.ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;

public class DialogHelper {
    public static Dialog submissionDialog() {
        final Dialog dialog = new Dialog();
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING); // optional
        dialog.setHeaderTitle("Person details");
        dialog.setModal(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("520px");
        dialog.setMaxWidth("90vw");
        dialog.getElement().setAttribute("aria-label", "Person details");

        return dialog;
    }
}
