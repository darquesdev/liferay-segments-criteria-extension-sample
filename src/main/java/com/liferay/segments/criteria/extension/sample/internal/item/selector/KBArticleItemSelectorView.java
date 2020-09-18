/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.segments.criteria.extension.sample.internal.item.selector;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.ItemSelectorViewDescriptorRenderer;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleService;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(
	property = "item.selector.view.order:Integer=100",
	service = ItemSelectorView.class
)
public class KBArticleItemSelectorView
	implements ItemSelectorView<KBArticleItemSelectorCriterion> {

	@Override
	public Class<KBArticleItemSelectorCriterion>
		getItemSelectorCriterionClass() {

		return KBArticleItemSelectorCriterion.class;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	public String getTitle(Locale locale) {
		return "Knowledge Base Articles";
	}

	@Override
	public void renderHTML(
			ServletRequest servletRequest, ServletResponse servletResponse,
			KBArticleItemSelectorCriterion kbArticleItemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		_itemSelectorViewDescriptorRenderer.renderHTML(
			servletRequest, servletResponse, kbArticleItemSelectorCriterion,
			portletURL, itemSelectedEventName, search,
			new ItemSelectorViewDescriptor<KBArticle>() {

				@Override
				public String getDefaultDisplayStyle() {
					return "descriptive";
				}

				@Override
				public ItemDescriptor getItemDescriptor(KBArticle kbArticle) {
					return new ItemDescriptor() {

						@Override
						public String getIcon() {
							return null;
						}

						@Override
						public String getImageURL() {
							return null;
						}

						@Override
						public String getPayload() {
							return JSONUtil.put(
								"entityid", kbArticle.getResourcePrimKey()
							).put(
								"entityname", kbArticle.getTitle()
							).toString();
						}

						@Override
						public String getSubtitle(Locale locale) {
							return kbArticle.getUserName();
						}

						@Override
						public String getTitle(Locale locale) {
							return kbArticle.getTitle();
						}

					};
				}

				@Override
				public ItemSelectorReturnType getItemSelectorReturnType() {
					return new KBArticleItemSelectorReturnType();
				}

				@Override
				public SearchContainer<KBArticle> getSearchContainer()
					throws PortalException {

					PortletRequest portletRequest =
						(PortletRequest)servletRequest.getAttribute(
							JavaConstants.JAVAX_PORTLET_REQUEST);

					long groupId = _portal.getScopeGroupId(portletRequest);

					SearchContainer<KBArticle> kbArticleSearchContainer =
						new SearchContainer<>(
							portletRequest, portletURL, null, null);

					kbArticleSearchContainer.setTotal(
						_kbArticleService.getGroupKBArticlesCount(
							groupId, WorkflowConstants.STATUS_ANY));

					kbArticleSearchContainer.setResults(
						_kbArticleService.getGroupKBArticles(
							groupId, WorkflowConstants.STATUS_ANY,
							kbArticleSearchContainer.getStart(),
							kbArticleSearchContainer.getEnd(), null));

					return kbArticleSearchContainer;
				}

				@Override
				public boolean isShowBreadcrumb() {
					return false;
				}

				@Override
				public boolean isShowManagementToolbar() {
					return false;
				}

			});
	}

	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.singletonList(
			new KBArticleItemSelectorReturnType());

	@Reference
	private ItemSelectorViewDescriptorRenderer<KBArticleItemSelectorCriterion>
		_itemSelectorViewDescriptorRenderer;

	@Reference
	private KBArticleService _kbArticleService;

	@Reference
	private Portal _portal;

}