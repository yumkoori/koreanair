<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // AuthenticationFilter에서 이미 인증을 확인했으므로 바로 사용자 정보 가져오기
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 대시보드</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
    <!-- 다음 우편번호 서비스 -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body class="airline-main-body">
    <jsp:include page="/views/common/header.jsp" />
    <main class="main-content">
    <div class="container dashboard-container">
        <h1>대시보드</h1>
        
        <%
            // 메시지 표시
            String message = (String) request.getAttribute("message");
            String error = (String) request.getAttribute("error");
            
            if (message != null) {
        %>
            <div class="success"><%= message %></div>
        <%
            }
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
        %>
        
        <div class="user-info">
            <h3>회원 정보</h3>
            <p><strong>아이디:</strong> <%= user.getUserId() %></p>
            <p><strong>한글 이름:</strong> <%= user.getKoreanName() %></p>
            <p><strong>영문 이름:</strong> <%= user.getEnglishName() %></p>
            <% if (user.getBirthDate() != null) { %>
                <p><strong>생년월일:</strong> <%= new SimpleDateFormat("yyyy년 MM월 dd일").format(user.getBirthDate()) %></p>
            <% } %>
            <p><strong>성별:</strong> <%= "M".equals(user.getGender()) ? "남성" : "여성" %></p>
            <p><strong>이메일:</strong> <%= user.getEmail() %></p>
            <p><strong>휴대폰:</strong> <%= user.getPhone() %></p>
            <% if (user.getAddress() != null && !user.getAddress().trim().isEmpty()) { %>
                <p><strong>주소:</strong> <%= user.getAddress() %></p>
            <% } %>
            <% if (user.getRegDate() != null) { %>
                <p><strong>가입일:</strong> <%= new SimpleDateFormat("yyyy년 MM월 dd일").format(user.getRegDate()) %></p>
            <% } %>
            
            <!-- 카카오 연동 정보 표시 -->
            <% if (user.getLoginType() != null) { %>
                <p><strong>로그인 타입:</strong> 
                    <% if ("kakao".equals(user.getLoginType())) { %>
                        <span style="color: #FEE500; font-weight: bold;">카카오 계정</span>
                    <% } else if ("both".equals(user.getLoginType())) { %>
                        <span style="color: #007bff; font-weight: bold;">일반 + 카카오 연동</span>
                    <% } else { %>
                        일반 계정
                    <% } %>
                </p>
            <% } %>
            
            <% if (user.getKakaoId() != null && user.getProfileImage() != null) { %>
                <p><strong>프로필 이미지:</strong></p>
                <img src="<%= user.getProfileImage() %>" alt="프로필 이미지" 
                     style="width: 80px; height: 80px; border-radius: 50%; margin: 10px 0;">
            <% } %>
            
            <!-- 회원 정보 수정 버튼 -->
            <div class="edit-buttons" style="margin-top: 20px;">
                <% if (!"kakao".equals(user.getLoginType())) { %>
                    <button type="button" class="btn btn-info" onclick="openEditProfileModal()">프로필 수정</button>
                    <button type="button" class="btn btn-warning" onclick="openChangePasswordModal()">비밀번호 변경</button>
                <% } else { %>
                    <p style="color: #666; font-size: 14px; margin-top: 10px;">
                        <i class="fas fa-info-circle"></i> 카카오 계정은 정보 수정이 제한됩니다.
                    </p>
                <% } %>
            </div>
        </div>
        
        <div class="actions">
            <a href="./" class="btn btn-primary">메인 페이지로</a>
            <a href="logout.do" class="btn btn-secondary">로그아웃</a>
        </div>
        
        <div class="delete-section">
            <h3>회원탈퇴</h3>
            <% if ("kakao".equals(user.getLoginType())) { %>
                <p>카카오 계정으로 가입하신 경우 확인 버튼만 클릭하시면 탈퇴됩니다.</p>
                <p><strong>주의:</strong> 탈퇴 후에는 모든 정보가 삭제되며 복구할 수 없습니다.</p>
                
                <form action="deleteAccount.do" method="post" onsubmit="return confirmKakaoDeleteAccount()">
                    <input type="hidden" name="loginType" value="kakao">
                    <button type="submit" class="btn btn-danger">회원탈퇴</button>
                </form>
            <% } else { %>
            <p>회원탈퇴를 원하시면 비밀번호를 입력하고 탈퇴 버튼을 클릭해주세요.</p>
            <p><strong>주의:</strong> 탈퇴 후에는 모든 정보가 삭제되며 복구할 수 없습니다.</p>
            
            <form action="deleteAccount.do" method="post" onsubmit="return confirmDeleteAccount()">
                <div class="form-group">
                    <label for="deletePassword">비밀번호 확인</label>
                    <input type="password" id="deletePassword" name="confirmPassword" required 
                           placeholder="현재 비밀번호를 입력하세요">
                </div>
                <button type="submit" class="btn btn-danger">회원탈퇴</button>
            </form>
            <% } %>
        </div>
    </div>
    </main>
    
    <!-- 프로필 수정 모달 -->
    <div id="editProfileModal" class="modal" style="display: none;">
        <div class="modal-content">
            <div class="modal-header">
                <h3>프로필 수정</h3>
                <span class="close" onclick="closeEditProfileModal()">&times;</span>
            </div>
            <form action="updateUser.do" method="post" onsubmit="return validateProfileForm()">
                <input type="hidden" name="updateType" value="profile">
                
                <div class="form-group">
                    <label for="editKoreanName">한글 이름 *</label>
                    <input type="text" id="editKoreanName" name="koreanName" value="<%= user.getKoreanName() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="editEnglishName">영문 이름 *</label>
                    <input type="text" id="editEnglishName" name="englishName" value="<%= user.getEnglishName() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="editBirthDate">생년월일</label>
                    <input type="date" id="editBirthDate" name="birthDate" 
                           value="<%= user.getBirthDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthDate()) : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="editGender">성별</label>
                    <select id="editGender" name="gender">
                        <option value="M" <%= "M".equals(user.getGender()) ? "selected" : "" %>>남성</option>
                        <option value="F" <%= "F".equals(user.getGender()) ? "selected" : "" %>>여성</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="editEmail">이메일 *</label>
                    <input type="email" id="editEmail" name="email" value="<%= user.getEmail() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="editPhone">휴대폰 *</label>
                    <input type="tel" id="editPhone" name="phone" value="<%= user.getPhone() %>" required 
                           placeholder="휴대폰 번호를 입력하세요 (예: 010-1234-5678)" 
                           oninput="formatPhoneNumber(this)">
                </div>
                
                <div class="form-group">
                    <label for="editAddress">자택 주소</label>
                    <div class="address-input-group">
                        <div class="address-search">
                            <input type="text" id="editPostcode" placeholder="우편번호" readonly>
                            <button type="button" class="btn-address-search" onclick="openEditPostcodeSearch()">
                                <i class="fas fa-search"></i> 주소 검색
                            </button>
                        </div>
                        <input type="text" id="editRoadAddress" placeholder="도로명주소" readonly>
                        <input type="text" id="editJibunAddress" placeholder="지번주소" readonly style="display: none;">
                        <input type="text" id="editDetailAddress" placeholder="상세주소를 입력하세요" 
                               oninput="updateEditFullAddress()">
                        <input type="hidden" id="editAddress" name="address" value="">
                    </div>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">수정하기</button>
                    <button type="button" class="btn btn-secondary" onclick="closeEditProfileModal()">취소</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 비밀번호 변경 모달 -->
    <div id="changePasswordModal" class="modal" style="display: none;">
        <div class="modal-content">
            <div class="modal-header">
                <h3>비밀번호 변경</h3>
                <span class="close" onclick="closeChangePasswordModal()">&times;</span>
            </div>
            <form action="updateUser.do" method="post" onsubmit="return validatePasswordForm()">
                <input type="hidden" name="updateType" value="password">
                
                <div class="form-group">
                    <label for="currentPassword">현재 비밀번호 *</label>
                    <input type="password" id="currentPassword" name="currentPassword" required>
                </div>
                
                <div class="form-group">
                    <label for="newPassword">새 비밀번호 *</label>
                    <input type="password" id="newPassword" name="newPassword" required minlength="4"
                           placeholder="4자 이상의 비밀번호를 입력하세요">
                    <small>비밀번호는 4자 이상이어야 합니다.</small>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">새 비밀번호 확인 *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required minlength="4"
                           placeholder="비밀번호를 다시 입력하세요">
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">변경하기</button>
                    <button type="button" class="btn btn-secondary" onclick="closeChangePasswordModal()">취소</button>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="/views/common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/index.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
    
    <script>
        // 프로필 수정 모달 열기
        function openEditProfileModal() {
            // 모달을 열기 전에 폼을 원래 값으로 초기화
            resetProfileForm();
            document.getElementById('editProfileModal').style.display = 'block';
        }
        
        // 프로필 수정 모달 닫기
        function closeEditProfileModal() {
            document.getElementById('editProfileModal').style.display = 'none';
            // 모달을 닫을 때도 폼 초기화
            resetProfileForm();
        }
        
        // 비밀번호 변경 모달 열기
        function openChangePasswordModal() {
            // 모달을 열기 전에 폼을 초기화
            resetPasswordForm();
            document.getElementById('changePasswordModal').style.display = 'block';
        }
        
        // 비밀번호 변경 모달 닫기
        function closeChangePasswordModal() {
            document.getElementById('changePasswordModal').style.display = 'none';
            // 모달을 닫을 때도 폼 초기화
            resetPasswordForm();
        }
        
        // 프로필 수정 폼 초기화 (원래 DB 값으로 복원)
        function resetProfileForm() {
            // 서버에서 전달된 원본 데이터로 복원
            document.getElementById('editKoreanName').value = '<%= user.getKoreanName() %>';
            document.getElementById('editEnglishName').value = '<%= user.getEnglishName() %>';
            document.getElementById('editBirthDate').value = '<%= user.getBirthDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthDate()) : "" %>';
            document.getElementById('editGender').value = '<%= user.getGender() != null ? user.getGender() : "" %>';
            document.getElementById('editEmail').value = '<%= user.getEmail() %>';
            document.getElementById('editPhone').value = '<%= user.getPhone() %>';
            
            // 주소 관련 필드 초기화
            const originalAddress = '<%= user.getAddress() != null ? user.getAddress() : "" %>';
            
            // 기존 주소를 파싱하여 각 필드에 분리 입력
            parseAndSetAddress(originalAddress);
        }
        
        // 기존 주소를 파싱하여 각 필드에 설정하는 함수
        function parseAndSetAddress(fullAddress) {
            // 주소 필드들 초기화
            document.getElementById('editPostcode').value = '';
            document.getElementById('editRoadAddress').value = '';
            document.getElementById('editJibunAddress').value = '';
            document.getElementById('editDetailAddress').value = '';
            document.getElementById('editAddress').value = fullAddress || '';
            
            if (!fullAddress || fullAddress.trim() === '') {
                return;
            }
            
            // 기존 주소 형식: (우편번호) 도로명주소 (참고항목) 상세주소
            const postcodeMatch = fullAddress.match(/^\((\d{5})\)/);
            if (postcodeMatch) {
                const postcode = postcodeMatch[1];
                document.getElementById('editPostcode').value = postcode;
                
                // 우편번호 부분을 제거한 나머지 주소
                const remainingAddress = fullAddress.replace(/^\(\d{5}\)\s*/, '');
                
                // 두 번째 괄호 ')' 위치 찾기
                const secondBracketIndex = remainingAddress.indexOf(')');
                
                if (secondBracketIndex !== -1) {
                    // 두 번째 괄호가 있는 경우: 괄호 앞까지는 도로명주소, 괄호 뒤는 상세주소
                    const roadAddressPart = remainingAddress.substring(0, secondBracketIndex + 1).trim();
                    const detailAddressPart = remainingAddress.substring(secondBracketIndex + 1).trim();
                    
                    document.getElementById('editRoadAddress').value = roadAddressPart;
                    if (detailAddressPart) {
                        document.getElementById('editDetailAddress').value = detailAddressPart;
                    }
                } else {
                    // 두 번째 괄호가 없는 경우: 전체를 도로명주소로 설정 (상세주소 분리 안함)
                    document.getElementById('editRoadAddress').value = remainingAddress;
                }
            } else {
                // 우편번호가 없는 경우 전체를 도로명주소로 설정
                document.getElementById('editRoadAddress').value = fullAddress;
            }
            
            // 파싱 완료 후 전체 주소 업데이트
            updateEditFullAddress();
        }
        
        // 비밀번호 변경 폼 초기화 (모든 필드 비우기)
        function resetPasswordForm() {
            document.getElementById('currentPassword').value = '';
            document.getElementById('newPassword').value = '';
            document.getElementById('confirmPassword').value = '';
        }
        
        // 프로필 수정용 우편번호 검색 함수
        function openEditPostcodeSearch() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
                    
                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수
                    
                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }
                    
                    // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    if(data.userSelectedType === 'R'){
                        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                            extraAddr += data.bname;
                        }
                        // 건물명이 있고, 공동주택일 경우 추가한다.
                        if(data.buildingName !== '' && data.apartment === 'Y'){
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                        if(extraAddr !== ''){
                            extraAddr = ' (' + extraAddr + ')';
                        }
                        // 조합된 참고항목을 해당 필드에 넣는다.
                        addr += extraAddr;
                    }
                    
                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('editPostcode').value = data.zonecode;
                    document.getElementById('editRoadAddress').value = addr;
                    document.getElementById('editJibunAddress').value = data.jibunAddress;
                    
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById('editDetailAddress').focus();
                    
                    // 전체 주소 업데이트 (DOM 업데이트 후 실행)
                    setTimeout(updateEditFullAddress, 0);
                }
            }).open();
        }
        
        // 프로필 수정용 전체 주소 업데이트 함수
        function updateEditFullAddress() {
            const postcode = document.getElementById('editPostcode').value || '';
            const roadAddress = document.getElementById('editRoadAddress').value || '';
            const detailAddress = document.getElementById('editDetailAddress').value || '';
            
            let fullAddress = '';
            
            if (postcode.trim() && roadAddress.trim()) {
                fullAddress = '(' + postcode.trim() + ') ' + roadAddress.trim();
                if (detailAddress.trim()) {
                    fullAddress += ' ' + detailAddress.trim();
                }
            } else if (roadAddress.trim()) {
                fullAddress = roadAddress.trim();
                if (detailAddress.trim()) {
                    fullAddress += ' ' + detailAddress.trim();
                }
            } else if (detailAddress.trim()) {
                fullAddress = detailAddress.trim();
            }
            
            document.getElementById('editAddress').value = fullAddress;
        }
        
        // 프로필 수정 폼 검증 (회원가입 유효성 검사 재사용)
        function validateProfileForm() {
            const koreanName = document.getElementById('editKoreanName').value.trim();
            const englishName = document.getElementById('editEnglishName').value.trim();
            const birthDate = document.getElementById('editBirthDate').value;
            const gender = document.getElementById('editGender').value;
            const email = document.getElementById('editEmail').value.trim();
            const phone = document.getElementById('editPhone').value.trim();
            
            // 필수 필드 체크
            const requiredFields = [
                { value: koreanName, focus: () => document.getElementById('editKoreanName').focus(), name: '한글 이름' },
                { value: englishName, focus: () => document.getElementById('editEnglishName').focus(), name: '영문 이름' },
                { value: email, focus: () => document.getElementById('editEmail').focus(), name: '이메일' },
                { value: phone, focus: () => document.getElementById('editPhone').focus(), name: '휴대폰 번호' }
            ];
            
            for (let field of requiredFields) {
                if (!field.value || field.value.trim() === '') {
                    alert(field.name + '은(는) 필수 입력 항목입니다.');
                    field.focus();
                    return false;
                }
            }
            
            // 한글 이름 검증 (login.js의 validateKoreanName 함수 사용)
            if (!validateKoreanName(koreanName)) {
                document.getElementById('editKoreanName').focus();
                return false;
            }
            
            // 영문 이름 검증 (login.js의 validateEnglishName 함수 사용)
            if (!validateEnglishName(englishName)) {
                document.getElementById('editEnglishName').focus();
                return false;
            }
            
            // 생년월일 검증 (선택사항이지만 입력된 경우 검증)
            if (birthDate && !validateBirthDate(birthDate)) {
                document.getElementById('editBirthDate').focus();
                return false;
            }
            
            // 이메일 형식 검증 (login.js의 validateEmail 함수 사용)
            if (!validateEmail(email)) {
                document.getElementById('editEmail').focus();
                return false;
            }
            
            // 휴대폰 번호 형식 검증 (login.js의 validatePhoneNumber 함수 사용)
            if (!validatePhoneNumber(phone)) {
                document.getElementById('editPhone').focus();
                return false;
            }
            
            return confirm('회원 정보를 수정하시겠습니까?');
        }
        
        // 비밀번호 변경 폼 검증 (회원가입 유효성 검사 재사용)
        function validatePasswordForm() {
            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            // 필수 필드 체크
            const requiredFields = [
                { value: currentPassword, focus: () => document.getElementById('currentPassword').focus(), name: '현재 비밀번호' },
                { value: newPassword, focus: () => document.getElementById('newPassword').focus(), name: '새 비밀번호' },
                { value: confirmPassword, focus: () => document.getElementById('confirmPassword').focus(), name: '새 비밀번호 확인' }
            ];
            
            for (let field of requiredFields) {
                if (!field.value || field.value.trim() === '') {
                    alert(field.name + '을(를) 입력해주세요.');
                    field.focus();
                    return false;
                }
            }
            
            // 새 비밀번호 길이 검증 (login.js의 validatePassword 함수 사용)
            if (!validatePassword(newPassword)) {
                document.getElementById('newPassword').focus();
                return false;
            }
            
            // 비밀번호 확인 검증 (login.js의 validatePasswordConfirm 함수 사용)
            if (!validatePasswordConfirm(newPassword, confirmPassword)) {
                document.getElementById('confirmPassword').focus();
                return false;
            }
            
            // 현재 비밀번호와 새 비밀번호 동일성 체크
            if (currentPassword === newPassword) {
                alert('현재 비밀번호와 새 비밀번호가 같습니다. 다른 비밀번호를 입력해주세요.');
                document.getElementById('newPassword').focus();
                return false;
            }
            
            return confirm('비밀번호를 변경하시겠습니까?');
        }
        
        // 모달 외부 클릭 시 닫기
        window.onclick = function(event) {
            const profileModal = document.getElementById('editProfileModal');
            const passwordModal = document.getElementById('changePasswordModal');
            
            if (event.target === profileModal) {
                closeEditProfileModal(); // 이미 초기화 로직 포함
            }
            if (event.target === passwordModal) {
                closeChangePasswordModal(); // 이미 초기화 로직 포함
            }
        }
        
        // ESC 키로 모달 닫기
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                const profileModal = document.getElementById('editProfileModal');
                const passwordModal = document.getElementById('changePasswordModal');
                
                // 열린 모달만 닫기 (display가 'block'인 경우)
                if (profileModal.style.display === 'block') {
                    closeEditProfileModal(); // 이미 초기화 로직 포함
                }
                if (passwordModal.style.display === 'block') {
                    closeChangePasswordModal(); // 이미 초기화 로직 포함
                }
            }
        });
    </script>
</body>
</html> 