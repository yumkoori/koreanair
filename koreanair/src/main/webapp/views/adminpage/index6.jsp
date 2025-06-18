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

<title>대한항공 관리자 페이지 | Index6</title>

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
				<%-- 사이드바 전체 --%>
				<jsp:include page="sidebar.jsp"></jsp:include>
			</div>

			<%-- 상단 네비게이션 전체 --%>
			<jsp:include page="topnav.jsp"></jsp:include>

			<%-- 메인 컨텐츠 --%>
			<div class="right_col" role="main">
				<div class="">
					<div class="page-title">
						<div class="title_left">
							<h3>사용자 검색</h3>
						</div>
					</div>
					<div class="clearfix"></div>
					
					<!-- 검색 패널 -->
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="x_panel">
								<div class="x_title">
									<h2>사용자 검색</h2>
									<ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<div class="row">
										<div class="col-md-8">
											<div class="input-group">
												<input type="text" class="form-control" id="userSearchInput" 
													   placeholder="사용자 이름을 입력하세요..." />
												<span class="input-group-btn">
													<button class="btn btn-primary" type="button" id="searchUserBtn">
														<i class="fa fa-search"></i> 검색
													</button>
												</span>
											</div>
										</div>
										<div class="col-md-4">
											<button type="button" class="btn btn-info" id="clearSearchBtn">
												<i class="fa fa-refresh"></i> 초기화
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<!-- 검색 결과 패널 -->
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="x_panel">
								<div class="x_title">
									<h2>검색 결과</h2>
									<div class="pull-right">
										<span class="badge bg-blue" id="resultCount">0</span> 건의 검색 결과
									</div>
									<ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<!-- 검색 결과 테이블 -->
									<div class="table-responsive">
										<table class="table table-striped table-bordered" id="userSearchResultTable">
											<!-- 테이블 헤더와 바디가 동적으로 생성됩니다 -->
										</table>
									</div>
									
									<!-- 페이지네이션 -->
									<nav aria-label="검색 결과 페이지">
										<ul class="pagination justify-content-center" id="searchPagination">
											<!-- 페이지 번호들이 여기에 동적으로 추가됩니다 -->
										</ul>
									</nav>
								</div>
							</div>
						</div>
					</div>
					
					<!-- 사용자 상세 정보 모달 -->
					<div class="modal fade" id="userDetailModal" tabindex="-1" role="dialog">
						<div class="modal-dialog modal-lg" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span>&times;</span>
									</button>
									<h4 class="modal-title">
										<i class="fa fa-user"></i> 사용자 상세 정보
									</h4>
								</div>
								<div class="modal-body">
									<div class="row">
										<div class="col-md-4">
											<div class="profile_img">
												<div id="crop-avatar">
													<img class="img-responsive avatar-view" 
														 src="images/picture.jpg" 
														 alt="Avatar" title="프로필 사진">
												</div>
											</div>
										</div>
										<div class="col-md-8">
											<table class="table table-user-information">
												<tbody>
													<tr>
														<td><strong>사용자 ID:</strong></td>
														<td id="modalUserId">-</td>
													</tr>
													<tr>
														<td><strong>이름:</strong></td>
														<td id="modalUserName">-</td>
													</tr>
													<tr>
														<td><strong>이메일:</strong></td>
														<td id="modalUserEmail">-</td>
													</tr>
													<tr>
														<td><strong>전화번호:</strong></td>
														<td id="modalUserPhone">-</td>
													</tr>
													<tr>
														<td><strong>생년월일:</strong></td>
														<td id="modalUserBirth">-</td>
													</tr>
													<tr>
														<td><strong>주소:</strong></td>
														<td id="modalUserAddress">-</td>
													</tr>
													<tr>
														<td><strong>등록일:</strong></td>
														<td id="modalUserRegDate">-</td>
													</tr>
													<tr>
														<td><strong>마지막 로그인:</strong></td>
														<td id="modalUserLastLogin">-</td>
													</tr>
													<tr>
														<td><strong>계정 상태:</strong></td>
														<td>
															<span class="label label-success" id="modalUserStatus">활성</span>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
									
									<!-- 예약 내역 -->
									<div class="row" style="margin-top: 20px;">
										<div class="col-md-12">
											<h4><i class="fa fa-plane"></i> 최근 예약 내역</h4>
											<div class="table-responsive">
												<table class="table table-striped" id="modalReservationTable">
													<!-- 예약 내역 테이블이 동적으로 생성됩니다 -->
												</table>
											</div>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-warning">
										<i class="fa fa-edit"></i> 수정
									</button>
									<button type="button" class="btn btn-danger">
										<i class="fa fa-ban"></i> 계정 정지
									</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">
										<i class="fa fa-times"></i> 닫기
									</button>
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
	
	<!-- Project JS -->
	<script src="<%=contextPath%>/views/adminpage/js/projectjs.js"></script>
	
	<script>
		// contextPath 전역 변수 설정
		const contextPath = '<%=contextPath%>';
		
		// 페이지 로드 시 이벤트 리스너 연결
		document.addEventListener('DOMContentLoaded', function() {
			console.log('페이지 로드 완료, contextPath:', contextPath);
			
			// 검색 버튼 클릭 이벤트
			const searchBtn = document.getElementById('searchUserBtn');
			if (searchBtn) {
				searchBtn.addEventListener('click', searchUsers);
				console.log('검색 버튼 이벤트 연결 완료');
			} else {
				console.error('검색 버튼을 찾을 수 없습니다.');
			}
			
			// 초기화 버튼 클릭 이벤트
			const clearBtn = document.getElementById('clearSearchBtn');
			if (clearBtn) {
				clearBtn.addEventListener('click', clearSearch);
				console.log('초기화 버튼 이벤트 연결 완료');
			} else {
				console.error('초기화 버튼을 찾을 수 없습니다.');
			}
			
			// 엔터키 검색 이벤트
			const searchInput = document.getElementById('userSearchInput');
			if (searchInput) {
				searchInput.addEventListener('keypress', function(e) {
					if (e.key === 'Enter') {
						searchUsers();
					}
				});
				console.log('검색창 엔터키 이벤트 연결 완료');
			} else {
				console.error('검색창을 찾을 수 없습니다.');
			}
		});
	</script>

</body>
</html>