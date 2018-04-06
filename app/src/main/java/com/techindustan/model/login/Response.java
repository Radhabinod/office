package com.techindustan.model.login;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Response{
  @SerializedName("access_token")
  @Expose
  private String access_token;
  @SerializedName("skype")
  @Expose
  private String skype;
  @SerializedName("gender")
  @Expose
  private String gender;
  @SerializedName("user_image")
  @Expose
  private String user_image;
  @SerializedName("phone")
  @Expose
  private String phone;
  @SerializedName("last_name")
  @Expose
  private String last_name;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("first_name")
  @Expose
  private String first_name;
  @SerializedName("job_title")
  @Expose
  private String job_title;
  public void setAccess_token(String access_token){
   this.access_token=access_token;
  }
  public String getAccess_token(){
   return access_token;
  }
  public void setSkype(String skype){
   this.skype=skype;
  }
  public String getSkype(){
   return skype;
  }
  public void setGender(String gender){
   this.gender=gender;
  }
  public String getGender(){
   return gender;
  }
  public void setUser_image(String user_image){
   this.user_image=user_image;
  }
  public String getUser_image(){
   return user_image;
  }
  public void setPhone(String phone){
   this.phone=phone;
  }
  public String getPhone(){
   return phone;
  }
  public void setLast_name(String last_name){
   this.last_name=last_name;
  }
  public String getLast_name(){
   return last_name;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setFirst_name(String first_name){
   this.first_name=first_name;
  }
  public String getFirst_name(){
   return first_name;
  }
  public void setJob_title(String job_title){
   this.job_title=job_title;
  }
  public String getJob_title(){
   return job_title;
  }
}