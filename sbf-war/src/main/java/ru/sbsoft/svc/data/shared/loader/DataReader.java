/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

/**
 * Interface for objects that translate raw data into a given type. For example,
 * an XML string being returned from the server could be used to create model
 * instances.
 * 
 * @param <M> the return data type
 * @param <D> the incoming data type to be read
 */
public interface DataReader<M, D> {

  /**
   * Reads the raw data and returns the typed data.
   * 
   * @param loadConfig the load config information
   * @param data the data to read
   * @return the processed and / or converted data
   */
  public M read(Object loadConfig, D data);

}
