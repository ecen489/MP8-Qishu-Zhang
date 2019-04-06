package com.example.mp8_qishu_zhang;

public class ListItem {

    private String head;
    private String des1;
    private String des2;
    private String des3;

    public ListItem(String head, String des1, String des2, String des3){
        this.head = head;
        this.des1 = des1;
        this.des2 = des2;
        this.des3 = des3;
    }

    public String getHead(){
        return head;
    }

    public String getDes1(){
        return des1;
    }

    public String getDes2(){
        return des2;
    }

    public String getDes3(){
        return des3;
    }
}
