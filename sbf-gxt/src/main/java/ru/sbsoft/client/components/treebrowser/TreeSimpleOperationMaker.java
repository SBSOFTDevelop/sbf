package ru.sbsoft.client.components.treebrowser;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.AbstractParamFormFactory;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.operation.AbstractOperation;
import ru.sbsoft.client.components.operation.BaseOperationParamForm;
import ru.sbsoft.client.components.operation.CompleteHandler;
import ru.sbsoft.client.components.operation.TypedOperation;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author sokolov
 */
public class TreeSimpleOperationMaker extends TreeGridOperationMaker {
    
    private IParamFormFactory paramFormFactory;
    private final Boolean reloadOnComplete;
    private final List<CompleteHandler> completeHandlers = new ArrayList<CompleteHandler>();


    public TreeSimpleOperationMaker(OperationType type, AbstractTreeGrid grid, IParamFormFactory paramFormFactory) {
        this(type, grid, paramFormFactory, null);
    }
    
    public TreeSimpleOperationMaker(OperationType type, AbstractTreeGrid grid, IParamFormFactory paramFormFactory, Boolean reloadOnComplete) {
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
    
    public TreeSimpleOperationMaker(OperationType type, AbstractTreeGrid grid) {
        this(type, grid, (IParamFormFactory)null, (Boolean)null);
    }
    
    public TreeSimpleOperationMaker(OperationType type, AbstractTreeGrid grid, boolean reloadOnComplete) {
        this(type, grid, (IParamFormFactory)null, reloadOnComplete);
    }

    public TreeSimpleOperationMaker(OperationType type, IParamFormFactory paramFormFactory) {
        this(type, (AbstractTreeGrid)null, paramFormFactory, null);
    }
    
    public TreeSimpleOperationMaker(OperationType type) {
        this(type, (IParamFormFactory)null);
    }

    public TreeSimpleOperationMaker setParamFormFactory(IParamFormFactory paramFormFactory) {
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
            final TreeGridOperation operation = new TreeGridOperation(getGrid(), getType());
            if(reloadOnComplete != null){
                operation.setReloadOnComplete(reloadOnComplete);
            }
            for(CompleteHandler h : completeHandlers){
                operation.addCompleteHandler(h);
            }
            if (paramForm != null) {
//                if(paramForm instanceof IParentGridAware){
//                    ((IParentGridAware)paramForm).setParentGrid(getGrid());
//                }
                paramForm.setHeading(I18n.get(getType().getTitle()));
                operation.setParamWindow(paramForm);
            }
            
            return operation;

        } else {
            return TypedOperation.create(getType(), paramForm);
        }
    }
}
