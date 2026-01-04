package com.ync.intranet.dto;

import java.util.List;
import java.util.Map;

public class ExcelDownloadRequest {
    private String member;        // 사용자 이름
    private String position;      // 직급
    private String dept;          // 부서명
    private String yearMonth;     // YYYY-MM 형식
    private List<Map<String, Object>> items;  // 지출 내역 리스트

    public ExcelDownloadRequest() {
    }

    public ExcelDownloadRequest(String member, String position, String dept, String yearMonth, List<Map<String, Object>> items) {
        this.member = member;
        this.position = position;
        this.dept = dept;
        this.yearMonth = yearMonth;
        this.items = items;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ExcelDownloadRequest{" +
                "member='" + member + '\'' +
                ", position='" + position + '\'' +
                ", dept='" + dept + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                ", items=" + (items != null ? items.size() + " items" : "null") +
                '}';
    }
}
