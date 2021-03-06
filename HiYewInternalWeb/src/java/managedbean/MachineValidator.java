/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.mail.internet.InternetAddress;
import session.stateless.HiYewSystemBeanLocal;
import java.util.regex.*;
import session.stateless.MachineSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "machineValidator")
@RequestScoped
public class MachineValidator implements Validator {
    @EJB
    private MachineSystemBeanLocal machineSystemBean;



    /**
     * Creates a new instance of machineValidator
     */
    public MachineValidator() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
    }

    public void validateNumeric(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String numeric = (String) submittedAndConvertedValue;

        if (numeric == null || numeric.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        try {
            Integer i = Integer.parseInt(numeric);
            if (numeric.substring(0, 1).equals("8") || numeric.substring(0, 1).equals("9") || numeric.substring(0, 1).equals("6")) {
            } else {
                throw new ValidatorException(new FacesMessage("Contact number is invalid!"));
            }
            if (!(numeric.length() == 8)) {
                throw new ValidatorException(new FacesMessage("Contact number is invalid!"));
            }

        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("Contzct number is invalid!"));
        }

    }

    public void validateMachineName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (machineSystemBean.existMachineName(username)) {
            throw new ValidatorException(new FacesMessage("Machine Name already in use, choose another."));
        }
    }

    public void validateTime(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String numeric = (String) submittedAndConvertedValue;

        if (numeric == null || numeric.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        try {
            if (!(numeric.length() == 4)) {
                throw new ValidatorException(new FacesMessage("Time is invalid."));
            }

            String first2 = numeric.substring(0, 2);
            String last2 = numeric.substring(2, 4);

            Integer j = Integer.parseInt(first2);
            if (j > 23) {
                throw new ValidatorException(new FacesMessage("Time is invalid."));
            }

            if (Integer.parseInt(last2) > 59) {
                throw new ValidatorException(new FacesMessage("Time is invalid."));
            }
            Integer i = Integer.parseInt(numeric);

        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("Time is invalid."));
        }

    }

    public void validateMachineExpiry(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String name = (String) submittedAndConvertedValue;

        if (name == null || name.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (machineSystemBean.notExistMachine(name)) {
            throw new ValidatorException(new FacesMessage("Machine not existent. Please check."));
        }
    }

}
