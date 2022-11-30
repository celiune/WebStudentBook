<%--
  Created by IntelliJ IDEA.
  User: celin
  Date: 25/11/2022
  Time: 09:47
  To change this template use File | Settings | File Templates.
--%>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Web Student Tracker</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
<!-- ${STUDENT_LIST}-->
<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>
<div id="container">
    <div id="content">
        <table>
            <tr>
                <th>First Name </th>
                <th>Last Name</th>
                <th>Email </th>
            </tr>
            <c:forEach var="tempStudent" items="${STUDENT_LIST }" >
            <tr>
                <td> ${tempStudent.firstName}</td>
                <td> ${tempStudent.lastName}</td>
                <td> ${tempStudent.email}</td>
                </c:forEach>
        </table>
    </div>
    <form name = "BtnForm" action = "add-student.jsp" method="post">
        <button>Add student</button>
    </form>
</div>
</body>
</html>