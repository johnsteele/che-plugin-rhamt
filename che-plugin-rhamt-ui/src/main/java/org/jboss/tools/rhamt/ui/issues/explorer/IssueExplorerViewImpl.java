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

import static java.util.Collections.singletonList;
import static org.eclipse.che.ide.project.node.SyntheticNode.CUSTOM_BACKGROUND_FILL;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.api.resources.Container;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.menu.ContextMenu;
import org.eclipse.che.ide.part.explorer.project.EmptyTreePanel;
import org.eclipse.che.ide.part.explorer.project.NodeTypeComparator;
import org.eclipse.che.ide.project.node.SyntheticNode;
import org.eclipse.che.ide.resources.tree.ContainerNode;
import org.eclipse.che.ide.resources.tree.ResourceNode;
import org.eclipse.che.ide.resources.tree.SkipHiddenNodesInterceptor;
import org.eclipse.che.ide.ui.smartTree.NodeDescriptor;
import org.eclipse.che.ide.ui.smartTree.NodeLoader;
import org.eclipse.che.ide.ui.smartTree.NodeStorage;
import org.eclipse.che.ide.ui.smartTree.NodeStorage.StoreSortInfo;
import org.eclipse.che.ide.ui.smartTree.SortDir;
import org.eclipse.che.ide.ui.smartTree.Tree;
import org.eclipse.che.ide.ui.smartTree.TreeStyles;
import org.eclipse.che.ide.ui.smartTree.data.HasAction;
import org.eclipse.che.ide.ui.smartTree.data.HasAttributes;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.data.NodeInterceptor;
import org.eclipse.che.ide.ui.smartTree.presentation.DefaultPresentationRenderer;
import org.eclipse.che.ide.ui.status.StatusWidget;
import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IssueExplorerViewImpl extends BaseView<IssueExplorerView.ActionDelegate>
    implements IssueExplorerView {
	
  private final Tree tree;
  private final SkipHiddenNodesInterceptor skipHiddenNodesInterceptor;

  private static final String ISSUE_TREE_WIDGET_ID = "issueTree";

  private IssueExplorerPlaceholder issueExplorerPlaceholder;

  @Inject
  public IssueExplorerViewImpl(
      final ContextMenu contextMenu,
      final RhamtLocalizationConstant coreLocalizationConstant,
      final Set<NodeInterceptor> nodeInterceptorSet,
      final SkipHiddenNodesInterceptor skipHiddenNodesInterceptor,
      final IssueExplorerPlaceholderPanel placeholderPanel,
      final IssueExplorerPlaceholder issueExplorerPlaceholder) {
    this.skipHiddenNodesInterceptor = skipHiddenNodesInterceptor;

    setTitle(coreLocalizationConstant.issuesViewTitle());

    this.issueExplorerPlaceholder = issueExplorerPlaceholder;

    NodeStorage nodeStorage = new NodeStorage();

    NodeLoader nodeLoader = new NodeLoader(nodeInterceptorSet);
    nodeLoader.getNodeInterceptors().add(skipHiddenNodesInterceptor);

    tree = new Tree(nodeStorage, nodeLoader, new StatusWidget<Tree>(placeholderPanel));
    tree.setContextMenuInvocationHandler(
        new Tree.ContextMenuInvocationHandler() {
          @Override
          public void onInvokeContextMenu(int x, int y) {
            contextMenu.show(x, y);
          }
        });

    tree.getNodeStorage().addSortInfo(new StoreSortInfo(new NodeTypeComparator(), SortDir.ASC));
    tree.getNodeStorage()
        .addSortInfo(
            new StoreSortInfo(
                new Comparator<Node>() {
                  @Override
                  public int compare(Node o1, Node o2) {
                    if (o1 instanceof ResourceNode && o2 instanceof ResourceNode) {
                      return ((ResourceNode) o1).compareTo((ResourceNode) o2);
                    }

                    return 0;
                  }
                },
                SortDir.ASC));

    tree.setPresentationRenderer(new IssueExplorerRenderer(tree.getTreeStyles()));
    tree.ensureDebugId(ISSUE_TREE_WIDGET_ID);
    tree.setAutoSelect(true);
    tree.getNodeLoader().setUseCaching(false);

    setContentWidget(tree);
  }

  @Override
  protected void focusView() {
    tree.setFocus(true);
  }

  @Override
  protected void blurView() {
    tree.setFocus(false);
  }

  @Override
  public Tree getTree() {
    return tree;
  }

  @Override
  public void reloadChildren(Node parent) {
    reloadChildren(parent, false);
  }

  @Override
  public void reloadChildren(Node parent, boolean deep) {
    for (Node node : parent == null ? tree.getRootNodes() : singletonList(parent)) {
      if (node.isLeaf()) {
        continue;
      }

      if (tree.isExpanded(node)) {
        tree.getNodeLoader().loadChildren(node, deep);
      }
    }
  }

  @Override
  public void reloadChildrenByType(Class<?> type) {
    List<Node> rootNodes = tree.getRootNodes();
    for (Node rootNode : rootNodes) {
      List<Node> allChildren = tree.getNodeStorage().getAllChildren(rootNode);
      for (Node child : allChildren) {
        if (child.getClass().equals(type)) {
          NodeDescriptor nodeDescriptor = tree.getNodeDescriptor(child);
          if (nodeDescriptor.isLoaded()) {
            tree.getNodeLoader().loadChildren(child);
          }
        }
      }
    }
  }

  @Override
  public void select(Node node, boolean keepExisting) {
    tree.getSelectionModel().select(node, keepExisting);
  }

  @Override
  public void select(List<Node> nodes, boolean keepExisting) {
    tree.getSelectionModel().select(nodes, keepExisting);
  }

  @Override
  public void showHiddenFilesForAllExpandedNodes(boolean show) {
    if (show) {
      tree.getNodeLoader().getNodeInterceptors().remove(skipHiddenNodesInterceptor);
    } else {
      tree.getNodeLoader().getNodeInterceptors().add(skipHiddenNodesInterceptor);
    }

    for (Node node : tree.getRootNodes()) {
      reloadChildren(node, true);
    }
  }

  @Override
  public boolean setGoIntoModeOn(Node node) {
    return tree.getGoInto().activate(node);
  }

  @Override
  public void setGoIntoModeOff() {
    tree.getGoInto().reset();
  }

  @Override
  public boolean isGoIntoActivated() {
    return tree.getGoInto().isActive();
  }

  @Override
  public void collapseAll() {
    tree.collapseAll();
  }

  private class IssueExplorerRenderer extends DefaultPresentationRenderer<Node> {

    IssueExplorerRenderer(TreeStyles treeStyles) {
      super(treeStyles);
    }

    @Override
    public Element render(Node node, String domID, Tree.Joint joint, int depth) {
      Element element = super.render(node, domID, joint, depth);

      element.setAttribute("name", node.getName());

      if (node instanceof ResourceNode) {
        final Resource resource = ((ResourceNode) node).getData();
        element.setAttribute("path", resource.getLocation().toString());

        Project project = resource.getProject();
        if (project != null) {
          element.setAttribute("project", project.getLocation().toString());
        }
      }

      if (node instanceof HasAction) {
        element.setAttribute("actionable", "true");
      }

      if (node instanceof SyntheticNode<?>) {
        element.setAttribute("synthetic", "true");
        element.setAttribute("project", ((SyntheticNode) node).getProject().toString());
      }

      if (node instanceof HasAttributes
          && ((HasAttributes) node).getAttributes().containsKey(CUSTOM_BACKGROUND_FILL)) {
        element
            .getFirstChildElement()
            .getStyle()
            .setBackgroundColor(
                ((HasAttributes) node).getAttributes().get(CUSTOM_BACKGROUND_FILL).get(0));
      }

      if (node instanceof ContainerNode) {
        Container container = ((ContainerNode) node).getData();
        if (container instanceof Project) {
          String head = container.getProject().getAttribute("git.current.head.name");
          if (head != null) {
            Element nodeContainer = element.getFirstChildElement();
            DivElement divElement = Document.get().createDivElement();
            divElement.setInnerText("(" + head + ")");
            divElement.setClassName(treeStyles.treeStylesCss().vcsHeadContainer());
            nodeContainer.insertBefore(divElement, nodeContainer.getLastChild());
          }
        }
      }
      return element;
    }
  }

  @Override
  public void showPlaceholder(boolean placeholder) {
    if (placeholder) {
      if (!issueExplorerPlaceholder.getElement().hasParentElement()) {
        getElement().appendChild(issueExplorerPlaceholder.getElement());
      }
    } else {
      if (issueExplorerPlaceholder.getElement().hasParentElement()) {
        getElement().removeChild(issueExplorerPlaceholder.getElement());
      }
    }
  }
}
