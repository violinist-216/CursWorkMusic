<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Singer info</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/singers/{id}" method="get">
    <div class="createWr1" >
        <h2 > Let`s learn more about singers</h2>
    </div >
    <div class="conteiner">
        <div class="label12">Id</div>
        <div class="label12">Name</div>
        <div class="label12">Age</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Singer.id}</div>
        <div class="text">${Singer.singername}</div>
        <div class="text">${Singer.singerage}</div>
    </div>
    <div class="wrapper1">
        <div align="center" class="label">Albums of this singer:</div>
    </div>
    <div class="wrapper1">
        <div class="label12">Id</div>
        <div class="label12">Title</div>
    </div>
    <c:forEach items="${Singer.albums}" var="album">
        <div class="wrapper2">
            <div class="text">${album.id}</div>
            <div class="text">${album.title}</div>
            <div class="text text-dark">
                <button class="btn btn-outline-danger"><a href="/singers/${Singer.id}/removeAlbum/${album.id}">Remove album</a></button>
            </div>
        </div>
    </c:forEach>
    <div class="wrapper1">
        <div align="center" class="label">Songs of this singer:</div>
    </div>
    <div class="wrapper1">
        <div class="label12">Id</div>
        <div class="label12">Title</div>
    </div>
    <c:forEach items="${Singer.songs}" var="song">
        <div class="wrapper2">
            <div class="text">${song.id}</div>
            <div class="text">${song.title}</div>
            <div class="text text-dark">
                <button class="btn btn-outline-danger"><a href="/singers/${Singer.id}/removeSong/${song.id}">Remove song</a></button>
            </div>
        </div>
    </c:forEach>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/singers/${Singer.id}" method = "post" >
        <div class="createWr1" >
            <h2 > Update singer</h2>
        </div >
        <div class="createWr6" >
            <div class="label12">Name</div>
            <div class="label12">Age</div>
            <div class="label12">Producer</div>
        </div >
        <div class="createWr6" >
            <div class="space" ><input type = "text" name = "singername" value="${Singer.singername}" required ></div >
            <div class="space" ><input type = "number" min= "16" max= "120" name = "singerage" value="${Singer.singerage}" required ></div >
            <div class="space" >
                <select name="producer">
                    <c:forEach items="${Producers}" var="producer" >
                        <option value="${producer.id}">${producer.fullName}</option>
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