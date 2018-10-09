<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<body>

<h1>Spring Boot - Upload Status</h1>

<div th:if="${message}">
    <h2 th:text="${message}"></h2>

    <input type="button" onclick="location.href='/'" value="Upload another file!">

    <form method="GET" action="/read/" enctype="multipart/form-data">
        <input type="text" name="fileName" /><br/><br/>
        <input type="submit" value="Submit" />
    </form>
</div>

</body>
</html>