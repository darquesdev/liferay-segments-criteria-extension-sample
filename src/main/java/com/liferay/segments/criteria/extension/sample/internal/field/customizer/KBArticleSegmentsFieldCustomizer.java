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

package com.liferay.segments.criteria.extension.sample.internal.field.customizer;

import com.liferay.item.selector.ItemSelector;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.segments.criteria.extension.sample.internal.item.selector.KBArticleItemSelectorCriterion;
import com.liferay.segments.field.Field;
import com.liferay.segments.field.customizer.SegmentsFieldCustomizer;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(
	immediate = true,
	property = {
		"segments.field.customizer.entity.name=KBArticle",
		"segments.field.customizer.key=" + KBArticleSegmentsFieldCustomizer.KEY,
		"segments.field.customizer.priority:Integer=50"
	},
	service = SegmentsFieldCustomizer.class
)
public class KBArticleSegmentsFieldCustomizer
	implements SegmentsFieldCustomizer {

	public static final String KEY = "kbArticle";

	@Override
	public ClassedModel getClassedModel(String fieldValue) {
		return _getKBArticle(fieldValue);
	}

	@Override
	public String getClassName() {
		return KBArticle.class.getName();
	}

	@Override
	public List<String> getFieldNames() {
		return _fieldNames;
	}

	@Override
	public String getFieldValueName(String fieldValue, Locale locale) {
		KBArticle kbArticle = _getKBArticle(fieldValue);

		if (kbArticle == null) {
			return fieldValue;
		}

		return kbArticle.getTitle();
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public Field.SelectEntity getSelectEntity(PortletRequest portletRequest) {
		try {
			PortletURL portletURL = _itemSelector.getItemSelectorURL(
				RequestBackedPortletURLFactoryUtil.create(portletRequest),
				"selectEntity", new KBArticleItemSelectorCriterion());

			return new Field.SelectEntity(
				"selectEntity",
				_getSelectEntityTitle(
					_portal.getLocale(portletRequest),
					KBArticle.class.getName()),
				portletURL.toString(), false);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get select entity", exception);
			}

			return null;
		}
	}

	private KBArticle _getKBArticle(String fieldValue) {
		long resourcePrimKey = GetterUtil.getLong(fieldValue);

		if (resourcePrimKey == 0) {
			return null;
		}

		return _kbArticleLocalService.fetchLatestKBArticle(
			resourcePrimKey, WorkflowConstants.STATUS_ANY);
	}

	private String _getSelectEntityTitle(Locale locale, String className) {
		String title = ResourceActionsUtil.getModelResource(locale, className);

		return LanguageUtil.format(locale, "select-x", title);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KBArticleSegmentsFieldCustomizer.class);

	private static final List<String> _fieldNames = ListUtil.fromArray(
		"kbArticleId");

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private KBArticleLocalService _kbArticleLocalService;

	@Reference
	private Portal _portal;

}