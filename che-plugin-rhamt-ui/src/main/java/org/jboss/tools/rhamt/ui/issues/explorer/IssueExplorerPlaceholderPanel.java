/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.rhamt.ui.issues.explorer;

import static org.eclipse.che.ide.api.resources.Resource.PROJECT;

import javax.inject.Inject;

import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.Presentation;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.keybinding.KeyBindingAgent;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.resources.ResourceChangedEvent;
import org.eclipse.che.ide.api.resources.ResourceDelta;
import org.eclipse.che.ide.api.theme.Style;
import org.eclipse.che.ide.ui.toolbar.PresentationFactory;
import org.eclipse.che.ide.util.dom.Elements;
import org.eclipse.che.ide.util.input.KeyMapUtil;
import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;
import org.jboss.tools.rhamt.ui.action.NewRunConfigurationAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import elemental.dom.Element;
import elemental.dom.Node;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.LIElement;
import elemental.html.SpanElement;

public class IssueExplorerPlaceholderPanel extends Composite implements ResourceChangedEvent.ResourceChangedHandler {

	private static IssueExplorerPlaceholderPanelUiBinder uiBinder = GWT
			.create(IssueExplorerPlaceholderPanelUiBinder.class);
	protected final AppContext appContext;
	private final ActionManager actionManager;
	private final KeyBindingAgent keyBindingAgent;
	private final PresentationFactory presentationFactory;
	private final RhamtLocalizationConstant local;
	private final NewRunConfigurationAction newRunConfigurationAction;
	@UiField
	protected DivElement title;
	@UiField
	protected DivElement root;
	@UiField
	protected DivElement container;
	@UiField
	protected DivElement logo;
	@UiField
	Css style;

	@Inject
	public IssueExplorerPlaceholderPanel(ActionManager actionManager, KeyBindingAgent keyBindingAgent,
			AppContext appContext, EventBus eventBus, RhamtLocalizationConstant local,
			NewRunConfigurationAction newRunConfigurationAction) {

		this.actionManager = actionManager;
		this.keyBindingAgent = keyBindingAgent;
		this.appContext = appContext;
		this.local = local;
		this.newRunConfigurationAction = newRunConfigurationAction;

		presentationFactory = new PresentationFactory();

		initWidget(uiBinder.createAndBindUi(this));

		eventBus.addHandler(ResourceChangedEvent.getType(), this);
		Timer hoverToRenderTimer = new Timer() {
			@Override
			public void run() {
				render();
			}
		};
		hoverToRenderTimer.schedule(500);
	}

	@Override
	public void onResourceChanged(ResourceChangedEvent event) {
		final ResourceDelta delta = event.getDelta();
		final Resource resource = delta.getResource();

		if (!(resource.getResourceType() == PROJECT && resource.getLocation().segmentCount() == 1)) {
			return;
		}

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				render();
			}
		});
	}

	protected void render() {
		if (appContext.getProjects() != null && appContext.getProjects().length == 0) {
			doRender();
			return;
		}
	}

	private void doRender() {
		this.title.setInnerText(local.noIssuesTitle());
		container.removeAllChildren();
		Element listElement = Elements.createElement("ul", new String[] { style.list() });
		LIElement liElement = Elements.createLiElement();
		liElement.appendChild(renderAction(local.newRunConfigurationLabel(), newRunConfigurationAction));
		listElement.appendChild(liElement);
		container.appendChild((com.google.gwt.dom.client.Node) listElement);
	}

	private Node renderAction(String title, final Action action) {
		final Presentation presentation = presentationFactory.getPresentation(action);
		Element divElement = Elements.createDivElement(style.listElement());
		divElement.addEventListener("click", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				ActionEvent event = new ActionEvent(presentation, actionManager);
				action.actionPerformed(event);
			}
		}, true);
		divElement.getStyle().setCursor("pointer");
		divElement.getStyle().setColor(Style.getOutputLinkColor());
		Element label = Elements.createDivElement(style.actionLabel());
		label.setInnerText(title);
		divElement.appendChild(label);

		String hotKey = KeyMapUtil.getShortcutText(keyBindingAgent.getKeyBinding(actionManager.getId(action)));
		if (hotKey == null) {
			hotKey = "&nbsp;";
		} else {
			hotKey = "<nobr>&nbsp;" + hotKey + "&nbsp;</nobr>";
		}
		SpanElement hotKeyElement = Elements.createSpanElement(style.hotKey());
		hotKeyElement.setInnerHTML(hotKey);
		divElement.appendChild(hotKeyElement);
		return divElement;
	}

	interface IssueExplorerPlaceholderPanelUiBinder extends UiBinder<Widget, IssueExplorerPlaceholderPanel> {}

	interface Css extends CssResource {
		String list();
		String parent();
		String center();
		String child();
		String listElement();
		String title();
		String hotKey();
		String actionLabel();
	}
}
