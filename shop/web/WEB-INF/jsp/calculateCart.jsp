<%--
  Created by IntelliJ IDEA.
  User: AVDEY
  Date: 20.05.2020
  Time: 13:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div class="mx-auto my-2 my-sm-3 my-lg-4 p-3">
    Подсчет стоимости покупок пользователя:
    <form method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="text" name="calc" value="${calc}"/>
        <input type="submit" class="btn btn-dark" value="найти"/>
    </form>

    <table border="1">
        <tr>
            <th>Имя пользователя</th>
            <th>общая сумма покупок</th>
        </tr>
        <tr>
<%--            <td><p><%=request.getParameter("calc")%></p></td>--%>
            <td>${calcOwner}</td>
            <td> ${total}</td>
        </tr>
    </table>
</div>


<br>
<br>


</body>
</html>
