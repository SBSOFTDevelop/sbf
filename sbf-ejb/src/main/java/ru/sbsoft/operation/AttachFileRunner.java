package ru.sbsoft.operation;

import java.math.BigDecimal;
import java.util.Collection;
import ru.sbsoft.dao.entity.IBaseEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.consts.Dict;

/**
 *
 * @author sokolov
 * @param <E> - entity which has relation to collection of file entities of type F
 * @param <F> - entity which contans file
 */
public abstract class AttachFileRunner<E extends IBaseEntity, F extends IFormEntity & IFileContainer> extends DocImpExpRunner<F> {
    private final Class<E> eClass;

    protected AttachFileRunner(Class<E> eClass, Class<F> fClass) {
        this.eClass = eClass;
        setDir(LoadFileDirection.IMPORT);
        setEntClass(fClass);
    }
    
    protected abstract Collection<F> getFiles(E e);
    
    protected abstract F createFile(E e);

    @Override
    protected BigDecimal askSelectedRecord() {
        F fe;
        BigDecimal id = getBigDecimalParam(Dict.RECORD_ID);
        BigDecimal fileId = getBigDecimalParam(Dict.FILE_ID);
        E e = notNull(em.find(eClass, id), getLocaleResource(SBFExceptionStr.documNotFound, String.valueOf(id.longValue())) + "\n Type: " + eClass.getName());
        final Collection<F> files = getFiles(e);
        if (files == null || files.isEmpty() || fileId == null) {
            fe = createFile(e);
            fe.setFileName("empty");
            fe.setFileBody(new byte[]{0});
            em.persist(fe);
        } else {
            fe = files.stream().filter((t) -> {
                return t.getId().equals(fileId);
            }).findFirst().get();
        }
        return fe.getId();
    }
}
