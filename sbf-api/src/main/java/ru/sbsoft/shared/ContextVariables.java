package ru.sbsoft.shared;

import java.io.Serializable;

/**
 *
 * @author Sokoloff
 */
public class ContextVariables implements Serializable {

    public enum TypeVariable {

        DATE,
        STRING,
        NUMBER
    }

    public enum UserVariable {

        CURRENTDATE(TypeVariable.DATE, "Current date"),
        DATE_FROM(TypeVariable.DATE, "From date"),
        DATE_TILL(TypeVariable.DATE, "Till date");
        // -------------------------------------------------------------------
        private final TypeVariable typeVar;
        private final String descriptionVar;

        UserVariable(final TypeVariable typeVar,
                             final String descriptionVar) {
            this.typeVar = typeVar;
            this.descriptionVar = descriptionVar;
        }

        public String getDescriptionVar() {
            return descriptionVar;
        }

        public TypeVariable getTypeVar() {
            return typeVar;
        }
    }
}
