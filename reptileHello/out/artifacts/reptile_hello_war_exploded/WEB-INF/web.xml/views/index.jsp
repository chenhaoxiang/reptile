<%--
  Created by IntelliJ IDEA.
  User: 陈浩翔
  Date: 2017/2/26
  Time: 下午 7:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>简单Reptile</title>
</head>
<body>
    <h1>百度知道数据获取</h1>
    <form action="acquire" method="post">
        <input type="text" placeholder="请输入搜索数据" class="keyword" name="keyword">
        <input type="submit" value="抓取">
    </form>
</body>
</html>
