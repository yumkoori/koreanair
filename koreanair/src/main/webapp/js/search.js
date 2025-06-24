// 승객 수 정보 추출 함수 (전역)
function getPassengerCount() {
    console.log('=== 승객 수 추출 시작 ===');
    
    // 1순위: JSP에서 계산된 승객 수 직접 사용
    if (typeof window.passengerCount !== 'undefined' && window.passengerCount > 0) {
        console.log('✅ JSP에서 계산된 승객 수 사용:', window.passengerCount);
        console.log('상세 정보 - 성인:', window.adultCount, '소아:', window.childCount, '유아:', window.infantCount);
        return window.passengerCount;
    }
    
    let passengersParam = null;
    
    // 2순위: JSP에서 전달된 승객 정보 문자열 파싱
    if (window.passengersInfo) {
        passengersParam = window.passengersInfo;
        console.log('✅ JSP에서 전달받은 승객 정보:', passengersParam);
    } 
    // 3순위: URL 파라미터에서 승객 수 정보 추출 (URL 디코딩 포함)
    else {
        const urlParams = new URLSearchParams(window.location.search);
        const rawPassengersParam = urlParams.get('passengers');
        if (rawPassengersParam) {
            // URL 디코딩 적용
            passengersParam = decodeURIComponent(rawPassengersParam.replace(/\+/g, ' '));
            console.log('✅ URL 파라미터 (원본):', rawPassengersParam);
            console.log('✅ URL 파라미터 (디코딩):', passengersParam);
        }
    }
    
    if (passengersParam) {
        // 다양한 형태의 승객 정보 파싱
        // "성인 2명", "성인2명", "성인 2명, 소아 1명" 등의 형태 지원
        const adultMatch = passengersParam.match(/성인\s*(\d+)명/);
        const childMatch = passengersParam.match(/소아\s*(\d+)명/);
        const infantMatch = passengersParam.match(/유아\s*(\d+)명/);
        
        const adultCount = adultMatch ? parseInt(adultMatch[1]) : 0;
        const childCount = childMatch ? parseInt(childMatch[1]) : 0;
        const infantCount = infantMatch ? parseInt(infantMatch[1]) : 0;
        
        // 유아는 무료이므로 성인과 소아만 계산
        const totalPassengers = adultCount + childCount;
        
        console.log('✅ 승객 수 파싱 결과:', {
            원본: passengersParam,
            성인: adultCount,
            소아: childCount,
            유아: infantCount,
            총계: totalPassengers
        });
        
        if (totalPassengers > 0) {
            console.log('✅ 최종 승객 수:', totalPassengers);
            return totalPassengers;
        }
    }
    
    // 4순위: 페이지의 승객 표시에서 추출 시도
    const passengerDisplayElement = document.querySelector('.passengers span');
    if (passengerDisplayElement) {
        const displayText = passengerDisplayElement.textContent;
        console.log('✅ 페이지 승객 표시:', displayText);
        
        const adultMatch = displayText.match(/성인\s*(\d+)명/);
        const childMatch = displayText.match(/소아\s*(\d+)명/);
        
        const adultCount = adultMatch ? parseInt(adultMatch[1]) : 0;
        const childCount = childMatch ? parseInt(childMatch[1]) : 0;
        const totalPassengers = adultCount + childCount;
        
        if (totalPassengers > 0) {
            console.log('✅ 페이지에서 추출한 승객 수:', totalPassengers);
            return totalPassengers;
        }
    }
    
    // 기본값: 성인 1명
    console.log('⚠️ 기본값 사용: 성인 1명');
    return 1;
}

// 디버깅용 전역 함수들
window.testPassengerCount = function() {
    console.log('🧪 === 승객 수 테스트 ===');
    console.log('🔍 현재 URL:', window.location.href);
    console.log('📊 window.passengerCount:', window.passengerCount);
    console.log('👨 window.adultCount:', window.adultCount);
    console.log('👶 window.childCount:', window.childCount);
    console.log('🍼 window.infantCount:', window.infantCount);
    console.log('📝 window.passengersInfo:', window.passengersInfo);
    
    const count = getPassengerCount();
    console.log('✅ 최종 승객 수:', count);
    
    const alertMsg = `🧪 승객 수 테스트 결과\n` +
                    `👥 총 승객: ${count}명\n` +
                    `👨 성인: ${window.adultCount || 0}명\n` +
                    `👶 소아: ${window.childCount || 0}명\n` +
                    `🍼 유아: ${window.infantCount || 0}명\n\n` +
                    `📝 승객 정보: ${window.passengersInfo || 'N/A'}`;
    alert(alertMsg);
    return count;
};

window.testPriceCalculation = function(testPrice = 500000) {
    console.log('💰 === 가격 계산 테스트 ===');
    const passengerCount = getPassengerCount();
    const totalPrice = testPrice * passengerCount;
    
    console.log(`🧮 테스트 계산: ${testPrice.toLocaleString('ko-KR')}원 × ${passengerCount}명 = ${totalPrice.toLocaleString('ko-KR')}원`);
    
    const alertMsg = `💰 가격 계산 테스트\n` +
                    `💵 개별가격: ${testPrice.toLocaleString('ko-KR')}원\n` +
                    `👥 승객수: ${passengerCount}명\n` +
                    `💎 총가격: ${totalPrice.toLocaleString('ko-KR')}원`;
    alert(alertMsg);
    return totalPrice;
};

window.testTotalAmountUpdate = function(testPrice = 500000) {
    console.log('🎯 === 총액 업데이트 테스트 ===');
    const totalAmountDisplay = document.querySelector('.total-amount');
    console.log('📍 총액 표시 요소:', totalAmountDisplay);
    
    if (totalAmountDisplay) {
        const passengerCount = getPassengerCount();
        const totalPrice = testPrice * passengerCount;
        const formattedPrice = totalPrice.toLocaleString('ko-KR');
        
        const oldAmount = totalAmountDisplay.textContent;
        totalAmountDisplay.textContent = formattedPrice + '원';
        
        console.log('✅ 업데이트 성공:', oldAmount, ' → ', totalAmountDisplay.textContent);
        alert(`🎯 총액 업데이트 테스트 성공\n${oldAmount} → ${totalAmountDisplay.textContent}`);
    } else {
        console.error('❌ 총액 표시 요소를 찾을 수 없습니다');
        alert('❌ 총액 표시 요소를 찾을 수 없습니다');
    }
};

window.debugUrlParams = function() {
    console.log('🔍 === URL 파라미터 디버깅 ===');
    console.log('현재 URL:', window.location.href);
    console.log('쿼리 스트링:', window.location.search);
    
    const urlParams = new URLSearchParams(window.location.search);
    const rawPassengers = urlParams.get('passengers');
    
    console.log('원본 passengers 파라미터:', rawPassengers);
    
    if (rawPassengers) {
        const decodedPassengers = decodeURIComponent(rawPassengers.replace(/\+/g, ' '));
        console.log('디코딩된 passengers:', decodedPassengers);
    }
    
    const alertMsg = `🔍 URL 파라미터 디버깅\n` +
                    `원본: ${rawPassengers || 'null'}\n` +
                    `디코딩: ${rawPassengers ? decodeURIComponent(rawPassengers.replace(/\+/g, ' ')) : 'null'}\n` +
                    `현재 승객 수: ${getPassengerCount()}명`;
    alert(alertMsg);
};



document.addEventListener('DOMContentLoaded', function() {
    // 현재 페이지 URL 경로 확인
    const currentPath = window.location.pathname;
    const isSearchResults = currentPath.includes('search-results.html') || currentPath.includes('flightSearch.do');
    
    // 페이지 로드 시 승객 수 확인
    console.log('🔍 페이지 로드 시 승객 수 확인');
    const currentPassengerCount = getPassengerCount();
    console.log('📊 현재 페이지의 승객 수:', currentPassengerCount);
    
    // 모든 페이지에서 공통적으로 사용되는 코드
    initializeCommonFunctionality();
    
    // 페이지별 초기화
    if (isSearchResults) {
        initializeSearchResultsPage();
    } else {
        initializeHomePage();
    }
    
    // 공통 기능 초기화
    function initializeCommonFunctionality() {
        // 헤더 드롭다운 메뉴 (모든 페이지에서 사용)
        const dropdowns = document.querySelectorAll('.dropdown');
        
        if (window.innerWidth <= 768) {
            dropdowns.forEach(dropdown => {
                const link = dropdown.querySelector('a');
                
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    
                    const dropdownContent = this.nextElementSibling;
                    
                    // 현재 클릭한 드롭다운이 아닌 다른 드롭다운 메뉴 닫기
                    dropdowns.forEach(otherDropdown => {
                        if (otherDropdown !== dropdown) {
                            const otherContent = otherDropdown.querySelector('.dropdown-content');
                            if (otherContent) {
                                otherContent.style.display = 'none';
                                    }
    }

    // Search Results Page Specific Functionality - JSP Version
    function initializeSearchResultsJSPFunctionality() {
        // Get all necessary elements
        const farePopup = document.getElementById('fareDetailsPopup');
        const flightDetailsPopup = document.getElementById('flightDetailsPopup');
        const overlay = document.getElementById('popupOverlay');
        const closeFareBtn = document.getElementById('closePopupBtn');
        const closeFlightDetailsBtn = document.getElementById('closeFlightDetailsBtn');
        const confirmFlightDetailsBtn = document.getElementById('confirmFlightDetailsBtn');
        const fareColumns = document.querySelectorAll('.fare-column:not(.disabled)');
        const detailBtns = document.querySelectorAll('.detail-btn');
        const totalAmountDisplay = document.querySelector('.total-amount');
        let selectedFarePrice = '';
        
        // Hide overlay initially
        if (overlay) {
            overlay.style.display = 'none';
        }
        
        // Set up event listeners for all fare columns
        fareColumns.forEach(column => {
            column.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                // Get data from the column's data attributes
                const fareType = this.getAttribute('data-fare-type');
                const farePrice = this.getAttribute('data-fare-price');
                const seats = this.getAttribute('data-seats');
                const flightNumber = this.getAttribute('data-flight-number');
                const airline = this.getAttribute('data-airline');
                const changeFee = this.getAttribute('data-change-fee');
                const cancelFee = this.getAttribute('data-cancel-fee');
                const baggage = this.getAttribute('data-baggage');
                const upgrade = this.getAttribute('data-upgrade');
                const mileage = this.getAttribute('data-mileage');
                
                // Store the fare price for potential selection
                selectedFarePrice = farePrice;
                
                // Set popup content
                if (document.getElementById('fareTitle')) {
                    document.getElementById('fareTitle').textContent = fareType;
                    document.getElementById('fareLargePrice').textContent = farePrice + '원';
                    document.getElementById('fareSeats').textContent = seats + '석';
                    document.getElementById('fareFlightNumber').textContent = '편명 ' + flightNumber;
                    document.getElementById('fareAirline').textContent = airline;
                    document.getElementById('changeFee').textContent = changeFee + '원';
                    document.getElementById('cancelFee').textContent = '최대 ' + cancelFee + '원';
                    document.getElementById('baggageInfo').textContent = baggage;
                    document.getElementById('upgradePossibility').textContent = upgrade;
                    document.getElementById('mileageAccrual').textContent = mileage + ' 마일 적립';
                }
                
                // Position the popup in the center of the screen
                if (farePopup) {
                    farePopup.style.position = 'fixed';
                    farePopup.style.left = '50%';
                    farePopup.style.top = '50%';
                    farePopup.style.transform = 'translate(-50%, -50%)';
                    farePopup.style.width = '500px';
                    farePopup.style.maxWidth = '90vw';
                    farePopup.style.maxHeight = '80vh';
                    farePopup.style.display = 'block';
                }
                
                // Show overlay
                if (overlay) {
                    overlay.style.display = 'block';
                }
            });
        });
        
        // Set up event listeners for detail buttons
        detailBtns.forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                // Get flight information from the closest flight-card
                const flightCard = this.closest('.flight-card');
                const flightInfoColumn = flightCard.querySelector('.flight-info-column');
                
                // Extract flight data
                const departureTime = flightInfoColumn.querySelector('.departure-time').textContent;
                const departureCode = flightInfoColumn.querySelector('.departure-code').textContent;
                const arrivalTime = flightInfoColumn.querySelector('.arrival-time').textContent;
                const arrivalCode = flightInfoColumn.querySelector('.arrival-code').textContent;
                const durationTime = flightInfoColumn.querySelector('.duration-time').textContent;
                const flightNumber = flightInfoColumn.querySelector('.flight-number').textContent;
                const airlineName = flightInfoColumn.querySelector('.airline-name').textContent;
                
                // Fill flight details popup with data
                updateFlightDetailsPopup(departureTime, departureCode, arrivalTime, arrivalCode, durationTime, flightNumber, airlineName);
                
                // Position and show the flight details popup
                if (flightDetailsPopup) {
                    flightDetailsPopup.style.position = 'fixed';
                    flightDetailsPopup.style.left = '50%';
                    flightDetailsPopup.style.top = '50%';
                    flightDetailsPopup.style.transform = 'translate(-50%, -50%)';
                    flightDetailsPopup.style.display = 'block';
                }
                
                // Show overlay
                if (overlay) {
                    overlay.style.display = 'block';
                }
            });
        });
        
        // Update flight details popup content
        function updateFlightDetailsPopup(departureTime, departureCode, arrivalTime, arrivalCode, durationTime, flightNumber, airlineName) {
            // Set route information
            if (document.getElementById('flightDetailDeparture')) {
                document.getElementById('flightDetailDeparture').textContent = `출발지 ${departureCode} 서울/인천`;
                document.getElementById('flightDetailArrival').textContent = `도착지 ${arrivalCode} 도쿄/나리타`;
                document.getElementById('flightDetailDuration').textContent = `총 ${durationTime} 여정`;
                
                // Set flight information
                document.getElementById('flightDetailNumber').textContent = `항공편명 ${flightNumber}`;
                document.getElementById('flightDetailAircraft').textContent = '항공기종 B737-800';
                document.getElementById('flightDetailOperator').textContent = airlineName;
                
                // Set departure information
                document.getElementById('flightDetailDepartureCode').textContent = `${departureCode} 서울/인천`;
                document.getElementById('flightDetailDepartureTime').textContent = `출발시간 2025년 05월 22일 (목) ${departureTime}`;
                document.getElementById('flightDetailDepartureTerminal').textContent = '터미널 2';
                
                // Set journey duration
                document.getElementById('flightDetailJourneyTime').textContent = `${durationTime} 소요`;
                
                // Set arrival information
                document.getElementById('flightDetailArrivalCode').textContent = `${arrivalCode} 도쿄/나리타`;
                document.getElementById('flightDetailArrivalTime').textContent = `도착시간 2025년 05월 22일 (목) ${arrivalTime}`;
                document.getElementById('flightDetailArrivalTerminal').textContent = '터미널 1';
            }
        }
        
        // Close fare popup when clicking the close button
        if (closeFareBtn) {
            closeFareBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                if (farePopup) farePopup.style.display = 'none';
                if (overlay) overlay.style.display = 'none';
            });
        }
        
        // Close flight details popup when clicking the close button
        if (closeFlightDetailsBtn) {
            closeFlightDetailsBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                if (flightDetailsPopup) flightDetailsPopup.style.display = 'none';
                if (overlay) overlay.style.display = 'none';
            });
        }
        
        // Close flight details popup when clicking the confirm button
        if (confirmFlightDetailsBtn) {
            confirmFlightDetailsBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                if (flightDetailsPopup) flightDetailsPopup.style.display = 'none';
                if (overlay) overlay.style.display = 'none';
            });
        }
        
        // Close popup when clicking outside
        if (overlay) {
            overlay.addEventListener('click', function() {
                if (farePopup) farePopup.style.display = 'none';
                if (flightDetailsPopup) flightDetailsPopup.style.display = 'none';
                if (overlay) overlay.style.display = 'none';
            });
        }
        
        // Prevent popup from closing when clicking inside it
        if (farePopup) {
            farePopup.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }
        
        if (flightDetailsPopup) {
            flightDetailsPopup.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }
        
        // Seat preview button
        const seatsPreviewBtn = document.getElementById('seatsPreviewBtn');
        if (seatsPreviewBtn) {
            seatsPreviewBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                alert('좌석 정보를 확인합니다.');
            });
        }
        
        // Select fare button
        const selectFareBtn = document.getElementById('selectFareBtn');
        if (selectFareBtn) {
            selectFareBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                
                // Update the total amount in the bottom payment bar
                if (selectedFarePrice && totalAmountDisplay) {
                    // 승객 수 가져오기
                    const passengerCount = getPassengerCount();
                    
                    console.log('=== 팝업에서 가격 계산 시작 ===');
                    console.log('선택된 가격 (원시):', selectedFarePrice);
                    console.log('선택된 가격 (숫자):', parseInt(selectedFarePrice));
                    console.log('승객 수:', passengerCount);
                    
                    // 총 가격 계산 (개별 가격 × 승객 수)
                    const individualPrice = parseInt(selectedFarePrice);
                    const totalPrice = individualPrice * passengerCount;
                    const formattedTotalPrice = totalPrice.toLocaleString('ko-KR');
                    
                    console.log('계산: ', individualPrice, ' × ', passengerCount, ' = ', totalPrice);
                    console.log('포맷된 가격:', formattedTotalPrice);
                    
                    const oldAmount = totalAmountDisplay.textContent;
                    totalAmountDisplay.textContent = formattedTotalPrice + '원';
                    
                    console.log('총액 업데이트:', oldAmount, ' → ', totalAmountDisplay.textContent);
                    console.log('=== 팝업에서 가격 계산 완료 ===');
                    
                    // Visual feedback for selection
                    const selectedFare = document.getElementById('fareTitle').textContent;
                    const selectedPrice = document.getElementById('fareLargePrice').textContent;
                    
                    // Remove any previous selection indicators
                    fareColumns.forEach(col => {
                        col.classList.remove('selected');
                    });
                    
                    // Add selection indicator to the selected fare
                    fareColumns.forEach(col => {
                        if (col.getAttribute('data-fare-type') === selectedFare && 
                            col.getAttribute('data-fare-price') + '원' === selectedPrice) {
                            col.classList.add('selected');
                        }
                    });
                }
                
                // Close the popup
                if (farePopup) farePopup.style.display = 'none';
                if (overlay) overlay.style.display = 'none';
            });
        }



        // 새로운 기능: 좌석 카드 직접 클릭으로 총액 업데이트
        const clickableFareCards = document.querySelectorAll('.clickable-fare');
        console.log('찾은 클릭 가능한 좌석 카드 수:', clickableFareCards.length);
        
        clickableFareCards.forEach((card, index) => {
            console.log(`좌석 카드 ${index + 1} 정보:`, {
                fareType: card.getAttribute('data-fare-type'),
                flightId: card.getAttribute('data-flight-id'),
                priceElement: card.querySelector('.fare-price[data-price]')
            });
            
            // 매진된 카드는 클릭 불가
            const noAvailableElement = card.querySelector('.fare-price.no-available');
            if (noAvailableElement) {
                console.log(`좌석 카드 ${index + 1}은 매진되어 클릭 불가`);
                return; // 매진된 경우 이벤트 리스너 추가하지 않음
            }
            
            card.addEventListener('click', function(e) {
                console.log('🎯 좌석 카드 클릭됨:', this.getAttribute('data-fare-type'));
                
                // 기존 팝업 표시 방지
                e.preventDefault();
                e.stopPropagation();
                
                // 다른 선택된 카드에서 selected 클래스 제거
                clickableFareCards.forEach(otherCard => {
                    otherCard.classList.remove('selected');
                });
                
                // 현재 카드에 selected 클래스 추가
                this.classList.add('selected');
                
                // 가격 정보 가져오기
                const priceElement = this.querySelector('.fare-price[data-price]');
                console.log('💰 가격 요소:', priceElement);
                console.log('📍 총액 표시 요소:', totalAmountDisplay);
                
                if (priceElement && totalAmountDisplay) {
                    const price = priceElement.getAttribute('data-price');
                    const fareType = this.getAttribute('data-fare-type');
                    const flightId = this.getAttribute('data-flight-id');
                    
                    console.log('🎫 좌석 정보:', { fareType, flightId, price });
                    
                    // 승객 수 가져오기
                    const passengerCount = getPassengerCount();
                    
                    console.log('🧮 === 가격 계산 시작 ===');
                    console.log('💵 개별 가격 (원시):', price);
                    console.log('💵 개별 가격 (숫자):', parseInt(price));
                    console.log('👥 승객 수:', passengerCount);
                    
                    // 총 가격 계산 (개별 가격 × 승객 수)
                    const individualPrice = parseInt(price);
                    
                    if (isNaN(individualPrice)) {
                        console.error('❌ 가격을 숫자로 변환할 수 없습니다:', price);
                        return;
                    }
                    
                    if (passengerCount <= 0) {
                        console.error('❌ 승객 수가 유효하지 않습니다:', passengerCount);
                        return;
                    }
                    
                    const totalPrice = individualPrice * passengerCount;
                    
                    console.log('🧮 계산식:', individualPrice, ' × ', passengerCount, ' = ', totalPrice);
                    
                    // 총액 업데이트 (천 단위 콤마 추가)
                    const formattedPrice = totalPrice.toLocaleString('ko-KR');
                    
                    console.log('🎨 포맷된 가격:', formattedPrice);
                    
                    if (totalAmountDisplay) {
                        const oldAmount = totalAmountDisplay.textContent;
                        totalAmountDisplay.textContent = formattedPrice + '원';
                        console.log('✅ 총액 업데이트 성공:', oldAmount, ' → ', totalAmountDisplay.textContent);
                        
                        // 시각적 피드백 추가
                        totalAmountDisplay.style.backgroundColor = '#4CAF50';
                        totalAmountDisplay.style.color = 'white';
                        setTimeout(() => {
                            totalAmountDisplay.style.backgroundColor = '';
                            totalAmountDisplay.style.color = '';
                        }, 1000);
                        
                    } else {
                        console.error('❌ 총액 표시 요소를 찾을 수 없습니다!');
                    }
                    
                    console.log('🎉 === 가격 계산 완료 ===');
                } else {
                    console.error('❌ 가격 요소 또는 총액 표시 요소를 찾을 수 없음');
                    console.log('priceElement:', priceElement);
                    console.log('totalAmountDisplay:', totalAmountDisplay);
                }
            });
        });
        
        // Currency dropdown functionality
        const currencyBtn = document.querySelector('.currency-btn');
        const currencyDropdown = document.querySelector('.currency-dropdown');
        const currencyOptions = document.querySelectorAll('.currency-option');
        const currencyCode = document.querySelector('.currency-code');
        
        // Toggle currency dropdown
        if (currencyBtn) {
            currencyBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                if (currencyDropdown) {
                    currencyDropdown.classList.toggle('active');
                }
            });
        }
        
        // Handle currency option selection
        currencyOptions.forEach(option => {
            option.addEventListener('click', function(e) {
                e.stopPropagation();
                const currency = this.getAttribute('data-currency');
                
                // Update the button text
                if (currencyCode) {
                    currencyCode.textContent = currency;
                }
                
                // Update selected class
                currencyOptions.forEach(opt => opt.classList.remove('selected'));
                this.classList.add('selected');
                
                // Hide dropdown
                if (currencyDropdown) {
                    currencyDropdown.classList.remove('active');
                }
            });
        });
        
        // Close currency dropdown when clicking outside
        document.addEventListener('click', function() {
            if (currencyDropdown) {
                currencyDropdown.classList.remove('active');
            }
        });
        
        // Next button functionality - 이제 seat-selection.js에서 처리함
        // const nextBtn = document.querySelector('.next-btn');
        // if (nextBtn) {
        //     nextBtn.addEventListener('click', function() {
        //         const currentTotal = totalAmountDisplay ? totalAmountDisplay.textContent : '0원';
        //         if (currentTotal === '0원') {
        //             alert('항공권을 먼저 선택해주세요.');
        //         } else {
        //             alert('선택하신 ' + currentTotal + ' 항공권으로 다음 여정을 진행합니다.');
        //         }
        //     });
        // }
    }

    // Initialize JSP-specific functionality if on search results page
    if (isSearchResults || currentPath.includes('search-results.jsp')) {
        initializeSearchResultsJSPFunctionality();
    }
});
                    
                    // 현재 드롭다운 메뉴 토글
                    if (dropdownContent.style.display === 'block') {
                        dropdownContent.style.display = 'none';
                    } else {
                        dropdownContent.style.display = 'block';
                    }
                });
            });
        }
        
        // 스크롤 이벤트 - 헤더 애니메이션
        const header = document.querySelector('header');
        const headerTop = document.querySelector('.header-top');
        let lastScrollTop = 0;
        let scrollDirection = 'none';
        
        window.addEventListener('scroll', function() {
            const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            
            // 스크롤 방향 감지
            if (scrollTop > lastScrollTop) {
                scrollDirection = 'down';
            } else {
                scrollDirection = 'up';
            }
            
            // 스크롤 위치에 따른 헤더 스타일 변경
            if (scrollTop > 100) {
                header.classList.add('scrolled');
                
                // 스크롤 다운 시 상단 메뉴 숨기기
                if (scrollDirection === 'down' && scrollTop > 200 && headerTop) {
                    headerTop.style.transform = 'translateY(-100%)';
                    headerTop.style.opacity = '0';
                    header.style.transform = 'translateY(-' + headerTop.offsetHeight + 'px)';
                } 
                // 스크롤 업 시 상단 메뉴 다시 보이기
                else if (scrollDirection === 'up' && headerTop) {
                    headerTop.style.transform = 'translateY(0)';
                    headerTop.style.opacity = '1';
                    header.style.transform = 'translateY(0)';
                }
            } else {
                header.classList.remove('scrolled');
                if (headerTop) {
                    headerTop.style.transform = 'translateY(0)';
                    headerTop.style.opacity = '1';
                    header.style.transform = 'translateY(0)';
                }
            }
            
            lastScrollTop = scrollTop <= 0 ? 0 : scrollTop; // iOS 바운스 효과 방지
        });
    }
    

    
    // 검색 결과 페이지 기능 초기화 (search-results.html)
    function initializeSearchResultsPage() {
        console.log('검색 결과 페이지 초기화');
        
        // 날짜 선택 기능 초기화
        initializeDatePicker();
        
        // 인원수 선택 기능 초기화
        initializePassengerSelector();
        
        // 좌석 등급 선택 기능 초기화
        initializeSeatTypeSelector();
        
        // 날짜별 가격 선택 기능 초기화
        initializeDatePriceBar();
        
        // 좌석 등급 버튼 기능 초기화
        initializeSeatClassButtons();
        
        // 필터 버튼 기능 초기화
        initializeFilterButtons();
        
        // 공항 데이터
        const airports = [
            { code: 'ICN', name: '인천국제공항', city: '서울/인천' },
            { code: 'GMP', name: '김포국제공항', city: '서울' },
            { code: 'PUS', name: '김해국제공항', city: '부산' },
            { code: 'CJU', name: '제주국제공항', city: '제주' },
            { code: 'NRT', name: '나리타국제공항', city: '도쿄' },
            { code: 'HND', name: '하네다국제공항', city: '도쿄' },
            { code: 'KIX', name: '간사이국제공항', city: '오사카' },
            { code: 'PEK', name: '베이징수도국제공항', city: '베이징' },
            { code: 'PVG', name: '푸동국제공항', city: '상하이' },
            { code: 'HKG', name: '홍콩국제공항', city: '홍콩' },
        ];
        
        // 위치 선택자 초기화
        const locationSelectors = document.querySelectorAll('.location-selector');
        const swapLocationsBtn = document.querySelector('.swap-locations');
        
        if (locationSelectors.length > 0) {
            console.log('위치 선택자 초기화:', locationSelectors.length);
            
            locationSelectors.forEach(selector => {
                const locationSpan = selector.querySelector('.location');
                const dropdown = selector.querySelector('.location-dropdown');
                
                if (!locationSpan || !dropdown) {
                    console.log('위치 선택자 요소 누락:', selector);
                    return;
                }
                
                // 드롭다운 초기 상태를 명시적으로 설정
                dropdown.style.display = 'none';
                
                console.log('위치 선택자 설정:', locationSpan.textContent);
                
                // 위치 클릭 이벤트
                locationSpan.addEventListener('click', function(e) {
                    console.log('위치 클릭됨:', this.textContent);
                    e.preventDefault();
                    e.stopPropagation();
                    
                    // 다른 모든 드롭다운 닫기
                    document.querySelectorAll('.location-dropdown').forEach(d => {
                        if (d !== dropdown) {
                            d.style.display = 'none';
                        }
                    });
                    
                    // 현재 드롭다운 토글
                    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
                    console.log('드롭다운 상태:', dropdown.style.display);
                });
                
                // 검색 기능
                const searchInput = dropdown.querySelector('input[type="text"]');
                if (searchInput) {
                    searchInput.addEventListener('click', function(e) {
                        e.stopPropagation();
                    });
                    
                    searchInput.addEventListener('input', function(e) {
                        const searchTerm = e.target.value.toLowerCase();
                        const allAirports = dropdown.querySelectorAll('li');
                        
                        allAirports.forEach(airport => {
                            const text = airport.textContent.toLowerCase();
                            airport.style.display = text.includes(searchTerm) ? 'block' : 'none';
                        });
                    });
                }
                
                // 공항 선택 기능
                const airportLists = dropdown.querySelectorAll('ul');
                airportLists.forEach(list => {
                    list.addEventListener('click', function(e) {
                        e.stopPropagation();
                        if (e.target.tagName === 'LI') {
                            locationSpan.textContent = e.target.textContent;
                            dropdown.style.display = 'none';
                        }
                    });
                });
            });
            
            // 드롭다운 외부 클릭 시 닫기
            document.addEventListener('click', function(e) {
                if (!e.target.closest('.location-selector')) {
                    document.querySelectorAll('.location-dropdown').forEach(dropdown => {
                        dropdown.style.display = 'none';
                    });
                }
            });
        }
        
        // 위치 교환 기능
        if (swapLocationsBtn) {
            swapLocationsBtn.addEventListener('click', function() {
                const departureSpan = document.querySelector('.location[data-type="departure"]');
                const arrivalSpan = document.querySelector('.location[data-type="arrival"]');
                
                if (departureSpan && arrivalSpan) {
                    const tempText = departureSpan.textContent;
                    departureSpan.textContent = arrivalSpan.textContent;
                    arrivalSpan.textContent = tempText;
                }
            });
        }
        
        // 정렬 기능
        const sortSelect = document.querySelector('.sort-by');
        if (sortSelect) {
            sortSelect.addEventListener('change', function() {
                const flightsList = document.querySelector('.flights-list');
                if (!flightsList) return;
                
                const flightCards = Array.from(flightsList.querySelectorAll('.flight-card'));
                
                flightCards.sort((a, b) => {
                    const valueA = getSortValue(a, this.value);
                    const valueB = getSortValue(b, this.value);
                    return valueA - valueB;
                });
                
                flightsList.innerHTML = '';
                flightCards.forEach(card => flightsList.appendChild(card));
            });
        }
        
        function getSortValue(card, sortType) {
            try {
                switch(sortType) {
                    case 'price':
                        return parseInt(card.querySelector('.amount').textContent.replace(/,/g, ''));
                    case 'duration':
                        const duration = card.querySelector('.duration span').textContent;
                        const [hours, minutes] = duration.match(/\d+/g) || [0, 0];
                        return parseInt(hours) * 60 + parseInt(minutes);
                    case 'departure':
                        return parseInt(card.querySelector('.departure time').textContent.replace(':', ''));
                    default:
                        return 0;
                }
            } catch (e) {
                console.error('정렬 값 계산 오류:', e);
                return 0;
            }
        }
        
        // URL 파라미터 처리
        const params = new URLSearchParams(window.location.search);
        
        // 필수 파라미터가 있는지 확인
        const departure = params.get('departure');
        const arrival = params.get('arrival');
        const departureDate = params.get('departureDate') ? new Date(params.get('departureDate')) : null;
        
        if (departure && arrival && departureDate) {
            const returnDate = params.get('returnDate') ? new Date(params.get('returnDate')) : null;
            const passengers = params.get('passengers') || '1';
            const seatClass = params.get('seatClass') || '일반석';

            // DOM 요소 업데이트
            const departureEl = document.querySelector('.location[data-type="departure"]');
            const arrivalEl = document.querySelector('.location[data-type="arrival"]');
            
            if (departureEl && arrivalEl) {
                departureEl.textContent = departure;
                arrivalEl.textContent = arrival;
            }

            // 날짜 표시
            const dateRangeEl = document.querySelector('.date-range span');
            if (dateRangeEl) {
                // 날짜 형식 변환 함수 사용 (YYYY.MM.DD 형식)
                function formatDateForDisplay(date) {
                    const year = date.getFullYear();
                    const month = String(date.getMonth() + 1).padStart(2, '0');
                    const day = String(date.getDate()).padStart(2, '0');
                    return `${year}.${month}.${day}`;
                }
                
                let dateText = formatDateForDisplay(departureDate);
                
                if (returnDate) {
                    dateText += ' ~ ' + formatDateForDisplay(returnDate);
                }
                
                dateRangeEl.textContent = dateText;
            }

            // 승객 수와 좌석 등급 업데이트
            const passengersEl = document.querySelector('.passengers span');
            const seatTypeEl = document.querySelector('.seat-type span');
            
            if (passengersEl && seatTypeEl) {
                passengersEl.textContent = `${passengers}`;
                seatTypeEl.textContent = seatClass;
            }
        }
        
        // 날짜 선택 달력 기능
        function initializeDatePicker() {
            const dateRange = document.querySelector('.date-range');
            const dateSpan = dateRange.querySelector('span');
            const calendarDropdown = dateRange.querySelector('.calendar-dropdown');
            const currentMonthSpan = dateRange.querySelector('.current-month');
            const prevMonthBtn = dateRange.querySelector('.prev-month');
            const nextMonthBtn = dateRange.querySelector('.next-month');
            const daysContainer = dateRange.querySelector('.days');
            const applyBtn = dateRange.querySelector('.apply-date');
            const cancelBtn = dateRange.querySelector('.cancel-date');
            
            let currentDate = new Date();
            let selectedStartDate = null;
            let selectedEndDate = null;
            let tempEndDate = null;
            
            // 날짜 형식 변환 함수 (YYYY.MM.DD)
            function formatDate(date) {
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                return `${year}.${month}.${day}`;
            }
            
            // 달력 날짜 생성 함수
            function generateCalendar(year, month) {
                daysContainer.innerHTML = '';
                
                // 현재 월의 첫 날과 마지막 날
                const firstDay = new Date(year, month, 1);
                const lastDay = new Date(year, month + 1, 0);
                
                // 이전 달의 마지막 날들
                const firstDayIndex = firstDay.getDay(); // 0:일요일, 1:월요일, ...
                
                // 다음 달의 첫 날들
                const lastDayIndex = lastDay.getDay();
                const nextDays = 7 - lastDayIndex - 1;
                
                // 월 표시 업데이트
                currentMonthSpan.textContent = `${year}년 ${month + 1}월`;
                
                // 이전 달의 마지막 날들
                for (let i = firstDayIndex; i > 0; i--) {
                    const emptyDay = document.createElement('div');
                    emptyDay.classList.add('day', 'empty');
                    daysContainer.appendChild(emptyDay);
                }
                
                const today = new Date();
                
                // 현재 달의 날짜들
                for (let i = 1; i <= lastDay.getDate(); i++) {
                    const day = document.createElement('div');
                    day.classList.add('day');
                    day.textContent = i;
                    
                    const currentDateObj = new Date(year, month, i);
                    
                    // 오늘 날짜 표시
                    if (today.getFullYear() === year && 
                        today.getMonth() === month && 
                        today.getDate() === i) {
                        day.classList.add('today');
                    }
                    
                    // 선택된 날짜 범위 표시
                    if (selectedStartDate && selectedEndDate) {
                        if (currentDateObj.getTime() === selectedStartDate.getTime() ||
                            currentDateObj.getTime() === selectedEndDate.getTime()) {
                            day.classList.add('selected');
                        } else if (currentDateObj > selectedStartDate && currentDateObj < selectedEndDate) {
                            day.classList.add('range');
                        }
                    } else if (selectedStartDate && tempEndDate) {
                        if (currentDateObj.getTime() === selectedStartDate.getTime() ||
                            currentDateObj.getTime() === tempEndDate.getTime()) {
                            day.classList.add('selected');
                        } else if (currentDateObj > selectedStartDate && currentDateObj < tempEndDate) {
                            day.classList.add('range');
                        }
                    } else if (selectedStartDate && currentDateObj.getTime() === selectedStartDate.getTime()) {
                        day.classList.add('selected');
                    }
                    
                    // 날짜 클릭 이벤트
                    day.addEventListener('click', function() {
                        const clickedDate = new Date(year, month, i);
                        
                        if (!selectedStartDate || (selectedStartDate && selectedEndDate)) {
                            // 시작일 선택 또는 새로운 선택 시작
                            selectedStartDate = clickedDate;
                            selectedEndDate = null;
                            tempEndDate = null;
                        } else {
                            // 종료일 선택
                            if (clickedDate < selectedStartDate) {
                                // 시작일보다 이전 날짜를 선택한 경우, 시작일을 변경
                                selectedEndDate = selectedStartDate;
                                selectedStartDate = clickedDate;
                            } else {
                                selectedEndDate = clickedDate;
                            }
                        }
                        
                        // 달력 다시 생성
                        generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                    });
                    
                    // 마우스 호버 효과 (범위 미리보기)
                    day.addEventListener('mouseenter', function() {
                        if (selectedStartDate && !selectedEndDate) {
                            const hoverDate = new Date(year, month, i);
                            tempEndDate = hoverDate;
                            generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                        }
                    });
                    
                    daysContainer.appendChild(day);
                }
                
                // 다음 달의 첫 날들
                for (let i = 1; i <= nextDays; i++) {
                    const emptyDay = document.createElement('div');
                    emptyDay.classList.add('day', 'empty');
                    daysContainer.appendChild(emptyDay);
                }
            }
            
            // 현재 월 달력 생성
            if (dateRange && calendarDropdown) {
                generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                
                // 달력 표시/숨김 토글
                dateSpan.addEventListener('click', function(e) {
                    e.stopPropagation();
                    calendarDropdown.style.display = calendarDropdown.style.display === 'block' ? 'none' : 'block';
                });
                
                // 이전 달 버튼
                prevMonthBtn.addEventListener('click', function() {
                    currentDate.setMonth(currentDate.getMonth() - 1);
                    generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                });
                
                // 다음 달 버튼
                nextMonthBtn.addEventListener('click', function() {
                    currentDate.setMonth(currentDate.getMonth() + 1);
                    generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                });
                
                // 적용 버튼
                applyBtn.addEventListener('click', function() {
                    if (selectedStartDate && selectedEndDate) {
                        const startFormatted = formatDate(selectedStartDate);
                        const endFormatted = formatDate(selectedEndDate);
                        dateSpan.textContent = `${startFormatted} ~ ${endFormatted}`;
                        calendarDropdown.style.display = 'none';
                    } else if (selectedStartDate) {
                        const startFormatted = formatDate(selectedStartDate);
                        dateSpan.textContent = startFormatted;
                        calendarDropdown.style.display = 'none';
                    }
                });
                
                // 취소 버튼
                cancelBtn.addEventListener('click', function() {
                    calendarDropdown.style.display = 'none';
                });
                
                // 외부 클릭 시 달력 닫기
                document.addEventListener('click', function(e) {
                    if (!dateRange.contains(e.target)) {
                        calendarDropdown.style.display = 'none';
                    }
                });
            }
        }
        
        // 인원수 선택 기능
        function initializePassengerSelector() {
            const passengersElement = document.querySelector('.passengers');
            const passengersSpan = passengersElement.querySelector('span');
            const passengersDropdown = passengersElement.querySelector('.passengers-dropdown');
            
            // 인원수 선택 요소들
            const adultCountEl = passengersElement.querySelector('.adult-count');
            const childCountEl = passengersElement.querySelector('.child-count');
            const infantCountEl = passengersElement.querySelector('.infant-count');
            
            // 증가/감소 버튼들
            const decreaseBtns = passengersElement.querySelectorAll('.decrease');
            const increaseBtns = passengersElement.querySelectorAll('.increase');
            
            // 적용 버튼
            const applyBtn = passengersElement.querySelector('.apply-passengers');
            
            // 현재 인원수 상태
            let adultCount = parseInt(adultCountEl.textContent);
            let childCount = parseInt(childCountEl.textContent);
            let infantCount = parseInt(infantCountEl.textContent);
            
            // 최소/최대 인원수 제한
            const MIN_ADULT = 1;  // 최소 성인 1명
            const MAX_ADULT = 9;
            const MIN_CHILD = 0;
            const MAX_CHILD = 8;
            const MIN_INFANT = 0;
            const MAX_INFANT = 4;
            const MAX_TOTAL = 9;  // 총 인원수 제한
            
            // 버튼 활성화/비활성화 상태 업데이트
            function updateButtonStates() {
                // 총 인원수 계산
                const totalCount = adultCount + childCount + infantCount;
                
                // 성인 감소 버튼
                decreaseBtns[0].classList.toggle('disabled', adultCount <= MIN_ADULT);
                
                // 성인 증가 버튼
                increaseBtns[0].classList.toggle('disabled', adultCount >= MAX_ADULT || totalCount >= MAX_TOTAL);
                
                // 소아 감소 버튼
                decreaseBtns[1].classList.toggle('disabled', childCount <= MIN_CHILD);
                
                // 소아 증가 버튼
                increaseBtns[1].classList.toggle('disabled', childCount >= MAX_CHILD || totalCount >= MAX_TOTAL);
                
                // 유아 감소 버튼
                decreaseBtns[2].classList.toggle('disabled', infantCount <= MIN_INFANT);
                
                // 유아 증가 버튼 (유아는 성인 수를 초과할 수 없음)
                increaseBtns[2].classList.toggle('disabled', 
                    infantCount >= MAX_INFANT || 
                    infantCount >= adultCount || 
                    totalCount >= MAX_TOTAL);
            }
            
            // 인원수 표시 업데이트
            function updateCountDisplay() {
                adultCountEl.textContent = adultCount;
                childCountEl.textContent = childCount;
                infantCountEl.textContent = infantCount;
                
                updateButtonStates();
            }
            
            // 증가 버튼 이벤트
            increaseBtns.forEach((btn, index) => {
                btn.addEventListener('click', function(e) {
                    e.stopPropagation();
                    
                    if (btn.classList.contains('disabled')) return;
                    
                    if (index === 0) { // 성인
                        adultCount = Math.min(adultCount + 1, MAX_ADULT);
                    } else if (index === 1) { // 소아
                        childCount = Math.min(childCount + 1, MAX_CHILD);
                    } else if (index === 2) { // 유아
                        infantCount = Math.min(infantCount + 1, MAX_INFANT);
                    }
                    
                    updateCountDisplay();
                });
            });
            
            // 감소 버튼 이벤트
            decreaseBtns.forEach((btn, index) => {
                btn.addEventListener('click', function(e) {
                    e.stopPropagation();
                    
                    if (btn.classList.contains('disabled')) return;
                    
                    if (index === 0) { // 성인
                        adultCount = Math.max(adultCount - 1, MIN_ADULT);
                        // 성인 수가 줄어들면 유아 수도 조정
                        if (infantCount > adultCount) {
                            infantCount = adultCount;
                        }
                    } else if (index === 1) { // 소아
                        childCount = Math.max(childCount - 1, MIN_CHILD);
                    } else if (index === 2) { // 유아
                        infantCount = Math.max(infantCount - 1, MIN_INFANT);
                    }
                    
                    updateCountDisplay();
                });
            });
            
            // 적용 버튼 이벤트
            applyBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                
                // 인원수 텍스트 구성
                let passengersText = `성인 ${adultCount}명`;
                
                if (childCount > 0) {
                    passengersText += `, 소아 ${childCount}명`;
                }
                
                if (infantCount > 0) {
                    passengersText += `, 유아 ${infantCount}명`;
                }
                
                // 텍스트 업데이트 및 드롭다운 닫기
                passengersSpan.textContent = passengersText;
                passengersDropdown.style.display = 'none';
            });
            
            // 인원수 영역 클릭 시 드롭다운 토글
            passengersSpan.addEventListener('click', function(e) {
                e.stopPropagation();
                
                // 달력 드롭다운이 열려있으면 닫기
                const calendarDropdown = document.querySelector('.calendar-dropdown');
                if (calendarDropdown) {
                    calendarDropdown.style.display = 'none';
                }
                
                // 위치 드롭다운이 열려있으면 닫기
                document.querySelectorAll('.location-dropdown').forEach(dropdown => {
                    dropdown.style.display = 'none';
                });
                
                // 인원수 드롭다운 토글
                passengersDropdown.style.display = 
                    passengersDropdown.style.display === 'block' ? 'none' : 'block';
                
                updateButtonStates();
            });
            
            // 외부 클릭 시 드롭다운 닫기
            document.addEventListener('click', function(e) {
                if (!passengersElement.contains(e.target)) {
                    passengersDropdown.style.display = 'none';
                }
            });
            
            // 초기 버튼 상태 설정
            updateButtonStates();
        }
        
        // 좌석 등급 코드 매핑 함수
        function getSeatClassCode(seatType) {
            if (seatType === 'economy' || seatType === '일반석') {
                return 'ECONOMY';
            } else if (seatType === 'prestige' || seatType === '프레스티지석') {
                return 'PRE';
            } else if (seatType === 'first' || seatType === '일등석') {
                return 'FIR';
            }
            return 'null'; // 기본값
        }
        
        // 좌석 등급 선택 기능
        function initializeSeatTypeSelector() {
            const seatTypeElement = document.querySelector('.seat-type');
            const seatTypeSpan = seatTypeElement.querySelector('span');
            const seatTypeDropdown = seatTypeElement.querySelector('.seat-type-dropdown');
            const seatOptions = seatTypeElement.querySelectorAll('.seat-option');
            
            // 현재 선택된 좌석 등급 (기본: 일반석) - 전역 변수로 만들기
            window.selectedSeatType = 'economy';
            
            // 초기 선택 상태 설정
            updateSeatSelection();
            
            // 좌석 선택 상태 업데이트
            function updateSeatSelection() {
                seatOptions.forEach(option => {
                    const type = option.getAttribute('data-type');
                    if (type === window.selectedSeatType) {
                        option.classList.add('selected');
                    } else {
                        option.classList.remove('selected');
                    }
                });
            }
            
            // 좌석 옵션 클릭 이벤트
            seatOptions.forEach(option => {
                option.addEventListener('click', function(e) {
                    e.stopPropagation();
                    
                    window.selectedSeatType = this.getAttribute('data-type');
                    
                    // 선택 상태 업데이트
                    updateSeatSelection();
                    
                    // 표시 텍스트 업데이트
                    let seatTypeName = '일반석';
                    
                    if (window.selectedSeatType === 'prestige') {
                        seatTypeName = '프레스티지석';
                    } else if (window.selectedSeatType === 'first') {
                        seatTypeName = '일등석';
                    }
                    
                    seatTypeSpan.textContent = seatTypeName;
                    
                    console.log('좌석 등급 선택:', window.selectedSeatType, '코드:', getSeatClassCode(window.selectedSeatType));
                    
                    // 드롭다운 닫기
                    seatTypeDropdown.style.display = 'none';
                });
            });
            
            // 좌석 등급 영역 클릭 시 드롭다운 토글
            seatTypeSpan.addEventListener('click', function(e) {
                e.stopPropagation();
                
                // 다른 드롭다운들 닫기
                const calendarDropdown = document.querySelector('.calendar-dropdown');
                if (calendarDropdown) {
                    calendarDropdown.style.display = 'none';
                }
                
                const passengersDropdown = document.querySelector('.passengers-dropdown');
                if (passengersDropdown) {
                    passengersDropdown.style.display = 'none';
                }
                
                document.querySelectorAll('.location-dropdown').forEach(dropdown => {
                    dropdown.style.display = 'none';
                });
                
                // 좌석 등급 드롭다운 토글
                seatTypeDropdown.style.display = 
                    seatTypeDropdown.style.display === 'block' ? 'none' : 'block';
            });
            
            // 외부 클릭 시 드롭다운 닫기
            document.addEventListener('click', function(e) {
                if (!seatTypeElement.contains(e.target)) {
                    seatTypeDropdown.style.display = 'none';
                }
            });
        }
        
        // 날짜별 가격 선택 기능
        function initializeDatePriceBar() {
            const datePriceItems = document.querySelectorAll('.date-price-item');
            
            // 날짜 가격 아이템 클릭 이벤트
            datePriceItems.forEach(item => {
                item.addEventListener('click', function() {
                    // 기존 선택 항목 제거
                    datePriceItems.forEach(item => item.classList.remove('active'));
                    
                    // 현재 항목 선택
                    this.classList.add('active');
                    
                    // 날짜와 요일 정보 가져오기
                    const day = this.querySelector('.date-day').textContent.padStart(2, '0');
                    const weekday = this.querySelector('.date-weekday').textContent;
                    const price = this.querySelector('.price-amount').textContent;
                    
                    console.log(`선택된 날짜: ${day}일(${weekday}), 가격: ${price}`);
                    
                    // 현재 URL에서 쿼리 파라미터 가져오기
                    const urlParams = new URLSearchParams(window.location.search);
                    const currentDepartureDate = urlParams.get('departureDate');
                    
                    if (currentDepartureDate) {
                        // 원래 기준 날짜 설정 (처음 요청된 날짜를 계속 유지)
                        let originalDepartureDate = urlParams.get('originalDepartureDate');
                        if (!originalDepartureDate) {
                            // 첫 요청인 경우 현재 departureDate를 원래 날짜로 설정
                            originalDepartureDate = currentDepartureDate;
                        }
                        
                        // 원래 기준 날짜에서 연도와 월 추출
                        const [originalYear, originalMonth, originalDay] = originalDepartureDate.split('-');
                        
                        // 새로운 날짜 생성 (원래 기준 날짜의 연도/월 사용)
                        const newDepartureDate = `${originalYear}-${originalMonth}-${day}`;
                        
                        // 새로운 URL 생성
                        urlParams.set('departureDate', newDepartureDate);
                        urlParams.set('originalDepartureDate', originalDepartureDate); // 원래 기준 날짜 유지
                        // returnDate는 원래 값을 그대로 유지 (변경하지 않음)
                        
                        // 페이지 새로고침으로 새로운 날짜로 검색
                        const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
                        window.location.href = newUrl;
                    }
                });
            });
            
            // 검색 조건 막대의 날짜 업데이트 함수
            function updateSearchDate(day, weekday) {
                const dateRangeSpan = document.querySelector('.date-range span');
                if (dateRangeSpan) {
                    // 현재 날짜 정보에서 월과 연도 추출
                    const currentDateText = dateRangeSpan.textContent;
                    const dateParts = currentDateText.split('~')[0].trim().split('.');
                    
                    if (dateParts.length >= 2) {
                        const year = dateParts[0];
                        const month = dateParts[1];
                        
                        // 변경된 날짜로 업데이트
                        const newDateText = `${year}.${month}.${day.padStart(2, '0')} ~ ${year}.${month}.${day.padStart(2, '0')}`;
                        dateRangeSpan.textContent = newDateText;
                        
                        // 상단 검색 요약의 날짜도 업데이트
                        const searchSummaryDate = document.querySelector('.search-summary .date');
                        if (searchSummaryDate) {
                            searchSummaryDate.textContent = `${year}년 ${month}월 ${day}일`;
                        }
                    }
                }
            }
        }
        
        // 좌석 등급 버튼 기능
        function initializeSeatClassButtons() {
            const seatClassBtns = document.querySelectorAll('.seat-class-btn');
            
            // 좌석 등급 버튼 클릭 이벤트
            seatClassBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    // 기존 선택 항목 제거
                    seatClassBtns.forEach(btn => btn.classList.remove('active'));
                    
                    // 현재 항목 선택
                    this.classList.add('active');
                    
                    // 좌석 유형 가져오기
                    const seatType = this.getAttribute('data-seat-type');
                    
                    console.log(`선택된 좌석 등급: ${seatType}`);
                    
                    // 좌석 등급에 따른 추가 로직 구현
                    updateSeatTypeDisplay(seatType);
                    
                    // 여기에 좌석 등급 변경에 따른 추가 로직 구현 가능
                    // 예: 선택한 좌석 등급에 대한 항공편 목록 필터링 등
                });
            });
            
            // 좌석 등급 표시 업데이트 함수
            function updateSeatTypeDisplay(seatType) {
                const seatTypeSpan = document.querySelector('.seat-type span');
                const seatTypeDropdown = document.querySelector('.seat-type-dropdown');
                
                if (seatTypeSpan) {
                    let seatTypeName = '일반석';
                    
                    if (seatType === 'prestige') {
                        seatTypeName = '프레스티지석';
                    } else if (seatType === 'first') {
                        seatTypeName = '일등석';
                    }
                    
                    // 상단 좌석 유형 텍스트 업데이트
                    seatTypeSpan.textContent = seatTypeName;
                    
                    // 드롭다운의 선택 상태도 업데이트
                    if (seatTypeDropdown) {
                        const seatOptions = seatTypeDropdown.querySelectorAll('.seat-option');
                        seatOptions.forEach(option => {
                            const optionType = option.getAttribute('data-type');
                            if (optionType === seatType) {
                                option.classList.add('selected');
                            } else {
                                option.classList.remove('selected');
                            }
                        });
                    }
                }
            }
        }
        
        // 필터 버튼 기능
        function initializeFilterButtons() {
            // 정렬 옵션 드롭다운
            initializeDropdown('.sort-dropdown', '.sort-btn', '.sort-options', 'sort-option');
            
            // 경유 옵션 드롭다운
            initializeDropdown('.stopover-dropdown', '.stopover-btn', '.stopover-options', 'stopover-option');
            
            // 드롭다운 초기화 함수
            function initializeDropdown(dropdownSelector, buttonSelector, optionsSelector, radioName) {
                const dropdown = document.querySelector(dropdownSelector);
                const button = dropdown.querySelector(buttonSelector);
                const dropdownContent = dropdown.querySelector(optionsSelector);
                const radioOptions = dropdown.querySelectorAll(`input[name="${radioName}"]`);
                
                // 버튼 클릭 이벤트
                button.addEventListener('click', function(e) {
                    e.stopPropagation();
                    
                    // 다른 모든 드롭다운 닫기
                    document.querySelectorAll('.dropdown-content').forEach(content => {
                        if (content !== dropdownContent) {
                            content.classList.remove('active');
                        }
                    });
                    
                    // 버튼 활성화 상태 토글
                    const isActive = dropdownContent.classList.contains('active');
                    button.classList.toggle('active', !isActive);
                    
                    // 드롭다운 토글
                    dropdownContent.classList.toggle('active');
                });
                
                // 라디오 옵션 클릭 이벤트
                radioOptions.forEach(radio => {
                    radio.addEventListener('change', function() {
                        // 선택된 옵션 텍스트 가져오기
                        const selectedLabel = this.parentNode.querySelector('.radio-label').textContent;
                        
                        // 버튼 텍스트 업데이트
                        if (radioName === 'sort-option') {
                            button.querySelector('.selected-option').textContent = selectedLabel + ' 정렬';
                        } else {
                            button.querySelector('.selected-option').textContent = selectedLabel;
                        }
                        
                        // 드롭다운 닫기
                        dropdownContent.classList.remove('active');
                        button.classList.remove('active');
                        
                        // 필터링 또는 정렬 기능 적용
                        applyFilterOrSort(radioName, this.value);
                    });
                });
            }
            
            // 필터링 또는 정렬 적용 함수
            function applyFilterOrSort(type, value) {
                console.log(`${type} 변경: ${value}`);
                
                // 여기에 실제 필터링 또는 정렬 로직 구현
                // 예: 항공편 목록을 정렬하거나 필터링
                const flightCards = document.querySelectorAll('.flight-card');
                
                if (type === 'sort-option') {
                    // 정렬 로직
                    switch (value) {
                        case 'recommended':
                            console.log('추천순으로 정렬');
                            break;
                        case 'departure-time':
                            console.log('출발 시간순으로 정렬');
                            break;
                        case 'arrival-time':
                            console.log('도착 시간순으로 정렬');
                            break;
                        case 'duration':
                            console.log('여행 시간순으로 정렬');
                            break;
                        case 'price':
                            console.log('최저 요금순으로 정렬');
                            break;
                    }
                } else if (type === 'stopover-option') {
                    // 경유 필터링 로직
                    switch (value) {
                        case 'all':
                            console.log('모든 항공편 표시');
                            flightCards.forEach(card => {
                                card.style.display = 'flex';
                            });
                            break;
                        case 'direct':
                            console.log('직항 항공편만 표시');
                            // 직항 필터링 예시 (실제 구현은 데이터에 따라 달라짐)
                            flightCards.forEach(card => {
                                const isDirect = !card.querySelector('.stopover-info');
                                card.style.display = isDirect ? 'flex' : 'none';
                            });
                            break;
                        case 'stopover':
                            console.log('경유 항공편만 표시');
                            // 경유 필터링 예시 (실제 구현은 데이터에 따라 달라짐)
                            flightCards.forEach(card => {
                                const isStopover = card.querySelector('.stopover-info');
                                card.style.display = isStopover ? 'flex' : 'none';
                            });
                            break;
                    }
                }
            }
            
            // 외부 클릭 시 모든 드롭다운 닫기
            document.addEventListener('click', function(e) {
                const isDropdown = e.target.closest('.filter-dropdown');
                
                if (!isDropdown) {
                    document.querySelectorAll('.dropdown-content').forEach(content => {
                        content.classList.remove('active');
                    });
                    
                    document.querySelectorAll('.filter-btn').forEach(btn => {
                        btn.classList.remove('active');
                    });
                }
            });
        }
    }

    // Initialize JSP-specific functionality if on search results page
    if (isSearchResults || currentPath.includes('search-results.jsp')) {
        // JSP specific functionality can be added here if needed
        console.log('Search results JSP page detected');
    }
}); 