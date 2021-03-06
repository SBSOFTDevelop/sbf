/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.core.client.dom.DomHelper;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.gestures.ScrollGestureRecognizer;
import ru.sbsoft.svc.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import ru.sbsoft.svc.core.client.gestures.TapGestureRecognizer;
import ru.sbsoft.svc.core.client.gestures.TouchData;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.util.DelayedTask;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.core.shared.FastMap;
import ru.sbsoft.svc.core.shared.event.GroupingHandlerRegistration;
import ru.sbsoft.svc.data.shared.IconProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent.StoreAddHandler;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent.StoreClearHandler;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent.StoreSortHandler;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
import ru.sbsoft.svc.data.shared.event.TreeStoreRemoveEvent;
import ru.sbsoft.svc.data.shared.loader.TreeLoader;
import ru.sbsoft.svc.widget.core.client.CheckProvider;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.BeforeCheckChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeCheckChangeEvent.BeforeCheckChangeHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeCheckChangeEvent.HasBeforeCheckChangeHandlers;
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseItemEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseItemEvent.BeforeCollapseItemHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseItemEvent.HasBeforeCollapseItemHandlers;
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandItemEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandItemEvent.BeforeExpandItemHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandItemEvent.HasBeforeExpandItemHandlers;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent.HasCheckChangeHandlers;
import ru.sbsoft.svc.widget.core.client.event.CheckChangedEvent;
import ru.sbsoft.svc.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent.HasCollapseItemHandlers;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent.HasExpandItemHandlers;
import ru.sbsoft.svc.widget.core.client.event.XEvent;
import ru.sbsoft.svc.widget.core.client.tree.TreeView.TreeViewRenderMode;

/**
 * A {@link Tree} provides support for displaying hierarchical data. The tree gets its data from a {@link TreeStore}.
 * Each model in the store is rendered as an item in the tree. Any updates to the store are automatically pushed to the
 * tree.
 * <p/>
 * In SVC, {@link ModelKeyProvider}s and {@link ValueProvider}s provide the interface between your data model,
 * the tree store and the tree. This enables a tree to work with data of any object type. The tree uses a value
 * provider, passed to the constructor, to get the value to display for each model in the tree.
 * <p/>
 * You can provide your own implementation of these interfaces, or you can use a Sbsoft supplied generator to create
 * them for you automatically. A generator runs at compile time to create a Java class that is compiled to JavaScript.
 * The Sbsoft supplied generator can create classes for interfaces that extend the {@link PropertyAccess} interface. The
 * generator transparently creates the class at compile time and the {@link GWT#create(Class)} method returns an
 * instance of that class at run time. The generated class is managed by GWT and SVC and you generally do not need to
 * worry about what the class is called, where it is located, or other similar details.
 * <p/>
 * To customize the appearance of the item in the tree, provide a cell implementation using {@link Tree#setCell(Cell)}.
 * <p/>
 * The following code snippet illustrates the creation of a simple tree with local data for test purposes. For more
 * practical examples that show how to load data from remote sources, see the Async Tree and Async Json Tree examples in
 * the online Explorer demo.
 * </p>
 * 
 * <pre>{@code 
    // Generate the key provider and value provider for the Data class
    DataProperties dp = GWT.create(DataProperties.class);

    // Create the store that the contains the data to display in the tree
    TreeStore<Data> s = new TreeStore<Test.Data>(dp.key());
    
    Data r1 = new Data("Parent 1", "value1");
    s.add(r1);
    s.add(r1, new Data("Child 1.1", "value2"));
    s.add(r1, new Data("Child 1.2", "value3"));

    Data r2 = new Data("Parent 2", "value4");
    s.add(r2);
    s.add(r2, new Data("Child 2.1", "value5"));
    s.add(r2, new Data("Child 2.2", "value6"));

    // Create the tree using the store and value provider for the name field
    Tree<Data, String> t = new Tree<Data, String>(s, dp.name());

    // Add the tree to a container
    RootPanel.get().add(t);
 * }</pre>
 * <p/>
 * To use the Sbsoft supplied generator to create model key providers and value providers, extend the
 * <code>PropertyAccess</code> interface, parameterized with the type of data you want to access (as shown below) and
 * invoke the <code>GWT.create</code> method on its <code>class</code> member (as shown in the code snippet above). This
 * creates an instance of the class that can be used to initialize the tree and tree store. In the following code
 * snippet we define a new interface called <code>DataProperties</code> that extends the <code>PropertyAccess</code>
 * interface and is parameterized with <code>Data</code>, a Plain Old Java Object (POJO).
 * <p/>
 * 
 * <pre>
  public interface DataProperties extends PropertyAccess<Data> {
    &#64;Path("name")
    ModelKeyProvider<Data> key();
    ValueProvider&lt;Data, String> name();
    ValueProvider&lt;Data, String> value();
  }

  public class Data {
    private String name;
    private String value;

    public Data(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }
    public String getName() {
      return name;
    }
    public String getValue() {
      return value;
    }
    public void setName(String name) {
      this.name = name;
    }
    public void setValue(String value) {
      this.value = value;
    }
  }
 * </pre>
 * <p/>
 * To enable check box support for a tree, add the following:
 * <p/>
 * 
 * <pre>
    t.setCheckable(true);
    t.setCheckStyle(CheckCascade.CHILDREN);
 * </pre>
 * See {@link CheckCascade} for additional check box styles.
 * <p/>
 * To save and restore the expand / collapse state of tree items, add the following (must be after the tree is added to
 * the container). This works with both local and asynchronous loading of tree items.
 * <p/>
 * 
 * <pre>
    t.setStateId("TreeCodeSnippetTest");
    StateManager.get().setProvider(new CookieProvider("/", null, null, SVC.isSecure()));
    TreeStateHandler<Data> sh = new TreeStateHandler<Data>(t);
    sh.loadState();
 * </pre>
 * <p/>
 * 
 * @param <M> the model type
 * @param <C> the cell data type
 */
public class Tree<M, C> extends Component implements HasBeforeExpandItemHandlers<M>, HasExpandItemHandlers<M>,
    HasBeforeCollapseItemHandlers<M>, HasCollapseItemHandlers<M>, HasBeforeCheckChangeHandlers<M>,
    HasCheckChangeHandlers<M>, CheckProvider<M> {

  /**
   * Check cascade enum.
   */
  public enum CheckCascade {
    /**
     * Checks cascade to all child nodes.
     */
    CHILDREN,
    /**
     * Checks to not cascade.
     */
    NONE,
    /**
     * Checks cascade to all parent nodes.
     */
    PARENTS,

    /**
     * Tri-state check behavior.
     */
    TRI
  }

  /**
   * Check nodes enum.
   */
  public enum CheckNodes {
    /**
     * Check boxes for both leafs and parent nodes.
     */
    BOTH,
    /**
     * Check boxes for only leaf nodes.
     */
    LEAF,
    /**
     * Check boxes for only parent nodes.
     */
    PARENT
  }

  public enum CheckState {
    CHECKED, PARTIAL, UNCHECKED
  }

  /**
   * Joint enum.
   */
  public enum Joint {
    COLLAPSED(1), EXPANDED(2), NONE(0);

    private int value;

    private Joint(int value) {
      this.value = value;
    }

    public int value() {
      return value;
    }
  }

  public interface TreeAppearance {
    ImageResource closeNodeIcon();

    String elementSelector();

    XElement findIconElement(XElement target);

    XElement findJointElement(XElement target);

    XElement getCheckElement(XElement container);

    XElement getContainerElement(XElement node);

    XElement getIconElement(XElement container);

    XElement getJointElement(XElement container);

    XElement getTextElement(XElement container);

    boolean isCheckElement(XElement target);

    boolean isJointElement(XElement target);

    String itemSelector();

    ImageResource loadingIcon();

    XElement onCheckChange(XElement node, XElement checkElement, boolean checkable, CheckState state);

    void onDropOver(XElement node, boolean over);

    void onHover(XElement node, boolean over);

    XElement onJointChange(XElement node, XElement jointElement, Joint joint, TreeStyle ts);

    void onSelect(XElement node, boolean select);

    ImageResource openNodeIcon();

    void render(SafeHtmlBuilder sb);

    void renderContainer(SafeHtmlBuilder sb);

    void renderNode(SafeHtmlBuilder sb, String id, SafeHtml html, TreeStyle style, ImageResource icon,
        boolean checkable, CheckState checked, Joint joint, int level, TreeViewRenderMode renderMode);

    String textSelector();
  }

  /**
   * Maintains the internal state of nodes contained in the tree. Should not need to be referenced in typical usage.
   */
  public static class TreeNode<M> {

    protected CheckState checked = CheckState.UNCHECKED;
    protected Element element;
    protected final String modelId;
    protected final String domId;
    protected boolean leaf = true;

    private Element checkElement;
    private boolean childrenRendered;
    private Element containerElement;
    private Element elContainer;
    private boolean expand;
    private boolean expandDeep;
    private boolean expanded;

    private Element iconElement;
    private Element jointElement;
    private boolean loaded;
    private boolean loading;
    private M model;
    private Element textElement;

    protected TreeNode(String modelId, M m, String domId) {
      this.modelId = modelId;
      this.domId = domId;
      this.model = m;
    }

    public void clearElements() {
      jointElement = null;
      checkElement = null;
      iconElement = null;
      textElement = null;
    }

    public Element getCheckElement() {
      return checkElement;
    }

    public Element getContainerElement() {
      return containerElement;
    }

    public String getDomId() {
      return domId;
    }

    /**
     * Gets the root element of the tree node that this object represents. May return null if the tree hasn't looked up
     * the element yet, or if the node isn't rendered yet. Typically
     * {@link TreeView#getElement(ru.sbsoft.svc.widget.core.client.tree.Tree.TreeNode)} should be used instead.
     * 
     * @see TreeView#getElement(ru.sbsoft.svc.widget.core.client.tree.Tree.TreeNode)
     * @return an element if it is rendered and cached, null otherwise
     */
    public Element getElement() {
      return element;
    }

    public Element getElementContainer() {
      return elContainer;
    }

    public Element getIconElement() {
      return iconElement;
    }

    public Element getJointElement() {
      return jointElement;
    }

    public M getModel() {
      return model;
    }

    public String getModelId() {
      return modelId;
    }

    public Element getTextElement() {
      return textElement;
    }

    public boolean isChildrenRendered() {
      return childrenRendered;
    }

    public boolean isExpand() {
      return expand;
    }

    public boolean isExpandDeep() {
      return expandDeep;
    }

    public boolean isExpanded() {
      return expanded;
    }

    public boolean isLeaf() {
      return leaf;
    }

    public boolean isLoaded() {
      return loaded;
    }

    public boolean isLoading() {
      return loading;
    }

    public void setCheckElement(Element checkElement) {
      this.checkElement = checkElement;
    }

    public void setChildrenRendered(boolean childrenRendered) {
      this.childrenRendered = childrenRendered;
    }

    public void setContainerElement(Element containerElement) {
      this.containerElement = containerElement;
    }

    public void setElContainer(Element elContainer) {
      this.elContainer = elContainer;
    }

    /**
     * Sets the element to be used as the root of this node.
     * 
     * @param element the element to cache as the root of this tree node
     */
    public void setElement(Element element) {
      this.element = element;
    }

    public void setExpand(boolean expand) {
      this.expand = expand;
    }

    public void setExpandDeep(boolean expandDeep) {
      this.expandDeep = expandDeep;
    }

    public void setExpanded(boolean expanded) {
      this.expanded = expanded;
    }

    public void setIconElement(Element iconElement) {
      this.iconElement = iconElement;
    }

    public void setJointElement(Element jointElement) {
      this.jointElement = jointElement;
    }

    public void setLeaf(boolean leaf) {
      this.leaf = leaf;
    }

    public void setLoaded(boolean loaded) {
      this.loaded = loaded;
    }

    public void setLoading(boolean loading) {
      this.loading = loading;
    }

    public void setTextElement(Element textElement) {
      this.textElement = textElement;
    }
  }

  private class Handler implements StoreAddHandler<M>, StoreClearHandler<M>, StoreDataChangeHandler<M>,
      StoreFilterHandler<M>, StoreRemoveHandler<M>, StoreUpdateHandler<M>, StoreSortHandler<M> {

    @Override
    public void onAdd(StoreAddEvent<M> event) {
      Tree.this.onAdd(event);
    }

    @Override
    public void onClear(StoreClearEvent<M> event) {
      Tree.this.onClear(event);
    }

    @Override
    public void onDataChange(StoreDataChangeEvent<M> event) {
      Tree.this.onDataChanged(event);
    }

    @Override
    public void onFilter(StoreFilterEvent<M> event) {
      Tree.this.onFilter(event);
    }

    @Override
    public void onRemove(StoreRemoveEvent<M> event) {
      Tree.this.onRemove(event);
    }

    @Override
    public void onSort(StoreSortEvent<M> event) {
      Tree.this.onSort(event);
    }

    @Override
    public void onUpdate(StoreUpdateEvent<M> event) {
      Tree.this.onUpdate(event);
    }

  }

  private final TreeAppearance appearance;
  protected boolean filtering;

  protected XElement focusEl;
  protected final FocusImpl focusImpl = FocusImpl.getFocusImplForPanel();
  protected boolean focusConstrainScheduled = false;
  protected TreeLoader<M> loader;
  protected Map<String, TreeNode<M>> nodes = new FastMap<TreeNode<M>>();
  protected Map<String, TreeNode<M>> nodesByDom = new FastMap<TreeNode<M>>();

  protected TreeSelectionModel<M> sm;
  protected TreeStore<M> store;
  final protected ValueProvider<? super M, C> valueProvider;
  protected TreeView<M> view = new TreeView<M>();
  protected Element rootContainer;

  private boolean autoExpand;
  private boolean autoLoad;
  private boolean autoSelect;
  private boolean bufferRender = false;
  private boolean caching = true;
  private boolean cascade = true;
  private Cell<C> cell;
  private boolean checkable;
  private CheckNodes checkNodes = CheckNodes.BOTH;
  private CheckCascade checkStyle = CheckCascade.PARENTS;
  private boolean expandOnFilter = true;
  private GroupingHandlerRegistration storeHandlers = new GroupingHandlerRegistration();
  private IconProvider<M> iconProvider;
  private TreeStyle style = new TreeStyle();
  private boolean trackMouseOver = true;
  private DelayedTask updateTask, cleanTask;

  /**
   * Creates a new tree panel.
   * 
   * @param store the tree store
   */
  @UiConstructor
  public Tree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider) {
    this(store, valueProvider, (TreeAppearance) GWT.create(TreeAppearance.class));
  }

  public Tree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider, TreeAppearance appearance) {
    this.appearance = appearance;
    this.valueProvider = valueProvider;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

    ensureFocusElement();

    setStore(store);

    setSelectionModel(new TreeSelectionModel<M>());
    view.bind(this);

    addGestureRecognizer(new TapGestureRecognizer() {

      @Override
      protected void onTap(TouchData touchData) {
        Event event = touchData.getLastNativeEvent().cast();
        onClick(event);
        getSelectionModel().onMouseDown(event);
        getSelectionModel().onMouseClick(event);
        super.onTap(touchData);
      }
    });
    addGestureRecognizer(new ScrollGestureRecognizer(getElement(), ScrollDirection.BOTH));
  }

  @Override
  public HandlerRegistration addBeforeCheckChangeHandler(BeforeCheckChangeHandler<M> handler) {
    return addHandler(handler, BeforeCheckChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeCollapseHandler(BeforeCollapseItemHandler<M> handler) {
    return addHandler(handler, BeforeCollapseItemEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeExpandHandler(BeforeExpandItemHandler<M> handler) {
    return addHandler(handler, BeforeExpandItemEvent.getType());
  }

  @Override
  public HandlerRegistration addCheckChangedHandler(CheckChangedHandler<M> handler) {
    return addHandler(handler, CheckChangedEvent.getType());
  }

  @Override
  public HandlerRegistration addCheckChangeHandler(CheckChangeHandler<M> handler) {
    return addHandler(handler, CheckChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseItemHandler<M> handler) {
    return addHandler(handler, CollapseItemEvent.getType());
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandItemHandler<M> handler) {
    return addHandler(handler, ExpandItemEvent.getType());
  }

  /**
   * Collapses all nodes.
   */
  public void collapseAll() {
    for (M child : store.getRootItems()) {
      setExpanded(child, false, true);
    }
  }

  /**
   * Expands all nodes.
   */
  public void expandAll() {
    for (M child : store.getRootItems()) {
      setExpanded(child, true, true);
    }
  }

  /**
   * Returns the tree node for the given target.
   * 
   * @param target the target element
   * @return the tree node or null if no match
   */
  public TreeNode<M> findNode(Element target) {
    Element elem = target.<XElement> cast().findParentElement(appearance.itemSelector(), 10);
    if (elem != null) {
      TreeNode<M> node = nodesByDom.get(elem.getId());
      return node;
    }
    return null;
  }

  /**
   * Returns the tree node for the given model.
   * 
   * @param model the model
   * @return the tree node or null if no match
   */
  public TreeNode<M> findNode(M model) {
    if (store == null || model == null) return null;
    return nodes.get(generateModelId(model));
  }

  @Override
  public void focus() {
    focusImpl.focus(focusEl);
  }

  /**
   * Returns the tree appearance.
   * 
   * @return the tree appearance
   */
  public TreeAppearance getAppearance() {
    return appearance;
  }

  /**
   * Return the tree's cell.
   * 
   * @return the cell
   */
  public Cell<C> getCell() {
    return cell;
  }

  /**
   * Returns the models checked state.
   * 
   * @param model the model
   * @return the check state
   */
  public CheckState getChecked(M model) {
    TreeNode<M> node = findNode(model);
    if (node != null && isCheckable(node)) {
      return node.checked;
    }
    return CheckState.UNCHECKED;
  }

  /**
   * Returns the current checked selection. Only items that have been rendered will be returned in this list. Set
   * {@link #setAutoLoad(boolean)} to {@code true} to fully render the tree to receive all checked items in the tree.
   * 
   * @return the checked selection
   */
  @Override
  public List<M> getCheckedSelection() {
    List<M> checked = new ArrayList<M>();
    for (TreeNode<M> n : nodes.values()) {
      if (n.checked == CheckState.CHECKED) {
        checked.add(n.getModel());
      }
    }
    return checked;
  }

  /**
   * Returns the child nodes value which determines what node types have a check box. Only applies when check boxes have
   * been enabled ( {@link #setCheckable(boolean)}.
   * 
   * @return the child nodes value
   */
  public CheckNodes getCheckNodes() {
    return checkNodes;
  }

  /**
   * The check cascade style value which determines if check box changes cascade to parent and children.
   * 
   * @return the check cascade style
   */
  public CheckCascade getCheckStyle() {
    return checkStyle;
  }

  /**
   * Returns the model icon provider.
   * 
   * @return the icon provider
   */
  public IconProvider<M> getIconProvider() {
    return iconProvider;
  }

  /**
   * Returns the tree's selection model.
   * 
   * @return the selection model
   */
  public TreeSelectionModel<M> getSelectionModel() {
    return sm;
  }

  /**
   * Returns the tree's store.
   * 
   * @return the store
   */
  public TreeStore<M> getStore() {
    return store;
  }

  /**
   * Returns the tree style.
   * 
   * @return the tree style
   */
  public TreeStyle getStyle() {
    return style;
  }

  /**
   * Returns the tree's view.
   * 
   * @return the view
   */
  public TreeView<M> getView() {
    return view;
  }

  /**
   * Returns {@code true} if auto expand is enabled.
   * 
   * @return the auto expand state
   */
  public boolean isAutoExpand() {
    return autoExpand;
  }

  /**
   * Returns {@code true} if auto load is enabled.
   * 
   * @return the auto load state
   */
  public boolean isAutoLoad() {
    return autoLoad;
  }

  /**
   * Returns {@code true} if select on load is enabled.
   * 
   * @return the auto select state
   */
  public boolean isAutoSelect() {
    return autoSelect;
  }

  /**
   * Returns {@code true} if buffered rendering is enabled.
   * 
   * @return true for buffered rendering
   */
  public boolean isBufferedRender() {
    return bufferRender;
  }

  /**
   * Returns {@code true} when a loader is queried for it's children each time a node is expanded. Only applies when
   * using a loader with the tree store.
   * 
   * @return true if caching
   */
  public boolean isCaching() {
    return caching;
  }

  /**
   * Returns {@code true} if check boxes are enabled.
   * 
   * @return the check box state
   */
  public boolean isCheckable() {
    return checkable;
  }

  @Override
  public boolean isChecked(M model) {
    TreeNode<M> node = findNode(model);
    if (node != null && isCheckable(node)) {
      return node.checked == CheckState.CHECKED;
    }
    return false;
  }

  /**
   * Returns {@code true} if the model is expanded.
   * 
   * @param model the model
   * @return true if expanded
   */
  public boolean isExpanded(M model) {
    TreeNode<M> node = findNode(model);
    return node != null && node.expanded;
  }

  /**
   * Returns the if expand all and collapse all is enabled on filter changes.
   * 
   * @return the expand all collapse all state
   */
  public boolean isExpandOnFilter() {
    return expandOnFilter;
  }

  /**
   * Returns {@code true} if the model is a leaf node. The leaf state allows a tree item to specify if it has children
   * before the children have been realized.
   * 
   * @param model the model
   * @return the leaf state
   */
  public boolean isLeaf(M model) {
    return !hasChildren(model);
  }

  /**
   * Returns {@code true} if nodes are highlighted on mouse over.
   * 
   * @return true if enabled
   */
  public boolean isTrackMouseOver() {
    return trackMouseOver;
  }

  @Override
  public void onBrowserEvent(Event event) {
    if (cell != null) {
      CellWidgetImplHelper.onBrowserEvent(this, event);
    }

    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        onClick(event);
        break;
      case Event.ONDBLCLICK:
        onDoubleClick(event);
        break;
      case Event.ONSCROLL:
        onScroll(event);
        break;
      case Event.ONFOCUS:
        onFocus(event);
        break;
    }
    view.onEvent(event);

    handleEventForCell(event);
  }

  public void refresh(M model) {
    if (isOrWasAttached()) {
      TreeNode<M> node = findNode(model);
      if (node != null && view.getElement(node) != null) {
        view.onIconStyleChange(node, calculateIconStyle(model));
        view.onJointChange(node, calculateJoint(model));
        view.onTextChange(node, getCellContent(model));
        boolean checkable = isCheckable(node);
        if (checkable) {
          setChecked(node.getModel(), node.checked);
        }
      }
    }
  }

  /**
   * Scrolls the tree to ensure the given model is visible.
   * 
   * @param model the model to scroll into view
   */
  public void scrollIntoView(M model) {
    TreeNode<M> node = findNode(model);
    if (node != null) {
      XElement con = (XElement) node.getElementContainer();
      if (con != null) {
        con.scrollIntoView(getElement(), false);
        focusEl.setXY(con.getXY());
      }
    }
  }

  /**
   * If set to {@code true}, all non leaf nodes will be expanded automatically (defaults to {@code false}).
   * 
   * @param autoExpand the auto expand state to set.
   */
  public void setAutoExpand(boolean autoExpand) {
    this.autoExpand = autoExpand;
  }

  /**
   * Sets whether all children should automatically be loaded recursively (defaults to false). Useful when the tree must
   * be fully populated when initially rendered.
   * 
   * @param autoLoad {@code true} to auto load
   */
  public void setAutoLoad(boolean autoLoad) {
    this.autoLoad = autoLoad;
  }

  /**
   * True to select the first model after the store's data changes (defaults to {@code false}).
   * 
   * @param autoSelect {@code true} to auto select
   */
  public void setAutoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
  }

  /**
   * True to only render tree nodes that are in view (defaults to {@code false}). Set this to {@code true} when dealing
   * with very large trees.
   * 
   * @param bufferRender {@code true} to buffer render
   */
  public void setBufferedRender(boolean bufferRender) {
    this.bufferRender = bufferRender;
  }

  /**
   * Sets whether the children should be cached after first being retrieved from the store (defaults to {@code true}).
   * When {@code false}, a load request will be made each time a node is expanded.
   * 
   * @param caching the caching state
   */
  public void setCaching(boolean caching) {
    this.caching = caching;
  }

  /**
   * Sets the tree's cell.
   * 
   * @param cell the cell
   */
  public void setCell(Cell<C> cell) {
    this.cell = cell;

    if (cell != null) {
      CellWidgetImplHelper.sinkEvents(this, cell.getConsumedEvents());
    }
  }

  /**
   * Sets whether check boxes are used in the tree (defaults to {@code false}).
   * 
   * @param checkable {@code true} for check boxes
   */
  public void setCheckable(boolean checkable) {
    this.checkable = checkable;
  }

  /**
   * Sets the check state of the item. The checked state will only be set for nodes that have been rendered,
   * {@link #setAutoLoad(boolean)} can be used to render all children.
   * 
   * @param item the item
   * @param checked the check state
   */
  public void setChecked(M item, CheckState checked) {
    if (!checkable) return;
    TreeNode<M> node = findNode(item);
    if (node != null) {
      if (node.checked == checked) {
        return;
      }

      boolean leaf = isLeaf(item);
      if ((!leaf && checkNodes == CheckNodes.LEAF) || (leaf && checkNodes == CheckNodes.PARENT)) {
        return;
      }

      if (fireCancellableEvent(new BeforeCheckChangeEvent<M>(node.getModel(), node.checked))) {

        node.checked = checked;

        view.onCheckChange(node, checkable, checked);

        fireEvent(new CheckChangeEvent<M>(item, node.checked));
        fireEvent(new CheckChangedEvent<M>(getCheckedSelection()));

        if (cascade) {
          onCheckCascade(item, checked);
        }
      }
    }
  }

  @Override
  public void setCheckedSelection(List<M> selection) {
    for (M m : store.getAll()) {
      setChecked(m, selection != null && selection.contains(m) ? CheckState.CHECKED : CheckState.UNCHECKED);
    }
  }

  /**
   * Sets which tree items will display a check box (defaults to BOTH).
   * <p>
   * Valid values are:
   * <ul>
   * <li>BOTH - both nodes and leafs</li>
   * <li>PARENT - only nodes with children</li>
   * <li>LEAF - only leafs</li>
   * </ul>
   * 
   * @param checkNodes the child nodes value
   */
  public void setCheckNodes(CheckNodes checkNodes) {
    this.checkNodes = checkNodes;
    if (isOrWasAttached()) {
      for (M m : store.getAll()) {
        refresh(m);
      }
    }
  }

  /**
   * Sets the cascading behavior for check tree (defaults to PARENTS). When using CHILDREN, it is important to note that
   * the cascade will only be applied to rendered nodes. {@link #setAutoLoad(boolean)} can be used to fully render the
   * tree on render.
   * <p>
   * Valid values are:
   * <ul>
   * <li>NONE - no cascading</li>
   * <li>PARENTS - cascade to parents</li>
   * <li>CHILDREN - cascade to children</li>
   * </ul>
   * 
   * @param checkStyle the child style
   */
  public void setCheckStyle(CheckCascade checkStyle) {
    this.checkStyle = checkStyle;
  }

  /**
   * Sets the item's expand state.
   * 
   * @param model the model
   * @param expand {@code true} to expand
   */
  public void setExpanded(M model, boolean expand) {
    setExpanded(model, expand, false);
  }

  /**
   * Sets the item's expand state.
   * 
   * @param model the model
   * @param expand {@code true} to expand
   * @param deep {@code true} to expand all children recursively
   */
  public void setExpanded(M model, boolean expand, boolean deep) {
    if (expand) {
      // make item visible by expanding parents
      List<M> list = new ArrayList<M>();
      M p = model;
      while ((p = store.getParent(p)) != null) {
        TreeNode<M> n = findNode(p);
        if (n == null || !n.isExpanded()) {
          list.add(p);
        }
      }
      for (int i = list.size() - 1; i >= 0; i--) {
        M item = list.get(i);
        setExpanded(item, expand, false);
      }
    }
    TreeNode<M> node = findNode(model);
    if (node == null) {
      assert !expand;
      return;
    }
    if (!isAttached()) {
      node.setExpand(expand);
      return;
    }
    if (expand) {
      onExpand(model, node, deep);
    } else {
      onCollapse(model, node, deep);
    }
  }

  /**
   * Sets whether the tree should expand all and collapse all when filters are applied (defaults to {@code true}).
   * 
   * @param expandOnFilter {@code true} to expand and collapse on filter changes
   */
  public void setExpandOnFilter(boolean expandOnFilter) {
    this.expandOnFilter = expandOnFilter;
  }

  /**
   * Sets the tree's model icon provider which provides the icon style for each model.
   * 
   * @param iconProvider the icon provider
   */
  public void setIconProvider(IconProvider<M> iconProvider) {
    this.iconProvider = iconProvider;
  }

  /**
   * Sets the item's leaf state. The leaf state allows control of the expand icon before the children have been
   * realized.
   * 
   * @param model the model
   * @param leaf the leaf state
   */
  public void setLeaf(M model, boolean leaf) {
    TreeNode<M> t = findNode(model);
    if (t != null) {
      t.setLeaf(leaf);
      refresh(model);
    }
  }

  /**
   * Sets the tree loader.
   * 
   * @param loader the loader
   */
  public void setLoader(TreeLoader<M> loader) {
    this.loader = loader;
  }

  /**
   * Sets the tree's selection model.
   * 
   * @param sm the selection model
   */
  public void setSelectionModel(TreeSelectionModel<M> sm) {
    if (this.sm != null) {
      this.sm.bindTree(null);
    }
    this.sm = sm;
    if (sm != null) {
      sm.bindTree(this);
    }
  }

  /**
   * Assigns a new store to the tree. May be null, in which case the tree must not be attached to the dom. All selection
   * is lost when this takes place and items are re-rendered.
   * 
   * @param store the new store to bind to, or null to detach from the store
   */
  public void setStore(TreeStore<M> store) {
    if (this.store != null) {
      storeHandlers.removeHandler();
      if (isOrWasAttached()) {
        clear();
      }
    }

    this.store = store;

    if (this.store != null) {
      Handler handler = new Handler();
      storeHandlers.add(store.addStoreAddHandler(handler));
      storeHandlers.add(store.addStoreUpdateHandler(handler));
      storeHandlers.add(store.addStoreRemoveHandler(handler));
      storeHandlers.add(store.addStoreDataChangeHandler(handler));
      storeHandlers.add(store.addStoreClearHandler(handler));
      storeHandlers.add(store.addStoreFilterHandler(handler));
      storeHandlers.add(store.addStoreSortHandler(handler));

      if (getSelectionModel() != null) {
        getSelectionModel().bind(store);
      }
      if (isOrWasAttached()) {
        renderChildren(null);
        if (autoSelect) {
          getSelectionModel().select(0, false);
        }
      }
    } else {
      // no store, make sure we aren't attached
      if (isAttached()) {
        throw new IllegalStateException(
            "Tree must have a store when attached, either detach or pass in a non-null store");
      }
    }
  }

  /**
   * Sets the tree style.
   * 
   * @param style the tree style
   */
  public void setStyle(TreeStyle style) {
    this.style = style;
  }

  /**
   * True to highlight nodes when the mouse is over (defaults to {@code true}).
   * 
   * @param trackMouseOver {@code true} to highlight nodes on mouse over
   */
  public void setTrackMouseOver(boolean trackMouseOver) {
    this.trackMouseOver = trackMouseOver;
  }

  /**
   * Sets the tree's view. Only needs to be called when customizing the tree's presentation.
   * 
   * @param view the view
   */
  public void setView(TreeView<M> view) {
    this.view = view;
    view.bind(this);
  }

  /**
   * Toggles the model's expand state. If the model is not visible, does nothing.
   * 
   * @param model the model
   */
  public void toggle(M model) {
    TreeNode<M> node = findNode(model);
    if (node != null) {
      setExpanded(model, !node.isExpanded());
    }
  }

  protected ImageResource calculateIconStyle(M model) {
    ImageResource style = null;
    if (iconProvider != null) {
      ImageResource iconStyle = iconProvider.getIcon(model);
      if (iconStyle != null) {
        return iconStyle;
      }
    }
    TreeStyle ts = getStyle();
    if (!isLeaf(model)) {
      if (isExpanded(model)) {
        style = ts.getNodeOpenIcon() != null ? ts.getNodeOpenIcon() : appearance.openNodeIcon();
      } else {
        style = ts.getNodeCloseIcon() != null ? ts.getNodeCloseIcon() : appearance.closeNodeIcon();
      }
    } else {
      style = ts.getLeafIcon();
    }
    return style;
  }

  protected Joint calculateJoint(M model) {
    if (model == null) {
      return Joint.NONE;
    }
    TreeNode<M> node = findNode(model);
    return isLeaf(model) ? Joint.NONE : node.isExpanded() ? Joint.EXPANDED : Joint.COLLAPSED;
  }

  protected boolean cellConsumesEventType(Cell<?> cell, String eventType) {
    if (cell == null) {
      return false;
    }
    Set<String> consumedEvents = cell.getConsumedEvents();
    return consumedEvents != null && consumedEvents.contains(eventType);
  }

  protected void clean() {
    if (cleanTask == null) {
      cleanTask = new DelayedTask() {
        @Override
        public void onExecute() {
          doClean();
        }
      };
    }
    cleanTask.delay(view.getCleanDelay());
  }

  protected void cleanCollapsed(M parent) {
    List<M> list = store.getAllChildren(parent);
    for (M m : list) {
      TreeNode<M> node = findNode(m);
      if (node != null && node.element != null) {
        cleanNode(node);
      }
    }
  }

  protected void cleanNode(TreeNode<M> node) {
    if (node != null && node.element != null) {
      node.clearElements();
      view.getElement(node).getFirstChildElement().<XElement> cast().removeChildren();
    }
  }

  protected void clear() {
    if (isOrWasAttached()){
      getContainer(null).<XElement> cast().removeChildren();
      nodes.clear();
      nodesByDom.clear();
      if (isAttached()) {
        moveFocus(getContainer(null));
      }
    }
  }

  protected void moveFocus(Element selectedElem) {
    if (selectedElem == null) return;
    constrainFocusElement();
  }

  protected void constrainFocusElement() {
    if (!focusConstrainScheduled) {
      focusConstrainScheduled = true;
      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          focusConstrainScheduled = false;
          int scrollLeft = getElement().getScrollLeft();
          int scrollTop = getElement().getScrollTop();
          int left = getElement().getWidth(true) / 2 + scrollLeft;
          int top = getElement().getHeight(true) / 2 + scrollTop;
          focusEl.setLeftTop(left, top);
        }
      });
    }
  }

  protected void doClean() {
    int count = getVisibleRowCount();
    if (count > 0) {
      List<M> rows = getChildModel(store.getRootItems(), true);
      int[] vr = getVisibleRows(rows, count);
      vr[0] -= view.getCacheSize();
      vr[1] += view.getCacheSize();

      int i = 0;

      // if first is less than 0, all rows have been rendered
      // so lets clean the end...
      if (vr[0] <= 0) {
        i = vr[1] + 1;
      }
      for (int len = rows.size(); i < len; i++) {
        // if current row is outside of first and last and
        // has content, clean the node
        if (i < vr[0] || i > vr[1]) {
          cleanNode(findNode(rows.get(i)));
        }
      }
    }

  }

  protected void doUpdate() {
    int count = getVisibleRowCount();
    if (count > 0) {
      List<M> rootItems = store.getRootItems();
      List<M> visible = getChildModel(rootItems, true);
      int[] vr = getVisibleRows(visible, count);

      for (int i = vr[0]; i <= vr[1]; i++) {
        if (!isRowRendered(i, visible)) {
          M parent = store.getParent(visible.get(i));
          SafeHtml html = renderChild(parent, visible.get(i), store.getDepth(parent), TreeViewRenderMode.BUFFER_BODY);
          view.getElement(findNode(visible.get(i))).getFirstChildElement().setInnerSafeHtml(html);
        }
      }
      clean();
    }

  }

  protected void findChildren(M parent, List<M> list, boolean onlyVisible) {
    for (M child : store.getChildren(parent)) {
      list.add(child);
      if (!onlyVisible || findNode(child).isExpanded()) {
        findChildren(child, list, onlyVisible);
      }
    }
  }

  protected void fireEventToCell(Event event, String eventType, Element cellParent, final M m, Context context) {
    if (cell != null && cellConsumesEventType(cell, eventType)) {
      C value = getValue(m);
      cell.onBrowserEvent(context, cellParent, value, event, new ValueUpdater<C>() {
        @Override
        public void update(C value) {
          Tree.this.getStore().getRecord(m).addChange(valueProvider, value);
        }
      });
    }
  }

  protected String generateModelId(M m) {
    return store.getKeyProvider().getKey(m);
  }

  protected SafeHtml getCellContent(M model) {
    C value = getValue(model);
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    if (cell == null) {
      String text = null;
      if (value != null) {
        text = value.toString();
      }
      sb.append(Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text));
    } else {
      Context context = new Context(store.indexOf(model), 0, store.getKeyProvider().getKey(model));
      cell.render(context, value, sb);
    }
    return sb.toSafeHtml();
  }

  protected List<M> getChildModel(List<M> l, boolean onlyVisible) {
    List<M> list = new ArrayList<M>();
    for (M m : l) {
      list.add(m);
      if (!onlyVisible || findNode(m).isExpanded()) {
        findChildren(m, list, onlyVisible);
      }
    }
    return list;
  }

  protected Element getContainer(M model) {
    if (model == null) {
      return rootContainer;
    }
    TreeNode<M> node = findNode(model);
    if (node != null) {
      return view.getContainer(node);
    }
    return null;
  }

  protected Element getRootContainer() {
    // TODO we should be asking appearance for the root container element rather
    // than assuming its the last child.
    XElement root = getElement();

    if (root.getFirstChildElement() != null && root.getFirstChildElement().getTagName().equals("TABLE")) {
      root = root.selectNode("td");
    }
    return root;
  }

  protected C getValue(M m) {
    C value;
    if (store.hasRecord(m)) {
      value = store.getRecord(m).getValue(valueProvider);
    } else {
      value = valueProvider.getValue(m);
    }
    return value;
  }

  protected int getVisibleRowCount() {
    int rh = view.getCalculatedRowHeight();
    int visibleHeight = getElement().getHeight(true);
    return (int) ((visibleHeight < 1) ? 0 : Math.ceil(visibleHeight / rh));
  }

  protected int[] getVisibleRows(List<M> visible, int count) {
    int sc = getElement().getScrollTop();
    int start = (int) (sc == 0 ? 0 : Math.floor(sc / view.getCalculatedRowHeight()) - 1);
    int first = Math.max(start, 0);
    int last = Math.min(start + count + 2, visible.size() - 1);
    return new int[] {first, last};
  }

  protected void handleEventForCell(Event event) {
    // Get the event target.
    EventTarget eventTarget = event.getEventTarget();
    if (cell == null || !Element.is(eventTarget)) {
      return;
    }
    final Element target = event.getEventTarget().cast();

    TreeNode<M> node = findNode(target);
    if (node == null) {
      return;
    }
    M value = node.getModel();

    Element cellParent = getView().getTextElement(node);
    if (value != null && cellParent != null) {
      Context context = new Context(store.indexOf(value), 0, getStore().getKeyProvider().getKey(value));
      fireEventToCell(event, event.getType(), cellParent, value, context);
    }
  }

  protected boolean hasChildren(M model) {
    TreeNode<M> node = findNode(model);
    if (loader != null && !node.isLoaded()) {
      return loader.hasChildren(node.getModel());
    }
    if (!node.leaf || store.hasChildren(node.getModel())) {
      return true;
    }
    return false;
  }

  protected boolean isCheckable(TreeNode<M> node) {
    boolean leaf = isLeaf(node.getModel());
    boolean check = checkable;
    switch (checkNodes) {
      case LEAF:
        if (!leaf) {
          check = false;
        }
        break;
      case PARENT:
        if (leaf) {
          check = false;
        }
      default:
        // empty
    }
    return check;
  }

  protected boolean isRowRendered(int i, List<M> visible) {
    Element e = view.getElement(findNode(visible.get(i)));
    return e != null && e.getFirstChild().hasChildNodes();
  }

  @Override
  protected void notifyShow() {
    super.notifyShow();
    update();
  }

  protected void onAdd(StoreAddEvent<M> event) {
    for (M child : event.getItems()) {
      register(child);
    }
    if (isOrWasAttached()) {
      M parent = store.getParent(event.getItems().get(0));
      TreeNode<M> pn = findNode(parent);
      if (parent == null || (pn != null && pn.isChildrenRendered())) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();

        int parentDepth = parent == null ? 0 : store.getDepth(parent);
        for (M child : event.getItems()) {
          TreeViewRenderMode mode = !bufferRender ? TreeViewRenderMode.ALL : TreeViewRenderMode.BUFFER_WRAP;

          sb.append(renderChild(parent, child, parentDepth, mode));
        }
        int index = event.getIndex();
        int parentChildCount = parent == null ? store.getRootCount() : store.getChildCount(parent);
        if (index == 0) {
          DomHelper.insertFirst(getContainer(parent), sb.toSafeHtml());
        } else if (index == parentChildCount - event.getItems().size()) {
          DomHelper.insertHtml("beforeEnd", getContainer(parent), sb.toSafeHtml());
        } else {
          DomHelper.insertBefore((Element) getContainer(parent).getChild(index).cast(), sb.toSafeHtml());
        }

      }
      refresh(parent);
      update();
    }
  }

  protected void onAfterFirstAttach() {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.renderContainer(builder);

    // TODO we should be asking appearance for the root container element rather
    // than assuming its the last child.
    rootContainer = getRootContainer();

    rootContainer.setInnerSafeHtml(builder.toSafeHtml());

    getElement().show();
    getElement().getStyle().setProperty("overflow", "auto");

    if (store.getRootItems().size() == 0 && loader != null) {
      loader.load();
    } else {
      renderChildren(null);
      if (autoSelect) {
        getSelectionModel().select(0, false);
      }
    }

    // JAWS does not work when disabling text selection
    setAllowTextSelection(false);
    sinkEvents(Event.ONSCROLL | Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (store == null) {
      throw new IllegalStateException("Cannot attach a tree without a store");
    }
    update();
  }

  protected void onCheckCascade(M model, CheckState checked) {
    switch (getCheckStyle()) {
      case PARENTS:
        if (checked == CheckState.CHECKED) {
          M p = store.getParent(model);
          while (p != null) {
            setChecked(p, CheckState.CHECKED);
            p = store.getParent(p);
          }
        } else {
          for (M child : store.getChildren(model)) {
            setChecked(child, CheckState.UNCHECKED);
          }
        }
        break;
      case CHILDREN:
        for (M child : store.getChildren(model)) {
          setChecked(child, checked);
        }

        break;
      case TRI:
        onTriCheckCascade(model, checked);
        break;
      case NONE:
        // do nothing
        break;

    }
  }

  protected void onCheckClick(Event event, TreeNode<M> node) {
    event.stopPropagation();
    event.preventDefault();
    setChecked(node.getModel(), (node.checked == CheckState.CHECKED || node.checked == CheckState.PARTIAL)
        ? CheckState.UNCHECKED : CheckState.CHECKED);
  }

  protected void onClear(StoreClearEvent<M> event) {
    clear();
  }

  protected void onClick(Event event) {
    XEvent e = event.<XEvent> cast();
    TreeNode<M> node = findNode(event.getEventTarget().<Element> cast());
    if (node != null) {
      Element jointEl = view.getJointElement(node);
      if (jointEl != null && e.within(jointEl)) {
        toggle((M) node.getModel());
        // necessary to prevent synthetic mouse events in case of touch
        event.preventDefault();
        event.stopPropagation();
      }
      Element checkEl = view.getCheckElement(node);
      if (checkable && checkEl != null && isEnabled() && e.within(checkEl)) {
        onCheckClick(event, node);
      }
    }

    focusEl.setXY(event.getClientX(), event.getClientY());
    focus();
  }

  protected void onCollapse(M model, TreeNode<M> node, boolean deep) {
    if (node.isExpanded() && fireCancellableEvent(new BeforeCollapseItemEvent<M>(model))) {
      node.setExpanded(false);
      // collapse
      view.collapse(node);

      List<M> l = new ArrayList<M>();
      l.add(node.getModel());

      update();
      if (bufferRender) {
        cleanCollapsed(node.getModel());
      }
      moveFocus(node.getElement());
      fireEvent(new CollapseItemEvent<M>(model));
    }
    if (deep) {
      setExpandChildren(model, false);
    }
  }

  protected void onDataChanged(StoreDataChangeEvent<M> event) {
    redraw(event.getParent());
  }

  protected void onDoubleClick(Event event) {
    TreeNode<M> node = findNode(event.getEventTarget().<Element> cast());
    if (node != null) {
      setExpanded(node.getModel(), !node.isExpanded());
    }
  }

  protected void onExpand(M model, TreeNode<M> node, boolean deep) {
    if (!isLeaf(node.getModel())) {
      // if we are loading, ignore it
      if (node.isLoading()) {
        return;
      }
      // if we have a loader and node is not loaded make
      // load request and exit method
      if (!node.isExpanded() && loader != null && (!node.isLoaded() || !caching) && !filtering) {
        store.removeChildren(model);
        node.setExpand(true);
        node.setExpandDeep(deep);
        node.setLoading(true);
        view.onLoading(node);
        loader.loadChildren(model);
        return;
      }
      if (!node.isExpanded() && fireCancellableEvent(new BeforeExpandItemEvent<M>(model))) {
        node.setExpanded(true);

        if (!node.isChildrenRendered()) {
          renderChildren(model);
          node.setChildrenRendered(true);
        }
        // expand
        view.expand(node);

        update();
        fireEvent(new ExpandItemEvent<M>(model));
      }

      if (deep) {
        setExpandChildren(model, true);
      }
    }

  }

  protected void onFilter(StoreFilterEvent<M> se) {
    if (isOrWasAttached()) {
      filtering = store.isEnableFilters();
      clear();
      renderChildren(null);

      if (expandOnFilter && store.isFiltered()) {
        expandAll();
      }
      update();
    }
  }

  protected void onRemove(StoreRemoveEvent<M> se) {
    TreeNode<M> node = findNode(se.getItem());

    if (node != null) {
      if (view.getElement(node) != null) {
        // directly call node.getElement() since it won't be null
        node.getElement().removeFromParent();
      }
      unregister(se.getItem());

      TreeStoreRemoveEvent<M> remove = (TreeStoreRemoveEvent<M>) se;
      for (M child : remove.getChildren()) {
        unregister(child);
      }

      M itemParent = remove.getParent();
      TreeNode<M> p = findNode(itemParent);
      if (p != null && p.isExpanded() && store.getChildCount(p.getModel()) == 0) {
        setExpanded(p.getModel(), false);
      } else if (p != null && store.getChildCount(p.getModel()) == 0) {
        refresh(itemParent);
      }
      moveFocus(node.getElement());
    }
  }

  protected void onResize(int width, int height) {
    super.onResize(width, height);
    update();
    constrainFocusElement();
  }

  protected void onScroll(Event event) {
    update();
    constrainFocusElement();
  }

  protected void onSort(StoreSortEvent<M> se) {
    redraw(null);
  }

  protected void onTriCheckCascade(M model, CheckState checked) {
    if (checked == CheckState.CHECKED) {

      List<M> children = store.getAllChildren(model);
      cascade = false;
      for (M child : children) {
        TreeNode<M> n = findNode(child);
        if (n != null) {
          setChecked(child, checked);
        }

      }

      M parent = store.getParent(model);
      while (parent != null) {
        boolean allChildrenChecked = true;
        for (M child : store.getAllChildren(parent)) {
          TreeNode<M> n = findNode(child);
          if (n != null) {
            if (!isChecked(child)) {
              allChildrenChecked = false;
            }
          }
        }

        if (!allChildrenChecked) {
          setChecked(parent, CheckState.PARTIAL);
        } else {
          setChecked(parent, CheckState.CHECKED);
        }

        parent = store.getParent(parent);

      }
      cascade = true;
    } else if (checked == CheckState.UNCHECKED) {
      List<M> children = store.getAllChildren(model);
      cascade = false;
      for (M child : children) {
        setChecked(child, checked);
      }

      M parent = store.getParent(model);
      while (parent != null) {
        boolean anyChildChecked = false;
        for (M child : store.getAllChildren(parent)) {
          if (isChecked(child)) {
            anyChildChecked = true;
          }
        }

        if (anyChildChecked) {
          setChecked(parent, CheckState.PARTIAL);
        } else {
          setChecked(parent, CheckState.UNCHECKED);
        }

        parent = store.getParent(parent);
      }

      cascade = true;
    }
  }

  protected void onUpdate(StoreUpdateEvent<M> event) {
    for (M item : event.getItems()) {
      TreeNode<M> node = findNode(item);
      if (node != null) {
        if (node.model != item) {
          node.model = item;
        }
        refresh(item);
      }
    }
  }

  /**
   * Completely redraws the children of the given parent (or all items if parent is null), throwing away details like
   * currently expanded nodes, etc. Not designed to be used to update nodes, look into {@link #refresh(Object)} or
   * {@link Store#update(Object)}.
   * 
   * @param parent the parent of the items to redraw
   */
  protected void redraw(M parent) {
    if (!isOrWasAttached()) {
      return;
    }

    if (parent == null) {
      clear();
      renderChildren(null);

      if (autoSelect) {
        M m = store.getChild(0);
        if (m != null) {
          List<M> sel = new ArrayList<M>();
          sel.add(m);
          getSelectionModel().setSelection(sel);
        }
      }

    } else {
      TreeNode<M> n = findNode(parent);
      n.setLoaded(true);
      n.setLoading(false);
      if (n.isChildrenRendered()) {
        getContainer(parent).setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
      }

      renderChildren(parent);

      if (n.isExpand() && !isLeaf(n.getModel())) {
        n.setExpand(false);
        boolean c = caching;
        caching = true;
        boolean deep = n.isExpandDeep();
        n.setExpandDeep(false);
        setExpanded(parent, true, deep);
        caching = c;
      } else {
        refresh(parent);
      }
    }
  }

  protected String register(M m) {
    String id = generateModelId(m);
    if (nodes.containsKey(id)) {
      return nodes.get(id).getDomId();
    } else {
      String domId = XDOM.getUniqueId();
      TreeNode<M> node = new TreeNode<M>(id, m, domId);
      nodes.put(id, node);
      nodesByDom.put(domId, node);
      return domId;
    }
  }

  protected SafeHtml renderChild(M parent, M child, int depth, TreeViewRenderMode renderMode) {
    String domId = register(child);
    TreeNode<M> node = findNode(child);
    return view.getTemplate(child, domId, getCellContent(child), calculateIconStyle(child), isCheckable(node),
        node.checked, calculateJoint(child), depth, renderMode);
  }

  protected void renderChildren(M parent) {
    SafeHtmlBuilder markup = new SafeHtmlBuilder();
    int depth = store.getDepth(parent);
    List<M> children = parent == null ? store.getRootItems() : store.getChildren(parent);
    if (children.size() == 0) {
      return;
    }

    for (M child : children) {
      register(child);
    }
    for (int i = 0; i < children.size(); i++) {
      TreeViewRenderMode mode = !bufferRender ? TreeViewRenderMode.ALL : TreeViewRenderMode.BUFFER_WRAP;
      markup.append(renderChild(parent, children.get(i), depth, mode));
    }
    Element container = getContainer(parent);
    container.setInnerSafeHtml(markup.toSafeHtml());

    for (int i = 0; i < children.size(); i++) {
      M child = children.get(i);
      TreeNode<M> node = findNode(child);

      if (autoExpand) {
        setExpanded(child, true);
      } else if (node.isExpand() && !isLeaf(node.getModel())) {
        node.setExpand(false);
        setExpanded(child, true);
      } else if (loader != null) {
        if (autoLoad) {
          if (store.isFiltered()) {
            renderChildren(child);
          } else {
            if (loader.hasChildren(child)) {
              loader.loadChildren(child);
            }
          }
        }
      } else if (autoLoad) {
        renderChildren(child);
      }
    }

    TreeNode<M> n = findNode(parent);
    if (n != null) {
      if (n.checked == CheckState.CHECKED) {
        switch (checkStyle) {
          case TRI:
            cascade = false;
            for (M child : store.getChildren(parent)) {
              setChecked(child, CheckState.CHECKED);
            }
            cascade = true;
            break;
          case CHILDREN:
            onCheckCascade(n.getModel(), n.checked);
            break;
          default:
            // empty

        }

      }

      n.setChildrenRendered(true);
    }
    if (parent == null) {
      ensureFocusElement();
    }
    update();
  }

  protected void unregister(M m) {
    if (m != null) {
      TreeNode<M> n = nodes.remove(generateModelId(m));
      if (n != null) {
        nodesByDom.remove(n.getDomId());
        n.clearElements();
      }
    }
  }

  protected void update() {
    if (!bufferRender) {
      return;
    }
    if (updateTask == null) {
      updateTask = new DelayedTask() {

        @Override
        public void onExecute() {
          doUpdate();
        }
      };
    }
    updateTask.delay(view.getScrollDelay());
  }

  private void ensureFocusElement() {
    if (focusEl != null) {
      focusEl.removeFromParent();
    }
    focusEl = (XElement) getElement().appendChild(focusImpl.createFocusable());
    focusEl.addClassName(CommonStyles.get().noFocusOutline());
    if (focusEl.hasChildNodes()) {
      focusEl.getFirstChildElement().addClassName(CommonStyles.get().noFocusOutline());
      com.google.gwt.dom.client.Style focusElStyle = focusEl.getFirstChildElement().getStyle();
      focusElStyle.setBorderWidth(0, Unit.PX);
      focusElStyle.setFontSize(1, Unit.PX);
      focusElStyle.setPropertyPx("lineHeight", 1);
    }
    focusEl.setLeft(0);
    focusEl.setTop(0);
    focusEl.makePositionable(true);
    focusEl.addEventsSunk(Event.FOCUSEVENTS);

  }

  private void setExpandChildren(M m, boolean expand) {
    for (M child : store.getChildren(m)) {
      setExpanded(child, expand, true);
    }
  }

}
