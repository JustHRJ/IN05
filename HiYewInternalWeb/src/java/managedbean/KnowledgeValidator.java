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
import session.stateless.KnowledgeSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "knowledgeValidator")
@RequestScoped
public class KnowledgeValidator implements Validator {

    @EJB
    private KnowledgeSystemBeanLocal knowledgeSystemBean;

    /**
     * Creates a new instance of KnowledgeValidator
     */
    public KnowledgeValidator() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
    }

    public void fillerID(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        String id = (String) submittedAndConvertedValue;

        if (id == null || id.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        boolean check = knowledgeSystemBean.checkFillerID(id);
        if (check) {
            throw new ValidatorException(new FacesMessage("id has been taken"));
        }

    }

      public void metalName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        String id = (String) submittedAndConvertedValue;

        if (id == null || id.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        boolean check = knowledgeSystemBean.checkMetalName(id);
        if (check) {
            throw new ValidatorException(new FacesMessage("Name has been taken"));
        }

    }


    
    
    public void fillerName(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {

        String id = (String) submittedAndConvertedValue;

        if (id == null || id.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }

        boolean check = knowledgeSystemBean.checkFillerName(id);
        if (check) {
            throw new ValidatorException(new FacesMessage("Name has been taken"));
        }

    }

}
