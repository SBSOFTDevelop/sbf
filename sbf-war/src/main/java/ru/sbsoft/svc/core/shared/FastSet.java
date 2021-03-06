/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.shared;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

public class FastSet extends AbstractSet<String> implements Serializable {
  private Map<String, String> map;
  private static final String PRESENT = "";

  public FastSet() {
    map = new FastMap<String>();
  }

  @Override
  public boolean add(String s) {
    return map.put(s, PRESENT) == null;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public boolean contains(Object o) {
    return map.containsKey(o);
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public Iterator<String> iterator() {
    return map.keySet().iterator();
  }

  @Override
  public boolean remove(Object o) {
    String s = map.remove(o);
    return s != null && s.equals(PRESENT);
  }

  @Override
  public int size() {
    return map.size();
  }

}
