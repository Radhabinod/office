package com.techindustan.model.reset;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class ResetResponse{
  @SerializedName("success")
  @Expose
  private Boolean success;
  @SerializedName("message")
  @Expose
  private String message;
  public void setSuccess(Boolean success){
   this.success=success;
  }
  public Boolean getSuccess(){
   return success;
  }
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
}