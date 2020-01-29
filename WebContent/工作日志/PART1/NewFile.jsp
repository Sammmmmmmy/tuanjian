<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
	</script>
	<script>
	function show()
	{
		$.ajax({
		type:"POST",
		url:"servlet/part1_1",
		success:function(data,status){},
		data:{
				flag:0,
				Class:"2018-AI"
		},
		error:function(data,status){}
		});
	}
	function myFunction()
	{
	    alert("Hello World!");
	}
	</script>
<button id = "button" onclick="show()" >测试</button>
<form action="servlet/part1_1" method="post">
	<input type="submit" value="Post方式请求HelloServlet">
</form>
</body>
</html>