<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*, java.io.*, javax.xml.parsers.*, org.w3c.dom.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>항공편 전체 정보</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 5px;
            font-size: 12px;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h2>항공편 전체 정보 (김포공항 도착 기준)</h2>

<%! 
    // Null-safe 태그 텍스트 추출 함수
    public String getTagText(Element e, String tag) {
        NodeList list = e.getElementsByTagName(tag);
        if (list != null && list.getLength() > 0 && list.item(0) != null) {
            return list.item(0).getTextContent();
        } else {
            return "없음";
        }
    }
%>

<%
    // 필수: 실제 서비스 키로 교체하세요
    String serviceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D";

    StringBuilder urlBuilder = new StringBuilder("http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList");
    urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
    urlBuilder.append("&" + URLEncoder.encode("schLineType", "UTF-8") + "=" + URLEncoder.encode("D", "UTF-8")); // 국내선
    urlBuilder.append("&" + URLEncoder.encode("schIOType", "UTF-8") + "=" + URLEncoder.encode("I", "UTF-8")); // 도착
    urlBuilder.append("&" + URLEncoder.encode("schAirCode", "UTF-8") + "=" + URLEncoder.encode("GMP", "UTF-8")); // 김포공항
    urlBuilder.append("&" + URLEncoder.encode("schStTime", "UTF-8") + "=" + URLEncoder.encode("2100", "UTF-8")); // 조회 시작 시간
    urlBuilder.append("&" + URLEncoder.encode("schEdTime", "UTF-8") + "=" + URLEncoder.encode("2200", "UTF-8")); // 조회 종료 시간

    URL url = new URL(urlBuilder.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    InputStream is;
    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
        is = conn.getInputStream();
    } else {
        is = conn.getErrorStream();
    }

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(is);
    doc.getDocumentElement().normalize();

    NodeList list = doc.getElementsByTagName("item");
%>

<table>
    <tr>
        <th>항공사(국문)</th>
        <th>항공사(영문)</th>
        <th>항공편명</th>
        <th>기준공항코드</th>
        <th>출발공항(국문)</th>
        <th>출발공항(영문)</th>
        <th>도착공항(국문)</th>
        <th>도착공항(영문)</th>
        <th>변경시간</th>
        <th>예정시간</th>
        <th>항공편상태(국문)</th>
        <th>항공편상태(영문)</th>
        <th>게이트번호</th>
        <th>출/도착</th>
        <th>국내/국제</th>
        <th>운항코드</th>
    </tr>

<%
    for (int i = 0; i < list.getLength(); i++) {
        Node node = list.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
%>
    <tr>
        <td><%= getTagText(e, "airlineKorean") %></td>
        <td><%= getTagText(e, "airlineEnglish") %></td>
        <td><%= getTagText(e, "airFln") %></td>
        <td><%= getTagText(e, "airport") %></td>
        <td><%= getTagText(e, "boardingKor") %></td>
        <td><%= getTagText(e, "boardingEng") %></td>
        <td><%= getTagText(e, "arrivedKor") %></td>
        <td><%= getTagText(e, "arrivedEng") %></td>
        <td><%= getTagText(e, "etd") %></td>
        <td><%= getTagText(e, "std") %></td>
        <td><%= getTagText(e, "rmkKor") %></td>
        <td><%= getTagText(e, "rmkEng") %></td>
        <td><%= getTagText(e, "gate") %></td>
        <td><%= getTagText(e, "io") %></td>
        <td><%= getTagText(e, "line") %></td>
        <td><%= getTagText(e, "city") %></td>
    </tr>
<%
        }
    }
    is.close();
    conn.disconnect();
%>
</table>

</body>
</html>
