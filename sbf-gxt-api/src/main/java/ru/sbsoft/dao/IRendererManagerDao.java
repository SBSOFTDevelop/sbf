package ru.sbsoft.dao;

import javax.ejb.Local;
import ru.sbsoft.shared.renderer.RendererManager;

/**
 * Интерфейс определяет RendererManager пользовательского приложения
 * @author Sokoloff
 */
@Local
public interface IRendererManagerDao {
    
    RendererManager getRendererManager();

}
