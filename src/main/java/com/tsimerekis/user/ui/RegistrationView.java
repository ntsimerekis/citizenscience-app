package com.tsimerekis.user.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    public RegistrationView(@Autowired RegistrationFormBinder registrationFormBinder) {
        RegistrationForm registrationForm = new RegistrationForm();
        // Center the RegistrationForm
        setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

        add(registrationForm);

        registrationFormBinder.setRegistrationForm(registrationForm);

        registrationFormBinder.addBindingAndValidation();
    }
}
