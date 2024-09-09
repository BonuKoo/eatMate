package com.eatmate.team.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamForm {
   private Long teamId;
   
   private String userName; //신원확인
   private String roomId;
   private String userNickname;
   private String teamName;

   @Builder
   public TeamForm(Long teamId, String userName,String roomId) {
      this.teamId = teamId;
      this.userName = userName;
      this.roomId = roomId;
   }

   public void attachRoomIdAndNickname(String roomId,
                                       String userNickname,
                                       String teamName
   ) {
      this.roomId = roomId;
      this.userNickname = userNickname;
      this.teamName = teamName;
   }


}
