/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.List;

/**
 * Simple reader to turn {@link List}s into {@link ListLoadResult}.
 * 
 * @param <M> the model data type
 */
public class ListReader<M> implements DataReader<ListLoadResult<M>, List<M>> {

  @Override
  public ListLoadResult<M> read(Object loadConfig, List<M> data) {
    return new ListLoadResultBean<M>(data);
  }

}
