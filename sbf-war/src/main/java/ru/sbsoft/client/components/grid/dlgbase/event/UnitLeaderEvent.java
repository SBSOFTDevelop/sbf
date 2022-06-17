package ru.sbsoft.client.components.grid.dlgbase.event;

import ru.sbsoft.client.components.grid.dlgbase.IUnitLeader;
import ru.sbsoft.client.components.grid.dlgbase.Unit;

/**
 *
 * @author Kiselev
 */
public class UnitLeaderEvent extends UnitEvent {

    private final IUnitLeader holder;

    public UnitLeaderEvent(Unit unit, IUnitLeader holder) {
        super(unit);
        this.holder = holder;
    }

    public IUnitLeader getHolder() {
        return holder;
    }

}
