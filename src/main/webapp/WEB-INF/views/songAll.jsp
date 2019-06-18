
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Students</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
    <form action="/songs" method="get">
        <div class="createWr1" >
            <h2> Information about song</h2>
        </div>
        <div class="conteiner">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Content</div>
    </div>
    <div>
    <c:forEach  items="${SongList}" var ="song">
        <div class="wrapper2">
            <span class="text">${song.id}</span>
            <span class="text">${song.title}</span>
            <span class="text">${song.content}</span>
            <span class="text">
                <a type="button", class="btnAction", href="/songs/${song.id}">Detail</a>
                <security:authorize access="hasRole('ROLE_ADMIN')">
                    <a type="button", class="btnAction", href="/songs/delete/${song.id}">Delete</a>
                </security:authorize></span>
        </div>
    </c:forEach>
    </div>
    </form>
    <security:authorize access="hasRole('ROLE_ADMIN')">
        <form width="90%" action = "/songs" method = "post" >
            <div class="createWr1" >
                <h2> Add new song</h2>
            </div>
            <div class="createWr2" >
                <div class="label13" > Title </div >
                <div class="label13" > Content </div >
                <div class="label13" > Album </div >
                <div class="label13" > Singer </div >
            </div >
            <div class="createWr3" >
                <div class="space" ><input type = "text" name = "title" required ></div >
                <div class="space" ><input type = "text" name = "content" required ></div >
                <div class="space" >
                    <select name="album" >
                        <c:forEach items="${Albums}" var="album">
                            <option value="${album.id}"> ${album.title} ${album.genre}</option>
                        </c:forEach>
                    </select>
                </div >
                <div class="space" >
                    <select name="singer" >
                        <c:forEach items="${Singers}" var="singer">
                            <option value="${singer.id}">${singer.singername} ${singer.singerage}</option>
                        </c:forEach>
                    </select>
                </div >
            </div >

            <div class="forSubmit" >
                <button type = "submit" class="btn" > Submit </button >
            </div >
        </form>
    </security:authorize>

    </body>
    <jsp:include page="../views/footer.jsp" />
    </html>
