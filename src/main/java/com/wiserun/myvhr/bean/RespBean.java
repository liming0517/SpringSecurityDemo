package com.wiserun.myvhr.bean;

public class RespBean {
    private Integer status;
    private String msg;
    private Object obj;
    //构造方法1
    private RespBean(){

    }
    //构造方法2
    private RespBean(int status,String msg,Object obj){
        this.status=status;
        this.msg=msg;
        this.obj=obj;
    }
    //静态方法 返回错误信息
    public static RespBean ERROR(String msg){
        return new RespBean(500,msg,null);
    }
    //返回成功信息
    public static RespBean ok(String msg){
        return new RespBean(200,msg,null);
    }
    //返回成功信息重载
    public static RespBean ok(String msg,Object obj){
        return new RespBean(200,msg,obj);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
