package ru.sbsoft.system.cfg;

import ru.sbsoft.shared.model.user.Group;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 *
 * @author Kiselev
 */
public interface IConfigCommandContext {
    EntityManager getEm();
    String getUserName();
    Set<Group> getUserGroups();
}
