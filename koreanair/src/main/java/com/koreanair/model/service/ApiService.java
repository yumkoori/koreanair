package com.koreanair.model.service;

import org.w3c.dom.*;
import com.koreanair.model.dto.AirportDTO;

import javax.xml.parsers.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    public List<AirportDTO> fetchAirportList() {
        List<AirportDTO> list = new ArrayList<>();
        try {
            String serviceKey = "UNnuFixx2cWRxnujKddwl8pYBr1uw946cRcT6JayP4%2B5uvZqT0FnuZFWETlNz8N7%2BeSga0fya9NJzMv%2BUVm7wg%3D%3D";
            int pageNo = 1;
            int numOfRows = 100; // 한 번에 가져올 행 수 (최대값으로 설정)
            int totalCount = 0;

            do {
                String urlStr = String.format(
                        "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList?serviceKey=%s&pageNo=%d&numOfRows=%d",
                        serviceKey, pageNo, numOfRows
                );

                URL url = new URL(urlStr);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(url.openStream());
                doc.getDocumentElement().normalize();

                // 총 개수 추출
                if (pageNo == 1) {
                    NodeList totalCountList = doc.getElementsByTagName("totalCount");
                    if (totalCountList.getLength() > 0) {
                        totalCount = Integer.parseInt(totalCountList.item(0).getTextContent());
                    }
                }

                // item 파싱
                NodeList nodeList = doc.getElementsByTagName("item");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) node;

                        AirportDTO dto = AirportDTO.builder()
                                .airportId(getTagValue("cityCode", elem))
                                .airportName(getTagValue("cityKor", elem))
                                .airportEngName(getTagValue("cityEng", elem))
                                .cityKor(getTagValue("cityKor", elem))
                                .cityEng(getTagValue("cityEng", elem))
                                .build();

                        list.add(dto);
                    }
                }

                pageNo++; // 다음 페이지로

            } while ((pageNo - 1) * numOfRows < totalCount);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nList = element.getElementsByTagName(tag);
        if (nList.getLength() == 0) return "";
        NodeList children = nList.item(0).getChildNodes();
        if (children.getLength() == 0) return "";
        return children.item(0).getNodeValue();
    }
}
