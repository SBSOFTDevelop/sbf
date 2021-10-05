package ru.sbsoft.shared;

import java.io.Serializable;

/**
 *
 * @author Sokoloff
 */
public class ContextVariables implements Serializable {

    static public enum TypeVariable {

        DATE,
        STRING,
        NUMBER
    }

    static public enum UserVariable {

        CURRENTDATE(TypeVariable.DATE, "Current date"),
        DATE_FROM(TypeVariable.DATE, "From date"),
        DATE_TILL(TypeVariable.DATE, "Till date");
        // -------------------------------------------------------------------
        private TypeVariable typeVar;
        private String descriptionVar;

        private UserVariable(final TypeVariable typeVar,
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
