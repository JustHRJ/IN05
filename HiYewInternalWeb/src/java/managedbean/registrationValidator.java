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
/**
 *
 * @author JustHRJ
 */
@Named(value = "registrationValidator")
@RequestScoped
public class registrationValidator implements Validator {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;

    /**
     * Creates a new instance of registrationValidator
     */
    public registrationValidator() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.existEmployeeName(username)) {
            throw new ValidatorException(new FacesMessage("Employee Name already in use, choose another"));
        }
    }

    public void validateEmail(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        String email = (String) submittedAndConvertedValue;
         String emailregex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        
            if (!(email.isEmpty())) {
                if(email.matches(emailregex)){
                    
                }else{
                    throw new ValidatorException(new FacesMessage("Employee Name already in use, choose another"));
                }
            }
   

    }

    public void validatePay(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        try {
            Double numeric = (Double) submittedAndConvertedValue;

            if (numeric == null) {
                return; // Let required="true" or @NotNull handle it.
            }
            if (numeric < 500) {
                throw new ValidatorException(new FacesMessage("not enough pay"));
            }

        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("String is not numeric"));
        }

    }

    public void validatePayE(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        try {
            Double numeric = (Double) submittedAndConvertedValue;

            if (numeric == null) {
                return; // Let required="true" or @NotNull handle it.
            }
            if (numeric != 0) {
                if (numeric < 500) {
                    throw new ValidatorException(new FacesMessage("not enough pay"));
                }
            }
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("String is not numeric"));
        }

    }

    public void validateN(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        char first = username.charAt(0);
        char last = username.charAt(username.length() - 1);

        if (Character.isLetter(first) && Character.isLetter(last)) {
            try {
                Integer.parseInt(username.substring(1, username.length() - 1));
            } catch (Exception ex) {
                throw new ValidatorException(new FacesMessage("Middle is not numeric"));
            }
        } else {
            throw new ValidatorException(new FacesMessage("Employee Number already in use, choose another"));
        }

        if (hiYewSystemBean.existEmployeeNumber(username)) {
            throw new ValidatorException(new FacesMessage("Employee Number already in use, choose another"));
        }

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
                throw new ValidatorException(new FacesMessage("Contact number is invalid"));
            }
            if (!(numeric.length() == 8)) {
                throw new ValidatorException(new FacesMessage("Contact number is invalid"));
            }

        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("Contzct number is invalid."));
        }

    }

    public void validateNumericA(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String numeric = (String) submittedAndConvertedValue;

        if (numeric == null || numeric.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        try {
            Integer.parseInt(numeric);
            if (numeric.length() != 6) {
                throw new ValidatorException(new FacesMessage("numeric not postal code"));
            }

        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("String is not numeric"));
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
            throw new ValidatorException(new FacesMessage("Time is invalid"));
        }

    }

    public void validateU(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.existEmployeeUsername(username)) {
            throw new ValidatorException(new FacesMessage("Employee Number already in use, choose another"));
        }
    }

    public void validateMachineName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.existMachineName(username)) {
            throw new ValidatorException(new FacesMessage("Machine Name already in use, choose another"));
        }
    }

    public void validateExpiredName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String name = (String) submittedAndConvertedValue;

        if (name == null || name.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.notExistExpiredName(name)) {
            throw new ValidatorException(new FacesMessage("Employee not existent. Please check"));
        }
    }

    public void validateMachineExpiry(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String name = (String) submittedAndConvertedValue;

        if (name == null || name.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.notExistMachine(name)) {
            throw new ValidatorException(new FacesMessage("Machine not existent. Please check"));
        }
    }

}
