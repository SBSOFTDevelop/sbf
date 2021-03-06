/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.tree;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.tree.Tree.CheckState;
import ru.sbsoft.svc.widget.core.client.tree.Tree.Joint;
import ru.sbsoft.svc.widget.core.client.tree.Tree.TreeNode;

public class TreeView<M> {

  public enum TreeViewRenderMode {
    /**
     * Render the entire node.
     */
    ALL,
    /**
     * Render the node body, used with buffered rendering.
     */
    BUFFER_BODY,
    /**
     * Render the node wrapper, used with buffered rendering.
     */
    BUFFER_WRAP
  }

  protected TreeNode<M> over;
  protected Tree<M, ?> tree;

  private int cacheSize = 20;
  private int cleanDelay = 500;
  private int scrollDelay = 1;

  @SuppressWarnings("unchecked")
  public void bind(Component component) {
    this.tree = (Tree<M, ?>) component;
  }

  public void collapse(TreeNode<M> node) {
    getContainer(node).getStyle().setDisplay(Display.NONE);
    tree.refresh(node.getModel());
  }

  public void expand(TreeNode<M> node) {
    getContainer(node).getStyle().setDisplay(Display.BLOCK);
    tree.refresh(node.getModel());
  }

  /**
   * Returns the cache size.
   * 
   * @return the cache size
   */
  public int getCacheSize() {
    return cacheSize;
  }

  public Element getCheckElement(TreeNode<M> node) {
    if (node.getCheckElement() == null) {
      node.setCheckElement(getElementContainer(node) != null
          ? tree.getAppearance().getCheckElement(getElementContainer(node)) : null);
    }
    return node.getCheckElement();
  }

  public int getCleanDelay() {
    return cleanDelay;
  }

  public Element getContainer(TreeNode<M> node) {
    if (node.getContainerElement() == null) {
      SafeHtmlBuilder sb = new SafeHtmlBuilder();
      tree.getAppearance().renderContainer(sb);
      node.setContainerElement(getElement(node).appendChild(XDOM.create(sb.toSafeHtml())));
    }
    return node.getContainerElement();
  }

  /**
   * Gets the rendered element, if any, for the given tree node object. This method will look up the dom element if it
   * has not yet been seen. The getElement() method for the node will return the same value as this method does after
   * it has been cached.
   * 
   * @param node the tree node to find an element for
   * @return the element that the node represents, or null if not yet rendered
   */
  public XElement getElement(TreeNode<M> node) {
    XElement elt = node.getElement().cast();
    if (elt == null) {
      if (tree.isAttached() && tree.getElement().getOffsetParent() != null) {
        elt = Document.get().getElementById(node.getDomId()).cast();
      } else {
        elt = tree.getElement().child("*#" + node.getDomId());
      }
      node.setElement(elt);
    }
    return elt;
  }

  public XElement getElementContainer(TreeNode<M> node) {
    if (node.getElementContainer() == null) {
      node.setElContainer(getElement(node) != null
          ? tree.getAppearance().getContainerElement(getElement(node)) : null);
    }
    return node.getElementContainer().cast();
  }

  public Element getIconElement(TreeNode<M> node) {
    if (node.getIconElement() == null) {
      node.setIconElement(getElementContainer(node) != null ? tree.getAppearance().getIconElement(getElementContainer(node))
          : null);
    }
    return node.getIconElement();
  }

  public Element getJointElement(TreeNode<M> node) {
    if (node.getJointElement() == null) {
      node.setJointElement(tree.getAppearance().getJointElement(getElementContainer(node)));
    }
    return node.getJointElement();
  }

  public int getScrollDelay() {
    return scrollDelay;
  }

  public SafeHtml getTemplate(M m, String id, SafeHtml html, ImageResource icon, boolean checkable, CheckState checked,
      Joint joint, int level, TreeViewRenderMode renderMode) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    tree.getAppearance().renderNode(sb, id, html, tree.getStyle(), icon, checkable, checked, joint, level, renderMode);
    return sb.toSafeHtml();
  }

  public Element getTextElement(TreeNode<M> node) {
    if (node.getTextElement() == null) {
      node.setTextElement(getElementContainer(node) != null ? tree.getAppearance().getTextElement(getElementContainer(node))
          : null);
    }
    return node.getTextElement();
  }

  public boolean isSelectableTarget(M m, Element target) {
    TreeNode<M> n = findNode(m);
    if (n == null) {
      return false;
    }
    XElement xtarget = target.cast();
    boolean joint = tree.getAppearance().isJointElement(xtarget);
    if (joint) {
      return false;
    }
    if (!joint && tree.isCheckable()) {
      return !tree.getAppearance().isCheckElement(xtarget);
    }
    return true;
  }

  public void onCheckChange(TreeNode<M> node, boolean checkable, CheckState state) {
    Element checkEl = (Element) getCheckElement(node);
    if (checkEl != null) {
      node.setCheckElement(tree.getAppearance().onCheckChange(getElement(node),
          checkEl.<XElement> cast(), checkable, state));
    }
  }

  public void onDropChange(TreeNode<M> node, boolean drop) {
    XElement e = tree.getView().getElementContainer(node);
    tree.getAppearance().onDropOver(e, drop);
  }

  public void onEvent(Event ce) {
    int type = ce.getTypeInt();
    switch (type) {
      case Event.ONMOUSEOVER:
        if (tree.isTrackMouseOver()) {
          onMouseOver(ce);
        }
        break;
      case Event.ONMOUSEOUT:
        if (tree.isTrackMouseOver()) {
          onMouseOut(ce);
        }
        break;
    }
  }

  public void onIconStyleChange(TreeNode<M> node, ImageResource icon) {
    Element iconEl = getIconElement(node);
    if (iconEl != null) {
      Element e;
      if (icon != null) {
        e = getImage(icon);
      } else {
        e = DOM.createSpan();
      }
      e.setClassName(iconEl.getClassName());
      node.setIconElement((Element) getElement(node).getFirstChild().insertBefore(e, iconEl));
      iconEl.removeFromParent();
    }
  }

  public void onJointChange(TreeNode<M> node, Joint joint) {
    Element jointEl = getJointElement(node);
    if (jointEl != null) {
      node.setJointElement(tree.getAppearance().onJointChange(getElement(node),
          jointEl.<XElement> cast(), joint, tree.getStyle()));
    }
  }

  public void onLoading(TreeNode<M> node) {
    onIconStyleChange(node, tree.getAppearance().loadingIcon());
  }

  public void onOverChange(TreeNode<M> node, boolean over) {
    tree.getAppearance().onHover(getElementContainer(node).<XElement> cast(), over);
  }

  public void onSelectChange(M model, boolean select) {
    if (select) {
      M p = tree.getStore().getParent(model);
      if (p != null) {
        tree.setExpanded(tree.getStore().getParent(model), true);
      }
    }
    TreeNode<M> node = findNode(model);
    if (node != null) {
      Element e = getElementContainer(node);
      if (e != null) {
        tree.getAppearance().onSelect(e.<XElement> cast(), select);
      }
      tree.moveFocus(node.getElement());
    }
  }

  public void onTextChange(TreeNode<M> node, SafeHtml html) {
    Element textEl = getTextElement(node);
    if (textEl != null) {
      if (html == SafeHtmlUtils.EMPTY_SAFE_HTML) {
        textEl.setInnerSafeHtml(Util.NBSP_SAFE_HTML);
      } else {
        textEl.setInnerSafeHtml(html);
      }
    }
  }

  public void setCacheSize(int cacheSize) {
    this.cacheSize = cacheSize;
  }

  public void setCleanDelay(int cleanDelay) {
    this.cleanDelay = cleanDelay;
  }

  public void setScrollDelay(int scrollDelay) {
    this.scrollDelay = scrollDelay;
  }

  protected TreeNode<M> findNode(M m) {
    return tree.findNode(m);
  }

  protected int getCalculatedRowHeight() {
    return 21;
  }

  protected int getIndenting(TreeNode<M> node) {
    return 18;
  }

  protected void onMouseOut(NativeEvent ce) {
    if (over != null) {
      onOverChange(over, false);
      over = null;
    }
  }

  protected void onMouseOver(NativeEvent ne) {
    TreeNode<M> node = getNode((Element) ne.getEventTarget().cast());
    if (node != null) {
      if (over != node) {
        onMouseOut(ne);
        over = node;
        onOverChange(over, true);
      }
    }
  }

  private Element getImage(ImageResource ir) {
    return AbstractImagePrototype.create(ir).createElement();
  }

  protected TreeNode<M> getNode(Element target) {
    return tree.findNode(target);
  }

}
