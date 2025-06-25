/**
 * 대시보드 통계 관련 JavaScript
 * VERSION: 2024-12-19-v2 (브라우저 캐시 갱신용)
 * 
 * 이 파일의 주요 기능:
 * 1. 대시보드 기본 통계 데이터 로딩 및 표시
 * 2. 좌석 클래스별 수익 비율 데이터 로딩 (FIR, BIZ, PRE, ECONOMY)
 * 3. 통계 타일 및 차트 업데이트
 * 4. 페이지 로드 시 자동 초기화 및 데이터 로딩
 */



// API 호출 중복 방지 플래그
if (typeof window.loadingDashboardStats === 'undefined') {
    window.loadingDashboardStats = false;
}

// 통계 데이터를 가져와서 화면에 표시하는 함수
function loadDashboardStats() {
    // 강력한 중복 호출 방지 - 타임스탬프 기반 (500ms로 단축)
    const now = Date.now();
    if (window.lastDashboardStatsCall && (now - window.lastDashboardStatsCall) < 500) {
        return;
    }
    
    // 이미 로딩 중이면 중복 호출 방지
    if (window.loadingDashboardStats) {
        return;
    }
    
    window.lastDashboardStatsCall = now;
    window.loadingDashboardStats = true;
    
    // 캐시 무력화를 위한 타임스탬프 추가
    const cacheBreaker = Date.now();
    
    // 백엔드에서 통계 데이터 가져오기
    fetch(contextPath + '/dashboardstats.wi?t=' + cacheBreaker, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Cache-Control': 'no-cache, no-store, must-revalidate',
            'Pragma': 'no-cache',
            'Expires': '0'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        updateStatsTiles(data);
    })
    .catch(error => {
        console.error('통계 데이터 로딩 오류:', error);
        showStatsError();
    })
    .finally(() => {
        // 로딩 완료 후 플래그 해제
        window.loadingDashboardStats = false;
    });
}

// 통계 타일들을 업데이트하는 함수
function updateStatsTiles(responseData) {
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
        growthElement.innerHTML = '<i class="fa ' + arrowIcon + '"></i>' + Math.abs(data.growthRate) + '% ';
    }
}

// 숫자 포맷팅 함수 (콤마 추가)
function formatNumber(num) {
    if (num === undefined || num === null) return '0';
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// 통계 데이터 로딩 오류 시 처리
function showStatsError() {
    // 모든 통계 타일에 오류 표시 또는 기본값 설정
    const tiles = document.querySelectorAll('[data-stat]');
    tiles.forEach(function(tile) {
        var countElement = tile.querySelector('.count');
        if (countElement) {
            countElement.textContent = '---';
            countElement.style.color = '#ccc';
        }
        
        var growthElement = tile.querySelector('.count_bottom');
        if (growthElement) {
            growthElement.innerHTML = '<i class="red">데이터 로딩 실패</i>';
        }
    });
}

// 애니메이션 효과로 숫자 카운트업
function animateCounter(element, targetValue, duration) {
    duration = duration || 2000;
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
function startRealTimeStats(intervalMinutes) {
    intervalMinutes = intervalMinutes || 5;
    console.log('실시간 통계 업데이트 시작 (' + intervalMinutes + '분마다)');
    
    // 즉시 한 번 로드
    loadDashboardStats();
    loadSeatRevenueData();
    
    // 주기적으로 업데이트
    setInterval(function() {
        console.log('실시간 통계 업데이트 실행');
        loadDashboardStats();
        loadSeatRevenueData();
    }, intervalMinutes * 60 * 1000);
}

// 좌석 수익 데이터 로딩 중복 방지 플래그
if (typeof window.loadingSeatRevenueData === 'undefined') {
    window.loadingSeatRevenueData = false;
}

// 좌석 클래스별 수익 비율 데이터를 가져와서 화면에 표시하는 함수
function loadSeatRevenueData() {
    // 이미 로딩 중이면 중복 호출 방지
    if (window.loadingSeatRevenueData) {
        return;
    }
    
    window.loadingSeatRevenueData = true;
    
    // 백엔드에서 좌석-가격 비율 데이터 가져오기
    fetch(contextPath + '/seatrevenue.wi', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}, statusText: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        updateSeatRevenueChart(data);
        updateSeatRevenueTable(data);
    })
    .catch(error => {
        console.error('좌석 수익 비율 데이터 로딩 오류:', error);
        showSeatRevenueError();
    })
    .finally(() => {
        // 로딩 완료 후 플래그 해제
        window.loadingSeatRevenueData = false;
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
    revenueData.forEach(function(item, index) {
        console.log('데이터 ' + (index + 1) + ':', item);
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
        console.warn('Chart.js 로딩 상태 재확인 중...');
        
        // 잠시 후 다시 시도
        setTimeout(function() {
            if (typeof Chart !== 'undefined') {
                console.log('Chart.js가 지연 로딩되었습니다. 차트를 다시 생성합니다.');
                updateSeatRevenueChart(responseData);
            } else {
                console.warn('Chart.js를 찾을 수 없어 대체 차트를 사용합니다.');
                createFallbackChart(revenueData);
            }
        }, 1000);
        return;
    }
    
    const ctx = canvas.getContext('2d');
    
    // 차트 데이터 준비 (4가지 좌석 클래스용)
    const chartLabels = [];
    const chartValues = [];
    const chartColors = [];
    
    // 데이터 매핑 (FIR, BIZ, PRE, ECONOMY 순서로 정렬)
    var sortedData = revenueData.sort(function(a, b) {
        var order = { 'FIR': 0, 'BIZ': 1, 'PRE': 2, 'ECONOMY': 3 };
        return (order[a.seat_id] || 999) - (order[b.seat_id] || 999);
    });
    
    console.log('정렬된 데이터:', sortedData);
    
    sortedData.forEach(function(item, index) {
        var seatName = getSeatClassName(item.seat_id);
        var percentage = parseFloat(item.revenue_percent) || 0;
        var color = getChartColor(item.seat_id);
        
        console.log('차트 데이터 추가 - ' + item.seat_id + ': ' + seatName + ', ' + percentage + '%, 색상: ' + color);
        
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
                            return label + ': ' + value.toFixed(1) + '%';
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
    allRows.forEach(function(row) {
        var cell = row.querySelector('.revenue-percentage');
        if (cell) {
            cell.textContent = '0.0%';
            cell.style.color = '#999';
        }
        row.style.display = 'none'; // 일단 모든 행을 숨김
    });
    
    // 각 좌석 클래스별 비율 업데이트
    revenueData.forEach(function(item) {
        var seatClass = item.seat_id;
        var percentage = parseFloat(item.revenue_percent) || 0;
        
        console.log('테이블 업데이트 - ' + seatClass + ': ' + percentage + '%');
        
        // 데이터 속성으로 해당 행 찾기
        var seatKey = getSeatClassKey(seatClass);
        var row = document.querySelector('[data-seat-class="' + seatKey + '"]');
        if (row) {
            var percentageCell = row.querySelector('.revenue-percentage');
            if (percentageCell) {
                percentageCell.textContent = percentage.toFixed(1) + '%';
                percentageCell.style.color = '#333'; // 정상 색상으로 변경
                row.style.display = 'table-row'; // 해당 행을 보이게 함
                console.log('테이블 셀 업데이트 성공: ' + seatClass + ' -> ' + seatKey + ' -> ' + percentage.toFixed(1) + '%');
            } else {
                console.warn('퍼센트 셀을 찾을 수 없습니다: ' + seatClass);
            }
        } else {
            console.warn('테이블 행을 찾을 수 없습니다: ' + seatClass + ' (key: ' + seatKey + ')');
            console.warn('사용 가능한 data-seat-class 속성들:');
            document.querySelectorAll('[data-seat-class]').forEach(function(el) {
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
    console.log('누락된 테이블 행 생성: ' + seatClass);
    
    var table = document.querySelector('#seatRevenueTable');
    if (!table) {
        console.warn('좌석 수익 테이블을 찾을 수 없어 행을 추가할 수 없습니다');
        return;
    }
    
    var row = document.createElement('tr');
    row.setAttribute('data-seat-class', getSeatClassKey(seatClass));
    
    // HTML 구조에 맞게 아이콘과 함께 생성
    var seatName = getSeatClassName(seatClass);
    var colorClass = getColorClass(seatClass);
    
    row.innerHTML = 
        '<td>' +
            '<p><i class="fa fa-square ' + colorClass + '"></i>' + seatName + '</p>' +
        '</td>' +
        '<td class="revenue-percentage">' + percentage.toFixed(1) + '%</td>';
    
    table.appendChild(row);
    console.log('테이블 행 생성 완료: ' + seatClass);
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
    percentageCells.forEach(function(cell) {
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
    var total = data.reduce(function(sum, item) {
        return sum + item.value;
    }, 0);
    
    data.forEach(function(item, index) {
        var sliceAngle = (item.value / total) * 2 * Math.PI;
        
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
    revenueData.forEach(function(item) {
        var seatName = getSeatClassName(item.seat_id);
        var percentage = parseFloat(item.revenue_percent) || 0;
        var color = getChartColor(item.seat_id);
        
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

// 중복 실행 방지를 위한 플래그 (전역 window 객체에 저장)
if (typeof window.statisticsInitialized === 'undefined') {
    window.statisticsInitialized = false;
}

// 페이지 로드 시 통계 데이터 로드 (초기화 작업)
function initializeStatistics() {
    // 중복 실행 방지
    if (window.statisticsInitialized) {
        return;
    }
    window.statisticsInitialized = true;
    
    // contextPath가 설정되지 않은 경우 기본값 설정
    if (typeof contextPath === 'undefined') {
        window.contextPath = '';
    }
    
    // Chart.js와 기타 라이브러리 로딩을 위한 지연 시간
    setTimeout(function() {
        // 년도 선택기 초기화
        initializeYearSelector();
        initializeRouteStatsYearSelector();
        
        // 데이터 로드 (순차적으로 실행)
        setTimeout(function() {
            loadRouteStatsData();
        }, 100);
        
        setTimeout(function() {
            loadDashboardStats();
        }, 200);
        
        setTimeout(function() {
            loadSeatRevenueData();
        }, 300);
        
        setTimeout(function() {
            loadMonthlyReservationData();
        }, 400);
        
        setTimeout(function() {
            loadPopularRoutesData();
        }, 500);
        
    }, 300);
}

// DOM 로드 완료 시 초기화
document.addEventListener('DOMContentLoaded', initializeStatistics);

// 혹시 DOMContentLoaded 이벤트를 놓친 경우를 대비해 window.load에서도 체크
window.addEventListener('load', function() {
    if (!window.statisticsInitialized) {
        initializeStatistics();
    }
});

// 수동 새로고침 함수 (새로고침 버튼 등에서 호출 가능)
function refreshStats() {
    // 강제 새로고침을 위해 중복 방지 플래그 리셋
    window.loadingDashboardStats = false;
    window.loadingSeatRevenueData = false;
    window.loadingMonthlyReservationData = false;
    window.loadingRouteStatsData = false;
    window.loadingPopularRoutesData = false;
    window.lastDashboardStatsCall = 0;
    
    loadDashboardStats();
    loadSeatRevenueData();
    loadMonthlyReservationData();
    loadRouteStatsData();
    loadPopularRoutesData();
}

// ===== 월별 예약자 수 그래프 관련 함수들 =====

// 월별 예약자 수 데이터 로딩 중복 방지 플래그
if (typeof window.loadingMonthlyReservationData === 'undefined') {
    window.loadingMonthlyReservationData = false;
}

/**
 * 월별 예약자 수 데이터를 백엔드에서 가져와서 Network Activities 그래프에 표시하는 함수
 * 기존 Network Activities 그래프를 "달마다 예약자수" 데이터로 변경
 * Flot 차트 라이브러리를 사용하여 기존 그래프 형태 유지
 */
function loadMonthlyReservationData(year) {
    // 중복 호출 방지
    if (window.loadingMonthlyReservationData) {
        return;
    }
    
    // 년도가 지정되지 않은 경우 현재 년도 사용
    if (!year) {
        year = new Date().getFullYear();
    }
    
    window.loadingMonthlyReservationData = true;
    
    // 백엔드에서 월별 예약자 수 데이터 가져오기 (년도 파라미터 포함)
    fetch(contextPath + '/monthlyreservations.wi?year=' + year, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}, statusText: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        // Network Activities 그래프 업데이트
        updateNetworkActivitiesChart(data);
        
        // 그래프 제목도 함께 업데이트
        updateNetworkActivitiesTitle();
    })
    .catch(error => {
        console.error('월별 예약자 수 데이터 로딩 오류:', error);
        showMonthlyReservationError();
    })
    .finally(() => {
        // 로딩 완료 후 플래그 해제
        window.loadingMonthlyReservationData = false;
    });
}

/**
 * Network Activities 그래프를 월별 예약자 수 데이터로 업데이트하는 함수
 * Flot 차트 라이브러리를 사용하여 기존 그래프 스타일 유지
 * @param {Object} responseData - 백엔드에서 받은 월별 예약자 수 데이터
 */
function updateNetworkActivitiesChart(responseData) {
    console.log('=== Network Activities 차트 업데이트 시작 (월별 예약자 수) ===');
    
    // 백엔드 응답 구조에 맞게 데이터 추출
    let monthlyData = responseData;
    
    // 만약 data 속성이 있다면 그것을 사용
    if (responseData.data) {
        monthlyData = responseData.data;
    }
    
    console.log('처리할 월별 데이터:', monthlyData);
    
    if (!monthlyData || !Array.isArray(monthlyData)) {
        console.error('월별 예약자 수 데이터가 올바르지 않습니다:', monthlyData);
        showMonthlyReservationError();
        return;
    }
    
    // 차트 컨테이너 확인
    const chartContainer = document.getElementById('chart_plot_01');
    if (!chartContainer) {
        console.error('Network Activities 차트 컨테이너를 찾을 수 없습니다 (#chart_plot_01)');
        return;
    }
    
    console.log('차트 컨테이너 찾음:', chartContainer);
    
    // Flot 라이브러리 확인
    if (typeof jQuery === 'undefined' || typeof jQuery.plot === 'undefined') {
        console.error('Flot 차트 라이브러리가 로드되지 않았습니다');
        showMonthlyReservationError();
        return;
    }
    
    console.log('Flot 라이브러리 사용 가능');
    
    try {
        // 월별 데이터를 Flot 형식으로 변환
        const flotData = convertToFlotFormat(monthlyData);
        console.log('Flot 형식으로 변환된 데이터:', flotData);
        
        // Flot 차트 설정 (기존 Network Activities 스타일 유지)
        const plotOptions = getFlotPlotOptions();
        
        // 차트 그리기
        jQuery.plot(chartContainer, flotData, plotOptions);
        
        console.log('월별 예약자 수 차트 렌더링 완료');
        
        // Top Campaign Performance 업데이트 (예약이 가장 높은 4개월)
        updateTopCampaignPerformance(monthlyData);
        
    } catch (error) {
        console.error('월별 예약자 수 차트 생성 오류:', error);
        showMonthlyReservationError();
    }
}

/**
 * 월별 예약자 수 데이터를 Flot 차트 형식으로 변환하는 함수
 * @param {Array} monthlyData - 월별 데이터 배열 [{reservationdate: "06", reservation_count: 1}, ...]
 * @returns {Array} Flot 형식의 데이터 배열
 */
function convertToFlotFormat(monthlyData) {
    console.log('Flot 형식 변환 시작');
    console.log('원본 데이터:', monthlyData);
    
    // 현재 년도 가져오기
    const currentYear = new Date().getFullYear();
    
    // 백엔드 데이터를 월별로 매핑 (reservationdate 필드 사용)
    const dataMap = {};
    if (monthlyData && Array.isArray(monthlyData)) {
        monthlyData.forEach(item => {
            // reservationdate는 "06", "07" 형식으로 오므로 2자리로 패딩
            let month = item.reservationdate;
            if (month && month.length === 1) {
                month = '0' + month;
            }
            if (month && month.length === 2) {
                dataMap[month] = parseInt(item.reservation_count) || 0;
                console.log(`데이터 매핑: ${month}월 -> ${dataMap[month]}명`);
            }
        });
    }
    
    // 1월부터 12월까지 모든 달 생성 (데이터가 없는 달은 0으로 설정)
    const flotPoints = [];
    const allMonthsData = [];
    
    for (let month = 1; month <= 12; month++) {
        const monthStr = month.toString().padStart(2, '0');
        const count = dataMap[monthStr] || 0; // 데이터가 없으면 0
        
        // 날짜 객체 생성 (년도-월-01 형식)
        const date = new Date(currentYear, month - 1, 1);
        const timestamp = date.getTime();
        
        flotPoints.push([timestamp, count]);
        allMonthsData.push({ month: monthStr, count: count });
        
        console.log(`${month}월(${monthStr}): ${count}명 (timestamp: ${timestamp})`);
    }
    
    // 최대값 계산하여 y축 범위 결정을 위해 저장
    const maxCount = Math.max(...flotPoints.map(point => point[1]));
    window.monthlyReservationMaxValue = maxCount;
    console.log('월별 예약자 수 최대값:', maxCount);
    
    // Flot에 전달할 데이터셋 구성
    const flotDatasets = [
        {
            label: "월별 예약자 수",
            data: flotPoints,
            color: "#26B99A", // 기존 Network Activities와 유사한 색상
            lines: {
                show: true,
                fill: 0.1,
                lineWidth: 2
            },
            points: {
                show: true,
                radius: 4,
                fillColor: "#26B99A"
            },
            shadowSize: 0
        }
    ];
    
    console.log('Flot 형식 변환 완료 (1월-12월 전체):', flotDatasets);
    return flotDatasets;
}

/**
 * Flot 차트 옵션 설정 (기존 Network Activities 스타일 유지)
 * @returns {Object} Flot 차트 옵션 객체
 */
function getFlotPlotOptions() {
    // y축 최대값을 100 단위로 조정
    let yAxisMax = 100;
    if (window.monthlyReservationMaxValue) {
        // 최대값을 100 단위로 올림
        yAxisMax = Math.ceil(window.monthlyReservationMaxValue / 100) * 100;
        // 최소 100은 유지
        if (yAxisMax < 100) {
            yAxisMax = 100;
        }
    }
    
    console.log('y축 최대값 설정:', yAxisMax, '(원본 최대값:', window.monthlyReservationMaxValue, ')');
    
    return {
        grid: {
            borderWidth: 1,
            borderColor: "#f3f3f3",
            tickColor: "#f3f3f3",
            hoverable: true,
            clickable: true
        },
        series: {
            lines: {
                show: true,
                fill: true,
                lineWidth: 2,
                steps: false,
                fillColor: {
                    colors: [
                        { opacity: 0.0 },
                        { opacity: 0.4 }
                    ]
                }
            },
            points: {
                show: true,
                radius: 4,
                symbol: "circle",
                fillColor: "#ffffff",
                borderColor: "#26B99A"
            }
        },
        colors: ["#26B99A"],
        legend: {
            show: true,
            position: "nw"
        },
        tooltip: {
            show: true,
            content: function(label, xval, yval) {
                // 타임스탬프를 다시 월 형식으로 변환
                const date = new Date(xval);
                const year = date.getFullYear();
                const month = (date.getMonth() + 1).toString().padStart(2, '0');
                return `${year}-${month}월: ${formatNumber(yval)}명`;
            }
        },
        xaxis: {
            mode: "time",
            timeformat: "%m월",
            tickLength: 0,
            tickColor: "#f3f3f3",
            min: new Date(new Date().getFullYear(), 0, 1).getTime(), // 1월 1일
            max: new Date(new Date().getFullYear(), 11, 31).getTime(), // 12월 31일
            ticks: [
                [new Date(new Date().getFullYear(), 0, 1).getTime(), "1월"],
                [new Date(new Date().getFullYear(), 1, 1).getTime(), "2월"],
                [new Date(new Date().getFullYear(), 2, 1).getTime(), "3월"],
                [new Date(new Date().getFullYear(), 3, 1).getTime(), "4월"],
                [new Date(new Date().getFullYear(), 4, 1).getTime(), "5월"],
                [new Date(new Date().getFullYear(), 5, 1).getTime(), "6월"],
                [new Date(new Date().getFullYear(), 6, 1).getTime(), "7월"],
                [new Date(new Date().getFullYear(), 7, 1).getTime(), "8월"],
                [new Date(new Date().getFullYear(), 8, 1).getTime(), "9월"],
                [new Date(new Date().getFullYear(), 9, 1).getTime(), "10월"],
                [new Date(new Date().getFullYear(), 10, 1).getTime(), "11월"],
                [new Date(new Date().getFullYear(), 11, 1).getTime(), "12월"]
            ]
        },
        yaxis: {
            min: 0,
            max: yAxisMax,
            tickColor: "#f3f3f3",
            tickFormatter: function(val) {
                return formatNumber(val) + "명";
            },
            tickSize: 100 // y축 눈금을 100 단위로 설정
        }
    };
}

/**
 * Network Activities 그래프 제목을 "월별 예약자 수"로 업데이트하는 함수
 */
function updateNetworkActivitiesTitle() {
    console.log('Network Activities 제목 업데이트');
    
    // 제목 요소 찾기 및 업데이트
    const titleElement = document.querySelector('.dashboard_graph .x_title h3');
    if (titleElement) {
        titleElement.innerHTML = '월별 예약자 수 <small>Monthly Reservation Statistics</small>';
        console.log('그래프 제목 업데이트 완료');
    } else {
        console.warn('그래프 제목 요소를 찾을 수 없습니다');
    }
}

/**
 * 월별 예약자 수 데이터 로딩 오류 시 처리하는 함수
 */
function showMonthlyReservationError() {
    console.log('월별 예약자 수 데이터 오류 - 기본 메시지 표시');
    
    const chartContainer = document.getElementById('chart_plot_01');
    if (chartContainer) {
        // 오류 메시지 표시
        chartContainer.innerHTML = '<div style="text-align: center; padding: 50px; color: #999;">' +
                                   '<i class="fa fa-exclamation-triangle" style="font-size: 24px; margin-bottom: 10px;"></i><br>' +
                                   '월별 예약자 수 데이터를 불러올 수 없습니다' +
                                   '</div>';
    }
    
    // 제목도 오류 표시
    const titleElement = document.querySelector('.dashboard_graph .x_title h3');
    if (titleElement) {
        titleElement.innerHTML = '월별 예약자 수 <small style="color: #e74c3c;">데이터 로딩 실패</small>';
    }
}

// 캔버스 크기 조정 함수
function adjustCanvasSize(canvas) {
    // 차트를 더 작고 적절한 크기로 설정
    const targetSize = 155; // 크기 조정: 120 → 140 (원하는 크기로 변경 가능)
    
    console.log('기존 캔버스 크기: ' + canvas.width + 'x' + canvas.height);
    
    // 캔버스 크기 조정
    canvas.width = targetSize;
    canvas.height = targetSize;
    
    console.log('조정된 캔버스 크기: ' + canvas.width + 'x' + canvas.height);
    
    // CSS 스타일도 적용하여 비율 유지
    canvas.style.width = targetSize + 'px';
    canvas.style.height = targetSize + 'px';
    canvas.style.display = 'block';
    canvas.style.margin = '10px auto'; // 가운데 정렬
}

/**
 * 년도 선택기를 초기화하는 함수
 */
function initializeYearSelector() {
    console.log('=== 년도 선택기 초기화 ===');
    
    const yearSelect = document.getElementById('yearSelect');
    if (!yearSelect) {
        console.warn('년도 선택기 요소를 찾을 수 없습니다.');
        return;
    }
    
    // 현재 년도 가져오기
    const currentYear = new Date().getFullYear();
    
    // 년도 옵션 생성 (현재 년도 기준 전후 5년)
    const startYear = currentYear - 5;
    const endYear = currentYear + 2;
    
    // 기존 옵션 제거
    yearSelect.innerHTML = '';
    
    // 년도 옵션 추가
    for (let year = endYear; year >= startYear; year--) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year + '년';
        
        // 현재 년도를 기본 선택
        if (year === currentYear) {
            option.selected = true;
        }
        
        yearSelect.appendChild(option);
    }
    
    // 년도 변경 이벤트 리스너 추가
    yearSelect.addEventListener('change', function() {
        const selectedYear = parseInt(this.value);
        console.log('년도 변경:', selectedYear);
        
        // 선택된 년도로 월별 예약자 수 데이터 다시 로딩
        loadMonthlyReservationData(selectedYear);
    });
    
    console.log(`년도 선택기 초기화 완료. 범위: ${startYear}~${endYear}년`);
}

/**
 * Top Campaign Performance 영역을 예약이 가장 높은 4개월로 업데이트하는 함수
 * @param {Array} monthlyData - 백엔드에서 받은 월별 예약자 수 데이터
 */
function updateTopCampaignPerformance(monthlyData) {
    console.log('=== Top Campaign Performance 업데이트 시작 ===');
    console.log('원본 월별 데이터:', monthlyData);
    
    if (!monthlyData || !Array.isArray(monthlyData)) {
        console.error('월별 데이터가 올바르지 않습니다:', monthlyData);
        return;
    }
    
    // 월별 데이터를 정리하고 정렬
    const monthsData = [];
    monthlyData.forEach(item => {
        const monthNum = parseInt(item.reservationdate);
        const count = parseInt(item.reservation_count) || 0;
        
        if (monthNum >= 1 && monthNum <= 12) {
            monthsData.push({
                month: monthNum,
                monthName: monthNum + '월',
                count: count
            });
        }
    });
    
    // 예약 수 기준으로 내림차순 정렬
    monthsData.sort((a, b) => b.count - a.count);
    
    // 상위 4개월만 선택
    const top4Months = monthsData.slice(0, 4);
    console.log('상위 4개월 데이터:', top4Months);
    
    // 월별 예약자 수 그래프의 y축 최대값을 기준으로 퍼센트 계산
    // window.monthlyReservationMaxValue는 실제 데이터 최대값
    // y축 최대값은 100 단위로 올림된 값 사용
    let chartYAxisMax = 100; // 기본값
    if (window.monthlyReservationMaxValue) {
        // 최대값을 100 단위로 올림 (getFlotPlotOptions와 동일한 로직)
        chartYAxisMax = Math.ceil(window.monthlyReservationMaxValue / 100) * 100;
        if (chartYAxisMax < 100) {
            chartYAxisMax = 100;
        }
    }
    
    console.log('차트 y축 최대값 (퍼센트 계산 기준):', chartYAxisMax);
    console.log('실제 데이터 최대값:', window.monthlyReservationMaxValue);
    
    // Top Campaign Performance 컨테이너 찾기 (새로운 ID 사용)
    const campaignContainer = document.getElementById('topReservationContainer');
    if (!campaignContainer) {
        console.error('Top Campaign Performance 컨테이너를 찾을 수 없습니다 (#topReservationContainer)');
        return;
    }
    
    // 기존 내용 모두 제거하고 새로 구성
    campaignContainer.innerHTML = '';
    
    // 상위 4개월 각각에 대해 HTML 생성
    top4Months.forEach((monthData, index) => {
        // 월별 예약자 수 그래프의 y축 최대값을 기준으로 퍼센트 계산
        const percentage = chartYAxisMax > 0 ? Math.round((monthData.count / chartYAxisMax) * 100) : 0;
        
        // 퍼센트가 100을 넘지 않도록 제한
        const finalPercentage = Math.min(percentage, 100);
        
        const monthDiv = document.createElement('div');
        monthDiv.innerHTML = `
            <p>${monthData.monthName} - ${formatNumber(monthData.count)}개의 예약</p>
            <div class="">
                <div class="progress progress_sm" style="width: 76%;">
                    <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="${finalPercentage}" style="width: ${finalPercentage}%;"></div>
                </div>
            </div>
        `;
        
        campaignContainer.appendChild(monthDiv);
        console.log(`${monthData.monthName}: ${monthData.count}개 → ${finalPercentage}% (차트 기준: ${chartYAxisMax})`);
    });
    
    // Bootstrap progressbar 애니메이션 초기화 및 실제 width 적용
    if (typeof jQuery !== 'undefined') {
        // 약간의 지연 후 애니메이션 효과 적용
        setTimeout(function() {
            jQuery('.progress .progress-bar').each(function() {
                const goal = jQuery(this).attr('data-transitiongoal');
                if (goal) {
                    jQuery(this).animate({ width: goal + '%' }, 800);
                }
            });
        }, 100);
    }
    
    console.log('Top Campaign Performance 업데이트 완료');
}

// ===== 인기노선도 관련 함수들 =====

// 인기노선도 데이터 로딩 중복 방지 플래그
if (typeof window.loadingPopularRoutesData === 'undefined') {
    window.loadingPopularRoutesData = false;
}

/**
 * 인기노선도 데이터를 백엔드에서 가져와서 App Versions 패널에 표시하는 함수
 * @param {number} year - 조회할 년도 (선택사항, 기본값: 현재 년도)
 */
function loadPopularRoutesData(year) {
    // 중복 호출 방지
    if (window.loadingPopularRoutesData) {
        return;
    }
    
    // 년도가 지정되지 않은 경우 현재 년도 사용
    if (!year) {
        year = new Date().getFullYear();
    }
    
    window.loadingPopularRoutesData = true;
    
    // 캐시 무력화를 위한 타임스탬프 추가
    const cacheBreaker = Date.now();
    
    // 백엔드에서 인기노선도 데이터 가져오기 (년도 파라미터 포함)
    fetch(contextPath + '/popularroutes.wi?year=' + year + '&t=' + cacheBreaker, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Cache-Control': 'no-cache, no-store, must-revalidate',
            'Pragma': 'no-cache',
            'Expires': '0'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}, statusText: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        // App Versions 패널을 인기노선도로 업데이트
        updatePopularRoutesWidget(data);
        
        // 패널 제목도 함께 업데이트
        updatePopularRoutesTitle();
    })
    .catch(error => {
        console.error('인기노선도 데이터 로딩 오류:', error);
        showPopularRoutesError();
    })
    .finally(() => {
        // 로딩 완료 후 플래그 해제
        window.loadingPopularRoutesData = false;
    });
}

/**
 * App Versions 패널을 인기노선도 데이터로 업데이트하는 함수
 * @param {Object} responseData - 백엔드에서 받은 인기노선도 데이터
 */
function updatePopularRoutesWidget(responseData) {
    console.log('=== 인기노선도 위젯 업데이트 시작 ===');
    
    // 백엔드 응답 구조에 맞게 데이터 추출
    let routesData = responseData;
    
    // 만약 data 속성이 있다면 그것을 사용
    if (responseData.data) {
        routesData = responseData.data;
    }
    
    console.log('처리할 인기노선도 데이터:', routesData);
    
    if (!routesData || !Array.isArray(routesData)) {
        console.error('인기노선도 데이터가 올바르지 않습니다:', routesData);
        showPopularRoutesError();
        return;
    }
    
    // 인기노선도 패널 찾기 (ID를 통해 직접 접근)
    const popularRoutesPanel = document.getElementById('popularRoutesPanel');
    if (!popularRoutesPanel) {
        console.error('인기노선도 패널을 찾을 수 없습니다 (#popularRoutesPanel)');
        return;
    }
    
    const contentContainer = popularRoutesPanel.querySelector('.x_content');
    if (!contentContainer) {
        console.error('인기노선도 패널의 컨텐츠 영역을 찾을 수 없습니다');
        return;
    }
    
    console.log('인기노선도 패널 찾음, 데이터로 변경 중...');
    
    // 기존 내용 모두 제거
    contentContainer.innerHTML = '';
    
    // 새로운 제목 추가
    const titleElement = document.createElement('h4');
    titleElement.textContent = '인기 항공노선 통계';
    contentContainer.appendChild(titleElement);
    
    // 상위 5개 노선만 표시 (탑 5로 제한)
    const top5Routes = routesData.slice(0, 5);
    console.log('상위 5개 인기노선:', top5Routes);
    
    // 탑 5 노선의 총 예약 수 계산 (상대적 비율 계산용)
    const top5TotalReservations = top5Routes.reduce((total, route) => {
        const count = parseInt(route.count) || 0;
        return total + count;
    }, 0);
    
    // 각 노선에 대해 widget_summary 생성
    top5Routes.forEach((routeData, index) => {
        // DTO 필드명에 맞게 수정: departure_airport_id, arrival_airport_id, count, route_percent
        const routeName = (routeData.departure_airport_id && routeData.arrival_airport_id) 
                         ? `${routeData.departure_airport_id} → ${routeData.arrival_airport_id}` 
                         : `노선 ${index + 1}`;
        const reservationCount = parseInt(routeData.count) || 0;
        
        // 탑 5 내에서의 상대적 퍼센트 계산
        let routePercent = 0;
        if (top5TotalReservations > 0) {
            routePercent = (reservationCount / top5TotalReservations) * 100;
        }
        // 백엔드에서 이미 계산된 퍼센트가 있다면 그것을 사용
        if (routeData.route_percent !== undefined && routeData.route_percent > 0) {
            routePercent = parseFloat(routeData.route_percent);
        }
        
        console.log(`노선 ${index + 1}: ${routeName}, 예약수: ${reservationCount}, 비율: ${routePercent.toFixed(1)}%`);
        
        // widget_summary div 생성
        const widgetDiv = document.createElement('div');
        widgetDiv.className = 'widget_summary';
        
        widgetDiv.innerHTML = `
            <div class="w_left w_25">
                <span>${routeName}</span>
            </div>
            <div class="w_center w_55">
                <div class="progress">
                    <div class="progress-bar bg-green" role="progressbar" 
                         aria-valuenow="${routePercent}" aria-valuemin="0" aria-valuemax="100" 
                         style="width: ${Math.min(routePercent, 100)}%;" 
                         data-route-percent="${routePercent}">
                        <span class="sr-only">${routePercent}% Complete</span>
                    </div>
                </div>
            </div>
            <div class="w_right w_20">  
                <span>${formatNumber(reservationCount)}</span>
            </div>
            <div class="clearfix"></div>
        `;
        
        contentContainer.appendChild(widgetDiv);
    });
    
    // Bootstrap progressbar 애니메이션 효과 적용
    if (typeof jQuery !== 'undefined') {
        setTimeout(function() {
            jQuery('.progress .progress-bar').each(function() {
                const percent = jQuery(this).attr('data-route-percent');
                if (percent) {
                    jQuery(this).animate({ width: Math.min(percent, 100) + '%' }, 1000);
                }
            });
        }, 200);
    }
    
    console.log('인기노선도 위젯 업데이트 완료');
}

/**
 * App Versions 패널 제목을 "인기노선도"로 업데이트하는 함수
 */
function updatePopularRoutesTitle() {
    console.log('인기노선도 패널 제목 업데이트');
    
    // ID를 통해 패널 제목 요소 찾기
    const popularRoutesPanel = document.getElementById('popularRoutesPanel');
    if (popularRoutesPanel) {
        const titleElement = popularRoutesPanel.querySelector('.x_title h2');
        if (titleElement) {
            titleElement.textContent = '인기노선도';
            console.log('패널 제목을 "인기노선도"로 변경 완료');
        } else {
            console.warn('패널 제목 요소를 찾을 수 없습니다');
        }
    } else {
        console.warn('인기노선도 패널을 찾을 수 없습니다');
    }
}

/**
 * 인기노선도 데이터 로딩 오류 시 처리하는 함수
 */
function showPopularRoutesError() {
    console.log('인기노선도 데이터 오류 - 기본 메시지 표시');
    
    // ID를 통해 인기노선도 패널 찾기
    const popularRoutesPanel = document.getElementById('popularRoutesPanel');
    if (popularRoutesPanel) {
        const contentContainer = popularRoutesPanel.querySelector('.x_content');
        if (contentContainer) {
            // 오류 메시지 표시
            contentContainer.innerHTML = `
                <div style="text-align: center; padding: 50px 20px; color: #999;">
                    <i class="fa fa-exclamation-triangle" style="font-size: 24px; margin-bottom: 10px;"></i><br>
                    인기노선도 데이터를 불러올 수 없습니다<br>
                    <small>잠시 후 다시 시도해주세요</small>
                </div>
            `;
            
            // 제목도 오류 표시
            updatePopularRoutesTitle();
        }
    } else {
        console.error('인기노선도 패널을 찾을 수 없어 오류 메시지를 표시할 수 없습니다');
    }
}

// ===== 새로운 항공노선 통계 관련 함수들 =====

// 항공노선 통계 데이터 로딩 중복 방지 플래그
if (typeof window.loadingRouteStatsData === 'undefined') {
    window.loadingRouteStatsData = false;
}

/**
 * 항공노선 통계 년도 선택기를 초기화하는 함수
 */
function initializeRouteStatsYearSelector() {
    console.log('=== 항공노선 통계 년도 선택기 초기화 ===');
    
    const yearSelect = document.getElementById('routeStatsYearSelect');
    if (!yearSelect) {
        console.warn('항공노선 통계 년도 선택기 요소를 찾을 수 없습니다.');
        return;
    }
    
    // 현재 년도 가져오기
    const currentYear = new Date().getFullYear();
    
    // 년도 옵션 생성 (현재 년도 기준 전후 5년)
    const startYear = currentYear - 5;
    const endYear = currentYear + 2;
    
    // 기존 옵션 제거
    yearSelect.innerHTML = '';
    
    // 년도 옵션 추가
    for (let year = endYear; year >= startYear; year--) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year + '년';
        
        // 현재 년도를 기본 선택
        if (year === currentYear) {
            option.selected = true;
        }
        
        yearSelect.appendChild(option);
    }
    
    // 년도 변경 이벤트 리스너 추가
    yearSelect.addEventListener('change', function() {
        const selectedYear = parseInt(this.value);
        console.log('항공노선 통계 년도 변경:', selectedYear);
        
        // 선택된 년도로 항공노선 데이터 다시 로딩
        loadRouteStatsData(selectedYear);
    });
    
    console.log(`항공노선 통계 년도 선택기 초기화 완료. 범위: ${startYear}~${endYear}년`);
}

/**
 * 항공노선 통계 데이터를 백엔드에서 가져와서 패널에 표시하는 함수
 * @param {number} year - 조회할 년도 (선택사항, 기본값: 현재 년도)
 */
function loadRouteStatsData(year) {
    // 중복 호출 방지
    if (window.loadingRouteStatsData) {
        return;
    }
    
    // 년도가 지정되지 않은 경우 현재 년도 사용
    if (!year) {
        year = new Date().getFullYear();
    }
    
    window.loadingRouteStatsData = true;
    
    // 캐시 무력화를 위한 타임스탬프 추가
    const cacheBreaker = Date.now();
    const apiUrl = contextPath + '/popularroutes.wi?year=' + year + '&t=' + cacheBreaker;
    
    // 백엔드에서 항공노선 통계 데이터 가져오기 (년도 파라미터 포함)
    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Cache-Control': 'no-cache, no-store, must-revalidate',
            'Pragma': 'no-cache',
            'Expires': '0'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}, statusText: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        // 데이터 검증
        if (!data) {
            throw new Error('응답 데이터가 없습니다');
        }
        
        // 항공노선 통계 패널 업데이트
        updateRouteStatsWidget(data);
    })
    .catch(error => {
        console.error('항공노선 통계 데이터 로딩 오류:', error);
        showRouteStatsError();
    })
    .finally(() => {
        // 로딩 완료 후 플래그 해제
        window.loadingRouteStatsData = false;
    });
}

/**
 * 항공노선 통계 패널을 데이터로 업데이트하는 함수
 * @param {Object} responseData - 백엔드에서 받은 항공노선 통계 데이터
 */
function updateRouteStatsWidget(responseData) {
    // 백엔드 응답 구조에 맞게 데이터 추출
    let routesData = responseData;
    
    // 만약 data 속성이 있다면 그것을 사용
    if (responseData && responseData.data) {
        routesData = responseData.data;
    } else if (responseData && Array.isArray(responseData)) {
        routesData = responseData;
    }
    
    if (!routesData || !Array.isArray(routesData)) {
        console.error('항공노선 통계 데이터가 올바르지 않습니다:', routesData);
        showRouteStatsError();
        return;
    }
    
    // 항공노선 통계 패널 찾기
    const routeStatsPanel = document.getElementById('routeStatsPanel');
    if (!routeStatsPanel) {
        console.error('항공노선 통계 패널을 찾을 수 없습니다 (#routeStatsPanel)');
        return;
    }
    
    const contentContainer = document.getElementById('routeStatsContent');
    if (!contentContainer) {
        console.error('항공노선 통계 패널의 컨텐츠 영역을 찾을 수 없습니다 (#routeStatsContent)');
        return;
    }
    
    // 기존 내용 모두 제거
    contentContainer.innerHTML = '';
    
    // 새로운 제목 추가
    const titleElement = document.createElement('h4');
    titleElement.textContent = '연도별 인기 항공노선 순위';
    titleElement.style.marginBottom = '15px';
    contentContainer.appendChild(titleElement);
    
    // 상위 5개 노선만 표시 (탑 5로 제한)
    const top5Routes = routesData.slice(0, 5);
    
    if (top5Routes.length === 0) {
        contentContainer.innerHTML += '<div style="text-align: center; padding: 20px; color: #999;">표시할 노선 데이터가 없습니다</div>';
        return;
    }
    
    // 탑 5 노선의 총 예약 수 계산 (상대적 비율 계산용)
    const top5TotalReservations = top5Routes.reduce((total, route) => {
        const count = parseInt(route.count) || parseInt(route.reservation_count) || parseInt(route.reservationCount) || 0;
        return total + count;
    }, 0);
    
    // 각 노선에 대해 widget_summary 생성
    top5Routes.forEach((routeData, index) => {
        // DTO 필드명 확인 및 처리
        let routeName = '알 수 없는 노선';
        let reservationCount = 0;
        let routePercent = 0;
        
        // 다양한 필드명 패턴 확인
        if (routeData.departure_airport_id && routeData.arrival_airport_id) {
            routeName = `${routeData.departure_airport_id} → ${routeData.arrival_airport_id}`;
        } else if (routeData.route_name) {
            routeName = routeData.route_name;
        } else if (routeData.routeName) {
            routeName = routeData.routeName;
        } else {
            routeName = `노선 ${index + 1}`;
        }
        
        // 예약 수 확인
        if (routeData.count !== undefined) {
            reservationCount = parseInt(routeData.count) || 0;
        } else if (routeData.reservation_count !== undefined) {
            reservationCount = parseInt(routeData.reservation_count) || 0;
        } else if (routeData.reservationCount !== undefined) {
            reservationCount = parseInt(routeData.reservationCount) || 0;
        }
        
        // 탑 5 내에서의 상대적 퍼센트 계산
        if (top5TotalReservations > 0) {
            routePercent = (reservationCount / top5TotalReservations) * 100;
        } else {
            routePercent = 0;
        }
        
        // widget_summary div 생성
        const widgetDiv = document.createElement('div');
        widgetDiv.className = 'widget_summary';
        
        widgetDiv.innerHTML = `
            <div class="w_left w_25">
                <span style="font-size: 11px; display: block; word-wrap: break-word;">${routeName}</span>
            </div>
            <div class="w_center w_55">
                <div class="progress">
                    <div class="progress-bar bg-green" role="progressbar" 
                         aria-valuenow="${routePercent}" aria-valuemin="0" aria-valuemax="100" 
                         style="width: ${Math.min(routePercent, 100)}%;" 
                         data-route-percent="${routePercent}">
                        <span class="sr-only">${routePercent}% Complete</span>
                    </div>
                </div>
            </div>
            <div class="w_right w_20">  
                <span style="font-size: 11px;">${formatNumber(reservationCount)}</span>
            </div>
            <div class="clearfix"></div>
        `;
        
        contentContainer.appendChild(widgetDiv);
    });
    
    // Bootstrap progressbar 애니메이션 효과 적용
    if (typeof jQuery !== 'undefined') {
        setTimeout(function() {
            jQuery('#routeStatsContent .progress .progress-bar').each(function() {
                const percent = jQuery(this).attr('data-route-percent');
                if (percent) {
                    jQuery(this).animate({ width: Math.min(percent, 100) + '%' }, 1000);
                }
            });
        }, 200);
    }
}

/**
 * 항공노선 통계 데이터 로딩 오류 시 처리하는 함수
 */
function showRouteStatsError() {
    const contentContainer = document.getElementById('routeStatsContent');
    if (contentContainer) {
        // 오류 메시지 표시
        contentContainer.innerHTML = `
            <div style="text-align: center; padding: 30px 15px; color: #999;">
                <i class="fa fa-exclamation-triangle" style="font-size: 24px; margin-bottom: 10px; color: #e74c3c;"></i><br>
                <strong>항공노선 통계 데이터를 불러올 수 없습니다</strong><br>
                <small style="color: #666;">네트워크 연결 또는 서버 오류가 발생했습니다</small><br>
                <button onclick="loadRouteStatsData()" class="btn btn-sm btn-primary" style="margin-top: 10px;">
                    <i class="fa fa-refresh"></i> 다시 시도
                </button>
            </div>
        `;
    } else {
        console.error('routeStatsContent 요소를 찾을 수 없어 오류 메시지를 표시할 수 없습니다');
    }
}

