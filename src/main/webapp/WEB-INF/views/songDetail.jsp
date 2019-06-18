<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Song info</title>
    <link href="../../resources/studentDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about song:"}</h1>
<form action="/songs/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Content</div>
        <div class="label">Album</div>
        <div class="label">Singer</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Song.id}</div>
        <div class="text">${Song.title}</div>
        <div class="text">${Song.content}</div>
        <div class="text">${Song.album.title}</div>
        <div class="text">${Song.singer.singername}</div>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/songs/${Song.id}" method = "post" >
        <div class="updateWr1" >
            <h2 > Update song</h2>
        </div >
        <div class="updateWr4" >
            <div class="label" > Title </div >
            <div class="label" > Content </div >
            <div class="label" > Album </div >
            <div class="label" > Singer </div >
        </div >
        <div class="updateWr4" >
            <div class="space" ><input type = "text" name = "title" required value="${Song.title}" ></div >
            <div class="space" ><input type = "text" name = "content" required value="${Song.content}" ></div >
            <div class="space" >
                <select name="album">
                    <c:forEach items="${Albums}" var="album" >
                        <option value="${album.id}">${album.title}</option>
                    </c:forEach>
                </select>
            </div >
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

