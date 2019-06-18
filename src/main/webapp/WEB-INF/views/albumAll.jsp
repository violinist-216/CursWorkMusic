<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Albums</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/albums" method="get">
    <div class="createWr1" >
        <h2> Information about album</h2>
    </div>
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Genre</div>
        <div class="label">Singer</div>
    </div>
    <div>
        <c:forEach  items="${AlbumList}" var ="album">
            <div class="wrapper2">
                <div class="text">${album.id}</div>
                <div class="text">${album.title}</div>
                <div class="text">${album.genre}</div>
                <div class="text">${album.singer.singername}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/albums/${album.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/albums/delete/${album.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/albums" method = "post" >
        <div class="createWr1" >
            <h2> Add new album</h2>
        </div>
        <div class="createWr6" >
            <div class="label14">Title</div>
            <div class="label14">Genre</div>
            <div class="label14">Singer</div>
        </div >
        <div class="createWr6" >
            <div class="space" ><input type = "text" name = "title" required ></div >
            <div class="space" ><input type = "text" name = "genre" required ></div >
            <div class="space" >
                <select name="singer" >
                    <c:forEach items="${Singers}" var="singer">
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