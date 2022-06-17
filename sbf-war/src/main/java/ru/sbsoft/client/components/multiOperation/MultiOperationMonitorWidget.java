package ru.sbsoft.client.components.multiOperation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.CANCELED;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.DONE;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.ERROR;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Компонент для отображения в пользовательском интерфейсе информации о
 * запущенных и выполненных операциях.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class MultiOperationMonitorWidget implements IsWidget {

    private final VerticalLayoutContainer container = new VerticalLayoutContainer();
    private final TextButton text = new TextButton();
    private String customTitle;

    public MultiOperationMonitorWidget() {
        this(null);
    }

    public MultiOperationMonitorWidget(String title) {
        container.add(text, VLC.CONST_M2);
        customTitle = title;
        text.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                MultiOperationMonitorWidget.this.onClick();
            }
        });
        //короткая статистика операций.
        MultiOperationMonitor.getInstance().addHandler(new MultiOperationMonitor.AbstractMultiOperationMonitorHandler() {
            @Override
            public void onUpdate(List<OperationInfo> operationList) {
                int created = 0, running = 0, done = 0, error = 0;
                for (OperationInfo o : operationList) {
                    switch (o.getStatus()) {
                        case CREATED:
                        case READY_TO_START:
                            created++;
                            break;
                        case DONE:
                            done++;
                            break;
                        case ERROR:
                            error++;
                            break;
                        case STARTED:
                            running++;
                            break;
                        default:
                            break;
                    }
                }
                MultiOperationMonitorWidget.this.setOperationCount(operationList.size());
                final StringBuilder title = new StringBuilder();
                title.append(I18n.get(SBFEditorStr.msgToday)).append(": ")
                        .append(created).append(" ").append(I18n.get(SBFEditorStr.msgNotRunning)).append(", ")
                        .append(running).append(" ").append(I18n.get(SBFEditorStr.msgRunning)).append(", ")
                        .append(done).append(" ").append(I18n.get(SBFEditorStr.msgCompleted)).append(", ")
                        .append(error).append(" ").append(I18n.get(SBFEditorStr.msgWithError));
                MultiOperationMonitorWidget.this.setTitle(title.toString());
            }
        });

        //Оповещение о завершенных операциях
        MultiOperationMonitor.getInstance().addHandler(new MultiOperationMonitor.AbstractMultiOperationMonitorHandler() {

            @Override
            public void onChange(OperationInfo operationInfo) {
                checkDone(operationInfo);
            }

            @Override
            public void onNew(OperationInfo operationInfo) {
                checkDone(operationInfo);
            }

            private void checkDone(OperationInfo operationInfo) {
                if (isFinalStatus(operationInfo.getStatus()) && operationInfo.isUserNeedNotify() && !operationInfo.isUserNotified()) {
                    if (!MultiOperationLogWindow.isWindowOpened(operationInfo.getId())) {
                        showDoneWindow(operationInfo);
                    } else {
                        setUserNotified(operationInfo);
                    }
                }
            }

            private boolean isFinalStatus(MultiOperationStatus status) {
                return status == CANCELED || status == DONE || status == ERROR;
            }

            private void showDoneWindow(final OperationInfo operationInfo) {
                //Window.alert("Операция '" + operationName + "' выполнена");

                final String operationName = operationInfo.getTitle();
                final MultiOperationStatus status = operationInfo.getStatus();

                final BaseWindow window = new BaseWindow() {
                    {
                        final BaseWindow w = this;
                        setHeading(I18n.get(SBFEditorStr.msgOperationCompeted, operationName));
                        getWindow().add(new VerticalPanel() {
                            {
                                setHorizontalAlignment(ALIGN_CENTER);
                                add(new HTML("<p>" + I18n.get(SBFEditorStr.msgOperationWithStatus, operationName, String.valueOf(operationInfo.getId()))
                                        + " " + status.toString() + "</p><br/>"));
                                add(
                                        new HorizontalPanel() {
                                    {

                                        add(new Button(I18n.get(SBFEditorStr.labelDetailed)) {
                                            {
                                                addClickHandler(new ClickHandler() {

                                                    @Override
                                                    public void onClick(ClickEvent event) {
                                                        MultiOperationLogWindow.forOperation(operationInfo.getId()).show();
                                                        w.hide();
                                                        setUserNotified(operationInfo);
                                                    }
                                                });
                                            }
                                        });
                                        add(new Button("Ok") {
                                            {
                                                addClickHandler(new ClickHandler() {

                                                    @Override
                                                    public void onClick(ClickEvent event) {
                                                        w.hide();
                                                        setUserNotified(operationInfo);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        setResizable(false);
                        setCollapsible(false);
                        setMaximizable(false);
                        setClosable(false);
                        setModal(true);
                    }
                };
                window.show();

            }

            private void setUserNotified(OperationInfo operationInfo) {
                SBFConst.MULTI_OPERATION_SERVICE.setUserNotified(operationInfo.getId(), true, new DefaultAsyncCallback<Void>());
            }

        });

        setOperationCount(0);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    /**
     * Открывает/закрывает окно с журналом операций.
     */
    public void onClick() {
        final MultiOperationStatusWindow window = MultiOperationStatusWindow.getInstance();

        if (customTitle != null) {
            window.setHeading(customTitle);
        }
        window.setVisible(!window.isVisible());
    }

    /**
     * Устанавливает количество запущенных операций.
     *
     * @param count количество запущенных операций.
     */
    public void setOperationCount(int count) {
        text.setText(new StringBuilder(I18n.get(SBFEditorStr.labelOperations)).append(' ').append('(').append(count).append(')').toString());
    }

    /**
     * Устанавливает текст, отображаемый в компоненте.
     *
     * @param title текст, отображаемый в компоненте.
     */
    public void setTitle(String title) {
        container.setTitle(title);
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customHeader) {
        this.customTitle = customHeader;
    }

}
