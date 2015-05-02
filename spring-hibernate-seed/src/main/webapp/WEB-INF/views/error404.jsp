 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
  <head>
    <jsp:include page="common/inc.jsp"></jsp:include>
    <link href="css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="css/theme.css" rel="stylesheet">
  </head>

  <body role="document">
	<div class="container-fluid">
		<div class="row" style="padding: 100px 50px 100px 50px">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="panel panel-danger">
					<div class="panel-heading">
						<div class="panel-title">404</div>
					</div>
				  	<div class="panel-body">
						<h4 style="padding-bottom: 100px;">The page you request is not found! Click <a href="javascript:history.go(-1);" class="alert-link">here</a> and back to the front page.</h4>
					</div>
				</div>
			</div>
  		</div>
  	</div>

<jsp:include page="common/footer.jsp"></jsp:include>