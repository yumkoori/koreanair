<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
request.setCharacterEncoding("UTF-8");

String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>대한항공 관리자 페이지 | Index3</title>

<link
	href="<%=contextPath%>/views/vendors/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="<%=contextPath%>/views/vendors/font-awesome/css/font-awesome.min.css"
	rel="stylesheet">
<link href="<%=contextPath%>/views/vendors/nprogress/nprogress.css"
	rel="stylesheet">
<link
	href="<%=contextPath%>/views/vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css"
	rel="stylesheet">
<link
	href="<%=contextPath%>/views/vendors/bootstrap-daterangepicker/daterangepicker.css"
	rel="stylesheet">

<link href="<%=contextPath%>/views/build/css/custom.min.css"
	rel="stylesheet">
</head>

<body class="nav-md">
	<div class="container body">
		<div class="main_container">
			<div class="col-md-3 left_col">
				{/* --- 사이드바 전체 --- */}
				<jsp:include page="sidebar.jsp"></jsp:include>
			</div>

			{/* --- 상단 네비게이션 전체 --- */}
			<jsp:include page="topnav.jsp"></jsp:include>

			{/* --- 메인 컨텐츠 --- */}
			<div class="right_col" role="main">
				<div class="">
					<div class="page-title">
						<div class="title_left">
							<h3>Index3 페이지</h3>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="row">
						<div class="col-md-12 col-sm-12 ">
							<div class="x_panel">
								<div class="x_title">
									<h2>Index3 컨텐츠</h2>
									<ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i
												class="fa fa-chevron-up"></i></a></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<p>여기에 Index3 페이지의 컨텐츠를 추가하세요.</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /page content -->

			<!-- footer content -->
			<footer>
				<div class="pull-right">
					Gentelella - Bootstrap Admin Template by <a
						href="https://colorlib.com">Colorlib</a>
				</div>
				<div class="clearfix"></div>
			</footer>
			<!-- /footer content -->
		</div>
	</div>

	<!-- jQuery -->
	<script src="<%=contextPath%>/views/vendors/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="<%=contextPath%>/views/vendors/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
	<!-- FastClick -->
	<script src="<%=contextPath%>/views/vendors/fastclick/lib/fastclick.js"></script>
	<!-- NProgress -->
	<script src="<%=contextPath%>/views/vendors/nprogress/nprogress.js"></script>
	<!-- bootstrap-progressbar -->
	<script src="<%=contextPath%>/views/vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
	<!-- bootstrap-daterangepicker -->
	<script src="<%=contextPath%>/views/vendors/moment/min/moment.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

	<!-- Custom Theme Scripts -->
<<<<<<< HEAD
	<script src="<%=contextPath%>/views/build/js/custom.js"></script>
=======
	<script src="<%=contextPath%>/views/build/js/custom.min.js"></script>
>>>>>>> cd5ba6535013433d0eef20955581fa8717c00dbc

</body>
</html> 