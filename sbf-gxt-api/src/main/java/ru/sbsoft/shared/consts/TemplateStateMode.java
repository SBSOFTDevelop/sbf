package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.Modifier;

/**
 * Режимы вызова темплейта - помежаются в mode
 * @author sokolov
 */
public enum TemplateStateMode implements Modifier {
    
    META, DATA, ONLY_IDS, ROW, ROWS, AGGR, LOOKUP;
    
}
