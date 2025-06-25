/**
 * 
 */

/**
 * Index6.jsp 사용자 검색 관련 함수들
 */

// 사용자 검색 함수
function searchUsers() {
    console.log('searchUsers 함수 호출됨');
    
    const searchInput = document.getElementById('userSearchInput');
    console.log('검색 입력창 요소:', searchInput);
    
    if (!searchInput) {
        console.error('검색 입력창을 찾을 수 없습니다.');
        alert('검색 입력창을 찾을 수 없습니다.');
        return;
    }
    
    const searchValue = searchInput.value.trim();
    console.log('입력된 검색값:', searchValue);
    
    if (!searchValue) {
        alert('검색창에 검색할 사용자 이름을 입력해주세요.');
        return;
    }
    
    // 검색 파라미터 설정
    const params = new URLSearchParams({
        userName: searchValue,
        page: 1,
        size: 10
    });
    
    const requestUrl = `${contextPath}/searchusers.wi?${params}`;
    console.log('요청 URL:', requestUrl);
    console.log('파라미터:', params.toString());
    
    // GET 요청으로 사용자 검색
    fetch(requestUrl, {
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
        // 검색 성공 시 결과 처리
        handleSearchResults(data);
    })
    .catch(error => {
        console.error('검색 오류:', error);
        alert('사용자 검색 중 오류가 발생했습니다.');
    });
}

// 검색 결과 처리 함수
function handleSearchResults(data) {
    console.log('검색 결과 전체 데이터:', data);
    console.log('데이터 타입:', typeof data);
    console.log('데이터가 배열인가:', Array.isArray(data));
    
    const resultTable = document.getElementById('userSearchResultTable');
    const resultCount = document.getElementById('resultCount');
    
    // 데이터 구조 확인 - 배열이 직접 오는지, 객체로 감싸져 있는지 확인
    let userList = [];
    if (Array.isArray(data)) {
        userList = data;
        console.log('직접 배열 형태의 데이터');
    } else if (data.data && Array.isArray(data.data)) {
        userList = data.data;
        console.log('data.data 형태의 데이터');
    } else if (data.users && Array.isArray(data.users)) {
        userList = data.users;
        console.log('data.users 형태의 데이터');
    } else if (data.userList && Array.isArray(data.userList)) {
        userList = data.userList;
        console.log('data.userList 형태의 데이터');
    }
    
    console.log('최종 userList:', userList);
    console.log('userList 길이:', userList.length);
    
    // 검색 결과 개수 업데이트
    if (resultCount) {
        resultCount.textContent = data.totalCount || userList.length || 0;
    }
    
    // 테이블 동적 생성
    if (resultTable && userList && userList.length > 0) {
        console.log('테이블 생성 시작');
        createSearchResultTable(userList, resultTable);
    } else if (resultTable) {
        console.log('검색 결과 없음 - 빈 테이블 표시');
        // 검색 결과가 없을 때
        resultTable.innerHTML = '<tr><td colspan="13" class="text-center">검색 결과가 없습니다.</td></tr>';
    } else {
        console.error('resultTable 요소를 찾을 수 없습니다.');
    }
}

// 검색 결과 테이블 생성 함수
function createSearchResultTable(users, tableElement) {
    console.log('createSearchResultTable 호출됨');
    console.log('users 데이터:', users);
    console.log('첫 번째 사용자 데이터:', users[0]);
    
    // 테이블 헤더 생성
    let tableHTML = `
        <thead>
            <tr>
                <th>번호</th>
                <th>회원번호</th>
                <th>등급</th>
                <th>사용자 ID</th>
                <th>한글명</th>
                <th>영문명</th>
                <th>이메일</th>
                <th>생년월일</th>
                <th>성별</th>
                <th>주소</th>
                <th>전화번호</th>
                <th>가입일</th>
                <th>상태</th>
            </tr>
        </thead>
        <tbody>
    `;
    
    // 테이블 바디 생성
    users.forEach((user, index) => {
        tableHTML += `
            <tr>
                <td>${index + 1}</td>
                <td>${user.user_no || '-'}</td>
                <td>
                    <span class="badge badge-info">${user.grade || '-'}</span>
                </td>
                <td>${user.user_id || '-'}</td>
                <td>${user.ko_name || '-'}</td>
                <td>${user.en_name || '-'}</td>
                <td>${user.email || '-'}</td>
                <td>${user.birth_date || '-'}</td>
                <td>
                    <span class="badge ${user.gender === 'M' ? 'badge-primary' : 'badge-warning'}">
                        ${user.gender === 'M' ? '남성' : user.gender === 'F' ? '여성' : '-'}
                    </span>
                </td>
                <td>${user.address || '-'}</td>
                <td>${user.phone_number || '-'}</td>
                <td>${user.created_at || '-'}</td>
                <td>
                    <span class="badge ${user.status === 'ACTIVE' ? 'badge-success' : 'badge-secondary'}">
                        ${user.status === 'ACTIVE' ? '활성' : '비활성'}
                    </span>
                </td>
            </tr>
        `;
    });
    
    tableHTML += '</tbody>';
    console.log('생성된 테이블 HTML:', tableHTML);
    tableElement.innerHTML = tableHTML;
    console.log('테이블 생성 완료');
}



// 검색 초기화 함수
function clearSearch() {
    const searchInput = document.getElementById('userSearchInput');
    const resultTable = document.getElementById('userSearchResultTable');
    const resultCount = document.getElementById('resultCount');
    
    // 검색창 초기화
    if (searchInput) {
        searchInput.value = '';
    }
    
    // 결과 개수 초기화
    if (resultCount) {
        resultCount.textContent = '0';
    }
    
    // 테이블 초기화
    if (resultTable) {
        resultTable.innerHTML = '';
    }
}