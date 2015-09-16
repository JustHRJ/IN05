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
import session.stateful.HiYewSystemServerRemote;

/**
 *
 * @author JustHRJ
 */
@Named(value = "registrationValidator")
@RequestScoped
public class registrationValidator implements Validator {

    @EJB
    private HiYewSystemServerRemote hiYewSystemBean;

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

    public void validateN(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String username = (String) submittedAndConvertedValue;

        if (username == null || username.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.existEmployeeNumber(username)) {
            throw new ValidatorException(new FacesMessage("Employee Number already in use, choose another"));
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

    public void validateExpiredName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String name = (String) submittedAndConvertedValue;

        if (name == null || name.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        if (hiYewSystemBean.notExistExpiredName(name)) {
            throw new ValidatorException(new FacesMessage("Employee not existent. Please check"));
        }
    }

}
