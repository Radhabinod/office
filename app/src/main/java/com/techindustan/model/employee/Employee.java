package com.techindustan.model.employee;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Employee{
  @SerializedName("response")
  @Expose
  private List<Response> response;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setResponse(List<Response> response){
   this.response=response;
  }
  public List<Response> getResponse(){
   return response;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}