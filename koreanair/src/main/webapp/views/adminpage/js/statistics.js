/**
 * 대시보드 통계 관련 JavaScript
 * 
 * 이 파일의 주요 기능:
 * 1. 대시보드 기본 통계 데이터 로딩 및 표시
 * 2. 좌석 클래스별 수익 비율 데이터 로딩 (FIR, BIZ, PRE, ECONOMY)
 * 3. 통계 타일 및 차트 업데이트
 * 4. 페이지 로드 시 자동 초기화 및 데이터 로딩
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

// 좌석 클래스별 수익 비율 데이터를 가져와서 화면에 표시하는 함수
function loadSeatRevenueData() {
    console.log('좌석 수익 비율 데이터 로딩 시작...');
    console.log('예상 데이터 형식: [SeatRevenueDTO{seat_id, total_revenue, revenue_percent}]');
    
    // 백엔드에서 좌석-가격 비율 데이터 가져오기
    fetch(`${contextPath}/seatrevenue.wi`, {
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
        console.log('좌석 수익 비율 데이터 수신:', data);
        console.log('데이터 구조 확인:', JSON.stringify(data, null, 2));
        
        // 데이터가 배열인지 확인
        const actualData = data.data || data;
        if (Array.isArray(actualData)) {
            console.log('첫 번째 데이터 항목:', actualData[0]);
            console.log('필드 확인 - seat_id:', actualData[0]?.seat_id);
            console.log('필드 확인 - revenue_percent:', actualData[0]?.revenue_percent);
        }
        
        updateSeatRevenueChart(data);
        updateSeatRevenueTable(data);
    })
    .catch(error => {
        console.error('좌석 수익 비율 데이터 로딩 오류:', error);
        showSeatRevenueError();
    });
}

// 좌석 수익 비율 차트 업데이트 함수 (FIR, BIZ, PRE, ECONOMY 4가지 클래스)
function updateSeatRevenueChart(responseData) {
    console.log('좌석 수익 비율 차트 업데이트 시작');
    console.log('받은 데이터:', responseData);
    
    // 백엔드 응답 구조에 맞게 데이터 추출
    let revenueData = responseData;
    
    // 만약 data 속성이 있다면 그것을 사용
    if (responseData.data) {
        revenueData = responseData.data;
    }
    
    console.log('처리할 데이터:', revenueData);
    
    if (!revenueData || !Array.isArray(revenueData)) {
        console.error('좌석 수익 비율 데이터가 올바르지 않습니다:', revenueData);
        showSeatRevenueError();
        return;
    }
    
    console.log('데이터 개수:', revenueData.length);
    revenueData.forEach((item, index) => {
        console.log(`데이터 ${index + 1}:`, item);
    });
    
    const canvas = document.getElementById('seatRevenueChart');
    if (!canvas) {
        console.error('차트 캔버스를 찾을 수 없습니다');
        return;
    }
    
    console.log('캔버스 찾음:', canvas);
    console.log('Chart.js 사용 가능:', typeof Chart !== 'undefined');
    
    // 캔버스 크기 조정
    adjustCanvasSize(canvas);
    
    // Chart.js 라이브러리 확인
    if (typeof Chart === 'undefined') {
        console.warn('Chart.js 라이브러리가 로드되지 않았습니다. 대체 차트를 사용합니다.');
        createFallbackChart(revenueData);
        return;
    }
    
    const ctx = canvas.getContext('2d');
    
    // 차트 데이터 준비 (4가지 좌석 클래스용)
    const chartLabels = [];
    const chartValues = [];
    const chartColors = [];
    
    // 데이터 매핑 (FIR, BIZ, PRE, ECONOMY 순서로 정렬)
    const sortedData = revenueData.sort((a, b) => {
        const order = { 'FIR': 0, 'BIZ': 1, 'PRE': 2, 'ECONOMY': 3 };
        return (order[a.seat_id] || 999) - (order[b.seat_id] || 999);
    });
    
    console.log('정렬된 데이터:', sortedData);
    
    sortedData.forEach((item, index) => {
        const seatName = getSeatClassName(item.seat_id);
        const percentage = parseFloat(item.revenue_percent) || 0;
        const color = getChartColor(item.seat_id);
        
        console.log(`차트 데이터 추가 - ${item.seat_id}: ${seatName}, ${percentage}%, 색상: ${color}`);
        
        chartLabels.push(seatName);
        chartValues.push(percentage);
        chartColors.push(color);
    });
    
    // 기존 차트가 있으면 제거
    if (window.seatRevenueChartInstance) {
        window.seatRevenueChartInstance.destroy();
        console.log('기존 차트 제거됨');
    }
    
    try {
        // Chart.js 2.x 버전에 맞는 설정
        window.seatRevenueChartInstance = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: chartLabels,
                datasets: [{
                    data: chartValues,
                    backgroundColor: chartColors,
                    borderColor: '#fff',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        boxWidth: 12,
                        fontSize: 11,
                        padding: 8
                    }
                },
                tooltips: {
                    enabled: true,
                    backgroundColor: 'rgba(0,0,0,0.8)',
                    titleFontSize: 12,
                    bodyFontSize: 11,
                    titleFontColor: '#fff',
                    bodyFontColor: '#fff',
                    borderColor: '#fff',
                    borderWidth: 1,
                    cornerRadius: 4,
                    callbacks: {
                        label: function(tooltipItem, data) {
                            const label = data.labels[tooltipItem.index] || '';
                            const value = data.datasets[0].data[tooltipItem.index] || 0;
                            return `${label}: ${value.toFixed(1)}%`;
                        }
                    }
                },
                animation: {
                    animateRotate: true,
                    duration: 1500
                },
                cutoutPercentage: 50,
                elements: {
                    arc: {
                        borderWidth: 0
                    }
                }
            }
        });
        
        console.log('Chart.js 차트 생성 완료');
        console.log('차트 인스턴스:', window.seatRevenueChartInstance);
        
    } catch (error) {
        console.error('Chart.js 차트 생성 오류:', error);
        console.log('대체 차트로 전환합니다...');
        createFallbackChart(revenueData);
        return;
    }
    
    console.log('좌석 수익 비율 차트 업데이트 완료');
}

// 좌석 수익 비율 테이블 업데이트 함수
function updateSeatRevenueTable(responseData) {
    console.log('좌석 수익 비율 테이블 업데이트 시작');
    
    let revenueData = responseData;
    
    // 만약 data 속성이 있다면 그것을 사용
    if (responseData.data) {
        revenueData = responseData.data;
    }
    
    if (!revenueData || !Array.isArray(revenueData)) {
        console.error('좌석 수익 비율 테이블 데이터가 올바르지 않습니다:', revenueData);
        return;
    }
    
    // 먼저 모든 비율 셀을 초기화하고 행을 숨김
    const allRows = document.querySelectorAll('#seatRevenueTable tr[data-seat-class]');
    allRows.forEach(row => {
        const cell = row.querySelector('.revenue-percentage');
        if (cell) {
            cell.textContent = '0.0%';
            cell.style.color = '#999';
        }
        row.style.display = 'none'; // 일단 모든 행을 숨김
    });
    
    // 각 좌석 클래스별 비율 업데이트
    revenueData.forEach(item => {
        const seatClass = item.seat_id;
        const percentage = parseFloat(item.revenue_percent) || 0;
        
        console.log(`테이블 업데이트 - ${seatClass}: ${percentage}%`);
        
        // 데이터 속성으로 해당 행 찾기
        const seatKey = getSeatClassKey(seatClass);
        const row = document.querySelector(`[data-seat-class="${seatKey}"]`);
        if (row) {
            const percentageCell = row.querySelector('.revenue-percentage');
            if (percentageCell) {
                percentageCell.textContent = `${percentage.toFixed(1)}%`;
                percentageCell.style.color = '#333'; // 정상 색상으로 변경
                row.style.display = 'table-row'; // 해당 행을 보이게 함
                console.log(`테이블 셀 업데이트 성공: ${seatClass} -> ${seatKey} -> ${percentage.toFixed(1)}%`);
            } else {
                console.warn(`퍼센트 셀을 찾을 수 없습니다: ${seatClass}`);
            }
        } else {
            console.warn(`테이블 행을 찾을 수 없습니다: ${seatClass} (key: ${seatKey})`);
            console.warn('사용 가능한 data-seat-class 속성들:');
            document.querySelectorAll('[data-seat-class]').forEach(el => {
                console.warn('- ' + el.getAttribute('data-seat-class'));
            });
            // 동적으로 테이블 행 생성 (필요한 경우)
            createMissingTableRow(seatClass, percentage);
        }
    });
    
    console.log('좌석 수익 비율 테이블 업데이트 완료');
}

// 누락된 테이블 행을 동적으로 생성하는 함수
function createMissingTableRow(seatClass, percentage) {
    console.log(`누락된 테이블 행 생성: ${seatClass}`);
    
    const table = document.querySelector('#seatRevenueTable');
    if (!table) {
        console.warn('좌석 수익 테이블을 찾을 수 없어 행을 추가할 수 없습니다');
        return;
    }
    
    const row = document.createElement('tr');
    row.setAttribute('data-seat-class', getSeatClassKey(seatClass));
    
    // HTML 구조에 맞게 아이콘과 함께 생성
    const seatName = getSeatClassName(seatClass);
    const colorClass = getColorClass(seatClass);
    
    row.innerHTML = `
        <td>
            <p><i class="fa fa-square ${colorClass}"></i>${seatName}</p>
        </td>
        <td class="revenue-percentage">${percentage.toFixed(1)}%</td>
    `;
    
    table.appendChild(row);
    console.log(`테이블 행 생성 완료: ${seatClass}`);
}

// 좌석 클래스별 색상 클래스 반환 (HTML 아이콘용)
function getColorClass(seatClass) {
    const colorMapping = {
        'FIR': 'blue',
        'BIZ': 'green', 
        'PRE': 'purple',
        'ECONOMY': 'aero',
        'first': 'blue',
        'business': 'green',
        'premium': 'purple',
        'economy': 'aero'
    };
    
    return colorMapping[seatClass] || 'blue';
}

// 좌석 클래스별 차트 색상 반환 (Chart.js용 hex 색상)
function getChartColor(seatClass) {
    const colorMapping = {
        'FIR': '#3498db',     // 파랑
        'BIZ': '#2ecc71',     // 초록 
        'PRE': '#9b59b6',     // 보라
        'ECONOMY': '#1abc9c', // 청록 (aero)
        'first': '#3498db',
        'business': '#2ecc71',
        'premium': '#9b59b6',
        'economy': '#1abc9c'
    };
    
    return colorMapping[seatClass] || '#3498db';
}

// 좌석 클래스 이름 매핑 함수 (DB의 seat_id -> 화면 표시 이름)
function getSeatClassName(seatClass) {
    const classMapping = {
        'FIR': '퍼스트클래스',
        'BIZ': '비즈니스클래스', 
        'PRE': '프리미엄이코노미',
        'ECONOMY': '이코노미클래스',
        'fir': '퍼스트클래스',
        'biz': '비즈니스클래스',
        'pre': '프리미엄이코노미',
        'economy': '이코노미클래스',
        // 추가 매핑
        'first': '퍼스트클래스',
        'business': '비즈니스클래스',
        'premium': '프리미엄이코노미'
    };
    
    return classMapping[seatClass] || seatClass;
}

// 좌석 클래스 키 매핑 함수 (data-seat-class 속성용)
function getSeatClassKey(seatClass) {
    const keyMapping = {
        'FIR': 'first',
        'BIZ': 'business', 
        'PRE': 'premium',
        'ECONOMY': 'economy',
        '일등석': 'first',
        '비즈니스': 'business',
        '프레스티지': 'premium',
        '이코노미': 'economy',
        // 추가 매핑 (혹시 다른 형태로 올 경우 대비)
        'fir': 'first',
        'biz': 'business',
        'pre': 'premium',
        'economy': 'economy',
        'first': 'first',
        'business': 'business',
        'premium': 'premium'
    };
    
    return keyMapping[seatClass] || seatClass.toLowerCase();
}

// 좌석 수익 비율 데이터 로딩 오류 시 처리
function showSeatRevenueError() {
    console.log('좌석 수익 비율 데이터 오류 - 기본값 표시');
    
    // 모든 퍼센트 셀에 오류 표시
    const percentageCells = document.querySelectorAll('.revenue-percentage');
    percentageCells.forEach(cell => {
        cell.textContent = '---';
        cell.style.color = '#ccc';
    });
    
    // 차트 캔버스에 오류 메시지 표시
    const canvas = document.getElementById('seatRevenueChart');
    if (canvas) {
        drawErrorMessage(canvas, '데이터 로딩 실패');
    }
}

// 캔버스에 오류 메시지 그리기
function drawErrorMessage(canvas, message) {
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 배경 원 그리기
    ctx.beginPath();
    ctx.arc(canvas.width / 2, canvas.height / 2, 50, 0, 2 * Math.PI);
    ctx.fillStyle = '#f0f0f0';
    ctx.fill();
    ctx.strokeStyle = '#ddd';
    ctx.lineWidth = 2;
    ctx.stroke();
    
    // 텍스트 스타일 설정
    ctx.fillStyle = '#999';
    ctx.font = '12px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    
    // 메시지 그리기
    ctx.fillText(message, canvas.width / 2, canvas.height / 2);
}

// Chart.js 없이 간단한 도넛 차트 그리기 (대체 함수)
function drawSimpleDonutChart(canvas, data) {
    const ctx = canvas.getContext('2d');
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const outerRadius = Math.min(centerX, centerY) - 10;
    const innerRadius = outerRadius * 0.5;
    
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    let currentAngle = -Math.PI / 2; // 12시 방향부터 시작
    const total = data.reduce((sum, item) => sum + item.value, 0);
    
    data.forEach((item, index) => {
        const sliceAngle = (item.value / total) * 2 * Math.PI;
        
        // 도넛 조각 그리기
        ctx.beginPath();
        ctx.arc(centerX, centerY, outerRadius, currentAngle, currentAngle + sliceAngle);
        ctx.arc(centerX, centerY, innerRadius, currentAngle + sliceAngle, currentAngle, true);
        ctx.closePath();
        ctx.fillStyle = item.color;
        ctx.fill();
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = 2;
        ctx.stroke();
        
        currentAngle += sliceAngle;
    });
    
    // 중앙에 총 개수 표시
    ctx.fillStyle = '#333';
    ctx.font = 'bold 14px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText('수익 비율', centerX, centerY);
}

// Chart.js 대체 차트 생성 함수
function createFallbackChart(revenueData) {
    console.log('Chart.js 대체 차트 생성 시작');
    
    const canvas = document.getElementById('seatRevenueChart');
    if (!canvas) {
        console.error('캔버스를 찾을 수 없습니다');
        return;
    }
    
    // 데이터 준비
    const chartData = [];
    revenueData.forEach(item => {
        const seatName = getSeatClassName(item.seat_id);
        const percentage = parseFloat(item.revenue_percent) || 0;
        const color = getChartColor(item.seat_id);
        
        chartData.push({
            name: seatName,
            value: percentage,
            color: color
        });
    });
    
    // 대체 차트 그리기
    drawSimpleDonutChart(canvas, chartData);
    
    console.log('대체 차트 생성 완료');
}

// 페이지 로드 시 통계 데이터 로드 (초기화 작업)
document.addEventListener('DOMContentLoaded', function() {
    console.log('=== 통계 JavaScript 초기화 시작 ===');
    console.log('Chart.js 로드 상태:', typeof Chart !== 'undefined' ? '로드됨' : '로드되지 않음');
    console.log('contextPath 설정:', typeof contextPath !== 'undefined' ? contextPath : '설정되지 않음');
    
    // Canvas 요소 존재 확인
    const canvas = document.getElementById('seatRevenueChart');
    console.log('차트 캔버스 요소:', canvas ? '찾음' : '찾을 수 없음');
    
    console.log('1. 대시보드 기본 통계 데이터 로딩 시작');
    console.log('2. 좌석 클래스별 수익 비율 데이터 로딩 시작 (FIR, BIZ, PRE, ECONOMY)');
    
    // 짧은 지연 후 실행 (Chart.js 완전 로드 대기)
    setTimeout(() => {
        // 1. 기본 대시보드 통계 데이터 로드 (총 사용자, 남성/여성 수 등)
        loadDashboardStats();
        
        // 2. 좌석 클래스별 수익 비율 데이터 로드 (FIR: 일등석, BIZ: 비즈니스, PRE: 프레스티지, ECONOMY: 이코노미)
        loadSeatRevenueData();
    }, 100);
    
    // 3. 실시간 업데이트 시작 (주석 처리됨 - 필요시 활성화)
    // startRealTimeStats(5);
    
    console.log('=== 통계 JavaScript 초기화 완료 ===');
});

// 수동 새로고침 함수 (새로고침 버튼 등에서 호출 가능)
function refreshStats() {
    console.log('=== 수동 통계 새로고침 시작 ===');
    console.log('1. 대시보드 통계 새로고침 중...');
    loadDashboardStats();
    
    console.log('2. 좌석 수익 비율 새로고침 중... (FIR, BIZ, PRE, ECONOMY)');
    loadSeatRevenueData();
    
    console.log('=== 수동 통계 새로고침 완료 ===');
}

// 캔버스 크기 조정 함수
function adjustCanvasSize(canvas) {
    // HTML에 설정된 크기가 너무 작으면 조정
    const minSize = 200;
    const currentWidth = canvas.width;
    const currentHeight = canvas.height;
    
    console.log(`현재 캔버스 크기: ${currentWidth}x${currentHeight}`);
    
    if (currentWidth < minSize || currentHeight < minSize) {
        canvas.width = Math.max(currentWidth, minSize);
        canvas.height = Math.max(currentHeight, minSize);
        console.log(`캔버스 크기 조정됨: ${canvas.width}x${canvas.height}`);
    }
    
    // CSS 스타일도 적용
    canvas.style.width = canvas.width + 'px';
    canvas.style.height = canvas.height + 'px';
}