<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Gentelella Alela! | 정보 검색</title> {/* 페이지 타이틀 수정 */}

    <link href="../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <link href="../vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">
    <link href="../vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">
    
    <link href="../build/css/custom.min.css" rel="stylesheet">

    <style>
      /* 검색 결과 표시를 위한 간단한 스타일 (필요에 따라 수정) */
      #searchResultsContainer .result-item {
        border: 1px solid #ddd;
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 4px;
        background-color: #f9f9f9;
      }
      #searchResultsContainer .result-item h5 {
        margin-top: 0;
        color: #1ABB9C; /* Gentelella 테마 색상 */
      }
      #searchLoadingIndicator { /* ID를 searchByNameLoadingIndicator로 변경했으므로, 해당 ID로 스타일 적용 */
        display: none; /* 기본적으로 숨김 */
        text-align: center;
        padding: 20px;
      }
      #searchLoadingIndicator .fa-spinner { /* ID를 searchByNameLoadingIndicator로 변경했으므로, 해당 ID로 스타일 적용 */
        font-size: 2em;
        color: #1ABB9C;
      }
    </style>
  </head>

  <body class="nav-md">
    <div class="container body">
      <div class="main_container">
        <div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="index.html" class="site_title"><i class="fa fa-paw"></i> <span>Gentelella Alela!</span></a>
            </div>

            <div class="clearfix"></div>

            <div class="profile clearfix">
              <div class="profile_pic">
                <img src="images/img.jpg" alt="..." class="img-circle profile_img">
              </div>
              <div class="profile_info">
                <span>Welcome,</span>
                <h2>John Doe</h2>
              </div>
            </div>
            <br />

            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
                <h3>General</h3>
                <ul class="nav side-menu">
                  <li><a><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="index.html">Dashboard</a></li>
                      <li><a href="index2.html">Dashboard2</a></li>
                      <li><a href="index3.html">Dashboard3</a></li>
                      <li><a href="index4.html">좌석관리</a></li>
                      <li><a href="index5.html">비행스케줄표</a></li>
                      <li><a href="index6.html">정보 검색</a></li> {/* 새 페이지 링크 추가 */}
                    </ul>
                  </li>
                  <li><a><i class="fa fa-edit"></i> Forms <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="form.html">General Form</a></li>
                      <li><a href="form_advanced.html">Advanced Components</a></li>
                      <li><a href="form_validation.html">Form Validation</a></li>
                      <li><a href="form_wizards.html">Form Wizard</a></li>
                      <li><a href="form_upload.html">Form Upload</a></li>
                      <li><a href="form_buttons.html">Form Buttons</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-desktop"></i> UI Elements <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="general_elements.html">General Elements</a></li>
                      <li><a href="media_gallery.html">Media Gallery</a></li>
                      <li><a href="typography.html">Typography</a></li>
                      <li><a href="icons.html">Icons</a></li>
                      <li><a href="glyphicons.html">Glyphicons</a></li>
                      <li><a href="widgets.html">Widgets</a></li>
                      <li><a href="invoice.html">Invoice</a></li>
                      <li><a href="inbox.html">Inbox</a></li>
                      <li><a href="calendar.html">Calendar</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-table"></i> Tables <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="tables.html">Tables</a></li>
                      <li><a href="tables_dynamic.html">Table Dynamic</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-bar-chart-o"></i> Data Presentation <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="chartjs.html">Chart JS</a></li>
                      <li><a href="chartjs2.html">Chart JS2</a></li>
                      <li><a href="morisjs.html">Moris JS</a></li>
                      <li><a href="echarts.html">ECharts</a></li>
                      <li><a href="other_charts.html">Other Charts</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-clone"></i>Layouts <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="fixed_sidebar.html">Fixed Sidebar</a></li>
                      <li><a href="fixed_footer.html">Fixed Footer</a></li>
                    </ul>
                  </li>
                </ul>
              </div>
              <div class="menu_section">
                <h3>Live On</h3>
                <ul class="nav side-menu">
                  <li><a><i class="fa fa-bug"></i> Additional Pages <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="e_commerce.html">E-commerce</a></li>
                      <li><a href="projects.html">Projects</a></li>
                      <li><a href="project_detail.html">Project Detail</a></li>
                      <li><a href="contacts.html">Contacts</a></li>
                      <li><a href="profile.html">Profile</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-windows"></i> Extras <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="page_403.html">403 Error</a></li>
                      <li><a href="page_404.html">404 Error</a></li>
                      <li><a href="page_500.html">500 Error</a></li>
                      <li><a href="plain_page.html">Plain Page</a></li>
                      <li><a href="login.html">Login Page</a></li>
                      <li><a href="pricing_tables.html">Pricing Tables</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-sitemap"></i> Multilevel Menu <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                        <li><a href="#level1_1">Level One</a>
                        <li><a>Level One<span class="fa fa-chevron-down"></span></a>
                          <ul class="nav child_menu">
                            <li class="sub_menu"><a href="level2.html">Level Two</a></li>
                            <li><a href="#level2_1">Level Two</a></li>
                            <li><a href="#level2_2">Level Two</a></li>
                          </ul>
                        </li>
                        <li><a href="#level1_2">Level One</a></li>
                    </ul>
                  </li>                  
                  <li><a href="javascript:void(0)"><i class="fa fa-laptop"></i> Landing Page <span class="label label-success pull-right">Coming Soon</span></a></li>
                </ul>
              </div>
            </div>
            <div class="sidebar-footer hidden-small">
              <a data-toggle="tooltip" data-placement="top" title="Settings">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Lock">
                <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Logout" href="login.html">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div>
            </div>
        </div>

        <div class="top_nav">
          <div class="nav_menu">
              <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
              </div>
              <nav class="nav navbar-nav">
              <ul class=" navbar-right">
                <li class="nav-item dropdown open" style="padding-left: 15px;">
                  <a href="javascript:;" class="user-profile dropdown-toggle" aria-haspopup="true" id="navbarDropdown" data-toggle="dropdown" aria-expanded="false">
                    <img src="images/img.jpg" alt="">John Doe
                  </a>
                  <div class="dropdown-menu dropdown-usermenu pull-right" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item"  href="javascript:;"> Profile</a>
                      <a class="dropdown-item"  href="javascript:;">
                        <span class="badge bg-red pull-right">50%</span>
                        <span>Settings</span>
                      </a>
                  <a class="dropdown-item"  href="javascript:;">Help</a>
                    <a class="dropdown-item"  href="login.html"><i class="fa fa-sign-out pull-right"></i> Log Out</a>
                  </div>
                </li>

                <li role="presentation" class="nav-item dropdown open">
                  <a href="javascript:;" class="dropdown-toggle info-number" id="navbarDropdown1" data-toggle="dropdown" aria-expanded="false">
                    <i class="fa fa-envelope-o"></i>
                    <span class="badge bg-green">6</span>
                  </a>
                  <ul class="dropdown-menu list-unstyled msg_list" role="menu" aria-labelledby="navbarDropdown1">
                    <li class="nav-item">
                      <a class="dropdown-item">
                        <span class="image"><img src="images/img.jpg" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li class="nav-item">
                      <a class="dropdown-item">
                        <span class="image"><img src="images/img.jpg" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li class="nav-item">
                      <a class="dropdown-item">
                        <span class="image"><img src="images/img.jpg" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li class="nav-item">
                      <a class="dropdown-item">
                        <span class="image"><img src="images/img.jpg" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li class="nav-item">
                      <div class="text-center">
                        <a class="dropdown-item">
                          <strong>See All Alerts</strong>
                          <i class="fa fa-angle-right"></i>
                        </a>
                      </div>
                    </li>
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        <div class="right_col" role="main">
          <div class=""> {/* 이 div는 전체 right_col 내용을 감쌉니다. */}
            
            {/* ▼▼▼▼▼ '이름으로 정보 검색' 섹션 시작 ▼▼▼▼▼ */}
            <div class="page-title">
              <div class="title_left">
                <h3>이름으로 정보 검색 <small>(DB 연동)</small></h3>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="row">
              <div class="col-md-12 col-sm-12 ">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>검색 조건</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <br />
                    {/* 검색 입력 폼 */}
                    <form id="searchFormByName" class="form-horizontal form-label-left">
                      <div class="form-group row">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="searchInputByName">검색할 이름: <span class="required">*</span></label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="searchInputByName" required="required" class="form-control" placeholder="이름을 입력하세요">
                        </div>
                      </div>
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                          <button type="button" class="btn btn-primary" id="clearSearchByNameButton">초기화</button>
                          <button type="submit" class="btn btn-success">검색</button>
                        </div>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="row">
              <div class="col-md-12 col-sm-12 ">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>검색 결과</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <div id="searchByNameLoadingIndicator" style="display: none; text-align: center; padding: 20px;">
                        <i class="fa fa-spinner fa-spin" style="font-size: 2em; color: #1ABB9C;"></i> 로딩 중...
                    </div>
                    <div id="searchByNameResultsContainer">
                      <p class="text-center">검색어를 입력하고 검색 버튼을 눌러주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            {/* ▲▲▲▲▲ '이름으로 정보 검색' 섹션 끝 ▲▲▲▲▲ */}

          </div>
        </div>
        <footer>
          <div class="pull-right">
            Gentelella - Bootstrap Admin Template by <a href="https://colorlib.com">Colorlib</a>
          </div>
          <div class="clearfix"></div>
        </footer>
        </div>
    </div>

    <script src="../vendors/jquery/dist/jquery.min.js"></script>
    <script src="../vendors/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../vendors/fastclick/lib/fastclick.js"></script>
    <script src="../vendors/nprogress/nprogress.js"></script>
    <script src="../vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
    <script src="../vendors/moment/min/moment.min.js"></script>
    <script src="../vendors/bootstrap-daterangepicker/daterangepicker.js"></script>
    
    <script src="../build/js/custom.js"></script>

    <script>
    $(document).ready(function() {
        // 이름 검색 기능 관련 jQuery 객체 캐싱
        const $searchInputByName = $('#searchInputByName');
        const $searchResultsByNameContainer = $('#searchByNameResultsContainer');
        const $searchByNameLoadingIndicator = $('#searchByNameLoadingIndicator');
        const $searchFormByName = $('#searchFormByName');

        // 이름 검색 폼 제출 시 이벤트 처리
        $searchFormByName.on('submit', function(event) {
            event.preventDefault(); // 폼의 기본 제출 동작(페이지 새로고침) 방지
            performNameSearch();
        });

        // 이름 검색 초기화 버튼 클릭 이벤트
        $('#clearSearchByNameButton').on('click', function() {
            $searchInputByName.val(''); // 입력 필드 비우기
            $searchResultsByNameContainer.html('<p class="text-center">검색어를 입력하고 검색 버튼을 눌러주세요.</p>'); // 결과 영역 초기화
        });
        
        function performNameSearch() {
            const searchTerm = $searchInputByName.val().trim();

            if (searchTerm === "") {
                alert("검색할 이름을 입력해주세요.");
                $searchInputByName.focus();
                return;
            }

            $searchByNameLoadingIndicator.show();
            $searchResultsByNameContainer.html(''); // 이전 검색 결과 지우기

            // AJAX를 사용하여 서버에 이름 검색 요청
            $.ajax({
                url: 'SearchDataByName.jsp', // 데이터를 검색할 서버 측 JSP 파일 (별도 구현 필요)
                type: 'GET',
                data: { name: searchTerm }, // 'name' 파라미터로 검색어 전달
                dataType: 'json',
                cache: false,
                success: function(results) {
                    $searchByNameLoadingIndicator.hide();
                    console.log("[CLIENT DEBUG index6] Name Search results received:", results);

                    if (results && results.length > 0) {
                        let htmlContent = '';
                        results.forEach(function(item) {
                            // TODO: 서버(SearchDataByName.jsp)에서 반환하는 item 객체의 실제 필드명에 맞게 아래를 수정해야 합니다.
                            // 예시: item.userName, item.userEmail, item.userPhone, item.department 등
                            htmlContent += '<div class="result-item">';
                            htmlContent += '  <h5><i class="fa fa-user"></i> ' + (item.name || '이름 정보 없음') + '</h5>'; // 예시 필드: name
                            htmlContent += '  <p><strong>이메일:</strong> ' + (item.email || '이메일 정보 없음') + '</p>'; // 예시 필드: email
                            htmlContent += '  <p><strong>연락처:</strong> ' + (item.phone || '연락처 정보 없음') + '</p>'; // 예시 필드: phone
                            // 여기에 DB에서 가져온 다른 정보들을 추가할 수 있습니다.
                            // htmlContent += '  <p><strong>부서:</strong> ' + (item.department || '부서 정보 없음') + '</p>';
                            htmlContent += '</div>';
                        });
                        $searchResultsByNameContainer.html(htmlContent);
                    } else {
                        $searchResultsByNameContainer.html('<p class="text-center">"' + searchTerm + '"에 대한 검색 결과가 없습니다.</p>');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    $searchByNameLoadingIndicator.hide();
                    console.error("[CLIENT DEBUG index6] Name Search AJAX Error:", textStatus, errorThrown);
                    console.error("[CLIENT DEBUG index6] Response Text:", jqXHR.responseText);
                    $searchResultsByNameContainer.html('<p class="text-center text-danger">이름 검색 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>');
                }
            });
        }

        // index6.jsp 페이지에 해당하는 사이드바 메뉴 활성화
        $('ul.nav.side-menu a[href="index6.html"]').closest('li').addClass('current-page active');
        // Gentelella 테마에서 상위 메뉴도 active 상태로 만들기 위함 (<a> 태그가 직접 li의 자식이 아닐 경우)
        $('ul.nav.side-menu a[href="index6.html"]').parents('li').addClass('active');


        // (선택 사항) 만약 daterangepicker 같은 다른 JS 라이브러리 초기화가 필요하면 여기에 추가합니다.
        // 예: $('#myDatepicker').daterangepicker();
        // 이 페이지에서는 daterangepicker를 사용하지 않으므로 관련 초기화 코드는 없습니다.

    });
    </script>

  </body>
</html>