/**
 * 대시보드 통계 관련 JavaScript
 */

// 통계 데이터를 가져와서 화면에 표시하는 함수
function loadDashboardStats() {
    console.log('통계 데이터 로딩 시작...');
    
    // 백엔드에서 통계 데이터 가져오기
    fetch(`${contextPath}/dashboardstats.wi`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('통계 데이터 수신:', data);
        updateStatsTiles(data);
    })
    .catch(error => {
        console.error('통계 데이터 로딩 오류:', error);
        // 오류 발생 시 기본값 또는 오류 메시지 표시
        showStatsError();
    });
}

// 통계 타일들을 업데이트하는 함수
function updateStatsTiles(responseData) {
    console.log('통계 타일 업데이트 시작');
    
    // 백엔드 응답 구조: {status: "success", data: [DashBoardStatsDTO]}
    const statsData = responseData.data && responseData.data.length > 0 ? responseData.data[0] : null;
    
    if (!statsData) {
        console.error('통계 데이터가 없습니다');
        showStatsError();
        return;
    }
    
    // Total Users 업데이트 (totalCount 사용)
    updateStatTile('total-users', {
        count: statsData.totalCount,
        growthRate: 0, // 성장률 데이터가 없으므로 0으로 설정
        isPositive: true
    });
    
    // Average Time 업데이트 (기본값 사용)
    updateStatTile('average-time', {
        count: 0, // 현재 데이터에 없음
        growthRate: 0,
        isPositive: true
    });
    
    // Total Males 업데이트 (maleCount 사용)
    updateStatTile('total-males', {
        count: statsData.maleCount,
        growthRate: 0,
        isPositive: true
    });
    
    // Total Females 업데이트 (fmaleCount 사용 - 오타 주의)
    updateStatTile('total-females', {
        count: statsData.fmaleCount,
        growthRate: 0,
        isPositive: true
    });
    
    // Total Collections 업데이트 (기본값 사용)
    updateStatTile('total-collections', {
        count: statsData.reservations, // 현재 데이터에 없음
        growthRate: 0,
        isPositive: true
    });
    
    // Total Connections 업데이트 (기본값 사용)
    updateStatTile('total-connections', {
        count: 0, // 현재 데이터에 없음
        growthRate: 0,
        isPositive: true
    });
    
    console.log('통계 타일 업데이트 완료');
}

// 개별 통계 타일을 업데이트하는 함수
function updateStatTile(tileId, data) {
    // 데이터 속성으로 타일을 찾기
    const tile = document.querySelector(`[data-stat="${tileId}"]`);
    
    if (!tile) {
        console.warn(`통계 타일을 찾을 수 없습니다: ${tileId}`);
        return;
    }
    
    // 카운트 업데이트
    const countElement = tile.querySelector('.count');
    if (countElement) {
        // 숫자에 콤마 추가
        countElement.textContent = formatNumber(data.count);
    }
    
    // 증감률 업데이트
    const growthElement = tile.querySelector('.count_bottom i');
    if (growthElement) {
        // 증감률에 따라 색상 변경
        growthElement.className = data.isPositive ? 'green' : 'red';
        
        // 화살표 아이콘 업데이트
        const arrowIcon = data.isPositive ? 'fa-sort-asc' : 'fa-sort-desc';
        growthElement.innerHTML = `<i class="fa ${arrowIcon}"></i>${Math.abs(data.growthRate)}% `;
    }
}

// 숫자 포맷팅 함수 (콤마 추가)
function formatNumber(num) {
    if (num === undefined || num === null) return '0';
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// 통계 데이터 로딩 오류 시 처리
function showStatsError() {
    console.log('통계 데이터 오류 - 기본값 표시');
    
    // 모든 통계 타일에 오류 표시 또는 기본값 설정
    const tiles = document.querySelectorAll('[data-stat]');
    tiles.forEach(tile => {
        const countElement = tile.querySelector('.count');
        if (countElement) {
            countElement.textContent = '---';
            countElement.style.color = '#ccc';
        }
        
        const growthElement = tile.querySelector('.count_bottom');
        if (growthElement) {
            growthElement.innerHTML = '<i class="red">데이터 로딩 실패</i>';
        }
    });
}

// 애니메이션 효과로 숫자 카운트업
function animateCounter(element, targetValue, duration = 2000) {
    const startValue = 0;
    const increment = targetValue / (duration / 16); // 60fps 기준
    let currentValue = startValue;
    
    const timer = setInterval(() => {
        currentValue += increment;
        if (currentValue >= targetValue) {
            currentValue = targetValue;
            clearInterval(timer);
        }
        element.textContent = formatNumber(Math.floor(currentValue));
    }, 16);
}

// 실시간 통계 업데이트 (선택사항)
function startRealTimeStats(intervalMinutes = 5) {
    console.log(`실시간 통계 업데이트 시작 (${intervalMinutes}분마다)`);
    
    // 즉시 한 번 로드
    loadDashboardStats();
    
    // 주기적으로 업데이트
    setInterval(() => {
        console.log('실시간 통계 업데이트 실행');
        loadDashboardStats();
    }, intervalMinutes * 60 * 1000);
}

// 페이지 로드 시 통계 데이터 로드
document.addEventListener('DOMContentLoaded', function() {
    console.log('통계 JavaScript 초기화');
    
    // 통계 데이터 로드
    loadDashboardStats();
    
    // 실시간 업데이트 시작 (5분마다)
    // startRealTimeStats(5);
});

// 수동 새로고침 함수 (버튼 등에서 호출 가능)
function refreshStats() {
    console.log('수동 통계 새로고침');
    loadDashboardStats();
} 