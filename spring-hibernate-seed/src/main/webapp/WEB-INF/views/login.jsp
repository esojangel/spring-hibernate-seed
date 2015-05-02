<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
	<head>
		<jsp:include page="common/inc.jsp"></jsp:include>
		<link rel="stylesheet" href="css/login.css" style="text/css">
	</head>
	<body role="document">
		<div class="container">
			<div class="row loginBox"> 
				<div class="col-lg-7 col-md-7 col-sm-7 col-xs-7 left">
					<form style="width:250px;" action="<c:url value='/j_spring_security_check' />" method='POST'>
						<div class="frmTitle">
						    <h2><img alt="TLX Ststem" src="img/cat32.png"/>用户登录</h2>
							<c:if test="${not empty error}">
								<span class="label label-danger">${error}</span>
							</c:if>
							<c:if test="${not empty msg}">
								<span class="label label-warning">${msg}</span>
							</c:if>
						</div>
						<div class="frmBody">
							<div class="input-group input-group-md" style="padding-bottom: 12px;">
							  <span class="input-group-addon" aria-hidden="true">
							  	<span class="glyphicon glyphicon-user"></span>
							  </span>
							  <input type="text" class="form-control" name="username" placeholder="用户名" aria-describedby="basic-addon2">
							</div>
							<div class="input-group input-group-md" style="padding-bottom: 12px;">
							  <span class="input-group-addon" aria-hidden="true">
							  	<span class="glyphicon glyphicon-lock"></span>
							  </span>
							  <input type="password" class="form-control" name="password" placeholder="密码" aria-describedby="basic-addon2">
							</div>
							<div style="padding-bottom: 10px;">
								<label><input type="checkbox" id="chkRememberMe" name="_remember_me" style="margin-right: 3px;" />下次自动登录</label>
								<button type="button" class="btn btn-link pull-right" style="padding-top: 2px;">忘记密码？</button>
							</div>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</div>
						<div style="padding-bottom: 30px;" id="divSubmit">
							<input type="submit" style="font-size: 16px;" value="登&nbsp;&nbsp;&nbsp;&nbsp;录 " class="btn btn-primary btn-block">
						</div>
					</form>
				</div>
				<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
					<form action="<c:url value='/j_spring_security_check' />" method='POST'>
						<div class="frmTitle">
						   <h2>没有帐户？</h2>
						</div>
						<div class="frmBody">
						  <span>
						  	通过邮箱注册可以免费使用本系统，如在使用中发现问题或有任何建议请发送邮件到zhangjunjie5200@163.com联系系统管理员。
						  </span>
						</div>
						<div>
							<input type="button" value="注&nbsp;&nbsp;&nbsp;&nbsp;册 " class="btn btn-success btn-block regBtn" style="font-size: 16px;">
						</div>
					</form>
				</div>
			</div>
			<!-- /loginBox -->
		</div>
		<!-- /container -->
	</body>
</html>