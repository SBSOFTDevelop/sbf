package ru.sbsoft.client.components.operation;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.client.components.form.AbstractParamFormFactory;
import ru.sbsoft.client.components.form.IParentGridAware;
import ru.sbsoft.client.components.form.IParamFormFactory;

/**
 * Универсальная фабрика объектов операций. 
 * Может создавать как табличные операции, так и операции не связанные с таблицами.
 * Для операций, требующих задания параметров, использует фабрику формы параметров {@link IParamFormFactory}, передаваемую в конструкторе.
 * Если создается табличная операция (т.е. таблица задана) и фабрика формы является так же и {@link IParentGridAware} таблица ей будет передана.
 * @author kiselev
 */
public class SimpleOperationMaker extends GridOperationMaker {

    private IParamFormFactory paramFormFactory;
    private final Boolean reloadOnComplete;
    private final List<CompleteHandler> completeHandlers = new ArrayList<CompleteHandler>();


    public SimpleOperationMaker(OperationType type, BaseGrid grid, IParamFormFactory paramFormFactory) {
        this(type, grid, paramFormFactory, null);
    }
    
    public SimpleOperationMaker(OperationType type, BaseGrid grid, IParamFormFactory paramFormFactory, Boolean reloadOnComplete) {
        super(type, grid);
        this.paramFormFactory = paramFormFactory;
        this.reloadOnComplete = reloadOnComplete;
        if(paramFormFactory instanceof AbstractParamFormFactory){
            AbstractParamFormFactory f = (AbstractParamFormFactory)paramFormFactory;
            if(f.getOperationType() == null){
                String h = f.getHeader();
                f.setOperationType(type);
                if(h != null){
                    f.setHeader(h);
                }
            }
        }
    }
    
    public SimpleOperationMaker(OperationType type, BaseGrid grid) {
        this(type, grid, (IParamFormFactory)null, (Boolean)null);
    }
    
    public SimpleOperationMaker(OperationType type, BaseGrid grid, boolean reloadOnComplete) {
        this(type, grid, (IParamFormFactory)null, reloadOnComplete);
    }

    public SimpleOperationMaker(OperationType type, IParamFormFactory paramFormFactory) {
        this(type, (BaseGrid)null, paramFormFactory, null);
    }
    
    public SimpleOperationMaker(OperationType type) {
        this(type, (IParamFormFactory)null);
    }

    public SimpleOperationMaker setParamFormFactory(IParamFormFactory paramFormFactory) {
        this.paramFormFactory = paramFormFactory;
        return this;
    }

    public void addCompleteHandler(CompleteHandler completeHandler) {
        if(!completeHandlers.contains(completeHandler)){
            completeHandlers.add(completeHandler);
        }
    }

    @Override
    public AbstractOperation createOperation() {
        BaseOperationParamForm paramForm = paramFormFactory != null ? paramFormFactory.createForm() : null;
        if (getGrid() != null) {
            final GridOperation operation = new GridOperation(getGrid(), getType());
            if(reloadOnComplete != null){
                operation.setReloadOnComplete(reloadOnComplete);
            }
            for(CompleteHandler h : completeHandlers){
                operation.addCompleteHandler(h);
            }
            if (paramForm != null) {
                if(paramForm instanceof IParentGridAware){
                    ((IParentGridAware)paramForm).setParentGrid(getGrid());
                }
                paramForm.setHeading(I18n.get(getType().getTitle()));
                operation.setParamWindow(paramForm);
            }
            
            return operation;

        } else {
            return TypedOperation.create(getType(), paramForm);
        }
    }

}
