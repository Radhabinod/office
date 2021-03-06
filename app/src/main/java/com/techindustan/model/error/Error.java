package com.techindustan.model.error;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Error{
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}