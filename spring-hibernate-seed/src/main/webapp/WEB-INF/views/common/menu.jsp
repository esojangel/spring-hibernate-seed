<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Jay Admin v0.1</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Users <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="new-user.html">New User</a></li>
				<li class="divider"></li>
				<li><a href="users.html">Manage Users</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Roles <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="new-role.html">New Role</a></li>
				<li class="divider"></li>
				<li><a href="roles.html">Manage Roles</a></li>
              </ul>
            </li>
            <li><a href="stats.html">Stats</a></li>
            <li><a href="stats.html">Help</a></li>
            <li><a href="stats.html">About</a></li>
          </ul>
          <form class="navbar-form navbar-left" action="<c:url value='/' />" role="search">
        	<div class="input-group">
		   		<input type="text" class="form-control" placeholder="Search...">
		    	<span class="input-group-btn">
		        	<button class="btn btn-default" type="submit">&nbsp;<i class="glyphicon glyphicon-search" ></i></button>
		     	</span>
		  	</div>
	      </form>
          <div class="nav navbar-nav navbar-right" style="padding-top: 8px;padding-right: 8px;">
			<div class="btn-group" role="group">
			    <button id="btnLogout" type="button" class="btn btn-default "aria-expanded="false">
			      <i class="glyphicon glyphicon-user" ></i>${pageContext.request.userPrincipal.name}
			    </button>
			    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" >
			    	<span class="caret"></span>&nbsp;
			    </button>
			    <ul class="dropdown-menu" role="menu">
			      <li><a href="#"><i class="glyphicon glyphicon-user"></i>Profile</a></li>
			      <li><a href="#"><i class="glyphicon glyphicon-cog"></i>Setting</a></li>
			      <li><a href="javascript:formSubmit()"><i class="glyphicon glyphicon-off"></i>Logout</a></li>
			    </ul>
		 	</div><!-- /btn-group -->
          </div>
        </div><!-- /nav -->
      </div>
    </nav>

	<form action="<c:url value='/logout' />" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>