<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Album info</title>
    <link href="../../resources/albumDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/albums/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Genre</div>
        <div class="label">Singer</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Album.id}</div>
        <div class="text">${Album.title}</div>
        <div class="text">${Album.genre}</div>
        <div class="text">${Album.singer.singername}</div>
    </div>
    <div class="wrapper1">
        <div align="center" class="label">Songs of this album:</div>
    </div>
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label"></div>
    </div>
    <c:forEach items="${Album.songs}" var="song">
        <div class="wrapper2">
            <div class="text">${song.id}</div>
            <div class="text">${song.title}</div>
            <div class="text text-dark">
                <button class="btn btn-outline-danger"><a href="/albums/${Album.id}/removeSong/${song.id}">Remove song</a></button>
            </div>
        </div>
    </c:forEach>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/albums/${Album.id}" method = "post" >
        <div class="updateWr1" >
            <h2 > Update album</h2>
        </div >
        <div class="updateWr4" >
            <div class="label">Title</div>
            <div class="label">Genre</div>
            <div class="label">Singer</div>
        </div >
        <div class="updateWr5" >
            <div class="space" ><input type = "text" name = "title" value="${Album.title}" required ></div >
            <div class="space" ><input type = "text" name = "genre" value="${Album.genre}" required ></div >
            <div class="space" >
                <select name="singer">
                    <c:forEach items="${Singers}" var="singer" >
                        <option value="${singer.id}">${singer.singername}</option>
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