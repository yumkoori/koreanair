<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>좌석 관리</title>
    <link href="../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <link href="../vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">
    <link href="../build/css/custom.min.css" rel="stylesheet">
    <style>
    body {
        margin: 0;
        background: #f4f4f4;
        font-family: Arial, sans-serif;
    }
    /* === 기존 좌석 및 시설 관련 스타일 (변경 없음) === */
    .visual-seat-row { display: flex; align-items: center; margin: 3px 0; padding-left: 5px; }
    .row-number { width: 25px; flex-shrink: 0; text-align: right; font-size: 10px; font-weight: bold; color: #555; margin-right: 5px; }
    .row { display: flex; justify-content: center; flex-grow: 1; margin: 0; }
    .seat { 
        width: 8%; 
        height: auto; 
        aspect-ratio: 1 / 1; 
        background-color: #338fff; 
        color: white; 
        text-align: center; 
        display: flex; 
        flex-direction: column; 
        align-items: center; 
        justify-content: center; 
        border-radius: 4px; 
        margin: 1.5px; 
        position: relative; 
        font-weight: bold; 
        cursor: pointer;
        padding: 1px; 
        line-height: 1.1; 
    }
    .seat:hover { background-color: #2079e0; }
    .aisle { width: 3.5%; flex-shrink: 0; }
    .seat-removed { width: 8%; height: auto; aspect-ratio: 1 / 1; margin: 1.5px; visibility: hidden; }
    .exit-row { display: flex; justify-content: space-between; margin: 10px 0; padding: 0 3%; }
    .exit { text-align: center; color: white; background: red; font-size: 10px; font-weight: bold; padding: 2px 6px; border-radius: 3px; display: inline-block; }
    .facility-row { display: flex; justify-content: space-around; align-items: center; margin: 8px 0; padding: 4px 0; border-top: 1px solid #f0f0f0; border-bottom: 1px solid #f0f0f0; }
    .facility-group { display: flex; gap: 4px; }
    .facility-item { background: #e9e9e9; border-radius: 3px; padding: 3px 6px; font-size: 12px; color: #333; text-align: center; }
    .facility-item.exit-facility { background: red; color: white; font-weight: bold; }
    .section-divider { margin: 15px 0; border-top: 1px dashed #ccc; }
    .info-text { font-size: 9px; text-align: center; color: #555; margin-bottom: 2px; }

    /* 좌석 내부에 표시될 좌석 문자 및 가격 스타일 */
    .seat .seat-letter {
        font-size: 10px; 
        font-weight: bold;
        display: block; 
    }
    .seat .seat-price-display {
        font-size: 9px;
        color: #FFA500;
        margin-top: 0px; 
        display: block; 
        font-weight: normal;
    }
    .seat-selected-highlight .seat-price-display { 
        color: #333333 !important; 
    }


    /* === Gentelella 사이드바 및 상단바 고정 스타일 (기존 유지) === */
    .nav-md .left_col { position: fixed; top: 0; left: 0; bottom: 0; width: 230px; z-index: 1000; background: #2A3F54; }
    .nav-md .left_col .scroll-view { height: 100%; overflow-y: auto; }
    .nav-md .right_col { margin-left: 230px; }
    .nav-md .top_nav { margin-left: 230px; z-index: 1001; }

    /* === 비행기 좌석 관련 스타일 수정 === */
    .airplane {
        width: 100%; 
        max-width: 580px; 
        margin: 0 auto 20px auto; 
        background: white;
        border-radius: 290px 290px 50px 50px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
        padding: 20px 10px 30px 10px;
        position: relative;
    }
    .airplane::before { content: ""; position: absolute; top: -50px; left: 50%; transform: translateX(-50%); width: 100px; height: 50px; background: #ddd; border-radius: 0 0 50px 50px; }

    /* === 좌석 선택 패널 스타일 === */
    #seatSelectionPanel {
        width: 260px; 
        flex-shrink: 0; 
        padding: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: #f9f9f9;
        position: sticky;
        top: 70px; 
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        height: fit-content; 
    }
    #seatSelectionPanel .panel-title-bar { 
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px; 
    }
    #seatSelectionPanel .panel-title-bar h4 {
        margin-bottom: 0; 
    }
    #resetSelectedSeatsButton { 
        padding: 3px 7px; 
        line-height: 1; 
    }

    #seatSelectionPanel h4, #seatSelectionPanel h5 { margin-top: 0; margin-bottom: 10px; color: #333; }
    #seatSelectionPanel p { margin-bottom: 8px; font-size: 13px; color: #555; }
    .seat-selected-highlight { background-color: #ffc107 !important; color: #333 !important; border: 2px solid #e0a800 !important; box-shadow: 0 0 8px rgba(255,193,7,0.8); }

    #selectedSeatInfo { 
        max-height: 150px; 
        overflow-y: auto;  
        margin-bottom: 10px; 
    }
    #selectedSeatInfo ul {
        padding-left: 0;
        list-style-type: none;
        margin-bottom: 0; 
    }
    #selectedSeatInfo li {
        font-size: 12px;
        margin-bottom: 3px;
    }
    #seatSelectionPanel .form-group {
        margin-bottom: 5px;
    }

    /* === 검색 드롭다운 스타일 === */
    .aircraft-option:hover {
        background-color: #f5f5f5;
    }

    /* === 반응형 스타일 === */
    @media (max-width: 992px) { 
        .x_content > div[style*="display: flex;"] {
            flex-wrap: wrap !important; 
        }
        #airplaneContainerWrapper {
            width: 100%; 
            order: 1; 
        }
        #seatSelectionPanel {
            width: 100% !important; 
            margin-top: 20px;
            position: static !important; 
            top: auto !important;
            order: 2; 
            height: auto; 
            max-height: none; 
            overflow-y: visible; 
        }
        #selectedSeatInfo { 
            max-height: none;
            overflow-y: visible;
        }
    }

    @media (max-width: 768px) { 
        .airplane { max-width: 98%; border-radius: 120px 120px 25px 25px; margin: 10px auto; padding: 15px 5px 20px 5px;}
        .airplane::before { width: 70px; height: 35px; top: -35px; border-radius: 0 0 35px 35px;}
        .row-number { width: 20px; font-size: 9px; margin-right: 3px;}
        .seat { 
            width: 10%; 
            margin:1px; 
            border-radius: 3px;
        }
        .seat .seat-letter { font-size: 8px; } 
        .seat .seat-price-display { font-size: 8px; color: #FFA500; }

        .seat-removed { width: 10%; margin: 1px;}
        .aisle { width: 3%; }
        .facility-item { font-size: 10px; padding: 2px 4px;}
        .exit { font-size: 9px; padding: 2px 4px;}
    }
    </style>
</head>

<body class="nav-md">
    <div class="container body">
    <div class="main_container">
        <div class="col-md-3 left_col">
        <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
            <a href="index.html" class="site_title"><i class="fa fa-paw"></i> <span>제발좀</span></a>
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
                    <li><a href="index5.html">비행스</a></li>
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
                            <li class="sub_menu"><a href="level2.html">Level Two</a>
                            </li>
                            <li><a href="#level2_1">Level Two</a>
                            </li>
                            <li><a href="#level2_2">Level Two</a>
                            </li>
                        </ul>
                        </li>
                        <li><a href="#level1_2">Level One</a>
                        </li>
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
        <div class="">
            <div class="page-title">
            <div class="title_left">
                <h3>비행기 좌석 배치도</h3>
            </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
            <div class="col-md-12 col-sm-12 ">
                <div class="x_panel">
                <div class="x_title">
                    <h2>좌석 배치 <small id="aircraftModelName" style="color: blue;">보잉 787-9 (278석)</small></h2>
                    <div style="float: right; margin-left: 20px;">
                        <label for="aircraftSearch" style="margin-right: 5px; font-weight: normal; font-size: 13px; vertical-align: middle;">기종 검색:</label>
                        <div style="display: inline-block; position: relative;">
                            <input type="text" id="aircraftSearch" class="form-control" 
                                   style="display: inline-block; width: 180px; padding: 4px 8px; height: auto; font-size: 13px;"
                                   placeholder="기종명을 입력하세요"
                                   value="보잉 787-9 (278석)"
                                   autocomplete="off">
                            <div id="aircraftDropdown" style="position: absolute; top: 100%; left: 0; right: 0; background: white; border: 1px solid #ccc; border-top: none; max-height: 200px; overflow-y: auto; z-index: 1000; display: none;">
                                <div class="aircraft-option" data-value="model1" style="padding: 8px; cursor: pointer; border-bottom: 1px solid #eee;">보잉 787-9 (278석)</div>
                                <div class="aircraft-option" data-value="model2" style="padding: 8px; cursor: pointer; border-bottom: 1px solid #eee;">다른 기종 (준비중)</div>
                            </div>
                        </div>
                    </div>
                    <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                    <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                    </ul>
                    <div class="clearfix"></div>
                </div>
                <div class="x_content">
                    <div style="display: flex; flex-wrap: nowrap; align-items: flex-start; gap: 20px;">
                        <div id="airplaneContainerWrapper" style="flex-grow: 1;">
                            <div id="airplaneContainer" class="airplane">
                                {/* JavaScript로 동적 생성 */}
                            </div>
                        </div>
                        <div id="seatSelectionPanel">
                            <div class="panel-title-bar">
                                <h4>선택된 좌석</h4>
                                <button id="resetSelectedSeatsButton" class="btn btn-default btn-xs" title="선택 초기화">
                                    <i class="fa fa-undo"></i>
                                </button>
                            </div>
                            <div id="selectedSeatInfo"> 
                                <p>좌석을 선택하세요.</p>
                            </div>
                            <hr>
                            <h5>좌석 가격 일괄 입력</h5>
                            <div class="form-group"> 
                                <label for="seatPriceInput" style="font-weight: normal; font-size: 12px;">가격 (원):</label>
                                <input type="number" class="form-control input-sm" id="seatPriceInput" placeholder="숫자만 입력">
                            </div>
                            <button id="applyPriceButton" class="btn btn-primary btn-sm" style="width: 100%; margin-top: 10px;">선택 좌석 가격 적용</button>
                            <button id="saveSelectedSeatsButton" class="btn btn-success btn-sm" style="width: 100%; margin-top: 10px;">선택 내용 DB 저장 (준비)</button>
                            <button id="loadSeatsButton" class="btn btn-info btn-sm" style="width: 100%; margin-top: 10px;">저장된 좌석 불러오기</button>
                        </div>
                    </div>

                    {/* --- 좌석 배치도 생성 및 제어 스크립트 --- */}
                    <script>
                        const aircraftData = {
                            "model1": {
                                name: "보잉 787-9 (기본형)",
                                prestigeLayout: ['A', 'B', ' ', 'D', 'E', ' ', 'H', 'J'],
                                prestigeRows: [7, 8, 9, 10],
                                economyLayout: ['A', 'B', 'C', ' ', 'D', 'E', 'F', ' ', 'G', 'H', 'J'],
                                economySections: [
                                    { startRow: 28, endRow: 43, info: "Economy Class (Rows 28-43)", removedSeats: { 28: ['A','B','C'], 43: ['D','E','F'] } },
                                    { startRow: 44, endRow: 57, info: "Economy Class (Rows 44-57)", removedSeats: { 44: ['C','G','D','E','F'], 45: ['D','E','F'], 57: ['A','J'] } }
                                ],
                                frontFacilitiesHTML: `<div class="facility-row"> <div class="facility-group"> <span class="facility-item">🍽</span> <span class="facility-item">🍽</span> </div> </div><div class="facility-row"> <div class="facility-group"> <span class="facility-item exit-facility" style="margin-left: 20px;">EXIT</span> </div> <div class="facility-group"> <span class="facility-item">🍽</span> </div> <div class="facility-group"> <span class="facility-item">🚻♿</span> <span class="facility-item exit-facility" style="margin-right: 20px;">EXIT</span> </div> </div>`,
                                prestigeEndFacilitiesHTML: `<div class="exit-row"> <span class="exit">EXIT</span> <span class="exit">EXIT</span> </div><div class="facility-row"> <div class="facility-group"><span class="facility-item">🚻♿</span></div> <div class="facility-group"><span class="facility-item">🍽</span></div> <div class="facility-group"><span class="facility-item">🚻</span></div> </div>`,
                                economy1EndFacilitiesHTML: `<div class="exit-row"> <span class="exit">EXIT</span> <span class="exit">EXIT</span> </div><div class="facility-row"> <div class="facility-group"><span class="facility-item">🚻♿</span><span class="facility-item">🚻</span></div> <div class="facility-group"><span class="facility-item">🍽</span></div> <div class="facility-group"><span class="facility-item">🚻</span></div> </div>`,
                                rearFacilitiesHTML: `<div class="exit-row"> <span class="exit">EXIT</span> <span class="exit">EXIT</span> </div><div class="facility-row"> <div class="facility-group"><span class="facility-item">🚻</span></div> <div class="facility-group"><span class="facility-item">🍽</span><span class="facility-item">🍽</span></div> <div class="facility-group"><span class="facility-item">🚻</span></div> </div><div class="facility-row"> <div class="facility-group"> <span class="facility-item">🍽</span> <span class="facility-item">🍽</span> </div> </div>`
                            },
                            "model2": { name: "다른 기종 (준비중)" }
                        };

                        let selectedSeatsMap = new Map();
                        let seatsReadyForDB = [];

                        function updateSelectedSeatsDisplay() {
                            const selectedSeatInfoDiv = document.getElementById('selectedSeatInfo');
                            if (!selectedSeatInfoDiv) {
                                console.error("Element with ID 'selectedSeatInfo' not found.");
                                return;
                            }
                            if (selectedSeatsMap.size === 0) {
                                selectedSeatInfoDiv.innerHTML = '<p>좌석을 선택하세요.</p>';
                                return;
                            }
                            let listHtml = '<ul>'; 
                            selectedSeatsMap.forEach((details, key) => {
                                // FIXED
                                listHtml += `<li>\${details.row}열 \${details.seat}석</li>`;
                            });
                            listHtml += '</ul>';
                            selectedSeatInfoDiv.innerHTML = listHtml;
                        }
                        
                        function resetSelectedSeats() {
                            selectedSeatsMap.clear(); 
                            const highlightedSeats = document.querySelectorAll('#airplaneContainer .seat.seat-selected-highlight');
                            highlightedSeats.forEach(seat => {
                                seat.classList.remove('seat-selected-highlight');
                            });
                            updateSelectedSeatsDisplay(); 
                            document.getElementById('seatPriceInput').value = ''; 
                            console.log('Current seat selections and price input have been reset.');
                        }

                        function renderAircraft(modelKey) {
                            const airplaneDiv = document.getElementById("airplaneContainer");
                            if (!airplaneDiv) { console.error("Airplane container div ('airplaneContainer') not found!"); return; }
                            
                            let htmlContent = '';
                            const model = aircraftData[modelKey];
                            const modelNameDisplay = document.getElementById('aircraftModelName');
                            const searchInput = document.getElementById('aircraftSearch');
                            const selectedOptionText = searchInput ? searchInput.value : aircraftData[modelKey].name;

                            if (modelNameDisplay) { modelNameDisplay.innerText = selectedOptionText; }
                            
                            resetSelectedSeats(); 
                            seatsReadyForDB = []; 
                            console.log("Aircraft model changed, seatsReadyForDB has been reset.");

                            if (modelKey === "model2" || !model.prestigeLayout) {
                                // FIXED
                                htmlContent = `<p style="text-align:center; padding: 20px;">\${(modelKey === "model2") ? `\${selectedOptionText}의 좌석 배치도는 현재 준비 중입니다.` : '좌석 배치도 정보를 불러올 수 없습니다.'}</p>`;
                                airplaneDiv.innerHTML = htmlContent;
                                return;
                            }

                            htmlContent += model.frontFacilitiesHTML || '';
                            htmlContent += '<div class="section-divider"></div><p class="info-text">Prestige Class</p>';
                            model.prestigeRows.forEach(r => {
                                // FIXED
                                htmlContent += `<div class="visual-seat-row"><div class="row-number">\${r}</div><div class="row">`;
                                model.prestigeLayout.forEach(c => { 
                                    let seatDisplayContent = `<span class="seat-letter">\${c}</span>`;
                                    // FIXED
                                    htmlContent += (c === ' ') ? '<div class="aisle"></div>' : `<div class="seat" data-row="\${r}" data-seat="\${c}">\${seatDisplayContent}</div>`; 
                                });
                                htmlContent += '</div></div>';
                            });
                            htmlContent += model.prestigeEndFacilitiesHTML || '';
                            model.economySections.forEach((section, index) => {
                                // FIXED
                                htmlContent += `<div class="section-divider"></div><p class="info-text">\${section.info}</p>`;
                                for (let r = section.startRow; r <= section.endRow; r++) {
                                    // FIXED
                                    htmlContent += `<div class="visual-seat-row"><div class="row-number">\${r}</div><div class="row">`;
                                    model.economyLayout.forEach(c => {
                                        if (c === ' ') { htmlContent += '<div class="aisle"></div>'; }
                                        else {
                                            let isRemoved = (section.removedSeats && section.removedSeats[r] && section.removedSeats[r].includes(c));
                                            let seatDisplayContent = `<span class="seat-letter">\${c}</span>`;
                                            // FIXED
                                            htmlContent += isRemoved ? '<div class="seat-removed"></div>' : `<div class="seat" data-row="\${r}" data-seat="\${c}">\${seatDisplayContent}</div>`;
                                        }
                                    });
                                    htmlContent += '</div></div>';
                                }
                                if (index === 0 && model.economy1EndFacilitiesHTML) { htmlContent += model.economy1EndFacilitiesHTML; }
                            });
                            htmlContent += model.rearFacilitiesHTML || '';
                            airplaneDiv.innerHTML = htmlContent;

                            seatsReadyForDB.forEach(dbSeat => {
                                if (dbSeat.aircraft === aircraftData[modelKey].name) { 
                                    const seatElement = document.querySelector(`.seat[data-row="\${dbSeat.row}"][data-seat="\${dbSeat.seat}"]`);
                                    if (seatElement && typeof dbSeat.price === 'number') {
                                        // FIXED
                                        seatElement.innerHTML = `<span class="seat-letter">\${dbSeat.seat}</span><span class="seat-price-display">\${dbSeat.price.toLocaleString()}</span>`;
                                    }
                                }
                            });
                        }

                        function handleSeatClick(event) {
                            const targetSeatElement = event.target.closest('.seat'); 

                            if (targetSeatElement && !targetSeatElement.classList.contains('seat-removed')) {
                                const seatLetter = targetSeatElement.dataset.seat;
                                const rowNumber = targetSeatElement.dataset.row;
                                const seatKey = `\${rowNumber}-\${seatLetter}`; 

                                if (selectedSeatsMap.has(seatKey)) { 
                                    selectedSeatsMap.delete(seatKey);
                                    targetSeatElement.classList.remove('seat-selected-highlight');
                                } else { 
                                    selectedSeatsMap.set(seatKey, { row: rowNumber, seat: seatLetter, price: null });
                                    targetSeatElement.classList.add('seat-selected-highlight');
                                }
                                updateSelectedSeatsDisplay(); 
                            }
                        }

                        function applyPriceToSelectedSeats() {
                            const priceInput = document.getElementById('seatPriceInput');
                            const priceValue = parseInt(priceInput.value, 10);

                            if (isNaN(priceValue) || priceValue < 0) {
                                alert('유효한 가격을 입력해주세요 (숫자, 0 이상).');
                                priceInput.focus();
                                return;
                            }

                            if (selectedSeatsMap.size === 0) { 
                                alert('가격을 적용할 좌석을 먼저 UI에서 선택해주세요.');
                                return;
                            }

                            const currentSearchValue = document.getElementById('aircraftSearch').value;
                            let selectedModelKey = 'model1'; // 기본값
                            const aircraftOptions = document.querySelectorAll('.aircraft-option');
                            aircraftOptions.forEach(option => {
                                if (option.textContent === currentSearchValue) {
                                    selectedModelKey = option.getAttribute('data-value');
                                }
                            });
                            const aircraftModelName = aircraftData[selectedModelKey].name;
                            let appliedCount = 0;

                            selectedSeatsMap.forEach((details, seatKey) => {
                                const seatDataForDB = {
                                    aircraft: aircraftModelName,
                                    row: details.row,
                                    seat: details.seat,
                                    price: priceValue 
                                };

                                const existingSeatIndexInDB = seatsReadyForDB.findIndex(
                                    item => item.aircraft === seatDataForDB.aircraft && 
                                            item.row === seatDataForDB.row && 
                                            item.seat === seatDataForDB.seat
                                );

                                if (existingSeatIndexInDB > -1) {
                                    seatsReadyForDB[existingSeatIndexInDB] = seatDataForDB; 
                                } else {
                                    seatsReadyForDB.push(seatDataForDB); 
                                }
                                appliedCount++;

                                const seatElement = document.querySelector(`.seat[data-row="\${details.row}"][data-seat="\${details.seat}"]`);
                                if (seatElement) {
                                    // FIXED
                                    seatElement.innerHTML = `<span class="seat-letter">\${details.seat}</span><span class="seat-price-display">\${priceValue.toLocaleString()}</span>`;
                                }
                            });
                            

                            

                            if (appliedCount > 0) {
                                // FIXED
                                alert(`\${appliedCount}개 좌석에 \${priceValue.toLocaleString()}원이 적용(UI 표시)되었으며, 저장 대기 중입니다.`);
                            }
                            
                            resetSelectedSeats(); 
                            
                            console.log("좌석 최종 저장 대기 목록 (seatsReadyForDB):", JSON.parse(JSON.stringify(seatsReadyForDB)));
                        }

                        function prepareSaveSelectedSeats() {
                            if (seatsReadyForDB.length === 0) {
                                alert('DB에 저장할 좌석 정보가 없습니다.');
                                return;
                            }

                            // 현재 페이지 URL에서 craftid 파라미터 값을 가져옵니다
                            const urlParams = new URLSearchParams(window.location.search);
                            const craftId = urlParams.get('craftid') || '';

                            const contextPath = "${pageContext.request.contextPath}";
                            const url = `\${contextPath}/seatsave.wi?craftid=\${encodeURIComponent(craftId)}`;
                            const jsonData = JSON.stringify(seatsReadyForDB);
                            
                            console.log("저장 시 사용할 craftid:", craftId);
                            console.log("저장 요청 URL:", url);

                            fetch(url, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: jsonData,
                            })
                            .then(response => {
                                if (!response.ok) {
                                    // 서버가 4xx, 5xx 에러를 보냈을 경우
                                    throw new Error(`서버 에러 발생! 상태: \${response.status}`);
                                }
                                // 서버 응답을 JSON 객체로 바로 파싱하여 다음 then으로 넘깁니다.
                                return response.json(); 
                            })
                            .then(data => {
                                // 'data'는 서버가 보낸 JSON이 변환된 자바스크립트 객체입니다.
                                // 예: { status: 'success', message: '8개의 좌석...', savedCount: 8 }
                                console.log('서버로부터 받은 데이터:', data);

                                // 서버가 보낸 메시지를 그대로 alert 창에 보여줍니다.
                                alert(data.message); 

                                if (data.status === 'success') {
                                    // 성공했을 때만 페이지를 새로고침합니다.
                                    location.reload();
                                }
                            })
                            .catch(error => {
                                // 네트워크 통신 자체에 실패했거나, 위에서 throw된 에러를 처리합니다.
                                console.error('Fetch 요청 최종 실패:', error);
                                alert('요청 처리 중 문제가 발생했습니다.');
                            });
                        }
                        
                        function loadSavedSeats() {
                            alert("버튼 클릭 실행 성공!");
                            
                            // 현재 페이지 URL에서 craftid 파라미터 값을 가져옵니다
                            const urlParams = new URLSearchParams(window.location.search);
                            const craftId = urlParams.get('craftid') || ''; // craftid가 없으면 빈 문자열

                            // 기본 URL 설정
                            const contextPath = "${pageContext.request.contextPath}";
                            const baseUrl = `\${contextPath}/seatload.wi`;

                            // craftid만 파라미터로 전달
                            const finalUrl = `\${baseUrl}?craftid=\${encodeURIComponent(craftId)}`;

                            console.log("현재 URL의 craftid:", craftId);
                            console.log("최종 요청 URL:", finalUrl);
                            
                            fetch(finalUrl) // GET 요청은 URL만 넘겨주면 됩니다.
                            .then(response => {
                                // 1. HTTP 응답 상태를 확인합니다. (성공: 200~299)
                                if (!response.ok) {
                                    // 서버가 에러 코드를 응답한 경우, 여기서 에러를 발생시켜 catch 블록으로 보냅니다.
                                    throw new Error(`서버 에러 발생! 상태: ${response.status}`);
                                }
                                
                                // 2. 서버가 보낸 응답을 JSON 객체로 파싱합니다.
                                return response.json();
                            })
                            .then(userData => {
                                // 3. 성공적으로 받은 데이터(JSON이 변환된 자바스크립트 객체)를 사용합니다.
                                console.log('성공적으로 받은 사용자 데이터:', userData);
                                
                                // 예: 화면에 사용자 이름 표시
                                // document.getElementById('username').textContent = userData.name;
                            })
                            .catch(error => {
                                // 4. 네트워크 오류 또는 위 .then() 블록에서 throw된 에러를 처리합니다.
                                console.error('요청 실패:', error);
                                alert('데이터를 불러오는 데 실패했습니다.');
                            });
                        }
                        function Searchplane(){
                        	const searchInput = document.getElementById('aircraftSearch');
                        	const searchValue = searchInput.value.trim();
                        	
                        	console.log('검색어:', searchValue);
                        	
                        	// 검색어가 있으면 서버에 요청, 없으면 기본 비행기 표시
                        	if (searchValue) {
                        		console.log('검색 실행:', searchValue);
                        		
                        		const contextPath = "${pageContext.request.contextPath}";
                        		const url = `\${contextPath}/seachplane.wi?searchword=\${encodeURIComponent(searchValue)}`;
                        		console.log('요청 URL:', url);
                        		
                        		fetch(url)
                        		.then(response => {
                        			if (!response.ok) {
                        				throw new Error(`서버 에러 발생! 상태: \${response.status}`);
                        			}
                        			return response.json();
                        		})
                        		.then(userData => {
                        			console.log('서버 응답:', userData);
                        			
                        			// check 값에 따라 다른 처리
                        			if (userData.check === 1) {
                        				// 검색 성공 - 검색어로 변경되었다고 알림
                        				alert(`"${searchValue}" 검색 완료! 현재는 보잉 787-9만 지원됩니다.`);
                        				
                        				// 검색 결과로 기본 비행기 표시
                        				searchInput.value = searchValue; // 검색어 그대로 유지
                        				renderAircraft('model1');
                        				
                        				// URL에 검색어를 craftid 파라미터로 추가
                        				const currentUrl = new URL(window.location);
                        				currentUrl.searchParams.set('craftid', searchValue);
                        				window.history.pushState({}, '', currentUrl);
                        				
                        			} else {
                        				// 검색 실패 - DB에 없는 비행기
                        				alert('DB에 없는 비행기입니다.');
                        				
                        				// 검색창을 기본값으로 되돌림
                        				searchInput.value = "보잉 787-9 (278석)";
                        				renderAircraft('model1');
                        			}
                        		})
                        		.catch(error => {
                        			console.error('검색 실패:', error);
                        			alert('검색 중 오류가 발생했습니다.');
                        			
                        			// 에러 시 기본값으로 되돌림
                        			searchInput.value = "보잉 787-9 (278석)";
                        			renderAircraft('model1');
                        		});
                        	} else {
                        		alert('검색어를 입력해주세요.');
                        	}
                        }
                        

                        document.addEventListener('DOMContentLoaded', function() {
                            const searchInput = document.getElementById('aircraftSearch');
                            const dropdown = document.getElementById('aircraftDropdown');
                            const airplaneContainer = document.getElementById('airplaneContainer');
                            const applyPriceBtn = document.getElementById('applyPriceButton');
                            const saveButton = document.getElementById('saveSelectedSeatsButton');
                            const resetButton = document.getElementById('resetSelectedSeatsButton'); 
                            const loadButton = document.getElementById('loadSeatsButton');
                            let currentModelKey = 'model1';

                            // 기본으로 model1 렌더링 (페이지 로드 시 비행기 표시)
                            console.log('페이지 로드 완료 - 기본 비행기 표시');
                            renderAircraft(currentModelKey);

                            if (searchInput && dropdown) {
                                // 검색 입력 시 드롭다운 표시/필터링
                                searchInput.addEventListener('input', function() {
                                    const searchValue = this.value.toLowerCase();
                                    const options = dropdown.querySelectorAll('.aircraft-option');
                                    let hasVisibleOptions = false;
                                    
                                    options.forEach(option => {
                                        const text = option.textContent.toLowerCase();
                                        if (text.includes(searchValue)) {
                                            option.style.display = 'block';
                                            hasVisibleOptions = true;
                                        } else {
                                            option.style.display = 'none';
                                        }
                                    });
                                    
                                    dropdown.style.display = hasVisibleOptions && searchValue ? 'block' : 'none';
                                });

                                // 포커스 시 드롭다운 표시
                                searchInput.addEventListener('focus', function() {
                                    dropdown.style.display = 'block';
                                });

                                // 옵션 클릭 시 선택
                                dropdown.addEventListener('click', function(e) {
                                    if (e.target.classList.contains('aircraft-option')) {
                                        const selectedText = e.target.textContent;
                                        const selectedValue = e.target.getAttribute('data-value');
                                        
                                        searchInput.value = selectedText;
                                        dropdown.style.display = 'none';
                                        currentModelKey = selectedValue;
                                        renderAircraft(selectedValue);
                                    }
                                });

                                // 외부 클릭 시 드롭다운 닫기
                                document.addEventListener('click', function(e) {
                                    if (!searchInput.contains(e.target) && !dropdown.contains(e.target)) {
                                        dropdown.style.display = 'none';
                                    }
                                });
                            } else {
                                console.error("Aircraft search input or dropdown not found!");
                            }
                            if (airplaneContainer) {
                                airplaneContainer.addEventListener('click', handleSeatClick);
                            } else {
                                console.error("Airplane container for click listener not found!");
                            }
                            if (applyPriceBtn) {
                                applyPriceBtn.addEventListener('click', applyPriceToSelectedSeats);
                            } else {
                                console.error("Apply price button not found!");
                            }
                            if (saveButton) {
                                saveButton.addEventListener('click', prepareSaveSelectedSeats);
                            } else {
                                console.error("Save selected seats button not found!");
                            }
                            if (resetButton) { 
                                resetButton.addEventListener('click', resetSelectedSeats);
                            } else {
                                console.error("Reset selected seats button not found!");
                            }
                            if (loadButton) {
                                console.log('불러오기 버튼을 성공적으로 찾았습니다.');
                                loadButton.addEventListener('click', loadSavedSeats);
                            } else {
                                console.error('ID가 "loadSeatsButton"인 버튼을 찾을 수 없습니다!');
                            }
                            if (searchInput){
                            	console.log("검색 입력창 정상작동합니다");
                            	searchInput.addEventListener('keydown', function(event) {
                            	    if (event.key === 'Enter' || event.keyCode === 13) {
                            	        event.preventDefault(); // 폼 제출 방지
                            	        Searchplane();
                            	    }
                            	});
                            }

                        });
                    </script>
                </div>
                </div>
            </div>
            </div>
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
    <script src="../vendors/Chart.js/dist/Chart.min.js"></script>
    <script src="../vendors/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
    <script src="../vendors/Flot/jquery.flot.js"></script>
    <script src="../vendors/Flot/jquery.flot.pie.js"></script>
    <script src="../vendors/Flot/jquery.flot.time.js"></script>
    <script src="../vendors/Flot/jquery.flot.stack.js"></script>
    <script src="../vendors/Flot/jquery.flot.resize.js"></script>
    <script src="../vendors/flot.orderbars/js/jquery.flot.orderBars.js"></script>
    <script src="../vendors/flot-spline/js/jquery.flot.spline.min.js"></script>
    <script src="../vendors/flot.curvedlines/curvedLines.js"></script>
    <script src="../vendors/DateJS/build/date.js"></script>
    <script src="../vendors/moment/min/moment.min.js"></script>
    <script src="../vendors/bootstrap-daterangepicker/daterangepicker.js"></script>
    <script src="../build/js/custom.min.js"></script>
</body>
</html>