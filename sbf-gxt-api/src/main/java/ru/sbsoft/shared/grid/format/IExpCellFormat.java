package ru.sbsoft.shared.grid.format;

import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author sychugin
 */
public interface IExpCellFormat extends DTO {

    String getFormat(Row row);
    
    Integer getPrecision(Row row);
}
