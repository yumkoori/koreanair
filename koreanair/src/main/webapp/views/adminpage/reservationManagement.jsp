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

<title>대한항공 관리자 페이지 | 예약 관리</title>

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
							<h3>예약 관리</h3>
						</div>
					</div>
					<div class="clearfix"></div>
					
					<!-- 검색 패널 -->
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="x_panel">
								<div class="x_title">
									<h2>예약 검색</h2>
									<ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<div class="row">
										<div class="col-md-3">
											<div class="form-group">
												<label>검색 유형</label>
												<select class="form-control" id="searchType">
													<option value="reservationId">예약번호</option>
													<option value="userName">사용자명</option>
													<option value="userEmail">이메일</option>
													<option value="phone">연락처</option>
												</select>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label>검색어</label>
												<div class="input-group">
													<input type="text" class="form-control" id="reservationSearchInput" 
														   placeholder="검색어를 입력하세요..." />
													<span class="input-group-btn">
														<button class="btn btn-primary" type="button" id="searchReservationBtn">
															<i class="fa fa-search"></i> 검색
														</button>
													</span>
												</div>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label>예약 상태</label>
												<select class="form-control" id="reservationStatus">
													<option value="">전체</option>
													<option value="confirmed">확정</option>
													<option value="pending">대기</option>
													<option value="cancelled">취소</option>
													<option value="completed">완료</option>
												</select>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<button type="button" class="btn btn-info" id="clearSearchBtn">
												<i class="fa fa-refresh"></i> 초기화
											</button>
											<button type="button" class="btn btn-success" id="loadAllReservationsBtn">
												<i class="fa fa-list"></i> 전체 예약 조회
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
									<h2>예약 목록</h2>
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
										<table class="table table-striped table-bordered" id="reservationSearchResultTable" style="text-align: center;">
											<thead>
												<tr style="text-align: center;">
													<th style="text-align: center; vertical-align: middle;">예약번호</th>
													<th style="text-align: center; vertical-align: middle;">사용자명</th>
													<th style="text-align: center; vertical-align: middle;">이메일</th>
													<th style="text-align: center; vertical-align: middle;">연락처</th>
													<th style="text-align: center; vertical-align: middle;">출발지</th>
													<th style="text-align: center; vertical-align: middle;">도착지</th>
													<th style="text-align: center; vertical-align: middle;">출발일</th>
													<th style="text-align: center; vertical-align: middle;">예약일</th>
													<th style="text-align: center; vertical-align: middle;">상태</th>
													<th style="text-align: center; vertical-align: middle;">좌석 등급</th>
													<th style="text-align: center; vertical-align: middle;">승객 수</th>
													<th style="text-align: center; vertical-align: middle;">총 금액</th>
													<th style="text-align: center; vertical-align: middle;">관리</th>
												</tr>
											</thead>
											<tbody id="reservationTableBody" style="text-align: center; vertical-align: middle;">
												<!-- 검색 결과가 여기에 동적으로 추가됩니다 -->
											</tbody>
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
					
					<!-- 예약 상세 정보 모달 -->
					<div class="modal fade" id="reservationDetailModal" tabindex="-1" role="dialog">
						<div class="modal-dialog modal-lg" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span>&times;</span>
									</button>
									<h4 class="modal-title">
										<i class="fa fa-plane"></i> 예약 상세 정보
									</h4>
								</div>
								<div class="modal-body">
									<div class="row">
										<div class="col-md-6">
											<h5><i class="fa fa-info-circle"></i> 예약 정보</h5>
											<table class="table table-striped" style="text-align: center;">
												<tbody style="vertical-align: middle;">
													<tr>
														<td style="vertical-align: middle;"><strong>예약번호:</strong></td>
														<td style="vertical-align: middle;" id="modalReservationId">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>예약일:</strong></td>
														<td style="vertical-align: middle;" id="modalReservationDate">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>예약 상태:</strong></td>
														<td style="vertical-align: middle;">
															<span class="label" id="modalReservationStatus">-</span>
														</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>총 금액:</strong></td>
														<td style="vertical-align: middle;" id="modalTotalAmount">-</td>
													</tr>
												</tbody>
											</table>
										</div>
										<div class="col-md-6">
											<h5><i class="fa fa-user"></i> 예약자 정보</h5>
											<table class="table table-striped" style="text-align: center;">
												<tbody style="vertical-align: middle;">
													<tr>
														<td style="vertical-align: middle;"><strong>이름:</strong></td>
														<td style="vertical-align: middle;" id="modalUserName">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>이메일:</strong></td>
														<td style="vertical-align: middle;" id="modalUserEmail">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>연락처:</strong></td>
														<td style="vertical-align: middle;" id="modalUserPhone">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>생년월일:</strong></td>
														<td style="vertical-align: middle;" id="modalUserBirth">-</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
									
									<!-- 항공편 정보 -->
									<div class="row" style="margin-top: 20px;">
										<div class="col-md-12">
											<h5><i class="fa fa-plane"></i> 항공편 정보</h5>
											<table class="table table-striped" style="text-align: center;">
												<tbody style="vertical-align: middle;">
													<tr>
														<td style="vertical-align: middle;"><strong>항공편명:</strong></td>
														<td style="vertical-align: middle;" id="modalFlightNumber">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>출발지:</strong></td>
														<td style="vertical-align: middle;" id="modalDeparture">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>도착지:</strong></td>
														<td style="vertical-align: middle;" id="modalArrival">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>출발일시:</strong></td>
														<td style="vertical-align: middle;" id="modalDepartureTime">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>도착일시:</strong></td>
														<td style="vertical-align: middle;" id="modalArrivalTime">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>좌석 등급:</strong></td>
														<td style="vertical-align: middle;" id="modalSeatClass">-</td>
													</tr>
													<tr>
														<td style="vertical-align: middle;"><strong>승객 수:</strong></td>
														<td style="vertical-align: middle;" id="modalPassengerCount">-</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-info" id="editReservationBtn">
										<i class="fa fa-edit"></i> 수정
									</button>
									<button type="button" class="btn btn-danger" id="deleteReservationBtn">
										<i class="fa fa-trash"></i> 삭제
									</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">
										<i class="fa fa-times"></i> 닫기
									</button>
								</div>
							</div>
						</div>
					</div>
					
					<!-- 예약 삭제 확인 모달 -->
					<div class="modal fade" id="deleteConfirmModal" tabindex="-1" role="dialog">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span>&times;</span>
									</button>
									<h4 class="modal-title">
										<i class="fa fa-warning text-danger"></i> 예약 삭제 확인
									</h4>
								</div>
								<div class="modal-body">
									<p>정말로 이 예약을 삭제하시겠습니까?</p>
									<p><strong>예약번호:</strong> <span id="deleteReservationId"></span></p>
									<p><strong>예약자:</strong> <span id="deleteReservationUser"></span></p>
									<div class="alert alert-warning">
										<i class="fa fa-warning"></i> 
										<strong>주의:</strong> 삭제된 예약은 복구할 수 없습니다.
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-danger" id="confirmDeleteBtn">
										<i class="fa fa-trash"></i> 삭제
									</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">
										<i class="fa fa-times"></i> 취소
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
	<script src="<%=contextPath%>/views/build/js/custom.js"></script>
	
	<!-- Project JS -->
	<script src="<%=contextPath%>/views/adminpage/js/projectjs.js"></script>
	
	<!-- Reservation Management JS -->
	<script src="<%=contextPath%>/views/adminpage/js/reservation.js"></script>
	
	<script>
		// contextPath 전역 변수 설정
		const contextPath = '<%=contextPath%>';
		
		// 페이지 로드 시 이벤트 리스너 연결
		document.addEventListener('DOMContentLoaded', function() {
			console.log('예약 관리 페이지 로드 완료, contextPath:', contextPath);
			
			// reservation.js에서 초기화 함수 호출
			if (typeof initReservationManagement === 'function') {
				initReservationManagement();
			} else {
				console.error('reservation.js가 로드되지 않았습니다.');
			}
		});
	</script>

</body>
</html> 