<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
request.setCharacterEncoding("UTF-8");

String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- 캐시 무력화를 위한 메타 태그 추가 -->
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">

    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="images/korea.png" type="image/ico" /> <!-- 여기는 홈페이지 위에 띄워져 있는 아이콘을 관리한다-->

    <title>대한항공 관리자 페이지 </title>  <!-- 여기는 홈페이지 위에 띄는 글자들을 관리한다 -->

    <!-- Bootstrap -->  <!-- Bootstrap은 가장 널리 쓰이는 프론트엔드 UI 프레임워크야. 
                         이 줄은 Bootstrap의 기본 스타일(CSS) 을 적용하는 것.-->
    <link href="<%=contextPath%>/views/vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet"> 


    <!-- Font Awesome --> <!-- 웹에서 아이콘(font 형태로 된 이미지)을 쓸 수 있게 해주는 CSS야.-->
    <link href="<%=contextPath%>/views/vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">


    <!-- NProgress --> <!-- 페이지 로딩 시 상단에 로딩바(progress bar) 를 표시해주는 스타일. UX 개선에 사용용-->
    <link href="<%=contextPath%>/views/vendors/nprogress/nprogress.css" rel="stylesheet">


    <!-- iCheck --> <!-- checkbox나 radio 버튼을 예쁘게 커스터마이징할 때 쓰는 스타일.-->
    <link href="<%=contextPath%>/views/vendors/iCheck/skins/flat/green.css" rel="stylesheet">

	
    <!-- bootstrap-progressbar --> <!-- Bootstrap과 연동되는 진행 바(progress bar) 기능을 위한 CSS. 예를 들어 퍼센트 게이지를 보여주는 UI에 사용돼.-->
    <link href="<%=contextPath%>/views/vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">


    <!-- JQVMap --> <!-- JavaScript Vector Map: 대시보드나 관리자 페이지에서 인터랙티브 지도를 만들 때 사용.-->
    <link href="<%=contextPath%>/views/vendors/jqvmap/dist/jqvmap.min.css" rel="stylesheet"/>


    <!-- bootstrap-daterangepicker --> <!-- 날짜 범위 선택용 UI를 스타일링해주는 CSS.-->
    <link href="<%=contextPath%>/views/vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">


    <!-- Custom Theme Style --> <!-- 프로젝트에서 정의한 커스텀 스타일.-->
    <link href="<%=contextPath%>/views/build/css/custom.min.css" rel="stylesheet">
  </head>

  <body class="nav-md">
    <div class="container body">
      <div class="main_container">
        <div class="col-md-3 left_col">
          <jsp:include page="sidebar.jsp"></jsp:include>
        </div>

        <jsp:include page="topnav.jsp"></jsp:include>

        <div class="right_col" role="main">

          
          <!-- top tiles -->
          <div class="row" style="display: inline-block;" >
          <div class="tile_count">
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="total-users">
              <span class="count_top"><i class="fa fa-user"></i> Total Users</span>

              <div class="count"></div>


              <span class="count_bottom"><i class="green">4% </i> From last Week</span>
            </div>
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="average-time">
              <span class="count_top"><i class="fa fa-clock-o"></i> Average Time</span>
              <div class="count"></div>
              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>3% </i> From last Week</span>
            </div>
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="total-males">
              <span class="count_top"><i class="fa fa-user"></i> Total Males</span>
              <div class="count green"></div>
              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
            </div>
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="total-females">
              <span class="count_top"><i class="fa fa-user"></i> Total Females</span>
              <div class="count"></div>
              <span class="count_bottom"><i class="red"><i class="fa fa-sort-desc"></i>12% </i> From last Week</span>
            </div>
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="total-collections">
              <span class="count_top"><i class="fa fa-plane"></i> Total Reservations</span>

              <div class="count"></div>


              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
            </div>
            <div class="col-md-2 col-sm-4 tile_stats_count" data-stat="total-connections">
              <span class="count_top"><i class="fa fa-globe"></i> Total Sessions</span>

              <div class="count"></div>


              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
            </div>
          </div>
        </div>
          <!-- /top tiles -->

          <div class="row">
            <div class="col-md-12 col-sm-12 ">
              <div class="dashboard_graph">

                <div class="row x_title">
                  <div class="col-md-6">
                    <h3>Network Activities <small>Graph title sub-title</small></h3>
                  </div>
                  <div class="col-md-6">
                    <div id="yearSelector" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                      <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                      <select id="yearSelect" style="border: none; background: transparent; font-size: 14px;">
                        <!-- 년도 옵션들이 JavaScript로 추가됩니다 -->
                      </select>
                    </div>
                  </div>
                </div>

                <div class="col-md-9 col-sm-9 ">
                  <div id="chart_plot_01" class="demo-placeholder"></div>
                </div>
                <div class="col-md-3 col-sm-3  bg-white">
                  <div class="x_title">
                    <h2>월별 예약 순위 <small>Top Monthly Reservations</small></h2>
                    <div class="clearfix"></div>
                  </div>

                  <!-- JavaScript에서 동적으로 생성될 월별 예약 순위 컨테이너 -->
                  <div id="topReservationContainer" class="col-md-12 col-sm-12">
                    <!-- 데이터 로딩 중 표시 -->
                    <div class="text-center" style="padding: 50px 0; color: #999;">
                      <i class="fa fa-spinner fa-spin" style="font-size: 24px;"></i><br>
                      <small>월별 예약 데이터 로딩 중...</small>
                    </div>
                  </div>

                </div>

                <div class="clearfix"></div>
              </div>
            </div>

          </div>
          <br />

          <div class="row">


            <div class="col-md-4 col-sm-4 ">
              <div id="routeStatsPanel" class="x_panel tile fixed_height_320">
                <div class="x_title">
                  <div class="row">
                    <div class="col-md-6">
                      <h2>인기 항공노선</h2>
                    </div>
                    <div class="col-md-6">
                      <div id="routeStatsYearSelector" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; margin-top: 5px;">
                        <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                        <select id="routeStatsYearSelect" style="border: none; background: transparent; font-size: 12px;">
                          <!-- 년도 옵션들이 JavaScript로 추가됩니다 -->
                        </select>
                      </div>
                    </div>
                  </div>
                  <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    </li>
                    <li class="dropdown">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                          <a class="dropdown-item" href="#">Settings 1</a>
                          <a class="dropdown-item" href="#">Settings 2</a>
                        </div>
                    </li>
                    <li><a class="close-link"><i class="fa fa-close"></i></a>
                    </li>
                  </ul>
                  <div class="clearfix"></div>
                </div>
                <div class="x_content" id="routeStatsContent">
                  <!-- 데이터 로딩 중 표시 -->
                  <div class="text-center" style="padding: 50px 0; color: #999;">
                    <i class="fa fa-spinner fa-spin" style="font-size: 24px;"></i><br>
                    <small>항공노선 데이터 로딩 중...</small>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-4 col-sm-4 ">
              <div class="x_panel tile fixed_height_320 overflow_hidden">
                <div class="x_title">
                  <h2>좌석 클래스별 수익 비율</h2>
                  <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    </li>
                    <li class="dropdown">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                          <a class="dropdown-item" href="#">Settings 1</a>
                          <a class="dropdown-item" href="#">Settings 2</a>
                        </div>
                    </li>
                    <li><a class="close-link"><i class="fa fa-close"></i></a>
                    </li>
                  </ul>
                  <div class="clearfix"></div>
                </div>
                <div class="x_content" id="seat-revenue-chart">
                  <table class="" style="width:100%">
                    <tr>
                      <th style="width:37%;">
                        <p>수익 차트</p>
                      </th>
                      <th>
                        <div class="col-lg-7 col-md-7 col-sm-7 ">
                          <p class="">좌석 클래스</p>
                        </div>
                        <div class="col-lg-5 col-md-5 col-sm-5 ">
                          <p class="">비율</p>
                        </div>
                      </th>
                    </tr>
                    <tr>
                      <td style="text-align: center; vertical-align: middle; width: 37%;">
                        <canvas id="seatRevenueChart" height="130" width="130"></canvas>
                      </td>
                      <td style="vertical-align: middle;">
                        <table class="tile_info" id="seatRevenueTable">
                          <tr data-seat-class="first">
                            <td>
                              <p><i class="fa fa-square blue"></i>퍼스트클래스</p>
                            </td>
                            <td class="revenue-percentage">0%</td>
                          </tr>
                          <tr data-seat-class="business">
                            <td>
                              <p><i class="fa fa-square green"></i>비즈니스클래스</p>
                            </td>
                            <td class="revenue-percentage">0%</td>
                          </tr>
                          <tr data-seat-class="premium">
                            <td>
                              <p><i class="fa fa-square purple"></i>프리미엄이코노미</p>
                            </td>
                            <td class="revenue-percentage">0%</td>
                          </tr>
                          <tr data-seat-class="economy">
                            <td>
                              <p><i class="fa fa-square aero"></i>이코노미클래스</p>
                            </td>
                            <td class="revenue-percentage">0%</td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </div>
              </div>
            </div>


            


          <div class="row">
            <div class="col-md-4 col-sm-4 ">
              <div class="x_panel">
                <div class="x_title">
                  <h2>Recent Activities <small>Sessions</small></h2>
                  <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    </li>
                    <li class="dropdown">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                          <a class="dropdown-item" href="#">Settings 1</a>
                          <a class="dropdown-item" href="#">Settings 2</a>
                        </div>
                    </li>
                    <li><a class="close-link"><i class="fa fa-close"></i></a>
                    </li>
                  </ul>
                  <div class="clearfix"></div>
                </div>
                <div class="x_content">
                  <div class="dashboard-widget-content">

                    <ul class="list-unstyled timeline widget">
                      <li>
                        <div class="block">
                          <div class="block_content">
                            <h2 class="title">
                                              <a>Who Needs Sundance When You've Got&nbsp;Crowdfunding?</a>
                                          </h2>
                            <div class="byline">
                              <span>13 hours ago</span> by <a>Jane Smith</a>
                            </div>
                            <p class="excerpt">Film festivals used to be do-or-die moments for movie makers. They were where you met the producers that could fund your project, and if the buyers liked your flick, they'd pay to Fast-forward and… <a>Read&nbsp;More</a>
                            </p>
                          </div>
                        </div>
                      </li>
                      <li>
                        <div class="block">
                          <div class="block_content">
                            <h2 class="title">
                                              <a>Who Needs Sundance When You've Got&nbsp;Crowdfunding?</a>
                                          </h2>
                            <div class="byline">
                              <span>13 hours ago</span> by <a>Jane Smith</a>
                            </div>
                            <p class="excerpt">Film festivals used to be do-or-die moments for movie makers. They were where you met the producers that could fund your project, and if the buyers liked your flick, they'd pay to Fast-forward and… <a>Read&nbsp;More</a>
                            </p>
                          </div>
                        </div>
                      </li>
                      <li>
                        <div class="block">
                          <div class="block_content">
                            <h2 class="title">
                                              <a>Who Needs Sundance When You've Got&nbsp;Crowdfunding?</a>
                                          </h2>
                            <div class="byline">
                              <span>13 hours ago</span> by <a>Jane Smith</a>
                            </div>
                            <p class="excerpt">Film festivals used to be do-or-die moments for movie makers. They were where you met the producers that could fund your project, and if the buyers liked your flick, they'd pay to Fast-forward and… <a>Read&nbsp;More</a>
                            </p>
                          </div>
                        </div>
                      </li>
                      <li>
                        <div class="block">
                          <div class="block_content">
                            <h2 class="title">
                                              <a>Who Needs Sundance When You've Got&nbsp;Crowdfunding?</a>
                                          </h2>
                            <div class="byline">
                              <span>13 hours ago</span> by <a>Jane Smith</a>
                            </div>
                            <p class="excerpt">Film festivals used to be do-or-die moments for movie makers. They were where you met the producers that could fund your project, and if the buyers liked your flick, they'd pay to Fast-forward and… <a>Read&nbsp;More</a>
                            </p>
                          </div>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>


            <div class="col-md-8 col-sm-8 ">



              <div class="row">

                <div class="col-md-12 col-sm-12 ">
                  <div class="x_panel">
                    <div class="x_title">
                      <h2>Visitors location <small>geo-presentation</small></h2>
                      <ul class="nav navbar-right panel_toolbox">
                        <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                        </li>
                        <li class="dropdown">
                          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                              <a class="dropdown-item" href="#">Settings 1</a>
                              <a class="dropdown-item" href="#">Settings 2</a>
                            </div>
                        </li>
                        <li><a class="close-link"><i class="fa fa-close"></i></a>
                        </li>
                      </ul>
                      <div class="clearfix"></div>
                    </div>
                    <div class="x_content">
                      <div class="dashboard-widget-content">
                        <div class="col-md-4 hidden-small">
                          <h2 class="line_30">125.7k Views from 60 countries</h2>

                          <table class="countries_list">
                            <tbody>
                              <tr>
                                <td>United States</td>
                                <td class="fs15 fw700 text-right">33%</td>
                              </tr>
                              <tr>
                                <td>France</td>
                                <td class="fs15 fw700 text-right">27%</td>
                              </tr>
                              <tr>
                                <td>Germany</td>
                                <td class="fs15 fw700 text-right">16%</td>
                              </tr>
                              <tr>
                                <td>Spain</td>
                                <td class="fs15 fw700 text-right">11%</td>
                              </tr>
                              <tr>
                                <td>Britain</td>
                                <td class="fs15 fw700 text-right">10%</td>
                              </tr>
                            </tbody>
                          </table>
                        </div>
                        <div id="world-map-gdp" class="col-md-8 col-sm-12 " style="height:230px;"></div>
                      </div>
                    </div>
                  </div>
                </div>

              </div>
              <div class="row">


                <!-- Start to do list -->
                <div class="col-md-6 col-sm-6 ">
                  <div class="x_panel">
                    <div class="x_title">
                      <h2>To Do List <small>Sample tasks</small></h2>
                      <ul class="nav navbar-right panel_toolbox">
                        <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                        </li>
                        <li class="dropdown">
                          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                              <a class="dropdown-item" href="#">Settings 1</a>
                              <a class="dropdown-item" href="#">Settings 2</a>
                            </div>
                        </li>
                        <li><a class="close-link"><i class="fa fa-close"></i></a>
                        </li>
                      </ul>
                      <div class="clearfix"></div>
                    </div>
                    <div class="x_content">

                      <div class="">
                        <ul class="to_do">
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Schedule meeting with new client </p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Create email address for new intern</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Have IT fix the network printer</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Copy backups to offsite location</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Food truck fixie locavors mcsweeney</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Food truck fixie locavors mcsweeney</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Create email address for new intern</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Have IT fix the network printer</p>
                          </li>
                          <li>
                            <p>
                              <input type="checkbox" class="flat"> Copy backups to offsite location</p>
                          </li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- End to do list -->
                
                <!-- start of weather widget -->
                <div class="col-md-6 col-sm-6 ">
                  <div class="x_panel">
                    <div class="x_title">
                      <h2>Daily active users <small>Sessions</small></h2>
                      <ul class="nav navbar-right panel_toolbox">
                        <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                        </li>
                        <li class="dropdown">
                          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                              <a class="dropdown-item" href="#">Settings 1</a>
                              <a class="dropdown-item" href="#">Settings 2</a>
                            </div>
                        </li>
                        <li><a class="close-link"><i class="fa fa-close"></i></a>
                        </li>
                      </ul>
                      <div class="clearfix"></div>
                    </div>
                    <div class="x_content">
                      <div class="row">
                        <div class="col-sm-12">
                          <div class="temperature"><b>Monday</b>, 07:30 AM
                            <span>F</span>
                            <span><b>C</b></span>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-sm-4">
                          <div class="weather-icon">
                            <canvas height="84" width="84" id="partly-cloudy-day"></canvas>
                          </div>
                        </div>
                        <div class="col-sm-8">
                          <div class="weather-text">
                            <h2>Texas <br><i>Partly Cloudy Day</i></h2>
                          </div>
                        </div>
                      </div>
                      <div class="col-sm-12">
                        <div class="weather-text pull-right">
                          <h3 class="degrees">23</h3>
                        </div>
                      </div>

                      <div class="clearfix"></div>

                      <div class="row weather-days">
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Mon</h2>
                            <h3 class="degrees">25</h3>
                            <canvas id="clear-day" width="32" height="32"></canvas>
                            <h5>15 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Tue</h2>
                            <h3 class="degrees">25</h3>
                            <canvas height="32" width="32" id="rain"></canvas>
                            <h5>12 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Wed</h2>
                            <h3 class="degrees">27</h3>
                            <canvas height="32" width="32" id="snow"></canvas>
                            <h5>14 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Thu</h2>
                            <h3 class="degrees">28</h3>
                            <canvas height="32" width="32" id="sleet"></canvas>
                            <h5>15 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Fri</h2>
                            <h3 class="degrees">28</h3>
                            <canvas height="32" width="32" id="wind"></canvas>
                            <h5>11 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="col-sm-2">
                          <div class="daily-weather">
                            <h2 class="day">Sat</h2>
                            <h3 class="degrees">26</h3>
                            <canvas height="32" width="32" id="cloudy"></canvas>
                            <h5>10 <i>km/h</i></h5>
                          </div>
                        </div>
                        <div class="clearfix"></div>
                      </div>
                    </div>
                  </div>

                </div>
                <!-- end of weather widget -->
              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
          <div class="pull-right">
            Gentelella - Bootstrap Admin Template by <a href="https://colorlib.com">Colorlib</a>
          </div>
          <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
      </div>
    </div>

    <!-- jQuery -->
    <script src="<%=contextPath%>/views/vendors/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap -->
    <script src="<%=contextPath%>/views/vendors/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <!-- FastClick -->
    <script src="<%=contextPath%>/views/vendors/fastclick/lib/fastclick.js"></script>
    <!-- NProgress -->
    <script src="<%=contextPath%>/views/vendors/nprogress/nprogress.js"></script>
    <!-- Chart.js -->
    <script src="<%=contextPath%>/views/vendors/Chart.js/dist/Chart.min.js"></script>
    <!-- gauge.js -->
    <script src="<%=contextPath%>/views/vendors/gauge.js/dist/gauge.min.js"></script>
    <!-- bootstrap-progressbar -->
    <script src="<%=contextPath%>/views/vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
    <!-- iCheck -->
    <script src="<%=contextPath%>/views/vendors/iCheck/icheck.min.js"></script>
    <!-- Skycons -->
    <script src="<%=contextPath%>/views/vendors/skycons/skycons.js"></script>
    <!-- Flot -->
    <script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.js"></script>
    <script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.pie.js"></script>
    <script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.time.js"></script>
    <script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.stack.js"></script>
    <script src="<%=contextPath%>/views/vendors/Flot/jquery.flot.resize.js"></script>
    <!-- Flot plugins -->
    <script src="<%=contextPath%>/views/vendors/flot.orderbars/js/jquery.flot.orderBars.js"></script>
    <script src="<%=contextPath%>/views/vendors/flot-spline/js/jquery.flot.spline.min.js"></script>
    <script src="<%=contextPath%>/views/vendors/flot.curvedlines/curvedLines.js"></script>
    <!-- DateJS -->
    <script src="<%=contextPath%>/views/vendors/DateJS/build/date.js"></script>
    <!-- JQVMap -->
    <script src="<%=contextPath%>/views/vendors/jqvmap/dist/jquery.vmap.js"></script>
    <script src="<%=contextPath%>/views/vendors/jqvmap/dist/maps/jquery.vmap.world.js"></script>
    <script src="<%=contextPath%>/views/vendors/jqvmap/examples/js/jquery.vmap.sampledata.js"></script>
    <!-- bootstrap-daterangepicker -->
    <script src="<%=contextPath%>/views/vendors/moment/min/moment.min.js"></script>
    <script src="<%=contextPath%>/views/vendors/bootstrap-daterangepicker/daterangepicker.js"></script>

    <!-- Custom Theme Scripts -->
    <script src="<%=contextPath%>/views/build/js/custom.min.js"></script>

    <script>
        // contextPath 전역 변수 설정 (statistics.js 로드 전에 설정)
        const contextPath = '<%=contextPath%>';
        console.log('Index.jsp 로드 완료, contextPath:', contextPath);
    </script>
    
    <!-- Statistics JavaScript (캐시 방지용 버전 파라미터 추가) -->
    <script src="<%=contextPath%>/views/adminpage/js/statistics.js?v=20241219-v3"></script>
    
    <!-- 추가 디버깅을 위한 스크립트 -->
    <script>
        // 페이지 로드 완료 후 추가 디버깅
        window.addEventListener('load', function() {
            console.log('=== 페이지 완전 로드 완료 ===');
            console.log('Chart.js 사용 가능:', typeof Chart !== 'undefined');
            console.log('contextPath 설정:', contextPath);
            console.log('seatRevenueChart 캔버스:', document.getElementById('seatRevenueChart') ? '존재함' : '존재하지 않음');
            
            // statistics.js가 로드될 때까지 대기 후 함수 호출
            setTimeout(function() {
                if (typeof loadSeatRevenueData === 'function') {
                    console.log('loadSeatRevenueData 함수 사용 가능');
                } else {
                    console.error('loadSeatRevenueData 함수를 찾을 수 없습니다');
                }
            }, 100);
        });
    </script>
	
  </body>
</html>
