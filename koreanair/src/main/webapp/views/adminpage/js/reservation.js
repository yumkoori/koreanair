/**
 * 예약 관리 JavaScript
 * 예약 검색, 조회, 삭제 기능을 담당
 * 
 * API 엔드포인트:
 * - GET /reservations.wi : 예약 목록 조회 및 검색
 *   파라미터: searchType, searchKeyword, status, page, size
 * - DELETE /reservations.wi : 예약 삭제
 *   바디: { reservationId: "예약ID" }
 */

// 전역 변수
let currentPage = 1;
let currentPageSize = 10;
let currentReservations = [];
let selectedReservationId = null;

/**
 * 예약 관리 초기화 함수
 */
function initReservationManagement() {
    console.log('예약 관리 모듈 초기화 시작');
    
    // 이벤트 리스너 등록
    setupEventListeners();
    
    // 페이지 로드 시 전체 예약 목록 조회
    loadAllReservations();
    
    console.log('예약 관리 모듈 초기화 완료');
}

/**
 * 이벤트 리스너 설정
 */
function setupEventListeners() {
    // 검색 버튼 이벤트
    const searchBtn = document.getElementById('searchReservationBtn');
    if (searchBtn) {
        searchBtn.addEventListener('click', searchReservations);
    }
    
    // 전체 조회 버튼 이벤트
    const loadAllBtn = document.getElementById('loadAllReservationsBtn');
    if (loadAllBtn) {
        loadAllBtn.addEventListener('click', loadAllReservations);
    }
    
    // 초기화 버튼 이벤트
    const clearBtn = document.getElementById('clearSearchBtn');
    if (clearBtn) {
        clearBtn.addEventListener('click', clearSearch);
    }
    
    // 검색창 엔터키 이벤트
    const searchInput = document.getElementById('reservationSearchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchReservations();
            }
        });
    }
    
    // 모달 내 삭제 버튼 이벤트
    const deleteBtn = document.getElementById('deleteReservationBtn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', showDeleteConfirm);
    }
    
    // 확인 삭제 버튼 이벤트
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', confirmDeleteReservation);
    }
    
    console.log('이벤트 리스너 설정 완료');
}

/**
 * 전체 예약 목록 조회
 */
function loadAllReservations() {
    console.log('전체 예약 목록 조회 시작');
    
    showLoadingSpinner(true);
    
    // 서버에서 전체 예약 목록 조회
    const params = new URLSearchParams({
        page: currentPage,
        size: currentPageSize
    });
    
    // 빈 파라미터는 추가하지 않음
    
    const requestUrl = `${contextPath}/reservations.wi?${params}`;
    console.log('요청 URL:', requestUrl);
    
    fetch(requestUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('전체 예약 목록 조회 성공:', data);
        handleReservationSearchResponse(data);
    })
    .catch(error => {
        console.error('전체 예약 목록 조회 실패:', error);
        showNotification('예약 목록을 불러오는 중 오류가 발생했습니다.', 'error');
        
        // 오류 시 임시 데이터로 폴백
        console.log('임시 데이터로 폴백');
        const mockData = generateMockReservationData();
        const normalizedMockData = mockData.map(item => normalizeReservationData(item));
        currentReservations = normalizedMockData;
        displayReservations(normalizedMockData);
        updateResultCount(normalizedMockData.length);
    })
    .finally(() => {
        showLoadingSpinner(false);
    });
}

/**
 * 예약 검색
 */
function searchReservations() {
    const searchType = document.getElementById('searchType').value;
    const searchInput = document.getElementById('reservationSearchInput').value.trim();
    const statusFilter = document.getElementById('reservationStatus').value;
    
    console.log('예약 검색:', { searchType, searchInput, statusFilter });
    
    if (!searchInput && !statusFilter) {
        showNotification('검색어를 입력하거나 상태를 선택해주세요.', 'warning');
        return;
    }
    
    showLoadingSpinner(true);
    
    // 서버에 검색 요청
    const params = new URLSearchParams({
        page: currentPage,
        size: currentPageSize
    });
    
    // 비어있지 않은 파라미터만 추가
    if (searchType && searchInput) {
        params.append('searchType', searchType);
        params.append('searchKeyword', searchInput);
    }
    if (statusFilter) {
        params.append('status', statusFilter);
    }
    
    const requestUrl = `${contextPath}/reservations.wi?${params}`;
    console.log('검색 요청 URL:', requestUrl);
    console.log('검색 파라미터:', { searchType, searchInput, statusFilter });
    
    fetch(requestUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('예약 검색 성공:', data);
        handleReservationSearchResponse(data);
        
        if (data.reservations && data.reservations.length === 0) {
            showNotification('검색 결과가 없습니다.', 'info');
        }
    })
    .catch(error => {
        console.error('예약 검색 실패:', error);
        showNotification('예약 검색 중 오류가 발생했습니다.', 'error');
        
        // 오류 시 임시 데이터로 폴백
        console.log('임시 데이터로 폴백');
        const mockData = generateMockReservationData();
        let filteredData = mockData;
        
        // 검색어 필터링 (서버 응답 원본 데이터에 대해 먼저 필터링)
        if (searchInput) {
            filteredData = filteredData.filter(reservation => {
                // 원본 데이터 구조로 검색
                switch(searchType) {
                    case 'reservationId':
                        return (reservation.booking_id || reservation.reservationId || '').toLowerCase().includes(searchInput.toLowerCase());
                    case 'userName':
                        return (reservation.user_name || reservation.userName || '').toLowerCase().includes(searchInput.toLowerCase());
                    case 'userEmail':
                        return (reservation.email || reservation.userEmail || '').toLowerCase().includes(searchInput.toLowerCase());
                    case 'phone':
                        return (reservation.phone || '').includes(searchInput);
                    default:
                        return false;
                }
            });
        }
        
        // 상태 필터링
        if (statusFilter) {
            filteredData = filteredData.filter(reservation => 
                (reservation.status || reservation.reservation_status) === statusFilter
            );
        }
        
        // 폴백 데이터도 정규화
        const normalizedData = filteredData.map(item => normalizeReservationData(item));
        
        currentReservations = normalizedData;
        displayReservations(normalizedData);
        updateResultCount(normalizedData.length);
        
        if (filteredData.length === 0) {
            showNotification('검색 결과가 없습니다.', 'info');
        }
    })
    .finally(() => {
        showLoadingSpinner(false);
    });
}

/**
 * 검색 초기화
 */
function clearSearch() {
    console.log('검색 초기화');
    
    document.getElementById('searchType').value = 'reservationId';
    document.getElementById('reservationSearchInput').value = '';
    document.getElementById('reservationStatus').value = '';
    
    loadAllReservations();
    
    showNotification('검색 조건이 초기화되었습니다.', 'success');
}

/**
 * 예약 목록 화면에 표시
 */
function displayReservations(reservations) {
    const tableBody = document.getElementById('reservationTableBody');
    
    if (!tableBody) {
        console.error('테이블 바디를 찾을 수 없습니다.');
        return;
    }
    
    tableBody.innerHTML = '';
    
    if (reservations.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="13" class="text-center">
                    <i class="fa fa-search"></i> 예약 데이터가 없습니다.
                </td>
            </tr>
        `;
        return;
    }
    
    reservations.forEach((reservation, index) => {
        const row = createReservationRow(reservation, index);
        tableBody.appendChild(row);
    });
    
    console.log(`${reservations.length}개의 예약이 표시되었습니다.`);
}

/**
 * 예약 테이블 행 생성
 */
function createReservationRow(reservation, index) {
    const row = document.createElement('tr');
    
    const statusBadgeClass = getStatusBadgeClass(reservation.status);
    const formattedAmount = formatCurrency(reservation.totalAmount);
    
    row.innerHTML = `
        <td>${reservation.reservationId}</td>
        <td>${reservation.userName}</td>
        <td>${reservation.userEmail}</td>
        <td>${reservation.phone}</td>
        <td>${reservation.departure}</td>
        <td>${reservation.arrival}</td>
        <td>${formatDate(reservation.departureDate)}</td>
        <td>${formatDate(reservation.reservationDate)}</td>
        <td>
            <span class="label ${statusBadgeClass}">${getStatusText(reservation.status)}</span>
        </td>
        <td>${reservation.seatClass}</td>
        <td>${reservation.passengerCount}명</td>
        <td>${formattedAmount}</td>
        <td>
            <button class="btn btn-xs btn-info" onclick="showReservationDetail('${reservation.reservationId}')" title="상세보기">
                <i class="fa fa-eye"></i>
            </button>
            <button class="btn btn-xs btn-danger" onclick="showDeleteConfirmFromList('${reservation.reservationId}')" title="삭제">
                <i class="fa fa-trash"></i>
            </button>
        </td>
    `;
    
    return row;
}

/**
 * 예약 상세 정보 표시
 */
function showReservationDetail(reservationId) {
    console.log('예약 상세 정보 표시:', reservationId);
    
    const reservation = currentReservations.find(r => r.reservationId === reservationId);
    
    if (!reservation) {
        showNotification('예약 정보를 찾을 수 없습니다.', 'error');
        return;
    }
    
    selectedReservationId = reservationId;
    
    // 모달에 데이터 설정
    populateReservationModal(reservation);
    
    // 모달 표시
    $('#reservationDetailModal').modal('show');
}

/**
 * 예약 상세 모달에 데이터 설정
 */
function populateReservationModal(reservation) {
    // 예약 정보
    document.getElementById('modalReservationId').textContent = reservation.reservationId;
    document.getElementById('modalReservationDate').textContent = formatDateTime(reservation.reservationDate);
    
    const statusElement = document.getElementById('modalReservationStatus');
    statusElement.textContent = getStatusText(reservation.status);
    statusElement.className = `label ${getStatusBadgeClass(reservation.status)}`;
    
    document.getElementById('modalTotalAmount').textContent = formatCurrency(reservation.totalAmount);
    
    // 사용자 정보
    document.getElementById('modalUserName').textContent = reservation.userName;
    document.getElementById('modalUserEmail').textContent = reservation.userEmail;
    document.getElementById('modalUserPhone').textContent = reservation.phone;
    document.getElementById('modalUserBirth').textContent = formatDate(reservation.userBirth);
    
    // 항공편 정보
    document.getElementById('modalFlightNumber').textContent = reservation.flightNumber;
    document.getElementById('modalDeparture').textContent = reservation.departure;
    document.getElementById('modalArrival').textContent = reservation.arrival;
    document.getElementById('modalDepartureTime').textContent = formatDateTime(reservation.departureDate);
    document.getElementById('modalArrivalTime').textContent = formatDateTime(reservation.arrivalDate);
    document.getElementById('modalSeatClass').textContent = reservation.seatClass;
    document.getElementById('modalPassengerCount').textContent = reservation.passengerCount + '명';
}

/**
 * 삭제 확인 모달 표시 (목록에서)
 */
function showDeleteConfirmFromList(reservationId) {
    selectedReservationId = reservationId;
    showDeleteConfirm();
}

/**
 * 삭제 확인 모달 표시
 */
function showDeleteConfirm() {
    if (!selectedReservationId) {
        showNotification('삭제할 예약을 선택해주세요.', 'warning');
        return;
    }
    
    const reservation = currentReservations.find(r => r.reservationId === selectedReservationId);
    
    if (!reservation) {
        showNotification('예약 정보를 찾을 수 없습니다.', 'error');
        return;
    }
    
    // 삭제 확인 모달에 정보 설정
    document.getElementById('deleteReservationId').textContent = reservation.reservationId;
    document.getElementById('deleteReservationUser').textContent = reservation.userName;
    
    // 상세 모달 숨기기 (열려있다면)
    $('#reservationDetailModal').modal('hide');
    
    // 삭제 확인 모달 표시
    $('#deleteConfirmModal').modal('show');
}

/**
 * 예약 삭제 확인
 */
function confirmDeleteReservation() {
    if (!selectedReservationId) {
        showNotification('삭제할 예약이 선택되지 않았습니다.', 'error');
        return;
    }
    
    console.log('예약 삭제 확인:', selectedReservationId);
    
    showLoadingSpinner(true);
    
    // 서버에 삭제 요청
    const requestUrl = `${contextPath}/reservations.wi`;
    console.log('삭제 요청 URL:', requestUrl);
    console.log('삭제할 예약 ID:', selectedReservationId);
    
    fetch(requestUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            reservationId: selectedReservationId
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('예약 삭제 성공:', data);
        
        if (data.status === 'success') {
            // 로컬 데이터에서도 제거
            currentReservations = currentReservations.filter(r => r.reservationId !== selectedReservationId);
            displayReservations(currentReservations);
            updateResultCount(currentReservations.length);
            
            $('#deleteConfirmModal').modal('hide');
            showNotification('예약이 성공적으로 삭제되었습니다.', 'success');
            
            selectedReservationId = null;
        } else {
            showNotification(data.message || '예약 삭제에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('예약 삭제 실패:', error);
        showNotification('예약 삭제 중 오류가 발생했습니다.', 'error');
        
        // 오류 시에도 임시로 로컬에서 제거 (개발/테스트용)
        console.log('오류 발생, 임시로 로컬에서 제거');
        currentReservations = currentReservations.filter(r => 
            (r.reservationId || r.booking_id) !== selectedReservationId
        );
        displayReservations(currentReservations);
        updateResultCount(currentReservations.length);
        
        $('#deleteConfirmModal').modal('hide');
        showNotification('예약이 임시로 삭제되었습니다. (서버 오류 발생)', 'warning');
        
        selectedReservationId = null;
    })
    .finally(() => {
        showLoadingSpinner(false);
    });
}

/**
 * 서버 응답 처리 (공통)
 */
function handleReservationSearchResponse(data) {
    console.log('서버 응답 처리:', data);
    
    let reservationList = [];
    
    // 데이터 구조 확인 - 여러 가능한 응답 형태 지원
    if (Array.isArray(data)) {
        reservationList = data;
        console.log('직접 배열 형태의 데이터');
    } else if (data.reservations && Array.isArray(data.reservations)) {
        reservationList = data.reservations;
        console.log('data.reservations 형태의 데이터');
    } else if (data.data && Array.isArray(data.data)) {
        reservationList = data.data;
        console.log('data.data 형태의 데이터');
    } else if (data.list && Array.isArray(data.list)) {
        reservationList = data.list;
        console.log('data.list 형태의 데이터');
    }
    
    console.log('최종 reservationList:', reservationList);
    console.log('reservationList 길이:', reservationList.length);
    
    // 서버 응답 데이터를 내부 포맷으로 변환
    const normalizedReservations = reservationList.map(item => normalizeReservationData(item));
    
    // 전역 변수에 저장
    currentReservations = normalizedReservations;
    
    // 화면에 표시
    displayReservations(normalizedReservations);
    
    // 결과 개수 업데이트 (서버에서 제공된 총 개수 또는 배열 길이 사용)
    const totalCount = data.totalCount || data.total || reservationList.length;
    updateResultCount(totalCount);
}

/**
 * 서버 응답 데이터를 내부 포맷으로 변환
 */
function normalizeReservationData(serverData) {
    return {
        reservationId: serverData.booking_id || serverData.reservationId || '-',
        userName: serverData.user_name || serverData.userName || '-',
        userEmail: serverData.email || serverData.userEmail || '-',
        phone: serverData.phone || '-',
        userBirth: serverData.birth_date || serverData.user_birth || serverData.userBirth || '-',
        departure: serverData.start || serverData.departure || '-',
        arrival: serverData.end || serverData.arrival || '-',
        departureDate: serverData.startDate || serverData.departureDate || '-',
        arrivalDate: serverData.endDate || serverData.arrivalDate || '-',
        reservationDate: serverData.bookingDate || serverData.reservationDate || '-',
        status: serverData.status || serverData.reservation_status || 'confirmed',
        seatClass: formatSeatClass(serverData.seat_class || serverData.seatClass),
        passengerCount: serverData.passenger || serverData.passengerCount || 1,
        totalAmount: serverData.totalPrice || serverData.totalAmount || 0,
        flightNumber: serverData.flightNO || serverData.flight_number || serverData.flightNumber || '-'
    };
}

/**
 * 좌석 클래스 포맷팅
 */
function formatSeatClass(seatClass) {
    if (!seatClass) return '-';
    
    switch(seatClass.toUpperCase()) {
        case 'ECO':
        case 'ECONOMY':
            return '이코노미';
        case 'PRE':
        case 'PREMIUM':
        case 'PREMIUM_ECONOMY':
            return '프레스티지';
        case 'BUS':
        case 'BUSINESS':
            return '비즈니스';
        case 'FIRST':
        case 'FST':
            return '퍼스트';
        default:
            return seatClass;
    }
}

/**
 * 결과 수 업데이트
 */
function updateResultCount(count) {
    const countElement = document.getElementById('resultCount');
    if (countElement) {
        countElement.textContent = count;
    }
}

/**
 * 로딩 스피너 표시/숨김
 */
function showLoadingSpinner(show) {
    if (show) {
        NProgress.start();
    } else {
        NProgress.done();
    }
}

/**
 * 알림 메시지 표시
 */
function showNotification(message, type = 'info') {
    // PNotify 또는 다른 알림 라이브러리 사용
    console.log(`[${type.toUpperCase()}] ${message}`);
    
    // 간단한 alert 대신 더 나은 알림 시스템 구현 필요
    if (type === 'error') {
        alert('오류: ' + message);
    } else if (type === 'warning') {
        alert('경고: ' + message);
    } else {
        // info, success 등
        alert(message);
    }
}

/**
 * 상태 뱃지 클래스 반환
 */
function getStatusBadgeClass(status) {
    switch(status) {
        case 'confirmed': return 'label-success';
        case 'pending': return 'label-warning';
        case 'cancelled': return 'label-danger';
        case 'completed': return 'label-info';
        default: return 'label-default';
    }
}

/**
 * 상태 텍스트 반환
 */
function getStatusText(status) {
    switch(status) {
        case 'confirmed': return '확정';
        case 'pending': return '대기';
        case 'cancelled': return '취소';
        case 'completed': return '완료';
        default: return '알 수 없음';
    }
}

/**
 * 날짜 포맷팅
 */
function formatDate(dateString) {
    if (!dateString) return '-';
    
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
}

/**
 * 날짜/시간 포맷팅
 */
function formatDateTime(dateString) {
    if (!dateString) return '-';
    
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR');
}

/**
 * 통화 포맷팅
 */
function formatCurrency(amount) {
    if (amount == null) return '-';
    
    return new Intl.NumberFormat('ko-KR', {
        style: 'currency',
        currency: 'KRW'
    }).format(amount);
}




// 전역 함수로 노출 (HTML에서 onclick으로 호출 가능)
window.showReservationDetail = showReservationDetail;
window.showDeleteConfirmFromList = showDeleteConfirmFromList; 