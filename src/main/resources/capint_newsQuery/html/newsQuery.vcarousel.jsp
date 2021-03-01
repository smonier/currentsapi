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
<%@ taglib prefix="cnews" uri="http://www.jahia.org/tags/currentsNews" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<jcr:nodeProperty node="${currentNode}" name="searchKeyword" var="searchKeyword"/>
<jcr:nodeProperty node="${currentNode}" name="language" var="language"/>
<jcr:nodeProperty node="${currentNode}" name="category" var="category"/>
<jcr:nodeProperty node="${currentNode}" name="language" var="language"/>
<jcr:nodeProperty node="${currentNode}" name="region" var="region"/>
<jcr:nodeProperty node="${currentNode}" name="dateFrom" var="dateFrom"/>
<jcr:nodeProperty node="${currentNode}" name="queryType" var="queryType"/>
<template:addResources type="css" resources="vcarousel.css"/>

<c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
<c:set var="bannerText" value="${currentNode.properties['bannerText'].string}"/>
<template:addCacheDependency node="${currentNode}"/>
<c:set var="newsList" value="${cnews:getCurrentsNews(queryType, searchKeyword, language, category, region)}"/>


<div class="container">
    <div class="module_header">
        <div class="module_title">${currentNode.properties['jcr:title'].string}</div>
        <div class="module_divider">
        </div>
    </div>
    <div class="module_body">
        <div id="newsCarousel-${currentNode.UUID}" class="carousel vert slide" data-ride="carousel"
             data-interval="3000">
            <div class="carousel-inner">
                <c:forEach items="${newsList}" var="news" varStatus="status">
                    <div class="carousel-item ${status.first?' active':''}">
                        <div class="card">
                            <div class="card-horizontal" style="height:245px">
                                <div class="img-square-wrapper">
                                    <c:set var="imageURL" value="${news.getImage()}"/>
                                    <c:if test="${not empty imageURL and imageURL ne 'None'}">
                                        <img alt="${news.getTitle()}"
                                             src="${news.getImage()}"
                                             class="newsImg">
                                    </c:if>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title">${news.getTitle()}</h5>
                                    <p class="card-text">    ${news.getPublished()}
                                        <c:set var="author" value="${news.getAuthor()}"/>
                                        <c:if test="${not author.startsWith('[')}">
                                            <span class="text-muted font-italic"> by ${news.getAuthor()}</span>
                                        </c:if>
                                        <a href="${news.getUrl()}" class="btn btn-primary bottom-right m-2">Read
                                            More</a>
                                    </p>
                                </div>
                            </div>

                        </div>

                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

