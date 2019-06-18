<!DOCTYPE>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create new any user</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />

<body>
<div class="container col-6 justify-content-center">
    <div class="createWr1" >
        <h2 > Create new admin</h2>
    </div >
    <h4> ${error != null ? "Some error" : ""}</h4>
    <form method="post" action="/admins">
        <div class="createWr1">
            <div class="conteiner">
            <label for="username">Input username:</label>
            <input name="username" id="username" type="text" class="form-control" placeholder="Username" autofocus="true" required />
            <br>
            </div>
            <div class="conteiner">
            <label for="password">Input password:</label>
            <input name="password" id="password" type="password" class="form-control" placeholder="Password" required />
            </div>
            <span>${errorMsg}</span>
            <div class="conteiner">
                <label for="role">Select role:</label>
                <select required name="role" id="role">
                    <option>Admin</option>
                    <option>User</option>
                </select>
            </div>
            <br>

        </div>
        <div class="forSubmit" >
            <button type = "submit" class="btn" >Create new user</button >
        </div >
    </form>
</div>
</body>
<jsp:include page="../views/footer.jsp" />

</html>