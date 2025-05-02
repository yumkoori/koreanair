<%@ page contentType="application/json; charset=UTF-8" language="java" %>
<%@ page import="java.net.*, java.io.*, org.w3c.dom.*, javax.xml.parsers.*, java.util.*" %>
<%!
public String getTagValue(String tag, Element e) {
    NodeList nlList = e.getElementsByTagName(tag);
    if (nlList.getLength() == 0 || nlList.item(0) == null || nlList.item(0).getTextContent() == null) return "";
    return nlList.item(0).getTextContent();
}
%>
[
<%
    String serviceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D"; // 이미 인코딩된 경우 그대로 사용
    int pageNo = 1;
    int numOfRows = 100;
    int totalCount = 0;
    boolean first = true;
    boolean firstItem = true;

    do {
        String urlStr = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList"
            + "?ServiceKey=" + serviceKey
            + "&pageNo=" + pageNo
            + "&numOfRows=" + numOfRows;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            if (first) {
                totalCount = Integer.parseInt(doc.getElementsByTagName("totalCount").item(0).getTextContent());
                first = false;
            }

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element e = (Element) itemList.item(i);
                String cityKor = getTagValue("cityKor", e);
                if (cityKor != null && !cityKor.equals("")) {
                    if (!firstItem) out.print(",");
                    out.print("\"" + cityKor + "\"");
                    firstItem = false;
                }
            }

            is.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        pageNo++;
    } while ((pageNo - 1) * numOfRows < totalCount);
%>
]
