document.addEventListener('DOMContentLoaded', function() {

    // 1. 관리자 페이지의 비행기 레이아웃 데이터 객체
    const aircraftData = {
        "model1": {
            name: "보잉 787-9 (기본형)",
            prestigeLayout: ['A', 'B', ' ', 'D', 'E', ' ', 'H', 'J'],
            prestigeRows: [7, 8, 9, 10],
            economyLayout: ['A', 'B', 'C', ' ', 'D', 'E', 'F', ' ', 'G', 'H', 'J'],
            economySections: [
                { startRow: 28, endRow: 43, info: "프레스티지석", removedSeats: { 28: ['A','B','C'], 43: ['D','E','F'] } },
                { startRow: 44, endRow: 57, info: "일반석", removedSeats: { 44: ['C','G','D','E','F'], 45: ['D','E','F'], 57: ['A','J'] } }
            ],
            frontFacilitiesHTML: '<div class="facility-row"><div class="facility-group"><span class="facility-item">🍽</span><span class="facility-item">🍽</span></div></div><div class="facility-row"><div class="facility-group"><span class="facility-item exit-facility" style="margin-left: 20px;">EXIT</span></div><div class="facility-group"><span class="facility-item">🍽</span></div><div class="facility-group"><span class="facility-item">🚻♿</span><span class="facility-item exit-facility" style="margin-right: 20px;">EXIT</span></div></div>',
            prestigeEndFacilitiesHTML: '<div class="exit-row"><span class="exit">EXIT</span><span class="exit">EXIT</span></div><div class="facility-row"><div class="facility-group"><span class="facility-item">🚻♿</span></div><div class="facility-group"><span class="facility-item">🍽</span></div><div class="facility-group"><span class="facility-item">🚻</span></div></div>',
            economy1EndFacilitiesHTML: '<div class="exit-row"><span class="exit">EXIT</span><span class="exit">EXIT</span></div><div class="facility-row"><div class="facility-group"><span class="facility-item">🚻♿</span><span class="facility-item">🚻</span></div><div class="facility-group"><span class="facility-item">🍽</span></div><div class="facility-group"><span class="facility-item">🚻</span></div></div>',
            rearFacilitiesHTML: '<div class="exit-row"><span class="exit">EXIT</span><span class="exit">EXIT</span></div><div class="facility-row"><div class="facility-group"><span class="facility-item">🚻</span></div><div class="facility-group"><span class="facility-item">🍽</span><span class="facility-item">🍽</span></div><div class="facility-group"><span class="facility-item">🚻</span></div></div><div class="facility-row"><div class="facility-group"><span class="facility-item">🍽</span><span class="facility-item">🍽</span></div></div>'
        }
    };

    // 2. 관리자 페이지의 비행기 좌석 생성 함수
    function renderAircraft(modelKey) {
        const airplaneDiv = document.getElementById("airplaneContainer");
        if (!airplaneDiv) return;
        
        const model = aircraftData[modelKey];
        let htmlContent = '';

        if (!model || !model.prestigeLayout) {
            airplaneDiv.innerHTML = '<p>좌석 정보를 불러올 수 없습니다.</p>';
            return;
        }

        htmlContent += model.frontFacilitiesHTML || '';
        htmlContent += '<div class="section-divider"></div>';

        // Prestige Class
        htmlContent += '<p class="info-text">Prestige Class</p>';
        model.prestigeRows.forEach(r => {
            htmlContent += `<div class="visual-seat-row"><div class="row-number">${r}</div><div class="row-content">`;
            model.prestigeLayout.forEach(c => { 
                htmlContent += (c === ' ') ? '<div class="aisle"></div>' : `<div class="seat" data-row="${r}" data-seat="${c}"><span class="seat-letter">${c}</span></div>`; 
            });
            htmlContent += '</div></div>';
        });

        htmlContent += model.prestigeEndFacilitiesHTML || '';
        
        // Economy Sections
        model.economySections.forEach((section, index) => {
            htmlContent += '<div class="section-divider"></div><p class="info-text">' + section.info + '</p>';
            for (let r = section.startRow; r <= section.endRow; r++) {
                htmlContent += `<div class="visual-seat-row"><div class="row-number">${r}</div><div class="row-content">`;
                model.economyLayout.forEach(c => {
                    if (c === ' ') { 
                        htmlContent += '<div class="aisle"></div>'; 
                    } else {
                        let isRemoved = (section.removedSeats && section.removedSeats[r] && section.removedSeats[r].includes(c));
                        htmlContent += isRemoved ? '<div class="seat-removed"></div>' : `<div class="seat" data-row="${r}" data-seat="${c}"><span class="seat-letter">${c}</span></div>`;
                    }
                });
                htmlContent += '</div></div>';
            }
            if (index === 0 && model.economy1EndFacilitiesHTML) { htmlContent += model.economy1EndFacilitiesHTML; }
        });

        htmlContent += model.rearFacilitiesHTML || '';
        airplaneDiv.innerHTML = htmlContent;
        
        // 좌석 렌더링 후 이벤트 리스너 다시 바인딩
        bindSeatClickEvents();
    }

    // 3. 사용자 인터랙션 관련 로직
    const seatForm = document.getElementById('seatForm');
    if (!seatForm) return;

    const hiddenSeatIdInput = document.getElementById('seatId');
    const selectedSeatDisplay = document.getElementById('selectedSeatDisplay');

    function bindSeatClickEvents() {
        const allSeats = document.querySelectorAll('.seat');
        allSeats.forEach(seat => {
            seat.addEventListener('click', function() {
                // 이전에 선택된 좌석의 하이라이트 제거
                const currentSelected = document.querySelector('.seat-selected-highlight');
                if(currentSelected) {
                    currentSelected.classList.remove('seat-selected-highlight');
                }

                // 새로 선택된 좌석에 하이라이트 추가
                this.classList.add('seat-selected-highlight');
                
                const seatId = this.dataset.row + this.dataset.seat;
                const seatType = (parseInt(this.dataset.row) >= 28 && parseInt(this.dataset.row) <= 43) ? '프레스티지석' : '일반석';

                // 사이드바 정보 업데이트
                if(selectedSeatDisplay) {
                    selectedSeatDisplay.innerHTML = `
                        <p class="seat-number">${seatId}</p>
                        <p class="seat-type">${seatType}</p>
                    `;
                }

                // 숨겨진 input 값 설정
                if(hiddenSeatIdInput) {
                    hiddenSeatIdInput.value = seatId;
                }
            });
        });
    }

    seatForm.addEventListener('submit', function(event) {
        if (!hiddenSeatIdInput.value) {
            event.preventDefault();
            alert('좌석을 선택해주세요.');
        }
    });

    // 4. 페이지 로드 시 비행기 그리기
    renderAircraft('model1');

});