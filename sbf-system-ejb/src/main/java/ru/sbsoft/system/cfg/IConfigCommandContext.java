package ru.sbsoft.system.cfg;

import java.util.Set;
import javax.persistence.EntityManager;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.system.ISysParameters;

/**
 *
 * @author Kiselev
 */
public interface IConfigCommandContext {
    EntityManager getEm();
    String getUserName();
    Set<Group> getUserGroups();
}
