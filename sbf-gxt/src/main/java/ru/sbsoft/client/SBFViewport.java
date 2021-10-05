package ru.sbsoft.client;

import com.sencha.gxt.widget.core.client.container.Viewport;

/**
 * Главная панель приложения. Создается в {@link SBFEntryPoint}
 * @author balandin
 * @since Jul 16, 2014 4:30:55 PM
 */
public class SBFViewport extends Viewport {

	private final SBFEntryPoint entryPoint;

	public SBFViewport(SBFEntryPoint entryPoint) {
		this.entryPoint = entryPoint;
		setWidget(entryPoint.getApplicationPanel());
	}

	public SBFEntryPoint getEntryPoint() {
		return entryPoint;
	}
}
