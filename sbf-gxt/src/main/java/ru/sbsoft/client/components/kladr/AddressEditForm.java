package ru.sbsoft.client.components.kladr;

import ru.sbsoft.client.components.kladr.helper.QueueItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.SBFEntryPoint;
import ru.sbsoft.sbf.gxt.components.FieldsContainer;
import ru.sbsoft.client.components.form.BoundedTextField;
import ru.sbsoft.client.components.form.ModelForm;
import ru.sbsoft.client.components.form.SBSoftFieldSetContainer;
import ru.sbsoft.client.components.kladr.helper.KLADRComboBoxes;
import ru.sbsoft.client.components.validators.TextValidator;
import ru.sbsoft.client.consts.SBFConfig;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.consts.SBFVariable;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;
import ru.sbsoft.shared.services.IKLADRServiceAsync;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Форма редактирования адреса.
 * @author balandin
 * @since Mar 15, 2013 7:20:36 PM
 */
public class AddressEditForm extends ModelForm<AddressModel> {

    private static final IKLADRServiceAsync RPC = SBFConst.KLADR_SERVICE;
    private final AddressEdit addressEditField;
    private final boolean onlyActual;
    //
    private AddressLookupField searchComboBox;
    //
    private TextField codeKladrField;
    private TextButton fillAddressButton;
    //
    private KLADRComboBoxes editors;
    private KLADRItemComboBox regionField;
    private KLADRItemComboBox areaField;
    private KLADRItemComboBox cityField;
    private KLADRItemComboBox settlementField;
    private KLADRItemComboBox streetField;
    //
    private TextField houseField;
    private TextField buildingField;
    private TextField flatField;
    private TextField postcodeField;
    //
    private TextButton checkPostcodeButton;
    private String fullAddress;

    public AddressEditForm(final AddressEdit addressEditField, final boolean onlyActual) {
        setHeading((SafeHtml)null);
        setWidth(780);
        setResizable(false);

        this.onlyActual = onlyActual;
        this.addressEditField = addressEditField;
        this.editors.setOnlyActual(onlyActual);
        this.searchComboBox.setOnlyActual(onlyActual);
    }

    @Override
    public void setHeading(SafeHtml text) {
        super.setHeading(I18n.get(SBFEditorStr.labelFormAddress) + (text == null ? Strings.EMPTY : " [" + text + "]"));
    }

    @Override
    protected void fillToolBar(ToolBar toolBar) {
        super.fillToolBar(toolBar);

        toolBar.add(new SeparatorToolItem());

        TextButton btnSaveClose = new TextButton();
        btnSaveClose.setIcon(SBFResources.EDITOR_ICONS.Undo());
        btnSaveClose.setToolTip(I18n.get(SBFEditorStr.hintClearAll));
        btnSaveClose.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                reset();
            }
        });
        toolBar.add(btnSaveClose);
    }

    private void reset() {
        searchComboBox.setValue(null);

        regionField.setValue(null);
        areaField.setValue(null);
        cityField.setValue(null);
        settlementField.setValue(null);
        streetField.setValue(null);

        houseField.setValue(null);
        buildingField.setValue(null);
        flatField.setValue(null);
        postcodeField.setValue(null);

        setChanged(true);
    }

    @Override
    protected void createEditors() {
        SBSoftFieldSetContainer fieldSet = new SBSoftFieldSetContainer(120);
        fieldSet.getElement().getStyle().setBackgroundColor("white");

        fieldSet.add(50, arrange(new FieldLabel(searchComboBox = AddressLookupField.createInstance(), I18n.get(SBFEditorStr.labelSearch)), -1), false);
        if (SBFConfig.readBool(SBFVariable.CODE_KLADR)) {
            codeKladrField = new TextField();
            codeKladrField.setEnabled(false);

            SimplePanel separator = new SimplePanel();
            separator.setWidth("5px");

            fillAddressButton = new TextButton("", SBFResources.BROWSER_ICONS.QueryView16());
            fillAddressButton.setToolTip(I18n.get(SBFEditorStr.hintFormAddress));
            fillAddressButton.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    AddressModel addr = new AddressModel();
                    addr.setCodeKLADR(codeKladrField.getText());
                    new MatchAddressProcessor(addr).start();
                }
            });

            FieldsContainer fc = new FieldsContainer();
            fc.add(codeKladrField, HLC.FILL);
            fc.add(separator, HLC.CONST);
            fc.add(fillAddressButton, HLC.CONST);

            FieldLabel l = arrange(new FieldLabel(fc, I18n.get(SBFEditorStr.labelKLADR)), 150);
            l.getElement().getStyle().setPaddingBottom(10, Style.Unit.PX);
            fieldSet.add(160, l, true);
        }

        editors = new KLADRComboBoxes();
        regionField = createField(fieldSet, I18n.get(SBFEditorStr.labelRegion), "00", editors);
        areaField = createField(fieldSet, I18n.get(SBFEditorStr.labelArea), "000", editors);
        cityField = createField(fieldSet, I18n.get(SBFEditorStr.labelCity), "000", editors);
        settlementField = createField(fieldSet, I18n.get(SBFEditorStr.labelSettlement), "000", editors);
        streetField = createField(fieldSet, I18n.get(SBFEditorStr.labelStreet), "0000", editors);
        editors.setItems(new KLADRItemComboBox[]{regionField, areaField, cityField, settlementField, streetField});

        checkPostcodeButton = new TextButton("", SBFResources.BROWSER_ICONS.QueryView16());
        checkPostcodeButton.setToolTip(I18n.get(SBFEditorStr.hintDeterminePostIndex));
        checkPostcodeButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                checkPostcode();
            }
        });

        HorizontalLayoutContainer hp = new HorizontalLayoutContainer();
        hp.getElement().getStyle().setBackgroundColor("white");
        hp.add(arrange(new FieldLabel(houseField = createTextField(16), I18n.get(SBFEditorStr.labelHose))), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1));
        hp.add(arrange(new FieldLabel(buildingField = createTextField(16), I18n.get(SBFEditorStr.labelBlock))), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(0, 0, 0, 5)));
        hp.add(arrange(new FieldLabel(flatField = createTextField(16), I18n.get(SBFEditorStr.labelFlat))), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(0, 0, 0, 5)));
        hp.add(arrange(new FieldLabel(postcodeField = createPostCodeField(), I18n.get(SBFEditorStr.labelPostcode))), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(0, 0, 0, 5)));
        hp.add(checkPostcodeButton, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(20, 0, 0, 5)));

        final FieldLabel fieldLabel = new FieldLabel(hp, "");
        fieldLabel.setLabelSeparator("");
        fieldLabel.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        fieldLabel.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);
        fieldLabel.getElement().getStyle().setHeight(45, Style.Unit.PX);
        fieldSet.add(160, fieldLabel, true);

        addRegion(fieldSet, VLC.FILL);

        searchComboBox.addBeforeSelectionHandler(new BeforeSelectionHandler<SearchModel>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<SearchModel> event) {
                event.cancel();
                final SearchModel searchModel = event.getItem();
                RPC.address(searchModel.getScoreDoc(), new DefaultAsyncCallback<KLADRAddressDict>() {
                    @Override
                    public void onResult(KLADRAddressDict address) {
                        initFields(address == null ? new KLADRAddressDict() : address);
                    }
                });
            }
        });
    }

    private KLADRItemComboBox createField(SBSoftFieldSetContainer fieldSet, String caption, String defValue, KLADRComboBoxes locator) {
        FieldsContainer container = new FieldsContainer();

        KLADRItemComboBox result = KLADRItemComboBox.create(locator, defValue);
        container.add(result, HLC.FILL);

        SimplePanel separator = new SimplePanel();
        separator.setWidth("5px");
        container.add(separator, HLC.CONST);

        if (SBFConfig.readBool(SBFVariable.CODE_KLADR)) {
            TextField codeField = new TextField();
            codeField.setEnabled(false);
            codeField.setWidth(50);
            result.setData("c", codeField);
            container.add(codeField, HLC.CONST);
        }

        FieldLabel fieldLabel = new FieldLabel(container, caption);
        fieldSet.add(160, arrange(fieldLabel, 420), true);
        return result;
    }

    private TextField createTextField(int maxLength) {
        TextField f = new BoundedTextField(maxLength);
        f.setAllowBlank(true);
        f.addValidator(new MaxLengthValidator(maxLength));
        return f;
    }

    private TextField createPostCodeField() {
        TextField f = new BoundedTextField(6);
        f.addValidator(new TextValidator(true, true, 6, 6));
        return f;
    }

    private FieldLabel arrange(FieldLabel l) {
        l.setLabelAlign(FormPanel.LabelAlign.TOP);
        l.setWidth(100);
        return l;
    }

    private FieldLabel arrange(FieldLabel l, int width) {
        l.getElement().getFirstChildElement().getStyle().setProperty("textAlign", "right");
        if (width > 0) {
            l.setWidth(200 + width);
        } else {
            l.getElement().getStyle().setPaddingTop(10, Style.Unit.PX);
            l.getElement().getStyle().setPaddingBottom(10, Style.Unit.PX);
            l.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);
        }
        return l;
    }

    @Override
    protected void dataToForm(AddressModel dataModel) {
        if (SBFConfig.readBool(SBFVariable.CODE_KLADR)) {
            final String code = dataModel.getCodeKLADR();
            codeKladrField.setValue(code);
            fillAddressButton.setEnabled(Strings.coalesce(code).length() == 15);
        }

        postcodeField.setValue(dataModel.getPostIndex());
        houseField.setValue(dataModel.getHouse());
        buildingField.setValue(dataModel.getBlock());
        flatField.setValue(dataModel.getFlat());

        regionField.setValue(null);
        areaField.setValue(null);
        cityField.setValue(null);
        settlementField.setValue(null);
        streetField.setValue(null);

        fullAddress = dataModel.getFullAddress();

        new CheckAddressProcessor(dataModel).start();
    }

    @Override
    protected void formToData(AddressModel dataModel) {
        dataModel.setPostIndex(postcodeField.getValue());
        dataModel.setRegionName(get(regionField.getValue()));
        dataModel.setRegionCode(regionField.getCode());
        dataModel.setAreaName(get(areaField.getValue()));
        dataModel.setAreaCode(areaField.getCode());
        dataModel.setCityName(get(cityField.getValue()));
        dataModel.setCityCode(cityField.getCode());
        dataModel.setVillageName(get(settlementField.getValue()));
        dataModel.setVillageCode(settlementField.getCode());
        dataModel.setStreetName(get(streetField.getValue()));
        dataModel.setStreetCode(streetField.getCode());
        dataModel.setHouse(houseField.getValue());
        dataModel.setBlock(buildingField.getValue());
        dataModel.setFlat(flatField.getValue());
    }

    private String get(KLADRItem item) {
        return item == null ? null : item.getFullName();
    }

    private void checkPostcode() {
        String house = Strings.clean(houseField.getCurrentValue());
        if (house == null) {
            ClientUtils.alertWarning("", I18n.get(SBFEditorStr.msgNotSpecifyHouse));
            return;
        }
        String building = Strings.clean(buildingField.getCurrentValue());

        final String[] request = new String[editors.getItems().length];
        for (int i = 0; i < editors.getItems().length; i++) {
            final String tmp = editors.getItems()[i].getCode();
            if (tmp == null) {
                ClientUtils.message(I18n.get(SBFEditorStr.captionError), 
                        I18n.get(SBFEditorStr.msgAddressNotMeet));
                return;
            }
            request[i] = tmp;
        }

        RPC.postcode(request, house, building, new DefaultAsyncCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!Strings.isEmpty(result)) {
                    setChanged(true);
                    postcodeField.setValue(result);
                }
            }
        });
    }

    @Override
    public void show(AddressModel address) {
        super.show(address == null ? new AddressModel() : address);
    }

    @Override
    protected void onSave() {
        addressEditField.setValue(null);
        addressEditField.setValue(getDataModel(), true);
    }

    private void initFields(KLADRAddressDict address) {
        regionField.setValue(address.getRegion());
        areaField.setValue(address.getArea());
        cityField.setValue(address.getCity());
        settlementField.setValue(address.getSettlement());
        streetField.setValue(address.getStreet());
        postcodeField.setValue(address.getPostcode());

        setChanged(true);
    }

    private abstract class Processor {

        protected final List<QueueItem> queue;

        public Processor(AddressModel addr) {
            queue = new ArrayList<QueueItem>();
            queue.add(new QueueItem(regionField, addr.getRegionCode(), addr.getRegionName()));
            queue.add(new QueueItem(areaField, addr.getAreaCode(), addr.getAreaName()));
            queue.add(new QueueItem(cityField, addr.getCityCode(), addr.getCityName()));
            queue.add(new QueueItem(settlementField, addr.getVillageCode(), addr.getVillageName()));
            queue.add(new QueueItem(streetField, addr.getStreetCode(), addr.getStreetName()));
        }

        public void start() {
            prepare(false);
        }

        ;

        protected void prepare(final boolean wasErrors) {
            if (queue.isEmpty()) {
                unmask();

                final AddressModel arrd = new AddressModel();
                formToData(arrd);

                final boolean changed = !Strings.equals(fullAddress, arrd.getFullAddress());
                setChanged(changed);
                if (changed) {
                    ClientUtils.alertWarning("", I18n.get(SBFEditorStr.msgAddressChanged));
                }
            } else {
                mask(I18n.get(SBFEditorStr.msgMatchingAddress));

                final QueueItem command = queue.remove(0);
                iteration(command.getField(), Strings.clean(command.getCode()), Strings.clean(command.getName()), wasErrors);
            }
        }

        ;

        protected abstract void iteration(KLADRItemComboBox field, String code, String name, boolean wasErrors);

        protected void error(final KLADRItemComboBox field, final String code, final String name, final boolean wasErrors) {
            field.setValue(stub(name, code));
            field.validate();
            field.collapse();
            prepare(wasErrors);
        }

        private KLADRItem stub(String name, String code) {
            name = Strings.clean(name);
            if (name == null) {
                return null;
            }
            String prefix = null;
            int n = name.lastIndexOf(" ");
            if (n != -1) {
                prefix = name.substring(n + 1);
                name = name.substring(0, n);
            }
            if (name != null) {
                final KLADRItem item = new KLADRItem(prefix, name, code);
                item.setUserInput(true);
                return item;
            }
            return null;
        }
    }

    private class CheckAddressProcessor extends Processor {

        public CheckAddressProcessor(AddressModel addr) {
            super(addr);
        }

        @Override
        protected void iteration(final KLADRItemComboBox field, final String code, final String name, boolean wasErrors) {
            if (name == null || wasErrors) {
                error(field, code, name, wasErrors);
                return;
            }

            final String[] context = field.getContext();
            RPC.lookup(context, onlyActual, new AsyncCallback<List<KLADRItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ClientUtils.alertException(caught);
                    error(field, code, name, true);
                }

                @Override
                public void onSuccess(List<KLADRItem> data) {
                    field.init(context, data);
                    if (!field.selectByValue(name)) {
                        error(field, code, name, true);
                    } else {
                        field.collapse();
                        prepare(false);
                    }
                }
            });
        }
    }

    private class MatchAddressProcessor extends Processor {

        public MatchAddressProcessor(AddressModel addr) {
            super(addr);
        }

        @Override
        protected void iteration(final KLADRItemComboBox field, final String code, final String name, boolean wasErrors) {
            if (code == null) {
                field.setValue(null, false, true);
                prepare(wasErrors);
                return;
            }

            if (wasErrors) {
                error(field, code, I18n.get(SBFEditorStr.msgErrorMapping), true);
                return;
            }

            final String[] context = field.getContext();
            RPC.lookup(context, onlyActual, new AsyncCallback<List<KLADRItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ClientUtils.alertException(caught);
                    error(field, code, I18n.get(SBFEditorStr.msgErrorMapping), true);
                }

                @Override
                public void onSuccess(List<KLADRItem> data) {
                    field.init(context, data);
                    if (!field.selectByCode(code)) {
                        error(field, code, I18n.get(SBFEditorStr.msgErrorMapping), true);
                    } else {
                        field.collapse();
                        prepare(false);
                    }
                }
            });
        }
    }
}
