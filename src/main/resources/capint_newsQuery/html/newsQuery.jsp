<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%@ page import="org.jahia.modules.currentsapi.filters.News" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<template:addResources type="css" resources="surfstitch.css"/>
<jcr:nodeProperty node="${currentNode}" name="searchKeyword" var="searchKeyword"/>
<jcr:nodeProperty node="${currentNode}" name="language" var="language"/>
<jcr:nodeProperty node="${currentNode}" name="category" var="category"/>
<jcr:nodeProperty node="${currentNode}" name="language" var="language"/>
<jcr:nodeProperty node="${currentNode}" name="region" var="region"/>
<jcr:nodeProperty node="${currentNode}" name="dateFrom" var="dateFrom"/>

<c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
<c:set var="bannerText" value="${currentNode.properties['bannerText'].string}"/>


<!--Title-->
<h1 class="card-title text-primary">${title} from ${category}</h1>
<!--Text-->
<p class="card-text text-secondary">${bannerText}</p>
<div class="row">
    <c:forEach items="${newsList}" var="news" varStatus="item">
        <div class="blog mb-5 col-md-4 mb-3" id="${news.getId()}">
            <div class="blog-wrapper">
                <div class="img-blog ">
                    <a href="${news.getUrl()}" title="${news.getTitle()}">
                        <c:set var="imageURL" value="${news.getImage()}"/>
                        <c:if test="${not empty imageURL and imageURL ne 'None'}">
                            <img alt="${news.getTitle()}"
                                 src="${news.getImage()}">
                        </c:if>
                    </a>
                </div>
                <div class="content-blog">
                    <h2 class="blog-title">${news.getTitle()}</h2>
                    <div>
                        <p class="description-blog">${news.getDescription()}</p>
                            ${news.getPublished()}<br/>
                        <p><span class="text-muted font-italic">${news.getAuthor()}</span></p>
                    </div>
                    <a href="${news.getUrl()}" class="read-more align-right" title="${news.getTitle()}">Read More</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
