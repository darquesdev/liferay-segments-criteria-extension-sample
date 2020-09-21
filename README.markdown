# Liferay Segments Criteria Extension Sample

Code for Liferay's [/dev/24](https://liferay.dev/24) talk:

<details>
 <summary>How to create your own segments criteria</summary>

  * Short introduction to Segmentation and Personalization
  * Personalization in Action!
  * Creating complex segments:
    * User & Organization criteria: fields and custom fields
    * Session criteria: session properties, request attributes, cookies, etc
    * Segments criteria: segments as criterion. **Introduced in 7.3**.
  * Hands-on Creating a Segment Criteria Contributor

</details>

#

This repo will guide you step-by-step on how to extend the [Liferay Segments Criteria](https://help.liferay.com/hc/en-us/articles/360028746752-Introduction-to-Segmentation-and-Personalization) capabilities by creating your own [Segment Criteria Contributor](https://help.liferay.com/hc/en-us/articles/360028746752-Introduction-to-Segmentation-and-Personalization#segmentscriteriacontributor). As an example we'll create a contributor that segments users based on the *Knowledge Base* articles they have authored.

![Screenshot of the Knowledge base segments criteria](screenshot.png)

## Steps

1. Make your related entity ([KBArticle](https://github.com/liferay/liferay-portal/blob/master/modules/apps/knowledge-base/knowledge-base-api/src/main/java/com/liferay/knowledge/base/model/KBArticle.java)) searchable through [OData](https://www.odata.org/) queries:
    - [EntityModel](https://github.com/liferay/liferay-portal/blob/master/modules/apps/portal-odata/portal-odata-api/src/main/java/com/liferay/portal/odata/entity/EntityModel.java): represents your associated entity with its fields of interest.
    - [ODataRetriever](https://github.com/liferay/liferay-portal/blob/master/modules/apps/segments/segments-api/src/main/java/com/liferay/segments/odata/retriever/ODataRetriever.java): obtains the KBArticles that match a given OData query.
2. Customize fields of type *ID* to use an entity selector:
    - [ItemSelector](https://github.com/liferay/liferay-portal/blob/master/modules/apps/item-selector/item-selector-api/src/main/java/com/liferay/item/selector/ItemSelector.java): Provides a KBArticles selector.
    - [SegmentsFieldCustomizer](https://github.com/liferay/liferay-portal/blob/master/modules/apps/segments/segments-api/src/main/java/com/liferay/segments/field/customizer/SegmentsFieldCustomizer.java): Connects the *ID* field with the ItemSelector and provides a meaningful field value name.
3. Create the [SegmentsCriteriaContributor](https://github.com/liferay/liferay-portal/blob/master/modules/apps/segments/segments-api/src/main/java/com/liferay/segments/criteria/contributor/SegmentsCriteriaContributor.java): It contributes a query to filters users based on the articles they authored.

Follow [the commits](https://github.com/darquesdev/liferay-segments-criteria-extension-sample/commits) for a step by step explanation.

## How to run it

Clone the repo and run the following:
```bash
LIFERAY_HOME=<your liferay home folder> ./gradlew deploy
```

In my case:
```bash
LIFERAY_HOME=~/projects/liferay/portal/bundles ./gradlew deploy ./gradlew deploy
```

## Segmentation & Personalization docs
* [Creating a Segment Criteria Contributor](https://help.liferay.com/hc/en-us/articles/360029067411-Creating-a-Segment-Criteria-Contributor)
* [Developers Guide](https://help.liferay.com/hc/en-us/articles/360028746752-Introduction-to-Segmentation-and-Personalization).
* [Admin Guide](https://help.liferay.com/hc/en-us/articles/360028721372-Introduction-to-Segmentation-and-Personalization).

## Thanks

Thanks to [@4lejandrito](https://github.com/4lejandrito), and the [@liferay-tango](https://github.com/liferay-tango) team for helping with this talk.