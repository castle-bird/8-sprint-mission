package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication1 {

    public static void main(String[] args) {
        // 유저 CRUD 테스트를 위함
        
        UserService userService = new JCFUserService();

        User user1 = new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동1",
                "골목대장1",
                "홍길동1@gmail.com"
        );

        User user2 = new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동2",
                "골목대장2",
                "홍길동2@gmail.com"
        );

        System.out.println("=============등록==============");
        User user1Insert = userService.insertUser(user1);
        User user2Insert = userService.insertUser(user2);
        System.out.println(user1Insert);
        System.out.println(user2Insert);

        System.out.println("\n=============조회[단건]==============");
        User user1Select = userService.selectUser(user1Insert.getId());
        User user2Select = userService.selectUser(user2Insert.getId());
        System.out.println(user1Select);
        System.out.println(user2Select);

        System.out.println("\n=============조회[다건]==============");
        List<User> users = userService.selectUsers(List.of(user1Insert.getId(), user2Insert.getId()));
        System.out.println(users);

        System.out.println("\n=============수정==============");
        user1.setName("홍길동1 수정");
        user1.setNickname("골목대장1 수정");
        user1.setUpdatedAt(System.currentTimeMillis());
        User user1Update = userService.updateUser(user1);
        System.out.println(user1Update);

        System.out.println("\n=============삭제==============");
        User user1Delete = userService.deleteUser(user1Insert.getId());
        System.out.println(user1Delete);

        System.out.println("\n=============삭제가 되었는지 전체 확인==============");
        List<User> usersLast = userService.selectUsers(List.of(user1Insert.getId(), user2Insert.getId()));
        System.out.println(usersLast);
    }
}
