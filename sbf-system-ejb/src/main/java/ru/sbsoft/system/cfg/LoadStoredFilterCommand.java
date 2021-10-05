package ru.sbsoft.system.cfg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.sbsoft.common.SysConst;
import ru.sbsoft.common.SystemProperty;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.filter.IStoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.meta.filter.StoredFilterType;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE_;

/**
 *
 * @author Kiselev
 */
public class LoadStoredFilterCommand extends ConfigCommand {

    public LoadStoredFilterCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public IStoredFilterList exec() {
        StoredFilterList res = new StoredFilterList();
        SYS_OBJECT sysObject = getSysObject(false);
        if (sysObject != null) {
            String globalGroup = new SystemProperty(SysConst.FILTER_GLOBAL_GROUP, null).getParameterValue();
            if (globalGroup != null && (globalGroup = globalGroup.trim()).isEmpty()) {
                globalGroup = null;
            }
            Set<Group> groups = context.getUserGroups();
            String user = context.getUserName();
            Set<String> users = new HashSet<>(groups.size() + 2);
            users.add(user);
            if(globalGroup != null){
                users.add(globalGroup);
            }
            for(Group g : groups){
                users.add(g.getCode());
            }
            List<StoredFilterPath> paths = new StoreQueryBuilder(sysObject, users).setStoreName(null, false).doQuery(StoredFilterPath.class, SYS_OBJ_STORAGE_.USER_NAME, SYS_OBJ_STORAGE_.STORAGE_NAME);
            for(StoredFilterPath p : paths){
                String idName = p.getIdentityName();
                StoredFilterType type = StoredFilterType.GROUP;
                if(globalGroup != null && globalGroup.equals(idName)){
                    type = StoredFilterType.GLOBAL;
                }else if(user.equals(idName)){
                    type = StoredFilterType.PERSONAL;
                }
                res.add(type, p);
            }
        }
        return res;
    }
}
