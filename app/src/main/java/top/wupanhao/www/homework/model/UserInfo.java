/**
  * Copyright 2018 bejson.com 
  */
package top.wupanhao.www.homework.model;
import java.util.List;

/**
 * Auto-generated: 2018-07-24 9:34:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserInfo {

    private List<String> likeUserList;
    private int type;
    private int userCount;
    public void setLikeUserList(List<String> likeUserList) {
         this.likeUserList = likeUserList;
     }
     public List<String> getLikeUserList() {
         return likeUserList;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setUserCount(int userCount) {
         this.userCount = userCount;
     }
     public int getUserCount() {
         return userCount;
     }

}