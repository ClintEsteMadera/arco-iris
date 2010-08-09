/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package commons.gui.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * A concrete implementation of a node in a preference dialog tree. This class also supports lazy creation of the node's
 * preference page.
 */
public class PreferenceNode implements IPreferenceNode {

	/**
	 * Creates a new preference node with the given nodeId. The new node has no subnodes.
	 * 
	 * @param nodeId
	 *            the node nodeId
	 */
	public PreferenceNode(String nodeId) {
		Assert.isNotNull(nodeId);
		this.nodeId = nodeId;
	}

	/**
	 * Creates a preference node with the given nodeId, label, and image, and lazily-loaded preference page. The
	 * preference node assumes (sole) responsibility for disposing of the image; this will happen when the node is
	 * disposed.
	 * 
	 * @param nodeId
	 *            the node nodeId
	 * @param label
	 *            the label used to display the node in the preference dialog's tree
	 * @param image
	 *            the image displayed left of the label in the preference dialog's tree, or <code>null</code> if none
	 * @param className
	 *            the class name of the preference page; this class must implement <code>IPreferencePage</code>
	 */
	public PreferenceNode(String nodeId, String label, ImageDescriptor image, IPreferencePage preferencePage) {
		this(nodeId);
		this.imageDescriptor = image;
		Assert.isNotNull(label);
		this.label = label;
		Assert.isNotNull(preferencePage);
		page = preferencePage;
	}

	/**
	 * Creates a preference node with the given nodeId and preference page. The title of the preference page is used for
	 * the node label. The node will not have an image.
	 * 
	 * @param nodeId
	 *            the node nodeId
	 * @param preferencePage
	 *            the preference page
	 */
	public PreferenceNode(String nodeId, IPreferencePage preferencePage) {
		this(nodeId);
		Assert.isNotNull(preferencePage);
		page = preferencePage;
	}

	public void add(IPreferenceNode node) {
		if (subNodes == null) {
			subNodes = new ArrayList<IPreferenceNode>();
		}
		subNodes.add(node);
	}

	/**
	 * @see IPreferenceNode#createPage()
	 */
	public void createPage() {
		page = null; // Antes se invocaba a un método que siempre retornaba null
		if (getLabelImage() != null) {
			page.setImageDescriptor(imageDescriptor);
		}
		page.setTitle(label);
	}

	/**
	 * (non-Javadoc) Method declared on IPreferenceNode.
	 */
	public void disposeResources() {
		if (image != null) {
			image.dispose();
			image = null;
		}
		if (page != null) {
			page.dispose();
			page = null;
		}
	}

	public IPreferenceNode findSubNode(String subNodeId) {
		Assert.isNotNull(subNodeId);
		Assert.isTrue(subNodeId.length() > 0);
		IPreferenceNode result = null;
		if (this.subNodes != null) {
			IPreferenceNode node;
			for (int i = 0; i < this.subNodes.size(); i++) {
				node = this.subNodes.get(i);
				if (subNodeId.equals(node.getId())) {
					result = node;
					break;
				}
			}
		}
		return result;
	}

	public String getId() {
		return this.nodeId;
	}

	/**
	 * Returns the image descriptor for this node.
	 * 
	 * @return the image descriptor
	 */
	protected ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	public Image getLabelImage() {
		if (image == null && imageDescriptor != null) {
			image = imageDescriptor.createImage();
		}
		return image;
	}

	public String getLabelText() {
		return (page == null) ? label : page.getTitle();
	}

	public IPreferencePage getPage() {
		return page;
	}

	public IPreferenceNode[] getSubNodes() {
		IPreferenceNode[] result = new IPreferenceNode[0];
		if (subNodes != null) {
			result = subNodes.toArray(new IPreferenceNode[subNodes.size()]);
		}
		return result;
	}

	public IPreferenceNode remove(String subNodeId) {
		IPreferenceNode node = findSubNode(subNodeId);
		if (node != null) {
			remove(node);
		}
		return node;
	}

	public boolean remove(IPreferenceNode node) {
		boolean remove = false;
		if (subNodes != null) {
			remove = subNodes.remove(node);
		}
		return remove;
	}

	/**
	 * Set the current page to be newPage.
	 * 
	 * @param newPage
	 */
	public void setPage(IPreferencePage newPage) {
		page = newPage;
	}

	/**
	 * Preference page, or <code>null</code> if not yet loaded.
	 */
	private IPreferencePage page;

	/**
	 * The list of subnodes (immediate children) of this node (element type: <code>IPreferenceNode</code>).
	 */
	private List<IPreferenceNode> subNodes;

	/**
	 * The nodeId of this node.
	 */
	private String nodeId;

	/**
	 * Text label for this node. Note that this field is only used prior to the creation of the preference page.
	 */
	private String label;

	/**
	 * Image descriptor for this node, or <code>null</code> if none.
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * Cached image, or <code>null</code> if none.
	 */
	private Image image;
}
