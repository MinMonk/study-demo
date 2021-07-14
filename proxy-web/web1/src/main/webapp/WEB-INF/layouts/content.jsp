<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:url value="/" var="home"/>
<!DOCTYPE html >
<html>

	<head>
	</head>
	
  	<body style="overflow-x:hidden;" ondragstart="return false">
		<tiles:insertAttribute name="body"/>
	</body>
</html>
