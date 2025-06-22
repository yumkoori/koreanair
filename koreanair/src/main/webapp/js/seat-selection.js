// 좌석 카드 클릭 기능 (JSP 전용)
document.addEventListener('DOMContentLoaded', function() {
    console.log('좌석 선택 JavaScript 로드됨');
    
    // 좌석 등급 코드 매핑 함수
    function getSeatClassCode(fareType) {
        if (fareType === '일반석') {
            return 'ECONOMY';
        } else if (fareType === '프레스티지석') {
            return 'PRE';
        } else if (fareType === '일등석') {
            return 'FIR';
        }
        return 'null'; // 기본값
    }
    
    // 복항편 페이지에서 가는 편 정보 표시
    const urlParams = new URLSearchParams(window.location.search);
    const isReturnLeg = urlParams.get('leg') === 'return';
    
    if (isReturnLeg) {
        displayOutboundFlightInfo();
    }
    
    const totalAmountDisplay = document.querySelector('.total-amount');
    const clickableFareCards = document.querySelectorAll('.clickable-fare');
    
    console.log('총액 표시 요소:', totalAmountDisplay);
    console.log('클릭 가능한 좌석 카드 수:', clickableFareCards.length);
    
    if (clickableFareCards.length === 0) {
        console.log('클릭 가능한 좌석 카드를 찾을 수 없습니다.');
        return;
    }
    
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
            return;
        }
        
        card.addEventListener('click', function(e) {
            console.log('좌석 카드 클릭됨:', this.getAttribute('data-fare-type'));
            
            // 기존 팝업 표시 방지
            e.preventDefault();
            e.stopPropagation();
            
            // 기존 선택 해제
            clickableFareCards.forEach(otherCard => {
                otherCard.classList.remove('selected');
            });
            
            // 현재 카드 선택
            this.classList.add('selected');
            
            // 가격 정보 가져오기
            const priceElement = this.querySelector('.fare-price[data-price]');
            console.log('가격 요소:', priceElement);
            
            if (priceElement && totalAmountDisplay) {
                const price = priceElement.getAttribute('data-price');
                const fareType = this.getAttribute('data-fare-type');
                const flightId = this.getAttribute('data-flight-id');
                
                console.log('가격 정보:', { price, fareType, flightId });
                
                // 승객 수 가져오기 (getPassengerCount 함수 사용)
                const passengerCount = (typeof getPassengerCount === 'function') ? getPassengerCount() : 
                                     (typeof window.passengerCount !== 'undefined' && window.passengerCount > 0) ? window.passengerCount : 1;
                
                console.log('🧮 === seat-selection.js 가격 계산 ===');
                console.log('💵 개별 가격:', price);
                console.log('👥 승객 수:', passengerCount);
                
                // 총 가격 계산 (개별 가격 × 승객 수)
                const individualPrice = parseInt(price);
                const totalPrice = individualPrice * passengerCount;
                
                console.log('🧮 계산식:', individualPrice, ' × ', passengerCount, ' = ', totalPrice);
                
                // 총액 업데이트 (천 단위 콤마 추가)
                const formattedPrice = totalPrice.toLocaleString('ko-KR');
                totalAmountDisplay.textContent = formattedPrice + '원';
                
                console.log(`✅ 선택된 좌석: ${fareType}, 항공편: ${flightId}, 개별가격: ${individualPrice.toLocaleString('ko-KR')}원, 승객수: ${passengerCount}명, 총가격: ${formattedPrice}원`);
                console.log('총액 업데이트 완료:', totalAmountDisplay.textContent);
            } else {
                console.log('가격 요소 또는 총액 표시 요소를 찾을 수 없음');
            }
        });
    });

    // 다음 여정 버튼 처리 (왕복 여행용)
    const nextBtn = document.querySelector('.next-btn');
    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            const currentTotal = totalAmountDisplay ? totalAmountDisplay.textContent : '0원';
            
            if (currentTotal === '0원') {
                alert('항공권을 먼저 선택해주세요.');
                return;
            }

            // 현재 URL 파라미터 가져오기
            const urlParams = new URLSearchParams(window.location.search);
            const tripType = urlParams.get('tripType');
            
                         // 왕복 여행인 경우에만 반대 방향 검색 수행
             if (tripType === 'round' && !isReturnLeg) {
                const departure = urlParams.get('departure');
                const arrival = urlParams.get('arrival');
                const departureDate = urlParams.get('returnDate'); // 복항날짜를 출발날짜로
                const returnDate = urlParams.get('departureDate'); // 출발날짜를 복항날짜로
                const passengers = urlParams.get('passengers') || '성인 1명';
                const seatClass = urlParams.get('seatClass') || 'economy';
                
                // 선택된 항공편 정보 저장 (세션 스토리지 사용)
                const selectedCard = document.querySelector('.clickable-fare.selected');
                if (selectedCard) {
                    const individualPrice = selectedCard.querySelector('.fare-price[data-price]').getAttribute('data-price');
                    
                    // 승객 수 가져오기
                    const passengerCount = (typeof getPassengerCount === 'function') ? getPassengerCount() : 
                                         (typeof window.passengerCount !== 'undefined' && window.passengerCount > 0) ? window.passengerCount : 1;
                    
                    // 총 가격 계산 (개별 가격 × 승객 수)
                    const totalPrice = parseInt(individualPrice) * passengerCount;
                    
                    console.log('🎫 가는 편 가격 계산:', {
                        개별가격: individualPrice,
                        승객수: passengerCount,
                        총가격: totalPrice
                    });
                    
                    const selectedFlight = {
                        flightId: selectedCard.getAttribute('data-flight-id'),
                        fareType: selectedCard.getAttribute('data-fare-type'),
                        seatClass: getSeatClassCode(selectedCard.getAttribute('data-fare-type')),
                        individualPrice: individualPrice,  // 개별 가격 (원본)
                        price: totalPrice.toString(),      // 총 가격 (승객 수 × 개별 가격)
                        passengerCount: passengerCount,
                        departure: departure,
                        arrival: arrival,
                        departureDate: urlParams.get('departureDate'),
                        direction: 'outbound' // 가는 편
                    };
                    
                    sessionStorage.setItem('selectedOutboundFlight', JSON.stringify(selectedFlight));
                    console.log('✅ 가는 편 항공편 저장 (승객 수 반영):', selectedFlight);
                }
                
                // 반대 방향으로 검색 URL 생성 (출발지와 도착지 바뀜)
                const newSearchParams = new URLSearchParams({
                    departure: arrival,        // 기존 도착지가 새로운 출발지
                    arrival: departure,        // 기존 출발지가 새로운 도착지
                    departureDate: departureDate, // 기존 복항날짜가 새로운 출발날짜
                    returnDate: returnDate,       // 기존 출발날짜가 새로운 복항날짜
                    passengers: passengers,
                    seatClass: seatClass,
                    tripType: tripType,
                    leg: 'return' // 복항편임을 표시
                });
                
                const newUrl = 'flightSearch.do?' + newSearchParams.toString();
                console.log('복항편 검색 URL:', newUrl);
                
                                 // 새로운 검색 페이지로 이동
                 window.location.href = newUrl;
             } else if (tripType === 'round' && isReturnLeg) {
                 // 복항편 선택 완료 - 예약 페이지로 이동
                 const selectedCard = document.querySelector('.clickable-fare.selected');
                 if (selectedCard) {
                     const individualPrice = selectedCard.querySelector('.fare-price[data-price]').getAttribute('data-price');
                     
                     // 승객 수 가져오기
                     const passengerCount = (typeof getPassengerCount === 'function') ? getPassengerCount() : 
                                          (typeof window.passengerCount !== 'undefined' && window.passengerCount > 0) ? window.passengerCount : 1;
                     
                     // 총 가격 계산 (개별 가격 × 승객 수)
                     const totalPrice = parseInt(individualPrice) * passengerCount;
                     
                     console.log('🎫 복항편 가격 계산:', {
                         개별가격: individualPrice,
                         승객수: passengerCount,
                         총가격: totalPrice
                     });
                     
                     const returnFlight = {
                         flightId: selectedCard.getAttribute('data-flight-id'),
                         fareType: selectedCard.getAttribute('data-fare-type'),
                         seatClass: getSeatClassCode(selectedCard.getAttribute('data-fare-type')),
                         individualPrice: individualPrice,  // 개별 가격 (원본)
                         price: totalPrice.toString(),      // 총 가격 (승객 수 × 개별 가격)
                         passengerCount: passengerCount,
                         departure: urlParams.get('departure'),
                         arrival: urlParams.get('arrival'),
                         departureDate: urlParams.get('departureDate'),
                         direction: 'return' // 복항편
                     };
                     
                     sessionStorage.setItem('selectedReturnFlight', JSON.stringify(returnFlight));
                     console.log('✅ 복항편 항공편 저장 (승객 수 반영):', returnFlight);
                     
                     // 총 예약 정보 확인 및 booking.jsp로 이동
                     const outboundFlightStr = sessionStorage.getItem('selectedOutboundFlight');
                     if (outboundFlightStr) {
                         const outboundFlight = JSON.parse(outboundFlightStr);
                         const totalPrice = parseInt(outboundFlight.price) + parseInt(returnFlight.price);
                         
                         // booking.do로 이동하는 URL 생성
                         const bookingParams = new URLSearchParams({
                             outboundFlightId: outboundFlight.flightId,
                             returnFlightId: returnFlight.flightId,
                             outboundFareType: outboundFlight.fareType,
                             returnFareType: returnFlight.fareType,
                             outboundSeatClass: outboundFlight.seatClass || getSeatClassCode(outboundFlight.fareType),
                             returnSeatClass: returnFlight.seatClass || getSeatClassCode(returnFlight.fareType),
                             outboundPrice: outboundFlight.price,
                             returnPrice: returnFlight.price,
                             totalPrice: totalPrice,
                             tripType: 'round',
                             departure: outboundFlight.departure,
                             arrival: outboundFlight.arrival,
                             departureDate: outboundFlight.departureDate,
                             returnDate: returnFlight.departureDate,
                             passengers: urlParams.get('passengers') || '성인 1명'
                         });
                         
                         // contextPath 가져오기 (여러 방법 시도)
                         const contextPath = window.contextPath || 
                                           (window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '') ||
                                           '';
                         const bookingUrl = contextPath + '/booking.do?' + bookingParams.toString();
                         console.log('🎯 === 왕복 예약 페이지 이동 ===');
                         console.log('contextPath:', contextPath);
                         console.log('가는 편 총가격:', parseInt(outboundFlight.price).toLocaleString('ko-KR'), '원');
                         console.log('복항편 총가격:', parseInt(returnFlight.price).toLocaleString('ko-KR'), '원');
                         console.log('전체 총가격:', totalPrice.toLocaleString('ko-KR'), '원');
                         console.log('🔍 좌석 클래스 디버깅:');
                         console.log('- outboundFlight.fareType:', outboundFlight.fareType);
                         console.log('- returnFlight.fareType:', returnFlight.fareType);
                         console.log('- outboundFlight.seatClass:', outboundFlight.seatClass);
                         console.log('- returnFlight.seatClass:', returnFlight.seatClass);
                         console.log('- getSeatClassCode(outboundFlight.fareType):', getSeatClassCode(outboundFlight.fareType));
                         console.log('- getSeatClassCode(returnFlight.fareType):', getSeatClassCode(returnFlight.fareType));
                         console.log('최종 booking URL:', bookingUrl);
                         console.log('✈️ 예약 페이지로 이동합니다...');
                         window.location.href = bookingUrl;
                     }
                 }
             } else {
                 // 편도 여행이거나 다른 경우 - 예약 페이지로 이동
                 const selectedCard = document.querySelector('.clickable-fare.selected');
                 if (selectedCard) {
                     const flightId = selectedCard.getAttribute('data-flight-id');
                     const fareType = selectedCard.getAttribute('data-fare-type');
                     const individualPrice = selectedCard.querySelector('.fare-price[data-price]').getAttribute('data-price');
                     
                     // 승객 수 가져오기
                     const passengerCount = (typeof getPassengerCount === 'function') ? getPassengerCount() : 
                                          (typeof window.passengerCount !== 'undefined' && window.passengerCount > 0) ? window.passengerCount : 1;
                     
                     // 총 가격 계산 (개별 가격 × 승객 수)
                     const totalPrice = parseInt(individualPrice) * passengerCount;
                     
                     console.log('🎫 편도 가격 계산:', {
                         개별가격: individualPrice,
                         승객수: passengerCount,
                         총가격: totalPrice
                     });
                     
                     // booking.do로 이동하는 URL 생성
                     const bookingParams = new URLSearchParams({
                         flightId: flightId,
                         fareType: fareType,
                         seatClass: getSeatClassCode(fareType),
                         individualPrice: individualPrice,  // 개별 가격 (원본)
                         totalPrice: totalPrice,            // 총 가격 (승객 수 반영)
                         passengerCount: passengerCount,
                         tripType: 'oneway',
                         departure: urlParams.get('departure'),
                         arrival: urlParams.get('arrival'),
                         departureDate: urlParams.get('departureDate'),
                         passengers: urlParams.get('passengers') || '성인 1명'
                     });
                     
                     // contextPath 가져오기 (여러 방법 시도)
                     const contextPath = window.contextPath || 
                                       (window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '') ||
                                       '';
                     const bookingUrl = contextPath + '/booking.do?' + bookingParams.toString();
                     console.log('🎯 === 편도 예약 페이지 이동 ===');
                     console.log('contextPath:', contextPath);
                     console.log('개별 가격:', parseInt(individualPrice).toLocaleString('ko-KR'), '원');
                     console.log('승객 수:', passengerCount, '명');
                     console.log('총 가격:', totalPrice.toLocaleString('ko-KR'), '원');
                     console.log('🔍 좌석 클래스 디버깅:');
                     console.log('- fareType:', fareType);
                     console.log('- getSeatClassCode(fareType):', getSeatClassCode(fareType));
                     console.log('최종 booking URL:', bookingUrl);
                     console.log('✈️ 예약 페이지로 이동합니다...');
                     window.location.href = bookingUrl;
                 } else {
                     alert('항공권을 먼저 선택해주세요.');
                 }
             }
                 });
     }
     
     // 가는 편 정보 표시 함수
     function displayOutboundFlightInfo() {
         const outboundFlightStr = sessionStorage.getItem('selectedOutboundFlight');
         if (outboundFlightStr) {
             const outboundFlight = JSON.parse(outboundFlightStr);
             const outboundDetailsElement = document.getElementById('outbound-details');
             
             if (outboundDetailsElement) {
                 const formattedPrice = parseInt(outboundFlight.price).toLocaleString('ko-KR');
                 const passengerInfo = outboundFlight.passengerCount ? `(${outboundFlight.passengerCount}명)` : '';
                 const individualPriceInfo = outboundFlight.individualPrice ? 
                     ` - 개별: ₩${parseInt(outboundFlight.individualPrice).toLocaleString('ko-KR')}` : '';
                 
                 outboundDetailsElement.innerHTML = `
                     <div style="display: flex; justify-content: space-between; align-items: center;">
                         <div>
                             <span style="font-weight: 500;">${outboundFlight.departure} → ${outboundFlight.arrival}</span>
                             <span style="margin-left: 10px; color: #888;">${outboundFlight.departureDate}</span>
                             <span style="margin-left: 10px; background-color: #e3f2fd; color: #1976d2; padding: 2px 6px; border-radius: 3px; font-size: 12px;">${outboundFlight.fareType}</span>
                             ${passengerInfo ? `<span style="margin-left: 10px; background-color: #f3e5f5; color: #7b1fa2; padding: 2px 6px; border-radius: 3px; font-size: 12px;">${passengerInfo}</span>` : ''}
                         </div>
                         <div style="font-weight: bold; color: #0064de;">
                             ₩${formattedPrice}
                         </div>
                     </div>
                     <div style="font-size: 12px; color: #999; margin-top: 2px;">
                         항공편: ${outboundFlight.flightId}${individualPriceInfo}
                     </div>
                 `;
                 console.log('✅ 가는 편 정보 표시됨 (승객 수 반영):', outboundFlight);
             }
         } else {
             console.log('저장된 가는 편 정보가 없습니다.');
         }
     }
}); 