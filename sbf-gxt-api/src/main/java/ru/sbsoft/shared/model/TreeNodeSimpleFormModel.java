package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.FormModel;

/**
 *
 * @author Kiselev
 */
public class TreeNodeSimpleFormModel extends FormModel {

    private LookupInfoModel parentNode;
    private String nodeName;

    public LookupInfoModel getParentNode() {
        return parentNode;
    }

    public void setParentNode(LookupInfoModel parentNode) {
        this.parentNode = parentNode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
