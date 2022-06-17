package ru.sbsoft.client.components;

public class DefaultContainerHolder
        implements IHasParentElement {

    final SimpleHolder<IElementContainer> holder = new SimpleHolder<IElementContainer>();

    @Override
    public void setParentElement(IElementContainer parent) {
        holder.setItem(parent);
    }

    @Override
    public IElementContainer getParentElement() {
        return holder.getItem();
    }

}
