package com.techindustan.model.notification;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Notification{
  @SerializedName("response")
  @Expose
  private String response;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setResponse(String response){
   this.response=response;
  }
  public String getResponse(){
   return response;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}