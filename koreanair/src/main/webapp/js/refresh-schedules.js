/**
 * 실시간 스케줄 새로고침 기능
 * RefreshSchedulesHandler와 연동하여 최신 항공편 스케줄 정보를 가져옵니다.
 */

// 전역 변수
let isRefreshing = false;
let refreshInterval = null;

/**
 * 수동으로 스케줄을 새로고침하는 함수
 * @param {string} date - 조회할 날짜 (YYYY-MM-DD 형식)
 * @param {string} flightType - 항공편 타입 (all, domestic, international)
 */
function refreshSchedules(date = null, flightType = 'all') {
    if (isRefreshing) {
        console.log('이미 새로고침 중입니다...');
        return;
    }
    
    isRefreshing = true;
    updateRefreshButton(true);
    
    // 현재 날짜를 기본값으로 설정
    if (!date) {
        date = new Date().toISOString().split('T')[0];
    }
    
    console.log(`스케줄 새로고침 시작 - 날짜: ${date}, 타입: ${flightType}`);
    
    // AJAX 요청
    $.ajax({
        url: '/refreshschedules.wi',
        type: 'GET',
        data: {
            date: date,
            flightType: flightType
        },
        dataType: 'json',
        timeout: 30000, // 30초 타임아웃
        success: function(response) {
            console.log('새로고침 성공:', response);
            
            if (response.status === 'success') {
                // 성공 메시지 표시
                showNotification('success', response.message + ` (${response.count}개 항목)`);
                
                // 데이터 업데이트
                updateScheduleDisplay(response.data);
                
                // 마지막 새로고침 시간 업데이트
                updateLastRefreshTime(response.refreshTime);
                
            } else {
                showNotification('error', response.message || '새로고침 중 오류가 발생했습니다.');
            }
        },
        error: function(xhr, status, error) {
            console.error('새로고침 실패:', error);
            let errorMessage = '서버와의 통신 중 오류가 발생했습니다.';
            
            if (status === 'timeout') {
                errorMessage = '요청 시간이 초과되었습니다. 다시 시도해주세요.';
            } else if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            
            showNotification('error', errorMessage);
        },
        complete: function() {
            isRefreshing = false;
            updateRefreshButton(false);
        }
    });
}

/**
 * 자동 새로고침을 시작하는 함수
 * @param {number} intervalMinutes - 새로고침 간격 (분 단위)
 */
function startAutoRefresh(intervalMinutes = 5) {
    if (refreshInterval) {
        clearInterval(refreshInterval);
    }
    
    refreshInterval = setInterval(function() {
        const currentDate = $('#dateInput').val() || new Date().toISOString().split('T')[0];
        const currentFlightType = $('#flightTypeSelect').val() || 'all';
        
        console.log(`자동 새로고침 실행 - ${intervalMinutes}분 간격`);
        refreshSchedules(currentDate, currentFlightType);
    }, intervalMinutes * 60 * 1000);
    
    console.log(`자동 새로고침이 ${intervalMinutes}분 간격으로 시작되었습니다.`);
    showNotification('info', `자동 새로고침이 ${intervalMinutes}분 간격으로 설정되었습니다.`);
}

/**
 * 자동 새로고침을 중지하는 함수
 */
function stopAutoRefresh() {
    if (refreshInterval) {
        clearInterval(refreshInterval);
        refreshInterval = null;
        console.log('자동 새로고침이 중지되었습니다.');
        showNotification('info', '자동 새로고침이 중지되었습니다.');
    }
}

/**
 * 새로고침 버튼 상태를 업데이트하는 함수
 * @param {boolean} isLoading - 로딩 상태 여부
 */
function updateRefreshButton(isLoading) {
    const refreshBtn = $('#refreshBtn');
    const refreshIcon = $('#refreshIcon');
    
    if (isLoading) {
        refreshBtn.prop('disabled', true);
        refreshIcon.addClass('fa-spin');
        refreshBtn.find('.btn-text').text('새로고침 중...');
    } else {
        refreshBtn.prop('disabled', false);
        refreshIcon.removeClass('fa-spin');
        refreshBtn.find('.btn-text').text('새로고침');
    }
}

/**
 * 스케줄 표시를 업데이트하는 함수
 * @param {Array} scheduleData - 새로운 스케줄 데이터
 */
function updateScheduleDisplay(scheduleData) {
    // 기존 테이블이나 리스트를 업데이트
    // 이 부분은 실제 UI 구조에 맞게 수정해야 합니다
    
    if (typeof updateFlightTable === 'function') {
        updateFlightTable(scheduleData);
    } else if (typeof refreshFlightList === 'function') {
        refreshFlightList(scheduleData);
    } else {
        console.log('스케줄 데이터 업데이트:', scheduleData);
        // 기본적인 테이블 업데이트 로직
        updateDefaultScheduleTable(scheduleData);
    }
}

/**
 * 기본 스케줄 테이블 업데이트 함수
 * @param {Array} scheduleData - 스케줄 데이터
 */
function updateDefaultScheduleTable(scheduleData) {
    const tableBody = $('#scheduleTableBody');
    if (tableBody.length === 0) return;
    
    tableBody.empty();
    
    scheduleData.forEach(function(flight) {
        const row = `
            <tr>
                <td>${flight.flightNumber || ''}</td>
                <td>${flight.departure || ''}</td>
                <td>${flight.arrival || ''}</td>
                <td>${flight.departureTime || ''}</td>
                <td>${flight.arrivalTime || ''}</td>
                <td>${flight.status || ''}</td>
            </tr>
        `;
        tableBody.append(row);
    });
}

/**
 * 마지막 새로고침 시간을 업데이트하는 함수
 * @param {string} refreshTime - 새로고침 시간
 */
function updateLastRefreshTime(refreshTime) {
    const lastRefreshElement = $('#lastRefreshTime');
    if (lastRefreshElement.length > 0) {
        const now = new Date().toLocaleTimeString();
        lastRefreshElement.text(`마지막 업데이트: ${now}`);
    }
}

/**
 * 알림 메시지를 표시하는 함수
 * @param {string} type - 알림 타입 (success, error, info, warning)
 * @param {string} message - 알림 메시지
 */
function showNotification(type, message) {
    // 기존 알림이 있다면 제거
    $('.notification').remove();
    
    const notificationClass = {
        'success': 'alert-success',
        'error': 'alert-danger',
        'info': 'alert-info',
        'warning': 'alert-warning'
    };
    
    const notification = `
        <div class="alert ${notificationClass[type]} notification" style="position: fixed; top: 20px; right: 20px; z-index: 9999; min-width: 300px;">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            ${message}
        </div>
    `;
    
    $('body').append(notification);
    
    // 5초 후 자동 제거
    setTimeout(function() {
        $('.notification').fadeOut(500, function() {
            $(this).remove();
        });
    }, 5000);
}

// 페이지 로드 시 이벤트 바인딩
$(document).ready(function() {
    // 새로고침 버튼 클릭 이벤트
    $(document).on('click', '#refreshBtn', function() {
        const date = $('#dateInput').val();
        const flightType = $('#flightTypeSelect').val();
        refreshSchedules(date, flightType);
    });
    
    // 자동 새로고침 토글 이벤트
    $(document).on('change', '#autoRefreshToggle', function() {
        if ($(this).is(':checked')) {
            const interval = $('#refreshInterval').val() || 5;
            startAutoRefresh(parseInt(interval));
        } else {
            stopAutoRefresh();
        }
    });
    
    // 새로고침 간격 변경 이벤트
    $(document).on('change', '#refreshInterval', function() {
        if ($('#autoRefreshToggle').is(':checked')) {
            const interval = $(this).val() || 5;
            startAutoRefresh(parseInt(interval));
        }
    });
    
    // 키보드 단축키 (Ctrl + R 또는 F5)
    $(document).on('keydown', function(e) {
        if ((e.ctrlKey && e.key === 'r') || e.key === 'F5') {
            e.preventDefault();
            const date = $('#dateInput').val();
            const flightType = $('#flightTypeSelect').val();
            refreshSchedules(date, flightType);
        }
    });
    
    console.log('실시간 스케줄 새로고침 기능이 초기화되었습니다.');
}); 