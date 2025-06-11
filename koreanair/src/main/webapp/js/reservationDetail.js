document.addEventListener('DOMContentLoaded', function() {
    // 여정 카드 헤더를 클릭했을 때 최소화/확대 기능을 추가합니다.
    const journeyCard = document.querySelector('.journey-card-outer');
    const journeyHeader = document.querySelector('.journey-card-header');

    if (journeyHeader && journeyCard) {
        journeyHeader.addEventListener('click', function() {
            // is-collapsed 라는 클래스를 추가하거나 제거하여 상태를 변경합니다.
            journeyCard.classList.toggle('is-collapsed');
        });
    }

    // 탑승객 정보, 여행 변경 섹션의 토글 기능도 추가합니다.
    const sectionHeaders = document.querySelectorAll('.section-card .section-title');
    sectionHeaders.forEach(header => {
        header.addEventListener('click', function(e) {
            // 제목 영역의 어느 곳을 클릭해도 동작하도록 합니다.
            if(e.target.closest('.section-title')) {
                const sectionCard = header.closest('.section-card');
                if(sectionCard) {
                    sectionCard.classList.toggle('is-collapsed');
                }
            }
        });
    });
});