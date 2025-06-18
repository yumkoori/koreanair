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

<title>Gentelella Alela! | 비행 스케줄 현황</title>

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

<style>
.flight-schedule-container {
	display: flex;
	flex-direction: column;
}

.query-controls-container {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	margin-bottom: 20px;
	gap: 15px;
}

.calendar-input-container {
	flex: 0 0 230px;
}

#flightFilterContainer .btn {
	margin-right: 5px;
	margin-bottom: 5px;
}

.schedule-table-container {
	flex: 1;
	min-width: 0;
}

#dateSelectorInput {
	cursor: pointer;
	background-color: #fff !important;
}

.table-responsive {
	overflow-x: auto;
}

.pagination>.active>a, .pagination>.active>a:focus, .pagination>.active>a:hover,
	.pagination>.active>span, .pagination>.active>span:focus, .pagination>.active>span:hover
	{
	background-color: #1ABB9C;
	border-color: #1ABB9C;
}

.loading-overlay {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(255, 255, 255, 0.7);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.loading-overlay .fa {
	font-size: 2em;
	color: #1ABB9C;
}
</style>
</head>

<body class="nav-md">
	<div class="container body">
		<div class="main_container">
			<div class="col-md-3 left_col">
				{/* --- 사이드바 전체 --- */}
				<jsp:include page="sidebar.jsp"></jsp:include>

			{/* --- 상단 네비게이션 전체 --- */}
			<jsp:include page="topnav.jsp"></jsp:include>

			{/* --- 메인 컨텐츠 --- */}
			<div class="right_col" role="main">
				<div class="">
					<div class="page-title">
						<div class="title_left">
							<h3>실시간 비행 스케줄 현황</h3>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="row">
						<div class="col-md-12 col-sm-12 ">
							<div class="x_panel">
								<div class="x_title">
									<h2>조회 조건</h2>
									<div class="pull-right" style="margin-right: 50px;">
										<button type="button" class="btn btn-success" id="saveToDbBtn"
											style="margin-right: 10px;">
											<i class="fa fa-database"></i> DB에 저장하기
										</button>
										<button type="button" class="btn btn-primary" id="syncDataBtn">
											<i class="fa fa-refresh"></i> 최신데이터 동기화
										</button>
									</div>
									<ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i
												class="fa fa-chevron-up"></i></a></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<div class="flight-schedule-container">
										<div class="query-controls-container">
											<div class="calendar-input-container">
												<label for="dateSelectorInput"
													style="display: block; margin-bottom: 5px;">날짜 선택</label>
												<div class="input-group">
													<input type="text" id="dateSelectorInput"
														class="form-control" readonly placeholder="날짜를 선택하세요">
													<span class="input-group-addon" style="cursor: pointer;">
														<span class="glyphicon glyphicon-calendar"></span>
													</span>
												</div>
											</div>
											<div id="flightFilterContainer" class="btn-group"
												role="group" aria-label="Flight Type Filters">
												<label style="display: block; margin-bottom: 5px;">유형
													선택</label>
												<div>
													<button type="button"
														class="btn btn-default filter-btn active"
														data-filter="all">전체 스케줄</button>
													<button type="button" class="btn btn-info filter-btn"
														data-filter="realtime">실시간 현황</button>
												</div>
											</div>
										</div>

										<div class="schedule-table-container">
											<h4 id="scheduleDateDisplay">오늘의 전체 스케줄</h4>
											<div class="table-responsive">
												<table class="table table-striped table-bordered"
													id="flightScheduleTable">
													<thead>
														<tr>
															<th>편명</th>
															<th>항공사</th>
															<th>출발지</th>
															<th>도착지</th>
															<th>출발시간</th>
															<th>도착시간</th>
															<th>상태</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td colspan="7" class="text-center">날짜와 필터를 선택해주세요.</td>
														</tr>
													</tbody>
												</table>
											</div>
											<nav aria-label="Page navigation">
												<ul class="pagination justify-content-center"
													id="paginationControls"></ul>
											</nav>
											<div id="loadingIndicator" class="loading-overlay"
												style="display: none; position: relative; min-height: 100px;">
												<i class="fa fa-spinner fa-spin"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			{/* --- 푸터 전체 --- */}
			<footer>
				<div class="pull-right">
					Gentelella - Bootstrap Admin Template by <a
						href="https://colorlib.com">Colorlib</a>
				</div>
				<div class="clearfix"></div>
			</footer>
		</div>
	</div>

	{/* --- JavaScript 라이브러리 로드 전체 --- */}
	<script src="<%=contextPath%>/views/vendors/jquery/dist/jquery.min.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/fastclick/lib/fastclick.js"></script>
	<script src="<%=contextPath%>/views/vendors/nprogress/nprogress.js"></script>
	<script src="<%=contextPath%>/views/vendors/Chart.js/dist/Chart.min.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/raphael/raphael.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/morris.js/morris.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/gauge.js/dist/gauge.min.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
	<script src="<%=contextPath%>/views/vendors/skycons/skycons.js"></script>
	<script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.js"></script>
	<script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.pie.js"></script>
	<script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.time.js"></script>
	<script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.stack.js"></script>
	<script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.resize.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/flot.orderbars/js/jquery.flot.orderBars.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/flot-spline/js/jquery.flot.spline.min.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/flot.curvedlines/curvedLines.js"></script>
	<script src="<%=contextPath%>/views/vendors/DateJS/build/date.js"></script>
	<script src="<%=contextPath%>/views/vendors/moment/min/moment.min.js"></script>
	<script
		src="<%=contextPath%>/views/vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

	<script src="<%=contextPath%>/views/build/js/custom.js"></script>



	<script>
    // --- 1. 핸들러가 넘겨준 데이터를 JavaScript 변수로 바로 받기 (수정된 방식) ---
    // 핸들러에서 request.setAttribute("initialData", jsonString)으로 넘겨준 값입니다.
    // 이 방식이 더 간단하고 안전합니다.
    var initialData = <c:out value="${initialData}" default="[]" escapeXml="false"/>;


    $(document).ready(function() {
        const ITEMS_PER_PAGE = 50;
        let currentPage = 1;
        let currentSchedules = [];
        
        // 페이지 로딩 시의 날짜는 서버에서 데이터를 가져온 날짜(오늘)로 설정합니다.
        let selectedDate = moment().format('YYYY-MM-DD');
        let currentFilterType = 'all'; 

        const $loadingIndicator = $('#loadingIndicator');
        const $dateSelectorInput = $('#dateSelectorInput');
        const $flightScheduleTableBody = $('#flightScheduleTable tbody');
        const $paginationControls = $('#paginationControls');
        const $scheduleDateDisplay = $('#scheduleDateDisplay');
        const $filterButtons = $('.filter-btn');

        // 로딩 상태 표시 - 데이터 로드 중일 때 스피너와 로딩 메시지를 보여줌
        function showLoading() {
            $loadingIndicator.show();
            $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">운항 스케줄을 불러오고 있습니다...</td></tr>');
            $paginationControls.empty();
        }

        // 로딩 상태 숨김 - 데이터 로드 완료 후 로딩 인디케이터를 숨김
        function hideLoading() {
            $loadingIndicator.hide();
        }

        // 스케줄 제목 업데이트 - 선택된 날짜와 필터에 따라 제목을 동적으로 변경
        function updateScheduleDisplayTitle(dateStr, filter) {
            let dateFormatted = moment(dateStr).format("YYYY년 MM월 DD일");
            let filterText = "";
            
            if (filter === 'realtime') {
                filterText = " 실시간 현황";
            } else {
                filterText = " 전체 스케줄";
            }
            $scheduleDateDisplay.text(dateFormatted + filterText);
        }

        $dateSelectorInput.daterangepicker({
            singleDatePicker: true,
            showDropdowns: true,
            autoUpdateInput: false,
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: "선택",
                cancelLabel: "취소",
                daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
                monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
            }
        });

        $dateSelectorInput.on('apply.daterangepicker', function(ev, picker) {
            const newlySelectedDate = picker.startDate.format('YYYY-MM-DD');
            console.log('달력에서 선택된 날짜:', newlySelectedDate);
            selectedDate = newlySelectedDate; 
            $(this).val(selectedDate);  

            // 사용자가 선택한 필터 타입을 그대로 유지 (강제로 변경하지 않음)
            console.log('달력 선택 - 날짜:', selectedDate, '현재 필터:', currentFilterType);
            loadAndDisplaySchedules(selectedDate, currentFilterType);
        });
        
        $('.input-group-addon').on('click', function() {
            $dateSelectorInput.trigger('click'); 
        });

        $filterButtons.on('click', function() {
            const $this = $(this);
            const newFilterType = $this.data('filter');

            $filterButtons.removeClass('active btn-success btn-info').addClass('btn-default');
            if (newFilterType === 'realtime') {
                $this.removeClass('btn-default').addClass('active btn-info');
            } else {
                $this.removeClass('btn-default').addClass('active btn-success');
            }

            // 사용자가 선택한 날짜를 그대로 사용 (강제로 오늘 날짜로 변경하지 않음)
            currentFilterType = newFilterType; 
            
            // 달력에서 선택된 날짜가 있으면 그 날짜를 사용, 없으면 현재 selectedDate 사용
            const dateToUse = $dateSelectorInput.val() || selectedDate;
            console.log('필터 버튼 클릭 - 사용할 날짜:', dateToUse, '필터 타입:', newFilterType);
            
            loadAndDisplaySchedules(dateToUse, currentFilterType); 
        });

        // 스케줄 데이터 로드 및 표시 - 서버에서 스케줄 데이터를 가져와서 테이블에 표시
        function loadAndDisplaySchedules(dateToQuery, filterToApply) {
            console.log('loadAndDisplaySchedules 호출됨 - 날짜:', dateToQuery, '필터:', filterToApply);
            
            // 전역 변수 업데이트
            selectedDate = dateToQuery;
            currentFilterType = filterToApply;
            
            // 달력 입력 필드도 업데이트
            $dateSelectorInput.val(selectedDate);
            
            showLoading();
            updateScheduleDisplayTitle(dateToQuery, filterToApply);

            // 실시간 데이터는 핸들러를 통해 가져오기
            if (filterToApply === 'realtime') {
                $.ajax({
                    url: '${pageContext.request.contextPath}/adminpage/index5.wi',
                    type: 'GET',
                    data: { 
                        date: dateToQuery,
                        flightType: 'realtime'
                    },
                    dataType: 'json',
                    cache: false,
                    headers: {
                        'Accept': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    success: function(data) {
                        currentSchedules = [];
                        if (Array.isArray(data) && data.length > 0 && data[0] && data[0].error) {
                            $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">실시간 데이터 조회 실패: ' + data[0].error + '</td></tr>');
                        } else if (Array.isArray(data)) {
                            currentSchedules = data;
                        } else {
                            $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">잘못된 형식의 응답입니다.</td></tr>');
                        }
                        
                        currentPage = 1; 
                        renderTable();
                        renderPagination();
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error('AJAX 오류:', textStatus, errorThrown);
                        console.error('응답 상태:', jqXHR.status);
                        console.error('응답 텍스트:', jqXHR.responseText);
                        $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">데이터를 불러오는 중 오류가 발생했습니다. (서버 응답 코드: ' + jqXHR.status + ')</td></tr>');
                        currentSchedules = [];
                        renderTable();
                        renderPagination();
                    },
                    complete: function() {
                        hideLoading();
                    }
                });
                          } else {
                                  // 기본 스케줄 데이터는 기존 핸들러 사용
                $.ajax({
                    url: '${pageContext.request.contextPath}/adminpage/index5.wi',
                    type: 'GET',
                    data: { 
                        date: dateToQuery,
                        flightType: filterToApply 
                    },
                    dataType: 'json',
                    cache: false,
                    headers: {
                        'Accept': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    success: function(data) {
                        currentSchedules = [];
                        if (Array.isArray(data) && data.length > 0 && data[0] && data[0].error) {
                            $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">스케줄 조회 실패: ' + data[0].error + '</td></tr>');
                        } else if (Array.isArray(data)) {
                            currentSchedules = data;
                        } else {
                            $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">잘못된 형식의 응답입니다.</td></tr>');
                        }
                        
                        currentPage = 1; 
                        renderTable();
                        renderPagination();
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error('AJAX 오류:', textStatus, errorThrown);
                        console.error('응답 상태:', jqXHR.status);
                        console.error('응답 텍스트:', jqXHR.responseText);
                        $flightScheduleTableBody.empty().append('<tr><td colspan="7" class="text-center">스케줄을 불러오는 중 오류가 발생했습니다. (서버 응답 코드: ' + jqXHR.status + ')</td></tr>');
                        currentSchedules = [];
                        renderTable();
                        renderPagination();
                    },
                    complete: function() {
                        hideLoading();
                    }
                });
            }
        }

        // 테이블 렌더링 - 현재 페이지에 해당하는 스케줄 데이터를 테이블에 표시
        function renderTable() {
            if (isNaN(parseInt(currentPage, 10))) currentPage = 1;
            $flightScheduleTableBody.empty();

            if (!Array.isArray(currentSchedules) || currentSchedules.length === 0) {
                 $flightScheduleTableBody.append('<tr><td colspan="7" class="text-center">선택하신 조건에 해당하는 스케줄이 없습니다.</td></tr>');
                return;
            }
            
            if (currentSchedules[0] && currentSchedules[0].error) {
                 $flightScheduleTableBody.append('<tr><td colspan="7" class="text-center">' + currentSchedules[0].error + '</td></tr>');
                return;
            }

            const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
            const endIndex = startIndex + ITEMS_PER_PAGE;
            const pageSchedules = currentSchedules.slice(startIndex, endIndex);
            
            if (pageSchedules.length === 0 && currentSchedules.length > 0) {
                 $flightScheduleTableBody.append('<tr><td colspan="7" class="text-center">해당 페이지에 스케줄이 없습니다.</td></tr>');
                 return;
            }
            
            let rowsHtml = "";
            pageSchedules.forEach(function(flight) {
                rowsHtml += '<tr>' +
                                '<td>' + (flight.flightNo || 'N/A') + '</td>' +
                                '<td>' + (flight.airline || 'N/A') + '</td>' +
                                '<td>' + (flight.origin || 'N/A') + '</td>' +
                                '<td>' + (flight.destination || 'N/A') + '</td>' +
                                '<td>' + (flight.departureTime || 'N/A') + '</td>' +
                                '<td>' + (flight.arrivalTime || 'N/A') + '</td>' +
                                '<td>' + (flight.status || 'N/A') + '</td>' +
                              '</tr>';
            });
            $flightScheduleTableBody.html(rowsHtml);
        }

        // 페이지네이션 렌더링 - 전체 데이터 수에 따라 페이지 번호 버튼들을 생성
        function renderPagination() {
            if (isNaN(parseInt(currentPage, 10))) currentPage = 1;
            $paginationControls.empty(); 
            const totalPages = Math.ceil(currentSchedules.length / ITEMS_PER_PAGE); 
            if (totalPages <= 1) { return; }

            if (currentPage > 1) { $paginationControls.append('<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage - 1) + '">&laquo;</a></li>'); } 
            else { $paginationControls.append('<li class="page-item disabled"><span class="page-link">&laquo;</span></li>'); }
            let startPage = Math.max(1, currentPage - 2);
            let endPage = Math.min(totalPages, currentPage + 2);
            if (endPage - startPage + 1 < 5) { 
                if (startPage === 1) endPage = Math.min(totalPages, startPage + 4);
                else if (endPage === totalPages) startPage = Math.max(1, endPage - 4);
            }
            if (startPage > 1) { 
                $paginationControls.append('<li class="page-item"><a class="page-link" href="#" data-page="1">1</a></li>');
                if (startPage > 2) $paginationControls.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
            }
            for (let i = startPage; i <= endPage; i++) { 
                const activeClass = (i === currentPage) ? 'active' : ''; 
                $paginationControls.append('<li class="page-item ' + activeClass + '"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>');
            }
            if (endPage < totalPages) { 
                if (endPage < totalPages - 1) $paginationControls.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
                $paginationControls.append('<li class="page-item"><a class="page-link" href="#" data-page="' + totalPages + '">' + totalPages + '</a></li>');
            }
            if (currentPage < totalPages) { $paginationControls.append('<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage + 1) + '">&raquo;</a></li>'); } 
            else { $paginationControls.append('<li class="page-item disabled"><span class="page-link">&raquo;</span></li>'); }
        }

        // 페이지네이션 클릭 이벤트 핸들러 - 페이지 번호 클릭 시 해당 페이지의 데이터를 표시
        $paginationControls.on('click', 'a.page-link', function(e) {
            e.preventDefault();
            const page = parseInt($(this).data('page'), 10);
            if (!isNaN(page) && page !== currentPage) {
                currentPage = page;
                renderTable();
                renderPagination();
                $('html, body').animate({ scrollTop: $(".schedule-table-container").offset().top - 70 }, 300);
            }
        });

        // --- 페이지 첫 로딩 시 실행될 로직 ---
        // 페이지 로드 시 바로 오늘 날짜의 전체 스케줄을 로드
        $dateSelectorInput.val(selectedDate); 
        $filterButtons.filter('[data-filter="all"]').removeClass('btn-default').addClass('active btn-success'); 
        updateScheduleDisplayTitle(selectedDate, 'all');
        
        // 초기 데이터가 있으면 바로 표시, 없으면 서버에서 가져오기
        if (initialData && Array.isArray(initialData) && initialData.length > 0) {
            currentSchedules = initialData;
            renderTable();
            renderPagination();
        } else {
            // 초기 데이터가 없으면 서버에서 오늘 날짜의 전체 스케줄을 가져오기
            loadAndDisplaySchedules(selectedDate, 'all');
        }

        $('ul.nav.side-menu a[href$="index5.html"]').closest('li').removeClass('current-page active');
        $('ul.nav.side-menu a[href*="index5.wi"]').closest('li').addClass('current-page');
        $('ul.nav.side-menu a[href*="index5.wi"]').parents('li').addClass('active');

        // --- 버튼 이벤트 핸들러 ---
        
        // DB 저장 버튼 이벤트 핸들러 - 현재 스케줄 데이터를 JSON 형태로 서버에 전송하여 DB에 저장
        $('#saveToDbBtn').on('click', function() {
            const $btn = $(this);
            
            // 현재 표시된 스케줄 데이터가 있는지 확인
            if (!currentSchedules || currentSchedules.length === 0) {
                alert('저장할 스케줄 데이터가 없습니다.');
                return;
            }
            
            // 버튼 비활성화 및 로딩 상태 표시
            $btn.prop('disabled', true);
            $btn.html('<i class="fa fa-spinner fa-spin"></i> 저장 중...');
            
            // 저장할 데이터 준비
            const dataToSave = {
                schedules: currentSchedules,
                date: selectedDate,
                filterType: currentFilterType,
                timestamp: new Date().toISOString()
            };
            console.log("저장할 스케줄 데이터:", currentSchedules);
            alert(JSON.stringify(currentSchedules[0], null, 2)); // 첫 번째 항목 확인용
            const contextPath = "${pageContext.request.contextPath}";
            const url = contextPath + "/saveschedulesdb.wi";
            console.log("최종 요청 URL:", url);
            // fetch를 사용하여 JSON 데이터 전송
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(dataToSave)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 응답 오류: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
            	if (data.status === "success") {
                    alert('스케줄 데이터가 성공적으로 저장되었습니다.');
                } else {
                    alert('저장 실패: ' + (data.message || '알 수 없는 오류'));
                }
            })
            .catch(error => {
                console.error('DB 저장 오류:', error);
                alert('DB 저장 중 오류가 발생했습니다: ' + error.message);
            })
            .finally(() => {
                // 버튼 상태 복원
                $btn.prop('disabled', false);
                $btn.html('<i class="fa fa-database"></i> DB에 저장하기');
            });
        });
        
        // 데이터 동기화 버튼 이벤트 핸들러 - 기존 데이터와 비교하여 새로운 데이터만 추가
        $('#syncDataBtn').on('click', function() {
            const $btn = $(this);
            
            // 버튼 비활성화 및 로딩 상태 표시
            $btn.prop('disabled', true);
            $btn.html('<i class="fa fa-spinner fa-spin"></i> 동기화 중...');
            
            const contextPath = "${pageContext.request.contextPath}";
            
            // 1단계: 현재 데이터 가져오기 (index5.wi)
            fetch(contextPath + "/adminpage/index5.wi?" + new URLSearchParams({
                date: selectedDate,
                flightType: currentFilterType
            }), {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('첫 번째 요청 실패: ' + response.status);
                }
                return response.json();
            })
            .then(currentData => {
                console.log('1단계 - 현재 데이터 가져오기 완료:', currentData);
                
                // 2단계: 새로운 데이터 확인 및 추가 (refreshschedules.wi)
                return fetch(contextPath + "/refreshschedules.wi", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify({
                        currentSchedules: currentData,
                        date: selectedDate,
                        filterType: currentFilterType
                    })
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('두 번째 요청 실패: ' + response.status);
                    }
                    return response.json();
                })
                .then(refreshResult => {
                    console.log('2단계 - 새로운 데이터 확인 완료:', refreshResult);
                    
                    if (refreshResult.status === 'success') {
                        // 새로운 데이터가 있는 경우
                        if (refreshResult.newSchedules && refreshResult.newSchedules.length > 0) {
                            // 기존 스케줄에 새로운 스케줄 추가
                            const addedCount = refreshResult.newSchedules.length;
                            currentSchedules = [...currentSchedules, ...refreshResult.newSchedules];
                            
                            // 테이블과 페이지네이션 다시 렌더링
                            renderTable();
                            renderPagination();
                            
                            alert(`동기화 완료! ${addedCount}개의 새로운 스케줄이 추가되었습니다.`);
                        } else {
                            alert('동기화 완료! 새로운 스케줄이 없습니다.');
                        }
                    } else {
                        alert('새로 추가된 스케줄이 없습니다.');
                    }
                });
            })
            .catch(error => {
                console.error('동기화 오류:', error);
                alert('동기화 중 오류가 발생했습니다: ' + error.message);
            })
            .finally(() => {
                // 버튼 상태 복원
                $btn.prop('disabled', false);
                $btn.html('<i class="fa fa-refresh"></i> 최신데이터 동기화');
            });
        });
    });
</script>

</body>
</html>