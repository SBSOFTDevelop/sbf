package ru.sbsoft.shared.model.operation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;

public class OperationInfo implements Serializable {

    public static final String OPERATION_GRID_PARAMETERS = "__OPERATION_GRID_PARAMETERS";
    public static final String SCHEDULER_CONTEXT = "__SCHEDULER_CONTEXT";
    public static final List<String> SYSTEM_PARAMETERS = Arrays.asList(OPERATION_GRID_PARAMETERS, SCHEDULER_CONTEXT);
    //
    public static final String FINISH_MARKS = "__FINISH_MARKS";
    //

    public static boolean isSystemParameter(OperationObject parameter) {
        return SYSTEM_PARAMETERS.contains(parameter.getName());
    }

    private Long id;
    private MultiOperationStatus status;
    private String title;
    private String code;
    private BigDecimal progress;
    private String createComment;
    private String processComment;
    private boolean userNotified;
    private boolean userNeedNotify;
    private String locale;

    public OperationInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultiOperationStatus getStatus() {
        return status;
    }

    public void setStatus(MultiOperationStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public String getCreateComment() {
        return createComment;
    }

    public void setCreateComment(String createComment) {
        this.createComment = createComment;
    }

    public String getProcessComment() {
        return processComment;
    }

    public void setProcessComment(String processComment) {
        this.processComment = processComment;
    }

    public boolean isUserNotified() {
        return userNotified;
    }

    public void setUserNotified(boolean userNotified) {
        this.userNotified = userNotified;
    }

    public boolean isUserNeedNotify() {
        return userNeedNotify;
    }

    public void setUserNeedNotify(boolean userNeedNotify) {
        this.userNeedNotify = userNeedNotify;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
