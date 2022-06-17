package ru.sbsoft.svc.themebuilder.base.rebind;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.SupportsGeneratorResultCaching;

public class DetailsResourceGenerator
extends AbstractResourceGenerator
implements SupportsGeneratorResultCaching {
    public String createAssignment(final TreeLogger logger, final ResourceContext context, final JMethod method) throws UnableToCompleteException {
        return String.format("%1$s.create(%2$s.class)", GWT.class.getName(), method.getReturnType().getQualifiedSourceName());
    }
}





